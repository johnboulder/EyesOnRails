package com.wherethismove.eyesonrails;

/**
 * Created by stockweezie on 3/2/2016.
 */
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * TODO
 */

public class GetPageJsonTask extends AsyncTask<String, Void, String>
{

    private GetPageDataCallback mFragmentCallBack;

    public GetPageJsonTask(GetPageDataCallback fragment)
    {
        mFragmentCallBack = fragment;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String url = params[0];
        String charset = "UTF-8";
        String response = "";

        try
        {
            URLConnection c = new URL(url).openConnection();
            c.setRequestProperty("Accept-Charset", charset);
            InputStream res = c.getInputStream();

            BufferedReader bReader = new BufferedReader(new InputStreamReader(res, "utf-8"), 8);
            StringBuilder sBuilder = new StringBuilder();

            String line;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            res.close();
            response = sBuilder.toString();
        }
        catch (Exception e)
        {
            Log.e("GetPageJsonTask", "Error converting result");
            e.printStackTrace();
            return null;
        }

        return response;
    }

    @Override
    protected void onProgressUpdate(Void... values)
    {

    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        if(result != null)
        {
            // Process it into a json and return it
            try
            {
                JSONArray jArray = new JSONArray(result);
                mFragmentCallBack.callback(jArray);
            }
            catch (JSONException e)
            {
                Log.e("JSONException", "Error: " + e.toString());
            }
        }
        else
        {
            // Call the error function
            mFragmentCallBack.error();
        }
    }

    public interface GetPageDataCallback
    {
        void callback(JSONArray pageData);
        void error();
    }
}
