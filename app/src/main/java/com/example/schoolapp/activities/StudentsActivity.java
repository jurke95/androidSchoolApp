package com.example.schoolapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Config.MyApplication;
import com.example.schoolapp.R;
import com.example.schoolapp.adapters.RecyclerViewAdapter;
import com.google.android.material.navigation.NavigationView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class StudentsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        Toolbar toolbar = findViewById(R.id.toolbar_students);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ArrayList<String> students = new ArrayList<>();
        ArrayList<String> imagesUrl = new ArrayList<>();


        GetStudentsAsync studentss = new GetStudentsAsync();

        try {
            String serverIpAddress = ((MyApplication) this.getApplication()).getServerIpAddress();
            String result = studentss.execute(serverIpAddress + "Student/GetStudentsFromMyClass").get();

            JSONArray jsonArray = new JSONArray(result);

            for(int i = 0; i<jsonArray.length(); i++)
            {
                JSONObject userProfile = jsonArray.getJSONObject(i);
                students.add(userProfile.getString("firstName") + " " + userProfile.getString("lastName"));
                imagesUrl.add(userProfile.getString("imageUrl"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, students, imagesUrl);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class GetStudentsAsync extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;

            SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(StudentsActivity.this);
            String name = getPreferences.getString("token",null);

            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();

                JSONObject tokenDetail = new JSONObject(name);
                String token = tokenDetail.getString("token");

                //JWT
                connection.addRequestProperty("Authorization", "Bearer " + token);

                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

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




            }
            catch(Exception e){
                e.printStackTrace();
                result = null;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStop(){
        super.onStop();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){

            case R.id.nav_class:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_subjects:
                Intent intent = new Intent(this, SubjectsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_announcements:
                Intent intentA = new Intent(StudentsActivity.this, MainActivity.class);
                //intentA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentA);
                finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                Intent s = new Intent(this, SettingsActivity.class);
                startActivity(s);
                break;

        }
        return super.onOptionsItemSelected(item);
    }





}