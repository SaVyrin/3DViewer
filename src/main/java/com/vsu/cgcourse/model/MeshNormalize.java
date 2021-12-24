package com.vsu.cgcourse.model;

import com.vsu.cgcourse.math.vectors.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

public class MeshNormalize {
    public static void addNormals(Mesh mesh) {
        int index = 0; //текущий индекс
        mesh.polygonNormalIndices.clear();
        mesh.normals.clear();
        for(ArrayList<Integer> listPolygonIndex : mesh.polygonVertexIndices){

            Vector3f v1 = mesh.vertices.get(listPolygonIndex.get(0));
            Vector3f v2 = mesh.vertices.get(listPolygonIndex.get(1));
            Vector3f v3 = mesh.vertices.get(listPolygonIndex.get(2));

            Vector3f vector1 = new Vector3f();
            vector1.sub(v2,v1);
            Vector3f vector2 = new Vector3f();
            vector2.sub(v3,v1);

            Vector3f vectorNormal = new Vector3f();
            vectorNormal.cross(vector1, vector2);

            mesh.normals.add(vectorNormal);
            mesh.polygonNormalIndices.add(new ArrayList<>(Arrays.asList(index,index,index)));
            index++;
        }
    }
}
