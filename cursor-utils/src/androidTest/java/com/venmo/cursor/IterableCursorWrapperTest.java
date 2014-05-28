package com.venmo.cursor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;

import java.util.Arrays;
import java.util.Random;

public class IterableCursorWrapperTest extends AndroidTestCase {

    public void testRetrievalHelpers() {
        TestDb db = new TestDb(getContext());

        db.insertRow(1, 1l, 1.1f, 1.2d, (short) 1, true, new byte[]{1, 2, 3}, "a");
        IterableCursorWrapper<?> cursor = new IterableCursorWrapper<Object>(db.query()) {
            @Override
            public Object peek() {
                return null;
            }
        };

        String strFound = cursor.getStringHelper("some_str", "not_found");
        assertEquals("a", strFound);
        String strNotFound = cursor.getStringHelper("other", "not_found");
        assertEquals("not_found", strNotFound);

        int intFound = cursor.getIntegerHelper("some_int", -1);
        assertEquals(1, intFound);
        int intNotFound = cursor.getIntegerHelper("other", -1);
        assertEquals(-1, intNotFound);

        long longFound = cursor.getLongHelper("some_long", -1l);
        assertEquals(1l, longFound);
        long longNotFound = cursor.getLongHelper("other", -1l);
        assertEquals(-1l, longNotFound);

        boolean booleanFound = cursor.getBooleanHelper("some_boolean", false);
        assertEquals(true, booleanFound);
        boolean booleanNotFound = cursor.getBooleanHelper("other", false);
        assertEquals(false, booleanNotFound);

        float floatFound = cursor.getFloatHelper("some_float", 2.0f);
        assertEquals(1.1f, floatFound);
        float floatNotFound = cursor.getFloatHelper("other", 2.0f);
        assertEquals(2.0f, floatNotFound);

        double doubleFound = cursor.getDoubleHelper("some_double", 3.0d);
        assertEquals(1.2d, doubleFound);
        double doubleNotFound = cursor.getDoubleHelper("other", 3.0d);
        assertEquals(3.0d, doubleNotFound);

        short shortFound = cursor.getShortHelper("some_short", (short) 4);
        assertEquals((short) 1, shortFound);
        short shortNotFound = cursor.getShortHelper("other", (short) 4);
        assertEquals((short) 4, shortNotFound);

        byte[] blobFound = cursor.getBlobHelper("some_byte_array", new byte[]{4, 5, 6});
        assertTrue(Arrays.equals(new byte[]{1, 2, 3}, blobFound));
        byte[] blobNotFound = cursor.getBlobHelper("other", new byte[]{4, 5, 6});
        assertTrue(Arrays.equals(new byte[]{4, 5, 6}, blobNotFound));
    }

    public void testIterating() {
        TestDb db = new TestDb(getContext());

        db.insertRow(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0");
        db.insertRow(1, 1l, 1f, 1d, (short) 1, true, new byte[]{1, 1}, "1");

        IterableCursor<Pojo> cursor = new PojoCursor(db.query());

        Pojo[] samples = new Pojo[]{
                new Pojo(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0"),
                new Pojo(1, 1l, 1f, 1d, (short) 1, true, new byte[]{1, 1}, "1"),
        };

        iterationHelper(cursor, samples);
    }

    public void testMerging() {
        TestDb db0 = new TestDb(getContext());
        TestDb db1 = new TestDb(getContext());

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

    public void testIsEmpty() {
        TestDb db0 = new TestDb(getContext());
        TestDb db1 = new TestDb(getContext());

        db0.insertRow(0, 0l, 0f, 0d, (short) 0, true, new byte[]{0, 0}, "0");

        IterableCursorWrapper<Pojo> cursor0 = new PojoCursor(db0.query());
        IterableCursorWrapper<Pojo> cursor1 = new PojoCursor(db1.query());

        assertFalse(cursor0.isEmpty());
        assertTrue(cursor1.isEmpty());
    }

    public void testMovingAround() {
        TestDb db = new TestDb(getContext());

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

    public void testMovingAroundWithOnlyOneItem() {
        TestDb db = new TestDb(getContext());

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

    private static class TestDb extends SQLiteOpenHelper {

        private static final int VERSION = 1;

        public TestDb(Context context) {
            super(context, randomFileName(), null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table TEST (" + "some_str TEXT, " + "some_int INTEGER, " +
                    "some_long LONG, " + "some_boolean INTEGER, " + "some_float REAL, " +
                    "some_double REAL, " + "some_short INTEGER, " + "some_byte_array BLOB" + ")");
        }

        /** (int i, long l, float f, double d, short s, boolean b, byte[] bytes, ( */
        public void insertRow(int i, long l, float f, double d, short s, boolean b, byte[] bytes,
                String str) {
            ContentValues cv = new ContentValues();
            cv.put("some_str", str);
            cv.put("some_int", i);
            cv.put("some_long", l);
            cv.put("some_boolean", b);
            cv.put("some_float", f);
            cv.put("some_double", d);
            cv.put("some_short", s);
            cv.put("some_byte_array", bytes);
            getWritableDatabase().insert("TEST", null, cv);
        }

        public Cursor query() {
            return getReadableDatabase().query("TEST", null, null, null, null, null, null);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            throw new IllegalStateException("why would you want to upgrade?");
        }
    }

    private static String randomFileName() {
        return "test_" + new Random().nextInt(1000000) + ".sqlite";
    }

    private class Pojo {
        int i;
        long l;
        float f;
        double d;
        short s;
        boolean b;
        byte[] bytes;
        String str;

        private Pojo(int i, long l, float f, double d, short s, boolean b, byte[] bytes,
                String str) {
            this.i = i;
            this.l = l;
            this.f = f;
            this.d = d;
            this.s = s;
            this.b = b;
            this.bytes = bytes;
            this.str = str;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pojo pojo = (Pojo) o;

            if (b != pojo.b) return false;
            if (Double.compare(pojo.d, d) != 0) return false;
            if (Float.compare(pojo.f, f) != 0) return false;
            if (i != pojo.i) return false;
            if (l != pojo.l) return false;
            if (s != pojo.s) return false;
            if (!Arrays.equals(bytes, pojo.bytes)) return false;
            if (str != null ? !str.equals(pojo.str) : pojo.str != null) return false;

            return true;
        }

        @Override
        public String toString() {
            return "Pojo{" +
                    "i=" + i +
                    ", l=" + l +
                    ", f=" + f +
                    ", d=" + d +
                    ", s=" + s +
                    ", b=" + b +
                    ", bytes=" + Arrays.toString(bytes) +
                    ", str='" + str + '\'' +
                    '}';
        }
    }

    private class PojoCursor extends IterableCursorWrapper<Pojo> {

        private PojoCursor(Cursor cursor) {
            super(cursor);
        }

        @Override
        public Pojo peek() {
            return new Pojo(
                    getIntegerHelper("some_int", -1),
                    getLongHelper("some_long", -1l),
                    getFloatHelper("some_float", -1f),
                    getDoubleHelper("some_double", -1d),
                    getShortHelper("some_short", (short) -1),
                    getBooleanHelper("some_boolean", false),
                    getBlobHelper("some_byte_array", new byte[]{-1, -1}),
                    getStringHelper("some_str", "-1")
            );
        }
    }

}