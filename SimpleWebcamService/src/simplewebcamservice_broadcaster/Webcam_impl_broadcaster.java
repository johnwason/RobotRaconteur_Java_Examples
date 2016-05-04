package simplewebcamservice_broadcaster;

import com.robotraconteur.*;


import org.opencv.*;
import org.opencv.core.*;
import org.opencv.video.*;
import org.opencv.videoio.*;
import experimental.createwebcam.*;

import java.util.*;

//Class to implement the "Webcam" Robot Raconteur object
public class Webcam_impl_broadcaster implements Webcam {
	
	VideoCapture m_capture;
	
	//Initialize the webcam
	public Webcam_impl_broadcaster(int cameraid, String cameraname)
	{
		m_capture=new VideoCapture(cameraid);
		m_capture.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, 320);
		m_capture.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, 240);
		m_Name=cameraname;
	}
	
	//Shutdown the webcam
	public synchronized void shutdown()
	{
		m_capture.release();
	}
	
	String m_Name;
	//Return the webcam Name
	public String get_Name()
	{
		return m_Name;
	}
    public void set_Name(String value)
    {
    	throw new RuntimeException("Read only property");
    }
    
    //Function to capture a frame and return the Robot Raconteur WebcamImage structure
    public synchronized WebcamImage CaptureFrame()
    {
    	m_capture.grab();
    	Mat image=new Mat();
    	m_capture.retrieve(image);
    	WebcamImage o=new WebcamImage();
    	o.height=image.rows();
    	o.width=image.cols();
    	o.step=(int)image.step1();
    	byte[] data=new byte[(int) (image.total()*image.channels())];
    	image.get(0, 0,data);
    	o.data=new UnsignedBytes(data);
    	return o;
    }
    
    boolean streaming=false;
    
    //Start streaming frames
    public synchronized void StartStreaming()
    {
    	if (streaming) throw new RuntimeException("Already streaming");
    	streaming=true;
    	//Create a thread that retrieves and transmits frames
    	Thread t=new Thread(new frame_threadfunc());
    	t.start();
    }
    
    //Stop the image streaming frame
    public synchronized void StopStreaming()
    {
    	if (!streaming) throw new RuntimeException("Not streaming");
    	streaming=false;
    }
           
    Pipe<WebcamImage> m_FrameStream;
    PipeBroadcaster<WebcamImage> m_FrameStreamBroadcaster;
    
    //Get the FrameStream pipe server
    public synchronized Pipe<WebcamImage> get_FrameStream()
    {
    	return m_FrameStream;
   
    }
    
    //Set the FrameStream pipe server
    public synchronized void set_FrameStream(Pipe<WebcamImage> value)
    {
    	m_FrameStream=value;
    	m_FrameStreamBroadcaster=new PipeBroadcaster<WebcamImage>(value, 3);    	
    }
    
    class AsyncSendPacketHandler implements Action
    {
		@Override
		public void action() {			
			//We don't need to do anything here
		}  	    	
    }
    
    //Thread to stream frames by capturing data and sending it to
    //all connected PipeEndpoints
    class frame_threadfunc implements Runnable
    {

		@Override
		public void run() {
			while(streaming)
			{
				//Capture a frame
				WebcamImage frame=CaptureFrame();
				
				m_FrameStreamBroadcaster.asyncSendPacket(frame, new AsyncSendPacketHandler());
				
				try
				{
					
					Thread.sleep(100);
				}
				catch (Exception e)
				{
					
				}				
			}			
		}    	
    }
    
    UnsignedBytes m_buffer=null;
    MultiDimArray m_multidimbuffer=null;
    
    
    //Capture a frame and save it to the memory buffers
    public WebcamImage_size CaptureFrameToBuffer()
    {
    	WebcamImage image = CaptureFrame();
        m_buffer = image.data;

        //Rearrange the data into the correct format for MATLAB arrays
        UnsignedBytes mdata=new UnsignedBytes(new byte[image.height*image.width*3]);
        MultiDimArray mdbuf = new MultiDimArray(new int[] {image.height, image.width, 3 }, mdata);
        for (int channel=0; channel < 3; channel++)
        {
            int channel0 = image.height * image.width * channel;
            for (int x = 0; x < image.width; x++)
            {                        
                for (int y = 0; y < image.height; y++)
                {
                    byte value = image.data.value[(y * image.step + x*3)  + (2-channel)];
                    mdata.value[channel0 + x * image.height + y]=value;
                }
            }
        }
        m_multidimbuffer=mdbuf;

        WebcamImage_size size = new WebcamImage_size();
        size.width = image.width;
        size.height = image.height;
        size.step = image.step;
        return size;
    }
    
    
  //Return an ArrayMemory for the "buffer" data containing the image.
    public ArrayMemory<UnsignedBytes> get_buffer()
    {
    	//In many cases this ArrayMemory would not be initialized every time,
        //but for this example return a new ArrayMemory
    	return new ArrayMemory<UnsignedBytes>(m_buffer);
    
    }
    
    //Return a MultiDimArray for the "multidimbuffer" data containing the image
    public MultiDimArrayMemory<UnsignedBytes> get_multidimbuffer()
    {
    	//In many cases this MultiDimArrayMemory would not be initialized every time,
        //but for this example return a new MultiDimArrayMemory
    	return new MultiDimArrayMemory<UnsignedBytes>(m_multidimbuffer);
    }
	
}
