package com.venmo.cursor.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

public class TestDb extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public TestDb(Context context) {
        super(context, randomFileName(), null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table TEST (" + "some_str TEXT, " + "some_int INTEGER, " +
                "some_long LONG, " + "some_boolean INTEGER, " + "some_float REAL, " +
                "some_double REAL, " + "some_short INTEGER, " + "some_byte_array BLOB" + ")");
    }

    /** (int i, long l, float f, double d, short s, boolean b, byte[] bytes, ( */
    public void insertRow(int i, long l, float f, double d, short s, boolean b, byte[] bytes,
            String str) {
        ContentValues cv = new ContentValues();
        cv.put("some_str", str);
        cv.put("some_int", i);
        cv.put("some_long", l);
        cv.put("some_boolean", b);
        cv.put("some_float", f);
        cv.put("some_double", d);
        cv.put("some_short", s);
        cv.put("some_byte_array", bytes);
        getWritableDatabase().insert("TEST", null, cv);
    }

    public Cursor query() {
        return getReadableDatabase().query("TEST", null, null, null, null, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException("why would you want to upgrade?");
    }

    private static String randomFileName() {
        return "test_" + new Random().nextInt(1000000) + ".sqlite";
    }
}