package com.synapse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pqiorg.multitracker.R;
import com.synapse.listener.DrivePathClickListener;
import com.synapse.model.PathDrive;

import java.util.ArrayList;
import java.util.List;


public class DrivePathAdapter extends RecyclerView.Adapter<DrivePathAdapter.BookViewHolder> {

    private List<PathDrive> pathList;
    private Context context;
    DrivePathClickListener drivePathClickListener;

    public DrivePathAdapter(List<PathDrive> pathList, Context context, DrivePathClickListener drivePathClickListener) {
        this.pathList = pathList;
        this.context = context;
        this.drivePathClickListener = drivePathClickListener;
    }

    @Override
    public DrivePathAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drive_file_path, parent, false);

        return new DrivePathAdapter.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        holder.path.setText(pathList.get(position).getName());

        if (position == (getItemCount() - 1)) {
            holder.icon.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        } else {
            holder.icon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
        }


        holder.path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String folder_id = pathList.get(position).getMasterId();
                String folder_name = pathList.get(position).getName();

                // if (context instanceof DriveActivity) {
                //      ((DriveActivity)context).fileFolderClickHandle(folder_id,folder_name);
                //  }
                drivePathClickListener.onDrivePathClick(folder_id, folder_name);


                refreshPath(position);

            }
        });
    }

    private void refreshPath(int position) {
        if (position < pathList.size()) {
            List<PathDrive> tempPathList = new ArrayList<>();
            for (int i = 0; i <= position; i++) {
                tempPathList.add(pathList.get(i));
            }
            pathList.clear();
            pathList.addAll(tempPathList);
            notifyDataSetChanged();
        }

    }


    @Override
    public int getItemCount() {
        return pathList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        public TextView path;
        public ImageView icon;

        public BookViewHolder(View view) {
            super(view);

            path = (TextView) view.findViewById(R.id.tv_path);
            icon = (ImageView) view.findViewById(R.id.icon);
        }
    }

    public String getPath() {
        String path = "";
        for (PathDrive pathDrive : pathList) {
            path = path + pathDrive.getName() + "/";
        }
        return path;
    }

}