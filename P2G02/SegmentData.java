
//Author: Vamsy Krishna Nanduri
package P2G02;

public class SegmentData {
    //All the needed variables to store the data on the segments
    String type;
    String GPSLatStart;
    String GPSLonStart;
    String GPSLatEnd;
    String GPSLonEnd;
    String curveDir;
    double avgSpeed;
    double maxSpeed;
    double minSpeed;
    double maxAccLon;
    double minAccLon;
    double maxAccLat;
    double minAccLat;
    double deg;
    double maxSteeringAng;
    double length;

    //This pretty prints the segment data too the console.
    //It switches based on type
    public void print()
    {
        switch(type){
            case "Straight":
                System.out.println("SegmentData type: "+type);
                System.out.println("GPS Start Coordinates Longitude: "+GPSLonStart +"  Latitude: " + GPSLatStart);
                System.out.println("GPS End Coordinates Longitude: "+GPSLonEnd +"  Latitude: " + GPSLatEnd);
                System.out.println("Average Speed: "+avgSpeed+" Km/h");
                System.out.println("Maximum Speed: "+maxSpeed+" Km/h");
                System.out.println("Minimum Speed: "+minSpeed+" Km/h");
                System.out.println("Maximum Acceleration Longitude: "+maxAccLon);
                System.out.println("Minimum Acceleration Longitude: "+minAccLon);
                System.out.println("Segement Length: "+length);
                System.out.println();

                break;
            case "Curve":
                System.out.println("SegmentData type: "+type);
                System.out.println("GPS Start Coordinates Longitude: "+GPSLonStart +"  Latitude: " + GPSLatStart);
                System.out.println("GPS End Coordinates Longitude: "+GPSLonEnd +"  Latitude: " + GPSLatEnd);
                System.out.println("Average Speed: "+avgSpeed+" Km/h");
                System.out.println("Curve Direction: "+curveDir);
                System.out.println("Maximum Acceleration Latitude: "+maxAccLat);
                System.out.println("Minimum Acceleration Latitude: "+minAccLat);
                System.out.println("Degrees of curve: "+deg);
                System.out.println("Maximum Steering Wheel Angle: "+ maxSteeringAng);
                System.out.println();
                break;

            default:
                System.out.println("SegmentData type not correctly set");
                break;
        }
    }
}
