import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.SocketException;
/**
 * This class is responsible to start the application.
 * 
 * @author ADK
 * @version v1.0.1741
 */
public class Start
{
    private static Scanner reader = new Scanner(System.in);
    private static Client client;  
    public static boolean online = false;
    /**
     * The application starts execution from this method.
     * Console object is created and started from this method.
     * It catches and handles UnknownHostException and SocketException thrown by Client()
     * 
     */
    public static void main(String[] args)
    {
        try
        {
            //PROMPT FOR USERNAME, SERVER IP AND CONNECTION UDP PORT
            System.out.println("Enter a username : ");
            String userName = reader.nextLine();
            System.out.println("Enter server address (IP) : ");
            String serverIP = reader.nextLine();
            System.out.println("Enter UDP connection port : (i.e. 4501)");
            int udpPort = Integer.parseInt(reader.nextLine());
            System.out.println("Enter TCP connection port : (i.e. 6501)");
            int tcpPort = Integer.parseInt(reader.nextLine());

            /* TEST INPUT
            //String userName = "john";
            //String serverIP = "192.168.1.8";
            //int udpPort = 4501;
            //int tcpPort = 6501;
            //System.out.println("Username : " + userName);
            //System.out.println("Server   : " + serverIP);
            //System.out.println("UDP Port : " + udpPort);
            //System.out.println("TCP Port : " + tcpPort);
             */

            //CREATE CLIENT OBJECT
            client = new Client(userName, serverIP, udpPort, tcpPort);
            client.updateMyVideoList();
            printMenu();
        }
        catch(UnknownHostException e)
        {
            System.out.println(
                "============================================\n" +
                "UnknownHostException in main : " +
                "\n" + e + 
                "\n============================================");
        }
        catch(SocketException e)
        {
            System.out.println(
                "============================================\n" +
                "SocketException in main : " +
                "\n" + e + 
                "\n============================================");
        }
        catch(IOException e)
        {
            System.out.println(
                "============================================\n" +
                "IOException in main : " +
                "\n" + e + 
                "\n============================================");
        }
    }

    /**
     * A method to print console UI menu
     * 
     */
    public static void printMenu()
    {
        while(true)
        {
            //SET STATUS STRING FOR MENU
            String status;
            if(online){status = "Online";} else {status = "Offline";}

            //PRINT MENU
            System.out.println("" +
                "\n============================================" +
                "\nStatus: " + status + 
                "\n============================================" +
                "\n[1] Connect to server" +
                "\n[2] Display online video list" +
                "\n[3] Download a video" +
                "\n[4] Refresh local video list" +
                "\n[5] Display local video list" +
                "\n[6] Add file to local video list" +
                "\n[7] Remove file from local video list" +
                "\n[8] Disconnect from server" +
                "\n[0] Exit" +
                "\nSelect option : " +
                "\n============================================");

            //READ USER INPUT FOR SELECTION
            int menuSelection = Integer.parseInt(reader.nextLine());
            switch (menuSelection) 
            {
                case 1: goOnline(); break;
                case 2: printOnlineVideos(); break;
                case 3: requestVideo(); break;
                case 4: client.updateMyVideoList(); break;
                case 5: printLocalVideos(); break;
                case 6: addLocalVideo(); break;
                case 7: removeLocalVideo(); break;
                case 8: goOffline(); break;
                case 0: return;
            }
        }
    }

    /**
     * A UI method to connect server and start listening
     * It catches and handles IOException and SocketException thrown by Client.goOnline()
     * 
     */
    public static void goOnline()
    {
        try
        {
            //CONNECT TO SERVER
            client.goOnline();
            System.out.println("Connected to server");
        }
        catch(IOException e)
        {
            System.out.println("Connection to server failed");
            System.out.println(
                "============================================\n" +
                "IOException in Client.goOnline : " +
                "\n" + e + 
                "\n============================================");
        }
    }

