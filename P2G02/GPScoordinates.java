package P2G02;//Author: Vamsy Krishna Nanduri

public class GPScoordinates {
    private  String Latitude;
    private  String Longitude;
    private  int offset;

    //This is an object that is used to store the lat, long, and offset of the GPS coordinates
    public GPScoordinates(String Latitude, String Longitude, int offset){
        this. Latitude = Latitude;
        this. Longitude = Longitude;
        this. offset = offset;
    }
//these are getters and setters for the variables listed above
    public void setLatitude(String la)
    {
        Latitude = la;
    }

    public String getLatitude(){return Latitude;}

    public void setLongitude(String lo)
    {
        Longitude = lo;
    }

    public String getLongitude(){return Longitude;}



    public int getoffset(){return offset;}



//A custom print method to aid in testing of this object. it pretty prints the variables to the console
    public void print(){
        System.out.println("Latitude: " + Latitude);
        System.out.println("Longitude: "+ Longitude);
        System.out.println("Time offset: "+ offset);
        System.out.println();
    }
}