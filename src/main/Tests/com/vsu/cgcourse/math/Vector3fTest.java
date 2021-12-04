package com.vsu.cgcourse.math;

import com.vsu.cgcourse.math.vectors.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Vector3fTest {

    @Test
    void add() {
        Vector3f actual = new Vector3f(1f, 2f, 3f);
        Vector3f v2 = new Vector3f(3f, 4f, 5f);
        actual.add(v2);

        Vector3f expected = new Vector3f(4f, 6f, 8f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void sub() {
        Vector3f v1 = new Vector3f(1f, 2f, 3f);
        Vector3f v2 = new Vector3f(3f, 4f, 5f);
        Vector3f actual = new Vector3f();
        actual.sub(v1, v2);

        Vector3f expected = new Vector3f(-2f, -2f, -2f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void multi() {
        Vector3f v1 = new Vector3f(1f, 2f, 3f);
        Vector3f actual = v1.multi(2f);

        Vector3f expected = new Vector3f(2f, 4f, 6f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void div() {
        Vector3f v1 = new Vector3f(1f, 2f, 3f);
        Vector3f actual = v1.div(2f);

        Vector3f expected = new Vector3f(0.5f, 1f, 1.5f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void length() {
        Vector3f v1 = new Vector3f(1f, 2f, 2f);
        float actual = v1.length();

        float expected = 3f;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void normalize() {
        Vector3f actual = new Vector3f(2f, 4f, 4f);
        actual.normalize();

        Vector3f expected = new Vector3f(2f / 6f, 4f / 6f, 4f / 6f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void dot() {
        Vector3f v1 = new Vector3f(3f, 4f, 5f);
        Vector3f v2 = new Vector3f(3f, 4f, 5f);
        float actual = v1.dot(v2);

        float expected = 50;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cross() {
        Vector3f v1 = new Vector3f(1f, 2f, 3f);
        Vector3f v2 = new Vector3f(3f, 4f, 5f);
        Vector3f actual = new Vector3f();
        actual.cross(v1, v2);

        Vector3f expected = new Vector3f(-2f, 4f, -2f);
        Assertions.assertEquals(expected, actual);
    }
}