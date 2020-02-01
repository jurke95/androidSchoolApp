package com.example.schoolapp.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolapp.R;
import com.example.schoolapp.adapters.SubjectsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SubjectsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubjectsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private HashMap<String, String> dataSubj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("USAOO U CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        recyclerView = (RecyclerView) findViewById(R.id.subjects_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dataSubj = new HashMap<String, String>();
        mAdapter = new SubjectsAdapter(this, dataSubj);
        recyclerView.setAdapter(mAdapter);
        new SubjectsTask().execute("subjects");
      }
        private class SubjectsTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {
                String result = setSubjects();
               return result;
            }
        }

        String setSubjects(){
            System.out.println("USAOO U SET SUBJECTS");
            String res;
            Uri uri = Uri.parse(SchoolProvider.CONTENT_URI_CLASS_PERSON +"/id");
            Cursor cursor = getContentResolver().query(uri,null,null, null,null);
            if(cursor == null){
                System.out.println("JBG NULL JE CURSOR");
            }else{
                System.out.println("PA NIJE IPAK NULL CURSOR");
            }
            if( cursor != null){
                System.out.println("USAO SAM U OVAJ IFFFFF");
                cursor.moveToFirst();
                res = cursor.getString(cursor.getColumnIndex("MARKS"));
                System.out.println(res);
                cursor.close();
                try {

                    JSONObject jObject = new JSONObject(res.toString());
                    Iterator<?> keys = jObject.keys();

                    while( keys.hasNext() ){
                        String key = (String)keys.next();
                        String value = jObject.getString(key);
                        dataSubj.put(key, value);
                        mAdapter.updateDataset(dataSubj);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return  "subjects";
        }




    }

