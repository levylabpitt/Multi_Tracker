package com.synapse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pqiorg.multitracker.R;
import com.synapse.listener.SpinnerBeaconSelectedListener;


import java.util.List;


/**
 * Created by ravi on 16/11/17.
 */

public class SpinnerBeaconListAdapter extends RecyclerView.Adapter<SpinnerBeaconListAdapter.MyViewHolder> {
    private Context context;

    List<String> beaconsList;

    SpinnerBeaconSelectedListener spinnerBeaconSelectedListener;

    public SpinnerBeaconListAdapter(Context context, List<String> beaconsList, SpinnerBeaconSelectedListener spinnerBeaconSelectedListener) {
        this.context = context;
        this.beaconsList = beaconsList;

        this.spinnerBeaconSelectedListener = spinnerBeaconSelectedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.state, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {



        holder.title.setText(beaconsList.get(position));



    }

    @Override
    public int getItemCount() {
        //     return MenuItem.length;
        return beaconsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spinnerBeaconSelectedListener.onBeaconSelected( beaconsList.get(getAdapterPosition()));

                }
            });
        }
    }
}

