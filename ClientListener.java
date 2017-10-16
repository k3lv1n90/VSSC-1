import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
/**
 * ClientListener is the class that listens TCP port.
 * It is a Runnable class which triggered by Client class.
 * It triggers VideoStreamer Thread a video request received
 * 
 * @author ADK
 * @version v1.0.1741
 */
public class ClientListener implements Runnable
{
    private int tcpPort;
    private ServerSocket sSocket;
    /**
     * Constructor for objects of class ClientListener
     * 
     * @param tcpPort the TCP port of connection
     */
    public ClientListener(ServerSocket tcpServerSocket, int tcpPort)
    {
        this.sSocket = tcpServerSocket;
        this.tcpPort = tcpPort;
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
                Socket socket = sSocket.accept();
                //LISTEN FOR INCOMING REQUESTS AND STARTS VIDEOSTREAMER THREAD IF ANY REQUEST RECEIVED
                Thread videoStreamer = new Thread(new VideoStreamer(socket));
                videoStreamer.start();
            }
        }
        catch (IOException e)
        {
            //IOException caused by Socket.close() by Client class.         
            /*
            System.out.println(
                "============================================\n" +
                "IOException in ClientListener : " +
                "\n" + e + 
                "\n============================================");
            */
        }     
    }
}
