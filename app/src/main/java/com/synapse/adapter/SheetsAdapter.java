package com.synapse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pqiorg.multitracker.R;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.model.SpreadsheetItem;

import java.util.List;


/**
 * Created by ravi on 16/11/17.
 */

public class SheetsAdapter extends RecyclerView.Adapter<SheetsAdapter.MyViewHolder> {
    private Context context;

    List<SpreadsheetItem> sheetList;


    public SheetsAdapter(Context context, List<SpreadsheetItem> sheetList) {
        this.context = context;
        this.sheetList = sheetList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sheet_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.sheetName.setText(String.valueOf(sheetList.get(position).getName()));
        holder.createdDate.setText(String.valueOf(sheetList.get(position).getDateCreated()));

        String defaultSheetId = SharedPreferencesUtil.getDefaultSheetId(context);
        if (sheetList.get(position).getId()!=null && sheetList.get(position).getId().equals(defaultSheetId)) {
            //   if (sheetList.get(position).isDefaultForWrite()) {
            holder.favourite_added.setVisibility(View.VISIBLE);
            holder.addTofavourite.setVisibility(View.GONE);
        } else {
            holder.favourite_added.setVisibility(View.GONE);
            holder.addTofavourite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        //     return MenuItem.length;
        return sheetList.size();
    }


    public void refresh(int position) {
      /*  for (int i = 0; i < sheetList.size(); i++) {
            sheetList.get(i).setDefaultForWrite(false);
        }
        sheetList.get(position).setDefaultForWrite(true);*/
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sheetName;
        public TextView createdDate;
        ImageView favourite_added;
        ImageView addTofavourite;

        public MyViewHolder(View view) {
            super(view);
            sheetName = view.findViewById(R.id.sheetName);
            createdDate = view.findViewById(R.id.createdDate);

            favourite_added = view.findViewById(R.id.favourite_added);
            addTofavourite = view.findViewById(R.id.addTofavourite);

            favourite_added.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.showToast(context, "Default for writing data.");
                }
            });
            addTofavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.showToast(context, "Selected for writing data.");
                    SharedPreferencesUtil.setDefaultSheetId(context, sheetList.get(getAdapterPosition()).getId());
                    refresh(getAdapterPosition());
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   //"https://docs.google.com/gview?embedded=true&url="+
                    /*String url = "https://docs.google.com/spreadsheets/d/" + sheetList.get(getAdapterPosition()).getId()+"/edit";
                    Intent i = new Intent(context, FullscreenImageView.class);
                    i.putExtra("url",url);
                    context.startActivity(i);

                    */


                   /* String id =  sheetList.get(getAdapterPosition()).getId();
                    Intent i = new Intent(context, ReadSpreadSheetDataActivity.class);
                    i.putExtra("id",id);
                    context.startActivity(i);*/

                }
            });


        }


    }
}

