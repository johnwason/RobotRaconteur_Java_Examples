package simplewebcamclient;

import com.robotraconteur.*;

import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

import org.opencv.*;
import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.opencv.imgcodecs.*;

import experimental.createwebcam2.*;

//Simple client to read images from a Webcam server
//and display the images

public class SimpleWebcamClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Load the opencv library
		System.loadLibrary("opencv_java410");
		
		// Use ClientNodeSetup to initialize node
		ClientNodeSetup setup = new ClientNodeSetup();
		try
		{		
			RobotRaconteurNode.s().registerServiceType(new experimental__createwebcam2Factory());
			
			//Connect to service
			WebcamHost c_host=(WebcamHost)RobotRaconteurNode.s().connectService(args[0],null,null,null,"experimental.createwebcam2.WebcamHost");
			
			//Get the Webcam objects from the "Webcams" objref
			Webcam c1=c_host.get_Webcams(0);
			Webcam c2=c_host.get_Webcams(1);
			
			//Capture an image and convert to OpenCV image type
			Mat frame1=WebcamImageToMat(c1.CaptureFrame());
			Mat frame2=WebcamImageToMat(c2.CaptureFrame());
			
			//Show the images
			showImage(c1.get_Name(),frame1);
			showImage(c2.get_Name(),frame2);
		
		}
		finally
		{
			setup.finalize();
		}
		
		
	}
	
	//Convert WebcamImage to OpenCV format
	public static Mat WebcamImageToMat(WebcamImage image)
	{
		Mat mat=new Mat(image.height, image.width, CvType.CV_8UC3);
		mat.put(0, 0, image.data.value);
		return mat;		
	}
	
	public static void showImage(String title, Mat img) {
	    MatOfByte matOfByte = new MatOfByte();
	    
	    Imgcodecs.imencode(".bmp", img, matOfByte);
	    byte[] byteArray = matOfByte.toArray();
	    BufferedImage bufImage = null;
	    try {
	        InputStream in = new ByteArrayInputStream(byteArray);
	        bufImage = ImageIO.read(in);
	        JFrame frame = new JFrame();
	        frame.setTitle(title);
	        frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        frame.pack();
	        frame.setVisible(true);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
