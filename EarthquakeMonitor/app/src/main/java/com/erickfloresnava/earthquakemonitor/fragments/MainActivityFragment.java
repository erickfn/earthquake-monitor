package com.erickfloresnava.earthquakemonitor.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.erickfloresnava.earthquakemonitor.adapters.EarthQuakesAdapter;
import com.erickfloresnava.earthquakemonitor.beans.EarthquakeBean;
import com.erickfloresnava.earthquakemonitor.R;
import com.erickfloresnava.earthquakemonitor.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivityFragment extends Fragment {

    private Activity activity;
    private RecyclerView rvEarthquakes;
    private EarthQuakesAdapter adapter;
    private LinearLayoutManager llManager;
    private ArrayList<EarthquakeBean> listEarthquakes = new ArrayList<>();
    private TextView tvTitle;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    private static final int TASK_CYCLE = 1000 * 60; // every minute
    private AsyncTask earthQuakesTask = null;
    private Timer timer;

    public static String FRAGMENT_TAG = "MainFragment";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragment = inflater.inflate(R.layout.fragment_main, container, false);

        initComponents(myFragment);
        initAdapter();
        initData();

        return myFragment;
    }

    private void initComponents(View myFragment) {
        activity = getActivity();

        ((AppCompatActivity)activity).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if(activity.findViewById(R.id.action_map) != null)
            activity.findViewById(R.id.action_map).setVisibility(View.VISIBLE);

        rvEarthquakes = (RecyclerView) myFragment.findViewById(R.id.rv);
        llManager = new LinearLayoutManager(activity);

        rvEarthquakes.setLayoutManager(llManager);
        rvEarthquakes.setHasFixedSize(true);

        tvTitle = (TextView) myFragment.findViewById(R.id.tvTitle);
        progressBar = (ProgressBar) myFragment.findViewById(R.id.progressBar);
        swipeRefresh = (SwipeRefreshLayout) myFragment.findViewById(R.id.swipe_container);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    private void initData() {

        if(timer != null)
            timer.cancel();

        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        listEarthquakes = new ArrayList<>();

                        // check if you are connected or not
                        if(Utils.isConnected(activity)){
                            earthQuakesTask = new GetEarthQuakesTask().execute(getString(R.string.service_usgs));
                        }
                        else{
                            Toast.makeText(activity, activity.getString(R.string.msg_connection), Toast.LENGTH_LONG).show();
                            swipeRefresh.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            tvTitle.setText(getString(R.string.msg_connection));
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, TASK_CYCLE);  // interval of one minute
    }

    private void initAdapter() {
        adapter = new EarthQuakesAdapter(activity, listEarthquakes);
        rvEarthquakes.setAdapter(adapter);

        tvTitle.setVisibility(View.VISIBLE);
        rvEarthquakes.setVisibility(View.VISIBLE);
    }

    private void updateAdapter() {
        adapter.setData(listEarthquakes);
        adapter.notifyDataSetChanged();
    }

    private class GetEarthQuakesTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Log.i("MainActivityFragment", "Getting data...");
        }

        @Override
        protected String doInBackground(String... urls) {

            return Utils.GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.GONE);
            swipeRefresh.setRefreshing(false);

            if(result != null) {
                parseData(result);
            }
            else {
                Toast.makeText(activity, activity.getString(R.string.no_service), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void parseData(String result) {
        try {
            String title;

            JSONObject objResult = new JSONObject(result);


            if(objResult.has("metadata")) {
                JSONObject objMetadata = objResult.getJSONObject("metadata");
                if(objMetadata.has("title")) {
                    title = objMetadata.getString("title");
                    tvTitle.setText(title);
                }
            }
            if(objResult.has("features")) {
                JSONArray arrayFeatures = objResult.getJSONArray("features");
                for(int i=0; i<arrayFeatures.length(); i++) {

                    String eqTitle = "";
                    String eqMag = "";
                    String eqPlace = "";
                    String eqTime = "";
                    ArrayList<String> alEqCoordinates = new ArrayList<>();

                    JSONObject objEarthQuake = arrayFeatures.getJSONObject(i);

                    if(objEarthQuake.has("properties")) {
                        JSONObject objProperties = objEarthQuake.getJSONObject("properties");

                        if(objProperties.has("mag"))
                            eqMag = objProperties.getString("mag");
                        if(objProperties.has("place"))
                            eqPlace = objProperties.getString("place");
                        if(objProperties.has("time"))
                            eqTime = objProperties.getString("time");
                        if(objProperties.has("title"))
                            eqTitle = objProperties.getString("title");
                    }

                    if(objEarthQuake.has("geometry")) {
                        JSONObject objGeo = objEarthQuake.getJSONObject("geometry");

                        if(objGeo.has("coordinates")) {
                            JSONArray arrayCoordinates = objGeo.getJSONArray("coordinates");

                            for(int j=0; j<arrayCoordinates.length(); j++)
                                alEqCoordinates.add(arrayCoordinates.getString(j));
                        }
                    }

                    listEarthquakes.add(new EarthquakeBean(eqTitle, eqMag, eqPlace, eqTime, alEqCoordinates));
                }

                if(arrayFeatures.length() == 0)
                    tvTitle.setText(getString(R.string.no_data));

                updateAdapter();
            }
            else {
                tvTitle.setText(getString(R.string.data_error));
            }

        }catch (JSONException jsonException){
            Toast.makeText(activity, activity.getString(R.string.data_error), Toast.LENGTH_LONG).show();
            tvTitle.setText(getString(R.string.data_error));
        }
    }

    public ArrayList<EarthquakeBean> getAlEarthQuakes() {
        return listEarthquakes;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(earthQuakesTask != null) {
            earthQuakesTask.cancel(true);
            timer.cancel();
        }
    }
}
