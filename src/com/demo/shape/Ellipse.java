package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.models.Point2D;

/**
 * Create by VinhIT
 * On 11/04/2021
 */

public class Ellipse extends Geometry {

    public Ellipse(DrawCanvas canvas, Point2D startPoint, Point2D endPoint) {
        super(canvas, startPoint, endPoint);
    }

    public Ellipse(DrawCanvas canvas) {
        super(canvas);
    }

    @Override
    public void setupDraw() {
        if (startPoint != null && endPoint != null) {
            swapList();

            midEllipse(startPoint.getX(), startPoint.getY(), Math.abs(endPoint.getX() - startPoint.getX()), Math.abs(endPoint.getY() - startPoint.getY()), DrawCanvas.currentColor);

            clearOldPoints();
            drawNewPoints();
        }
    }

    @Override
    public void showPointsCoordinate() {

    }

    void plot(int xc, int yc, int x, int y, int color) {
        listDraw.add(new Point2D(xc + x, yc + y, DrawCanvas.currentColor));
        listDraw.add(new Point2D(xc - x, yc + y, DrawCanvas.currentColor));
        listDraw.add(new Point2D(xc + x, yc - y, DrawCanvas.currentColor));
        listDraw.add(new Point2D(xc - x, yc - y, DrawCanvas.currentColor));
    }

    void midEllipse(int xc, int yc, int a, int b, int color) {
        int x, y, fx, fy, a2, b2, p;
        x = 0;
        y = b;
        a2 = a * a; //a2
        b2 = b * b; // b2
        fx = 0;
        fy = 2 * a2 * y; // 2a2y
        plot(xc, yc, x, y, color);
        p = (int) Math.round(b2 - (a2 * b) + (0.25 * a2)); // p=b2 - a2b + a2/4
        while (fx < fy) {
            x++;
            fx += 2 * b2; //2b2
            if (p < 0)
                p += b2 * (2 * x + 3); // p=p + b2 (2x +3)
            else {
                y--;
                p += b2 * (2 * x + 3) + a2 * (-2 * y + 2); // p = p + b2(2x +3) + a2 (-2y +2)
                fy -= 2 * a2; // 2a2
            }
            plot(xc, yc, x, y, color);
        }
        p = (int) Math.round(b2 * (x + 0.5) * (x + 0.5) + a2 * (y - 1) * (y - 1) - a2 * b2);
        while (y > 0) {
            y--;
            fy -= 2 * a2; // 2a2
            if (p >= 0)
                p += a2 * (3 - 2 * y); //p =p + a2(3-2y)
            else {
                x++;
                fx += 2 * b2; // 2b2
                p += b2 * (2 * x + 2) + a2 * (-2 * y + 3); //p=p + b2(2x +2) +a2(-2y +3)
            }
            plot(xc, yc, x, y, color);
        }

    }

}
