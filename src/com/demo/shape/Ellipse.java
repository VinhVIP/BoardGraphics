package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.models.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by VinhIT
 * On 11/04/2021
 */

public class Ellipse extends Geometry {

    private Point2D rootPoint;
    private double rotateAngle;
    private int a,b;

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
    public Geometry copy() {
        Ellipse g = new Ellipse(canvas);
        g.setStartPoint(new Point2D(startPoint));
        g.setEndPoint(new Point2D(endPoint));
        g.setDrawMode(drawMode);

        g.points = new Point2D[totalPoints];
        for(int i=0; i<totalPoints; i++)
            g.points[i] = new Point2D(points[i]);

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        return g;
    }

    @Override
    public void setupDraw() {
        if (startPoint != null && endPoint != null) {
            swapList();

            process();

            clearOldPoints();
            drawNewPoints();
        }
    }

    /*
       --A(2)--
     -         -
    |    O'(0)  |B(1)
     -         -
       -------
     */
    private void process() {
//        int x = points[0].getX();
//        int y = points[0].getY();
//
//        // Dịch chuyển tâm elip về gốc tọa độ
//        for(int i=0; i<points.length; i++)
//            points[i].set(points[i].getX()-x, points[i].getY()-y);

        // Xác định góc giữa O'B và trục Ox
        Vector2D vOB = new Vector2D(points[0], points[1]);
        double angle = vOB.angleRadian(Vector2D.oX);

        if (points[1].getY() > points[0].getY()) angle = -angle;

        // Xoay O'B trùng với trục Ox
        // O'A xoay theo tương ứng
        if (angle != 0) {
            points[1] = points[1].rotate(points[0], angle);
            points[2] = points[2].rotate(points[0], angle);
        }

        a = (int) Math.sqrt((points[0].getX() - points[1].getX()) * (points[0].getX() - points[1].getX()) + (points[0].getY() - points[1].getY()) * (points[0].getY() - points[1].getY()));
        b = (int) Math.sqrt((points[0].getX() - points[2].getX()) * (points[0].getX() - points[2].getX()) + (points[0].getY() - points[2].getY()) * (points[0].getY() - points[2].getY()));
        midEllipse(points[0].getX(), points[0].getY(), a, b, color);

        // Xoay trở về vị trí cũ
        if (angle != 0) {
            angle = -angle;

            points[1] = points[1].rotate(points[0], angle);
            points[2] = points[2].rotate(points[0], angle);
        }

        choosePoints();

        for (int i = 0; i < listDraw.size(); i++) {
            Point2D p = listDraw.get(i);
            if (rootPoint != null)
                p = p.rotate(rootPoint, rotateAngle);
            else
                p = p.rotate(points[0], angle);
            listDraw.set(i, p);
        }

    }

    public void setRotate(Point2D rootPoint, double rotateAngle) {
        this.rootPoint = rootPoint;
        this.rotateAngle = rotateAngle;
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
                int x = point.getX() - points[0].getX();
                int y = point.getY() - points[0].getY();

                switch (k) {
                    case 2 -> {
                        Point2D p = new Point2D(points[0].getX() + x, points[0].getY() - y, point.getColor());
                        if (!isListDrawContain(p)) listTmp.add(0, p);
                    }
                    case 3 -> {
                        Point2D p = new Point2D(points[0].getX() - x, points[0].getY() - y, point.getColor());
                        if (!isListDrawContain(p)) listTmp.add(p);
                    }
                    case 4 -> {
                        Point2D p = new Point2D(points[0].getX() - x, points[0].getY() + y, point.getColor());
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
        if (endPoint.getY() < points[0].getY()) {
            points[2].set(points[0].getX(), points[0].getY() + (points[0].getY() - endPoint.getY()));
        } else {
            points[2].set(points[0].getX(), endPoint.getY());
        }
    }

    @Override
    public String toString() {
        try {
            return String.format("Ellipse: (%d, %d) a=%d, b=%d",
                    points[0].getX(), points[0].getY(),
                    a,
                    b);
        } catch (Exception e) {
            return "";
        }

    }
}
