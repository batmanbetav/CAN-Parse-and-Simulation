package P2G02;//Author: Vamsy Krishna Nanduri

import javax.swing.*;


//This class recives data from the simulation and uses it to display the information on the GUI
//This also handles the console print out of the simulation information

public class SensorDataReceiver {
    //Variables for the differnt GUI elements
    private JLabel time;
    private JLabel gpsLAT;
    private JLabel  gpsLON;
    private JLabel  vSpd;
    private JLabel  ang;
    private JLabel  gLat;
    private JLabel  gLon;
    private JLabel yaw;

    private JLabel Curve;
//Construct takes all the reference to the GUI elements from SimulatorGUI and stores them
    public SensorDataReceiver(JLabel timeVar, JLabel gpsLat, JLabel gpsLon, JLabel vehicleSPD, JLabel angle, JLabel gyroLat, JLabel gyroLong,JLabel  yawrate,JLabel Curve_d) {
        System.out.println("Current Time | Vehicle Speed | SteerAngle | YawRate | LatAccel | LongAccel | GPS Lat | GPS Lon");
        time = timeVar;
        gpsLAT = gpsLat;
        gpsLON = gpsLon;
        vSpd = vehicleSPD;
        ang = angle;
        gLat = gyroLat;
        gLon = gyroLong;
        yaw = yawrate;
        Curve = Curve_d;
    }

//This method is used to recive the data.
    //Using a switch and the passed in indentifier it uses the SensorValue object to get the data recived from the simulator
    public void receive(double value, int offset, String identifier) {

            switch (identifier) {
                //Handles prinint and displaying in the GUI data from the steering wheel angle sensor
                case "Curve":
                    time.setText(String.valueOf((float)offset/10000) +" Seconds");
                    if(value==1){
                        Curve.setText("Curved Left");
                    }
                    else if(value==-1){
                        Curve.setText("Curved Right");
                    }
                    else{
                        Curve.setText("Straight");
                    }
                    break;
                case "0003":

                    time.setText(String.valueOf((float)offset/10000) +" Seconds");
                    ang.setText(value+"°");

                    break;
                //Handles prining and displaying in the GUI data from the speed sensors
                case "019F":

                    time.setText(String.valueOf((float)offset/10000) +" Seconds");
                    vSpd.setText(value + " Km/h");
                    break;
                case "0245lon":
                    //Handles priningy and displaying in the GUI data from the gyro lon sensors

                    time.setText(String.valueOf((float)offset/10000) +" Seconds");
                    gLon.setText(String.valueOf(value));


                    break;
                case "0245lat":
                    //Handles priningy and displaying in the GUI data from the gyro lat sensors

                    time.setText(String.valueOf((float)offset/10000) +" Seconds");
                    gLat.setText(String.valueOf(value));


                    break;
                case "0245yaw":
                    //Handles priningy and displaying in the GUI data from the gyro yaw sensors

                    time.setText(String.valueOf((float)offset/10000) +" Seconds");
                    yaw.setText(String.valueOf(value));


                    break;
                //Handles prining and displaying in the GUI data from the gps
                case "gpsLon":

                    time.setText(String.valueOf((float)offset/10000) +" Seconds");
                    gpsLON.setText(String.valueOf(value));

                    break;
                case "gpsLat":

                    time.setText(String.valueOf((float)offset/10000) +" Seconds");
                    gpsLAT.setText(String.valueOf(value));
                    break;
                default:
                    time.setText(String.valueOf((float)offset/10000) +" Seconds");
                    break;

            }

        System.out.println(time.getText() + " | "+vSpd.getText()+" | " + ang.getText() + " | "+yaw.getText()+" | "+gLat.getText()+" | "+gLon.getText()+" | "+gpsLAT.getText()+" |"+gpsLON.getText()+" |"+Curve.getText());
    }
}