package com.vsu.cgcourse.render_engine;

import com.vsu.cgcourse.math.matrices.Matrix4f;
import com.vsu.cgcourse.math.points.Point2f;
import com.vsu.cgcourse.math.vectors.Vector3f;
import com.vsu.cgcourse.model.Mesh;
import com.vsu.cgcourse.model.TransformMesh;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GraphicConveyorTest {
    private static final float EPS = (float) Math.pow(10, -6);

    @Test
    void modelMatrix() {
        Vector3f scale = new Vector3f(1, 2, 3);
        Vector3f rotate = new Vector3f(360, 360, 360);
        Vector3f translate = new Vector3f(10, 10, 10);

        Mesh mesh = new Mesh();
        float[][] actualFloat = GraphicConveyor.modelMatrix(scale, rotate, translate).getMatrix();

        float[][] expectedFloat = {
                {1, 0, 0, 10},
                {0, 2, 0, 10},
                {0, 0, 3, 10},
                {0, 0, 0, 1}
        };
        for (int i = 0; i < actualFloat.length; i++) {
            for (int j = 0; j < actualFloat[i].length; j++) {
                if (Math.abs(expectedFloat[i][j] - actualFloat[i][j]) < EPS) {
                    continue;
                }
                Assertions.fail();
            }
        }
        Assertions.assertTrue(true);
    }

    @Test
    void scale() {
        float[][] actualFloat = {
                {2, 0, 0, 10},
                {0, 1, 0, 5},
                {0, 0, 1, 2},
                {0, 0, 0, 1}
        };
        Matrix4f actual = new Matrix4f(actualFloat);
        GraphicConveyor.scale(new Vector3f(2, 3, 4), actual);

        float[][] expectedFloat = {
                {4, 0, 0, 10},
                {0, 3, 0, 5},
                {0, 0, 4, 2},
                {0, 0, 0, 1}
        };
        Matrix4f expected = new Matrix4f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void rotate() {
        Matrix4f actual = new Matrix4f(false);
        GraphicConveyor.rotate(new Vector3f(180.0f, 180.0f, 180.0f), actual);

        float[][] actualFloat = actual.getMatrix();
        float[][] expectedFloat = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        for (int i = 0; i < actualFloat.length; i++) {
            for (int j = 0; j < actualFloat[i].length; j++) {
                if (Math.abs(expectedFloat[i][j] - actualFloat[i][j]) < EPS) {
                    continue;
                }
                Assertions.fail();
            }
        }
    }

    @Test
    void translate() {
        Matrix4f actual = new Matrix4f(false);
        actual = GraphicConveyor.translate(new Vector3f(10, 20, 30), actual);

        float[][] expectedFloat = {
                {1, 0, 0, 10},
                {0, 1, 0, 20},
                {0, 0, 1, 30},
                {0, 0, 0, 1}
        };
        Matrix4f expected = new Matrix4f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void lookAt() {
        Vector3f origin = new Vector3f(1, 1, 1);
        Vector3f target = new Vector3f(2, 2, 2);
        float[][] expectedArray = new float[][]{
                {(float) (1 / Math.sqrt(2)), -(float) (1 / Math.sqrt(6)), (float) (1 / Math.sqrt(3)), -0f},
                {0f, (float) (2 / Math.sqrt(6)), (float) (1 / Math.sqrt(3)), -0f},
                {(float) (-1 / Math.sqrt(2)), (float) (-1 / Math.sqrt(6)), (float) (1 / Math.sqrt(3)), -(float) (3 / Math.sqrt(3))},
                {0f, 0f, 0f, 1f}
        };
        Matrix4f expected = new Matrix4f(expectedArray);
        Matrix4f actual = GraphicConveyor.lookAt(origin, target);
        for (int i = 0; i < expected.getMatrix().length; i++) {
            for (int j = 0; j < expected.getMatrix().length; j++) {
                if (Math.abs(expected.getMatrixElem(i, j) - actual.getMatrixElem(i, j)) < EPS) {
                    continue;
                }
                Assertions.fail();
            }
        }
    }

    @Test
    void perspective() {
        float[][] expectedArray = new float[][]{
                {(float) (1f / Math.tan(45f)) / 0.5f, 0f, 0f, 0f},
                {0f, (float) (1f / Math.tan(45f)), 0f, 0f},
                {0f, 0f, 3f, -20f},
                {0f, 0f, 1f, 0}
        };
        Matrix4f expected = new Matrix4f(expectedArray);
        Matrix4f actual = GraphicConveyor.perspective(90f, 0.5f, 5f, 10f);

        for (int i = 0; i < expected.getMatrix().length; i++) {
            for (int j = 0; j < expected.getMatrix().length; j++) {
                if (Math.abs(expected.getMatrixElem(i, j) - actual.getMatrixElem(i, j)) < EPS) {
                    continue;
                }
                Assertions.fail();
            }
        }
    }

    @Test
    void vertexToPoint() {
        Point2f pointExpected = new Point2f(300f, -50f);
        Point2f pointActual = GraphicConveyor.vertexToPoint(new Vector3f(1f, 1f, 1f), 200, 100);
        Assertions.assertEquals(pointExpected, pointActual);
    }
}