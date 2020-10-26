package com.synapse.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.ammarptn.gdriverest.GoogleDriveFileHolder;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;*/
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pqiorg.multitracker.drive.FullscreenImageViewDrive;
import com.google.android.gms.drive.DriveFolder;
import com.pqiorg.multitracker.R;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.listener.DriveItemListener;
import com.synapse.listener.DriveListener;


import java.util.ArrayList;
import java.util.Arrays;

/*
import planet.info.skyline.R;
import planet.info.skyline.drive.DriveActivity;
import planet.info.skyline.util.Utility;
*/

public class AdapterDrive extends RecyclerView.Adapter<AdapterDrive.MyViewHolder> {
    Context context;
    ArrayList<GoogleDriveFileHolder> GoogleDriveFiles = new ArrayList<>();
    DriveItemListener driveItemListener;
    String defaultFolderId;
    DriveListener driveListener;

    public AdapterDrive(Context context, ArrayList<GoogleDriveFileHolder> list, DriveItemListener driveItemListener, DriveListener driveListener) {
        this.context = context;
        this.GoogleDriveFiles = list;
        this.driveItemListener = driveItemListener;
        defaultFolderId = SharedPreferencesUtil.getDefaultDriveFolderId(context);
        this.driveListener = driveListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_drive_folder, parent, false);
        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final String fileMIMETYPE = GoogleDriveFiles.get(position).getMimeType();
        final String file_Folder_Name = GoogleDriveFiles.get(position).getName();
        final String fileFolderId = GoogleDriveFiles.get(position).getId();
        final String thumbnailLink = GoogleDriveFiles.get(position).getThumbnailLink();
        final String googleId = GoogleDriveFiles.get(position).getId();
        final String WebContentLink = GoogleDriveFiles.get(position).getWebContentLink();
        final String WebViewLink = GoogleDriveFiles.get(position).getWebViewLink();
        Long epochTime = GoogleDriveFiles.get(position).getCreatedTime().getValue();

