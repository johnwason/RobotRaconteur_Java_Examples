//This file is automatically generated. DO NOT EDIT!
package experimental.createwebcam2;
import java.util.*;
import com.robotraconteur.*;
public class Webcam_default_impl implements Webcam{
    protected PipeBroadcaster<WebcamImage> rrvar_FrameStream;
    protected String rrvar_Name;
    public String get_Name() { return rrvar_Name; }
    public void set_Name(String value) { rrvar_Name = value; }
    public WebcamImage CaptureFrame() {
    throw new UnsupportedOperationException();    }
    public void StartStreaming() {
    throw new UnsupportedOperationException();    }
    public void StopStreaming() {
    throw new UnsupportedOperationException();    }
    public WebcamImage_size CaptureFrameToBuffer() {
    throw new UnsupportedOperationException();    }
    public Pipe<WebcamImage> get_FrameStream()
    { return rrvar_FrameStream.getPipe();  }
    public void set_FrameStream(Pipe<WebcamImage> value)
    {
    if (rrvar_FrameStream!=null) throw new IllegalStateException("Pipe already set");
    rrvar_FrameStream= new PipeBroadcaster<WebcamImage>(value);
    }
    public ArrayMemory<UnsignedBytes> get_buffer()
    { throw new UnsupportedOperationException(); }
    public MultiDimArrayMemory<UnsignedBytes> get_multidimbuffer()
    { throw new UnsupportedOperationException(); }
}
