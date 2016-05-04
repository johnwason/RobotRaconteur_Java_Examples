package simplewebcamservice_broadcaster;
import experimental.createwebcam.*;


import com.robotraconteur.*;

//This program provides a simple Robot Raconteur server for viewing multiple webcams.
//It uses the Webcam_interface.robdef service definition

public class SimpleWebcamService_broadcaster {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			
			//Load the opencv library
			System.loadLibrary("opencv_java310");
			
						
			//Load a list of names of Webcams
			WebcamHost_impl_broadcaster.Webcam_name[] names=new WebcamHost_impl_broadcaster.Webcam_name[2];
			names[0]=new WebcamHost_impl_broadcaster.Webcam_name(0,"Left");
			names[1]=new WebcamHost_impl_broadcaster.Webcam_name(1,"Right");
			
			//Initialize the webcams
			WebcamHost_impl_broadcaster host=new WebcamHost_impl_broadcaster(names);
			
			//Initialize the local transport
			LocalTransport t1=new LocalTransport();
			t1.startServerAsNodeName("experimental.createwebcam.WebcamHost");
			RobotRaconteurNode.s().registerTransport(t1);
			
			//Initialize the TCP transport and start listening for connections on port 2355
			TcpTransport t2=new TcpTransport();
			t2.startServer(2355);
			
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
			
			//Register the TCP channel
			RobotRaconteurNode.s().registerTransport(t2);
			
			//Register the Webcam_interface type so that the node can understand the service definition
			RobotRaconteurNode.s().registerServiceType(new experimental__createwebcamFactory());
			
			//Register the webcam host object as a service so that it can be connected to
			RobotRaconteurNode.s().registerService("Webcam", "experimental.createwebcam", host);
			
			
			//Stay open until shut down
			System.out.println("Webcam server started press enter to quit");
			System.in.read();
			
			//Shutdown
			host.shutdown();
			RobotRaconteurNode.s().shutdown();
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
		
		
	}

}
