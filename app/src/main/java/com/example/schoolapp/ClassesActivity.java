package com.example.schoolapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);



        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> classes = new ArrayList<>();
        classes.add("I-1");
        classes.add("I-2");
        classes.add("I-3");
        classes.add("I-4");
        classes.add("I-5");

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this,classes);
        recyclerView.setAdapter(mAdapter);
    }
}
