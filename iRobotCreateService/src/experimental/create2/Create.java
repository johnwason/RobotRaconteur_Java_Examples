//This file is automatically generated. DO NOT EDIT!
package experimental.create2;
import java.util.*;
import com.robotraconteur.*;
@RobotRaconteurServiceObjectInterface
public interface Create
{
    int get_DistanceTraveled();
    int get_AngleTraveled();
    UnsignedByte get_Bumpers();
    void Drive(short velocity, short radius);
    void StartStreaming();
    void StopStreaming();
    void  addBumpListener(Action listener); 
    void  removeBumpListener(Action listener); 
    Callback<Func2<Integer,Integer,UnsignedBytes>> get_play_callback();
    void set_play_callback(Callback<Func2<Integer,Integer,UnsignedBytes>> value);
    Wire<SensorPacket> get_packets();
    void set_packets(Wire<SensorPacket> value);
}

