//This file is automatically generated. DO NOT EDIT!
package experimental.createwebcam;
import java.util.*;
import com.robotraconteur.*;
public class experimental__createwebcamFactory extends ServiceFactory
{
    public String defString()
{
    String out="";
out+="#Service to provide sample interface to webcams\n";
out+="service experimental.createwebcam\n";
out+="\n";
out+="option version 0.5\n";
out+="\n";
out+="struct WebcamImage\n";
out+="field int32 width\n";
out+="field int32 height\n";
out+="field int32 step\n";
out+="field uint8[] data\n";
out+="end struct\n";
out+="\n";
out+="struct WebcamImage_size\n";
out+="field int32 width\n";
out+="field int32 height\n";
out+="field int32 step\n";
out+="end struct\n";
out+="\n";
out+="object Webcam\n";
out+="property string Name\n";
out+="function WebcamImage CaptureFrame()\n";
out+="\n";
out+="function void StartStreaming()\n";
out+="function void StopStreaming()\n";
out+="pipe WebcamImage FrameStream\n";
out+="\n";
out+="function WebcamImage_size CaptureFrameToBuffer()\n";
out+="memory uint8[] buffer\n";
out+="memory uint8[*] multidimbuffer\n";
out+="\n";
out+="end object\n";
out+="\n";
out+="object WebcamHost\n";
out+="property string{int32} WebcamNames\n";
out+="objref Webcam{int32} Webcams\n";
out+="end object\n";
return out;    }
    public String getServiceName() {return "experimental.createwebcam";}
    public WebcamImage_stub WebcamImage_stubentry;
    public WebcamImage_size_stub WebcamImage_size_stubentry;
    public experimental__createwebcamFactory()
{
    WebcamImage_stubentry=new WebcamImage_stub(this);
    WebcamImage_size_stubentry=new WebcamImage_size_stub(this);
    }
    public IStructureStub findStructureStub(String objecttype)
    {
    String objshort=removePath(objecttype);
    if (objshort.equals("WebcamImage"))    return WebcamImage_stubentry;
    if (objshort.equals("WebcamImage_size"))    return WebcamImage_size_stubentry;
    throw new DataTypeException("Cannot find appropriate structure stub");
    }
    public MessageElementStructure packStructure(Object s) {
    if (s==null) return null;
    String objtype=s.getClass().getName().toString();
    if (removeTrailingUnderscore(RobotRaconteurNode.splitQualifiedName(objtype)[0]).equals("experimental.createwebcam")) {
    String objshort=removePath(objtype);
    if (objshort.equals( "WebcamImage"))
    return  WebcamImage_stubentry.packStructure(s);
    if (objshort.equals( "WebcamImage_size"))
    return  WebcamImage_size_stubentry.packStructure(s);
    } else {
    return RobotRaconteurNode.s().packStructure(s);
    }
    throw new DataTypeException("");
    }
    public <T> T unpackStructure(MessageElementStructure l) {
    if (l==null) return null;
    if (removeTrailingUnderscore(RobotRaconteurNode.splitQualifiedName(l.getType())[0]).equals( "experimental.createwebcam")) {
    String objshort=removePath(l.getType());
    if (objshort.equals("WebcamImage"))
    return  WebcamImage_stubentry.<T>unpackStructure(l);
    if (objshort.equals("WebcamImage_size"))
    return  WebcamImage_size_stubentry.<T>unpackStructure(l);
    } else {
    return RobotRaconteurNode.s().<T>unpackStructure(l);
    }
    throw new DataTypeException("Could not unpack structure");
    }
    public ServiceStub createStub(WrappedServiceStub innerstub) {
    String objecttype=innerstub.getRR_objecttype().getServiceDefinition().getName() + "." + innerstub.getRR_objecttype().getName();
    if (RobotRaconteurNode.splitQualifiedName(objecttype)[0].equals( "experimental.createwebcam")) {
    String objshort=removePath(objecttype);
    if(objshort.equals( "Webcam"))
    return new Webcam_stub(innerstub);
    if(objshort.equals( "WebcamHost"))
    return new WebcamHost_stub(innerstub);
    } else {
    String ext_service_type=removeTrailingUnderscore(RobotRaconteurNode.splitQualifiedName(objecttype)[0]);
    return RobotRaconteurNode.s().getServiceType(ext_service_type).createStub(innerstub);
    }
    throw new ServiceException("Could not create stub");
    }
    public ServiceSkel createSkel(Object obj) {
    String objtype=ServiceSkelUtil.findParentInterface(obj.getClass()).getName().toString();
    if (removeTrailingUnderscore(RobotRaconteurNode.splitQualifiedName(objtype.toString())[0]).equals( "experimental.createwebcam")) {
    String sobjtype=removePath(objtype);
    if(sobjtype.equals( "Webcam"))
    return new Webcam_skel((Webcam)obj);
    if(sobjtype.equals( "WebcamHost"))
    return new WebcamHost_skel((WebcamHost)obj);
    } else {
    String ext_service_type=removeTrailingUnderscore(RobotRaconteurNode.splitQualifiedName(objtype.toString())[0]);
    return RobotRaconteurNode.s().getServiceType(ext_service_type).createSkel(obj);
    }
    throw new ServiceException("Could not create skel");
    }
    public RobotRaconteurException downCastException(RobotRaconteurException rr_exp){
    if (rr_exp==null) return rr_exp;
    String rr_type=rr_exp.error;
    if (!rr_type.contains(".")) return rr_exp;
    String[] rr_stype = RobotRaconteurNode.splitQualifiedName(rr_type);
    if (!rr_stype[0].equals("experimental.createwebcam")) return RobotRaconteurNode.s().downCastException(rr_exp);
    return rr_exp;
    }
}
