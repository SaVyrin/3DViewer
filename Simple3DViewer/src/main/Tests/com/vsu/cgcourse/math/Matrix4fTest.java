package com.vsu.cgcourse.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Matrix4fTest {

    @Test
    void zeroConstructor() {
        Matrix4f actual = new Matrix4f(true);

        float[][] expectedFloat = {
                {0f, 0f, 0f, 0f},
                {0f, 0f, 0f, 0f},
                {0f, 0f, 0f, 0f},
                {0f, 0f, 0f, 0f}
        };
        Matrix4f expected = new Matrix4f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void unitConstructor() {
        Matrix4f actual = new Matrix4f(false);

        float[][] expectedFloat = {
                {1f, 0f, 0f, 0f},
                {0f, 1f, 0f, 0f},
                {0f, 0f, 1f, 0f},
                {0f, 0f, 0f, 1f}
        };
        Matrix4f expected = new Matrix4f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void matrix4fConstructor() {
        Matrix4f unit = new Matrix4f(false);
        Matrix4f actual = new Matrix4f(unit);

        float[][] expectedFloat = {
                {1f, 0f, 0f, 0f},
                {0f, 1f, 0f, 0f},
                {0f, 0f, 1f, 0f},
                {0f, 0f, 0f, 1f}
        };
        Matrix4f expected = new Matrix4f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void constructorException() {
        try {
            float[][] actualFloat = {
                    {1f, 1f, 1f},
                    {2f, 2f, 2f},
                    {3f, 3f, 3f},
                    {4f, 4f, 4f}
            };
            Matrix4f m1 = new Matrix4f(actualFloat);
        } catch (MatrixSizeException ex) {
            String actual = ex.getMessage();
            String expected = "Matrix initialize error: incorrect matrix size for Matrix4f";
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    void sum() {
        float[][] actualFloat = {
                {1f, 1f, 1f, 1f},
                {2f, 2f, 2f, 2f},
                {3f, 3f, 3f, 3f},
                {4f, 4f, 4f, 4f}
        };
        Matrix4f m1 = new Matrix4f(actualFloat);
        Matrix4f m2 = new Matrix4f(actualFloat);
        Matrix4f actual = m1.sum(m2);

        float[][] expectedFloat = {
                {2f, 2f, 2f, 2f},
                {4f, 4f, 4f, 4f},
                {6f, 6f, 6f, 6f},
                {8f, 8f, 8f, 8f}
        };
        Matrix4f expected = new Matrix4f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void diff() {
        float[][] actualFloat = {
                {1f, 1f, 1f, 1f},
                {2f, 2f, 2f, 2f},
                {3f, 3f, 3f, 3f},
                {4f, 4f, 4f, 4f}
        };
        Matrix4f m1 = new Matrix4f(actualFloat);
        Matrix4f m2 = new Matrix4f(actualFloat);
        Matrix4f actual = m1.diff(m2);

        float[][] expectedFloat = {
                {0f, 0f, 0f, 0f},
                {0f, 0f, 0f, 0f},
                {0f, 0f, 0f, 0f},
                {0f, 0f, 0f, 0f}
        };
        Matrix4f expected = new Matrix4f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void vectorMulti() {
        float[][] actualFloat = {
                {1f, 1f, 1f, 1f},
                {2f, 2f, 2f, 2f},
                {3f, 3f, 3f, 3f},
                {4f, 4f, 4f, 4f}
        };
        Matrix4f m1 = new Matrix4f(actualFloat);
        Vector4f v1 = new Vector4f(1f, 2f, 3f, 4f);
        Vector4f actual = m1.vectorMulti(v1);

        Vector4f expected = new Vector4f(10f, 20f, 30f, 40f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void mul() {
        float[][] actualFloat = {
                {1f, 1f, 1f, 1f},
                {2f, 2f, 2f, 2f},
                {3f, 3f, 3f, 3f},
                {4f, 4f, 4f, 4f}
        };
        Matrix4f actual = new Matrix4f(actualFloat);
        Matrix4f m2 = new Matrix4f(actualFloat);
        actual.mul(m2);

        float[][] expectedFloat = {
                {10f, 10f, 10f, 10f},
                {20f, 20f, 20f, 20f},
                {30f, 30f, 30f, 30f},
                {40f, 40f, 40f, 40f}
        };
        Matrix4f expected = new Matrix4f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void trans() {
        float[][] actualFloat = {
                {1f, 1f, 1f, 1f},
                {2f, 2f, 2f, 2f},
                {3f, 3f, 3f, 3f},
                {4f, 4f, 4f, 4f}
        };
        Matrix4f actual = new Matrix4f(actualFloat);
        actual.trans();

        float[][] expectedFloat = {
                {1f, 2f, 3f, 4f},
                {1f, 2f, 3f, 4f},
                {1f, 2f, 3f, 4f},
                {1f, 2f, 3f, 4f}
        };
        Matrix4f expected = new Matrix4f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }
}