package com.vsu.cgcourse.obj_writer;

import com.vsu.cgcourse.math.Vector2f;
import com.vsu.cgcourse.math.Vector3f;
import com.vsu.cgcourse.model.Mesh;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ObjWriterTest {
    @Test
    public void testVertices() {
        Locale.setDefault(Locale.ROOT);

        try {
            Mesh mesh = new Mesh();
            ArrayList<Vector3f> vertices = new ArrayList<>();
            vertices.add(new Vector3f(1.34f, 0.56f, -1.2f));
            mesh.vertices = vertices;
            List<Mesh> meshList = new ArrayList<>();
            meshList.add(mesh);
            ObjWriter.write(meshList, "MyModel1");

            Path fileName = Path.of("MyModel1.obj");
            String result = Files.readString(fileName);
            String expectedResult = "v 1.340000 0.560000 -1.200000\n";

            Assertions.assertEquals(result, expectedResult);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testTextureVertices() {
        Locale.setDefault(Locale.ROOT);

        try {
            Mesh mesh = new Mesh();
            ArrayList<Vector2f> vertices = new ArrayList<>();
            vertices.add(new Vector2f(1.34f, 0.56f));
            mesh.textureVertices = vertices;
            List<Mesh> meshList = new ArrayList<>();
            meshList.add(mesh);
            ObjWriter.write(meshList, "MyModel2");

            Path fileName = Path.of("MyModel2.obj");
            String result = Files.readString(fileName);
            String expectedResult = "\n" +
                    "vt 1.340000 0.560000\n";

            Assertions.assertEquals(result, expectedResult);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testNormals() {
        Locale.setDefault(Locale.ROOT);

        try {
            Mesh mesh = new Mesh();
            ArrayList<Vector3f> vertices = new ArrayList<>();
            vertices.add(new Vector3f(1.34f, 0.56f, -1.2f));
            mesh.normals = vertices;
            List<Mesh> meshList = new ArrayList<>();
            meshList.add(mesh);
            ObjWriter.write(meshList, "MyModel3");

            Path fileName = Path.of("MyModel3.obj");
            String result = Files.readString(fileName);
            String expectedResult = "\n" +
                    "vn 1.340000 0.560000 -1.200000\n";

            Assertions.assertEquals(result, expectedResult);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testPolygons01() {
        Locale.setDefault(Locale.ROOT);

        try {
            Mesh mesh = new Mesh();
            ArrayList<ArrayList<Integer>> polygonVertexIndices = new ArrayList<>();
            ArrayList<ArrayList<Integer>> polygonTextureVertexIndices = new ArrayList<>();
            ArrayList<ArrayList<Integer>> polygonNormalIndices = new ArrayList<>();

            ArrayList<Integer> vertex = new ArrayList<>();
            vertex.add(0);
            vertex.add(1);
            vertex.add(2);
            polygonVertexIndices.add(vertex);

            ArrayList<Integer> texture = new ArrayList<>();
            polygonTextureVertexIndices.add(texture);

            ArrayList<Integer> normal = new ArrayList<>();
            polygonNormalIndices.add(normal);

            mesh.polygonVertexIndices = polygonVertexIndices;
            mesh.polygonTextureVertexIndices = polygonTextureVertexIndices;
            mesh.polygonNormalIndices = polygonNormalIndices;
            List<Mesh> meshList = new ArrayList<>();
            meshList.add(mesh);
            ObjWriter.write(meshList, "MyModel4");

            Path fileName = Path.of("MyModel4.obj");
            String result = Files.readString(fileName);
            String expectedResult = "\n" +
                    "f 1 2 3 \n";

            Assertions.assertEquals(result, expectedResult);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testPolygons02() {
        Locale.setDefault(Locale.ROOT);

        try {
            Mesh mesh = new Mesh();
            ArrayList<ArrayList<Integer>> polygonVertexIndices = new ArrayList<>();
            ArrayList<ArrayList<Integer>> polygonTextureVertexIndices = new ArrayList<>();
            ArrayList<ArrayList<Integer>> polygonNormalIndices = new ArrayList<>();

            ArrayList<Integer> vertex = new ArrayList<>();
            vertex.add(0);
            vertex.add(1);
            vertex.add(2);
            polygonVertexIndices.add(vertex);

            ArrayList<Integer> texture = new ArrayList<>();
            texture.add(0);
            texture.add(1);
            texture.add(2);
            polygonTextureVertexIndices.add(texture);

            ArrayList<Integer> normal = new ArrayList<>();
            polygonNormalIndices.add(normal);

            mesh.polygonVertexIndices = polygonVertexIndices;
            mesh.polygonTextureVertexIndices = polygonTextureVertexIndices;
            mesh.polygonNormalIndices = polygonNormalIndices;
            List<Mesh> meshList = new ArrayList<>();
            meshList.add(mesh);
            ObjWriter.write(meshList, "MyModel5");

            Path fileName = Path.of("MyModel5.obj");
            String result = Files.readString(fileName);
            String expectedResult = "\n" +
                    "f 1/1 2/2 3/3 \n";

            Assertions.assertEquals(result, expectedResult);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testPolygons03() {
        Locale.setDefault(Locale.ROOT);

        try {
            Mesh mesh = new Mesh();
            ArrayList<ArrayList<Integer>> polygonVertexIndices = new ArrayList<>();
            ArrayList<ArrayList<Integer>> polygonTextureVertexIndices = new ArrayList<>();
            ArrayList<ArrayList<Integer>> polygonNormalIndices = new ArrayList<>();

            ArrayList<Integer> vertex = new ArrayList<>();
            vertex.add(0);
            vertex.add(1);
            vertex.add(2);
            polygonVertexIndices.add(vertex);

            ArrayList<Integer> texture = new ArrayList<>();
            texture.add(0);
            texture.add(1);
            texture.add(2);
            polygonTextureVertexIndices.add(texture);

            ArrayList<Integer> normal = new ArrayList<>();
            normal.add(0);
            normal.add(1);
            normal.add(2);
            polygonNormalIndices.add(normal);

            mesh.polygonVertexIndices = polygonVertexIndices;
            mesh.polygonTextureVertexIndices = polygonTextureVertexIndices;
            mesh.polygonNormalIndices = polygonNormalIndices;
            List<Mesh> meshList = new ArrayList<>();
            meshList.add(mesh);
            ObjWriter.write(meshList, "MyModel6");

            Path fileName = Path.of("MyModel6.obj");
            String result = Files.readString(fileName);
            String expectedResult = "\n" +
                    "f 1/1/1 2/2/2 3/3/3 \n";

            Assertions.assertEquals(result, expectedResult);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testPolygons04() {
        Locale.setDefault(Locale.ROOT);

        try {
            Mesh mesh = new Mesh();
            ArrayList<ArrayList<Integer>> polygonVertexIndices = new ArrayList<>();
            ArrayList<ArrayList<Integer>> polygonTextureVertexIndices = new ArrayList<>();
            ArrayList<ArrayList<Integer>> polygonNormalIndices = new ArrayList<>();

            ArrayList<Integer> vertex = new ArrayList<>();
            vertex.add(0);
            vertex.add(1);
            vertex.add(2);
            polygonVertexIndices.add(vertex);

            ArrayList<Integer> texture = new ArrayList<>();
            polygonTextureVertexIndices.add(texture);

            ArrayList<Integer> normal = new ArrayList<>();
            normal.add(0);
            normal.add(1);
            normal.add(2);
            polygonNormalIndices.add(normal);

            mesh.polygonVertexIndices = polygonVertexIndices;
            mesh.polygonTextureVertexIndices = polygonTextureVertexIndices;
            mesh.polygonNormalIndices = polygonNormalIndices;
            List<Mesh> meshList = new ArrayList<>();
            meshList.add(mesh);
            ObjWriter.write(meshList, "MyModel7");

            Path fileName = Path.of("MyModel7.obj");
            String result = Files.readString(fileName);
            String expectedResult = "\n" +
                    "f 1//1 2//2 3//3 \n";

            Assertions.assertEquals(result, expectedResult);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testPolygons05() {
        Locale.setDefault(Locale.ROOT);

        try {
            Mesh mesh = new Mesh();
            ArrayList<ArrayList<Integer>> polygonVertexIndices = new ArrayList<>();
            ArrayList<ArrayList<Integer>> polygonTextureVertexIndices = new ArrayList<>();
            ArrayList<ArrayList<Integer>> polygonNormalIndices = new ArrayList<>();

            ArrayList<Integer> vertex = new ArrayList<>();
            vertex.add(0);
            vertex.add(1);
            vertex.add(2);
            polygonVertexIndices.add(vertex);

            ArrayList<Integer> texture = new ArrayList<>();
            polygonTextureVertexIndices.add(texture);

            ArrayList<Integer> normal = new ArrayList<>();
            normal.add(0);
            polygonNormalIndices.add(normal);

            mesh.polygonVertexIndices = polygonVertexIndices;
            mesh.polygonTextureVertexIndices = polygonTextureVertexIndices;
            mesh.polygonNormalIndices = polygonNormalIndices;
            List<Mesh> meshList = new ArrayList<>();
            meshList.add(mesh);
            ObjWriter.write(meshList, "MyModel8");

        } catch (ObjWriterException ex) {
            String expectedError = "Error writing OBJ file on line: 9. Too few arguments.";
            Assertions.assertEquals(expectedError, ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }
}
