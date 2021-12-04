package com.vsu.cgcourse.math;

import com.vsu.cgcourse.math.vectors.Vector4f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Vector4fTest {

    @Test
    void add() {
        Vector4f actual = new Vector4f(1f, 2f, 3f, 4f);
        Vector4f v2 = new Vector4f(3f, 4f, 5f, 6f);
        actual.add(v2);

        Vector4f expected = new Vector4f(4f, 6f, 8f, 10f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void sub() {
        Vector4f v1 = new Vector4f(1f, 2f, 3f, 4f);
        Vector4f v2 = new Vector4f(3f, 4f, 5f, 6f);
        Vector4f actual = new Vector4f();
        actual.sub(v1, v2);

        Vector4f expected = new Vector4f(-2f, -2f, -2f, -2f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void multi() {
        Vector4f v1 = new Vector4f(1f, 2f, 3f, 4f);
        Vector4f actual = v1.multi(2f);

        Vector4f expected = new Vector4f(2f, 4f, 6f, 8f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void div() {
        Vector4f v1 = new Vector4f(1f, 2f, 3f, 4f);
        Vector4f actual = v1.div(2f);

        Vector4f expected = new Vector4f(0.5f, 1f, 1.5f, 2f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void length() {
        Vector4f v1 = new Vector4f(2f, 2f, 2f, 2f);
        float actual = v1.length();

        float expected = 4f;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void normalize() {
        Vector4f actual = new Vector4f(2f, 2f, 2f, 2f);
        actual.normalize();

        Vector4f expected = new Vector4f(2f / 4f, 2f / 4f, 2f / 4f, 2f / 4f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void dot() {
        Vector4f v1 = new Vector4f(3f, 4f, 5f, 6f);
        Vector4f v2 = new Vector4f(3f, 4f, 5f, 6f);
        float actual = v1.dot(v2);

        float expected = 86;
        Assertions.assertEquals(expected, actual);
    }
}