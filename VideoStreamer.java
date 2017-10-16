import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
/**
 * VideoStreamer is the class that sends the video file
 * It is a Runnable class which triggered by ClientListener class.
 * 
 * @author ADK
 * @version v1.0.1741
 */
public class VideoStreamer implements Runnable
{
    private Socket socketPort;
    /**
     * Constructor for objects of class VideoStreamer
     * 
     * @param socketPort the TCP connection
     */
    public VideoStreamer(Socket socketPort)
    {
        this.socketPort = socketPort;
    }

    /**
     * Run method overrides the Runnable interface run() method.
     * 
     */
    public void run()
    {
        try
        {

            DataInputStream input = new DataInputStream(socketPort.getInputStream());
            String videoName = input.readUTF();

            //SPECIFY THE FILE AND CONVERT TO BUFFERED INPUT STREAM
            File file = new File("./videos/" + videoName);
            FileInputStream fIS = new FileInputStream(file);
            BufferedInputStream bIS = new BufferedInputStream(fIS); 

            //GET SOCKET'S OUPUT STREAM
            OutputStream oS = socketPort.getOutputStream();

            //READ FILE CONTENTS INTO CONTENTS ARRAY
            byte[] contents;
            long fileLength = file.length(); 
            long current = 0;
            long start = System.nanoTime();

            while(current!=fileLength){ 
                int size = 1000000;
                if(fileLength - current >= size)
                    current += size;    
                else{ 
                    size = (int)(fileLength - current); 
                    current = fileLength;
                } 
                contents = new byte[size]; 
                bIS.read(contents, 0, size); 
                oS.write(contents);                 
                //PRINT TRANSFER STATUS
                System.out.print("Sending file to "+ socketPort.getInetAddress().toString() +" " +(current*100)/fileLength+"% complete!\n");                
            }   
            System.out.println("File sent succesfully!");

            //FLUSH OUTPUT STREAM AND CLOSE SOCKET CONNECTION
            oS.flush(); 
            socketPort.close();
        }
        catch (IOException e)
        {
            System.out.println(
                "============================================\n" +
                "IOException in VideoStreamer : " +
                "\n" + e + 
                "\n============================================");
        }
    }
}
