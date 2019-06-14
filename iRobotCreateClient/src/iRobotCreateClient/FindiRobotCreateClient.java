package iRobotCreateClient;

import experimental.create2.*;

import com.robotraconteur.*;

//This program uses the FindServiceByType function to find the iRobot Create service
//using autodiscovery

public class FindiRobotCreateClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		// Use ClientNodeSetup to initialize node
		ClientNodeSetup setup=new ClientNodeSetup();
		try
		{
		
			//Register the Create_interface service type
			RobotRaconteurNode.s().registerServiceType(new experimental__create2Factory());
			
			
			//Wait 5 seconds to receive the beacon packets for autodiscovery which are
	        
			try
			{
				Thread.sleep(5000);
				
			}
			catch (Exception e) {}
			
			//Search for the "Create_interface.Create" object type on "tcp" transports
			ServiceInfo2[] res=RobotRaconteurNode.s().findServiceByType("experimental.create2.Create", new String[] {"rr+local","rr+tcp"});
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
				Create c=(Create)RobotRaconteurNode.s().connectService(res[0].ConnectionURL,null,null,null,"experimental.create2.Create");
				
				//Drive a bit
				c.Drive((short)200, (short)5000);
				try
				{
					
					Thread.sleep(1000);
				}
				catch (Exception e) {}
				
				c.Drive((short)0, (short)0);
			}		
		}
		finally
		{
			setup.finalize();			
		}
	}

}
