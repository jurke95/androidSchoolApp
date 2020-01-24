package com.example.schoolapp.activities;

import android.content.Context;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        recyclerView = (RecyclerView) findViewById(R.id.subjects_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dataSubj= new HashMap<String, String>();
        mAdapter = new SubjectsAdapter(this,dataSubj);
        recyclerView.setAdapter(mAdapter);
        System.out.println("Stigaoooo");

        String URL = "http://192.168.0.13:5000/api/getMark";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE");
                        Log.e("Rest Response", response.toString());
                        System.out.println(response);
                        try {

                            JSONObject jObject = new JSONObject(response.toString());
                            Iterator<?> keys = jObject.keys();

                            while( keys.hasNext() ){
                                String key = (String)keys.next();
                                String value = jObject.getString(key);
                                dataSubj.put(key, value);

                            }

                            mAdapter.updateDataset(dataSubj);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    },
                    new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("ERROR");
                            Log.e("Rest Response", error.toString());
                        }
                    }
                    );

        objectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(objectRequest);




    }
}
