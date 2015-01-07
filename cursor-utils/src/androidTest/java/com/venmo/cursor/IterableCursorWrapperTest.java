package com.venmo.cursor;

import com.venmo.cursor.test.Pojo;
import com.venmo.cursor.test.PojoCursor;
import com.venmo.cursor.test.TestDb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class IterableCursorWrapperTest {

    private static final float DELTA = .00001f;

    @Test
    public void retrievalHelpers() {
        TestDb db = new TestDb(Robolectric.application);

        db.insertRow(1, 1l, 1.1f, 1.2d, (short) 1, true, new byte[]{1, 2, 3}, "a");
        IterableCursorWrapper<?> cursor = new IterableCursorWrapper<Object>(db.query()) {
            @Override
            public Object peek() {
                return null;
            }
        };

        String strFound = cursor.getString("some_str", "not_found");
        assertEquals("a", strFound);
        String strNotFound = cursor.getString("other", "not_found");
        assertEquals("not_found", strNotFound);

        int intFound = cursor.getInteger("some_int", -1);
        assertEquals(1, intFound);
        int intNotFound = cursor.getInteger("other", -1);
        assertEquals(-1, intNotFound);

        long longFound = cursor.getLong("some_long", -1l);
        assertEquals(1l, longFound);
        long longNotFound = cursor.getLong("other", -1l);
        assertEquals(-1l, longNotFound);

        boolean booleanFound = cursor.getBoolean("some_boolean", false);
        assertEquals(true, booleanFound);
        boolean booleanNotFound = cursor.getBoolean("other", false);
        assertEquals(false, booleanNotFound);

        float floatFound = cursor.getFloat("some_float", 2.0f);
        assertEquals(1.1f, floatFound, DELTA);
        float floatNotFound = cursor.getFloat("other", 2.0f);
        assertEquals(2.0f, floatNotFound, DELTA);

        double doubleFound = cursor.getDouble("some_double", 3.0d);
        assertEquals(1.2d, doubleFound, DELTA);
        double doubleNotFound = cursor.getDouble("other", 3.0d);
        assertEquals(3.0d, doubleNotFound, DELTA);

        short shortFound = cursor.getShort("some_short", (short) 4);
        assertEquals((short) 1, shortFound);
        short shortNotFound = cursor.getShort("other", (short) 4);
        assertEquals((short) 4, shortNotFound);

        byte[] blobFound = cursor.getBlob("some_byte_array", new byte[]{4, 5, 6});
        assertTrue(Arrays.equals(new byte[]{1, 2, 3}, blobFound));
        byte[] blobNotFound = cursor.getBlob("other", new byte[]{4, 5, 6});
        assertTrue(Arrays.equals(new byte[]{4, 5, 6}, blobNotFound));
    }

    @Test
    public void iterating() {
        TestDb db = new TestDb(Robolectric.application);

        db.insertRow(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0");
        db.insertRow(1, 1l, 1f, 1d, (short) 1, true, new byte[]{1, 1}, "1");

        IterableCursor<Pojo> cursor = new PojoCursor(db.query());

        Pojo[] samples = new Pojo[]{
                new Pojo(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0"),
                new Pojo(1, 1l, 1f, 1d, (short) 1, true, new byte[]{1, 1}, "1"),
        };

        iterationHelper(cursor, samples);
    }

    @Test
    public void merging() {
        TestDb db0 = new TestDb(Robolectric.application);
        TestDb db1 = new TestDb(Robolectric.application);

        db0.insertRow(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0");
        db0.insertRow(1, 1l, 1f, 1d, (short) 1, true, new byte[]{1, 1}, "1");
        db1.insertRow(2, 2l, 2f, 2d, (short) 2, true, new byte[]{2, 2}, "2");
        db1.insertRow(3, 3l, 3f, 3d, (short) 3, true, new byte[]{3, 3}, "3");

        IterableCursor<Pojo> cursor = new IterableMergeCursor<Pojo>(
                new PojoCursor(db0.query()), new PojoCursor(db1.query()));

        Pojo[] samples = new Pojo[]{
                new Pojo(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0"),
                new Pojo(1, 1l, 1f, 1d, (short) 1, true, new byte[]{1, 1}, "1"),
                new Pojo(2, 2l, 2f, 2d, (short) 2, true, new byte[]{2, 2}, "2"),
                new Pojo(3, 3l, 3f, 3d, (short) 3, true, new byte[]{3, 3}, "3")
        };

        iterationHelper(cursor, samples);
    }

    @Test
    public void isEmpty() {
        TestDb db0 = new TestDb(Robolectric.application);
        TestDb db1 = new TestDb(Robolectric.application);

        db0.insertRow(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0");

        IterableCursorWrapper<Pojo> cursor0 = new PojoCursor(db0.query());
        IterableCursorWrapper<Pojo> cursor1 = new PojoCursor(db1.query());

        assertFalse(cursor0.isEmpty());
        assertTrue(cursor1.isEmpty());
    }

    @Test
    public void movingAround() {
        TestDb db = new TestDb(Robolectric.application);

        db.insertRow(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0");
        db.insertRow(1, 1l, 1f, 1d, (short) 1, true, new byte[]{1, 1}, "1");
        db.insertRow(2, 2l, 2f, 2d, (short) 2, true, new byte[]{2, 2}, "2");
        db.insertRow(3, 3l, 3f, 3d, (short) 3, true, new byte[]{3, 3}, "3");

        IterableCursorWrapper<Pojo> cursor = new PojoCursor(db.query());

        Pojo p0 = new Pojo(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0");
        Pojo p1 = new Pojo(1, 1l, 1f, 1d, (short) 1, true, new byte[]{1, 1}, "1");
        Pojo p2 = new Pojo(2, 2l, 2f, 2d, (short) 2, true, new byte[]{2, 2}, "2");
        Pojo p3 = new Pojo(3, 3l, 3f, 3d, (short) 3, true, new byte[]{3, 3}, "3");

        assertEquals(p0, cursor.nextDocument());
        assertEquals(p0, cursor.previousDocument());

        assertEquals(p0, cursor.nextDocument());
        assertEquals(p1, cursor.nextDocument());
        assertEquals(p2, cursor.nextDocument());
        assertEquals(p3, cursor.nextDocument());
        assertEquals(p3, cursor.previousDocument());
        assertEquals(p2, cursor.previousDocument());
        assertEquals(p1, cursor.previousDocument());
        assertEquals(p0, cursor.previousDocument());

        assertEquals(p0, cursor.nextDocument());
        assertEquals(p1, cursor.nextDocument());
        assertEquals(p2, cursor.nextDocument());
        assertEquals(p3, cursor.nextDocument());
    }

    @Test
    public void movingAroundWithOnlyOneItem() {
        TestDb db = new TestDb(Robolectric.application);

        db.insertRow(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0");

        IterableCursorWrapper<Pojo> cursor = new PojoCursor(db.query());

        Pojo p0 = new Pojo(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0");

        assertEquals(p0, cursor.nextDocument());
        assertEquals(p0, cursor.previousDocument());
    }

    private void iterationHelper(IterableCursor<Pojo> cursor, Pojo[] samples) {
        int i = 0;
        for (Pojo pojo : cursor) {
            Pojo expected = samples[i];
            assertEquals(expected, pojo);
            i++;
        }
        assertEquals(samples.length, i);
    }

}