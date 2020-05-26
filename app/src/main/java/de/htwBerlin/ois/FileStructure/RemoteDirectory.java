package de.htwBerlin.ois.FileStructure;

import java.io.Serializable;

/**
 * Class do describe a directory from the FTP Server
 *
 * @author WilliBoelke
 */
public class RemoteDirectory implements Serializable
{

    //------------Instance Variables------------

    private String filename;
    private String path;
    private String creationDate;


    //------------Constructors------------

    public RemoteDirectory(String path, String creationDate)
    {
        this.filename = path.replace("_", " ");
        this.path = path;
        this.creationDate = creationDate;
    }


    //------------Setter------------

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getPath()
    {
        return path;
    }


    //------------Getter------------

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(String creationDate)
    {
        this.creationDate = creationDate;
    }
}