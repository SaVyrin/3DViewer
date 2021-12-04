package com.vsu.cgcourse.math;

import com.vsu.cgcourse.math.matrices.Matrix3f;
import com.vsu.cgcourse.math.matrices.MatrixSizeException;
import com.vsu.cgcourse.math.vectors.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Matrix3fTest {

    @Test
    void zeroConstructor() {
        Matrix3f actual = new Matrix3f(true);

        float[][] expectedFloat = {
                {0f, 0f, 0f},
                {0f, 0f, 0f},
                {0f, 0f, 0f}
        };
        Matrix3f expected = new Matrix3f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void unitConstructor() {
        Matrix3f actual = new Matrix3f(false);

        float[][] expectedFloat = {
                {1f, 0f, 0f},
                {0f, 1f, 0f},
                {0f, 0f, 1f}
        };
        Matrix3f expected = new Matrix3f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void matrix3fConstructor() {
        Matrix3f unit = new Matrix3f(false);
        Matrix3f actual = new Matrix3f(unit);

        float[][] expectedFloat = {
                {1f, 0f, 0f},
                {0f, 1f, 0f},
                {0f, 0f, 1f}
        };
        Matrix3f expected = new Matrix3f(expectedFloat);
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
            Matrix3f m1 = new Matrix3f(actualFloat);
        } catch (MatrixSizeException ex) {
            String actual = ex.getMessage();
            String expected = "Matrix initialize error: incorrect matrix size for Matrix3f";
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    void getMatrixElem() {
        float[][] actualFloat = {
                {1f, 1f, 1f},
                {2f, 2f, 2f},
                {3f, 3f, 3f}
        };
        Matrix3f m1 = new Matrix3f(actualFloat);

        float actual = m1.getMatrixElem(2, 2);
        float expected = 3f;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void setMatrixElem() {
        float[][] actualFloat = {
                {1f, 1f, 1f},
                {2f, 2f, 2f},
                {3f, 3f, 3f}
        };
        Matrix3f actual = new Matrix3f(actualFloat);
        actual.setMatrixElem(2, 2, 0);

        float[][] expectedFloat = {
                {1f, 1f, 1f},
                {2f, 2f, 2f},
                {3f, 3f, 0f}
        };
        Matrix3f expected = new Matrix3f(expectedFloat);
        Assertions.assertEquals(expected, actual);

    }

    @Test
    void sum() {
        float[][] actualFloat = {
                {1f, 1f, 1f},
                {2f, 2f, 2f},
                {3f, 3f, 3f}
        };
        Matrix3f m1 = new Matrix3f(actualFloat);
        Matrix3f m2 = new Matrix3f(actualFloat);
        Matrix3f actual = m1.sum(m2);

        float[][] expectedFloat = {
                {2f, 2f, 2f},
                {4f, 4f, 4f},
                {6f, 6f, 6f}
        };
        Matrix3f expected = new Matrix3f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void diff() {
        float[][] actualFloat = {
                {1f, 1f, 1f},
                {2f, 2f, 2f},
                {3f, 3f, 3f}
        };
        Matrix3f m1 = new Matrix3f(actualFloat);
        Matrix3f m2 = new Matrix3f(actualFloat);
        Matrix3f actual = m1.diff(m2);

        float[][] expectedFloat = {
                {0f, 0f, 0f},
                {0f, 0f, 0f},
                {0f, 0f, 0f}
        };
        Matrix3f expected = new Matrix3f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void mul() {
        float[][] actualFloat = {
                {1f, 1f, 1f},
                {2f, 2f, 2f},
                {3f, 3f, 3f}
        };
        Matrix3f actual = new Matrix3f(actualFloat);
        Matrix3f m2 = new Matrix3f(actualFloat);
        actual.mul(m2);

        float[][] expectedFloat = {
                {6f, 6f, 6f},
                {12f, 12f, 12f},
                {18f, 18f, 18f}
        };
        Matrix3f expected = new Matrix3f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void vectorMulti() {
        float[][] actualFloat = {
                {1f, 1f, 1f},
                {2f, 2f, 2f},
                {3f, 3f, 3f}
        };
        Matrix3f m1 = new Matrix3f(actualFloat);
        Vector3f v1 = new Vector3f(1f, 2f, 3f);
        Vector3f actual = m1.vectorMulti(v1);

        Vector3f expected = new Vector3f(6f, 12f, 18f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void trans() {
        float[][] actualFloat = {
                {1, 1, 1},
                {2, 2, 2},
                {3, 3, 3}
        };
        Matrix3f actual = new Matrix3f(actualFloat);
        actual.trans();

        float[][] expectedFloat = {
                {1, 2, 3},
                {1, 2, 3},
                {1, 2, 3}
        };
        Matrix3f expected = new Matrix3f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }
}