package com.vsu.cgcourse.math.matrices;

public class MatrixSizeException extends RuntimeException {
    public MatrixSizeException(String errorMessage) {
        super("Matrix initialize error: " + errorMessage);
    }
}
