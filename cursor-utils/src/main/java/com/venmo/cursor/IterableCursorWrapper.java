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
    public boolean getBoolean(String columnName, boolean defaultValue) {
        int defaultValueAsInt = (defaultValue) ? SQLITE_TRUE : SQLITE_FALSE;

        return getInteger(columnName, defaultValueAsInt) == SQLITE_TRUE;
    }

    /**
     * @deprecated use {@link #getBoolean(String, boolean)}
     */
    @Deprecated
    public boolean getBooleanHelper(String columnName, boolean defaultValue) {
        return getBoolean(columnName, defaultValue);
    }

    /**
     * Convenience alias to {@code getString\(getColumnIndex(columnName))}. If the column does not
     * exist for the cursor, return {@code defaultValue}.
     */
    public String getString(String columnName, String defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getString(index);
        } else {
            return defaultValue;
        }
    }

    /**
     * @deprecated use {@link #getString(String, String)}
     */
    public String getStringHelper(String columnName, String defaultValue) {
        return getString(columnName, defaultValue);
    }

    /**
     * Convenience alias to {@code getLong\(getColumnIndex(columnName))}. If the column does not
     * exist for the cursor, return {@code defaultValue}.
     */
    public long getLong(String columnName, long defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getLong(index);
        } else {
            return defaultValue;
        }
    }

    /**
     * @deprecated use {@link #getLong(String, long)}
     */
    public long getLongHelper(String columnName, long defaultValue) {
        return getLong(columnName, defaultValue);
    }

    /**
     * Convenience alias to {@code getInt\(getColumnIndex(columnName))}. If the column does not
     * exist for the cursor, return {@code defaultValue}.
     */
    public int getInteger(String columnName, int defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getInt(index);
        } else {
            return defaultValue;
        }
    }

    /**
     * @deprecated use {@link #getInteger(String, int)}
     */
    public int getIntegerHelper(String columnName, int defaultValue) {
        return getInteger(columnName, defaultValue);
    }

    /**
     * Convenience alias to {@code getDouble\(getColumnIndex(columnName))}. If the column does not
     * exist for the cursor, return {@code defaultValue}.
     */
    public double getDouble(String columnName, double defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getDouble(index);
        } else {
            return defaultValue;
        }
    }

    /**
     * @deprecated use {@link #getDouble(String, double)}
     */
    public double getDoubleHelper(String columnName, double defaultValue) {
        return getDouble(columnName, defaultValue);
    }

    /**
     * Convenience alias to {@code getBlob\(getColumnIndex(columnName))}. If the column does not
     * exist for the cursor, return {@code defaultValue}.
     */
    public byte[] getBlob(String columnName, byte[] defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getBlob(index);
        } else {
            return defaultValue;
        }
    }

    /**
     * @deprecated use {@link #getBlob(String, byte[])}
     */
    public byte[] getBlobHelper(String columnName, byte[] defaultValue) {
        return getBlob(columnName, defaultValue);
    }

    /**
     * Convenience alias to {@code getFloat\(getColumnIndex(columnName))}. If the column does not
     * exist for the cursor, return {@code defaultValue}.
     */
    public float getFloat(String columnName, float defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getFloat(index);
        } else {
            return defaultValue;
        }
    }

    /**
     * @deprecated use {@link #getFloat(String, float)}
     */
    public float getFloatHelper(String columnName, float defaultValue) {
        return getFloat(columnName, defaultValue);
    }

    /**
     * Convenience alias to {@code getShort\(getColumnIndex(columnName))}. If the column does not
     * exist for the cursor, return {@code defaultValue}.
     */
    public short getShort(String columnName, short defaultValue) {
        int index = getColumnIndex(columnName);
        if (isValidIndex(index)) {
            return getShort(index);
        } else {
            return defaultValue;
        }
    }

    /**
     * @deprecated use {@link #getShort(String, short)}
     */
    public short getShortHelper(String columnName, short defaultValue) {
        return getShort(columnName, defaultValue);
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < getColumnCount();
    }

}
