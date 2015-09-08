package com.erickfloresnava.earthquakemonitor.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erickfloresnava.earthquakemonitor.R;
import com.erickfloresnava.earthquakemonitor.beans.EarthquakeBean;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by erickfloresnava on 9/7/15.
 */
public class ResumeMapFragment extends Fragment implements OnMapReadyCallback {

    private Activity activity;
    private SupportMapFragment mapFragment;
    private ArrayList<EarthquakeBean> alEarthquakeData;

    public ResumeMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragment = inflater.inflate(R.layout.fragment_resume_map, container, false);

        initComponents(myFragment);
        initData();

        return myFragment;
    }

    private void initComponents(View myFragment) {
        activity = getActivity();

        ((AppCompatActivity)activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.findViewById(R.id.action_map).setVisibility(View.GONE);

        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.resumeMap));
        mapFragment.getMapAsync(this);
    }

    private void initData() {
        Bundle bundle = getArguments();
        alEarthquakeData = (ArrayList<EarthquakeBean>) bundle.getSerializable("earthquakesData");

    }

    @Override
    public void onMapReady(GoogleMap map) {

        LatLng lastEpicenter = new LatLng(0, 0);;

        for(int i=0; i<alEarthquakeData.size(); i++) {

            double lon = Double.valueOf(alEarthquakeData.get(i).getAlCoordinates().get(0));
            double lat = Double.valueOf(alEarthquakeData.get(i).getAlCoordinates().get(1));

            LatLng epicenter = new LatLng(lat, lon);

            /* Magnitude color */
            float magnitude = Float.valueOf(alEarthquakeData.get(i).getMag());
            float color;

            if(magnitude < 0.0)
                color = 287f;
            else if(magnitude >= 0.0 && magnitude <=0.9)
                color = 123f;
            else if(magnitude > 0.9 && magnitude < 2.0)
                color = 216f;
            else if(magnitude >= 2.0 && magnitude < 3.0)
                color = 54f;
            else if(magnitude >= 3.0 && magnitude < 4.0)
                color = 185f;
            else if(magnitude >= 4.0 && magnitude < 5.0)
                color = 206f;
            else if(magnitude >= 5.0 && magnitude < 6.0)
                color = 37f;
            else if(magnitude >= 6.0 && magnitude < 7.0)
                color = 235f;
            else if(magnitude >= 7.0 && magnitude < 8.0)
                color = 21f;
            else if(magnitude >= 8.0 && magnitude < 9.0)
                color = 267f;
            else if(magnitude >= 9.0 && magnitude <= 9.9)
                color = 0f;
            else
                color = 328f;

            map.addMarker(new MarkerOptions()
                .position(epicenter)
                .title(alEarthquakeData.get(i).getTitle())
                .icon(BitmapDescriptorFactory.defaultMarker(color))
            );

            lastEpicenter = epicenter;
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastEpicenter, 0f));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
