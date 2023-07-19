package P2G02;//Author: Vamsy Krishna Nanduri
//Contributed: Kace Curtis
import javax.swing.*;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

//this is the main simulation class
//This is a threadable class as it needs to run on a seperate thread to keep the gui from freeing
public class CANSimulation extends Thread{

    LinkedList<CANFrame> canFrameList;
    List<GPScoordinates> gpsCoordinates;
    LinkedList<SegmentData> SegDataList = new LinkedList<SegmentData>();
    SegmentData currentSeg = new SegmentData();
    //This is used to keep track of the current segment
    // 0 - no segment
    // 1 - straight
    // 2 - curve;
    int currentSegment = 0;


    String GPSLat = "0.0";
    String GPSLon = "0.0";
    //these vars help the sim keep track of what the current values of the sensors are
    // This is used for calcuation for segment information.
    String curveDir;
    double currSpeed = 0;
    double speedticks = 0;
    double avgSpeed = 0;
    double maxSpeed;
    double minSpeed = 9999;
    double maxAccLon = 0;
    double minAccLon = 0;
    double maxAccLat = 0;
    double minAccLat = 0;

    double maxSteeringAng = 0;
    double minSteeringAng = 0;

    double length = 0;

    private static int left_curve=0;
    private static int right_curve=0;
    private static int straight = 0;
    int segmentCount = 0;

    SensorDataReceiver senorDataReceiver;
    boolean exit = false;
    JButton simButton;
    // constucts the simulatoir object and hands it the needed lists of data from canframes and gps coordinates
    public CANSimulation(LinkedList<CANFrame> canFrameList, List<GPScoordinates> gpsCoordinates){
        this.canFrameList=canFrameList;
        this.gpsCoordinates=gpsCoordinates;
    }
    //This hands the simulator a reference to the run sim button. This is so when the sim is done running it will allow the sim to be run again.
    public void passButton(JButton button)
    {
        simButton = button;
    }
    //this allows the simulatorGui to pass in a sensordatareceiver with the appropriate refrences to the gui
    public void passSensorDataReceiver(SensorDataReceiver sdr)
    {
        senorDataReceiver = sdr;
    }


