package com.synapse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pqiorg.multitracker.R;
import com.synapse.listener.TeamsSelectedListener;
import com.synapse.model.teams_by_org.Datum;

import java.util.List;



/**
 * Created by ravi on 16/11/17.
 */

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.MyViewHolder> {
    private Context context;

    List<Datum> teamsList;

    TeamsSelectedListener teamsSelectedListener;

    public TeamsAdapter(Context context, List<Datum> teamsList, TeamsSelectedListener teamsSelectedListener) {
        this.context = context;
        this.teamsList = teamsList;

        this.teamsSelectedListener = teamsSelectedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.state, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {



        holder.title.setText(teamsList.get(position).getName());



    }

    @Override
    public int getItemCount() {
        //     return MenuItem.length;
        return teamsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    teamsSelectedListener.onItemSelected( teamsList.get(getAdapterPosition()).getName(), teamsList.get(getAdapterPosition()).getGid());

                }
            });
        }
    }
}

