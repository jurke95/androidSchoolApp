package com.example.schoolapp.fragments;

import android.database.Cursor;
import android.net.Uri;
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
import com.example.schoolapp.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

public class StudentsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_students,
                container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("My class");

        ArrayList<String> students = new ArrayList<>();
        ArrayList<String> imagesUrl = new ArrayList<>();

        try {

            Uri uri = Uri.parse(SchoolProvider.CONTENT_URI_PERSON + "/all");
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {

                    while (cursor.moveToNext()) {

                        students.add(cursor.getString(cursor.getColumnIndexOrThrow("FIRSTNAME")) + " " +  cursor.getString(cursor.getColumnIndexOrThrow("LASTNAME")));
                        imagesUrl.add(cursor.getString(cursor.getColumnIndexOrThrow("IMAGEURL")));
                    }
                } finally {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), students, imagesUrl);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
