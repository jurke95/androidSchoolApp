package com.example.schoolapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;
import com.example.schoolapp.adapters.SubjectsAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class SubjectsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        recyclerView = (RecyclerView) findViewById(R.id.subjects_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        HashMap<String, String> dataSubj = new HashMap<String, String>();
        dataSubj.put("maths","5,4,3,4,1");
        dataSubj.put("history","2,4,2,5,3");
        dataSubj.put("english","2,4,3,4,4");
        dataSubj.put("biology","2,4,1,3,1");
        dataSubj.put("chemistry","5,4,3,4,1");
        dataSubj.put("art","2,4,2,5,3");
        dataSubj.put("geography","2,4,3,4,4");
        dataSubj.put("physics","2,4,1,3,1");
        dataSubj.put("music","5,4,3,4,1");
        dataSubj.put("sports","2,4,2,5,3");
        dataSubj.put("spanish","2,4,3,4,4");
        dataSubj.put("french","2,4,1,3,1");
        dataSubj.put("serbian","5,4,3,4,1");
        dataSubj.put("informatics","2,4,2,5,3");
        dataSubj.put("driving","2,4,3,4,4");
        dataSubj.put("chess","2,4,1,3,1");

        mAdapter = new SubjectsAdapter(this,dataSubj);
        recyclerView.setAdapter(mAdapter);


    }
}
