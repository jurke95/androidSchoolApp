package com.example.schoolapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.schoolapp.R;
import com.example.schoolapp.activities.MainActivity;
import com.example.schoolapp.activities.SchoolProvider;
import com.example.schoolapp.activities.UserProfileActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter"; //ovo je za debugging

    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> imagesUrl = new ArrayList<>();
    private ArrayList<String> studentId = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context) {
        this.mContext = context;

        try {

            Uri uri = Uri.parse(SchoolProvider.CONTENT_URI_PERSON + "/all");
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {

                    while (cursor.moveToNext()) {

                        students.add(cursor.getString(cursor.getColumnIndexOrThrow("FIRSTNAME")) + " " +  cursor.getString(cursor.getColumnIndexOrThrow("LASTNAME")));
                        imagesUrl.add(cursor.getString(cursor.getColumnIndexOrThrow("IMAGEURL")));
                        studentId.add(cursor.getString(cursor.getColumnIndexOrThrow("ID")));
                    }
                } finally {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //ovaj metod je odgovoran za punjenje view-a
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    //u ovoj metodi zamenjujem podatke view koji je izasao iz liste sa podacima novog view koji treba da se prikaze u listi
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap() //govori glideu da hocemo da bude bitmap
                .load(imagesUrl.get(position))  //hocemo ovaj url
                .into(holder.image);  //stavljamo sliku u ImageView

        holder.imageName.setText(students.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on" + students.get(position));
                //Toast.makeText(mContext, students.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("student_id", studentId.get(position));
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    public class  ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        //ona cuva svaki widget u memoriji za svaki element te nase liste
        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.student_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


}
