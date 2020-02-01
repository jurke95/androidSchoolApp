package com.example.schoolapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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

import com.example.schoolapp.R;
import com.example.schoolapp.adapters.RecyclerViewAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class StudentsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ArrayList<String> students = new ArrayList<>();
        ArrayList<String> imagesUrl = new ArrayList<>();


        try {

            Uri uri = Uri.parse(SchoolProvider.CONTENT_URI_PERSON + "/all");
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {

                    while (cursor.moveToNext()) {

                        students.add(cursor.getString(cursor.getColumnIndexOrThrow("FIRSTNAME")) + cursor.getString(cursor.getColumnIndexOrThrow("LASTNAME")));
                        imagesUrl.add(cursor.getString(cursor.getColumnIndexOrThrow("IMAGEURL")));
                    }
                } finally {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       // RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, students, imagesUrl);
        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
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
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

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
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent s = new Intent(this, SettingsActivity.class);
                startActivity(s);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}