package com.example.splashapplication.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.example.splashapplication.Model.Category;
import com.example.splashapplication.Model.Constants;
import com.example.splashapplication.Halper.DBHelper;
import com.example.splashapplication.Model.Task;
import com.example.splashapplication.Model.User;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class DBAdapter {
    Context c;
    SQLiteDatabase db;
    DBHelper helper;

    public DBAdapter(Context c) {
        this.c = c;
        helper=new DBHelper(c);
    }

    //OPEN
    public void openDB() {
        try {
            db=helper.getWritableDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //CLOSE DB
    public void closeDB()
    {
        try {
            helper.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    INSERT/SAVE
    public void addUser(User user) {

        //create content values to insert
        ContentValues values = new ContentValues();
        //Put username in  @values
        values.put(Constants.KEY_USER_NAME, user.userName);
        //Put email in  @values
        values.put(Constants.KEY_EMAIL, user.email);
        //Put password in  @values
        values.put(Constants.KEY_PASSWORD, user.password);
        // insert row
        long todo_id = db.insert(Constants.TABLE_USERS, null, values);
    }

    public void addTask(Task task){
        db = helper.getReadableDatabase();
        try{
            db.execSQL("INSERT INTO "+ Constants.TABLE_TASK +"("+ Constants.KEY_TASK +","+Constants.KEY_TASK_DESCRIPTION +","+ Constants.KEY_DATE +","+Constants.KEY_CATEGORY+","+Constants.KEY_IMAGE+") VALUES('"+task.title+"','"+task.description+"','"+task.date+"','"+task.id+"','"+task.image+"');");
        } catch (SQLException e) {
            Log.d("Exception",e.getMessage());
            e.printStackTrace();
        }
//        db.execSQL("INSERT INTO "+ Constants.TABLE_TASK +"("+ Constants.KEY_TASK +","+Constants.KEY_TASK_DESCRIPTION +","+ Constants.KEY_DATE +","+Constants.KEY_CATEGORY+") VALUES('"+task.title+"','"+task.description+"','"+task.date+"','"+task.id+"');");
    }

    public long addCategory(String category){
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_CATEGORY, category);
        values.put(Constants.KEY_CATEGORY_FLAG, 0);
        db = helper.getReadableDatabase();
        long c = db.insert(Constants.TABLE_CATEGORY,null,values);
        return c;
    }

    public ArrayList<Task> getAllData() {
        db = helper.getReadableDatabase();
        ArrayList<Task> list = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM "+Constants.TABLE_TASK,null);
        if(res.moveToFirst()){
            do {
                int id = res.getInt(0);   //0 is the number of id column in your database table
                String title = res.getString(1);
                String desc = res.getString(2);
                String date = res.getString(3);
                String categoryId = res.getString(4);
                String imagePath = res.getString(5);
                Log.d("Path Adapter",imagePath);
                Cursor  cursor = db.rawQuery("SELECT * FROM " + Constants.TABLE_CATEGORY + " where "+Constants.KEY_CATEGORY_ID+" = '" +categoryId+ "'" , null);
                if (cursor.moveToFirst()){
                    do{
                        String category = cursor.getString(1);
                        Task newTask = new Task(id, title, desc, date,category,imagePath);
                        list.add(newTask);
                    }while (cursor.moveToNext());
                }else{
                    Task newTask = new Task(title, desc, date,id,imagePath);
                    list.add(newTask);
                }
            }while(res.moveToNext());
        }
        return list;
    }

    public ArrayList<Category> getAllCategory() {
        db = helper.getReadableDatabase();
        ArrayList<Category> list = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM "+Constants.TABLE_CATEGORY,null);
        if(res.moveToFirst()){
            do {
                int id = res.getInt(0);   //0 is the number of id column in your database table
                String title = res.getString(1);
                int flag = res.getInt(2);
//                Category newCategory = new Category(id, title);
                Category newCategory = new Category(id, title,flag);
                list.add(newCategory);
            }while(res.moveToNext());
        }
        return list;
    }

    public User Authenticate(User user) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_USERS,// Selecting Table
                new String[]{Constants.KEY_ID, Constants.KEY_USER_NAME, Constants.KEY_EMAIL, Constants.KEY_PASSWORD},//Selecting columns want to query
                Constants.KEY_EMAIL + "=?",
                new String[]{user.email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            //Match both passwords check they are same or not
            if (user.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }
        //if user password does not matches or there is no record with that email then return @false
        return null;
    }

    public boolean isEmailExists(String email) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_USERS,// Selecting Table
                new String[]{Constants.KEY_ID, Constants.KEY_USER_NAME, Constants.KEY_EMAIL, Constants.KEY_PASSWORD},//Selecting columns want to query
                Constants.KEY_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);
        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }
        //if email does not exist return false
        return false;
    }

    //DELETE/REMOVE
    public boolean delete(int id) {
        try {
            int result=db.delete(Constants.TABLE_TASK,Constants.KEY_TASK_ID+" =?",new String[]{String.valueOf(id)});
            if(result>0) {
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCategory(int id) {
        try {
            int result=db.delete(Constants.TABLE_CATEGORY,Constants.KEY_CATEGORY_ID+" =?",new String[]{String.valueOf(id)});
            if(result>0) {
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //UPDATE
    public boolean updatePassword(String email,String password){
        db = helper.getReadableDatabase();
        db.execSQL("UPDATE "+Constants.TABLE_USERS+" SET password = "+"'"+password+"' "+ "WHERE email = "+"'"+email+"'");
        return  true;
    }

    public boolean updateTask(int id,String title,String desc,String date,int category,String image){
        db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TASK,title);
        values.put(Constants.KEY_TASK_DESCRIPTION, desc);
        values.put(Constants.KEY_DATE, date);
        values.put(Constants.KEY_CATEGORY, category);
        values.put(Constants.KEY_IMAGE, image);
        int count = db.update(Constants.TABLE_TASK,values,"taskID=?",new String[]{String.valueOf(id)});
        Log.d("Update", String.valueOf(count));
        return  true;
    }

    public boolean updateCategory(int id,String title){
        db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_CATEGORY,title);
        values.put(Constants.KEY_CATEGORY_FLAG,0);
        db.update(Constants.TABLE_CATEGORY,values,"categoryID=?",new String[]{String.valueOf(id)});
        return  true;
    }
}