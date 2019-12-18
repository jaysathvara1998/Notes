package com.example.splashapplication.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.splashapplication.Model.Task;
import com.example.splashapplication.R;

import java.io.File;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>
{
    private ArrayList<Task> taskList;
    private Context context;
    public TaskAdapter(Context context,ArrayList<Task> taskList) {
        this.context=context;
        this.taskList = taskList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id,title, desc, date,category;
        public ImageView banner;
//        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvTitle);
            desc = (TextView) view.findViewById(R.id.tvTask);
            date = (TextView) view.findViewById(R.id.tvDate);
            category = (TextView)view.findViewById(R.id.tvCategory);
            banner = (ImageView)view.findViewById(R.id.taskBanner);
//            relativeLayout = (RelativeLayout)view.findViewById(R.id.layoutBanner);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_task, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(taskList.get(position).getTitle());
        holder.desc.setText(taskList.get(position).getDescription());
        holder.date.setText(taskList.get(position).getDate());
        holder.category.setText(taskList.get(position).getCategory());
        String image = taskList.get(position).getImage();
        File file = new File(image);
        Log.d("Task Adapter",image);
        if (image.equals("null")){
            holder.banner.setVisibility(View.GONE);
        }if (!file.exists()){
            holder.banner.setVisibility(View.GONE);
        }
        else {
            Glide.with(context).load(image).into(holder.banner);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void removeItem(int position) {
        Task p=taskList.get(position);
        int id=p.getId();
        DBAdapter db=new DBAdapter(context);
        db.openDB();
        if(db.delete(id)) {
            taskList.remove(position);
        }else {
            Toast.makeText(context,"Unable To Delete",Toast.LENGTH_SHORT).show();
        }
        db.closeDB();

        this.notifyItemRemoved(position);
    }
}