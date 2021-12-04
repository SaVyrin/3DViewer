package com.vsu.cgcourse.obj_reader;

import com.vsu.cgcourse.math.vectors.Vector2f;
import com.vsu.cgcourse.math.vectors.Vector3f;
import com.vsu.cgcourse.model.Mesh;
import com.vsu.cgcourse.model.TransformMesh;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ObjReader {

    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public static TransformMesh read(String fileContent, String meshName) {
        Mesh mesh = new Mesh();
        TransformMesh result = new TransformMesh(meshName, mesh);

        int lineInd = 0;
        Scanner scanner = new Scanner(fileContent);
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            ArrayList<String> wordsInLine = new ArrayList<>(Arrays.asList(line.split("\\s+")));
            if (wordsInLine.isEmpty())
                continue;

            final String token = wordsInLine.get(0);
            wordsInLine.remove(0);

            ++lineInd;
            switch (token) {

                case OBJ_VERTEX_TOKEN -> mesh.vertices.add(parseVertex(wordsInLine, lineInd));
                case OBJ_TEXTURE_TOKEN -> mesh.textureVertices.add(parseTextureVertex(wordsInLine, lineInd));
                case OBJ_NORMAL_TOKEN -> mesh.normals.add(parseNormal(wordsInLine, lineInd));

                case OBJ_FACE_TOKEN -> parseFace(
                        wordsInLine,
                        mesh.polygonVertexIndices,
                        mesh.polygonTextureVertexIndices,
                        mesh.polygonNormalIndices,
                        lineInd);
                default -> {
                }
            }
        }
        return result;
    }

    protected static Vector3f parseVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        try {
            return new Vector3f(
                    Float.parseFloat(wordsInLineWithoutToken.get(0)),
                    Float.parseFloat(wordsInLineWithoutToken.get(1)),
                    Float.parseFloat(wordsInLineWithoutToken.get(2)));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, new ObjReaderException("Failed to parse float value.", lineInd).getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new ObjReaderException("Failed to parse float value.", lineInd);

        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, new ObjReaderException("Too few vertex arguments.", lineInd).getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new ObjReaderException("Too few vertex arguments.", lineInd);
        }
    }

    protected static Vector2f parseTextureVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        try {
            return new Vector2f(
                    Float.parseFloat(wordsInLineWithoutToken.get(0)),
                    Float.parseFloat(wordsInLineWithoutToken.get(1)));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, new ObjReaderException("Failed to parse float value.", lineInd).getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new ObjReaderException("Failed to parse float value.", lineInd);

        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, new ObjReaderException("Too few texture vertex arguments.", lineInd).getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new ObjReaderException("Too few texture vertex arguments.", lineInd);
        }
    }

    protected static Vector3f parseNormal(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
        try {
            return new Vector3f(
                    Float.parseFloat(wordsInLineWithoutToken.get(0)),
                    Float.parseFloat(wordsInLineWithoutToken.get(1)),
                    Float.parseFloat(wordsInLineWithoutToken.get(2)));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, new ObjReaderException("Failed to parse float value.", lineInd).getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new ObjReaderException("Failed to parse float value.", lineInd);

        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, new ObjReaderException("Too few normal arguments.", lineInd).getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new ObjReaderException("Too few normal arguments.", lineInd);
        }
    }

    protected static void parseFace(
            final ArrayList<String> wordsInLineWithoutToken,
            ArrayList<ArrayList<Integer>> polygonVertexIndices,
            ArrayList<ArrayList<Integer>> polygonTextureVertexIndices,
            ArrayList<ArrayList<Integer>> polygonNormalIndices,
            int lineInd) {
        ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonNormalIndices = new ArrayList<>();

        for (String s : wordsInLineWithoutToken) {
            parseFaceWord(s, onePolygonVertexIndices, onePolygonTextureVertexIndices, onePolygonNormalIndices, lineInd);
        }

        polygonVertexIndices.add(onePolygonVertexIndices);
        polygonTextureVertexIndices.add(onePolygonTextureVertexIndices);
        polygonNormalIndices.add(onePolygonNormalIndices);
    }

    protected static void parseFaceWord(
            String wordInLine,
            ArrayList<Integer> onePolygonVertexIndices,
            ArrayList<Integer> onePolygonTextureVertexIndices,
            ArrayList<Integer> onePolygonNormalIndices,
            int lineInd) {
        try {
            String[] wordIndices = wordInLine.split("/");
            switch (wordIndices.length) {
                case 1 -> onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
                case 2 -> {
                    onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
                    onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
                }
                case 3 -> {
                    onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
                    onePolygonNormalIndices.add(Integer.parseInt(wordIndices[2]) - 1);
                    if (!wordIndices[1].equals("")) {
                        onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
                    }
                }
                default -> {
                    JOptionPane.showMessageDialog(null, new ObjReaderException("Invalid element size.", lineInd).getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new ObjReaderException("Invalid element size.", lineInd);
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, new ObjReaderException("Failed to parse int value.", lineInd).getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new ObjReaderException("Failed to parse int value.", lineInd);

        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, new ObjReaderException("Too few arguments.", lineInd).getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new ObjReaderException("Too few arguments.", lineInd);
        }
    }
}
