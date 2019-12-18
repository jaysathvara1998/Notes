package com.example.splashapplication.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.splashapplication.Activities.AddTaskActivity;
import com.example.splashapplication.Adapter.DBAdapter;
import com.example.splashapplication.Adapter.TaskAdapter;
import com.example.splashapplication.Controller.SwipeController;
import com.example.splashapplication.Controller.SwipeControllerActions;
import com.example.splashapplication.Model.Task;
import com.example.splashapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    DBAdapter sqLiteHelper;
    FloatingActionButton addTask;
    private ArrayList<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter mAdapter;
    SwipeController swipeController = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteHelper = new DBAdapter(getActivity());
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        taskList.clear();
        taskList.addAll(sqLiteHelper.getAllData());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new TaskAdapter(getActivity(),taskList);
        recyclerView.setAdapter(mAdapter);
        enableSwipeToDeleteAndEdit();

        addTask = (FloatingActionButton)view.findViewById(R.id.fabAddTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(i);
            }
        });
        return  view;
    }
    private void enableSwipeToDeleteAndEdit() {
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                mAdapter.removeItem(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            }

            @Override
            public void onLeftClicked(int position) {
                Intent i = new Intent(getActivity(),AddTaskActivity.class);
                Task t = taskList.get(position);
                int id = t.getId();
                String title = t.getTitle();
                String desc = t.getDescription();
                String date = t.getDate();
                String category = t.getCategory();
                String image = t.getImage();
                i.putExtra("id",id);
                i.putExtra("title",title);
                i.putExtra("desc",desc);
                i.putExtra("date",date);
                i.putExtra("category",category);
                i.putExtra("image",image);
                i.putExtra("button","Edit Task");
                startActivity(i);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }
}
