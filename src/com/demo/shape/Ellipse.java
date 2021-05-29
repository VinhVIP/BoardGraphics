package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.models.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Warriors Team
 * On 11/04/2021
 */

public class Ellipse extends Geometry {

    private int radiusA, radiusB;

    private int totalPoints = 3;

    public Ellipse(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color, int colorFill, boolean isFillColor, boolean is2DShape) {
        super(canvas, startPoint, endPoint, drawMode, color, colorFill, isFillColor, is2DShape);
        initSizePoints(totalPoints);
    }

    public Ellipse(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color) {
        super(canvas, startPoint, endPoint, drawMode, color);
        initSizePoints(totalPoints);
    }

    public Ellipse(DrawCanvas canvas, DrawMode drawMode, int color) {
        super(canvas, drawMode, color);
        initSizePoints(totalPoints);
    }

    public Ellipse(DrawCanvas canvas, DrawMode drawMode, boolean isFillColor) {
        super(canvas, drawMode, isFillColor);
        initSizePoints(totalPoints);
    }

    public Ellipse(DrawCanvas canvas) {
        super(canvas);
        initSizePoints(totalPoints);
    }


    @Override
    public Geometry copy() {
        Ellipse g = new Ellipse(canvas, new Point2D(startPoint), new Point2D(endPoint), drawMode, color, colorFill, isFillColor, is2DShape);

        for (int i = 0; i < totalPoints; i++)
            g.points[i] = new Point2D(points[i]);

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        return g;
    }

    @Override
    public void processDraw() {
        // Xác định góc giữa O'B và trục Ox
        Vector2D vOB = new Vector2D(points[0], points[1]);
        double angle = vOB.angleRadian(Vector2D.oX);
        if (Double.isNaN(angle)) return;

        swapList();
        process(angle);
        if (drawMode == DrawMode.DEFAULT) connect();
    }

    /*
           --A(2)--
         -         -
        |    O'(0)  |B(1)
         -         -
           -------
    */
    protected void process(double angle) {

        if (points[1].getY() > points[0].getY()) angle = -angle;

        // Xoay O'B trùng với trục Ox
        // O'A xoay theo tương ứng
        if (angle != 0) {
            points[1] = points[1].rotate(points[0], angle);
            points[2] = points[2].rotate(points[0], angle);
        }

        radiusA = Math.max(1, points[0].distance(points[1]));
        radiusB = Math.max(1, points[0].distance(points[2]));

        midEllipse(points[0].getX(), points[0].getY(), radiusA, radiusB, color);

        // Xoay trở về vị trí cũ
        if (angle != 0) {
            angle = -angle;

            points[1] = points[1].rotate(points[0], angle);
            points[2] = points[2].rotate(points[0], angle);
        }

        genPoints();

        if (angle != 0) {
            for (int i = 0; i < listDraw.size(); i++) {
                Point2D p = listDraw.get(i);
                p = p.rotate(points[0], angle);
                listDraw.set(i, p);
            }
        }
    }

    private void connect() {
        List<Point2D> list = new ArrayList<>();
        Point2D p1, p2;
        Line line = new Line(canvas, DrawMode.DEFAULT, color);

        for (int i = 0; i < listDraw.size(); i++) {
            p1 = listDraw.get(i);
            p2 = listDraw.get((i + 1) % listDraw.size());
            if (p1.distance(p2) > 1) {
                line.setStartPoint(new Point2D(p1));
                line.setEndPoint(new Point2D(p2));
                line.processDraw();
                for (int j = 1; j < line.getListDraw().size() - 1; j++) {
                    list.add(new Point2D(line.getListDraw().get(j)));
                }
            }
        }

        listDraw.addAll(list);
        list.clear();
    }

    protected void genPoints() {
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

        choosePoints(n);
    }

    protected void choosePoints(int n) {
        List<Point2D> listTmp = new ArrayList<>();
        for (int i = 0; i < listDraw.size(); i++) {
            if (isShowPoint(i)) listTmp.add(listDraw.get(i));
        }

        listDraw.clear();
        listDraw.addAll(listTmp);
        listTmp.clear();
    }

    protected void plot(int xc, int yc, int x, int y, int color) {
        Point2D p = new Point2D(xc + x, yc + y, color);
        if (!isListDrawContain(p)) listDraw.add(p);
    }

    void midEllipse(float xc, float yc, float rx, float ry, int color) {

        float dx, dy, d1, d2, x, y;
        x = 0;
        y = ry;

        d1 = (ry * ry) - (rx * rx * ry) +
                (0.25f * rx * rx);
        dx = 2 * ry * ry * x;
        dy = 2 * rx * rx * y;

        while (dx < dy) {

            plot((int) xc, (int) yc, (int) x, (int) y, color);

            if (d1 < 0) {
                x++;
                dx = dx + (2 * ry * ry);
                d1 = d1 + dx + (ry * ry);
            } else {
                x++;
                y--;
                dx = dx + (2 * ry * ry);
                dy = dy - (2 * rx * rx);
                d1 = d1 + dx - dy + (ry * ry);
            }
        }

        d2 = ((ry * ry) * ((x + 0.5f) * (x + 0.5f)))
                + ((rx * rx) * ((y - 1) * (y - 1)))
                - (rx * rx * ry * ry);

        while (y >= 0) {

            plot((int) xc, (int) yc, (int) x, (int) y, color);

            y--;
            if (d2 > 0) {
                dy = dy - (2 * rx * rx);
                d2 = d2 + (rx * rx) - dy;
            } else {
                x++;
                dx = dx + (2 * ry * ry);
                dy = dy - (2 * rx * rx);
                d2 = d2 + dx - dy + (rx * rx);
            }
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
    public Point2D getCenterPoint() {
        return new Point2D(points[0]);
    }

    @Override
    public String toString() {
        try {
            return String.format("Ellipse: (%d, %d) a=%d, b=%d",
                    points[0].getX(), points[0].getY(),
                    radiusA,
                    radiusB);
        } catch (Exception e) {
            return "";
        }

    }
}
