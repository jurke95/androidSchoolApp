package com.example.schoolapp.fragments;

import android.content.SharedPreferences;
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

        ArrayList<String> listOfTitles = new ArrayList<>();
        ArrayList<String> listOfDescriptions = new ArrayList<>();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("Announcement");

        RecyclerView recyclerView =rootView.findViewById(R.id.pm);
        AnnouncementsAdapter adapter = new AnnouncementsAdapter(getActivity(), listOfTitles, listOfDescriptions);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        String serverIpAddress = MyApplication.getServerIpAddress();

        String myUrl = serverIpAddress + "api/GetAnnouncementFromMyClass";
        String result="";
        SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name = getPreferences.getString("token",null);


        try{
            JSONObject tokenDetail = new JSONObject(name);
            String token = tokenDetail.getString("token");

            HttpGetRequest getRequest = new HttpGetRequest();
            result=getRequest.execute(myUrl, token).get();
            System.out.println(result);

            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject announcementDetail = jsonArray.getJSONObject(i);
                listOfTitles.add(announcementDetail.getString("title"));
                listOfDescriptions.add(announcementDetail.getString("description"));
            }
        }
        catch(Exception ex){
            System.out.println("exception");
        }

        return rootView;
    }
}
