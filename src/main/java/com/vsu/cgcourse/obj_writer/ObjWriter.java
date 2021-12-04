package com.vsu.cgcourse.obj_writer;

import com.vsu.cgcourse.math.vectors.Vector2f;
import com.vsu.cgcourse.math.vectors.Vector3f;
import com.vsu.cgcourse.model.Mesh;
import com.vsu.cgcourse.model.TransformMesh;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ObjWriter {

    private static int lineInd = 0;

    public static void write(List<TransformMesh> meshList, String fileName) {
        Locale.setDefault(Locale.ROOT);

        try (FileWriter writer = new FileWriter(fileName + ".obj", false)) {
            int verticesCount = 0;
            for (TransformMesh transformMesh : meshList) {
                Mesh mesh = transformMesh.getMesh();

                final ArrayList<Vector3f> vertices = mesh.vertices;
                final ArrayList<Vector2f> textureVertices = mesh.textureVertices;
                final ArrayList<Vector3f> normals = mesh.normals;
                final ArrayList<ArrayList<Integer>> polygonVertexIndices = mesh.polygonVertexIndices;
                final ArrayList<ArrayList<Integer>> polygonTextureVertexIndices = mesh.polygonTextureVertexIndices;
                final ArrayList<ArrayList<Integer>> polygonNormalIndices = mesh.polygonNormalIndices;

                writeV(vertices, writer);
                writeVt(textureVertices, writer);
                writeVn(normals, writer);

                writeF(
                        polygonVertexIndices,
                        polygonTextureVertexIndices,
                        polygonNormalIndices,
                        verticesCount,
                        writer
                );

                verticesCount += vertices.size();
                writer.flush();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void writeV(ArrayList<Vector3f> vertices, FileWriter writer) throws IOException {
        for (Vector3f v : vertices) {
            writer.write(String.format("v %.6f %.6f %.6f\n", v.getX(), v.getY(), v.getZ()));
            lineInd++;
        }
    }

    private static void writeVt(ArrayList<Vector2f> textureVertices, FileWriter writer) throws IOException {
        if (!textureVertices.isEmpty()) {
            writer.write("\n");
            lineInd++;
        }
        for (Vector2f vt : textureVertices) {
            writer.write(String.format("vt %.6f %.6f\n", vt.getX(), vt.getY()));
            lineInd++;
        }
    }

    private static void writeVn(ArrayList<Vector3f> normals, FileWriter writer) throws IOException {
        if (!normals.isEmpty()) {
            writer.write("\n");
            lineInd++;
        }
        for (Vector3f vn : normals) {
            writer.write(String.format("vn %.6f %.6f %.6f\n", vn.getX(), vn.getY(), vn.getZ()));
            lineInd++;
        }
    }

    private static void writeF(
            ArrayList<ArrayList<Integer>> polygonVertexIndices,
            ArrayList<ArrayList<Integer>> polygonTextureVertexIndices,
            ArrayList<ArrayList<Integer>> polygonNormalIndices,
            int verticesCount,
            FileWriter writer
    ) throws IOException {

        if (!polygonVertexIndices.isEmpty()) {
            writer.write("\n");
            lineInd++;
        }

        for (int i = 0; i < polygonVertexIndices.size(); i++) {
            StringBuilder builder = new StringBuilder();
            builder.append("f ");

            ArrayList<Integer> integersIndices = polygonVertexIndices.get(i);
            ArrayList<Integer> integersTexture = polygonTextureVertexIndices.get(i);
            ArrayList<Integer> integersNormal = polygonNormalIndices.get(i);

            int jMax = Math.max(
                    integersIndices.size(),
                    Math.max(
                            integersTexture.size(),
                            integersNormal.size()
                    )
            );

            for (int j = 0; j < jMax; j++) {
                try {
                    int indices = integersIndices.get(j) + 1 + verticesCount;
                    builder.append(indices);

                    if (!integersTexture.isEmpty()) {
                        int texture = integersTexture.get(j) + 1 + verticesCount;
                        builder.append("/");
                        builder.append(texture);
                    }

                    if (integersTexture.isEmpty() && !integersNormal.isEmpty()) {
                        builder.append("/");
                    }
                    if (!integersNormal.isEmpty()) {
                        int normal = integersNormal.get(j) + 1 + verticesCount;
                        builder.append("/");
                        builder.append(normal);
                    }
                    builder.append(" ");

                } catch (IndexOutOfBoundsException e) {
                    throw new ObjWriterException("Too few arguments.", lineInd);
                }
            }
            writer.write(builder + "\n");
            lineInd++;
        }
    }
}
