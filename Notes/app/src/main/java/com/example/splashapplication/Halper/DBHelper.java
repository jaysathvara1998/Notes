package com.example.splashapplication.Halper;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.splashapplication.Model.Constants;

public class DBHelper extends SQLiteOpenHelper {
    public static final String[] category = {"Home","Work","Personal"};
    public DBHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Constants.SQL_TABLE_USERS);
            db.execSQL(Constants.SQL_ADD_TASK);
            db.execSQL(Constants.SQL_CATEGORY);
            ContentValues cv = new ContentValues();
            for(int i=0;i < category.length;i++){
                cv.put(Constants.KEY_CATEGORY,category[i]);
                db.insert(Constants.TABLE_CATEGORY,null,cv);
            }
        }catch (SQLException e) {
            Log.d("Exception", String.valueOf(e));
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2){
            db.execSQL(Constants.SQL_CATEGORY_ALTER);
            db.execSQL(Constants.SQL_TASK_ALTER);
        }
        onCreate(db);
    }
}