package com.vsu.cgcourse.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Vector2fTest {

    @Test
    void add() {
        Vector2f actual = new Vector2f(1f, 2f);
        Vector2f v2 = new Vector2f(3f, 4f);
        actual.add(v2);

        Vector2f expected = new Vector2f(4f, 6f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void sub() {
        Vector2f v1 = new Vector2f(1f, 2f);
        Vector2f v2 = new Vector2f(3f, 4f);
        Vector2f actual = new Vector2f();
        actual.sub(v1, v2);

        Vector2f expected = new Vector2f(-2f, -2f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void multi() {
        Vector2f v1 = new Vector2f(1f, 2f);
        Vector2f actual = v1.multi(2f);

        Vector2f expected = new Vector2f(2f, 4f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void div() {
        Vector2f v1 = new Vector2f(1f, 2f);
        Vector2f actual = v1.div(2f);

        Vector2f expected = new Vector2f(0.5f, 1f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void length() {
        Vector2f v1 = new Vector2f(3f, 4f);
        float actual = v1.length();

        float expected = 5f;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void normalize() {
        Vector2f actual = new Vector2f(3f, 4f);
        actual.normalize();

        Vector2f expected = new Vector2f(3f / 5f, 4f / 5f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void scalarMulti() {
        Vector2f v1 = new Vector2f(3f, 4f);
        Vector2f v2 = new Vector2f(3f, 4f);
        float actual = v1.dot(v2);

        float expected = 25;
        Assertions.assertEquals(expected, actual);
    }
}