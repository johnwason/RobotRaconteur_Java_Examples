package simplewebcamservice;
import experimental.createwebcam2.*;


import com.robotraconteur.*;

//This program provides a simple Robot Raconteur server for viewing multiple webcams.
//It uses the Webcam_interface.robdef service definition

public class SimpleWebcamService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			
			//Load the opencv library
			System.loadLibrary("opencv_java410");
									
			//Load a list of names of Webcams
			WebcamHost_impl.Webcam_name[] names=new WebcamHost_impl.Webcam_name[2];
			names[0]=new WebcamHost_impl.Webcam_name(0,"Left");
			names[1]=new WebcamHost_impl.Webcam_name(1,"Right");
			
			//Initialize the webcams
			WebcamHost_impl host=new WebcamHost_impl(names);
			
			ServerNodeSetup setup = new ServerNodeSetup("experimental.createwebcam", 2355);
			try
			{						
				//Register the Webcam_interface type so that the node can understand the service definition
				RobotRaconteurNode.s().registerServiceType(new experimental__createwebcam2Factory());
				
				//Register the webcam host object as a service so that it can be connected to
				RobotRaconteurNode.s().registerService("Webcam", "experimental.createwebcam2", host);
				
				
				//Stay open until shut down
				System.out.println("Webcam server started press enter to quit");
				System.in.read();
				
				//Shutdown
				host.shutdown();
			}
			finally
			{
				setup.finalize();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
		
		
	}

}
