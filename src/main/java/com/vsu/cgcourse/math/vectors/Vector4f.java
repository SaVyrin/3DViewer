package com.vsu.cgcourse.math.vectors;

import java.util.Objects;

public class Vector4f {
    private float x;
    private float y;
    private float z;
    private float w;

    public Vector4f() {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
        this.w = 0.0F;
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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

    public float getW() {
        return w;
    }

    public void add(Vector4f v1) {
        this.x += v1.x;
        this.y += v1.y;
        this.z += v1.z;
        this.w += v1.w;
    }

    public void sub(Vector4f v1, Vector4f v2) {
        this.x = v1.x - v2.x;
        this.y = v1.y - v2.y;
        this.z = v1.z - v2.z;
        this.w = v1.w - v2.w;
    }

    public Vector4f multi(float a) {
        float multi1 = this.x * a;
        float multi2 = this.y * a;
        float multi3 = this.z * a;
        float multi4 = this.w * a;

        return new Vector4f(multi1, multi2, multi3, multi4);
    }

    public Vector4f div(float a) {
        float div1 = this.x / a;
        float div2 = this.y / a;
        float div3 = this.z / a;
        float div4 = this.w / a;

        return new Vector4f(div1, div2, div3, div4);
    }

    public float length() {
        float sqrt = this.x * this.x
                + this.y * this.y
                + this.z * this.z
                + this.w * this.w;

        return (float) Math.sqrt(sqrt);
    }

    public void normalize() {
        float length = length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        this.w /= length;
    }

    public float dot(Vector4f v2) {
        float x1 = this.x * v2.x;
        float x2 = this.y * v2.y;
        float x3 = this.z * v2.z;
        float x4 = this.w * v2.w;

        return x1 + x2 + x3 + x4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector4f vector4f = (Vector4f) o;
        return Float.compare(vector4f.x, x) == 0 && Float.compare(vector4f.y, y) == 0 && Float.compare(vector4f.z, z) == 0 && Float.compare(vector4f.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }
}
