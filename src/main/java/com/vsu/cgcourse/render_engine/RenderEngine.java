package com.vsu.cgcourse.render_engine;

import com.vsu.cgcourse.math.matrices.Matrix4f;
import com.vsu.cgcourse.math.points.Point2f;
import com.vsu.cgcourse.math.points.Point3f;
import com.vsu.cgcourse.math.vectors.Vector3f;
import com.vsu.cgcourse.model.Mesh;
import com.vsu.cgcourse.model.TransformMesh;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;

import java.util.ArrayList;
import java.util.Arrays;

import static com.vsu.cgcourse.render_engine.GraphicConveyor.vertexToPoint;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final TransformMesh transformMesh,
            final int width,
            final int height) {
        float[][] transformMatrix = transformMesh.getTransformationMatrix();
        Mesh mesh = transformMesh.getMesh();

        Matrix4f modelMatrix = new Matrix4f(transformMatrix);
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
        modelViewProjectionMatrix.mul(projectionMatrix);

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        TempRasterization.zBuffer = new float[900][1600];
        for (float[] ints : TempRasterization.zBuffer) {
            Arrays.fill(ints, Integer.MAX_VALUE);
        }

        final int nPolygons = mesh.polygonVertexIndices.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.polygonVertexIndices.get(polygonInd).size();

            ArrayList<Point3f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = mesh.vertices.get(mesh.polygonVertexIndices.get(polygonInd).get(vertexInPolygonInd));

                Vector3f vec = Matrix4f.multiplyMatrix4ByVector3(modelMatrix, vertex);
                Vector3f vec2 = Matrix4f.multiplyMatrix4ByVector3(viewMatrix, vec);
                Vector3f vec3 = Matrix4f.multiplyMatrix4ByVector3(projectionMatrix, vec2);

                Point2f point2f = vertexToPoint(vec3, width, height);
                Point3f resultPoint = new Point3f(point2f.getX(), point2f.getY(), vec3.getZ());
                resultPoints.add(resultPoint);
            }


            TempRasterization.drawTriangle(resultPoints, pixelWriter);
            /*for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).getX(),
                        resultPoints.get(vertexInPolygonInd - 1).getY(),
                        resultPoints.get(vertexInPolygonInd).getX(),
                        resultPoints.get(vertexInPolygonInd).getY());
            }

            if (nVerticesInPolygon > 0) {
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).getX(),
                        resultPoints.get(nVerticesInPolygon - 1).getY(),
                        resultPoints.get(0).getX(),
                        resultPoints.get(0).getY());
            }*/
        }
    }
}