package com.example.splashapplication.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.splashapplication.Adapter.DBAdapter;
import com.example.splashapplication.Model.Category;
import com.example.splashapplication.Model.Task;
import com.example.splashapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText title,task,date,etCategory;
    Button addTask,clear,addCategory;
    Context context;
    DatePickerDialog.OnDateSetListener day;
    private ArrayList<Category> taskList = new ArrayList<>();
    DBAdapter sqLiteHelper;
    Spinner spinner;
    View view;
    ImageView banner;
    String stTitle,stTask,stDate,btnString,match,stCategory,stImagePath;
    ArrayAdapter<Category> dataAdapter;
    int taskId;
    final Calendar myCalendar = Calendar.getInstance();
    Bitmap bitmap;
    Uri imageUri;
    File finalFile;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        init();
        if (true){
            getIntentData();
        }
        loadSpinnerData();
        setCategoryBtnVisibility();
        clickListener();

        if(match.equals("Edit Task")){
            getSupportActionBar().setTitle("Edit Task");
            addTask.setText("Delete Task");
            addTask.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delete, 0, 0, 0);
            addTask.setTextColor(Color.RED);
            view.setBackground(new ColorDrawable(Color.RED));
            match = addTask.getText().toString();
        }else {
            addTask.setText("Add Task");
            match = addTask.getText().toString();
        }

    }

    public void init(){

        context = this;
        sqLiteHelper = new DBAdapter(getApplicationContext());
        title = (EditText)findViewById(R.id.etTitle);
        task= (EditText)findViewById(R.id.etTask);
        date = (EditText)findViewById(R.id.etDate);
        clear = (Button)findViewById(R.id.clear);
        addTask = (Button)findViewById(R.id.btnAddTask);
        addCategory = (Button)findViewById(R.id.btnAddCategory);
        spinner = (Spinner)findViewById(R.id.spinner);
        banner = (ImageView)findViewById(R.id.ivBanner);
        view = (View)findViewById(R.id.view);
        spinner.setOnItemSelectedListener(this);
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,taskList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#49505B")));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#49505B"));
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        stImagePath = image.getAbsolutePath();
        return image;
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(pictureIntent.resolveActivity(getPackageManager()) != null){
                        //Create a file to store the image
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            Toast.makeText(context, "Photo file can't be created, please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(context,context.getPackageName(), photoFile);
                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    photoURI);
                            startActivityForResult(pictureIntent,
                                    0);
                        }
                    }

                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {

                case 0:
                    if (requestCode == 0 && resultCode == RESULT_OK) {
                        File imgFile = new  File(stImagePath);
                        if(imgFile.exists()){
//                            banner.setImageURI(Uri.fromFile(imgFile));
                            Glide.with(context).load(stImagePath).into(banner);
                        }
                    }
                    break;
                case 1:
                    if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                        try {
                            imageUri =  data.getData();
                            stImagePath = getRealPathFromURI(imageUri, this);
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            Glide.with(context).load(stImagePath).into(banner);
                            finalFile = new File(getRealPathFromURI(imageUri,this));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        return null;
    }

    public void getIntentData(){
        Intent i = getIntent();
        taskId= i.getIntExtra("id",0);
        stTitle = i.getStringExtra("title");
        stTask = i.getStringExtra("desc");
        stDate = i.getStringExtra("date");
        stCategory = i.getStringExtra("category");
        btnString = i.getStringExtra("button");
        title.setText(stTitle);
        task.setText(stTask);
        date.setText(stDate);
        addTask.setText(btnString);
        if(!title.getText().toString().isEmpty()){
            stImagePath = i.getStringExtra("image");
            Glide.with(context).load(stImagePath).into(banner);
        }
        match = addTask.getText().toString();
    }

    public void clickListener(){
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, day, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginDialog();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.getText().clear();
                setCategoryBtnVisibility();
            }
        });

        banner.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        day = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                setCategoryBtnVisibility();
            }
            private void updateLabel() {
                String myFormat = "dd MMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                date.setText(sdf.format(myCalendar.getTime()));
            }
        };

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stTitle = title.getText().toString();
                stTask = task.getText().toString();
                stDate = date.getText().toString();
                int itemId = spinner.getSelectedItemPosition();
                if(validation()){
                    if(match.equals("Add Task")){
                        if(itemId == -1){
                            int categoryId = 0;
                            sqLiteHelper.addTask(new Task(stTitle,stTask,stDate,categoryId,stImagePath));
                            Toast.makeText(AddTaskActivity.this, "Task Add successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Category t = taskList.get(itemId);
                            int categoryId = t.getId();
                            sqLiteHelper.addTask(new Task(stTitle,stTask,stDate,categoryId,stImagePath));
                            Toast.makeText(AddTaskActivity.this, "Task Add successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }else {
                        if (sqLiteHelper.delete(taskId)){
                            Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(context, "Unable to delete item", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private boolean validation() {
        if(stTitle.isEmpty()){
            title.setError("Please Enter Title");
        }else if(stTask.isEmpty()){
            task.setError("Please Enter Task");
        }else if(stDate.isEmpty()){
            date.setError("Please Select Date");
        } else {
            return true;
        }
        return false;
    }

    private void loadSpinnerData() {

        taskList.addAll(sqLiteHelper.getAllCategory());
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(getSupportActionBar().getTitle().equals("Edit Task")){
            menu.add(0, 0, 0, "Done").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0){
            stTitle = title.getText().toString();
            stTask = task.getText().toString();
            stDate = date.getText().toString();
            int itemId = spinner.getSelectedItemPosition();
            if(itemId == -1){
                if(validation()){
                    int categoryId = 0;
                    if (sqLiteHelper.updateTask(taskId,stTitle,stTask,stDate,categoryId,stImagePath)){
                        Toast.makeText(getApplicationContext(), "Task Update Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }else {
                Category t = taskList.get(itemId);
                int categoryId = t.getId();
                if(validation()){
                    if (sqLiteHelper.updateTask(taskId,stTitle,stTask,stDate,categoryId,stImagePath)){
                        Toast.makeText(getApplicationContext(), "Task Update Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCategoryBtnVisibility(){
        String data = date.getText().toString();
        if (data.isEmpty()){
            clear.setVisibility(View.GONE);
        }else {
            clear.setVisibility(View.VISIBLE);
        }
    }

    private void callLoginDialog()
    {
        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.add_category);
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);

        etCategory= (EditText) myDialog.findViewById(R.id.etCategory);
        Button btnAddCategory = (Button) myDialog.findViewById(R.id.btnAddCategory);
        Button cancel = (Button)myDialog.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stCategory = etCategory.getText().toString();
                if (!stCategory.isEmpty()){
                    long c = sqLiteHelper.addCategory(stCategory);
                    if(!String.valueOf(c).isEmpty()){
                        Toast.makeText(getApplicationContext(), "Category Added", Toast.LENGTH_LONG).show();
                        taskList.clear();
                        taskList.addAll(sqLiteHelper.getAllCategory());
                    }else {
                        Toast.makeText(getApplicationContext(), "Unable to add category", Toast.LENGTH_LONG).show();
                    }
                    myDialog.dismiss();
                }
            }
        });
        myDialog.show();
        myDialog.setCanceledOnTouchOutside(true);
    }
}