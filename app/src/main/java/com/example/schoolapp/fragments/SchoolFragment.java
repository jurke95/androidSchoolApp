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

import com.example.schoolapp.activities.SchoolProvider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.schoolapp.R;


public class SchoolFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private double longitude;
    private double latitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_school,
                container, false);

        mMapView = (MapView) rootView.findViewById(R.id.user_list_map);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarTextView);
        textView.setText("School");

        String SchoolName = "";
        String Description = "";
        String Location = "";

        try {

            Uri uri = Uri.parse(SchoolProvider.CONTENT_URI_ACADEMY + "");
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {

                    while (cursor.moveToNext()) {

                        SchoolName = (cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
                        Description = (cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")));
                        Location = (cursor.getString(cursor.getColumnIndexOrThrow("LOCATION")));
                    }
                } finally {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        TextView schoolname = (TextView) getActivity().findViewById(R.id.schoolNameTextView);
//        schoolname.setText(SchoolName);
//
//        TextView schooldescription = (TextView) getActivity().findViewById(R.id.school_description);
//        schooldescription.setText(Description);

        if(!Location.equals(""))
        {
            String[] paresLocation = Location.split(",");
            latitude = Double.parseDouble(paresLocation[0]);
            longitude = Double.parseDouble(paresLocation[1]);
        }



        initMaps(savedInstanceState);

        return rootView;
    }

    private void initMaps(Bundle savedInstanceState)
    {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
