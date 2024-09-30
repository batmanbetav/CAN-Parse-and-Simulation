package P2G02;

//Author: Kace Curtis
public class VehicleSpeedFrame extends CANFrame{
    //Speed is unique to this frame
    private String speed;
    //getter and setter for the speed variable
    public void setSpeed(String s){
        speed = s;
    }
    public String getSpeed(){
        return speed;
    }
    //pretty prints this frame
    public void print(){
        System.out.println("Display vehicle speed: "+speed+"km/h");
       // System.out.println(" Offset" + super.getOffset()+"s");
        System.out.println();
    }
}
