//This file is automatically generated. DO NOT EDIT!
package experimental.createwebcam2;
import java.util.*;
import com.robotraconteur.*;
public class WebcamImage_size_stub implements IStructureStub {
    public WebcamImage_size_stub(experimental__createwebcam2Factory d) {def=d;}
    private experimental__createwebcam2Factory def;
    public MessageElementStructure packStructure(Object s1) {
    vectorptr_messageelement m=new vectorptr_messageelement();
    try {
    if (s1 ==null) return null;
    WebcamImage_size s = (WebcamImage_size)s1;
    MessageElementUtil.addMessageElementDispose(m,MessageElementUtil.<int[]>packArray("width",new int[] {s.width}));
    MessageElementUtil.addMessageElementDispose(m,MessageElementUtil.<int[]>packArray("height",new int[] {s.height}));
    MessageElementUtil.addMessageElementDispose(m,MessageElementUtil.<int[]>packArray("step",new int[] {s.step}));
    return new MessageElementStructure("experimental.createwebcam2.WebcamImage_size",m);
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
    WebcamImage_size s=new WebcamImage_size();
    s.width =(MessageElementUtil.<int[]>castDataAndDispose(MessageElement.findElement(mm,"width")))[0];
    s.height =(MessageElementUtil.<int[]>castDataAndDispose(MessageElement.findElement(mm,"height")))[0];
    s.step =(MessageElementUtil.<int[]>castDataAndDispose(MessageElement.findElement(mm,"step")))[0];
    T st; try {st=(T)s;} catch (Exception e) {throw new RuntimeException(new DataTypeMismatchException("Wrong structuretype"));}
    return st;
    }
    finally {
    if (mm!=null) mm.delete();
    }
    }
}
