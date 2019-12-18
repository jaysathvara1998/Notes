package com.example.splashapplication.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splashapplication.Model.Category;
import com.example.splashapplication.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private ArrayList<Category> taskList;
    private Context context;

    public CategoryAdapter(Context context,ArrayList<Category> taskList) {
        this.context=context;
        this.taskList = taskList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id,category;

        public ViewHolder(View view) {
            super(view);
            category = (TextView) view.findViewById(R.id.tvCategory);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.category.setText(taskList.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void removeItem(int position) {
        Category p=taskList.get(position);
        int id=p.getId();
        DBAdapter db=new DBAdapter(context);
        db.openDB();
        if(db.deleteCategory(id))
        {
            taskList.remove(position);
        }else {
            Toast.makeText(context,"Unable To Delete",Toast.LENGTH_SHORT).show();
        }
        db.closeDB();

        this.notifyItemRemoved(position);
    }
}