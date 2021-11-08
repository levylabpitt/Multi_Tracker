package com.synapse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ammarptn.gdriverest.GoogleDriveFileHolder;
import com.google.android.gms.drive.DriveFolder;
import com.google.api.client.util.DateTime;
import com.pqiorg.multitracker.R;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.listener.DriveItemListener;
import com.synapse.listener.DriveListener;

import java.util.List;


/**
 * Created by ravi on 16/11/17.
 */

public class DriveSheetsAdapterNew extends RecyclerView.Adapter<DriveSheetsAdapterNew.MyViewHolder> {
    private Context context;

    List<GoogleDriveFileHolder> GoogleDriveFiles;
    DriveItemListener driveItemListener;
    String defaultSheetId;
    DriveListener driveListener;

    public DriveSheetsAdapterNew(Context context, List<GoogleDriveFileHolder> GoogleDriveFiles, DriveItemListener driveItemListener, DriveListener driveListener) {
        this.context = context;
        this.GoogleDriveFiles = GoogleDriveFiles;
        this.driveItemListener = driveItemListener;
        this.defaultSheetId = SharedPreferencesUtil.getDefaultSheetId(context);
        this.driveListener = driveListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    /*    View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sheet_item, parent, false);
*/
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sheet_new, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final String fileMIMETYPE = GoogleDriveFiles.get(position).getMimeType();
        String name = Utility.getLimitedCharFromString(GoogleDriveFiles.get(position).getName());
        holder.sheetName.setText(name);
       // DateTime d = GoogleDriveFiles.get(position).getCreatedTime();
        Long epochTime = GoogleDriveFiles.get(position).getCreatedTime().getValue();
        holder.createdDate.setText(Utility.convertEpochToDate(epochTime));


        if (fileMIMETYPE.equals(DriveFolder.MIME_TYPE)) {    // folder
            holder.icon.setImageResource(R.drawable.folder_new);

        //    holder.parent.setBackgroundTintMode(ContextCompat.getColorStateList(context, R.color.colorPrimary));
            holder.parent.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.color_bg_1));
        } else {/// spreadsheet

            holder.icon.setImageResource(R.drawable.google_sheets);
            holder.parent.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.color_bg_2));
            if (GoogleDriveFiles.get(position).getId().equals(defaultSheetId)) {
                holder.icon.setImageResource(R.drawable.selected_spreadsheet);
            } else {
                holder.icon.setImageResource(R.drawable.non_selected_spreadsheet);

            }

        }


    }

    @Override
    public int getItemCount() {
        //     return MenuItem.length;
        return GoogleDriveFiles.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sheetName;
        public TextView createdDate;
       // ImageView favourite_added;
      //  ImageView addTofavourite;
     //   LinearLayout ll_favourite;
        ImageView icon;
        ImageView imgvw_action;
        LinearLayout  parent;
        public MyViewHolder(View view) {
            super(view);
            sheetName = view.findViewById(R.id.sheetName);
            createdDate = view.findViewById(R.id.createdDate);

            parent = view.findViewById(R.id.parent);

           // favourite_added = view.findViewById(R.id.favourite_added);
           // addTofavourite = view.findViewById(R.id.addTofavourite);
          //  ll_favourite = view.findViewById(R.id.ll_favourite);
            icon = view.findViewById(R.id.thumbnail);
            imgvw_action = view.findViewById(R.id.imgvw_action);

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String fileMIMETYPE = GoogleDriveFiles.get(getAdapterPosition()).getMimeType();
                    String name = Utility.getLimitedCharFromString(GoogleDriveFiles.get(getAdapterPosition()).getName());
                    if (fileMIMETYPE.equals(DriveFolder.MIME_TYPE)) { // folder
                        driveItemListener.onItemClick(GoogleDriveFiles.get(getAdapterPosition()).getId(), name, "folder");
                    } else {  // sheet
                        driveListener.onFavouriteClick(GoogleDriveFiles.get(getAdapterPosition()).getId(), GoogleDriveFiles.get(getAdapterPosition()).getName());

                    }



                }


            });


            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String fileMIMETYPE = GoogleDriveFiles.get(getAdapterPosition()).getMimeType();
                    String name = Utility.getLimitedCharFromString(GoogleDriveFiles.get(getAdapterPosition()).getName());
                    if (fileMIMETYPE.equals(DriveFolder.MIME_TYPE)) { // folder
                        driveItemListener.onItemClick(GoogleDriveFiles.get(getAdapterPosition()).getId(), name, "folder");
                    } else {  // sheet
                        driveItemListener.onItemClick(GoogleDriveFiles.get(getAdapterPosition()).getId(), name, "sheet");
                    }


                }
            });

            imgvw_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    driveItemListener.onItemDelete(GoogleDriveFiles.get(getAdapterPosition()).getName(),GoogleDriveFiles.get(getAdapterPosition()).getId(),GoogleDriveFiles.get(getAdapterPosition()).getMimeType());
                }
            });


        }



    }
}

