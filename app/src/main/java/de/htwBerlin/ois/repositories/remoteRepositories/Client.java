package de.htwBerlin.ois.repositories.remoteRepositories;


import java.io.IOException;
import java.util.ArrayList;

import de.htwBerlin.ois.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;

public interface Client
{

    //------------Connection------------

    int connect();

    boolean isConnected();

    void closeConnection();


    //------------Listing------------

    ArrayList<RemoteFile> getAllFileList(String path) throws IOException;

    ArrayList<RemoteDirectory> getDirList(String path);

    /**
     * @param path
     * @return
     * @throws IOException
     */
    ArrayList<RemoteFile> getFileList(String path) throws IOException;


    //------------Downloading------------

    boolean downloadFile(String remoteFileName, String downloadPath);

}