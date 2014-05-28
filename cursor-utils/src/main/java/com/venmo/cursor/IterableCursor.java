package com.venmo.cursor;

import android.database.Cursor;

import java.util.Collection;
import java.util.Iterator;

/**
 * A convenient extension of the {@link Cursor} interface which provides two main benefits: (1)
 * simplifying the translation of a database row into a regular Java object, and (2) easily
 * iterating over a full SQLite query using Java's for-each loop. It is also useful in {@link
 * android.widget.CursorAdapter}s, cursor traversals, and interfacing a cursor as a Java {@link
 * Collection}.
 */
public interface IterableCursor<T> extends Cursor, Iterable<T> {

    /**
     * Turn the current row of this {@link Cursor} into an object. Use this to encapsulate the
     * translation of columns into an object's fields/constructor. This does not change the
     * position of the cursor.
     */
    public T peek();

    /**
     * Retrieve the object at the current position of the cursor and move forward one spot.
     */
    public T nextDocument();

    /**
     * Retrieve the previous object and move the cursor's position one spot backwards.
     */
    public T previousDocument();

    /**
     * Turn this cursor into an {@link Iterator}. <em>NOTE:</em> Since {@link Cursor}s are
     * inherently tied to their position, do not create multiple iterators per object or move the
     * underlying {@link Cursor} externally while also using an iterator. In order to iterate
     * multiple again, call {@link #moveToFirst()}.
     */
    @Override
    public Iterator<T> iterator();

}