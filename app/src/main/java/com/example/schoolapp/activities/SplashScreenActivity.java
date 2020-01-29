package com.example.schoolapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolapp.Config.MyApplication;
import com.example.schoolapp.R;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private DatabaseHelper schoolDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        schoolDatabase = new DatabaseHelper(this);
        schoolDatabase.getWritableDatabase();

        String serverIpAddress = ((MyApplication) this.getApplication()).getServerIpAddress();
        new InitTask().execute(serverIpAddress);
    }

    private class InitTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {


            String result = initStudents(params[0]);
            if(result == "UserLogin")
            {
                result = initAnnouncements(params[0]);
            }



            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if(result == "UserLogin")
            {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
            else
            {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }

        }

    }

    //inicijalizacija studenata
    public String initStudents(String serverIpAddress)
    {
        SystemClock.sleep(2000);
        String result;
        String inputLine;

        SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tokenPreferences = getPreferences.getString("token",null);

        if(tokenPreferences != "" && tokenPreferences != null)
        {

            try {
                //Create a URL object holding our url
                URL myUrl = new URL(serverIpAddress + "Student/GetStudentsFromMyClass");
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();

                JSONObject tokenDetail = new JSONObject(tokenPreferences);
                String token = tokenDetail.getString("token");

                //JWT
                connection.addRequestProperty("Authorization", "Bearer " + token);

                //Set methods and timeouts
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);

                //Connect to our url
                connection.connect();

                //Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();


                JSONArray jsonArray = new JSONArray(result);

                for(int i = 0; i<jsonArray.length(); i++)
                {
                    JSONObject userProfile = jsonArray.getJSONObject(i);

                    ContentValues values = new ContentValues();
                    values.put("ID",userProfile.getString("id"));
                    values.put("FIRSTNAME",userProfile.getString("firstName"));
                    values.put("LASTNAME",userProfile.getString("lastName"));
                    values.put("PHONENUMBER",userProfile.getString("phoneNumber"));
                    values.put("IMAGEURL",userProfile.getString("imageUrl"));
                    getContentResolver().insert(SchoolProvider.CONTENT_URI_PERSON,values);



                }

            }
            catch(Exception e){
                e.printStackTrace();
                result = null;
            }
            return "UserLogin";
        }
        return "UserNotLogin";

    }

    //inicijalizacija obavestenja
    public String initAnnouncements(String serverIpAddress)
    {
        return "UserLogin";
    }

}