    double currHeading = 0.0;
    //this is the threadable method that the simulator is run in
    public void run(){
        boolean dataUpdate = false;
        int startIndex = 0;
        boolean notFound = false;
        long startTime = 0;
        startTime = System.nanoTime();
        long currentTime = 0 ;
        boolean sim = true;

        int i = 0;
        //This is the simulation loop where i is time elapsed in nanosecods
        //Converted to be a realtime loop
        while(sim){



            notFound = true;
            int j = startIndex;
            while(notFound)
            {


                //This checks the canframelist for a canframe with a matching offset to i(simulated time)
                //If found it switches based on the ID to handle passing the correct data to the SensorDataReciver
                if(j < canFrameList.size()) {
                    if (i == (Double.parseDouble(canFrameList.get(j).getOffset())) * 10) {


                        switch (canFrameList.get(j).getID()) {
                            case "0003":

                                SteeringWheelAngleFrame tempAngle = (SteeringWheelAngleFrame) canFrameList.get(j);
                                if(Double.parseDouble(tempAngle.getAngle()) > maxSteeringAng)
                                {
                                    maxSteeringAng = Double.parseDouble(tempAngle.getAngle());
                                }

                                if(Double.parseDouble(tempAngle.getAngle()) < minSteeringAng)
                                {
                                    minSteeringAng = Double.parseDouble(tempAngle.getAngle());
                                }
                                senorDataReceiver.receive(Double.parseDouble(tempAngle.getAngle()), i, "0003");
                                startIndex = j;

                                notFound = false;
                                break;
                            case "019F":

                                VehicleSpeedFrame tempSpeed = (VehicleSpeedFrame) canFrameList.get(j);
                                currSpeed = Double.parseDouble(tempSpeed.getSpeed());
                                avgSpeed += currSpeed;
                                speedticks += 1;
                                if(currSpeed > maxSpeed)
                                {
                                    maxSpeed = currSpeed;
                                }
                                if(currSpeed < minSpeed)
                                {
                                    minSpeed = currSpeed;
                                }
                                senorDataReceiver.receive(Double.parseDouble(tempSpeed.getSpeed()), i, "019F");
                                startIndex = j;

                                notFound = false;

                                break;
                            case "0245":

                                VehicleGyroFrame tempGyro = (VehicleGyroFrame) canFrameList.get(j);
                                Double lat = Double.parseDouble(tempGyro.getLatAccel());
                                Double lon = Double.parseDouble(tempGyro.getLonAccel());
                                if(lat > maxAccLat)
                                { maxAccLat = lat;}
                                if(lat < minAccLat){ minAccLat = lat;}
                                if(lon > maxAccLon){maxAccLon = lon;}
                                if(lon < minAccLon){minAccLon = lon;}
                                if(lat > 1){
                                    left_curve+=1;
                                    right_curve=0;
                                    straight = 0;
                                    //This requires at least 5 updates from the gyro lat acceleration before
                                    //A segment can be confirmed
                                    if(left_curve>5){


                                        curveDir = "Left";
                                        //the bgening of the segment is detected if the value of
                                        //currentsegment does not match the
                                        if(currentSegment == 1 || currentSegment == 0)
                                        {
                                            if(currentSegment == 1)
                                            {
                                                //This is where a straight segment data gets put into the
                                                //data object at the end of a segment
                                                currentSeg.GPSLatEnd = GPSLat;
                                                currentSeg.GPSLonEnd = GPSLon;
                                                currentSeg.avgSpeed =  avgSpeed/speedticks;
                                                currentSeg.maxSpeed = maxSpeed;
                                                currentSeg.minSpeed = minSpeed;
                                                currentSeg.maxAccLon = maxAccLon;
                                                currentSeg.minAccLon = minAccLon;
                                                currentSeg.length = calcLength(Double.parseDouble(currentSeg.GPSLatStart),Double.parseDouble(currentSeg.GPSLatEnd),Double.parseDouble(currentSeg.GPSLonStart),Double.parseDouble(currentSeg.GPSLonEnd));

                                                //Add the completed segment to the list
                                                if(currentSeg.length > 0) {
                                                    SegDataList.add(currentSeg);
                                                }
                                                currentSeg = null;
                                            }
                                            //New Segment curved

                                            //Reseting the max min avg values
                                            currentSegment = 2;
                                            avgSpeed =0;
                                            maxSteeringAng = 0;
                                            minSteeringAng = 0;
                                            minSpeed = 9999;
                                            maxAccLon = 0;
                                            minAccLon = 0;
                                            maxAccLat = 0;
                                            minAccLat = 0;
                                            speedticks = 0;

                                            //new segmentData created to start storing data on the segment
                                            currentSeg = new SegmentData();
                                            currentSeg.type="Curve";
                                            currentSeg.curveDir = curveDir;
                                            currentSeg.GPSLatStart = GPSLat;
                                            currentSeg.GPSLonStart = GPSLon;


                                        }
                                        //Transmit to the datareceiver the segment info if straight or curved
                                        senorDataReceiver.receive(1, i, "Curve");
                                    }
                                }
                                else if(lat< -1){
                                    left_curve=0;
                                    right_curve+=1;
                                    straight = 0;
                                    //This requires at least 5 updates from the gyro lat acceleration before
                                    //A segment can be confirmed
                                    if(right_curve>5){


                                        curveDir = "Right";
                                        //the beggining of the segment is detected if the value of
                                        //currentsegment does not match the
                                        if(currentSegment == 1 || currentSegment == 0)
                                        {
                                            if(currentSegment == 1)
                                            {
                                                //This is where a straight segment data gets put into the
                                                //data object at the end of a segment
                                                currentSeg.GPSLatEnd = GPSLat;
                                                currentSeg.GPSLonEnd = GPSLon;
                                                currentSeg.avgSpeed =  avgSpeed/speedticks;
                                                currentSeg.maxSpeed = maxSpeed;
                                                currentSeg.minSpeed = minSpeed;
                                                currentSeg.maxAccLon = maxAccLon;
                                                currentSeg.minAccLon = minAccLon;
                                                //Calculates the length based on gps data if value is 0 the segment was not
                                                //Long enough to get enough gps data or there was no more gps updates
                                                currentSeg.length = calcLength(Double.parseDouble(currentSeg.GPSLatStart),Double.parseDouble(currentSeg.GPSLatEnd),Double.parseDouble(currentSeg.GPSLonStart),Double.parseDouble(currentSeg.GPSLonEnd));

                                                //Add the completed segment to the list

                                                    SegDataList.add(currentSeg);

                                                currentSeg = null;
                                            }
                                            //New Segment curved
                                            currentSegment = 2;
                                            segmentCount++;
                                            avgSpeed =0;
                                            maxSteeringAng = 0;
                                            minSteeringAng = 0;
                                             minSpeed = 9999;
                                             maxAccLon = 0;
                                             minAccLon = 0;
                                             maxAccLat = 0;
                                             minAccLat = 0;
                                             speedticks = 0;



                                            currentSeg = new SegmentData();
                                            currentSeg.type="Curve";
                                            currentSeg.curveDir = curveDir;
                                            currentSeg.GPSLatStart = GPSLat;
                                            currentSeg.GPSLonStart = GPSLon;


                                        }
                                        //Transmit to the datareceiver the segment info if straight or curved
                                        senorDataReceiver.receive(-1, i, "Curve");
                                    }
                                }
                                else{


                                    left_curve=0;
                                    right_curve=0;
                                    straight += 1;
                                    if(straight > 5) {
                                        if (currentSegment == 2 || currentSegment == 0) {
                                            if (currentSegment == 2) {
                                                //End the segment curved

                                                currentSeg.GPSLatEnd = GPSLat;
                                                currentSeg.GPSLonEnd = GPSLon;
                                                currentSeg.avgSpeed =  avgSpeed/speedticks;
                                                currentSeg.maxAccLat = maxAccLat;
                                                currentSeg.minAccLat = minAccLat;
                                                // this section determines if the max or min angle should be used
                                                // for steering wheel angle
                                                if(currentSeg.curveDir.equals("Right")) {
                                                    currentSeg.maxSteeringAng = maxSteeringAng;
                                                }else{currentSeg.maxSteeringAng = minSteeringAng;}
                                                //calculate degrees changed from start to finished for the curve segement
                                                // using the last known bearing and the new bearing degrees of the curve can be calculated
                                                double tempheading = calcDegree(Double.parseDouble(currentSeg.GPSLatStart),Double.parseDouble(currentSeg.GPSLatEnd),Double.parseDouble(currentSeg.GPSLonStart),Double.parseDouble(currentSeg.GPSLonEnd));
                                                currentSeg.deg = tempheading-currHeading;
                                                currHeading = tempheading;

                                                //Add the completed segment to the list
                                                SegDataList.add(currentSeg);
                                                currentSeg = null;
                                            }
                                            //start new segment data
                                            segmentCount++;
                                            currentSegment = 1;
                                            avgSpeed =0;
                                            maxSteeringAng = 0;
                                            minSteeringAng = 0;
                                            minSpeed = 9999;
                                            maxAccLon = 0;
                                            minAccLon = 0;
                                            maxAccLat = 0;
                                            minAccLat = 0;
                                            speedticks = 0;

                                            currentSeg = new SegmentData();
                                            currentSeg.type="Straight";
                                            currentSeg.GPSLatStart = GPSLat;
                                            currentSeg.GPSLonStart = GPSLon;



                                        }
                                        //Transmit to the datareceiver the segment info if straight or curved
                                        senorDataReceiver.receive(0, i, "Curve");
                                    }
                                }

                                senorDataReceiver.receive(Double.parseDouble(tempGyro.getLonAccel()), i, "0245lon");
                                senorDataReceiver.receive(Double.parseDouble(tempGyro.getLatAccel()), i, "0245lat");
                                senorDataReceiver.receive(Double.parseDouble(tempGyro.getYaw()), i, "0245yaw");
                                startIndex = j;

                                notFound = false;

                                break;
                            default:
                                break;
                        }
                    //this else breaks the loop if the offset is higher then the simulated time.
                        //this was necessary to skip looping through too many canframes slowing down the entire sim
                    } else if(i < (Double.parseDouble(canFrameList.get(j).getOffset())) * 10) { notFound = false;}
                } else {notFound = false;}
                j++;
            }
            //This loops through the list of gpscoordinates and checks if the offset matches the simulated time
            //If its a match pass the data from that object to the sensordatareciver to display the data in the sim and console
            for(int k = 0;k<=gpsCoordinates.size()-1;k++)
            {
                //System.out.println(gpsCoordinates.get(k).getoffset());
                if(i == gpsCoordinates.get(k).getoffset()*10) {
                    if(currHeading == 0 && Double.parseDouble(GPSLat) != 0)
                    {
                        currHeading = calcDegree(Double.parseDouble(GPSLat),Double.parseDouble(gpsCoordinates.get(k).getLatitude()),Double.parseDouble(GPSLon),Double.parseDouble(gpsCoordinates.get(k).getLongitude()));
                    }
                    GPSLat = gpsCoordinates.get(k).getLatitude();
                    GPSLon =  gpsCoordinates.get(k).getLongitude();

                    senorDataReceiver.receive(Double.parseDouble(gpsCoordinates.get(k).getLatitude()),i,"gpsLat");
                    senorDataReceiver.receive(Double.parseDouble(gpsCoordinates.get(k).getLongitude()),i,"gpsLon");



                }
            }

            senorDataReceiver.receive(0.00,i,"defualt");

            // this is used to keep track of the time passed
            currentTime =  System.nanoTime()- startTime;
            i = (int)(currentTime/100000);
            //breaks out of the while loop the test drive is over
            if(currentTime >= TimeUnit.SECONDS.toNanos(45))
            {
                sim = false;
            }




        }
        if(currentSegment == 1)
        {
            currentSeg.GPSLatEnd = GPSLat;
            currentSeg.GPSLonEnd = GPSLon;
            currentSeg.avgSpeed =  avgSpeed/speedticks;
            currentSeg.maxSpeed = maxSpeed;
            currentSeg.minSpeed = minSpeed;
            currentSeg.maxAccLon = maxAccLon;
            currentSeg.minAccLon = minAccLon;

            //Add the completed segment to the list
            SegDataList.add(currentSeg);
            currentSeg = null;
        } else if (currentSegment == 2)
        {
            currentSeg.GPSLatEnd = GPSLat;
            currentSeg.GPSLonEnd = GPSLon;
            currentSeg.avgSpeed =  avgSpeed/speedticks;
            currentSeg.maxAccLat = maxAccLat;
            currentSeg.minAccLat = minAccLat;
            if(currentSeg.curveDir.equals("Right")) {
                currentSeg.maxSteeringAng = maxSteeringAng;
            }else{currentSeg.maxSteeringAng = minSteeringAng;}
            //calculate degrees changed from start to finished for the curve segement

            //Add the completed segment to the list
            SegDataList.add(currentSeg);
            currentSeg = null;
        }
        System.out.println(segmentCount);
        for(int j = 0; j < SegDataList.size();j++)
        {
            SegDataList.get(j).print();
        }
        simButton.setEnabled(true);
    }


