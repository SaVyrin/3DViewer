package com.vsu.cgcourse.math.matrices;

import com.vsu.cgcourse.math.vectors.Vector3f;

import java.util.Arrays;

public class Matrix3f {
    private final int MATRIX_SIZE = 3;
    private float[][] matrix;

    public Matrix3f(boolean isZero) {
        float[][] matrix = new float[MATRIX_SIZE][MATRIX_SIZE];
        if (!isZero) {
            matrix[0][0] = 1;
            matrix[1][1] = 1;
            matrix[2][2] = 1;
        }
        this.matrix = matrix;
    }

    public Matrix3f(float[][] matrix) throws MatrixSizeException {
        if (matrix.length != MATRIX_SIZE || matrix[0].length != MATRIX_SIZE) {
            throw new MatrixSizeException("incorrect matrix size for Matrix3f");
        }
        this.matrix = matrix;
    }

    public Matrix3f(Matrix3f matrix3f) {
        this.matrix = matrix3f.matrix;
    }

    public void setMatrixElem(int i, int j, float value) {
        this.matrix[i][j] = value;
    }

    public float getMatrixElem(int i, int j) {
        return this.matrix[i][j];
    }

    public Matrix3f sum(Matrix3f m2) {
        float[][] sum = new float[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                sum[i][j] = this.matrix[i][j] + m2.matrix[i][j];
            }
        }
        return new Matrix3f(sum);
    }

    public Matrix3f diff(Matrix3f m2) {
        float[][] diff = new float[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                diff[i][j] = this.matrix[i][j] - m2.matrix[i][j];
            }
        }
        return new Matrix3f(diff);
    }

    public void mul(Matrix3f matrix3f) {
        float[][] mul = new float[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                mul[i][j] += this.matrix[i][0] * matrix3f.matrix[0][j];
                mul[i][j] += this.matrix[i][1] * matrix3f.matrix[1][j];
                mul[i][j] += this.matrix[i][2] * matrix3f.matrix[2][j];
            }
        }
        this.matrix = mul;
    }

    public Vector3f vectorMulti(Vector3f v) {
        float[] vectorMulti = new float[MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            vectorMulti[i] += this.matrix[i][0] * v.getX();
            vectorMulti[i] += this.matrix[i][1] * v.getY();
            vectorMulti[i] += this.matrix[i][2] * v.getZ();
        }
        return new Vector3f(vectorMulti[0], vectorMulti[1], vectorMulti[2]);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix3f matrix3f = (Matrix3f) o;
        return Arrays.deepEquals(matrix, matrix3f.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }
}
