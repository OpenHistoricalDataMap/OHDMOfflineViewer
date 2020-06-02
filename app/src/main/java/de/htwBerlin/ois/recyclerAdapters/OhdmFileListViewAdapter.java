package de.htwBerlin.ois.recyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.FtpTaskFileDownloading;

/**
 * ListView adapter that holds ohdmFiles
 * Since we started to use a recycler view we don't need that for now
 * I will let it stay here in case someone needs it in the Future// Willi
 *
 * @author morelly_t1
 */
@Deprecated
public class OhdmFileListViewAdapter extends ArrayAdapter<RemoteFile>
{

    private static final String TAG = "OhdmFileAdapter";

    private Context context;
    private int resource;

    public OhdmFileListViewAdapter(Context context, int resource, ArrayList<RemoteFile> ohdmFiles)
    {
        super(context, resource, ohdmFiles);
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final String fileName = getItem(position).getFilename();
        Long fileSize = getItem(position).getFileSize();
        String creationDate = getItem(position).getCreationDate();
        Boolean isDownloaded = getItem(position).isDownloaded();

        final RemoteFile ohdmFile = new RemoteFile(fileName, fileSize, creationDate, isDownloaded);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvFileName = convertView.findViewById(R.id.map_name_tv);
        TextView tvFileSize = convertView.findViewById(R.id.map_size_tv);
        TextView tvCreationDate = convertView.findViewById(R.id.date_of_creation_tv);


        final Button buttonDownloadFile = convertView.findViewById(R.id.buttonDownloadFile);
        final ProgressBar progressBar = convertView.findViewById(R.id.progressBar);

        buttonDownloadFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FtpTaskFileDownloading ftpTaskFileDownloading = new FtpTaskFileDownloading(context, "");
                Toast.makeText(getContext(), "Downloading " + fileName, Toast.LENGTH_SHORT).show();
                ftpTaskFileDownloading.execute(ohdmFile);
                disableButton(buttonDownloadFile);
            }
        });

        tvFileName.setText(fileName);
        tvFileSize.setText((int) (double) (fileSize / 1024) + " MB");
        tvCreationDate.setText(creationDate);

        if (isDownloaded) disableButton(buttonDownloadFile);

        return convertView;
    }

    /**
     * disables a button
     *
     * @param button
     */
    private void disableButton(Button button)
    {
        button.setEnabled(Boolean.FALSE);
    }
}


