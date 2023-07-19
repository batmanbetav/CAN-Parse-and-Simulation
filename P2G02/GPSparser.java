package P2G02;//Author: Vamsy Krishna Nanduri

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


//This class is used to open and parse the gps file.
public class GPSparser {
    String filePath;
    BufferedReader br;
    List<GPScoordinates> gps;
    private int i = 0;


    public  GPSparser(String fp) {
        //stores the file path and creates a new linked list to handle the P2G2.GPScoordinates objects
        filePath = fp;
        gps = new LinkedList<GPScoordinates>();
    }
    //This method opens the file reads all of the related data then returns it as a linked list of P2G2.GPScoordinates
    public List<GPScoordinates> readCoords() throws IOException {
        GPScoordinates temp = null;
        if(br == null)
        {
            try {
                //sets up a new BufferedReader if br was null. uses the path variable to open the file with FileReader
                br = new BufferedReader(new FileReader(filePath));
            }
            catch(Exception e){
               System.out.print(e) ;
            }

        }

        String line;


        //Loops until the next needed frame is found in the file or it reaches the end of the file.

        try {
            while(true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                if (line.indexOf(";") != -1) {
                    String tempLine = line.trim();
                    String[] tempLineArray = tempLine.split(", ");
                    tempLineArray[1] = tempLineArray[1].replace(";","");
                    GPScoordinates gpsCoordinates = new GPScoordinates(tempLineArray[0], tempLineArray[1], i);
                    gps.add(gpsCoordinates);
                    //System.out.println("Offset " + gpsCoordinates.getoffset());
                    //System.out.println("Latitude and Longitude " + tempLineArray[0] + " " + tempLineArray[1]);
                    i += 1000;
                }
            }
            
        } catch (IOException e) {
            System.out.print(e);
        }finally {
           // System.out.println("in finally "+br);
            if(br!=null){
                try{
                    br = null;

                }catch (Exception e){
                    br = null;
                    System.out.println("Exception occured while closing ");
                }
            }

        }
        return gps;
    }
 }




