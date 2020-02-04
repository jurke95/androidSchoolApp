package com.example.schoolapp.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schoolapp.Config.MyApplication;
import com.example.schoolapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public  class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static String serverIpAddress;
    private static SettingsActivity single_instance = null;
    private static Context context;
    private static DatabaseHelper schoolDatabase;



    public static SettingsActivity getInstance()
    {
        if (single_instance == null)
            single_instance = new SettingsActivity();

        return single_instance;
    }

    public static Context getAppContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        schoolDatabase = new DatabaseHelper(this);
        serverIpAddress = ((MyApplication) this.getApplication()).getServerIpAddress();
        context = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        if(findViewById(R.id.fragment_container)!=null)
        {
            if(savedInstanceState!=null)
                return;

            getFragmentManager().beginTransaction().add(R.id.fragment_container,new SettingsFragment()).commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);

        }


            @Override
            public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference preference) {

                String key = preference.getKey();
                System.out.println(key);
                if(key.equals("key_pref_sync")){
                    schoolDatabase.onUpgrade(schoolDatabase.getWritableDatabase(),1,1);
                    new SyncTask().execute(serverIpAddress);
                } else if(key.equals("key_enable_notifications")){

                    SwitchPreference switchPreference = (SwitchPreference) preference;
                    boolean enabled = switchPreference.isChecked();
                    String stringEnabled = Boolean.toString(enabled);
                    System.out.println("Da li je enableovano?");
                    SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(getAppContext());
                    String tokenPreferences = getPreferences.getString("token",null);
                    System.out.println(stringEnabled);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getAppContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("notifications",stringEnabled);
                    editor.apply();

                }
                return false;
            }
    }

    private static class SyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(getAppContext(),"Sync has started.",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            Looper.prepare();
            SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(getAppContext());
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



            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getAppContext(),"Sync has finished.",Toast.LENGTH_SHORT).show();
        }
    }

    public static String initStudents(String serverIpAddress, String tokenPreferences)
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
                    getAppContext().getContentResolver().insert(SchoolProvider.CONTENT_URI_PERSON,values);
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
    public static String initAnnouncements(String serverIpAddress, String tokenPreferences)
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
                    getAppContext().getContentResolver().insert(SchoolProvider.CONTENT_URI_ANNOUNCEMENT,values);
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
    public static String initSubjects(String  serverIpAddress, String tokenPreferences)
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
                getAppContext().getContentResolver().insert(SchoolProvider.CONTENT_URI_CLASS_PERSON,values);

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
}