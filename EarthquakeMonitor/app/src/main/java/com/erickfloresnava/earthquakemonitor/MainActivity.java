package com.erickfloresnava.earthquakemonitor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.erickfloresnava.earthquakemonitor.beans.EarthquakeBean;
import com.erickfloresnava.earthquakemonitor.fragments.DetailsFragment;
import com.erickfloresnava.earthquakemonitor.fragments.MainActivityFragment;
import com.erickfloresnava.earthquakemonitor.fragments.ResumeMapFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actiobar_bg)));

        if(savedInstanceState == null) {
            FragmentActivity fragment = (FragmentActivity) this;
            FragmentTransaction transaction = fragment.getSupportFragmentManager().beginTransaction();

            // Create new fragment and transaction
            MainActivityFragment mainFragment = new MainActivityFragment();
            transaction.replace(R.id.fragment, mainFragment, MainActivityFragment.FRAGMENT_TAG);

            // Commit the transaction
            transaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.getSupportFragmentManager().popBackStack();
                return true;

            case R.id.action_map:

                MainActivityFragment mainFragment = (MainActivityFragment)getSupportFragmentManager().findFragmentByTag(MainActivityFragment.FRAGMENT_TAG);
                if(mainFragment != null) {
                    changeToSummaryMapFragment(mainFragment.getAlEarthQuakes());
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void changeToSummaryMapFragment(ArrayList<EarthquakeBean> alEarthQuakes) {
        ResumeMapFragment resumeMapFragment = new ResumeMapFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Send data
        Bundle bundles = new Bundle();
        bundles.putSerializable("earthquakesData", alEarthQuakes);
        resumeMapFragment.setArguments(bundles);

        /* Animation */
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_bottom);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment, resumeMapFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
