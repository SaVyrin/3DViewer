package com.vsu.cgcourse.render_engine;

import com.vsu.cgcourse.Simple3DViewer;
import com.vsu.cgcourse.math.points.Point3f;
import com.vsu.cgcourse.math.vectors.Vector2f;
import com.vsu.cgcourse.math.vectors.Vector3f;
import com.vsu.cgcourse.model.Polygon;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rasterization {

    private static int[][] pixels;
    public static float[][] zBuffer;
    public static Polygon polygon;

    private static Point3f pt1;
    private static Point3f pt2;
    private static Point3f pt3;

    private static Vector2f vt11;
    private static Vector2f vt12;
    private static Vector2f vt13;

    static {
        try {
            BufferedImage bufferedImage = ImageIO.read(Objects.requireNonNull(
                    Simple3DViewer.class.getResource("textures/NeutralWrapped.jpg")));
            pixels = Rasterization.convertTo2DUsingGetRGB(bufferedImage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void drawLine(int x1, int y1, float z1,
                                 int x2, int y2, float z2,
                                 PixelWriter pixelWriter) {
        int dx = (x2 - x1 >= 0 ? 1 : -1);
        int dy = (y2 - y1 >= 0 ? 1 : -1);

        int lengthX = Math.abs(x2 - x1);
        int lengthY = Math.abs(y2 - y1);
        int length = Math.max(lengthX, lengthY);

        if (length == 0) {
            int z = (int) z1;
            if (zBuffer[y1][x1] > z) {
                zBuffer[y1][x1] = z;
                //int col = getColor(x1, y1);
                //writePixel(x1, y1, col, pixelWriter);
                //pixelWriter.setColor(x1, y1, Color.rgb(lengthX, lengthY, length));
                Color color = getColor2(x1, y1, z1, polygon.normal);
                pixelWriter.setColor(x1, y1, color);
            }
        }

        int x = x1;
        int y = y1;
        int d;
        length++;
        if (lengthY <= lengthX) {
            d = -lengthX;

            while (length > 0) {
                float z = (z1 + ((z2 - z1) / (x2 - x1)) * (x - x1));
                if (zBuffer[y][x] > z) {
                    zBuffer[y][x] = z;
                    //int col = getColor(x, y);
                    //writePixel(x, y, col, pixelWriter);
                    //pixelWriter.setColor(x, y, Color.rgb(Math.min(lengthX, 255), Math.min(lengthY, 255), Math.min(length, 255)));
                    Color color = getColor2(x, y, z, polygon.normal);
                    pixelWriter.setColor(x, y, color);
                }
                x += dx;
                d += 2 * lengthY;
                if (d > 0) {
                    d -= 2 * lengthX;
                    y += dy;
                }
                length--;
            }
        } else {
            d = -lengthY;

            while (length > 0) {
                int z = (int) (z1 + ((z2 - z1) / (x2 - x1)) * (x - x1));
                if (zBuffer[y][x] > z) {
                    zBuffer[y][x] = z;
                    //int col = getColor(x, y);
                    //writePixel(x, y, col, pixelWriter);
                    //pixelWriter.setColor(x, y, Color.rgb(Math.min(lengthX, 255), Math.min(lengthY, 255), Math.min(length, 255)));
                    Color color = getColor2(x, y, z, polygon.normal);
                    pixelWriter.setColor(x, y, color);
                }

                y += dy;
                d += 2 * lengthX;
                if (d > 0) {
                    d -= 2 * lengthY;
                    x += dx;
                }
                length--;
            }
        }
    }

    private static void fillBottomFlatTriangle(Point3f p1, Point3f p2, Point3f p3, PixelWriter pixelWriter) {
        float invSlopeX1 = (p2.getX() - p1.getX()) / (p2.getY() - p1.getY());
        float invSlopeX2 = (p3.getX() - p1.getX()) / (p3.getY() - p1.getY());

        float invSlopeZ1 = (p2.getZ() - p1.getZ()) / (p2.getY() - p1.getY());
        float invSlopeZ2 = (p3.getZ() - p1.getZ()) / (p3.getY() - p1.getY());

        float curX1 = p1.getX();
        float curX2 = p1.getX();

        float curZ1 = p1.getZ();
        float curZ2 = p1.getZ();

        float MaxX = Math.max(Math.max(p1.getX(), p2.getX()), p3.getX());
        float MinX = Math.min(Math.min(p1.getX(), p2.getX()), p3.getX());

        float MaxZ = Math.max(Math.max(p1.getZ(), p2.getZ()), p3.getZ());
        float MinZ = Math.min(Math.min(p1.getZ(), p2.getZ()), p3.getZ());

        for (int scanlineY = (int) p1.getY(); scanlineY <= p2.getY(); scanlineY++) {
            curX1 = Math.max(MinX, Math.min(MaxX, curX1 + invSlopeX1));
            curX2 = Math.min(MaxX, Math.max(MinX, curX2 + invSlopeX2));

            curZ1 = Math.max(MinZ, Math.min(MaxZ, curZ1 + invSlopeZ1));
            curZ2 = Math.min(MaxZ, Math.max(MinZ, curZ2 + invSlopeZ2));
            drawLine((int) curX1, scanlineY, curZ1, (int) curX2, scanlineY, curZ2, pixelWriter);
        }
    }

    private static void fillTopFlatTriangle(Point3f p1, Point3f p2, Point3f p3, PixelWriter pixelWriter) {
        float invslope1 = (p3.getX() - p1.getX()) / (p3.getY() - p1.getY());
        float invslope2 = (p3.getX() - p2.getX()) / (p3.getY() - p2.getY());

        float invSlopeZ1 = (p3.getZ() - p1.getZ()) / (p3.getY() - p1.getY());
        float invSlopeZ2 = (p3.getZ() - p2.getZ()) / (p3.getY() - p2.getY());

        float curx1 = p3.getX();
        float curx2 = p3.getX();

        float curZ1 = p3.getZ();
        float curZ2 = p3.getZ();

        float MaxX = Math.max(Math.max(p1.getX(), p2.getX()), p3.getX());
        float MinX = Math.min(Math.min(p1.getX(), p2.getX()), p3.getX());

        float MaxZ = Math.max(Math.max(p1.getZ(), p2.getZ()), p3.getZ());
        float MinZ = Math.min(Math.min(p1.getZ(), p2.getZ()), p3.getZ());

        for (int scanlineY = (int) p3.getY(); scanlineY > p1.getY(); scanlineY--) {
            curx1 = Math.max(MinX, Math.min(MaxX, curx1 - invslope1));
            curx2 = Math.min(MaxX, Math.max(MinX, curx2 - invslope2));

            curZ1 = Math.max(MinZ, Math.min(MaxZ, curZ1 - invSlopeZ1));
            curZ2 = Math.min(MaxZ, Math.max(MinZ, curZ2 - invSlopeZ2));
            drawLine((int) curx1, scanlineY, curZ1, (int) curx2, scanlineY, curZ2, pixelWriter);
        }
    }

    public static void drawTriangle(PixelWriter pixelWriter) {
        List<Point3f> point2fList = polygon.vertices;

        Point3f p1 = point2fList.get(0);
        Point3f p2 = point2fList.get(1);
        Point3f p3 = point2fList.get(2);

        List<Vector2f> vector2fList = polygon.textureVertices;

        Vector2f vt1 = vector2fList.get(0);
        Vector2f vt2 = vector2fList.get(1);
        Vector2f vt3 = vector2fList.get(2);

        if (p1.getY() > p2.getY()) {
            Point3f temp = p1;
            p1 = p2;
            p2 = temp;

            Vector2f tempVt = vt1;
            vt1 = vt2;
            vt2 = tempVt;
        }
        if (p1.getY() > p3.getY()) {
            Point3f temp = p1;
            p1 = p3;
            p3 = temp;

            Vector2f tempVt = vt1;
            vt1 = vt3;
            vt3 = tempVt;
        }
        if (p2.getY() > p3.getY()) {
            Point3f temp = p2;
            p2 = p3;
            p3 = temp;

            Vector2f tempVt = vt2;
            vt2 = vt3;
            vt3 = tempVt;
        }

        pt1 = p1;
        pt2 = p2;
        pt3 = p3;

        vt11 = vt1;
        vt12 = vt2;
        vt13 = vt3;

        if (p2.getY() == p3.getY()) {
            fillBottomFlatTriangle(p1, p2, p3, pixelWriter);
        } else if (p1.getY() == p2.getY()) {
            fillTopFlatTriangle(p1, p2, p3, pixelWriter);
        } else {
            Point3f p4 = new Point3f(
                    (p1.getX() + ((p2.getY() - p1.getY()) / (p3.getY() - p1.getY())) * (p3.getX() - p1.getX())),
                    p2.getY(),
                    (p1.getZ() + ((p2.getY() - p1.getY()) / (p3.getY() - p1.getY())) * (p3.getZ() - p1.getZ())));

            fillBottomFlatTriangle(p1, p2, p4, pixelWriter);
            fillTopFlatTriangle(p2, p4, p3, pixelWriter);
        }
    }

    private static int getColor(float x, float y) {
        float x0 = pt1.getX();
        float x1 = pt2.getX();
        float x2 = pt3.getX();

        float y0 = pt1.getY();
        float y1 = pt2.getY();
        float y2 = pt3.getY();

        float denominator = (y1 - y2) * (x0 - x2) + (x2 - x1) * (y0 - y2);

        float alf = ((y1 - y2) * (x - x2) + (x2 - x1) * (y - y2)) / denominator;
        float bet = ((y2 - y0) * (x - x2) + (x0 - x2) * (y - y2)) / denominator;
        float gamma = 1 - alf - bet;

        if (alf < 0 || bet < 0 || gamma < 0) {
            return -16777216;
        } else {
            int d = 0;
        }

        float resVtx = (vt11.getX() * alf + vt12.getX() * bet + vt13.getX() * gamma) * (pixels[0].length - 1);
        float resVty = (vt11.getY() * alf + vt12.getY() * bet + vt13.getY() * gamma) * (pixels.length - 1);
        int resX = (int) (resVtx);
        int resY = (int) (resVty);

        return pixels[resY][resX];
    }

    private static Color getColor2(float x, float y, float z, Vector3f normal) {
        Vector3f vectorLight = new Vector3f();
        vectorLight.sub(new Vector3f(x, y, z), new Vector3f(800, 350, -200)); //TODO что-то непонятное
        vectorLight.normalize();
        normal.normalize();
        double cos = normal.dot(vectorLight);
        if (cos < 0) {
            cos = 0;
        }
        return new Color(cos, 0, 0, 1);
    }

    private static void writePixel(int x, int y, int col, PixelWriter pixelWriter) {
        int blue = col & 0xff;
        int green = (col & 0xff00) >> 8;
        int red = (col & 0xff0000) >> 16;

        pixelWriter.setColor(x, y, Color.rgb(red, green, blue));
    }

    private static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[height-row-1][col] = image.getRGB(col, row);
            }
        }

        return result;
    }
}
