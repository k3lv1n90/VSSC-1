import java.util.ArrayList;
import java.io.File;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.net.ServerSocket;
/**
 * Client class represents the client.
 * Listens UDP and TCP port
 * There are three Threads triggered by client object: ServerListener, ClientListener and VideoDownloader
 * 
 * @author ADK
 * @version v1.0.1741
 */
public class Client
{
    private String userName;
    private String serverIP;
    private String myIP;
    private InetAddress serverAddress;
    private InetAddress myAddress;
    private int udpPort;
    private int tcpPort;
    private DatagramSocket outSocket;
    private DatagramSocket inSocket;
    private ServerSocket tcpServerSocket;
    private Thread serverListener;
    private Thread clientListener;
    private DatagramPacket inPacket;
    private byte[] buf;
    private ArrayList<Video> myVideos;
    private ArrayList<Video> onlineVideos;
    private File folder;
    private boolean online;
    private String outMsg;
    private String inMsg;
    /**
     * Constructor for objects of class Client
     * 
     * @param userName the user name of client
     * @param serverIP the IP address of server
     * @param updPort the UDP port of connection
     * @throws UnknownHostException, SocketException
     */
    public Client(String userName, String serverIP, int udpPort, int tcpPort) throws UnknownHostException, SocketException, IOException
    {
        this.userName = userName;
        this.serverIP = serverIP;
        this.serverAddress = InetAddress.getByName(serverIP);
        this.udpPort = udpPort;
        this.tcpPort = tcpPort;
        myAddress = InetAddress.getLocalHost();
        myIP = myAddress.getHostAddress();
        outSocket = new DatagramSocket();
        inSocket = new DatagramSocket(this.udpPort);
        tcpServerSocket = new ServerSocket(tcpPort);
        buf = new byte[1024];
        inPacket = new DatagramPacket(buf, buf.length);
        myVideos = new ArrayList<Video>();
        onlineVideos = new ArrayList<Video>();
        folder = new File("./videos");
        this.online = false;
    }

    /**
     * A method to connect to the server.
     * Sends CONN and receives ACK CONN (2-Way HandShake for Connection)
     * Starts Threads of ServerListener and ClientListener to listen UDP and TCP ports
     *
     * @throws IOException
     */
    public void goOnline() throws IOException
    {
        //CONN (HANDSHAKE FOR CONNECTION)
        String handShake = "I'm online!," + userName;
        DatagramPacket hSPacket1 = new DatagramPacket(handShake.getBytes(), handShake.getBytes().length, serverAddress, udpPort);
        outSocket.send(hSPacket1);

        //RECEIVE ACK CONN (HANDSHAKE FOR CONNECTION) : START THREADS
        inSocket.receive(inPacket);
        String inMsg = new String(inPacket.getData(), 0, inPacket.getLength());
        if(inMsg.equals("You are online!"))
        {
            Start.online = true;
            //SEND LOCAL VIDEO LIST
            sendMyVideoList();
            //START SERVERLISTERNER THREAD (UDP PORT LISTENER)
            serverListener = new Thread(new ServerListener(outSocket, inSocket, inPacket, buf, onlineVideos, myIP));
            serverListener.start();
            //START CLIENTLISTERNER THREAD (TCP PORT LISTENER)
            clientListener = new Thread(new ClientListener(tcpServerSocket, tcpPort));
            clientListener.start();
        }
    }

    /**
     * A method to update local videos list from videos folder
     *
     */
    public void updateMyVideoList()
    {
        //CLEAR ALL VIDEOS
        myVideos.clear();
        //GET FILE NAMES IN VIDEOS FOLDER
        File[] listOfFiles = folder.listFiles();
        //ADD VIDEOS TO ARRAYLIST
        for(int i = 0; i < listOfFiles.length; i++)
        {
            myVideos.add(new Video(listOfFiles[i].getName(), myAddress));
        }
    }

    /**
     * A method to send local videos list to server
     * Catches and handles IOException
     *
     */
    public void sendMyVideoList()
    {
        try
        {
            //SEND LOCAL VIDEO LIST
            DatagramPacket myVideosDP = new DatagramPacket(getMyVideosString().getBytes(), getMyVideosString().getBytes().length, serverAddress, udpPort);
            outSocket.send(myVideosDP);
        }
        catch(IOException e)
        {
            System.out.println(
                "============================================\n" +
                "IOException in Client.sendMyVideoList : " +
                "\n" + e + 
                "\n============================================");
        }
    }

    /**
     * A method to request online video from another client
     * Starts VideoDownloader Thread
     *
     * @param v video requested
     */
    public void requestVideo(Video v)
    {
        Thread requestVideo = new Thread(new VideoDownloader(v.getOwnerSTR(),v.getFileName(), tcpPort));
        requestVideo.run();
    }

    /**
     * A method to disconnect from server
     * Sends DISC to server (Disconnection HandShake)
     *
     * @throws IOException
     */
    public void goOffline() throws IOException
    {
        //DISC (HANDSHAKE FOR DISCONNECTION)
        String handShake = "bye!," + userName;
        DatagramPacket byePacket = new DatagramPacket(handShake.getBytes(), handShake.getBytes().length, serverAddress, udpPort);
        outSocket.send(byePacket);
        //STOP CLIENTLISTENER - CLIENTLISTENER WILL THROW IOEXCEPTION AND STOP
        tcpServerSocket.close();
    }

    /**
     * An accessor method to return the list of local videos
     *
     * @return ArrayList<Video> myVideos the list of local videos
     */
    public ArrayList<Video> getMyVideoList()
    {
        return myVideos;
    }

    /**
     * An accessor method to return the list of online videos as ArrayList
     *
     * @return ArrayList<Video> onlineVideos the list of online videos
     */
    public ArrayList<Video> getOnlineVideoList()
    {
        return onlineVideos;
    }

    /**
     * An accessor method to return the list of local videos as CSV
     *
     * @return String myVideosString the list of local videos as CSV
     */
    public String getMyVideosString()
    {
        String myVideosString = "myVideosList";
        for(Video v: myVideos)
        {
            myVideosString += "," + v.toString();
        } 
        return myVideosString;
    }

    /**
     * A method to add a new video to local videos list by file name
     *
     * @param fileName the name of the video file
     */
    public void addVideo(String fileName)
    {
        myVideos.add(new Video(fileName, myAddress));
    }

    /**
     * A method to find an online video by file name
     *
     * @param fileName the name of the video file
     * @return video the found video
     */
    public Video findOnlineVideo(String fileName)
    {
        Video video = null;
        for(Video v : onlineVideos)
        {
            if(v.getFileName().equals(fileName))
            {
                video = v;  
                break;
            }
        }
        return video;
    }
}
