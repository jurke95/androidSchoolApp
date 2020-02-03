package com.example.schoolapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.schoolapp.Config.MyApplication;
import com.example.schoolapp.R;
import com.example.schoolapp.activities.SchoolProvider;
import com.example.schoolapp.activities.SplashScreenActivity;
import com.example.schoolapp.sync.HttpPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddDialog extends DialogFragment {

    String titletext;
    String desctext;
    String token;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_add, null);
        final EditText title = (EditText) view.findViewById(R.id.title_add);
        final EditText description = (EditText) view.findViewById(R.id.description_add);

        SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String tokenPreferences = getPreferences.getString("token",null);

        JSONObject tokenDetail = null;
        try {
            tokenDetail = new JSONObject(tokenPreferences);
            token = tokenDetail.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        builder.setView(view)
                .setCancelable(false)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //save announcement in server
                        String serverIpAddress = ((MyApplication) getActivity().getApplication()).getServerIpAddress();
                        Map<String, String> postData = new HashMap<>();
                        postData.put("title", title.getText().toString());
                        postData.put("description", description.getText().toString());
                        String announcement="";


                        HttpPostRequest task = new HttpPostRequest(postData,token);
                        try {
                            announcement=task.execute(serverIpAddress + "api/AddAnnouncement").get();
                        }
                        catch(Exception e){

                        }

                        //save announcement in SQLite DB
                        try {
                            JSONObject announcementObject = new JSONObject(announcement);
                            ContentValues values = new ContentValues();
                            values.put("TITLE",announcementObject.getString("title"));
                            values.put("DESCRIPTION",announcementObject.getString("description"));
                            values.put("TIME",announcementObject.getString("time"));
                            values.put("PERSON_ID",announcementObject.getString("createdBy"));
                            getActivity().getContentResolver().insert(SchoolProvider.CONTENT_URI_ANNOUNCEMENT,values);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }


}