    //this functions calcualtes length based off of gps lon and lat
public double calcLength(double latStart,double latEnd, double lonStart,double lonEnd)
{
    //Radius of earth in KM to meters
    double radE = 6371.1 * 1000;
    double latStartR = latStart * Math.PI/180;
    double latEndR = latEnd * Math.PI/180;
    double changeLat = (latEnd - latStart) * Math.PI /180 ;
    double changeLon = (lonEnd- lonStart) * Math.PI /180 ;

    double a = Math.sin(changeLat/2) * Math.sin(changeLat/2) + Math.cos(latStartR) * Math.cos(latEndR) * Math.sin(changeLon/2) * Math.sin(changeLon/2);
    double c =  2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
    return radE * c;
}
    //this functions calcualtes the bearing off the car based off of gps lon and lat

public double calcDegree(double latStart,double latEnd, double lonStart,double lonEnd)
{
    double radE = 6371.1 * 1000;
    double latStartR = latStart * Math.PI/180;
    double latEndR = latEnd * Math.PI/180;
    double lonStartR = lonStart * Math.PI/180;
    double lonEndR = lonEnd * Math.PI/180;

    double changeLat = (latEnd - latStart) * Math.PI /180 ;
    double changeLon = (lonEnd- lonStart) * Math.PI /180 ;
    double x = Math.sin(lonEndR-lonStartR)*Math.cos(latEndR);
    double y = Math.cos(latStartR)*Math.sin(latEndR) - Math.sin(latStartR)*Math.cos(latEndR) * Math.cos(lonEndR-lonStartR);
    double angle = Math.atan2(y,x);
    double bearing = (angle*180/Math.PI+360) % 360;
    return bearing;
}



}
