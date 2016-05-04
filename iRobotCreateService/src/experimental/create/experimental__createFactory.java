//This file is automatically generated. DO NOT EDIT!
package experimental.create;
import java.util.*;
import com.robotraconteur.*;
public class experimental__createFactory extends ServiceFactory
{
    public String defString()
{
    String out="";
out+="\n";
out+="#Service to provide sample interface to the iRobot Create\n";
out+="service experimental.create\n";
out+="\n";
out+="option version 0.5\n";
out+="\n";
out+="struct SensorPacket\n";
out+="field uint8 ID\n";
out+="field uint8[] Data\n";
out+="end struct\n";
out+="\n";
out+="object Create\n";
out+="option constant int16 DRIVE_STRAIGHT 32767\n";
out+="option constant int16 SPIN_CLOCKWISE -1\n";
out+="option constant int16 SPIN_COUNTERCLOCKWISE 1\n";
out+="\n";
out+="\n";
out+="function void Drive(int16 velocity, int16 radius)\n";
out+="\n";
out+="function void StartStreaming()\n";
out+="function void StopStreaming()\n";
out+="\n";
out+="property int32 DistanceTraveled\n";
out+="property int32 AngleTraveled\n";
out+="property uint8 Bumpers\n";
out+="\n";
out+="event Bump()\n";
out+="\n";
out+="wire SensorPacket packets\n";
out+="\n";
out+="callback uint8[] play_callback(int32 DistanceTraveled, int32 AngleTraveled)\n";
out+="\n";
out+="end object\n";
return out;    }
    public String getServiceName() {return "experimental.create";}
    public SensorPacket_stub SensorPacket_stubentry;
    public experimental__createFactory()
{
    SensorPacket_stubentry=new SensorPacket_stub(this);
    }
    public IStructureStub findStructureStub(String objecttype)
    {
    String objshort=removePath(objecttype);
    if (objshort.equals("SensorPacket"))    return SensorPacket_stubentry;
    throw new DataTypeException("Cannot find appropriate structure stub");
    }
    public MessageElementStructure packStructure(Object s) {
    if (s==null) return null;
    String objtype=s.getClass().getName().toString();
    if (removeTrailingUnderscore(RobotRaconteurNode.splitQualifiedName(objtype)[0]).equals("experimental.create")) {
    String objshort=removePath(objtype);
    if (objshort.equals( "SensorPacket"))
    return  SensorPacket_stubentry.packStructure(s);
    } else {
    return RobotRaconteurNode.s().packStructure(s);
    }
    throw new DataTypeException("");
    }
    public <T> T unpackStructure(MessageElementStructure l) {
    if (l==null) return null;
    if (removeTrailingUnderscore(RobotRaconteurNode.splitQualifiedName(l.getType())[0]).equals( "experimental.create")) {
    String objshort=removePath(l.getType());
    if (objshort.equals("SensorPacket"))
    return  SensorPacket_stubentry.<T>unpackStructure(l);
    } else {
    return RobotRaconteurNode.s().<T>unpackStructure(l);
    }
    throw new DataTypeException("Could not unpack structure");
    }
    public ServiceStub createStub(WrappedServiceStub innerstub) {
    String objecttype=innerstub.getRR_objecttype().getServiceDefinition().getName() + "." + innerstub.getRR_objecttype().getName();
    if (RobotRaconteurNode.splitQualifiedName(objecttype)[0].equals( "experimental.create")) {
    String objshort=removePath(objecttype);
    if(objshort.equals( "Create"))
    return new Create_stub(innerstub);
    } else {
    String ext_service_type=removeTrailingUnderscore(RobotRaconteurNode.splitQualifiedName(objecttype)[0]);
    return RobotRaconteurNode.s().getServiceType(ext_service_type).createStub(innerstub);
    }
    throw new ServiceException("Could not create stub");
    }
    public ServiceSkel createSkel(Object obj) {
    String objtype=ServiceSkelUtil.findParentInterface(obj.getClass()).getName().toString();
    if (removeTrailingUnderscore(RobotRaconteurNode.splitQualifiedName(objtype.toString())[0]).equals( "experimental.create")) {
    String sobjtype=removePath(objtype);
    if(sobjtype.equals( "Create"))
    return new Create_skel((Create)obj);
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
    if (!rr_stype[0].equals("experimental.create")) return RobotRaconteurNode.s().downCastException(rr_exp);
    return rr_exp;
    }
}
