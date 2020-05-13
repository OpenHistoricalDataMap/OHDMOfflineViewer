package de.htwBerlin.ois.FTP;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;


public class HTTPRequestNewMap extends AsyncTask<Void, Void, String>
{
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
    private final String TAG = this.getClass().getSimpleName();
    private String date;
    private String coordinates;
    private String name;
    private HttpURLConnection client;
    private AsyncResponse delegate;
    private URL url;


    public HTTPRequestNewMap(String date, String coordinates, String name)
    {
        this.date = date;
        this.name = name;
        this.coordinates = coordinates;
    }

    @Override
    protected void onPreExecute()
    {
        Log.i(TAG, "onPreExecute: ");
        try
        {
            url = new URL("http://10.0.2.2:8080/request");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        super.onPreExecute();
    }


    private void request()
    {
        String response = null;
        try
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(this.buildParamsString());
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null)
                {
                    response += line;
                }
            }
            else
            {
                response = "";

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.printf("response");
    }

    private String buildParamsString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("name=");
        sb.append(this.name);
        sb.append("&coords=");
        sb.append(this.coordinates);
        sb.append("&date=");
        sb.append(this.date);

        return sb.toString();
    }

    @Override
    protected String doInBackground(Void... params)
    {
        request();
        return null;
    }

}