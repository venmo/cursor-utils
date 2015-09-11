package com.venmo.cursor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// TODO(ronshapiro): fail if regexp "import static junit.framework.*" is found
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
    sdk = 21)
public class CursorUtilsTest {

    @Test
    public void consumeToArrayList() {
        List<String> initial = Arrays.asList("1", "2", "3");

        IterableCursor<String> cursor = new CursorList<String>(initial);
        ArrayList<String> copy = CursorUtils.consumeToArrayList(cursor);
        assertEquals(initial, copy);
    }

    @Test
    public void consumeToLinkedList() {
        List<String> initial = Arrays.asList("1", "2", "3");

        IterableCursor<String> cursor = new CursorList<String>(initial);
        LinkedList<String> copy = CursorUtils.consumeToLinkedList(cursor);
        assertEquals(initial, copy);
    }

    @Test
    public void consumeToLinkedHashSet() {
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

    @Test
    public void removeDuplicates() {
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
