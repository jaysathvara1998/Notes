package com.example.splashapplication.Model;

public class Constants {
    //COLUMNS
//    static final String ROW_ID="id";
//    static final String NAME="name";
//
//    //DB PROPS
//    static final String DB_NAME="ff_DB";
//    static final String TB_NAME="ff_TB";
//    static final int DB_VERSION=1;

    public static final String DATABASE_NAME = "demo";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_USERS = "user";
    public static final String TABLE_TASK = "task";
    public static final String TABLE_CATEGORY= "category";

    public static final String KEY_ID = "userID";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_TASK_ID = "taskID";
    public static final String KEY_TASK = "task";
    public static final String KEY_TASK_DESCRIPTION = "taskDescription";
    public static final String KEY_DATE = "taskDate";
    public static final String KEY_IMAGE = "taskBanner";

    public static final String KEY_CATEGORY = "taskCategory";
    public static final String KEY_CATEGORY_ID = "categoryID";
    public static final String KEY_CATEGORY_FLAG = "isDefault";

    //CREATE TB
    public static final String SQL_TABLE_USERS = " CREATE TABLE " + TABLE_USERS
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_USER_NAME + " TEXT, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT"
            + " ) ";

    public static final String SQL_ADD_TASK= " CREATE TABLE " + TABLE_TASK
            + " ( "
            + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TASK + " TEXT, "
            + KEY_TASK_DESCRIPTION + " TEXT, "
            + KEY_DATE + " DATE, "
            + KEY_CATEGORY + " TEXT, "
            + KEY_IMAGE + " TEXT"
            + " ) ";

    public static final String SQL_CATEGORY= " CREATE TABLE " + TABLE_CATEGORY
            + " ( "
            + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_CATEGORY + " TEXT, "
            + KEY_CATEGORY_FLAG + " INTEGER DEFAULT 1"
            + " ) ";

    public static final String SQL_CATEGORY_ALTER= " ALTER TABLE " + TABLE_CATEGORY
            + " ADD COLUMN "
            + KEY_CATEGORY_FLAG + " INTEGER DEFAULT 1";

    public static final String SQL_TASK_ALTER = " ALTER TABLE " + TABLE_TASK
            + " ADD COLUMN "
            + KEY_IMAGE + " TEXT";

//    //TABLE DROP STMT
    public static final String DROP_SQL_TABLE_USERS="DROP TABLE IF EXISTS "+TABLE_USERS;
    public static final String DROP_SQL_ADD_TASK="DROP TABLE IF EXISTS "+TABLE_TASK;
    public static final String DROP_SQL_CATEGORY="DROP TABLE IF EXISTS "+TABLE_CATEGORY;
}