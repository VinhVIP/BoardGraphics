package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by VinhIT
 * On 11/04/2021
 */

public class Ellipse extends Geometry {

    private int totalPoints = 3;

    public Ellipse(DrawCanvas canvas, Point2D startPoint, Point2D endPoint) {
        super(canvas, startPoint, endPoint);
        initSizePoints(totalPoints);
    }

    public Ellipse(DrawCanvas canvas) {
        super(canvas);
        initSizePoints(totalPoints);
    }

    public Ellipse(DrawCanvas canvas, DrawMode drawMode) {
        super(canvas, drawMode);
        initSizePoints(totalPoints);
    }

    @Override
    public void setupDraw() {
        if (startPoint != null && endPoint != null) {
            swapList();

//            midEllipse(startPoint.getX(), startPoint.getY(), Math.abs(endPoint.getX() - startPoint.getX()), Math.abs(endPoint.getY() - startPoint.getY()), color);

            midEllipse(points[0].getX(), points[0].getY(), Math.abs(points[1].getX()-points[0].getX()), Math.abs(points[2].getY()-points[0].getY()), color);

            choosePoints();

            clearOldPoints();
            drawNewPoints();
        }
    }

    @Override
    public void showPointsCoordinate() {

    }

    void choosePoints() {
        int n = listDraw.size();
        List<Point2D> listTmp = new ArrayList<>();
        Point2D point;

        for (int k = 2; k <= 4; k++) {
            for (int i = 0; i < n; i++) {
                point = listDraw.get(i);
                int x = point.getX() - startPoint.getX();
                int y = point.getY() - startPoint.getY();

                switch (k) {
                    case 2 -> {
                        Point2D p = new Point2D(startPoint.getX() + x, startPoint.getY() - y, point.getColor());
                        if (!isListDrawContain(p)) listTmp.add(0, p);
                    }
                    case 3 -> {
                        Point2D p = new Point2D(startPoint.getX() - x, startPoint.getY() - y, point.getColor());
                        if (!isListDrawContain(p)) listTmp.add(p);
                    }
                    case 4 -> {
                        Point2D p = new Point2D(startPoint.getX() - x, startPoint.getY() + y, point.getColor());
                        if (!isListDrawContain(p)) listTmp.add(0, p);
                    }
                }
            }
            listDraw.addAll(listTmp);
            listTmp.clear();
        }

        for (int i = 0; i < listDraw.size(); i++) {
            if (isShowPoint(i)) listTmp.add(listDraw.get(i));
        }

        listDraw.clear();
        listDraw.addAll(listTmp);
        listTmp.clear();
    }

    void plot(int xc, int yc, int x, int y, int color) {
        Point2D p = new Point2D(xc + x, yc + y, color);
        if (!isListDrawContain(p)) listDraw.add(p);
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

    @Override
    public void setPoints(Point2D[] points) {
        super.setPoints(points);
        startPoint = points[0];
    }

    @Override
    public void setStartPoint(Point2D startPoint) {
        super.setStartPoint(startPoint);
        points[0] = startPoint;
    }

    @Override
    public void setEndPoint(Point2D endPoint) {
        super.setEndPoint(endPoint);
        points[1] = new Point2D(endPoint);
        points[1].set(endPoint.getX(), points[0].getY());

        points[2] = new Point2D(endPoint);
        points[2].set(points[0].getX(), endPoint.getY());
    }

    @Override
    public String toString() {
        try{
            return String.format("Ellipse: (%d, %d) a=%d, b=%d",
                    startPoint.getX(), startPoint.getY(),
                    Math.abs(endPoint.getX() - startPoint.getX()),
                    Math.abs(endPoint.getY() - startPoint.getY()));
        }catch (Exception e){
            return "";
        }

    }
}
