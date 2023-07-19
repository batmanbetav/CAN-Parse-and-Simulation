package P2G02;

//Author: Kace Curtis
public class SteeringWheelAngleFrame extends CANFrame {
   //Angle variable to store the angle unique to this frame type
    private String angle;
    //getter and setter for angle
   public void setAngle(String a)
   {
       angle = a;
   }
   public String getAngle(){
       return angle;
   }
   //Prettty prints this frame
    public void print(){
        System.out.println("Steering wheel angle: "+angle+"°");
        //System.out.println(" Offset" + super.getOffset()+"s");
        System.out.println();
    }

}
