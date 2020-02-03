package com.example.schoolapp.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.schoolapp.Config.MyApplication;
import com.example.schoolapp.R;
import com.example.schoolapp.adapters.ProfessorSubjectsAdapter;
import com.example.schoolapp.adapters.SubjectsAdapter;
import com.example.schoolapp.dialog.AddDialog;
import com.example.schoolapp.sync.HttpPostRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubjectsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfessorSubjectsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private HashMap<String, String> dataSubj;
    private String student_id;
    private String token;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_subjects);


        Toolbar toolbar = findViewById(R.id.toolbar_professor_subjects);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Subjects");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_professor);
        setupDrawerContent(navigationView);

        //info o useru u navigation view
        SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(SubjectsActivity.this);
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


        if (getIntent().hasExtra("student_id")) {
            student_id = getIntent().getStringExtra("student_id");

            String res;
            Uri uri = Uri.parse(SchoolProvider.CONTENT_URI_CLASS_PERSON +"/" + student_id);
            Cursor cursor = getContentResolver().query(uri,null,student_id, null,null);
            if( cursor.moveToFirst()){

                res = cursor.getString(cursor.getColumnIndex("MARKS"));
                System.out.println(res);
                cursor.close();
                try {

                    JSONObject jObject = new JSONObject(res.toString());
                    Iterator<?> keys = jObject.keys();
                    dataSubj = new HashMap<String, String>();
                    while( keys.hasNext() ){
                        String key = (String)keys.next();
                        String value = jObject.getString(key);

                        dataSubj.put(key, value);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //dataSubj = new HashMap<String, String>();
        mAdapter = new ProfessorSubjectsAdapter(this, dataSubj);
        recyclerView.setAdapter(mAdapter);

        String tokenPreferences = getPreferences.getString("token",null);

        JSONObject tokenDetail = null;
        try {
            tokenDetail = new JSONObject(tokenPreferences);
            token = tokenDetail.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button addButton = findViewById(R.id.save_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView = (RecyclerView) findViewById(R.id.rv);
                Map<String, String> grades = new HashMap<String, String>();
                int t = recyclerView.getChildCount();
                for (int i = mAdapter.getItemCount()-1; i >=0 ; i--) {
                    View recycview = recyclerView.getChildAt(i);
                    EditText gradesEditText = (EditText) recycview.findViewById(R.id.professor_subject_grades);
                    TextView subjectTextView = (TextView) recycview.findViewById(R.id.professor_subject_name);
                    String gradesValue = gradesEditText.getText().toString();
                    String subject = subjectTextView.getText().toString();
                    String subjectKey = subject.substring(0, subject.length() - 1);
                    String key = subjectKey;
                    String value = gradesValue;

                    grades.put(key, value);
                }

                //save grades in server
                String serverIpAddress = ((MyApplication) getApplication()).getServerIpAddress();
                Map<String, String> postData = new HashMap<>();
                JSONObject obj=new JSONObject(grades);
                JSONArray array=new JSONArray();
                try {
                    array=new JSONArray(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                postData.put("grades", obj.toString());
                postData.put("personidstring", student_id);


                HttpPostRequest task = new HttpPostRequest(postData,token);
                try {
                    task.execute(serverIpAddress + "api/ChangeGrades").get();
                }
                catch(Exception e){

                }

                //save grades to SQLite db

                try {
                    ContentValues values = new ContentValues();
                    values.put("MARKS",obj.toString());
                    Uri uri = Uri.parse(SchoolProvider.CONTENT_URI_CLASS_PERSON +"/update/"+student_id);
                    getContentResolver().update(uri,values,null,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(SubjectsActivity.this, MainActivity.class);
        intent.putExtra("fragment", "3");
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

        Intent intent = new Intent(SubjectsActivity.this, MainActivity.class);

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
                SharedPreferences preferences =PreferenceManager.getDefaultSharedPreferences(SubjectsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();

                startActivity(new Intent(SubjectsActivity.this, LoginActivity.class));
                finish();
            default:
                intent.putExtra("fragment", "1");
        }
        startActivity(intent);
        finish();
    }





}

