package com.example.schoolapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolapp.R;
import com.example.schoolapp.services.DownloadService;

public class ScheduleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_schedule,
                container, false);
        textView = (TextView) rootView.findViewById(R.id.status);

        rootView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickM(rootView);
            }
        });

        return rootView;
    }

    private TextView textView;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String string = bundle.getString(DownloadService.FILEPATH);
                int resultCode = bundle.getInt(DownloadService.RESULT);
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getActivity(),
                            "Download complete. Download URI: " + string,
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download done");
                } else {
                    Toast.makeText(getActivity(), "Download failed",
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download failed");
                }
            }
        }
    };

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        textView = (TextView) findViewById(R.id.status);
//
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(
                DownloadService.NOTIFICATION));
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    public void onClickM(View view) {

        Intent intent = new Intent(getActivity(), DownloadService.class);
        // add infos for the service which file to download and where to store
        intent.putExtra(DownloadService.FILENAME, "Schedule.html");
        intent.putExtra(DownloadService.URL,
                "https://drive.google.com/uc?export=download&id=1XLp16Zav6hWbSd0eK0YA05STjT6eF7u4");
        getActivity().startService(intent);
        textView.setText("Service started");
    }

}