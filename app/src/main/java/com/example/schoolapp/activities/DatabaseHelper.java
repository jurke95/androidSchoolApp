package com.example.schoolapp.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import androidx.annotation.Nullable;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "schooldata.db";


    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + "TABLE_PERSON" +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,FIRSTNAME TEXT,LASTNAME TEXT,PHONENUMBER TEXT,ROLE TEXT, IMAGEURL TEXT) ");
        db.execSQL("create table " + "TABLE_ACADEMY" +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,DESCRIPTION TEXT,LOCATION TEXT) ");
        db.execSQL("create table " + "TABLE_CLASS" +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,ACTIVE BOOLEAN NOT NULL DEFAULT 0,ACADEMY_ID INTEGER,GENERATION TEXT) ");
        db.execSQL("create table " + "TABLE_ANNOUNCEMENT" +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,DESCRIPTION TEXT,TIME TEXT,PERSON_ID INTEGER,CLASS_ID INTEGER) ");
        db.execSQL("create table " + "TABLE_COMMENT" +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TEXT TEXT,ANNOUNCEMENT_ID INTEGER,PERSON_ID INTEGER,TIME TEXT) ");
        db.execSQL("create table " + "TABLE_SUBJECT" +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,ACADEMY_ID INTEGER,PROFESSOR_ID INTEGER) ");
        db.execSQL("create table " + "TABLE_CLASS_PERSON" +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,PERSON_ID INTEGER,CLASS_ID INTEGER,MARKS TEXT) ");
        db.execSQL("create table " + "TABLE_CLASS_SUBJECT" +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,CLASS_ID INTEGER,SUBJECT_ID INTEGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_PERSON");
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_ACADEMY");
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_CLASS");
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_ANNOUNCEMENT");
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_COMMENT");
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_SUBJECT");
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_CLASS_PERSON");
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_CLASS_SUBJECT");
        onCreate(db);

    }

    public  boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }
}
