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
        return newView(context, getCursor().peek(), parent);
    }

    public abstract View newView(Context context, T t, ViewGroup parent);

    @Override
    public final void bindView(View view, Context context, Cursor cursor) {
        T t = getCursor().peek();
        bindView(view, context, t);
    }

    public abstract void bindView(View view, Context context, T t);

    @Override
    public IterableCursor<T> runQueryOnBackgroundThread(CharSequence constraint) {
        Cursor cursor = super.runQueryOnBackgroundThread(constraint);
        return enforceIterableCursor(cursor);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        enforceIterableCursor(newCursor);
        return super.swapCursor(newCursor);
    }

    @Override
    public IterableCursor<T> getCursor() {
        return cast(super.getCursor());
    }

    @Override
    public T getItem(int position) {
        Object superResult = super.getItem(position); // checks validity and moves the cursor
        @SuppressWarnings("unchecked") IterableCursor<T> cursor = ((IterableCursor<T>) superResult);
        return cursor.peek();
    }

    private IterableCursor<T> enforceIterableCursor(Cursor cursor) {
        if (cursor == null) return null;
        if (!(cursor instanceof IterableCursor)) {
            throw new IllegalArgumentException(
                    cursor.getClass().getName() + " is not an " + IterableCursor.class.getName());
        }
        return cast(cursor);
    }

    private IterableCursor<T> cast(Cursor cursor) {
        @SuppressWarnings("unchecked") IterableCursor<T> casted = (IterableCursor<T>) cursor;
        return casted;
    }
}