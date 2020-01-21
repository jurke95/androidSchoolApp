package com.example.schoolapp.fragments;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;
import com.example.schoolapp.adapters.AnnouncementsAdapter;
import com.example.schoolapp.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

public class AnnouncementsFragment extends Fragment {

    public static AnnouncementsFragment newInstance() {

        AnnouncementsFragment mpf = new AnnouncementsFragment();

        return mpf;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_announcements,
                container, false);

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        titles.add("First class");
        descriptions.add("desc1");
        titles.add("title2");
        descriptions.add("desc2");

        RecyclerView cardview =rootView.findViewById(R.id.pm);
        AnnouncementsAdapter adapter = new AnnouncementsAdapter(titles, descriptions);
        cardview.setAdapter(adapter);

        return rootView;
    }
}