    /**
     * A UI method to disconnect connect from server
     * It catches and handles IOException and SocketException thrown by Client.goOffline()
     *
     */
    public static void goOffline()
    {
        try
        {
            //DISCONNECT FROM SERVER
            client.goOffline();
            System.out.println("Disconnected from server");
        }
        catch(IOException e)
        {
            System.out.println("Disconnection from server failed");
            System.out.println(
                "============================================\n" +
                "IOException in Client.goOffline : " +
                "\n" + e + 
                "\n============================================");
        }
    }

    /**
     * A UI method to refresh video list and send it to server
     *
     */
    public static void refreshLocalVideos()
    {
        //UPDATE LOCAL VIDEOS FROM FOLDER
        client.updateMyVideoList();
        //UPDATE LOCAL VIDEOS LIST TO SERVER
        client.sendMyVideoList();
    }

    /**
     * A UI method to print online videos list
     *
     */
    public static void printOnlineVideos()
    {
        //UPDATE ONLINE VIDEOS LIST TO CONSOLE
        System.out.println(
            "============================================\n" +
            "ONLINE VIDEO LIST" + 
            "\n============================================");
        for(Video v : client.getOnlineVideoList())
        {
            System.out.println(v.getOwnerINET() + " : " + v.getFileName());
        }
        System.out.print("============================================");
    }

    /**
     * A UI method to print local videos list
     *
     */
    public static void printLocalVideos()
    {
        //UPDATE LOCAL VIDEOS LIST TO CONSOLE
        System.out.println(
            "============================================\n" +
            "LOCAL VIDEO LIST" + 
            "\n============================================");
        for(Video v : client.getMyVideoList())
        {
            System.out.println(v.getFileName());
        }
        System.out.print("============================================");
    }

    /**
     * A UI method to add a new local video
     *
     */
    public static void addLocalVideo()
    {
        System.out.println(
            "============================================\n" +
            "ADD LOCAL VIDEO TO LIST" + 
            "\n============================================\n" + 
            "Enter filename : ");
        //PROMPT FOR FILE NAME
        String fileName = reader.nextLine();
        client.addVideo(fileName);
        client.sendMyVideoList();
    }

    /**
     * An overloaded method to add a new local video for VideoDownloader class
     *
     */
    public static void addLocalVideo(String fileName)
    {
        client.addVideo(fileName);
        client.sendMyVideoList();
    }

    /**
     * A UI method to remove a local video
     *
     */
    public static void removeLocalVideo()
    {
        System.out.println(
            "============================================\n" +
            "REMOVE LOCAL VIDEO FROM LIST" + 
            "\n============================================");
        //DISPLAY EXISTING LOCAL VIDEOS IN LIST
        for(Video v : client.getMyVideoList())
        {
            System.out.println(v.getFileName());
        }
        //PROMPT FOR FILE NAME
        System.out.println("Enter filename : ");
        String fileName = reader.nextLine();
        //FIND THE VIDEO
        for(int i = 0; i < client.getMyVideoList().size(); i++)
        {
            if(fileName.equals(client.getMyVideoList().get(i).getFileName()))
            {
                //REMOVE VIDEO IF FILE NAME FOUND
                client.getMyVideoList().remove(i);
            }
            else
            {
                //DISPLAY ERROR IF FILE NAME NOT FOUND
                System.out.println("Video not found!");  
            }
        }
        //SEND UPDATED LIST TO SERVER
        client.sendMyVideoList();
    }

    /**
     * A UI method to request a video from another client
     *
     */
    public static void requestVideo()
    {
        System.out.println(
            "============================================\n" +
            "REQUEST A VIDEO" + 
            "\n============================================");
        //DISPLAY ONLINE VIDEOS
        for(Video v : client.getOnlineVideoList())
        {
            System.out.println(v.getOwnerINET() + " : " + v.getFileName());
        }
        //PROMPT FOR FILE NAME
        boolean found = false;   
        System.out.println("");
        System.out.println("\nPlease choose the video you would like to download/Stream: ");
        //LOOK FOR VIDEO BY NAME
        do
        { 
            String fileName = reader.nextLine();
            Video v = client.findOnlineVideo(fileName);
            if (v != null)
            {
                client.requestVideo(v);
                found = true;
            }
            else
            {
                System.out.println("Video does not exist. Please try again.");
                found = false;
            }    
        } while (found == false);
    }
}
