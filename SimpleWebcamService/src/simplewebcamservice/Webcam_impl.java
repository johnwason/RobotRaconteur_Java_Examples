package simplewebcamservice;

import com.robotraconteur.*;


import org.opencv.*;
import org.opencv.core.*;
import org.opencv.video.*;
import org.opencv.videoio.*;
import experimental.createwebcam.*;

import java.util.*;

//Class to implement the "Webcam" Robot Raconteur object
public class Webcam_impl implements Webcam {
	
	VideoCapture m_capture;
	
	//Initialize the webcam
	public Webcam_impl(int cameraid, String cameraname)
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
    
    //Get the FrameStream pipe server
    public synchronized Pipe<WebcamImage> get_FrameStream()
    {
    	return m_FrameStream;
   
    }
    
    //Set the FrameStream pipe server
    public synchronized void set_FrameStream(Pipe<WebcamImage> value)
    {
    	m_FrameStream=value;
    	//Set the callback for when PipeEndpoints connect
    	m_FrameStream.setPipeConnectCallback(new pipe_connect());
    }
    
    public HashMap<Long,HashMap<Integer,Pipe<WebcamImage>.PipeEndpoint>> m_FrameStream_endpoints=new HashMap<Long,HashMap<Integer,Pipe<WebcamImage>.PipeEndpoint>>();
    
    //Function called when a PipeEndpoint connects
    class pipe_connect implements Action1<Pipe<WebcamImage>.PipeEndpoint>
    {

		@Override
		public void action(Pipe<WebcamImage>.PipeEndpoint arg0) {
			synchronized(m_FrameStream_endpoints)
			{
				if (!m_FrameStream_endpoints.containsKey(arg0.getEndpoint()))
				{
					m_FrameStream_endpoints.put(arg0.getEndpoint(), new HashMap<Integer,Pipe<WebcamImage>.PipeEndpoint>());
				}
				
				HashMap<Integer,Pipe<WebcamImage>.PipeEndpoint> dict1=m_FrameStream_endpoints.get(arg0.getEndpoint());
				dict1.put(arg0.getIndex(), arg0);
				arg0.setPipeCloseCallback(new pipe_closed());
				
			}
			
		}
    	
    }
    
    //Remove closed PipeEndpoints from the dictionaries
    class pipe_closed implements Action1<Pipe<WebcamImage>.PipeEndpoint>
    {

		@Override
		public void action(Pipe<WebcamImage>.PipeEndpoint arg0) {
			synchronized(m_FrameStream_endpoints)
			{
				try
				{
					HashMap<Integer,Pipe<WebcamImage>.PipeEndpoint> dict1=m_FrameStream_endpoints.get(arg0.getEndpoint());
					dict1.remove(arg0.getIndex());
				}
				catch (Exception e) {}
			}
			
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
				
				//Loop through all connected PipeEndpoints and send data
				synchronized(m_FrameStream_endpoints)
				{
					Set<Long> ep1=m_FrameStream_endpoints.keySet();
					long[] endpoints=new long[ep1.size()];
					int i=0;
					for (long ep2 : ep1)	{ 
						endpoints[i]=ep2;
						i++;
					}
					for(long ep : endpoints)
					{
						if (m_FrameStream_endpoints.containsKey(ep))
						{
							HashMap<Integer,Pipe<WebcamImage>.PipeEndpoint> dict1=m_FrameStream_endpoints.get(ep);
							Set<Integer> i1=dict1.keySet();
							int[] indices=new int[i1.size()];
							int ii=0;
							for (int i2 : i1)	{ 
								indices[ii]=i2;
								ii++;
							}
							
							for (int ind : indices)
							{
								if (dict1.containsKey(ind))
								{
									Pipe<WebcamImage>.PipeEndpoint pipe_ep=null;
									try
									{
										pipe_ep=dict1.get(ind);
										
										//Send the data to the connected PipeEndpoint here
										pipe_ep.sendPacket(frame);
									
									}
									catch (Exception e)
									{
										//If there is an error sending the data to the pipe,
                                        //assume the pipe has been closed.
										try
										{
											
											dict1.remove(ind);
										}
										catch (Exception ee) {}
									}
									
								}
							}
						}
						
						
					}
				}
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
