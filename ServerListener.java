import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.io.IOException;
/**
 * ServerListener is the class that listens UDP port.
 * It is a Runnable class which triggered by Client class.
 * It triggers ListUpdater Thread if new online video list is received
 * 
 * @author ADK
 * @version v1.0.1741
 */
public class ServerListener implements Runnable
{
    private DatagramSocket outSocket;
    private DatagramSocket inSocket;
    private DatagramPacket inPacket;
    private String myIP;
    private byte[] buf;
    private ArrayList<Video> onlineVideos;
    private String inMsg;
    /**
     * Constructor for objects of class ConnectionAccepter
     * 
     * @param outSocket UDP socket for sending packets
     * @param inSocket UDP socket for receiving packets
     * @param inPacket received packet from server
     * @param buf buffer for incoming packets
     * @param onlineVideos online videos list
     * @param myIP ip of client
     */
    public ServerListener(DatagramSocket outSocket, DatagramSocket inSocket, DatagramPacket inPacket, byte[] buf, ArrayList<Video> onlineVideos, String myIP)
    {
        this.inSocket = inSocket;
        this.inPacket = inPacket;
        this.buf = buf;
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
            while(true)
            {
                //LISTEN FOR INCOMING PACKETS
                inSocket.receive(inPacket);
                //CONVERT RECEIVED PACKET TO STRING
                inMsg = new String(inPacket.getData(), 0, inPacket.getLength());
                //RECEIVED ACK DISC (HANDSHAKE FOR DISCONNECTION)
                if(inMsg.equals("You are offline!"))
                {
                    Start.online = false;
                    //CLOSE THE UDP SOCKETS
                    outSocket.close();
                    inSocket.close();
                    break;
                }
                else
                {
                    //RECEIVED NEW ONLINE VIDEO LIST : START LISTUPDATER THREAD
                    Thread listUpdater = new Thread(new ListUpdater(inMsg, onlineVideos, myIP));
                    listUpdater.start();
                }
            }
        }
        catch(IOException e)
        {
              System.out.println(
                "============================================\n" +
                "IOException in ServerListener : " +
                "\n" + e + 
                "\n============================================");
        }
    }
}