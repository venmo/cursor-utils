package com.venmo.cursor.test;

import android.database.Cursor;

import com.venmo.cursor.IterableCursorWrapper;

/**
 * Created by ronshapiro on 6/16/14.
 */
public class PojoCursor extends IterableCursorWrapper<Pojo> {

    public PojoCursor(Cursor cursor) {
        super(cursor);
    }

    @Override
    public Pojo peek() {
        return new Pojo(
                getIntegerHelper("some_int", -1),
                getLongHelper("some_long", -1l),
                getFloatHelper("some_float", -1f),
                getDoubleHelper("some_double", -1d),
                getShortHelper("some_short", (short) -1),
                getBooleanHelper("some_boolean", false),
                getBlobHelper("some_byte_array", new byte[]{-1, -1}),
                getStringHelper("some_str", "-1")
        );
    }
}