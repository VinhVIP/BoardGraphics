package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.models.Point2D;

import java.util.ArrayList;

/**
 * Create by VinhIT
 * On 29/03/2021
 */

public class Circle extends Geometry {

    public Circle(DrawCanvas canvas, Point2D startPoint, Point2D endPoint) {
        super(canvas, startPoint, endPoint);
    }

    public Circle(DrawCanvas canvas) {
        super(canvas);
    }

    @Override
    public void setupDraw() {
        if (startPoint != null && endPoint != null) {
            int r = (int) Math.sqrt((startPoint.getX() - endPoint.getX()) * (startPoint.getX() - endPoint.getX()) +
                    (startPoint.getY() - endPoint.getY()) * (startPoint.getY() - endPoint.getY()));

            swapList();

            circleMidPoint(r);

            choosePoints();

            clearOldPoints();
            drawNewPoints();
        }
    }

    @Override
    public void showPointsCoordinate() {

    }

    Point2D getPoint(Point2D p, int k) {
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

    boolean isListDrawContain(Point2D point) {
        for (Point2D p : listDraw) {
            if (p.equals(point)) {
                return true;
            }
        }
        return false;
    }

    void choosePoints() {
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

    void putPixel(int x, int y, int color) {
        int xc = startPoint.getX();
        int yc = startPoint.getY();

        Point2D p = new Point2D(x + xc, y + yc, color);
        if (!isListDrawContain(p))
            listDraw.add(p);
    }


    private boolean isShowPoint(int index) {
        switch (DrawCanvas.lineMode) {  // DrawCanvas.lineMode là chế độ vẽ
            case DEFAULT -> {           // Nét liền
                return true;
            }
            case DOT -> {               // Nét chấm
                return (index % 2) == 0;
            }
            case DASH -> {              // Nét gạch
                return (index % 6) < 4;
            }
            case DASH_DOT -> {          // Nét gạch chấm
                return (index % 6) < 3 || (index % 6) == 4;
            }
            case DASH_DOT_DOT -> {      // Nét gạch 2 chấm
                return (index % 12 < 4) || (index % 12) == 6 || (index % 12) == 9;
            }
        }
        return true;
    }

    void circleMidPoint(int R) {
        int x, y;

        x = 0;
        y = R;

        putPixel(x, y, DrawCanvas.currentColor);
        double p = 1.25 - R; // 5/4-R
        while (x < y) {
            if (p < 0) p += 2 * x + 3;
            else {
                p += 2 * (x - y) + 5;
                y--;
            }
            x++;
            putPixel(x, y, DrawCanvas.currentColor);
        }

    }
}
