//This file is automatically generated. DO NOT EDIT!
package experimental.createwebcam2;
import java.util.*;
import com.robotraconteur.*;
public class WebcamHost_stub extends ServiceStub implements WebcamHost, async_WebcamHost {
    public WebcamHost_stub(WrappedServiceStub innerstub) {
        super(innerstub); 
    }
    public Map<Integer,String> get_WebcamNames() {
    return MessageElementUtil.<Integer,String>unpackMapType(rr_innerstub.propertyGet("WebcamNames"));
    }
    public void dispatchEvent(String rr_membername, vectorptr_messageelement rr_m) {
    }
    public Webcam get_Webcams(int ind) {
    return (Webcam)findObjRefTyped("Webcams",String.valueOf(ind),"experimental.createwebcam2.Webcam");
    }
    public MessageElement callbackCall(String rr_membername, vectorptr_messageelement rr_m) {
    throw new MemberNotFoundException("Member not found");
    }
    public void async_get_WebcamNames(Action2<Map<Integer,String>,RuntimeException> rr_handler, int rr_timeout)
    {
    rr_async_PropertyGet("WebcamNames",new rrend_async_get_WebcamNames(),rr_handler,rr_timeout);
    }
    protected class rrend_async_get_WebcamNames implements Action3<MessageElement,RuntimeException,Object> {
    public void action(MessageElement value ,RuntimeException err,Object param)
    {
    Action2<Map<Integer,String>,RuntimeException> rr_handler=(Action2<Map<Integer,String>,RuntimeException>)param;
    if (err!=null)
    {
    rr_handler.action(null,err);
    return;
    }
    Map<Integer,String> rr_ret;
    try {
    rr_ret=MessageElementUtil.<Integer,String>unpackMapType(value);
    } catch (RuntimeException err2) {
    rr_handler.action(null,err2);
    return;
    }
    rr_handler.action(rr_ret,null);
    }
    }
    public void async_get_Webcams(int ind, Action2<Webcam,RuntimeException> handler, int timeout) {
    asyncFindObjRefTyped("Webcams",String.valueOf(ind),"experimental.createwebcam2.Webcam",handler,timeout);
    }
}