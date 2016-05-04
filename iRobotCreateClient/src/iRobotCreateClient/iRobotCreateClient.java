package iRobotCreateClient;

import com.robotraconteur.*;

import experimental.create.*;

//This program provides a simple client to the iRobotCreate service
//that connects, drives a bit, and then disconnects

public class iRobotCreateClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
						
			//Create and register a TcpTransport
			TcpTransport t=new TcpTransport();
			RobotRaconteurNode.s().registerTransport(t);
			
			//Register the Create_interface service type
			RobotRaconteurNode.s().registerServiceType(new experimental__createFactory());
			
			//Connect to the service
			Create c=(Create)RobotRaconteurNode.s().connectService("rr+tcp://localhost:2354?service=Create",null,null,null,"experimental.create.Create");
			
			//Set an event listener for the "Bump" event
			c.addBumpListener(new Bump());
			
			//Connect the "packets" wire and add a value changed event listener
			Wire<SensorPacket>.WireConnection wire=c.get_packets().connect();
			wire.addWireValueListener(new wire_changed());
			
			//Set a function to be used by the callback.  This function will be called
            //when the service calls a callback with the endpoint corresponding to this
            //client
			c.get_play_callback().setFunction(new play_callback());
			
			//Start streaming data to this client
			try
			{
				c.StartStreaming();
			}
			catch (Exception e) {}
			
			//Drive a bit
			c.Drive((short)150, (short)5000);
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception e) {}
			c.Drive((short)0, (short)5000);
			
			try
			{
				Thread.sleep(10000);
			}
			catch (Exception e) {}
			
			//Close the wire and stop streaming data
			try
			{
			wire.close();
			}
			catch (Exception e) {}
			
			try
			{
			c.StopStreaming();
			}
			catch (Exception e) {}
			
			//Shutdown Robot Raconteur.  This MUST be called on exit or the program will crash
			RobotRaconteurNode.s().shutdown();
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

	}
	
	//Function to handle the "Bump" event
	static class Bump implements Action
	{

		@Override
		public void action() {
			System.out.println("Bump!");
			
		}
		
		
	}
	 
	//Function to handle when the wire value changes
	static class wire_changed implements Action3<Wire<SensorPacket>.WireConnection, SensorPacket, TimeSpec>
	{

		@Override
		public void action(Wire<SensorPacket>.WireConnection arg0, SensorPacket arg1,
				TimeSpec arg2) {
			SensorPacket value2=arg0.getInValue();
			
			//Uncomment this line to print out the packets as they are received
			//System.out.println(value2.ID.value);
			
		}
		
	}
	
	//Function that is called by the service as a callback.  This returns
    //a few notes to play.
	static class play_callback implements Func2<Integer, Integer, UnsignedBytes> 
	{

		@Override
		public UnsignedBytes func(Integer arg0, Integer arg1) {
			byte[] notes={69, 16, 60, 16, 69, 16};
			return new UnsignedBytes(notes);
		}
		
	}
	

}
