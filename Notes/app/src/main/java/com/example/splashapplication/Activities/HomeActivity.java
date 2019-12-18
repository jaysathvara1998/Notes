package com.example.splashapplication.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.splashapplication.Adapter.DBAdapter;
import com.example.splashapplication.Adapter.TaskAdapter;
import com.example.splashapplication.Controller.SwipeController;
import com.example.splashapplication.Controller.SwipeControllerActions;
import com.example.splashapplication.Fragment.CategoryFragment;
import com.example.splashapplication.Fragment.HomeFragment;
import com.example.splashapplication.Halper.SessionManager;
import com.example.splashapplication.Model.Task;
import com.example.splashapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import static com.example.splashapplication.R.string.navigation_drawer_close;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawer;
    NavigationView navigationView;
    SessionManager sessionManager;
    Context context;
//    DBAdapter sqLiteHelper;
//    FloatingActionButton addTask;
//    Dialog dialog;
//    private ArrayList<Task> taskList = new ArrayList<>();
//    private RecyclerView recyclerView;
//    private TaskAdapter mAdapter;
//    SwipeController swipeController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.DKGRAY);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_home);
//        sqLiteHelper = new DBAdapter(getApplicationContext());
//        dialog = new Dialog(this);
//
//        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
//        taskList.addAll(sqLiteHelper.getAllData());
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new TaskAdapter(context,taskList);
//        recyclerView.setAdapter(mAdapter);
//        enableSwipeToDeleteAndEdit();

//        addTask = (FloatingActionButton)findViewById(R.id.fabAddTask);
//        addTask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),AddTaskActivity.class);
//                startActivity(i);
//            }
//        });
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_category:
                fragment = new CategoryFragment();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment newFragment;
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
            alert.setMessage("Are you sure?").setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sessionManager.logoutUser();
                }
            }).setNegativeButton("Cancel",null);
            AlertDialog alert1 = alert.create();
            alert1.show();
        }else if (id == R.id.nav_category){
            newFragment = new CategoryFragment();
            displaySelectedFragment(newFragment);
        }else if (id == R.id.nav_home){
            newFragment = new HomeFragment();
            displaySelectedFragment(newFragment);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    public void onBackPressed()
    {
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}