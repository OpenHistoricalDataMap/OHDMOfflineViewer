package de.htwBerlin.ois.ServerCommunication;

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

import javax.net.ssl.HttpsURLConnection;

import static de.htwBerlin.ois.ServerCommunication.Variables.HTTP_PORT;
import static de.htwBerlin.ois.ServerCommunication.Variables.SERVER_IP;


/**
 * AsyncTask to make a HTTP Request to the Server
 * <p>
 * The request contains name, coordinates and a date.
 * The server will the create that map and make it available
 *
 * @author WilliBoelke
 */
public class HTTPRequestNewMap extends AsyncTask<Void, Void, String>
{

    private final String TAG = this.getClass().getSimpleName();
    private String date;
    private String coordinates;
    private String name;
    private URL url;

    /**
     * Public constructor
     *
     * @param date
     * @param coordinates
     * @param name
     */
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
            url = new URL("http://" + SERVER_IP + ":" + HTTP_PORT + "/request");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        super.onPreExecute();
    }

    /**
     * Builds a string as accepted by the server
     * example:
     * name=mapname&coords=13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123&date=2117-12-11
     *
     * @return the String
     */
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
        Log.i(TAG, "Server response : " + response);
        return null;
    }

}
