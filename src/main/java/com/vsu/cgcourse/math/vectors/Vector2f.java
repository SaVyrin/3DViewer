package com.vsu.cgcourse.math.vectors;

import java.util.Objects;

public class Vector2f {
    private float x;
    private float y;

    public Vector2f() {
        this.x = 0.0F;
        this.y = 0.0F;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2f add(Vector2f v1) {
        float x = this.x + v1.x;
        float y = this.y + v1.y;

        return new Vector2f(x, y);
    }

    public void sub(Vector2f v1, Vector2f v2) {
        this.x = v1.x - v2.x;
        this.y = v1.y - v2.y;
    }

    public Vector2f multi(float a) {
        float multi1 = this.x * a;
        float multi2 = this.y * a;

        return new Vector2f(multi1, multi2);
    }

    public Vector2f div(float a) {
        float div1 = this.x / a;
        float div2 = this.y / a;

        return new Vector2f(div1, div2);
    }

    public float length() {
        float sqrt = this.x * this.x + this.y * this.y;

        return (float) Math.sqrt(sqrt);
    }

    public void normalize() {
        float length = length();
        this.x /= length;
        this.y /= length;
    }

    public float dot(Vector2f v2) {
        float x1 = this.x * v2.x;
        float x2 = this.y * v2.y;

        return x1 + x2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2f vector2f = (Vector2f) o;
        return Float.compare(vector2f.x, x) == 0 && Float.compare(vector2f.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
