package com.vsu.cgcourse.model;

import java.util.ArrayList;
import java.util.Arrays;

public class MeshTriangulation {

    public static void triangulation(Mesh meshToTriangulate) {

        meshToTriangulate.polygonVertexIndices = triangulationVertexIndices(meshToTriangulate.polygonVertexIndices);
        meshToTriangulate.polygonTextureVertexIndices = triangulationTexture(meshToTriangulate.polygonTextureVertexIndices);
        meshToTriangulate.polygonNormalIndices = triangulationNormal(meshToTriangulate.polygonNormalIndices);

    }

    private static ArrayList<ArrayList<Integer>> triangulationVertexIndices(ArrayList<ArrayList<Integer>> polygonVertexIndices){
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        for (ArrayList<Integer> value: polygonVertexIndices){
            int n = value.size() - 3;
            if (n > 0) {
                for (int i = 0; i <= n; i++) {
                    list.add(new ArrayList<>(Arrays.asList(value.get(0),
                            value.get(1 + i), value.get(2 + i))));
                }
            }
        }

        return list;
    }

    private static ArrayList<ArrayList<Integer>> triangulationNormal(ArrayList<ArrayList<Integer>> polygonNormalIndices){
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        for (ArrayList<Integer> value: polygonNormalIndices){
            int n = value.size() - 3;
            if (n > 0) {
                for (int i = 0; i <= n; i++) {
                    list.add(new ArrayList<>(Arrays.asList(value.get(0),
                            value.get(1 + i), value.get(2 + i))));
                }
            }
        }

        return list;
    }

    private static ArrayList<ArrayList<Integer>> triangulationTexture(ArrayList<ArrayList<Integer>> polygonTextureVertexIndices){
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        for (ArrayList<Integer> value: polygonTextureVertexIndices){
            int n = value.size() - 3;
            if (n > 0) {
                for (int i = 0; i <= n; i++) {
                    list.add(new ArrayList<>(Arrays.asList(value.get(0),
                            value.get(1 + i), value.get(2 + i))));
                }
            }
        }

        return list;
    }
}
