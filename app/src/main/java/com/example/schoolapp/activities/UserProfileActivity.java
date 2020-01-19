package com.example.schoolapp.activities;


import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.schoolapp.R;

public class UserProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Toolbar toolbar = findViewById(R.layout.toolbar);

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

}
