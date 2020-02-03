package com.example.schoolapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.schoolapp.R;
import com.example.schoolapp.fragments.AnnouncementsFragment;
import com.example.schoolapp.fragments.ScheduleFragment;
import com.example.schoolapp.fragments.SchoolFragment;
import com.example.schoolapp.fragments.StudentsFragment;
import com.example.schoolapp.fragments.SubjectsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);



        SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String tokenPreferences = getPreferences.getString("person",null);
        JSONObject personDetail = null;
        String firstAndLastName ="";
        String email = "";
        String imageUrl = "";
        try {
            personDetail = new JSONObject(tokenPreferences);
            firstAndLastName = personDetail.getString("firstAndLastName");
            email = personDetail.getString("email");
            imageUrl = personDetail.getString("imageUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View headerView = navigationView.getHeaderView(0);
        TextView tvfirstAndLastName = (TextView) headerView.findViewById(R.id.person_namee);
        tvfirstAndLastName.setText(firstAndLastName);

        TextView tvemail = (TextView) headerView.findViewById(R.id.person_email);
        tvemail.setText(email);

        CircleImageView ivimage = (CircleImageView) headerView.findViewById(R.id.person_image);
        Glide.with(this)
                .asBitmap() //govori glideu da hocemo da bude bitmap
                .load(imageUrl)  //hocemo ovaj url
                .into(ivimage);


        if(getIntent().hasExtra("fragment"))
        {
            String itemId = getIntent().getStringExtra("fragment");
            if(itemId.equals("1")){
                selectDrawerItem(R.id.nav_announcements);
            }
            else if(itemId.equals("2")){
                selectDrawerItem(R.id.nav_class);
            }
            else if(itemId.equals("3")){
                selectDrawerItem(R.id.nav_subjects);
            }
            else if(itemId.equals("4")){
                selectDrawerItem(R.id.nav_schedule);
            }
            else if(itemId.equals("5")){
                selectDrawerItem(R.id.nav_school);
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
            case R.id.nav_announcements:
                fragmentClass = AnnouncementsFragment.class;
                break;
            case R.id.nav_class:
                fragmentClass = StudentsFragment.class;
                break;
            case R.id.nav_school:
                if(isServicesOK()){
                    fragmentClass = SchoolFragment.class;
                }
                else{
                    fragmentClass = AnnouncementsFragment.class;
                }
                break;
            case R.id.nav_schedule:
                fragmentClass = ScheduleFragment.class;
                break;
            case R.id.nav_subjects:
                fragmentClass = SubjectsFragment.class;
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


    public boolean isServicesOK(){
        Log.d("MainActivity", "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d("MainActivity", "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d("MainActivity","isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, 9003);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
