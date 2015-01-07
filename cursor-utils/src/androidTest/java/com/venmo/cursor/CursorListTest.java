package com.venmo.cursor;

import com.venmo.cursor.test.Pojo;
import com.venmo.cursor.test.PojoCursor;
import com.venmo.cursor.test.TestDb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class CursorListTest {

    @Test
    public void accessingItemInListDoesNotModifyPosition() {
        List<String> list = buildList();

        CursorList<String> cursorList = new CursorList<>(list);
        int i = 0;
        for (String str : cursorList) {
            assertEquals(i + "", str);
            assertEquals("2", cursorList.get(2));
            assertEquals("2", list.get(2));
            i++;
        }

        assertEquals(3, i);
    }

    @Test
    public void hasColumnFor_id() {
        CursorList<String> cursor = new CursorList<>(buildList());
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
        for (String ignored : cursor) {
            assertEquals(id, cursor.getLong(index));
        }

        assertFalse(cursor.isNull(index));
    }

    @Test
    public void isClosedAfterClosed() {
        CursorList<String> cursor = new CursorList<>(buildList());
        cursor.close();
        assertTrue(cursor.isClosed());
    }

    @Test
    public void subListIsIterable() {
        CursorList<String> cursor = new CursorList<>(buildList()).subList(1, 3);

        int i = 0;
        for (String str : cursor) {
            assertEquals((i + 1) + "", str);
            i++;
        }
        assertEquals(2, i);
    }

    @Test
    public void decoratedListMustBeNonNull() {
        try {
            new CursorList<>(Arrays.asList("1", "2"));
        } catch (Exception e) {
            fail();
        }

        try {
            new CursorList<>(new ArrayList<String>());
        } catch (Exception e) {
            fail("Empty lists are ok");
        }

        try {
            new CursorList<>((List<String>) null);
            fail("This should throw an exception");
        } catch (Exception expected) {
            // expected
        }
    }

    @Test
    public void closingIsClosedAreInSync() {
        CursorList<String> cursor = new CursorList<>(buildList());

        assertFalse(cursor.isClosed());
        cursor.close();
        assertTrue(cursor.isClosed());
    }

    @Test
    public void canCloneOtherCursor() {
        TestDb db = new TestDb(Robolectric.application);
        db.insertRow(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0");
        db.insertRow(1, 1l, 1f, 1d, (short) 1, true, new byte[]{1, 1}, "1");

        IterableCursor<Pojo> initial = new PojoCursor(db.query());
        CursorList<Pojo> wrapper = new CursorList<>(initial);

        assertEquals(2, wrapper.size());
        assertFalse(initial.isClosed());

        IterableCursor<Pojo> moved = new PojoCursor(db.query());
        moved.moveToNext();
        CursorList<Pojo> movedThenWrapped = new CursorList<>(initial);
        assertEquals(2, movedThenWrapped.size());
    }

    private List<String> buildList() {
        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        return list;
    }
}