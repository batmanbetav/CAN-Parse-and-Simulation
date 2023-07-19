/*
import java.io.IOException;
import java.util.List;

// Author: Vamsy Krishna Nanduri
public class Test {
    public static void main(String args[]){
        //Creating new parser object. Pass it the file path for the constructor
        P2G2.CANParser parser = new P2G2.CANParser("19 CANmessages.trc");
        //Creating an empty CANFrame object
        CANFrame temp;
        P2G2.GPSparser gpsParser = new P2G2.GPSparser("GPStrace.txt");
        List<P2G2.GPScoordinates> gps = null;
        //Looping 30 times and callin the parsers getNextMessage() then printing the returned frame
        for(int i = 0; i < 30; i++)
        {
            temp = parser.getNextMessage();
            temp.print();
        }

        try {
            gps= gpsParser.readCoords();
        }catch (IOException e){
            System.out.println("Error Occured");
        }
        //Printing that the data structure is going to be reset
        System.out.println("Reseting data...");
        //resets the data structure and sets it back to the beginning
        parser.resetNextMessage();
        //looping another ten times and printing the returned frames
        for(int i = 0; i < 10; i++)
        {
            temp = parser.getNextMessage();
            temp.print();
        }



    }
}
*/
