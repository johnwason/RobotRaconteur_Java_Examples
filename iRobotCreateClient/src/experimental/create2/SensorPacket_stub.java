//This file is automatically generated. DO NOT EDIT!
package experimental.create2;
import java.util.*;
import com.robotraconteur.*;
public class SensorPacket_stub implements IStructureStub {
    public SensorPacket_stub(experimental__create2Factory d) {def=d;}
    private experimental__create2Factory def;
    public MessageElementStructure packStructure(Object s1) {
    vectorptr_messageelement m=new vectorptr_messageelement();
    try {
    if (s1 ==null) return null;
    SensorPacket s = (SensorPacket)s1;
    MessageElementUtil.addMessageElementDispose(m,MessageElementUtil.<UnsignedBytes>packArray("ID",s.ID.array()));
    MessageElementUtil.addMessageElementDispose(m,MessageElementUtil.<UnsignedBytes>packArray("Data",s.Data));
    return new MessageElementStructure("experimental.create2.SensorPacket",m);
    }
    finally {
    m.delete();
    }
    }
    public <T> T unpackStructure(MessageElementData m) {
    if (m == null ) return null;
    MessageElementStructure m2 = (MessageElementStructure)m;
    vectorptr_messageelement mm=m2.getElements();
    try {
    SensorPacket s=new SensorPacket();
    s.ID =MessageElementUtil.<UnsignedBytes>castDataAndDispose(MessageElement.findElement(mm,"ID")).get(0);
    s.Data =MessageElementUtil.<UnsignedBytes>unpackArray(MessageElement.findElement(mm,"Data"));
    T st; try {st=(T)s;} catch (Exception e) {throw new RuntimeException(new DataTypeMismatchException("Wrong structuretype"));}
    return st;
    }
    finally {
    if (mm!=null) mm.delete();
    }
    }
}