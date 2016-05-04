package iRobotCreateService;
import java.util.*;
import java.nio.*;
import java.io.*;

import com.robotraconteur.*;

import gnu.io.*;
import experimental.create.*;

//The implementation of the create object.  It implementes Create_interface.Create.  
//This allows the object to be exposed using the Create_interface
//service definition.

public class Create_impl implements Create  {

	private int m_DistanceTraveled=0;
	private int m_AngleTraveled=0;
	private UnsignedByte m_Bumpers=new UnsignedByte((byte) 0);
	
	private SerialPort serialPort;
	private InputStream serialPortInput;
	private OutputStream serialPortOutput;
	
	private boolean streaming=false;
	
	//Initialize the serial port and set the data received callback
	public void start(String portName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException
	{
		Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		
		CommPortIdentifier portId = null;  // will be set if port found
		while (portIdentifiers.hasMoreElements())
		{
		    CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();
		    if(pid.getPortType() == CommPortIdentifier.PORT_SERIAL &&
		       pid.getName().equals(portName)) 
		    {
		        portId = pid;
		        break;
		    }
		}
		
		if(portId == null)
		{
		    throw new NoSuchPortException();
		}
		
		serialPort=null;
		serialPort=(SerialPort)portId.open("iRobotCreateService",10000);
		
		serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		serialPort.enableReceiveThreshold(1);
		serialPort.disableReceiveTimeout();
		serialPortInput=serialPort.getInputStream();
		serialPortOutput=serialPort.getOutputStream();
		
		byte[] command={-128, -124};
		serialPortOutput.write(command);
		
		try
		{
			
			Thread.sleep(500);
		}
		catch (Exception e) {}
		
	}
	
	//Shutdown the robot
	public synchronized void shutdown()
	{
		try
		{
			streaming=false;
			byte[] command={-128,};
			serialPortOutput.write(command);
			
			try
			{
				
				Thread.sleep(500);
			}
			catch (Exception e) {}
			serialPort.close();
		}
		catch (Exception e)
		{
			throw new RobotRaconteurRemoteException(e.getClass().toString(),e.toString());
		}
		
		
	}
	
	
	//Read property for DistanceTraveled
	@Override
	public synchronized int get_DistanceTraveled() {
		return m_DistanceTraveled;
	}

	@Override
	public synchronized void set_DistanceTraveled(int value) {
		throw new RuntimeException("Read only property");
		
	}

	//Read property for AngleTraveled
	@Override
	public synchronized int get_AngleTraveled() {
		return m_AngleTraveled;
	}

	@Override
	public synchronized void set_AngleTraveled(int value) {
		throw new RuntimeException("Read only property");
		
	}

	//Read property for Bumpers
	@Override
	public synchronized UnsignedByte get_Bumpers() {
		return m_Bumpers;
	}

	@Override
	public synchronized void set_Bumpers(UnsignedByte value) {
		throw new RuntimeException("Read only property");
		
	}

	//Drive the robot with given velocity and radius
	@Override
	public synchronized void Drive(short velocity, short radius){
		try
		{
			ByteBuffer b=ByteBuffer.allocate(5);
			b.put((byte) 137);
			b.putShort(velocity);
			b.putShort(radius);
			byte[] command=b.array();
			
			serialPortOutput.write(command);
		}
		catch (Exception e)
		{
			throw new RobotRaconteurRemoteException(e.getClass().toString(),e.toString());
		}
		
		
	}

	long current_client=0;
	Thread t;

	//Start streaming data from the robot
	@Override
	public synchronized void StartStreaming() {
		if (streaming) throw new RuntimeException("Already streaming");
		try
		{
			
			//Flush the input
			while (serialPortInput.available()>0)
			{
			
			serialPortInput.skip(serialPortInput.available());
			try
			{
			Thread.sleep(100);
			}
			catch (Exception e) {}
			}
			//Send command to start streaming
			
			byte[] command={-108, 4, 7 ,19 ,20 ,18};
			serialPortOutput.write(command);
			streaming=true;
			
			//Save the current client endpoint.  This uniquely identifies
			//the client.
			current_client=ServerEndpoint.getCurrentEndpoint();
			
			//Start thread to receive serial data
			t=new Thread(new ReceiveSensorPackets());
			t.start();
		}
		catch (Exception e)
		{
			throw new RobotRaconteurRemoteException(e.getClass().toString(),e.toString());
		}
		
		
	}

	//Stop streaming data from the robot
	@Override
	public synchronized void StopStreaming() {
		if (!streaming) throw new RuntimeException("Not streaming");
		try
		{
			
			streaming=false;
			byte[] command={-106, 0};
			serialPortOutput.write(command);
			
			
			t.join();
		}
		catch (Exception e)
		{
			throw new RobotRaconteurRemoteException(e.getClass().toString(),e.toString());
		}
	}

	
	Vector<Action> bumpListeners=new Vector<Action>();
	
	//Add a listener for the Bumper event
	@Override
	public void addBumpListener(Action listener) {
		synchronized(bumpListeners)
		{
			bumpListeners.add(listener);
		
		}
	}

	//Remove a listener for the Bump event
	@Override
	public void removeBumpListener(Action listener) {
		synchronized(bumpListeners)
		{
			bumpListeners.remove(listener);
		
		}
		
	}
	
	//Fire the Bumper event
	protected void fire_Bump()
	{ 
		
		synchronized(bumpListeners)
		{
			for (Action a : bumpListeners)
			{
				a.action();
			}
		
		}
	}

	Callback<Func2<Integer, Integer, UnsignedBytes>> m_play_callback;
	
	//get and set for play_callback server
	@Override
	public Callback<Func2<Integer, Integer, UnsignedBytes>> get_play_callback() {
		return m_play_callback;
	}
	@Override
	public synchronized void set_play_callback(
			Callback<Func2<Integer, Integer, UnsignedBytes>> value) {
		m_play_callback=value;
	}

	
	Wire<SensorPacket> m_packets;
	
	//get and set for packets Wire 
	@Override
	public Wire<SensorPacket> get_packets() {
		
		return m_packets;
	}
	@Override
	public synchronized void set_packets(Wire<SensorPacket> value) {
		m_packets=value;
		//Set the callback for Wire connect
		value.setWireConnectCallback(new packets_connect());
		
	}
	
	HashMap<Long,Wire<SensorPacket>.WireConnection> wireconnections=new HashMap<Long,Wire<SensorPacket>.WireConnection>();
	
	//Callback for Wire connect
	class packets_connect implements Action2<Wire<SensorPacket>,Wire<SensorPacket>.WireConnection>
	{

		@Override
		public void action(Wire<SensorPacket> arg0, Wire<SensorPacket>.WireConnection arg1) {
			synchronized(wireconnections)
			{
				//Store WireConnection
				wireconnections.put( arg1.getEndpoint(), arg1);
				//Set the callback for wire closed
				arg1.setWireCloseCallback(new packets_closed());
			}
			
		}
		
	}
	
	//Callback for Wire closed
	class packets_closed implements Action1<Wire<SensorPacket>.WireConnection>
	{

		@Override
		public void action(Wire<SensorPacket>.WireConnection arg0) {
			synchronized(wireconnections)
			{
				//Delete the WireConnection
				wireconnections.remove(arg0.getEndpoint());
			}
		}
		
	
	}
	
	
	boolean lastbump=false;
	boolean lastplay=false;
	
	//Thread function to read data from the robot
	class ReceiveSensorPackets implements Runnable
	{
		public void run()
		{
			try
			{
				while(streaming)
				{
					byte seed=(byte)serialPortInput.read();
					if (seed!=19)
					{
						if (seed==-1)
						{
							try
							{
							Thread.sleep(100);
							continue;
							}
							catch (Exception e) {}
							
						}
						return;
					}
					
					byte nbytes=(byte)serialPortInput.read();
					if (nbytes==0)
					{
						
						return;
					}
					
										
					byte[] packets=new byte[nbytes+1];
					
					int bytesread=0;
					while (bytesread<packets.length)
					{
					bytesread+=serialPortInput.read(packets,bytesread,packets.length-bytesread);
					}
					
					int readpos=0;
					
					SendSensorPacket(new UnsignedByte(seed),new UnsignedBytes(packets));
					
					while (readpos < nbytes)
					{
						byte id=packets[readpos++];
						
						switch(id)
						{
						case 7:
							{
								byte flags=packets[readpos++];
								if (((flags & 0x1) != 0) || ((flags & 0x2) != 0))
	                            {
	                                if (lastbump == false)
	                                {
	                                    fire_Bump();
	                                }
	                                lastbump = true;
	                            }
	                            else
	                            {
	                                lastbump = false;
	                            }
	                            m_Bumpers = new UnsignedByte(flags);
							}
							break;
							
						case 19:
							{
								ByteBuffer b=ByteBuffer.allocate(2);
								b.put(packets[readpos++]);
								b.put(packets[readpos++]);
								b.rewind();
								
								m_DistanceTraveled+=b.getShort();
								
								
							}
							break;
						case 20:
							{
								ByteBuffer b=ByteBuffer.allocate(2);
								b.put(packets[readpos++]);
								b.put(packets[readpos++]);
								b.rewind();
								
								m_AngleTraveled+=b.getShort();
								
								
							}
							break;
						case 18:
							{
								 byte buttons=(byte)packets[readpos++];
                                 byte bplay=(byte)(buttons & ((byte)0x1));
                                 if (bplay==1)
                                 {
                                     if (!lastplay)
                                     {
                                         play();
                                     }
                                     lastplay=true;
                                 }
                                 else
                                 {
                                     lastplay=false;
                                 }
                             }
                             break;
                         default:

                             readpos++;
                             break;
							
						
						}
						
						
					}
					
					
					
				}
			}
			catch (Exception e)
			{
				if (streaming)
				{
					e.printStackTrace(System.out);
				}
			}
			
			
		}
		
		
	}
	
	//Send the sensor packet to all connected WireConnections
	private void SendSensorPacket(UnsignedByte id, UnsignedBytes packet )
	{
		SensorPacket p=new SensorPacket();
		p.ID =id;
		p.Data=packet;
		
		synchronized(wireconnections)
		{
			Set<Long> ep1=wireconnections.keySet();
			long[] ep=new long[ep1.size()];
			int i=0;
			for (long e : ep1)
			{
				ep[i]=e;
				i++;
			}
			
			//Loop through all connected WireConnections
			for (long e : ep)
			{
				Wire<SensorPacket>.WireConnection wend=wireconnections.get(e);
				
				try
				{
					//Set the OutValue to the current packet
					wend.setOutValue(p);
				}
				catch (Exception ex)
				{
					//If there is an error, assume the wire is closed
					//and remove it
					try
					{
						
						wend.close();
					}
					catch (Exception ex2) {}
					
					try
					{
						wireconnections.remove(e);
					}
					catch (Exception ex2) {}
				}
				
			
			}
			
		}
	
	}
	
	
	//Play a song.  This calls the client through the play_callback
	//to retrieve the notes
	private void play()
	{
		//If there is no current client, return
		if (current_client==0) return;
		
		//Call the play_callback on the client and receive notes
		UnsignedBytes notes=get_play_callback().getClientFunction(current_client).func(m_DistanceTraveled, m_AngleTraveled);
		
		//Generate the command and send to the robot to play the song
		byte[] command=new byte[notes.value.length + 5];
		command[0]=-116;
		command[1]=0;
		command[2]=(byte)(notes.value.length/2);
		System.arraycopy(notes.value, 0, command, 3, notes.value.length);
		command[3+notes.value.length]=-115;
		command[4+notes.value.length]=0;
		
		synchronized(this)
		{
			try
			{
			serialPortOutput.write(command);
			}
			catch (Exception e)
			{
				e.printStackTrace(System.out);
				
			}
		}
		
	}
	

}
