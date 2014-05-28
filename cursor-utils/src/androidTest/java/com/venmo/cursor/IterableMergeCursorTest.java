package com.venmo.cursor;

import junit.framework.TestCase;

import static com.venmo.cursor.Util.cursorOf;
import static com.venmo.cursor.Util.iterationHelper;

public class IterableMergeCursorTest extends TestCase {

    public void testOneCursor() {
        IterableCursor<String> cursor = cursorOf("0", "1", "2");

        IterableMergeCursor<String> merged = new IterableMergeCursor<String>(cursor);
        iterationHelper(merged, 3);
    }

    public void testTwoCursors() {
        IterableCursor<String> first = cursorOf("0", "1", "2");
        IterableCursor<String> second = cursorOf("3", "4");

        IterableMergeCursor<String> merged = new IterableMergeCursor<String>(first, second);
        iterationHelper(merged, 5);
    }

    public void testThreeCursors() {
        IterableCursor<String> first = cursorOf("0", "1", "2");
        IterableCursor<String> second = cursorOf("3", "4");
        IterableCursor<String> third = cursorOf("5");

        IterableMergeCursor<String> merged = new IterableMergeCursor<String>(first, second, third);
        iterationHelper(merged, 6);
    }

    public void testFourCursors() {
        IterableCursor<String> first = cursorOf("0", "1", "2");
        IterableCursor<String> second = cursorOf("3", "4");
        IterableCursor<String> third = cursorOf("5");
        IterableCursor<String> fourth = cursorOf("6", "7");

        IterableMergeCursor<String> merged =
                new IterableMergeCursor<String>(first, second, third, fourth);
        iterationHelper(merged, 8);
    }

    public void testFiveCursors() {
        IterableCursor<String> first = cursorOf("0", "1", "2");
        IterableCursor<String> second = cursorOf("3", "4");
        IterableCursor<String> third = cursorOf("5");
        IterableCursor<String> fourth = cursorOf("6", "7");
        IterableCursor<String> fifth = cursorOf("8", "9", "10");

        IterableMergeCursor<String> merged =
                new IterableMergeCursor<String>(first, second, third, fourth, fifth);
        iterationHelper(merged, 11);
    }

    public void testNCursors() {
        IterableCursor<String> first = cursorOf("0", "1", "2");
        IterableCursor<String> second = cursorOf("3", "4");
        IterableCursor<String> third = cursorOf("5");
        IterableCursor<String> fourth = cursorOf("6", "7");
        IterableCursor<String> fifth = cursorOf("8", "9", "10");
        IterableCursor<String> sixth = cursorOf("11");

        @SuppressWarnings("unchecked") IterableMergeCursor<String> merged =
                new IterableMergeCursor<String>(first, second, third, fourth, fifth, sixth);
        iterationHelper(merged, 12);
    }

    public void testWithNulls() {
        IterableCursor<String> first = null;
        IterableCursor<String> second = cursorOf("0", "1", "2");
        IterableCursor<String> middle = null;
        IterableCursor<String> fourth = cursorOf("3", "4");
        IterableCursor<String> last = null;

        IterableMergeCursor<String> merged =
                new IterableMergeCursor<String>(first, second, middle, fourth, last);
        iterationHelper(merged, 5);
    }

}