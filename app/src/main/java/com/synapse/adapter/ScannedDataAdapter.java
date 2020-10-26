package com.synapse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pqiorg.multitracker.R;
import com.synapse.model.ScannedData;

import java.util.List;


/**
 * Created by ravi on 16/11/17.
 */

public class ScannedDataAdapter extends RecyclerView.Adapter<ScannedDataAdapter.MyViewHolder> {
    private Context context;

    List<ScannedData> dataList;


    public ScannedDataAdapter(Context context, List<ScannedData> dataList) {
        this.context = context;
        this.dataList = dataList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

      //  holder.text.setText((position+1)+"- "+dataList.get(position).getText());

        holder.text.setText(dataList.get(position).getQr_text());
    }

    @Override
    public int getItemCount() {
        //     return MenuItem.length;
        return dataList.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView createdDate;


        public MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.text);
            createdDate = view.findViewById(R.id.createdDate);



        }


    }
}

