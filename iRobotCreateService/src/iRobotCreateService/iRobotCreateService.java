package iRobotCreateService;

import com.robotraconteur.*;
import experimental.create2.*;

//This program provides a simple Robot Raconteur server for controlling the iRobot Create.  It uses
//the Create_interface.robdef service definition.

public class iRobotCreateService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ServerNodeSetup setup = new ServerNodeSetup("experimental.create2", 2354);
		
		try
		{						
			//Initialize the create robot object
			Create_impl c=new Create_impl();
			c.start(args[0]);
			
			//Register the Create_interface type so that the node can understand the service definition
			RobotRaconteurNode.s().registerServiceType(new experimental.create2.experimental__create2Factory());
			
			//Register the create object as a service so that it can be connected to
			RobotRaconteurNode.s().registerService("Create", "experimental.create2", c);
						
			//Stay open until shut down
			System.out.println("Create server started press enter to quit");
			System.in.read();
			
			//Shutdown
			c.shutdown();
			
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		
		}
		finally
		{
			setup.finalize();
		}
	}
}
