package com.vsu.cgcourse.render_engine;

import com.vsu.cgcourse.math.Matrix4f;
import com.vsu.cgcourse.math.Vector3f;
import com.vsu.cgcourse.model.Mesh;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.logging.SocketHandler;

import static org.junit.jupiter.api.Assertions.*;

class GraphicConveyorTest {

    @Test
    void modelMatrix() {
        Vector3f scale = new Vector3f(1, 2, 3);
        Vector3f rotate = new Vector3f(360, 360, 360);
        Vector3f translate = new Vector3f(10, 10, 10);

        System.out.println(Math.sin(Math.toRadians(180)));
        System.out.println(Math.cos(Math.toRadians(180)));

        Mesh mesh = new Mesh();
        float[][] actualFloat = GraphicConveyor.modelMatrix(scale, rotate, translate, mesh).getMatrix();

        float[][] expectedFloat = {
                {1, 0, 0, 10},
                {0, 2, 0, 10},
                {0, 0, 3, 10},
                {0, 0, 0, 1}
        };
        for (int i = 0; i < actualFloat.length; i++) {
            for (int j = 0; j < actualFloat[i].length; j++) {
                if (expectedFloat[i][j] == 0 && actualFloat[i][j] < Math.pow(10, -10)) {
                    continue;
                }
                if (expectedFloat[i][j] == actualFloat[i][j]) {
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
                System.out.print(actualFloat[i][j] + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < actualFloat.length; i++) {
            for (int j = 0; j < actualFloat[i].length; j++) {
                if (expectedFloat[i][j] == 0 && actualFloat[i][j] < Math.pow(10, -10)) {
                    continue;
                }
                if (expectedFloat[i][j] == actualFloat[i][j]) {
                    continue;
                }
                Assertions.fail();
            }
        }
        Assertions.assertTrue(true);
    }

    @Test
    void translate() {
        Matrix4f actual = new Matrix4f(false);
        actual.setMatrixElem(0, 0, 2);
        actual = GraphicConveyor.translate(new Vector3f(10, 20, 30), actual);

        float[][] expectedFloat = {
                {2, 0, 0, 10},
                {0, 1, 0, 20},
                {0, 0, 1, 30},
                {0, 0, 0, 1}
        };
        Matrix4f expected = new Matrix4f(expectedFloat);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void lookAt() {
    }

    @Test
    void testLookAt() {
    }

    @Test
    void perspective() {
    }

    @Test
    void multiplyMatrix4ByVector3() {
        float[][] actualFloat = {
                {1, 0, 0, 10},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        Matrix4f actualMatrix = new Matrix4f(actualFloat);
        Vector3f actual = GraphicConveyor.multiplyMatrix4ByVector3(actualMatrix, new Vector3f(10, 10, 10));

        Vector3f expected = new Vector3f(20f, 10f, 10f);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void vertexToPoint() {
    }
}