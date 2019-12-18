package com.example.splashapplication.Fragment;

import android.app.Dialog;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splashapplication.Adapter.CategoryAdapter;
import com.example.splashapplication.Adapter.DBAdapter;
import com.example.splashapplication.Controller.SwipeController;
import com.example.splashapplication.Controller.SwipeControllerActions;
import com.example.splashapplication.Model.Category;
import com.example.splashapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CategoryFragment extends Fragment{

    public static ArrayList<Category> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    FloatingActionButton fabAddCategory;
    private CategoryAdapter mAdapter;
    DBAdapter sqLiteHelper;
    SwipeController swipeController = null;
    public static EditText category;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteHelper = new DBAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.categoryRecycler);
        taskList.clear();
        taskList.addAll(sqLiteHelper.getAllCategory());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CategoryAdapter(getActivity(),taskList);
        recyclerView.setAdapter(mAdapter);
        enableSwipeToDeleteAndEdit();

        fabAddCategory = (FloatingActionButton)view.findViewById(R.id.fabAddCategory);
        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginDialog();
            }
        });
        return  view;
    }

    private void callLoginDialog() {
        final Dialog myDialog = new Dialog(getActivity(),R.style.dialog);
        myDialog.setContentView(R.layout.add_category);
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);

        category = (EditText) myDialog.findViewById(R.id.etCategory);
        Button btnAddCategory = (Button) myDialog.findViewById(R.id.btnAddCategory);
        final Button cancel = (Button)myDialog.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stCategory = category.getText().toString();
                if (!stCategory.isEmpty()){
                    long c = sqLiteHelper.addCategory(stCategory);
                    if(!String.valueOf(c).isEmpty()){
                        Toast.makeText(getActivity(), "Category Added", Toast.LENGTH_LONG).show();
                        taskList.clear();
                        taskList.addAll(sqLiteHelper.getAllCategory());
                    }else {
                        Toast.makeText(getActivity(), "Unable to add category", Toast.LENGTH_LONG).show();
                    }
                    myDialog.dismiss();
                }
            }
        });
        myDialog.show();
        myDialog.setCanceledOnTouchOutside(true);
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
                Category t = taskList.get(position);
                int id = t.getId();
                String title = t.getCategory();
                callLoginDialog(id,title);
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

    public void callLoginDialog(final int id, final String title)
    {
        final Dialog myDialog = new Dialog(getActivity(),R.style.dialog);
        myDialog.setContentView(R.layout.add_category);
        myDialog.setCancelable(false);
        category = (EditText) myDialog.findViewById(R.id.etCategory);
        category.setText(title);
        Button login = (Button) myDialog.findViewById(R.id.btnAddCategory);
        Button cancel = (Button)myDialog.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        login.setText("Update Category");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stCategory = category.getText().toString();
                if (!stCategory.isEmpty()){
                    sqLiteHelper.updateCategory(id,stCategory);
                    Toast.makeText(getActivity(), "Category Updated", Toast.LENGTH_LONG).show();
                    Fragment fragment = new CategoryFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.detach(fragment);
                    fragmentTransaction.attach(fragment);
                    fragmentTransaction.commit();
                    myDialog.dismiss();
                }
            }
        });
        myDialog.show();
        myDialog.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Category");
    }
}