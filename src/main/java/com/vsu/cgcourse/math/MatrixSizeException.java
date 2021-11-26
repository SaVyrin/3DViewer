package com.vsu.cgcourse.math;

public class MatrixSizeException extends RuntimeException {
    public MatrixSizeException(String errorMessage) {
        super("Matrix initialize error: " + errorMessage);
    }
}
