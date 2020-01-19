package com.example.schoolapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;
import com.example.schoolapp.adapters.MyAdapter;
import com.example.schoolapp.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

public class StudentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

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

}