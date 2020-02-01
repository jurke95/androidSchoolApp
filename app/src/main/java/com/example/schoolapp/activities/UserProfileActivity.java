package com.example.schoolapp.activities;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
            default:
                intent.putExtra("fragment", "1");
        }
        startActivity(intent);
        finish();
    }

}
