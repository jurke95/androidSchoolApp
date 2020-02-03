package com.example.schoolapp.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

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

        if (getIntent().hasExtra("student_id")) {
            String student_id = getIntent().getStringExtra("student_id");
            String studentName = "";
            String studentPhone = "";
            String studentImage = "";

            try {

                Uri uri = Uri.parse(SchoolProvider.CONTENT_URI_PERSON + "/" + student_id);
                Cursor cursor = getContentResolver().query(uri, null, student_id, null, null);
                if (cursor != null) {
                    try {

                        while (cursor.moveToNext()) {

                            studentName = (cursor.getString(cursor.getColumnIndexOrThrow("FIRSTNAME")) + " " + cursor.getString(cursor.getColumnIndexOrThrow("LASTNAME")));
                            studentImage = (cursor.getString(cursor.getColumnIndexOrThrow("IMAGEURL")));
                            studentPhone = (cursor.getString(cursor.getColumnIndexOrThrow("PHONENUMBER")));
                        }
                    } finally {
                        cursor.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //info o useru u navigation view
            SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);
            String personPreferences = getPreferences.getString("person",null);
            JSONObject personDetail = null;
            String firstAndLastName ="";
            String email = "";
            String imageUrl = "";
            try {
                personDetail = new JSONObject(personPreferences);
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

            //

            TextView name = findViewById(R.id.student_name);
            name.setText(studentName);

            TextView phoneNumber = findViewById(R.id.student_phone_number);
            phoneNumber.setText(studentPhone);

            ImageView image = findViewById(R.id.image);
            Glide.with(this)
                    .asBitmap()
                    .load(studentImage)
                    .into(image);
        }
    }

    @Override
    public void onBackPressed() {
/*        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        intent.putExtra("fragment", "2");
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        intent.putExtra("fragment", "2");
        startActivity(intent);
        finish();
        return true;
    }

    private void setupDrawerContent(NavigationView navigationView) {
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

    public void selectDrawerItem(int itemId) {

        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);

        switch (itemId) {
            case R.id.nav_announcements:
                intent.putExtra("fragment", "1");
                break;
            case R.id.nav_class:
                intent.putExtra("fragment", "2");
                break;
            case R.id.nav_subjects:
                intent.putExtra("fragment", "3");
                break;
            case R.id.nav_schedule:
                intent.putExtra("fragment", "4");
                break;
            case R.id.nav_school:
                intent.putExtra("fragment", "5");
                break;
            case R.id.nav_logout:
                SharedPreferences preferences =PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();

                startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                finish();
            default:
                intent.putExtra("fragment", "1");
        }
        startActivity(intent);
        finish();
    }

}
