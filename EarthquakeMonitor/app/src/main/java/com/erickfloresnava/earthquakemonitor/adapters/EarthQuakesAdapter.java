package com.erickfloresnava.earthquakemonitor.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erickfloresnava.earthquakemonitor.beans.EarthquakeBean;
import com.erickfloresnava.earthquakemonitor.R;
import com.erickfloresnava.earthquakemonitor.fragments.DetailsFragment;
import com.erickfloresnava.earthquakemonitor.fragments.MainActivityFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erickfloresnava on 9/6/15.
 */
public class EarthQuakesAdapter extends RecyclerView.Adapter<EarthQuakesAdapter.EarthQuakeViewHolder> {

    private Context context;
    private List<EarthquakeBean> listEarthQuakes;

    public EarthQuakesAdapter(Context context, List<EarthquakeBean> listEarthQuakes){
        this.context = context;
        this.listEarthQuakes = listEarthQuakes;
    }

    public class EarthQuakeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        RelativeLayout rlCardContent;
        TextView tvPlace;
        TextView tvMagnitude;
        ImageView imgIcon;

        EarthQuakeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            rlCardContent = (RelativeLayout)itemView.findViewById(R.id.rlCardContent);
            tvPlace = (TextView)itemView.findViewById(R.id.tvPlace);
            tvMagnitude = (TextView)itemView.findViewById(R.id.tvMagnitude);
            imgIcon = (ImageView)itemView.findViewById(R.id.imgIcon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            changeFragment(getPosition());
        }
    }

    @Override
    public int getItemCount() {
        return listEarthQuakes.size();
    }

    @Override
    public EarthQuakeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_earthquake, viewGroup, false);
        EarthQuakeViewHolder earthQuakeViewHolder = new EarthQuakeViewHolder(view);
        return earthQuakeViewHolder;
    }

    @Override
    public void onBindViewHolder(EarthQuakeViewHolder earthQuakeViewHolder, int i) {

        float magnitude = Float.valueOf(listEarthQuakes.get(i).getMag());

        earthQuakeViewHolder.tvMagnitude.setText(listEarthQuakes.get(i).getMag());
        earthQuakeViewHolder.tvPlace.setText(listEarthQuakes.get(i).getPlace());

        if(magnitude < 0.0)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.eq0));
        else if(magnitude >= 0.0 && magnitude <=0.9)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.earthquake_easy));
        else if(magnitude > 0.9 && magnitude < 2.0)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.eq2));
        else if(magnitude >= 2.0 && magnitude < 3.0)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.eq3));
        else if(magnitude >= 3.0 && magnitude < 4.0)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.eq4));
        else if(magnitude >= 4.0 && magnitude < 5.0)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.eq5));
        else if(magnitude >= 5.0 && magnitude < 6.0)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.eq6));
        else if(magnitude >= 6.0 && magnitude < 7.0)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.eq7));
        else if(magnitude >= 7.0 && magnitude < 8.0)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.eq8));
        else if(magnitude >= 8.0 && magnitude < 9.0)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.eq9));
        else if(magnitude >= 9.0 && magnitude <= 9.9)
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.earthquake_hard));
        else
            earthQuakeViewHolder.rlCardContent.setBackgroundColor(context.getResources().getColor(R.color.eqn));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void changeFragment(int position) {

        FragmentActivity fragment = (FragmentActivity) context;

        // Create new fragment and transaction
        DetailsFragment detailsFragment = new DetailsFragment();
        FragmentTransaction transaction = fragment.getSupportFragmentManager().beginTransaction();

        // Send data
        Bundle bundles = new Bundle();
        EarthquakeBean earthquakeData = listEarthQuakes.get(position);
        bundles.putSerializable("earthquakeData", earthquakeData);
        detailsFragment.setArguments(bundles);

        /* Animation */
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_left);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment, detailsFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void setData(ArrayList<EarthquakeBean> listEarthQuakes) {
        this.listEarthQuakes = listEarthQuakes;
    }
}
