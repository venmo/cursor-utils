package com.venmo.cursor.support;

import android.content.Context;
import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup;

import com.venmo.cursor.CursorList;
import com.venmo.cursor.IterableCursor;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SupportIterableCursorAdapterTest extends AndroidTestCase {

    private class TestAdapter extends IterableCursorAdapter<Object> {

        private TestAdapter(IterableCursor<Object> c) {
            super(getContext(), c, false /* autoRequery */);
        }

        @Override
        public View newView(Context context, Object o, ViewGroup parent) {
            return new View(context);
        }

        @Override
        public void bindView(View view, Context context, Object o) {
            // intentionally empty
        }
    }

    public void testCanSwapWithNullCursor() {
        TestAdapter adapter = new TestAdapter(new CursorList<>());
        adapter.swapCursor(null);
    }

    public void testCanReturnNullCursorFromQuery() throws InterruptedException {
        final AtomicBoolean wasCalled = new AtomicBoolean(false);
        final CountDownLatch latch = new CountDownLatch(1);
        TestAdapter adapter = new TestAdapter(new CursorList<>()) {
            @Override
            public IterableCursor<Object> runQueryOnBackgroundThread(CharSequence constraint) {
                try {
                    return null;
                } finally {
                    wasCalled.set(true);
                    latch.countDown();
                }
            }
        };
        adapter.getFilter().filter(null);
        latch.await(1, TimeUnit.SECONDS);
        assertTrue(wasCalled.get());
    }

    public void testCanUseNullCursorForConstructor() {
        new TestAdapter(null);
    }

    public void testBindViewGetsCorrectObject() {
        Object obj1 = "obj1";
        Object obj2 = "obj2";
        final AtomicReference<Object> nextObject = new AtomicReference<>();
        final AtomicInteger counter = new AtomicInteger();
        TestAdapter adapter = new TestAdapter(new CursorList<>(Arrays.asList(obj1, obj2))) {
            @Override
            public void bindView(View view, Context context, Object o) {
                assertEquals(o, nextObject.get());
                counter.incrementAndGet();
                super.bindView(view, context, o);
            }
        };
        nextObject.set(obj1);
        adapter.bindView(new View(getContext()), getContext(), adapter.getCursor());
        adapter.getCursor().moveToNext(); // simulate list scrolling
        nextObject.set(obj2);
        adapter.bindView(new View(getContext()), getContext(), adapter.getCursor());
        assertEquals(2, counter.get());
    }
}
