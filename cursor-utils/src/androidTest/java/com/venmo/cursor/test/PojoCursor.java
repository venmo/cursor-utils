package com.venmo.cursor.test;

import android.database.Cursor;

import com.venmo.cursor.IterableCursorWrapper;

public class PojoCursor extends IterableCursorWrapper<Pojo> {

    public PojoCursor(Cursor cursor) {
        super(cursor);
    }

    @Override
    public Pojo peek() {
        return new Pojo(
                getInteger("some_int", -1),
                getLong("some_long", -1l),
                getFloat("some_float", -1f),
                getDouble("some_double", -1d),
                getShort("some_short", (short) -1),
                getBoolean("some_boolean", false),
                getBlob("some_byte_array", new byte[]{-1, -1}),
                getString("some_str", "-1")
        );
    }
}