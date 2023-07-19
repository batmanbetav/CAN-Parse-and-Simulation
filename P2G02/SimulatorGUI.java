package P2G02; //Author: Vamsy Krishna Nanduri

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

//This is the main entry point for the entire simulator
// This is where the GUI is created and the simulation is started from
public class SimulatorGUI {
    private JButton runSimButton;
    private JPanel mainPanel;
    private JButton closeButton;
    private JLabel timeVar;
    private JLabel GPSLat;
    private JLabel GPSLon;
    private JLabel VehicleSPD;
    private JLabel Angle;
    private JLabel gyroLat;
    private JLabel GyroLong;
    private JLabel yawRateLabel;
    private JLabel yawRate;
    private JLabel Curve;


    public SimulatorGUI(LinkedList<CANFrame> canList, List<GPScoordinates> gps) {



        runSimButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //When the run sim button is clicked it creates a new simulation with the CANFrame list and GPS coordinates list
                //CANSimulation is threadable so a new theread is started
                //This is needed to stop the GUI from freezing;
                CANSimulation canSimulation = new CANSimulation(canList,gps);
                SensorDataReceiver sdr = new SensorDataReceiver(timeVar,GPSLat,GPSLon,VehicleSPD,Angle,gyroLat,GyroLong,yawRate,Curve);
                canSimulation.passSensorDataReceiver(sdr);
                canSimulation.passButton(runSimButton);
                runSimButton.setEnabled(false);
                canSimulation.start();






            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            //the close button simply just closes the application
            public void actionPerformed(ActionEvent e) {
                System.exit(0);

            }
        });
    }

    public static void main(String[] args) {
        //Open up the trace file and parse all the data in it and create related frames
        CANParser parser = new CANParser("19 CANmessages.trc");
        //Creating an empty CANFrame object
        CANFrame temp;
        LinkedList<CANFrame> canList =null;

        GPSparser gpsParser = new GPSparser("GPStrace.txt");
        List<GPScoordinates> gps = null;
        parser.getAllMessages();
        canList = parser.getTrace();

        try {
            // Open and parse the gps data
            gps= gpsParser.readCoords();
        }catch (IOException e){
            System.out.println("Error Occured");
        }

        //Set up the GUI and pack it.
        JFrame frame = new JFrame("SimulatorGUI");
        frame.setContentPane(new SimulatorGUI(canList,gps).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200,300));
        frame.pack();
        //This will display the GUI window
        frame.setVisible(true);


    }


}

