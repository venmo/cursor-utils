package com.venmo.cursor.support;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.venmo.cursor.IterableCursor;

public abstract class IterableCursorAdapter<T> extends CursorAdapter {

    protected IterableCursorAdapter(Context context, IterableCursor<T> c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    protected IterableCursorAdapter(Context context, IterableCursor<T> c, int flags) {
        super(context, c, flags);
    }

    @Override
    public final View newView(Context context, Cursor cursor, ViewGroup parent) {
        return newView(context, getCursor(), parent);
    }

    public abstract View newView(Context context, IterableCursor<T> cursor, ViewGroup parent);

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        bindView(view, context, getCursor());
    }

    public abstract void bindView(View view, Context context, IterableCursor<T> cursor);

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        enforceIterableCursor(newCursor);
        return super.swapCursor(newCursor);
    }

    /**
     * Safe check for an {@link IterableCursor} - all cursors of this class must be.
     */
    @SuppressWarnings("unchecked")
    @Override
    public IterableCursor<T> getCursor() {
        return (IterableCursor<T>) super.getCursor();
    }

    @Override
    public T getItem(int position) {
        Object superResult = super.getItem(position); // checks validity and moves the cursor
        @SuppressWarnings("unchecked") IterableCursor<T> cursor = ((IterableCursor<T>) superResult);
        return cursor.peek();
    }

    private void enforceIterableCursor(Cursor cursor) {
        if (!(cursor instanceof IterableCursor)) {
            throw new IllegalArgumentException(
                    cursor.getClass().getName() + " is not an " + IterableCursor.class.getName());
        }
    }
}