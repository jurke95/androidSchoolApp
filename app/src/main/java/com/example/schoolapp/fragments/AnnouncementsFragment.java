package com.example.schoolapp.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Config.MyApplication;
import com.example.schoolapp.R;
import com.example.schoolapp.activities.SchoolProvider;
import com.example.schoolapp.adapters.AnnouncementsAdapter;
import com.example.schoolapp.sync.HttpGetRequest;
import com.example.schoolapp.Config.MyApplication;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;

public class AnnouncementsFragment extends Fragment {

    public static AnnouncementsFragment newInstance() {

        AnnouncementsFragment mpf = new AnnouncementsFragment();
        return mpf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_announcements,
                container, false);

        ArrayList<String> listOfTitlesAndPersons = new ArrayList<>();
        ArrayList<String> listOfDescriptions = new ArrayList<>();
        ArrayList<String> listOfTime = new ArrayList<>();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Announcement");

        try{
            Uri uri = Uri.parse(SchoolProvider.CONTENT_URI_ANNOUNCEMENT+"/all");
            Cursor cursor = getActivity().getContentResolver().query(uri,null, null, null,null);
            if(cursor!=null){
                try{
                    while(cursor.moveToNext()){
                        listOfTitlesAndPersons.add(cursor.getString(cursor.getColumnIndexOrThrow("TITLE"))+" by "+cursor.getString(cursor.getColumnIndexOrThrow("PERSON_ID")));
                        listOfDescriptions.add(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")));
                        listOfTime.add(cursor.getString(cursor.getColumnIndexOrThrow("TIME")));
                    }
                }finally {
                    cursor.close();
                }
            }
        }catch (Exception ex){

        }


        RecyclerView recyclerView =rootView.findViewById(R.id.pm);
        AnnouncementsAdapter adapter = new AnnouncementsAdapter(getActivity(), listOfTitlesAndPersons, listOfDescriptions, listOfTime);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
