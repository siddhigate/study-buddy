package com.example.studybuddy;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;

public class DBManager {

// Variables
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

//Constructor
    public DBManager(Context c) {
        context = c;
    }

//Open and close methods

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

//Insert into dailystatus

    public void insertDailyStatus(float hours,int score) {

        ContentValues contentValue = new ContentValues();

        Date d = Calendar.getInstance().getTime();
        String date = d.toString();

        contentValue.put(DatabaseHelper.DATE, date);
        contentValue.put(DatabaseHelper.HOURS, hours);
        contentValue.put(DatabaseHelper.SCORE, score);

        database.insert(DatabaseHelper.TABLE_NAME1, null, contentValue);

    }

//Cursor fetching dailystatus

    public Cursor fetchDailyStatus() {
        String[] columns = new String[] { DatabaseHelper.ID, DatabaseHelper.DATE, DatabaseHelper.HOURS,DatabaseHelper.SCORE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME1, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //Update dailystatus

    public int updateDailyStatus(String date, int hours,int score) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.HOURS, hours);
        contentValues.put(DatabaseHelper.SCORE, score);
        int i = database.update(DatabaseHelper.TABLE_NAME1, contentValues, DatabaseHelper.DATE + " = " + date, null);
        return i;
    }

    //Delete
    public void deleteDailyStatus(long id) {
        database.delete(DatabaseHelper.TABLE_NAME1, DatabaseHelper.ID + "=" + id, null);
    }

    // Get Total Score

    public int getScore(){

        int total_score=0;

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor res=sqLiteDatabase.rawQuery("select * from DAILYSTATUS ",null);

        res.moveToFirst();
        while(res.isAfterLast()==false){
            total_score = total_score + res.getInt(res.getColumnIndex("score"));
            res.moveToNext();
        }
        return total_score;
    }

    // Get Total Hours

    public float getHours(){

        float total_hours=0;

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor res=sqLiteDatabase.rawQuery("select * from DAILYSTATUS ",null);

        res.moveToFirst();
        while(res.isAfterLast()==false){
            total_hours= total_hours + res.getInt(res.getColumnIndex("hoursofstudy"));
            res.moveToNext();
        }
        return total_hours;
    }





}
