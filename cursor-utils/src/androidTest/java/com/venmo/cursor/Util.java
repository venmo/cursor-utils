package com.venmo.cursor;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class Util {

    public static void iterationHelper(IterableCursor<String> cursor, int count) {
        assertEquals(count, cursor.getCount());

        int i = 0;
        for (String str : cursor) {
            assertEquals(String.valueOf(i), str);
            i++;
        }
        assertEquals(count, i);
    }

    public static IterableCursor<String> cursorOf(String... strings) {
        return new CursorList<String>(Arrays.asList(strings));
    }
}
