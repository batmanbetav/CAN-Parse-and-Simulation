package P2G02;//Author: Kace Curtis
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;

public class CANParser {
    String filePath;
    BufferedReader br;
    LinkedList<CANFrame> CANList;

    //Constructor that takes a file path to the can trc file
    //It also creates a new LinkedList for the CAN frames
    public CANParser(String fp)
    {
        filePath = fp;
        CANList = new LinkedList<CANFrame>();
    }
    //Uses a buffered reader to read through the CAN trc file and parse the needed frames
    //Then adds the parsed frame to the CANList and returns the CANFrame
    public LinkedList<CANFrame> getAllMessages()
    {
        CANList = null;
        CANList = new LinkedList<CANFrame>();
        CANFrame temp = null;
        boolean getNext = true;
        if(br == null)
        {
            try {
                //sets up a new BufferedReader if br was null. uses the path variable to open the file with FileReader
                br = new BufferedReader(new FileReader(filePath));
            }catch(Exception e)
            {
                return null;
            }
        }


        String line;
        //Loops until the next needed frame is found in the file or it reaches the end of the file.
        while(getNext) {
            try {
                //Read the next line in file
                line = br.readLine();
                if (line == null) {
                    getNext =  false;
                    return null;
                }
                //Skips over lines that contain ";" as they are  comments and not needed to be parsed
                if (line.indexOf(";") == -1) {
                    String tempLine = line.trim();
                    String[] tempLineArray = tempLine.split("\\s+");
                    //Switch checks to see if the frame is one of three needed
                    switch (tempLineArray[3]) {
                        case "0003":
                            temp = createSWF(tempLineArray);
                            CANList.add(temp);

                            break;
                        case "019F":
                            temp = createVSF(tempLineArray);
                            CANList.add(temp);
                            break;
                        case "0245":
                            temp = createVGF(tempLineArray);
                            CANList.add(temp);
                            break;
                        default:
                            break;
                    }

                }
            } catch (Exception e) {
                break;
            }
        }
        return CANList;

    }

    public CANFrame getNextMessage()
    {
        CANFrame temp = null;
        if(br == null)
        {
            try {
                //sets up a new BufferedReader if br was null. uses the path variable to open the file with FileReader
                br = new BufferedReader(new FileReader(filePath));
            }catch(Exception e)
            {
                return null;
            }
        }

        boolean getNext = true;
        String line;
        //Loops until the next needed frame is found in the file or it reaches the end of the file.
        while(getNext) {
            try {
                //Read the next line in file
                line = br.readLine();
                if (line == null) {
                    return null;
                }
                //Skips over lines that contain ";" as they are  comments and not needed to be parsed
                if (line.indexOf(";") == -1) {
                    String tempLine = line.trim();
                    String[] tempLineArray = tempLine.split("\\s+");
                    //Switch checks to see if the frame is one of three needed
                    switch (tempLineArray[3]) {
                        case "0003":
                            temp = createSWF(tempLineArray);
                            CANList.add(temp);

                            return temp;
                        case "019F":
                            temp = createVSF(tempLineArray);
                            CANList.add(temp);
                            return temp;
                        case "0245":
                            temp = createVGF(tempLineArray);
                            CANList.add(temp);
                            return temp;
                        default:
                            break;
                    }

                }
            } catch (Exception e) {
                return null;
            }
        }




        return temp;

    }
    //Sets the buffered reader back to null after the file is closed
    //Sets the CANList to null to wipe out any stored data.
    public void resetNextMessage(){
        if(br != null)
        {
            try {
                br.close();
            }
            catch (Exception e)
            {
                br = null;
            }
        }
        CANList = null;
        br = null;
        CANList = new LinkedList<CANFrame>();

    }
    //Loops through the CANList and uses the CANFrame print to pretty print all stored frames
    //to the screen
    public void printTrace() {

        for(int i =0; i < CANList.size();i++)
        {
            CANList.get(i).print();
            System.out.println();
        }

    }
    //Returns the linkedlist of the CANFrames
    public LinkedList<CANFrame> getTrace(){
        return CANList;
    }
    //this creates P2G2.SteeringWheelAngleFrame and then populates it with the correct data
    public SteeringWheelAngleFrame createSWF(String[] dataArr){
        //separates the data field from the rest of the information
        String[] data = Arrays.copyOfRange(dataArr,5,5+Integer.parseInt(dataArr[4]));

        SteeringWheelAngleFrame tempFrame = new SteeringWheelAngleFrame();

        //Calculates the angle of the steering wheel
        int parsedInt = Integer.parseInt(dataArr[5]+dataArr[6],16);
        double calcDouble = (parsedInt*0.5)-2048;

        //Setting the vars in the frame
        tempFrame.setAngle(String.valueOf(calcDouble));
        tempFrame.setOffset(dataArr[1]);
        tempFrame.setData(data);
        tempFrame.setID(dataArr[3]);


        //returning the frame
        return tempFrame;
    }
    //used to create P2G2.VehicleSpeedFrame Object and populate variables
    public VehicleSpeedFrame createVSF(String[] dataArr) {
        VehicleSpeedFrame tempFrame = new VehicleSpeedFrame();
        //separates the data field from the rest of the information
        String[] data = Arrays.copyOfRange(dataArr,5,5+Integer.parseInt(dataArr[4]));

        //Calculating speed data into the correct format
        int parsedInt = Integer.parseInt(dataArr[5]+dataArr[6],16);
        double calcDouble = (parsedInt*0.1);

        //Setting the vars in the frame
        tempFrame.setOffset(dataArr[1]);
        tempFrame.setData(data);
        tempFrame.setID(dataArr[3]);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        tempFrame.setSpeed( decimalFormat.format(calcDouble));

        return tempFrame;
    }
    //used to create P2G2.VehicleGyroFrame Object and populate variables
    public VehicleGyroFrame createVGF(String[] dataArr) {
        VehicleGyroFrame tempFrame = new VehicleGyroFrame();
        String[] data = Arrays.copyOfRange(dataArr,5,5+Integer.parseInt(dataArr[4]));
        //Calculating the Yaw
        int parsedIntYaw = Integer.parseInt(dataArr[5]+dataArr[6],16);
        double calcDoubleYaw = (parsedIntYaw*0.01)-327.68;

        //Calculating the Longitude Acceleration
        int parsedIntLonAccel = Integer.parseInt(dataArr[9],16);
        double calcDoubleLonAccel = (parsedIntLonAccel*0.08)-10.24;

        //Calculating the Lateral Acceleration
        int parsedIntLatAccel = Integer.parseInt(dataArr[10],16);
        double calcDoubleLatAccel = (parsedIntLatAccel*0.08)-10.24;

        //used to format the decimal
        DecimalFormat df = new DecimalFormat("0.00");

        //set frame variables
        tempFrame.setYaw(String.valueOf(df.format(calcDoubleYaw)));
        tempFrame.setLonAccel(String.valueOf(df.format(calcDoubleLonAccel)));
        tempFrame.setLatAccel(String.valueOf(df.format(calcDoubleLatAccel)));
        tempFrame.setOffset(dataArr[1]);
        tempFrame.setData(data);
        tempFrame.setID(dataArr[3]);

        return tempFrame;
    }

}
