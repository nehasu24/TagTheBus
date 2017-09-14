package com.example.neha.tagthebus;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


/**
 * Created by NEHA on 5/6/2017.
 */

//SQL helper class for the database
public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Query the sql query in writable mode
    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //inserting into database all the details of the street. Images, Title, userName, time,id,streetname
    public void insertData(String name, String title, byte[] image, String time, String user) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO STREET VALUES (NULL, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindString(2, title);
        statement.bindBlob(3, image);
        statement.bindString(4, time);
        statement.bindString(5, user);
        statement.executeInsert();
    }

    public Cursor getData(String sql, Object o) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    //function to help delete the database
    public void delete(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);

    }
}