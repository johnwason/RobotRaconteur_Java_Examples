//This file is automatically generated. DO NOT EDIT!
package experimental.createwebcam;
import java.util.*;
import com.robotraconteur.*;
public interface async_Webcam
{
    void async_get_Name(Action2<String,RuntimeException> rr_handler, int rr_timeout);
    void async_set_Name(String value, Action1<RuntimeException> rr_handler, int rr_timeout);
    void async_CaptureFrame(Action2<WebcamImage,RuntimeException> rr_handler,int rr_timeout);
    void async_StartStreaming(Action1<RuntimeException> rr_handler,int rr_timeout);
    void async_StopStreaming(Action1<RuntimeException> rr_handler,int rr_timeout);
    void async_CaptureFrameToBuffer(Action2<WebcamImage_size,RuntimeException> rr_handler,int rr_timeout);
}

