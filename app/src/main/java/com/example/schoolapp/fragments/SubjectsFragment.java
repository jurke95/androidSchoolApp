package com.example.schoolapp.fragments;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;
import com.example.schoolapp.activities.SchoolProvider;
import com.example.schoolapp.activities.SubjectsActivity;
import com.example.schoolapp.adapters.RecyclerViewAdapter;
import com.example.schoolapp.adapters.SubjectsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class SubjectsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SubjectsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private HashMap<String, String> dataSubj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_subjects,
                container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("My subjects");

        recyclerView = rootView.findViewById(R.id.subjects_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        dataSubj = new HashMap<String, String>();
        mAdapter = new SubjectsAdapter(getActivity(), dataSubj);
        recyclerView.setAdapter(mAdapter);
        new SubjectsTask().execute("subjects");

        return rootView;
    }

    public class SubjectsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = setSubjects();
            return result;
        }
    }

    String setSubjects(){
        String res;
        Uri uri = Uri.parse(SchoolProvider.CONTENT_URI_CLASS_PERSON +"/id");
        Cursor cursor = getActivity().getContentResolver().query(uri,null,null, null,null);
        if( cursor.moveToFirst()){

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