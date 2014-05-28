package com.venmo.cursor;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.venmo.cursor.CursorUtils.nextDocumentHelper;
import static com.venmo.cursor.CursorUtils.previousDocumentHelper;

/**
 * A convenience class to turn a {@link List} into a {@link android.database.Cursor}. Useful to
 * combine one data set which is backed by a {@link List} with another which is a backed by a
 * {@link
 * android.database.Cursor} using a {@link com.venmo.cursor.IterableMergeCursor}.
 */
public class CursorList<E> implements List<E>, IterableCursor<E> {

    private static final String _ID = "_id";
    private static final int _ID_INDEX = 0;

    private List<E> mList;
    private int mPosition = 0;

    /**
     * Decorate the {@code list} as both a {@link android.database.Cursor} and also a {@link List}
     */
    public CursorList(List<E> list) {
        if (list == null) {
            throw new NullPointerException("List parameter must be non-null");
        }
        mList = list;
    }

    @Override
    public E peek() {
        return mList.get(mPosition);
    }

    @Override
    public E nextDocument() {
        return nextDocumentHelper(this);
    }

    @Override
    public E previousDocument() {
        return previousDocumentHelper(this);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getPosition() {
        return mPosition;
    }

    @Override
    public boolean move(int offset) {
        mPosition += offset;
        return clampPosition();
    }

    @Override
    public boolean moveToPosition(int position) {
        mPosition = position;
        return clampPosition();
    }

    /**
     * Helper for keeping {@link #mPosition} in bounds.
     *
     * @return false if mPosition was modified, true otherwise
     */
    private boolean clampPosition() {
        if (mPosition < 0) {
            mPosition = -1;
            return false;
        } else if (mPosition > mList.size()) { // TODO should this be >= instead of >
            mPosition = mList.size();
            return false;
        }
        return true;
    }

    @Override
    public boolean moveToFirst() {
        mPosition = 0;
        return mList.isEmpty();
    }

    @Override
    public boolean moveToLast() {
        mPosition = mList.size() - 1;
        return mList.isEmpty();
    }

    @Override
    public boolean moveToNext() {
        return move(1);
    }

    @Override
    public boolean moveToPrevious() {
        return move(-1);
    }

    @Override
    public boolean isFirst() {
        return mPosition == 0;
    }

    @Override
    public boolean isLast() {
        return mPosition == (mList.size() - 1);
    }

    @Override
    public boolean isBeforeFirst() {
        return mPosition < 0;
    }

    @Override
    public boolean isAfterLast() {
        return mPosition >= mList.size();
    }

    /**
     * Unsupported since {@link CursorList}s aren't backed by columns.
     */
    @Override
    public int getColumnIndex(String columnName) {
        if (_ID.equals(columnName)) {
            return _ID_INDEX;
        }
        return -1;
    }

    /**
     * Unsupported since {@link CursorList}s aren't backed by columns.
     */
    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        int result = getColumnIndex(columnName);
        if (result == -1) {
            throw new IllegalArgumentException("CursorList is not backed by columns.");
        }
        return result;
    }

    /**
     * Unsupported since {@link CursorList}s aren't backed by columns.
     */
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == _ID_INDEX) {
            return _ID;
        }
        return null;
    }

    /**
     * Unsupported since {@link CursorList}s aren't backed by columns.
     */
    @Override
    public String[] getColumnNames() {
        return new String[]{_ID};
    }

    /**
     * Unsupported since {@link CursorList}s aren't backed by columns.
     */
    @Override
    public int getColumnCount() {
        return 1;
    }

    /**
     * Unsupported since {@link CursorList}s aren't backed by columns.
     */
    @Override
    public byte[] getBlob(int columnIndex) {
        throw new IllegalArgumentException("CursorList is not backed by columns.");
    }

    @Override
    public String getString(int columnIndex) {
        throw new IllegalArgumentException("CursorList is not backed by columns.");
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        throw new IllegalArgumentException("CursorList is not backed by columns.");
    }

    @Override
    public short getShort(int columnIndex) {
        throw new IllegalArgumentException("CursorList is not backed by columns.");
    }

    @Override
    public int getInt(int columnIndex) {
        throw new IllegalArgumentException("CursorList is not backed by columns.");
    }

    @Override
    public long getLong(int columnIndex) {
        if (columnIndex == _ID_INDEX) {
            return mPosition;
        }
        throw new IllegalArgumentException("CursorList is not backed by columns.");
    }

    @Override
    public float getFloat(int columnIndex) {
        throw new IllegalArgumentException("CursorList is not backed by columns.");
    }

    @Override
    public double getDouble(int columnIndex) {
        throw new IllegalArgumentException("CursorList is not backed by columns.");
    }

    @Override
    public int getType(int columnIndex) {
        throw new IllegalArgumentException("CursorList is not backed by columns.");
    }

    @Override
    public boolean isNull(int columnIndex) {
        if (columnIndex == _ID_INDEX) {
            return false;
        }
        throw new IllegalArgumentException("CursorList is not backed by columns.");
    }

    @Deprecated
    @Override
    public void deactivate() {
        // noop
    }

    @Deprecated
    @Override
    public boolean requery() {
        // noop
        return false;
    }

    @Override
    public void close() {
        mList = null;
    }

    @Override
    public boolean isClosed() {
        return mList == null;
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {
        // noop
    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {
        // noop
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        // noop
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        // noop
    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {
        // noop
    }

    @Override
    public Uri getNotificationUri() {
        return null;
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return false;
    }

    @Override
    public Bundle getExtras() {
        return Bundle.EMPTY;
    }

    @Override
    public Bundle respond(Bundle extras) {
        return Bundle.EMPTY;
    }

    @Override
    public void add(int location, E object) {
        mList.add(location, object);
    }

    @Override
    public boolean add(E object) {
        return mList.add(object);
    }

    @Override
    public boolean addAll(int location, Collection<? extends E> collection) {
        return mList.addAll(location, collection);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return mList.addAll(collection);
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @Override
    public boolean contains(Object object) {
        return mList.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return mList.containsAll(collection);
    }

    @Override
    public E get(int location) {
        return mList.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return mList.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return mList.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return mList.lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator() {
        return mList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int location) {
        return mList.listIterator(location);
    }

    @Override
    public E remove(int location) {
        return mList.remove(location);
    }

    @Override
    public boolean remove(Object object) {
        return mList.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return mList.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return mList.retainAll(collection);
    }

    @Override
    public E set(int location, E object) {
        return mList.set(location, object);
    }

    @Override
    public int size() {
        return mList.size();
    }

    @Override
    public CursorList<E> subList(int start, int end) {
        return new CursorList<E>(mList.subList(start, end));
    }

    @Override
    public Object[] toArray() {
        return mList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return mList.toArray(array);
    }
}
