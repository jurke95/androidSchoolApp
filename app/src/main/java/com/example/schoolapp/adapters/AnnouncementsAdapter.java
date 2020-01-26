package com.example.schoolapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.schoolapp.R;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.ViewHolder> {

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private Context mContext;


    public AnnouncementsAdapter(Context context, ArrayList<String> titles, ArrayList<String> descriptions){
        this.titles=titles;
        this.descriptions=descriptions;
        this.mContext= context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_announcements, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.description.setText(descriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        RelativeLayout parentLayout;

        //ona cuva svaki widget u memoriji za svaki element te nase liste
        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.announcement_title);
            description = itemView.findViewById(R.id.announcement_description);
            parentLayout = itemView.findViewById(R.id.announcement_parent);
        }
    }
}
