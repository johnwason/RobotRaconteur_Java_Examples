package simplewebcamservice_broadcaster;

import com.robotraconteur.*;
import java.util.*;

import experimental.createwebcam.*;

public class WebcamHost_impl_broadcaster implements WebcamHost {

	public static class Webcam_name
	{
		
		
		public int index;
		public String name;
		
		public Webcam_name(int index, String name)
		{
			this.index=index;
			this.name=name;
		}
				
	}
	
	HashMap<Integer,Webcam_impl_broadcaster> webcams=new HashMap<Integer,Webcam_impl_broadcaster>();
	
	public WebcamHost_impl_broadcaster(Webcam_name[] names)
	{
		int camcount=0;
		for (Webcam_name c : names)
		{
			Webcam_impl_broadcaster w=new Webcam_impl_broadcaster(c.index, c.name);
			webcams.put(camcount, w);
			camcount++;
			try
			{
				//Throw out first frame to ignore a possible bad frame
				w.CaptureFrame();
			}
			catch (Exception e) {}
			
		}
		
	}
	
	
	public synchronized void shutdown()
	{
		for (Webcam_impl_broadcaster w : webcams.values())
		{
			w.shutdown();
		}
		
	}
	
	@Override
	public synchronized Map<Integer, String> get_WebcamNames() {
		HashMap<Integer,String> o=new HashMap<Integer,String>();
		for (int i : o.keySet())
		{
			o.put(i, webcams.get(i).get_Name());
			
		}
		return o;
	}

	@Override
	public void set_WebcamNames(Map<Integer, String> value) {
		throw new RuntimeException("Read only property");
		
	}

	@Override
	public synchronized Webcam get_Webcams(int ind) {
		
		return webcams.get(ind);
	}

	

}
