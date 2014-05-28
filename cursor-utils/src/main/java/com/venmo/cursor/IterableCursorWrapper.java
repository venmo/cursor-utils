package com.venmo.cursor;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Iterator;

import static com.venmo.cursor.CursorUtils.nextDocumentHelper;
import static com.venmo.cursor.CursorUtils.previousDocumentHelper;

public abstract class IterableCursorWrapper<T> extends CursorWrapper implements IterableCursor<T> {

    private static final int SQLITE_TRUE = 1;
    private static final int SQLITE_FALSE = 0;

    /**
     * Convenience class to create a {@link IterableCursor} backed by the {@link Cursor} {@code
     * cursor}.
     *
     * @see CursorWrapper
     */
    public IterableCursorWrapper(Cursor cursor) {
        super(cursor);
        moveToFirst();
    }

    @Override
    public T nextDocument() {
        return nextDocumentHelper(this);
    }

    @Override
    public T previousDocument() {
        return previousDocumentHelper(this);

    }

    /** Convenience alias {@code getCount() == 0} */
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new CursorIterator<T>(this);
    }

    /**
     * Booleans in SQLite are handled as {@code int}s. Use this as a convenience to retrieve a
     * boolean from a column.
     */
    public boolean getBooleanHelper(String columnName, boolean defaultValue) {
        int defaultValueAsInt = (defaultValue) ? SQLITE_TRUE : SQLITE_FALSE;

        return getIntegerHelper(columnName, defaultValueAsInt) == SQLITE_TRUE;
    }

    /**
     * Convenience alias to {@code getString\(getColumnIndex(columnName))}. If the column does not
     * exist for the cursor, return {@code defaultValue}.
     */
    public String getStringHelper(String columnName, String defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getString(index);
        } else {
            return defaultValue;
        }
    }

    /**
     * Convenience alias to {@code getLong\(getColumnIndex(columnName))}. If the column does not
     * exist for the cursor, return {@code defaultValue}.
     */
    public long getLongHelper(String columnName, long defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getLong(index);
        } else {
            return defaultValue;
        }
    }

    /**
     * Convenience alias to {@code getInt\(getColumnIndex(columnName))}. If the column does not
     * exist for the cursor, return {@code defaultValue}.
     */
    public int getIntegerHelper(String columnName, int defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getInt(index);
        } else {
            return defaultValue;
        }
    }

    public double getDoubleHelper(String columnName, double defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getDouble(index);
        } else {
            return defaultValue;
        }
    }

    public byte[] getBlobHelper(String columnName, byte[] defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getBlob(index);
        } else {
            return defaultValue;
        }
    }

    public float getFloatHelper(String columnName, float defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getFloat(index);
        } else {
            return defaultValue;
        }
    }

    public short getShortHelper(String columnName, short defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getShort(index);
        } else {
            return defaultValue;
        }
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < getColumnCount();
    }

}
