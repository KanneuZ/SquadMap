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

/*вспомогатальный класс для работы с базой данных SQL.*/

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "MyLogin2.db";
    /*конструктор.*/
    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 5);
    }

    @Override
    /*создание таблицы при запуске. 
    входящиие данные: обьект базы данных.*/
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(login TEXT primary key, password TEXT, username TEXT)");
    }

    @Override
    /*обновление данных при изменениях. 
    входящие данные: обьект базы данных, старая версия таблицы, новая версия таблицы??*/
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("create Table users(login TEXT primary key, password TEXT, username TEXT)");
    }

    /*проверка существования логина в БД.
 	входные значения: логин.
  	возвращаемые значения: true/false.*/
    public Boolean checklogin(String login) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where login = ?", new String[]{login});

        cursor.moveToNext();
//        Log.i("TAG checklogin", cursor.getString(1));
        return cursor.getCount() > 0;
    }

    /*вставка новых данных.
 	входные значения: логин, пароль, имя пользователя.
  	возвращаемые значения: true - успешно/false - ошибка.*/
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

    /*проверка на существование конкретных логина, пароля и имени пользователя.
 	входные значения: логин, пароль, имя пользователя.
  	возвращаемые значения: true - если сущетвует/false - не существует.*/
    public Boolean check(String login, String password, String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where login = ? and password = ? and username = ?", new String[] {login,password,username});

        Log.i("TAG check", cursor.getCount()+"");
        return cursor.getCount() > 0;
    }

//    login password username

    /*получение логина.
 	входные значения: - .
  	возвращаемые значения: логин или ноль если данных нет.*/
    public String getLogin() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users", null);
        if (cursor.getCount() <= 0) return null;

        cursor.moveToNext();
        return cursor.getString(0);
    }

    /*получение пароля.
 	входные значения: - .
  	возвращаемые значения: пароль или ноль если данных нет.*/
    public String getPassword() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users", null);
        if (cursor.getCount() <= 0) return null;

        cursor.moveToNext();
        return cursor.getString(1);
    }

    /*получение имени пользователя.
 	входные значения: - .
  	возвращаемые значения: имя пользователя или ноль если данных нет.*/
    public String getUsername() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users", null);
        if (cursor.getCount() <= 0) return null;

        cursor.moveToNext();
        return cursor.getString(2);
    }
}
