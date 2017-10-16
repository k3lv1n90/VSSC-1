import java.util.ArrayList;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * ListUpdater class is responsible for updating online videos ArrayList
 * It updates the list when a new list received from server
 * 
 * @author ADK
 * @version v1.0.1741
 */
public class ListUpdater implements Runnable
{
    private String inMsg;
    private String myIP;
    private ArrayList<Video> onlineVideos;
    /**
     * Constructor for objects of class ListUpdater
     * 
     * @param inMsg received String message from server
     * @param onlineVideos online videos list
     * @param myIP ip of client
     */
    public ListUpdater(String inMsg, ArrayList<Video> onlineVideos, String myIP)
    {
        this.inMsg = inMsg;
        this.onlineVideos = onlineVideos;
        this.myIP = myIP;
    }

    /**
     * Run method overrides the Runnable interface run() method.
     * Exceptions handled inside the run() method.
     * 
     */
    public void run()
    {
        try
        {
            //SPLITS THE CSV
            onlineVideos.clear();
            String[] v = inMsg.split(",");            
            for(int i = 0; i < v.length; i = i + 2)
            {
                //CHECKS IF THE VIDEO BELONGS TO THIS IP
                if(!(InetAddress.getByName(v[i+1]).equals(InetAddress.getLocalHost())))
                {
                   //ADDS VIDEO TO ONLINE VIDEOS ARRAYLIST
                   onlineVideos.add(new Video(v[i], InetAddress.getByName(v[i+1])));
                }
            }
            System.out.println("Online videos list updated!");
        }
        catch(UnknownHostException e)
        {
            System.out.println(
                "============================================\n" +
                "UnknownHostException in ListUpdater : " +
                "\n" + e + 
                "\n============================================");
        }
    }
}
