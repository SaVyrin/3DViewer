package com.vsu.cgcourse.math;

import java.util.Arrays;

public class Matrix4f {
    private final int MATRIX_SIZE = 4;
    private float[][] matrix;

    public Matrix4f(boolean isZero) {
        float[][] matrix = new float[MATRIX_SIZE][MATRIX_SIZE];
        if (!isZero) {
            matrix[0][0] = 1;
            matrix[1][1] = 1;
            matrix[2][2] = 1;
            matrix[3][3] = 1;
        }
        this.matrix = matrix;
    }

    public Matrix4f(float[][] matrix) throws MatrixSizeException {
        if (matrix.length != MATRIX_SIZE || matrix[0].length != MATRIX_SIZE) {
            throw new MatrixSizeException("incorrect matrix size for Matrix4f");
        }
        this.matrix = matrix;
    }

    public Matrix4f(Matrix4f matrix4f) {
        this.matrix = matrix4f.matrix;
    }

    public void setMatrixElem(int i, int j, float value) {
        this.matrix[i][j] = value;
    }

    public float getMatrixElem(int i, int j) {
        return this.matrix[i][j];
    }

    public float[][] getMatrix() {
        return this.matrix;
    }

    public Matrix4f sum(Matrix4f m2) {
        float[][] sum = new float[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                sum[i][j] = this.matrix[i][j] + m2.matrix[i][j];
            }
        }
        return new Matrix4f(sum);
    }

    public Matrix4f diff(Matrix4f m2) {
        float[][] diff = new float[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                diff[i][j] = this.matrix[i][j] - m2.matrix[i][j];
            }
        }
        return new Matrix4f(diff);
    }

    public void mul(Matrix4f matrix4f) {
        float[][] mul = new float[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                mul[i][j] += this.matrix[i][0] * matrix4f.matrix[0][j];
                mul[i][j] += this.matrix[i][1] * matrix4f.matrix[1][j];
                mul[i][j] += this.matrix[i][2] * matrix4f.matrix[2][j];
                mul[i][j] += this.matrix[i][3] * matrix4f.matrix[3][j];
            }
        }
        this.matrix = mul;
    }

    public Vector4f vectorMulti(Vector4f v) {
        float[] vectorMulti = new float[MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            vectorMulti[i] += this.matrix[i][0] * v.getX();
            vectorMulti[i] += this.matrix[i][1] * v.getY();
            vectorMulti[i] += this.matrix[i][2] * v.getZ();
            vectorMulti[i] += this.matrix[i][2] * v.getW();
        }
        return new Vector4f(vectorMulti[0], vectorMulti[1], vectorMulti[2], vectorMulti[3]);
    }

    public void trans() {
        float[][] trans = new float[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                trans[i][j] = this.matrix[j][i];
            }
        }

        this.matrix = trans;
    }

    public static Vector3f multiplyMatrix4ByVector3(final Matrix4f matrix, final Vector3f vertex) {
        final float x = (vertex.getX() * matrix.getMatrixElem(0, 0)) + (vertex.getY() * matrix.getMatrixElem(0, 1)) + (vertex.getZ() * matrix.getMatrixElem(0, 2)) + matrix.getMatrixElem(0, 3);
        final float y = (vertex.getX() * matrix.getMatrixElem(1, 0)) + (vertex.getY() * matrix.getMatrixElem(1, 1)) + (vertex.getZ() * matrix.getMatrixElem(1, 2)) + matrix.getMatrixElem(1, 3);
        final float z = (vertex.getX() * matrix.getMatrixElem(2, 0)) + (vertex.getY() * matrix.getMatrixElem(2, 1)) + (vertex.getZ() * matrix.getMatrixElem(2, 2)) + matrix.getMatrixElem(2, 3);
        final float w = (vertex.getX() * matrix.getMatrixElem(3, 0)) + (vertex.getY() * matrix.getMatrixElem(3, 1)) + (vertex.getZ() * matrix.getMatrixElem(3, 2)) + matrix.getMatrixElem(3, 3);
        return new Vector3f(x / w, y / w, z / w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix4f matrix4f = (Matrix4f) o;
        return Arrays.deepEquals(matrix, matrix4f.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }
}
