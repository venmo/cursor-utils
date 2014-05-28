package com.venmo.cursor;

import android.database.MergeCursor;

import java.util.Iterator;

import static com.venmo.cursor.CursorUtils.nextDocumentHelper;
import static com.venmo.cursor.CursorUtils.previousDocumentHelper;

public class IterableMergeCursor<T> extends MergeCursor implements IterableCursor<T> {

    private final IterableCursor<T>[] mCursors;
    private IterableCursor<T> mCurrent;

    public IterableMergeCursor(IterableCursor<T> cursor) {
        this(asArray(cursor));
    }

    @SuppressWarnings("unchecked")
    public IterableMergeCursor(IterableCursor<T> first, IterableCursor<T> second) {
        this(IterableMergeCursor.<T>asArray(first, second));
    }

    @SuppressWarnings("unchecked")
    public IterableMergeCursor(IterableCursor<T> first, IterableCursor<T> second,
            IterableCursor<T> third) {
        this(IterableMergeCursor.<T>asArray(first, second, third));
    }

    @SuppressWarnings("unchecked")
    public IterableMergeCursor(IterableCursor<T> first, IterableCursor<T> second,
            IterableCursor<T> third, IterableCursor<T> fourth) {
        this(IterableMergeCursor.<T>asArray(first, second, third, fourth));
    }

    @SuppressWarnings("unchecked")
    public IterableMergeCursor(IterableCursor<T> first, IterableCursor<T> second,
            IterableCursor<T> third, IterableCursor<T> fourth, IterableCursor<T> fifth) {
        this(IterableMergeCursor.<T>asArray(first, second, third, fourth, fifth));
    }

    public IterableMergeCursor(IterableCursor<T>... cursors) {
        super(cursors);
        mCursors = cursors;
        moveToFirst();
    }

    @SuppressWarnings("unchecked")
    private static <T> IterableCursor<T>[] asArray(IterableCursor<T> cursor) {
        return (IterableCursor<T>[]) new IterableCursor[]{cursor};
    }

    @SuppressWarnings("unchecked")
    private static <T> IterableCursor<T>[] asArray(IterableCursor... cursor) {
        return (IterableCursor<T>[]) cursor;
    }

    @Override
    public boolean onMove(int oldPosition, int newPosition) {
        int newPositionCopy = newPosition;
        for (IterableCursor<T> cursor : mCursors) {
            if (cursor != null) {
                if (newPositionCopy < cursor.getCount()) {
                    mCurrent = cursor;
                    break;
                } else {
                    newPositionCopy -= cursor.getCount();
                }
            }
        }
        return super.onMove(oldPosition, newPosition);
    }

    @Override
    public T peek() {
        return mCurrent.peek();
    }

    @Override
    public T nextDocument() {
        return nextDocumentHelper(this);
    }

    @Override
    public T previousDocument() {
        return previousDocumentHelper(this);
    }

    @Override
    public Iterator<T> iterator() {
        return new CursorIterator<T>(this);
    }

}
