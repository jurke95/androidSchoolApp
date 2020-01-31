package com.example.schoolapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.schoolapp.R;
import com.google.android.material.navigation.NavigationView;

public class UserProfileActivity extends AppCompatActivity {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        Toolbar toolbar = findViewById(R.id.toolbar_user_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Student info");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("student_name")){
            String imageUrl = getIntent().getStringExtra("image_url");
            String studentName = getIntent().getStringExtra("student_name");

            TextView name = findViewById(R.id.student_name);
            name.setText(studentName);

            ImageView image = findViewById(R.id.image);
            Glide.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .into(image);
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

        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);

        switch(itemId){
            case R.id.nav_announcements:
                intent.putExtra("fragment", "1");
                break;
            case R.id.nav_class:
                intent.putExtra("fragment", "2");
                break;
            case R.id.nav_subjects:
                intent.putExtra("fragment", "3");
                break;
            default:
                intent.putExtra("fragment", "1");
        }
        startActivity(intent);
        finish();
    }

}
