package com.example.schoolapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;
import com.example.schoolapp.adapters.MyAdapter;

import java.util.ArrayList;

public class StudentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        recyclerView = (RecyclerView) findViewById(R.id.student_recycler_view);



        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> students = new ArrayList<>();
        students.add("Pera Peric");
        students.add("Pera Peric");
        students.add("Milan Mikic");
        students.add("Pera Peric");
        students.add("Milan Mikic");
        students.add("Pera Peric");
        students.add("Milan Mikic");
        students.add("Pera Peric");
        students.add("Milan Mikic");
        students.add("Pera Peric");
        students.add("Milan Mikic");
        students.add("Pera Peric");
        students.add("Milan Mikic");
        students.add("Pera Peric");
        students.add("Milan Mikic");
        students.add("Pera Peric");
        students.add("Milan Mikic");


        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this,students);
        recyclerView.setAdapter(mAdapter);
    }
}