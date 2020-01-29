package com.example.schoolapp.sync;

import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpPostRequest extends AsyncTask<String, Void, String> {

    JSONObject postData;

    public HttpPostRequest(Map<String, String> postData){
        if(postData!=null){
            this.postData=new JSONObject(postData);
        }
    }

    @Override
    protected String doInBackground(String... params) {

        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        String stringUrl = params[0];
        String response="";

        try{
            URL myUrl = new URL(stringUrl);

            HttpURLConnection connection = (HttpURLConnection)myUrl.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "application/json");

            connection.setRequestMethod("POST");
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            int statusCode = connection.getResponseCode();
            if(statusCode == 200)
            {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                response = convertInputStreamToString(inputStream);
                return response;
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

        return "BadRequest";
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}
