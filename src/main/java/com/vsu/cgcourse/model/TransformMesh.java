package com.vsu.cgcourse.model;

public class TransformMesh {

    private final String name;
    private final Mesh mesh;
    private float[][] transformationMatrix = new float[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    };

    public TransformMesh(String name, Mesh mesh) {
        this.name = name;
        this.mesh = mesh;
    }

    public String getName() {
        return name;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setTransformationMatrix(float[][] transformationMatrix) {
        this.transformationMatrix = transformationMatrix;
    }

    public float[][] getTransformationMatrix() {
        return transformationMatrix;
    }
}
