package com.venmo.cursor;

import java.util.Iterator;

/**
 * Default iterator for all {@link IterableCursor}s.
 */
public class CursorIterator<T> implements Iterator<T> {

    private IterableCursor<T> mCursor;

    public CursorIterator(IterableCursor<T> cursor) {
        mCursor = cursor;
    }

    @Override
    public boolean hasNext() {
        return !mCursor.isBeforeFirst() && !mCursor.isAfterLast();
    }

    @Override
    public T next() {
        return mCursor.nextDocument();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove an object in a cursor");
    }
}