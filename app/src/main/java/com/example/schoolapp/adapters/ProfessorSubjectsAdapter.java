package com.example.schoolapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProfessorSubjectsAdapter extends RecyclerView.Adapter<ProfessorSubjectsAdapter.MyViewHolder>{



    private final ArrayList<Map.Entry<String, String>> subjData;
    private LayoutInflater mInflater;

    public ProfessorSubjectsAdapter(Context context, HashMap<String, String> map) {

        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        subjData = new ArrayList<Map.Entry<String, String>>(entrySet);

        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProfessorSubjectsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.professor_subject_row, parent, false);
        return new ProfessorSubjectsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessorSubjectsAdapter.MyViewHolder holder, int position) {
        String key = subjData.get(position).getKey();
        String value = subjData.get(position).getValue();
        holder.keyView.setText(key+":");
        holder.valueView.setText(value);
    }


    @Override
    public int getItemCount() {
        return subjData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView keyView;
        public EditText valueView;
        public MyViewHolder(View v) {
            super(v);
            keyView = v.findViewById(R.id.professor_subject_name);
            valueView = v.findViewById(R.id.professor_subject_grades);

        }
    }




}


