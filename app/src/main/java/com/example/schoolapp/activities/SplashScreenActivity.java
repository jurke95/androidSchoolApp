package com.example.schoolapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolapp.Config.MyApplication;
import com.example.schoolapp.R;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
        boolean dbExists = schoolDatabase.doesDatabaseExist(this, "schooldata.db");
        if(dbExists)
        {
            schoolDatabase.onUpgrade(schoolDatabase.getWritableDatabase(),1,1);
        }

        String serverIpAddress = ((MyApplication) this.getApplication()).getServerIpAddress();
        new InitTask().execute(serverIpAddress);
    }

    private class InitTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
            String tokenPreferences = getPreferences.getString("token",null);

            String result = initStudents(params[0], tokenPreferences);
            if(result == "UserLogin")
            {
                result = initAnnouncements(params[0], tokenPreferences);
            }
            if(result == "UserLogin")
            {
                result = initSubjects(params[0], tokenPreferences);
            }
            if(result == "UserLogin")
            {
                result=initSchool(params[0], tokenPreferences);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if(result == "UserLogin"){
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
            else{
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        }
    }

    //inicijalizacija studenata
    public String initStudents(String serverIpAddress, String tokenPreferences)
    {
        //SystemClock.sleep(2000);
        String result;
        String inputLine;

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
                return "UserNotLogin";
            }
            return "UserLogin";
        }
        return "UserNotLogin";

    }

    //inicijalizacija obavestenja
    public String initAnnouncements(String serverIpAddress, String tokenPreferences)
    {
        String result;
        String inputLine;

        if(tokenPreferences != "" && tokenPreferences != null)
        {
            try
            {
                URL myUrl = new URL(serverIpAddress + "api/GetAnnouncementFromMyClass");
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();

                JSONObject tokenDetail = new JSONObject(tokenPreferences);
                String token = tokenDetail.getString("token");
                connection.addRequestProperty("Authorization", "Bearer " + token);

                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);

                connection.connect();

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();

                JSONArray jsonArray = new JSONArray(result);


                for(int i = 0; i<jsonArray.length(); i++)
                {
                    JSONObject announcements = jsonArray.getJSONObject(i);

                    ContentValues values = new ContentValues();
                    values.put("ID",i+1);
                    values.put("TITLE",announcements.getString("title"));
                    values.put("DESCRIPTION",announcements.getString("description"));
                    values.put("TIME",announcements.getString("time"));
                    values.put("PERSON_ID",announcements.getString("createdBy"));
                    getContentResolver().insert(SchoolProvider.CONTENT_URI_ANNOUNCEMENT,values);
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
                return "UserNotLogin";
            }
            return "UserLogin";
        }
        return "UserNotLogin";
    }

    //inicijalizacija predmeta
    public String initSubjects(String  serverIpAddress, String tokenPreferences)
    {
        String result;
        String inputLine;

        if(tokenPreferences != "" && tokenPreferences != null)
        {

            try
            {
                URL myUrl = new URL(serverIpAddress + "api/GetSubjectsForUser");
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();

                JSONObject tokenDetail = new JSONObject(tokenPreferences);
                String token = tokenDetail.getString("token");
                connection.addRequestProperty("Authorization", "Bearer " + token);

                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);

                connection.connect();

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();

                JSONObject class_person = new JSONObject(result);
                ContentValues values = new ContentValues();
                values.put("PERSON_ID",class_person.getString("personId"));
                values.put("CLASS_ID",class_person.getString("classId"));
                values.put("MARKS",class_person.getString("grades"));
                getContentResolver().insert(SchoolProvider.CONTENT_URI_CLASS_PERSON,values);

            }
            catch(Exception e)
            {
                e.printStackTrace();
                return "UserNotLogin";
            }
            return "UserLogin";
        }
        return "UserNotLogin";
    }

    //inicijalizacija skole
    public String initSchool(String  serverIpAddress, String tokenPreferences){

        String result;
        String inputLine;

        if(tokenPreferences != "" && tokenPreferences != null){
            try{
                URL myUrl = new URL(serverIpAddress + "api/GetSchool");
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();

                JSONObject tokenDetail = new JSONObject(tokenPreferences);
                String token = tokenDetail.getString("token");
                connection.addRequestProperty("Authorization", "Bearer " + token);

                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);

                connection.connect();

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();

                JSONObject school = new JSONObject(result);
                ContentValues values = new ContentValues();
                values.put("ID",1);
                values.put("NAME",school.getString("name"));
                values.put("DESCRIPTION",school.getString("description"));
                values.put("LOCATION",school.getString("location"));
                getContentResolver().insert(SchoolProvider.CONTENT_URI_ACADEMY, values);

            }catch(Exception e){
                e.printStackTrace();
                return "UserNotLogin";
            }
            return "UserLogin";
        }
        return "UserNotLogin";
    }

}


