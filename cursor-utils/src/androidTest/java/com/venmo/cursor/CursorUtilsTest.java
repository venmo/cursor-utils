package com.venmo.cursor;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class CursorUtilsTest extends TestCase {

    public void testConsumeToArrayList() {
        List<String> initial = Arrays.asList("1", "2", "3");

        IterableCursor<String> cursor = new CursorList<String>(initial);
        ArrayList<String> copy = CursorUtils.consumeToArrayList(cursor);
        assertEquals(initial, copy);
    }

    public void testConsumeToLinkedList() {
        List<String> initial = Arrays.asList("1", "2", "3");

        IterableCursor<String> cursor = new CursorList<String>(initial);
        LinkedList<String> copy = CursorUtils.consumeToLinkedList(cursor);
        assertEquals(initial, copy);
    }

    public void testConsumeToLinkedHashSet() {
        List<String> initial = Arrays.asList("1", "2", "2", "3", "4", "3");

        IterableCursor<String> cursor = new CursorList<String>(initial);
        LinkedHashSet<String> copy = CursorUtils.consumeToLinkedHashSet(cursor);
        LinkedHashSet<String> expected = new LinkedHashSet<String>();
        expected.addAll(Arrays.asList("1", "2", "3", "4"));

        assertEquals(expected, copy);

        // assert ordered
        int i = 1;
        for (String str : copy) {
            assertEquals(i + "", str);
            i++;
        }
    }

    public void testRemoveDuplicates() {
        List<String> initial = Arrays.asList("1", "2", "2", "3", "3", "3", "4");
        IterableCursor<String> cursor = new CursorList<String>(initial);

        IterableCursor<String> unique = CursorUtils.removeDuplicates(cursor);
        assertTrue(cursor.isClosed());
        assertEquals(4, unique.getCount());
        assertEquals("1", unique.nextDocument());
        assertEquals("2", unique.nextDocument());
        assertEquals("3", unique.nextDocument());
        assertEquals("4", unique.nextDocument());
    }

}