import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
/**
 * VideoDownloader is the class that sends the video file
 * It is a Runnable class which triggered by Client class.
 * 
 * @author ADK
 * @version v1.0.1741
 */
public class VideoDownloader implements Runnable
{
    private String address;
    private String fileName;
    private int tcpPort;
    /**
     * Constructor for objects of class VideoDownloader
     * 
     * @param adress the IP adress of client video requested from
     * @param fileName the name of the video file
     */
    public VideoDownloader (String address, String fileName, int tcpPort)
    {
        this.address = address;
        this.fileName = fileName;
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
            //INITIALISE SOCKET
            Socket socket = new Socket(address, tcpPort);
            DataOutputStream output = new DataOutputStream( socket.getOutputStream());
            output.writeUTF(fileName);
            byte[] contents = new byte[1000000];
            //INITIALIZE THE FILEOUTPUTSTREAM TO THE OUTPUT FILE'S FULL PATH
            FileOutputStream fOS = new FileOutputStream("./videos/"+fileName);
            BufferedOutputStream bOS = new BufferedOutputStream(fOS);
            InputStream iS = socket.getInputStream();
            System.out.println("Receiving File");
            //PLAY VIDEO ON VLC PLAYER WHILE DOWNLOADING
            ProcessBuilder pB = new ProcessBuilder("C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe", "./videos/"+fileName);
            Process start = pB.start(); 
            //NO OF BYTES READ IN ONE READ() CALL
            int bytesRead = 0; 
            while((bytesRead=iS.read(contents))!=-1){
                System.out.println("Bytes Received: " + bytesRead);                       
                bOS.write(contents, 0, bytesRead); 
            }
            //FLUSH OUTPUT STREAM AND CLOSE SOCKET CONNECTION
            bOS.flush(); 
            socket.close(); 
            System.out.println("File saved successfully!");
            //ADD VIDEO TO LOCAL VIDEOS LIST AND SENT IT TO SERVER
            Start.addLocalVideo(fileName);
        }
        catch (UnknownHostException e)
        {
            System.out.println(
                "============================================\n" +
                "UnknownHostException in VideoDownloader : " +
                "\n" + e + 
                "\n============================================");
        }
        catch (IOException e)
        {
            System.out.println(
                "============================================\n" +
                "IOException in VideoDownloader : " +
                "\n" + e + 
                "\n============================================");
        }
    }    
}
