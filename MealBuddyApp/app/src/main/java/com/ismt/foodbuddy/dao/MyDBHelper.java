package com.ismt.foodbuddy.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String name = "foodbuddy.db";
   private static final int version = 1;
    public MyDBHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public long addNewUser(String username, String password){

        //step 1: create a connection with write acess
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //step 2: create content values to store the data in key value pair

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        //step 3: insert the data into database
        long rowId = sqLiteDatabase.insert("users", null, values);

        return rowId;


    }





}
