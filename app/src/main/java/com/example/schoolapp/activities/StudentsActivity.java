package com.example.schoolapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;
import com.example.schoolapp.adapters.MyAdapter;
import com.example.schoolapp.adapters.RecyclerViewAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

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

        ArrayList<String> students = new ArrayList<>();
        ArrayList<String> imagesUrl = new ArrayList<>();

        imagesUrl.add("https://drive.google.com/uc?export=view&id=1FoCT_SqnjeQLzjfadE217kvneNLBYdsL");
        students.add("Nikola Nikolic");
        imagesUrl.add("https://drive.google.com/uc?export=view&id=1yUN6KSfw7PAfPbr3j262K6McFn9q1nFB");
        students.add("Mara Maric");
        imagesUrl.add("https://drive.google.com/uc?export=view&id=1FoCT_SqnjeQLzjfadE217kvneNLBYdsL");
        students.add("Ivana Ivic");
        imagesUrl.add("https://drive.google.com/uc?export=view&id=1yUN6KSfw7PAfPbr3j262K6McFn9q1nFB");
        students.add("Stevan Setvanic");
        imagesUrl.add("https://drive.google.com/uc?export=view&id=1FoCT_SqnjeQLzjfadE217kvneNLBYdsL");
        students.add("Student5");
        imagesUrl.add("https://drive.google.com/uc?export=view&id=1yUN6KSfw7PAfPbr3j262K6McFn9q1nFB");
        students.add("Student6");
        imagesUrl.add("https://drive.google.com/uc?export=view&id=1FoCT_SqnjeQLzjfadE217kvneNLBYdsL");
        students.add("Student7");
        imagesUrl.add("https://drive.google.com/uc?export=view&id=1yUN6KSfw7PAfPbr3j262K6McFn9q1nFB");
        students.add("Student8");
        imagesUrl.add("https://drive.google.com/uc?export=view&id=1FoCT_SqnjeQLzjfadE217kvneNLBYdsL");
        students.add("Student9");
        imagesUrl.add("https://drive.google.com/uc?export=view&id=1yUN6KSfw7PAfPbr3j262K6McFn9q1nFB");
        students.add("Student10");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, students, imagesUrl);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){

            case R.id.nav_class:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_subjects:
                Intent s = new Intent(this, SubjectsActivity.class);
                startActivity(s);
                break;
            case R.id.nav_announcements:
                Intent a = new Intent(this, MainActivity.class);
                startActivity(a);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}