        //holder.tv_name.setText(Utility.getLimitedCharFromString(file_Folder_Name));
        holder.tv_name.setText(file_Folder_Name);
        if (fileMIMETYPE.equals(DriveFolder.MIME_TYPE)) {
            holder.thumbnail.setImageResource(R.drawable.folder_icon);
            holder.spinner.setVisibility(View.GONE);
            holder.ll_favourite.setVisibility(View.VISIBLE);
        } else {
            holder.ll_favourite.setVisibility(View.GONE);

            String fileExt = "";
            if (file_Folder_Name.contains(".")) {
                fileExt = file_Folder_Name.substring(file_Folder_Name.lastIndexOf("."));
            }

            boolean isImage = Arrays.asList(Utility.imgExt).contains(fileExt);
           /* boolean isDoc = Arrays.asList(Utility.docExt).contains(fileExt);
            boolean isMedia = Arrays.asList(Utility.mediaExt).contains(fileExt);
            boolean isWord = Arrays.asList(Utility.wordExt).contains(fileExt);
            boolean isPdf = Arrays.asList(Utility.pdfExt).contains(fileExt);
            boolean isExcel = Arrays.asList(Utility.excelExt).contains(fileExt);
            boolean isText = Arrays.asList(Utility.txtExt).contains(fileExt);*/


            if (isImage) {
                holder.createdDate.setText(Utility.convertEpochToDate(epochTime));
                //  holder.webView.loadUrl(url);
                Glide.with(context).load(thumbnailLink).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.spinner.setVisibility(View.GONE);
                        holder.thumbnail.setImageResource(R.drawable.no_image);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.spinner.setVisibility(View.GONE);
                        return false;
                    }
                })
                        .into(holder.thumbnail);
            } /*else if (isWord) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.doc);
            } else if (isPdf) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.pdf);
            } else if (isExcel) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.excel);
            } else if (isText) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.txt_file_icon);
            } else if (isMedia) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.media);
            } else {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.no_image);
            }*/

        }
       /* thumbnailLink =     "https://docs.google.com/feeds/vt?gd\\u003dtrue\\u0026id\\u003d1_PA7fPvv9oJ3Sck7iAKlOykOdGdm7BA6IfHnU4EjxPM\\u0026v\\u003d1\\u0026s\\u003dAMedNnoAAAAAXvw6WXe6k4QEK3PH-yEG86ZjqK03fvTP\\u0026sz\\u003ds220";
        Glide.with(context).load(thumbnailLink).into(holder.thumbnail);*/

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fileMIMETYPE.equals(DriveFolder.MIME_TYPE)) {
                    //if (context instanceof DriveActivity) {
                     //   ((DriveActivity) context).fileFolderClickHandle(fileFolderId, file_Folder_Name);
                   // }
                    driveItemListener.onItemClick(fileFolderId,file_Folder_Name,"");
                } else {
//                    Intent i = new Intent(context, FullscreenImageView.class);
//                    i.putExtra("url",  WebContentLink);
//                    i.putExtra("FileName", file_Folder_Name);
//                    context. startActivity(i);
//                    if (context instanceof DriveActivity) {
//                        ((DriveActivity) context).downloadFile(file_Folder_Name, googleId, fileMIMETYPE);
//                    }
                    String fullImageLink = thumbnailLink;
                    if (thumbnailLink.contains("=")) {
                        fullImageLink = thumbnailLink.substring(0, thumbnailLink.lastIndexOf("="));
                    }

                    Intent newIntent = new Intent(context, FullscreenImageViewDrive.class);
                    newIntent.putExtra("uri", fullImageLink);
                    newIntent.putExtra("name", file_Folder_Name);
                    context.startActivity(newIntent);
                }
            }
        });


        if (fileFolderId.equals(defaultFolderId)) {
            holder.favourite_added.setVisibility(View.VISIBLE);
            holder.addTofavourite.setVisibility(View.GONE);
        } else {
            holder.favourite_added.setVisibility(View.GONE);
            holder.addTofavourite.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        return GoogleDriveFiles.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView createdDate;
        ImageView thumbnail;
        TextView tv_name;
        ProgressBar spinner;
        WebView webView;
        LinearLayout imgvw_action, ll_favourite;
        ImageView addTofavourite, favourite_added;

        public MyViewHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            tv_name = itemView.findViewById(R.id.tv_name);
            spinner = itemView.findViewById(R.id.progressBar1);
            webView = itemView.findViewById(R.id.webView);
            imgvw_action = itemView.findViewById(R.id.imgvw_action);
            ll_favourite = itemView.findViewById(R.id.ll_favourite);
            addTofavourite = itemView.findViewById(R.id.addTofavourite);
            favourite_added = itemView.findViewById(R.id.favourite_added);
            createdDate = itemView.findViewById(R.id.createdDate);
           /* favourite_added.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.showToast(context, "Default for saving images.");
                }
            });
            addTofavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.showToast(context, "Selected for saving images.");
                    SharedPreferencesUtil.setDefaultDriveFolder(context, GoogleDriveFiles.get(getAdapterPosition()).getId());
                    driveItemListener.onDefaultFolderChanged();
                }
            });*/


            ll_favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    driveListener.onFavouriteClick(GoogleDriveFiles.get(getAdapterPosition()).getId(), GoogleDriveFiles.get(getAdapterPosition()).getName());
                  /*
                    if (GoogleDriveFiles.get(getAdapterPosition()).getId() != null && GoogleDriveFiles.get(getAdapterPosition()).getId().equals(defaultFolderId)) {
                        Utility.showToast(context, "Default for saving images.");
                    } else {
                        Utility.showToast(context, "Selected for saving images.");
                        SharedPreferencesUtil.setDefaultDriveFolder(context, GoogleDriveFiles.get(getAdapterPosition()).getId());
                        SharedPreferencesUtil.setDefaultDriveFolderName(context, GoogleDriveFiles.get(getAdapterPosition()).getName());
                        driveItemListener.onRefresh();
                    }*/
                }
            });

            imgvw_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 //   openOptionMenu(v);
                    driveItemListener.onItemDelete(GoogleDriveFiles.get(getAdapterPosition()).getName(),GoogleDriveFiles.get(getAdapterPosition()).getId(),GoogleDriveFiles.get(getAdapterPosition()).getMimeType());
                }
            });



        }

        public void openOptionMenu(View v) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);


            popup.getMenuInflater().inflate(R.menu.option_menu_post, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.menu2:
                            driveItemListener.onItemDelete(GoogleDriveFiles.get(getAdapterPosition()).getName(),GoogleDriveFiles.get(getAdapterPosition()).getId(),GoogleDriveFiles.get(getAdapterPosition()).getMimeType());
                            return true;


                        default:
                            return false;
                    }
                }
            });

            popup.show();
        }




    }


}
