package com.synapse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pqiorg.multitracker.R;
import com.synapse.listener.DashboardListener;
import com.synapse.model.DashboardModel;

import java.util.List;


/**
 * Created by ravi on 16/11/17.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {
    private Context context;
    List<DashboardModel> productDivisionList;
    DashboardListener dashboardListener;

    public DashboardAdapter(Context context, List<DashboardModel> productDivisionList, DashboardListener dashboardListener) {
        this.context = context;
        this.productDivisionList = productDivisionList;
        this.dashboardListener = dashboardListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (position == 0 || position == 1) {
            holder.parent.setBackgroundResource(0);
            ViewGroup.LayoutParams params = holder.parent.getLayoutParams();
            params.height = 100;
             params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.parent.setLayoutParams(params);
        }

        holder.txtvw_title.setText(String.valueOf(productDivisionList.get(position).getTitle()));
        holder.imgvw.setImageResource(productDivisionList.get(position).getIcon());


    }

    @Override
    public int getItemCount() {

        return productDivisionList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgvw;
        public TextView txtvw_title;
        public LinearLayout parent;


        public MyViewHolder(View view) {
            super(view);
            imgvw = view.findViewById(R.id.icon);
            txtvw_title = view.findViewById(R.id.title);
            parent = view.findViewById(R.id.parent);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dashboardListener.onDashboardItemClick(getAdapterPosition(), productDivisionList.get(getAdapterPosition()).getTitle());

                }
            });


        }


    }
}

