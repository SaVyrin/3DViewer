package com.vsu.cgcourse.model;

import com.vsu.cgcourse.math.points.Point3f;
import com.vsu.cgcourse.math.vectors.Vector2f;
import com.vsu.cgcourse.math.vectors.Vector3f;

import java.util.ArrayList;

public class Polygon {
    public ArrayList<Point3f> vertices;
    public ArrayList<Vector2f> textureVertices;
    public Vector3f normal;

    public Polygon(ArrayList<Point3f> vertices, ArrayList<Vector2f> textureVertices, Vector3f normal) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normal = normal;
    }
}
