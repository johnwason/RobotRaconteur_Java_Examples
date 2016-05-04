package iRobotCreateClient;

import experimental.create.*;

import com.robotraconteur.*;

//This program uses the FindServiceByType function to find the iRobot Create service
//using autodiscovery

public class FindiRobotCreateClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
						
		//Create and register a TcpTransport
		TcpTransport t=new TcpTransport();
		RobotRaconteurNode.s().registerTransport(t);
		
		//Register the Create_interface service type
		RobotRaconteurNode.s().registerServiceType(new experimental__createFactory());
		
		 //Enable the TcpChannel to listen for other nodes.  The IPNodeDiscoveryFlags will
        //normally be the same as here
		t.enableNodeDiscoveryListening();
		
		//Wait 10 seconds to receive the beacon packets for autodiscovery which are
        //sent every 5 seconds.
		try
		{
			Thread.sleep(10000);
			
		}
		catch (Exception e) {}
		
		//Search for the "Create_interface.Create" object type on "tcp" transports
		ServiceInfo2[] res=RobotRaconteurNode.s().findServiceByType("experimental.create.Create", new String[] {"rr+local","rr+tcp"});
		for (ServiceInfo2 r : res)
		{
			System.out.println(r.NodeName + " " + r.NodeID.toString() + " " + r.Name + r.ConnectionURL[0]);
		}
		
		if (res.length==0)
		{			
			System.out.println("Create not found!");
		}
		else
		{		
			//Connect to the found service
			Create c=(Create)RobotRaconteurNode.s().connectService(res[0].ConnectionURL,null,null,null,"experimental.create.Create");
			
			//Drive a bit
			c.Drive((short)200, (short)5000);
			try
			{
				
				Thread.sleep(1000);
			}
			catch (Exception e) {}
			
			c.Drive((short)0, (short)0);
		}
		
		//Shutdown Robot Raconteur
		RobotRaconteurNode.s().shutdown();
	}

}
