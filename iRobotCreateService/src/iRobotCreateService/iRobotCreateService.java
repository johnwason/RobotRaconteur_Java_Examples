package iRobotCreateService;

import com.robotraconteur.*;
import experimental.create.*;

//This program provides a simple Robot Raconteur server for controlling the iRobot Create.  It uses
//the Create_interface.robdef service definition.

public class iRobotCreateService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{						
			//Initialize the create robot object
			Create_impl c=new Create_impl();
			c.start(args[0]);
			
			//Initialize the local transport
			LocalTransport t1=new LocalTransport();
			t1.startServerAsNodeName("experimental.create.Create");
			
			//Register the Local transport
			RobotRaconteurNode.s().registerTransport(t1);
			
			//Initialize the TCP transport and start listening for connections on port 2354
			TcpTransport t2=new TcpTransport();			
			t2.startServer(2354);
			
			//Attempt to load a TLS certificate
			try
			{
				t2.loadTlsNodeCertificate();
			}
			catch (Exception e)
			{
				System.out.println("warning: could not load TLS certificate");
			}
			
			//Enable auto-discovery announcements
			t2.enableNodeAnnounce();
			
			//Register the TCP transport
			RobotRaconteurNode.s().registerTransport(t2);
			
			//Register the Create_interface type so that the node can understand the service definition
			RobotRaconteurNode.s().registerServiceType(new experimental.create.experimental__createFactory());
			
			//Register the create object as a service so that it can be connected to
			RobotRaconteurNode.s().registerService("Create", "experimental.create", c);
						
			//Stay open until shut down
			System.out.println("Create server started press enter to quit");
			System.in.read();
			
			//Shutdown
			c.shutdown();
			
			//Shutdown the node.  This must be called or the program won't exit.
			RobotRaconteurNode.s().shutdown();
		
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		
		}
		

	}

}
