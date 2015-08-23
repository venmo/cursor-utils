package com.venmo.cursor.test;

import java.util.Arrays;

public class Pojo {
    int i;
    long l;
    float f;
    double d;
    short s;
    boolean b;
    byte[] bytes;
    String str;

    public Pojo(int i, long l, float f, double d, short s, boolean b, byte[] bytes,
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