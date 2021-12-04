package com.vsu.cgcourse.render_engine;

import com.vsu.cgcourse.math.matrices.Matrix4f;
import com.vsu.cgcourse.math.points.Point2f;
import com.vsu.cgcourse.math.vectors.Vector3f;
import com.vsu.cgcourse.model.TransformMesh;

public class GraphicConveyor {

    public static Matrix4f modelMatrix(Vector3f scale, Vector3f rotate, Vector3f translate, TransformMesh transformMesh) {
        Matrix4f rtsMatrix = new Matrix4f(transformMesh.getTransformationMatrix());
        scale(scale, rtsMatrix);
        rotate(rotate, rtsMatrix);
        rtsMatrix = translate(translate, rtsMatrix);
        return rtsMatrix;
    }

    public static void scale(Vector3f scale, Matrix4f matrix4f) {
        float[][] scaleMatrix = new float[][]{
                {scale.getX(), 0, 0, 0},
                {0, scale.getY(), 0, 0},
                {0, 0, scale.getZ(), 0},
                {0, 0, 0, 1}
        };
        Matrix4f scaleMatrix4f = new Matrix4f(scaleMatrix);
        matrix4f.mul(scaleMatrix4f);
    }

    public static void rotate(Vector3f rotate, Matrix4f matrix4f) {
        float sinX = (float) Math.sin(Math.toRadians(rotate.getX()));
        float cosX = (float) Math.cos(Math.toRadians(rotate.getX()));
        float sinY = (float) Math.sin(Math.toRadians(rotate.getY()));
        float cosY = (float) Math.cos(Math.toRadians(rotate.getY()));
        float sinZ = (float) Math.sin(Math.toRadians(rotate.getZ()));
        float cosZ = (float) Math.cos(Math.toRadians(rotate.getZ()));

        float[][] rotateZMatrix = new float[][]{
                {cosZ, -sinZ, 0, 0},
                {sinZ, cosZ, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        float[][] rotateYMatrix = new float[][]{
                {cosY, 0, sinY, 0},
                {0, 1, 0, 0},
                {-sinY, 0, cosY, 0},
                {0, 0, 0, 1}
        };
        float[][] rotateXMatrix = new float[][]{
                {1, 0, 0, 0},
                {0, cosX, -sinX, 0},
                {0, sinX, cosX, 0},
                {0, 0, 0, 1}
        };
        Matrix4f rotateMatrix4f = new Matrix4f(rotateZMatrix);
        Matrix4f rotateYMatrix4f = new Matrix4f(rotateYMatrix);
        Matrix4f rotateXMatrix4f = new Matrix4f(rotateXMatrix);
        rotateMatrix4f.mul(rotateYMatrix4f);
        rotateMatrix4f.mul(rotateXMatrix4f);
        matrix4f.mul(rotateMatrix4f);
    }

    public static Matrix4f translate(Vector3f translate, Matrix4f matrix4f) {
        float[][] translateMatrix = new float[][]{
                {1, 0, 0, translate.getX()},
                {0, 1, 0, translate.getY()},
                {0, 0, 1, translate.getZ()},
                {0, 0, 0, 1}
        };
        Matrix4f translateMatrix4f = new Matrix4f(translateMatrix);
        translateMatrix4f.mul(matrix4f);
        return translateMatrix4f;
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f resultX = new Vector3f();
        Vector3f resultY = new Vector3f();
        Vector3f resultZ = new Vector3f();

        resultZ.sub(target, eye);
        resultX.cross(up, resultZ);
        resultY.cross(resultZ, resultX);

        resultX.normalize();
        resultY.normalize();
        resultZ.normalize();

        float[][] matrix = new float[][]{
                {resultX.getX(), resultY.getX(), resultZ.getX(), -resultX.dot(eye)},
                {resultX.getY(), resultY.getY(), resultZ.getY(), -resultY.dot(eye)},
                {resultX.getZ(), resultY.getZ(), resultZ.getZ(), -resultZ.dot(eye)},
                {0, 0, 0, 1}
        };
        return new Matrix4f(matrix);
    }

    public static Matrix4f perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4f result = new Matrix4f(true);
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.setMatrixElem(0, 0, tangentMinusOnDegree / aspectRatio);
        result.setMatrixElem(1, 1, tangentMinusOnDegree);
        result.setMatrixElem(2, 2, (farPlane + nearPlane) / (farPlane - nearPlane));
        result.setMatrixElem(2, 3, 2 * (nearPlane * farPlane) / (nearPlane - farPlane));
        result.setMatrixElem(3, 2, 1.0F);
        return result;
    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
