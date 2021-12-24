package com.vsu.cgcourse.render_engine;

import com.vsu.cgcourse.Simple3DViewer;
import com.vsu.cgcourse.math.points.Point3f;
import com.vsu.cgcourse.math.vectors.Vector2f;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TempRasterization {

    private static int[][] pixels;
    public static float[][] zBuffer;

    static {
        try {
            BufferedImage bufferedImage = ImageIO.read(Objects.requireNonNull(
                    Simple3DViewer.class.getResource("textures/gradient.jpg")));
            pixels = TempRasterization.convertTo2DUsingGetRGB(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void drawLine(int x1, int y1, float z1,
                                 int x2, int y2, float z2,
                                 Point3f p1, Point3f p2, Point3f p3,
                                 PixelWriter pixelWriter) {
        int dx = (x2 - x1 >= 0 ? 1 : -1);
        int dy = (y2 - y1 >= 0 ? 1 : -1);

        int lengthX = Math.abs(x2 - x1);
        int lengthY = Math.abs(y2 - y1);
        int length = Math.max(lengthX, lengthY);

        Vector2f vx1 = new Vector2f(p2.getX() - p1.getX(), p2.getY() - p1.getY());
        Vector2f vx2 = new Vector2f(p3.getX() - p1.getX(), p3.getY() - p1.getY());

        if (length == 0) {
            int z = (int) z1;
            if (zBuffer[y1][x1] > z) {
                zBuffer[y1][x1] = z;
                /*int col = getColor(x1, y1, p1, vx1, vx2);
                writePixel(x1, y1, col, pixelWriter);*/
                pixelWriter.setColor(x1, y1, Color.rgb(lengthX, lengthY, length));
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
                    /*int col = getColor(x, y, p1, vx1, vx2);
                    writePixel(x, y, col, pixelWriter);*/
                    pixelWriter.setColor(x, y, Color.rgb(Math.min(lengthX, 255), Math.min(lengthY, 255), Math.min(length, 255)));
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
                    /*int col = getColor(x, y, p1, vx1, vx2);
                    writePixel(x, y, col, pixelWriter);*/
                    pixelWriter.setColor(x, y, Color.rgb(Math.min(lengthX, 255), Math.min(lengthY, 255), Math.min(length, 255)));
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
            drawLine((int) curX1, scanlineY, curZ1, (int) curX2, scanlineY, curZ2, p1, p2, p3, pixelWriter);
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
            drawLine((int) curx1, scanlineY, curZ1, (int) curx2, scanlineY, curZ2, p1, p2, p3, pixelWriter);
        }
    }

    public static void drawTriangle(List<Point3f> point2fList, PixelWriter pixelWriter) {
        point2fList.sort((o1, o2) -> Float.compare(o1.getY(), o2.getY()));

        Point3f p1 = point2fList.get(0);
        Point3f p2 = point2fList.get(1);
        Point3f p3 = point2fList.get(2);

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

    private static int getColor(float x, float y, Point3f p1, Vector2f v1, Vector2f v2) {
        //Барицентрический координаты
        Vector2f vx3 = new Vector2f(x - p1.getX(), y - p1.getY());

        float projectionV1 = v1.dot(vx3) / v1.dot(v1);
        float projectionV2 = v2.dot(vx3) / v2.dot(v2);

        Vector2f result = v1.multi(projectionV1).add(v2.multi(projectionV2));
        result = result.add(new Vector2f(p1.getX(), p1.getY()));

        int resX = (int) result.getX();
        int resY = (int) result.getY();
        //Барицентрический координаты
        return pixels[resY][resX];
    }

    private static void writePixel(int x, int y, int col, PixelWriter pixelWriter) {
        int blue = col & 0xff;
        int green = (col & 0xff00) >> 8;
        int red = (col & 0xff0000) >> 16;
        //pixelWriter.setColor(x, y, Color.rgb(Math.min(lengthX, 255), Math.min(lengthY, 255), Math.min(length, 255)));
        pixelWriter.setColor(x, y, Color.rgb(red, green, blue));
    }

    private static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = image.getRGB(col, row);
            }
        }

        return result;
    }
}
