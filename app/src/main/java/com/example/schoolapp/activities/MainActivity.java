package com.example.schoolapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.schoolapp.R;
import com.example.schoolapp.fragments.AnnouncementsFragment;
import com.example.schoolapp.fragments.SchoolFragment;
import com.example.schoolapp.fragments.StudentsFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    //private DatabaseHelper schoolDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        schoolDatabase = new DatabaseHelper(this);
        schoolDatabase.getWritableDatabase();

        if(schoolDatabase.doesDatabaseExist(this,"schooldata.db")){
            System.out.println("Database is successfully created");
        }else{
            System.out.println("Database is not created");
        }*/
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        if(getIntent().hasExtra("fragment"))
        {
            String itemId = getIntent().getStringExtra("fragment");
            if(itemId == "1"){
                selectDrawerItem(R.id.nav_announcements);
            }
            else if(itemId == "2"){
                selectDrawerItem(R.id.nav_class);
            }
            else if(itemId == "3"){
                selectDrawerItem(R.id.nav_subjects);
            }
            else{
                selectDrawerItem(R.id.nav_announcements);
            }
        }
        else
        {
            selectDrawerItem(R.id.nav_announcements);
        }



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem.getItemId());
                        return true;
                    }
                }

        );
    }

    public void selectDrawerItem(int itemId){

        Fragment fragment=null;
        Class fragmentClass;
        switch(itemId){
            /*case R.id.nav_class:
                Intent intentStudents = new Intent(MainActivity.this,StudentsActivity.class);
                //intentStudents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentStudents);
                finish();
                break;
            case R.id.nav_subjects:
                Intent s = new Intent(this, SubjectsActivity.class);
                startActivity(s);
                break;*/
            case R.id.nav_announcements:
                fragmentClass = AnnouncementsFragment.class;
                break;
            case R.id.nav_class:
                fragmentClass = StudentsFragment.class;
                break;
            case R.id.nav_school:
                fragmentClass = SchoolFragment.class;
                break;
            default:
                fragmentClass = AnnouncementsFragment.class;
        }

        try{
            fragment = (Fragment)fragmentClass.newInstance();
        }catch(Exception e){

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        drawer.closeDrawers();
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
