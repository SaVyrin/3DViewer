package com.vsu.cgcourse.model;

import com.vsu.cgcourse.math.vectors.Vector3f;

public class TransformMesh {
    final private double EPS = Math.pow(10, -5);

    private final String name;
    private final Mesh mesh;

    private Vector3f scale = new Vector3f(1, 1, 1);
    private Vector3f rotation = new Vector3f(0, 0, 0);
    private Vector3f translation = new Vector3f(0, 0, 0);

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

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }

    public String getName() {
        return name;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setTransformationMatrix(float[][] transformationMatrix) {
        for (float[] floats : transformationMatrix) {
            for (int i = 0; i < transformationMatrix.length; i++) {
                if (Math.abs(floats[i]) < EPS) {
                    floats[i] = 0;
                }
            }
        }
        this.transformationMatrix = transformationMatrix;
    }

    public float[][] getTransformationMatrix() {
        return transformationMatrix;
    }
}
