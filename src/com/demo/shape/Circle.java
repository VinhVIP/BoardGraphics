package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

import java.util.ArrayList;

/**
 * Create by VinhIT
 * On 29/03/2021
 */

public class Circle extends Geometry {

    private int totalPoints = 2;

    private int radius;

    public Circle(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color, int colorFill, boolean isFillColor, boolean is2DShape) {
        super(canvas, startPoint, endPoint, drawMode, color, colorFill, isFillColor, is2DShape);
        initSizePoints(totalPoints);
    }

    public Circle(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color) {
        super(canvas, startPoint, endPoint, drawMode, color);
        initSizePoints(totalPoints);
    }

    public Circle(DrawCanvas canvas, DrawMode drawMode, int color) {
        super(canvas, drawMode, color);
        initSizePoints(totalPoints);
    }

    public Circle(DrawCanvas canvas, DrawMode drawMode, boolean isFillColor) {
        super(canvas, drawMode, isFillColor);
        initSizePoints(totalPoints);
    }

    public Circle(DrawCanvas canvas) {
        super(canvas);
        initSizePoints(totalPoints);
    }


    @Override
    public Geometry copy() {
        Circle g = new Circle(canvas, new Point2D(points[0]), new Point2D(points[1]), drawMode, color, colorFill, isFillColor, is2DShape);

        for (int i = 0; i < totalPoints; i++)
            g.points[i] = new Point2D(points[i]);

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        g.radius = radius;

        return g;
    }

    @Override
    public void processDraw() {
        if (points[0] != null && points[1] != null) {
            swapList();
            circleMidPoint(radius);
            choosePoints();
        }
    }

    @Override
    public void setPoints(Point2D[] points) {
        super.setPoints(points);
        startPoint = points[0];
        endPoint = points[1];
        radius = startPoint.distance(endPoint);
    }

    private Point2D getPoint(Point2D p, int k) {
        int xc = startPoint.getX();
        int yc = startPoint.getY();
        int x = p.getX() - xc;
        int y = p.getY() - yc;

        return switch (k) {
            case 0 -> new Point2D(y + xc, x + yc, p.getColor());
            case 1 -> new Point2D(y + xc, -x + yc, p.getColor());
            case 2 -> new Point2D(x + xc, -y + yc, p.getColor());
            case 3 -> new Point2D(-x + xc, -y + yc, p.getColor());
            case 4 -> new Point2D(-y + xc, -x + yc, p.getColor());
            case 5 -> new Point2D(-y + xc, x + yc, p.getColor());
            case 6 -> new Point2D(-x + xc, y + yc, p.getColor());
            default -> null;
        };
    }


    private void choosePoints() {
        int n = listDraw.size();

        ArrayList<Point2D> listTemp = new ArrayList<>();

        for (int k = 0; k < 7; k++) {
            for (int i = 0; i < n; i++) {
                Point2D p = getPoint(listDraw.get(i), k);
                if (!isListDrawContain(p)) {
                    if (k % 2 == 0) {
                        listTemp.add(0, p);
                    } else {
                        listTemp.add(p);
                    }
                }
            }
            listDraw.addAll(listTemp);
            listTemp.clear();
        }


        for (int i = 0; i < listDraw.size(); i++) {
            if (isShowPoint(i)) listTemp.add(listDraw.get(i));
        }

        listDraw.clear();
        listDraw.addAll(listTemp);
        listTemp.clear();

    }

    private void putPixel(int x, int y, int color) {
        int xc = startPoint.getX();
        int yc = startPoint.getY();

        Point2D p = new Point2D(x + xc, y + yc, color);
        if (!isListDrawContain(p))
            listDraw.add(p);
    }


    private void circleMidPoint(int R) {
        int x, y;

        x = 0;
        y = R;

        putPixel(x, y, color);
        double p = 1.25 - R; // 5/4-R
        while (x < y) {
            if (p < 0) p += 2 * x + 3;
            else {
                p += 2 * (x - y) + 5;
                y--;
            }
            x++;
            putPixel(x, y, color);
        }

    }

    @Override
    public void setStartPoint(Point2D startPoint) {
        super.setStartPoint(startPoint);
        points[0] = startPoint;
    }

    @Override
    public void setEndPoint(Point2D endPoint) {
        super.setEndPoint(endPoint);
        radius = points[0].distance(endPoint);
        points[1] = new Point2D(points[0].getX() + radius, points[0].getY());
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public Point2D getCenterPoint() {
        return new Point2D(points[0]);
    }

    @Override
    public String toString() {
        try {
            return String.format("Circle: (%d, %d) ; R = %d", startPoint.getX(), startPoint.getY(), radius);
        } catch (Exception e) {
            return "";
        }
    }
}
