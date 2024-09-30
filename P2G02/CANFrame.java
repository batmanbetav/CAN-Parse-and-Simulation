//Author: Kace Curtis
package P2G02;
//This is the base class for the can frame
public class CANFrame {
    //These variables are for all the fields that are common between frames
    //The ID stores the ID of the fra,
    private String ID;
    //Stores the time offset
    private String offset;
    //Stores the data field in an array of strings
    private String[] data;


    //Getter and setters for the above variables
    public void setID(String i){
        ID = i;
    }
    public String getID(){
        return ID;
    }
    public void setOffset(String o){
        offset = o;
    }
    public String getOffset(){
        return offset;
    }
    public void setData(String[] d){
        data = d;
    }
    public String[] getData() {
        return data;
    }
    //Prints the values of the variables to the screen
    public void print(){
        System.out.println("ID: "+ID);
        System.out.println("offset: "+offset);
        System.out.println("data: "+data);
        System.out.println();
    }

}
