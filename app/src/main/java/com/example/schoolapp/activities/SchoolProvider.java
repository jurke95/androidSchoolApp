package com.example.schoolapp.activities;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.SQLException;

public class SchoolProvider extends ContentProvider {

    private SQLiteDatabase schoolDatabase;
    public static final String AUTHORITY = "com.example.schoolapp";
    public static final Uri CONTENT_URI_CLASS_PERSON = Uri.parse("content://" + AUTHORITY + "/" + "class_person");

    private static final int CLASS_PERSON_PERSON_ID = 1;
    private static final int CLASS_PERSON = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        uriMatcher.addURI(AUTHORITY, "class_person/#", CLASS_PERSON_PERSON_ID);
        uriMatcher.addURI(AUTHORITY, "class_person", CLASS_PERSON);

    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        schoolDatabase = dbHelper.getWritableDatabase();
        if (schoolDatabase != null) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case CLASS_PERSON_PERSON_ID:
                cursor = schoolDatabase.query("TABLE_CLASS_PERSON",new String[] { "MARKS"}, "PERSON_ID = ?",new String[] {s},null,null,null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CLASS_PERSON_PERSON_ID:
                return "vnd.android.cursor.dir/table_class_person";
            case CLASS_PERSON:
                return "vnd.android.cursor.dir/table_class_person";
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id;
        Uri _uri;
        switch (uriMatcher.match(uri)) {
            case CLASS_PERSON:
                id = schoolDatabase.insert(	"TABLE_CLASS_PERSON", "", contentValues);
                _uri = ContentUris.withAppendedId(CONTENT_URI_CLASS_PERSON, id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (id > 0) {

            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        }else {
            try {
                throw new SQLException("Insertion Failed for URI :" + uri);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return _uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
