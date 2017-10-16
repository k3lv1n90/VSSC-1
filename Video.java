import java.net.InetAddress;
/**
 * Video class represents a video file in client's device
 *
 * @author ADK
 * @version v1.0.1741
 */
public class Video
{
    private String fileName;
    private InetAddress owner;

    /**
     * Constructor for objects of class Video
     * 
     * @param fileName the name of the video file
     * @param owner the owner of the video file
     */
    public Video(String fileName, InetAddress owner)
    {
        this.fileName = fileName;
        this.owner = owner;
    }

    /**
     * An accessor method to return the name of the video file
     *
     * @return String fileName the name of the video file
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * An accessor method to return the host of file as InetAddress
     *
     * @return InetAddress owner the host of file
     */
    public InetAddress getOwnerINET()
    {
        return owner;
    }

    /**
     * An accessor method to return the host of file as String
     *
     * @return String owner.getHostAddress() the host of file
     */
    public String getOwnerSTR()
    {
        return owner.getHostAddress();
    }

    /**
     * A method to return details of video file
     *
     * @return String "fileName,owner"
     */
    public String toString()
    {
        return fileName + "," + getOwnerSTR();
    }
}
