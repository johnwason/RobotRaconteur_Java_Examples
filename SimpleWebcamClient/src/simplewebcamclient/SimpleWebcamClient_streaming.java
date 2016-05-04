package simplewebcamclient;

import com.robotraconteur.*;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

import org.opencv.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.*;

import experimental.createwebcam.*;

//Simple client to read streaming images from the Webcam pipe to show
//a live view from the cameras

public class SimpleWebcamClient_streaming {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		//Load the opencv library
		System.loadLibrary("opencv_java310");
		
		//Register the transport
		TcpTransport t=new TcpTransport();
		RobotRaconteurNode.s().registerTransport(t);
		
		//Register the service type
		RobotRaconteurNode.s().registerServiceType(new experimental__createwebcamFactory());
		
		//Connect to the service
		WebcamHost c_host=(WebcamHost)RobotRaconteurNode.s().connectService(args[0],null,null,null,"experimental.createwebcam.WebcamHost");
		
		//Get the Webcam object from the "Webcams" objref
		Webcam c1=c_host.get_Webcams(0);
		
		//Connect to the FrameStream pipe and receive a PipeEndpoint
        //PipeEndpoints a symmetric on client and service meaning that
        //you can send and receive on both ends
		Pipe<WebcamImage>.PipeEndpoint p=c1.get_FrameStream().connect(-1);
		//Add a callback for when a new pipe packet is received
		p.addPacketReceivedListener(new new_frame());
		
		//Start the packets streaming.  If there is an exception ignore it.
        //Exceptions are passed transparently to the client/service.
		try
		{
			c1.StartStreaming();
		}
		catch (Exception e) {}

		JFrame frame=new JFrame();
		JLabel label=new JLabel();
		frame.setTitle("Live View");
        frame.getContentPane().add(label);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
		
		
		while (true)
		{
			if (current_frame != null)
			{
				try
				{
				MatOfByte matOfByte = new MatOfByte();
			    
			    Imgcodecs.imencode(".bmp", current_frame, matOfByte);
			    byte[] byteArray = matOfByte.toArray();
			    BufferedImage bufImage = null;
			    InputStream in = new ByteArrayInputStream(byteArray);
		        bufImage = ImageIO.read(in);
		        label.setIcon(new ImageIcon(bufImage));
		        frame.pack();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return;
				}
			}
			if (!frame.isVisible())
			{
				break;
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {				
				
			}
			
		}
		
		//Close the PipeEndpoint
		p.close();
		
		//Stop streaming the frame
		c1.StopStreaming();
		
		//Shutdown Robot Raconteur
		RobotRaconteurNode.s().shutdown();
		
	}
	
	//Convert WebcamImage to OpenCV format
	public static Mat WebcamImageToMat(WebcamImage image)
	{
		Mat mat=new Mat(image.height, image.width, CvType.CV_8UC3);
		mat.put(0, 0, image.data.value);
		return mat;
		
	}
	
	public static Mat current_frame;
	
	//Callback for when a new frame packet is received
	public static class new_frame implements Action1<Pipe<WebcamImage>.PipeEndpoint>
	{
		public void action(Pipe<WebcamImage>.PipeEndpoint pipe_ep)
		{
			while (pipe_ep.available() > 0)
			{
				WebcamImage image=pipe_ep.receivePacket();
				current_frame=WebcamImageToMat(image);
			}
		}
		
	}

}
