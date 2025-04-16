package com.example.squadmaps;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "MyLogin2.db";
    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(login TEXT primary key, password TEXT, username TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("create Table users(login TEXT primary key, password TEXT, username TEXT)");
    }

    public Boolean checklogin(String login) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where login = ?", new String[]{login});

        cursor.moveToNext();
//        Log.i("TAG checklogin", cursor.getString(1));
        return cursor.getCount() > 0;
    }

    public Boolean insertData(String login, String password, String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("create Table users(login TEXT primary key, password TEXT, username TEXT)");

        ContentValues contentValues= new ContentValues();
        contentValues.put("login", login);
        contentValues.put("password", password);
        contentValues.put("username", username);
        long result = MyDB.insert("users", null, contentValues);
//        DatabaseHelper.username = username;

        return result != -1;
    }

    public Boolean check(String login, String password, String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where login = ? and password = ? and username = ?", new String[] {login,password,username});

        Log.i("TAG check", cursor.getCount()+"");
        return cursor.getCount() > 0;
    }

//    login password username

    public String getLogin() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users", null);
        if (cursor.getCount() <= 0) return null;

        cursor.moveToNext();
        return cursor.getString(0);
    }

    public String getPassword() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users", null);
        if (cursor.getCount() <= 0) return null;

        cursor.moveToNext();
        return cursor.getString(1);
    }

    public String getUsername() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users", null);
        if (cursor.getCount() <= 0) return null;

        cursor.moveToNext();
        return cursor.getString(2);
    }
}
