package P2G02;

//Author: Kace Curtis
public class VehicleGyroFrame extends CANFrame{
    //Variables that are unique to this frame
    private String yaw;
    private String lonAccel;
    private String latAccel;

    //The Following are the getter and setters for the variables for this class
    public String getLatAccel() {
        return latAccel;
    }

    public void setLatAccel(String latAccel) {
        this.latAccel = latAccel;
    }

    public String getLonAccel() {
        return lonAccel;
    }

    public void setLonAccel(String lonAccel) {
        this.lonAccel = lonAccel;
    }

    public String getYaw() {
        return yaw;
    }

    public void setYaw(String yaw) {
        this.yaw = yaw;
    }
    //This is an override for print. this pretty prints to the console this objects data
    public void print(){
        System.out.println("Vehicle yaw rate: "+yaw+"°/s");
        System.out.println("Vehicle longitudinal acceleration: "+lonAccel+"m/s^2");
        System.out.println("Vehicle lateral acceleration: "+latAccel+"m/s^2");
        System.out.println();

    }


}
