package com.vsu.cgcourse.math;

import javax.vecmath.Tuple3f;
import java.util.Objects;

public class Vector3f {
    private float x;
    private float y;
    private float z;

    public Vector3f() {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
    }

    public Vector3f(float x, float y, float z) {
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

    public void add(Vector3f v1) {
        this.x += v1.x;
        this.y += v1.y;
        this.z += v1.z;
    }

    public void sub(Vector3f v1, Vector3f v2) {
        this.x = v1.x - v2.x;
        this.y = v1.y - v2.y;
        this.z = v1.z - v2.z;
    }

    public Vector3f multi(float a) {
        float multi1 = this.x * a;
        float multi2 = this.y * a;
        float multi3 = this.z * a;

        return new Vector3f(multi1, multi2, multi3);
    }

    public Vector3f div(float a) {
        float div1 = this.x / a;
        float div2 = this.y / a;
        float div3 = this.z / a;

        return new Vector3f(div1, div2, div3);
    }

    public float length() {
        float sqrt = this.x * this.x
                + this.y * this.y
                + this.z * this.z;

        return (float) Math.sqrt(sqrt);
    }

    public void normalize() {
        float length = length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
    }

    public float dot(Vector3f v2) {
        float x1 = this.x * v2.x;
        float x2 = this.y * v2.y;
        float x3 = this.z * v2.z;

        return x1 + x2 + x3;
    }

    public final void cross(Vector3f v1, Vector3f v2) {
        float x = v1.y * v2.z - v1.z * v2.y;
        float y = v1.z * v2.x - v1.x * v2.z;
        float z = v1.x * v2.y - v1.y * v2.x;

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3f vector3f = (Vector3f) o;
        return Float.compare(vector3f.x, x) == 0 && Float.compare(vector3f.y, y) == 0 && Float.compare(vector3f.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
