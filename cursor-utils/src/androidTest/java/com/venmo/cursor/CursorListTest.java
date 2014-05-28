package com.venmo.cursor;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CursorListTest extends TestCase {

    public void testAccessingItemInListDoesNotModifyPosition() {
        List<String> list = buildList();

        CursorList<String> cursorList = new CursorList<String>(list);
        int i = 0;
        for (String str : cursorList) {
            assertEquals(i + "", str);
            assertEquals("2", cursorList.get(2));
            assertEquals("2", list.get(2));
            i++;
        }

        assertEquals(3, i);
    }

    public void testHasColumnFor_id() {
        CursorList<String> cursor = new CursorList<String>(buildList());
        int index = cursor.getColumnIndexOrThrow("_id");
        String name = cursor.getColumnName(index);
        assertEquals("_id", name);

        boolean found = false;
        for (String column : cursor.getColumnNames()) {
            if (column.equals("_id")) {
                found = true;
            }
        }
        assertTrue(found);

        long id = 0;
        for (String str : cursor) {
            assertEquals(id, cursor.getLong(index));
        }

        assertFalse(cursor.isNull(index));
    }

    public void testIsClosedAfterClosed() {
        CursorList<String> cursor = new CursorList<String>(buildList());
        cursor.close();
        assertTrue(cursor.isClosed());
    }

    public void testSubListIsIterable() {
        CursorList<String> cursor = new CursorList<String>(buildList()).subList(1, 3);

        int i = 0;
        for (String str : cursor) {
            assertEquals((i + 1) + "", str);
            i++;
        }
        assertEquals(2, i);
    }

    public void testDecoratedListMustBeNonNull() {
        try {
            new CursorList<String>(Arrays.asList("1", "2"));
        } catch (Exception e) {
            fail();
        }

        try {
            new CursorList<String>(new ArrayList<String>());
        } catch (Exception e) {
            fail("Empty lists are ok");
        }

        try {
            new CursorList<String>(null);
            fail("This should throw an exception");
        } catch (Exception expected) {
            // expected
        }
    }

    private List<String> buildList() {
        List<String> list = new ArrayList<String>();
        list.add("0");
        list.add("1");
        list.add("2");
        return list;
    }
}