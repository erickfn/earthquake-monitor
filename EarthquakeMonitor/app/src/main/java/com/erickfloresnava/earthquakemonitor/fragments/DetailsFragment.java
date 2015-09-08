package com.erickfloresnava.earthquakemonitor.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.erickfloresnava.earthquakemonitor.R;
import com.erickfloresnava.earthquakemonitor.adapters.EarthQuakesAdapter;
import com.erickfloresnava.earthquakemonitor.beans.EarthquakeBean;
import com.erickfloresnava.earthquakemonitor.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class DetailsFragment extends Fragment implements OnMapReadyCallback{

    private Activity activity;
    private ProgressBar progressBar;
    private TextView tvMagnitude;
    private TextView tvPlace;
    private TextView tvDate;
    private TextView tvCoordinateValues;
    private EarthquakeBean earthquakeData;
    private SupportMapFragment mapFragment;

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragment = inflater.inflate(R.layout.fragment_details, container, false);

        initComponents(myFragment);
        initData();

        return myFragment;
    }

    private void initComponents(View myFragment) {
        activity = getActivity();

        ((AppCompatActivity)activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.findViewById(R.id.action_map).setVisibility(View.GONE);

        progressBar = (ProgressBar) myFragment.findViewById(R.id.progressBar);
        tvMagnitude = (TextView) myFragment.findViewById(R.id.tvMagnitude);
        tvPlace = (TextView) myFragment.findViewById(R.id.tvPlace);
        tvDate = (TextView) myFragment.findViewById(R.id.tvDate);
        tvCoordinateValues = (TextView) myFragment.findViewById(R.id.tvCoordinateValues);

        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
    }

    private void initData() {
        Bundle bundle = getArguments();
        earthquakeData = (EarthquakeBean) bundle.getSerializable("earthquakeData");

        progressBar.setVisibility(View.GONE);

        tvMagnitude.setText(earthquakeData.getMag());
        tvPlace.setText(earthquakeData.getPlace());

        String date = getDate(Long.parseLong(earthquakeData.getTime()), "MMM dd, yyyy HH:mm:ss");
        date = date.substring(0, 1).toUpperCase() + date.substring(1);
        tvDate.setText(date);

        String coordinateValues = "";

        for(int i=0; i<earthquakeData.getAlCoordinates().size(); i++) {
            if(i==0)
                coordinateValues += "Lon: ";
            else if(i==1)
                coordinateValues += "Lat: ";
            else
                coordinateValues += "Depth: ";

            coordinateValues += earthquakeData.getAlCoordinates().get(i);

            if((i+1) < earthquakeData.getAlCoordinates().size())
                coordinateValues += "\n";
        }

        tvCoordinateValues.setText(coordinateValues);

        /* Magnitude color */
        float magnitude = Float.valueOf(earthquakeData.getMag());

        if(magnitude < 0.0)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.eq0));
        else if(magnitude >= 0.0 && magnitude <=0.9)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.earthquake_easy));
        else if(magnitude > 0.9 && magnitude < 2.0)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.eq2));
        else if(magnitude >= 2.0 && magnitude < 3.0)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.eq3));
        else if(magnitude >= 3.0 && magnitude < 4.0)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.eq4));
        else if(magnitude >= 4.0 && magnitude < 5.0)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.eq5));
        else if(magnitude >= 5.0 && magnitude < 6.0)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.eq6));
        else if(magnitude >= 6.0 && magnitude < 7.0)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.eq7));
        else if(magnitude >= 7.0 && magnitude < 8.0)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.eq8));
        else if(magnitude >= 8.0 && magnitude < 9.0)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.eq9));
        else if(magnitude >= 9.0 && magnitude <= 9.9)
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.earthquake_hard));
        else
            tvMagnitude.setTextColor(activity.getResources().getColor(R.color.eqn));
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        double lon = Double.valueOf(earthquakeData.getAlCoordinates().get(0));
        double lat = Double.valueOf(earthquakeData.getAlCoordinates().get(1));

        LatLng epicenter = new LatLng(lat, lon);

        /* Magnitude color */
        float magnitude = Float.valueOf(earthquakeData.getMag());
        float color;

        if(magnitude < 0.0)
            color = 287f;
        else if(magnitude >= 0.0 && magnitude <=0.9)
            color = 130f;
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
            .title(earthquakeData.getTitle())
            .icon(BitmapDescriptorFactory.defaultMarker(color))
        );

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(epicenter, 5.0f));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
