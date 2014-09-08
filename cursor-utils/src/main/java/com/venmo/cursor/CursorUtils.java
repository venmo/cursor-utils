package com.venmo.cursor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Utility class of default implementations for {@link IterableCursor} methods. If Android gets
 * support for Java 8, these can become default methods in the interface.
 */
public final class CursorUtils {

    private CursorUtils() {
        throw new UnsupportedOperationException("Non-instantiable class");
    }

    /**
     * Add each item of this {@link android.database.Cursor} to the {@code collection} parameter.
     * Closes the cursor once completed.
     *
     * @return the same collection as the parameter.
     * @see #consumeToArrayList(com.venmo.cursor.IterableCursor)
     * @see #consumeToLinkedList(com.venmo.cursor.IterableCursor)
     * @see #consumeToLinkedHashSet(com.venmo.cursor.IterableCursor)
     */
    public static <T, C extends Collection<T>> C consumeToCollection(IterableCursor<T> cursor,
            C collection) {
        try {
            for (T t : cursor) {
                collection.add(t);
            }
        } finally {
            cursor.close();
        }
        return collection;
    }

    /**
     * Returns an {@link java.util.ArrayList} of the {@link android.database.Cursor} and closes it.
     */
    public static <T> ArrayList<T> consumeToArrayList(IterableCursor<T> cursor) {
        return consumeToCollection(cursor, new ArrayList<T>(cursor.getCount()));
    }

    /**
     * Returns an {@link java.util.LinkedList} of the {@link android.database.Cursor} and closes
     * it.
     */
    public static <T> LinkedList<T> consumeToLinkedList(IterableCursor<T> cursor) {
        return consumeToCollection(cursor, new LinkedList<T>());
    }

    /**
     * Returns an {@link java.util.LinkedHashSet} of the {@link android.database.Cursor} and closes
     * it.
     */
    public static <T> LinkedHashSet<T> consumeToLinkedHashSet(IterableCursor<T> cursor) {
        return consumeToCollection(cursor, new LinkedHashSet<T>(cursor.getCount()));
    }

    /**
     * Returns a {@link IterableCursor} that preserves order of the initial cursor, but excludes
     * any object that is {@link T#equals(Object)} to any other item in {@code cursor}. {@code
     * cursor} will be closed on any {@link Exception}.
     *
     * @param cursor to filter for uniqueness
     */
    public static <T> IterableCursor<T> removeDuplicates(IterableCursor<T> cursor) {
        LinkedHashSet<T> linkedHashSet = new LinkedHashSet<T>();
        try {
            for (T t : cursor) {
                linkedHashSet.add(t);
            }
        } finally {
            cursor.close();
        }
        CursorList<T> unique = new CursorList<T>(linkedHashSet.size());
        unique.addAll(linkedHashSet);
        return unique;
    }

    public static <T> T nextDocumentHelper(IterableCursor<T> cursor) {
        T t = cursor.peek();
        cursor.moveToNext();
        return t;
    }

    public static <T> T previousDocumentHelper(IterableCursor<T> cursor) {
        cursor.moveToPrevious();
        return cursor.peek();
    }
}
