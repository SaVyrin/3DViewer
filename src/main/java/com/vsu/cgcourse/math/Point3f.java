package com.vsu.cgcourse.math;

import java.util.Objects;

public class Point3f {
    private final float x;
    private final float y;
    private final float z;

    public Point3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3f point3f = (Point3f) o;
        return Float.compare(point3f.x, x) == 0 && Float.compare(point3f.y, y) == 0 && Float.compare(point3f.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
