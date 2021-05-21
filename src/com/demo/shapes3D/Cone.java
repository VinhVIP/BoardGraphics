package com.demo.shapes3D;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Ellipse;
import com.demo.shape.EllipseDash;
import com.demo.shape.Geometry;
import com.demo.shape.Line;

/**
 * Create by VinhIT
 * On 21/05/2021
 */

public class Cone extends Geometry {

    private Line[] lines = new Line[2];
    private Ellipse ellipse;

    public Cone(DrawCanvas canvas) {
        super(canvas);
        init();
    }

    private void init() {
        is2DShape = false;
        initSizePoints(5);

        lines[0] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[1] = new Line(canvas, DrawMode.DEFAULT, color);

    }

    @Override
    public Geometry copy() {
        return null;
    }

    @Override
    public void processDraw() {
        swapList();
        lines[0].setPoints(new Point2D[]{points[0], points[2]});
        lines[1].setPoints(new Point2D[]{points[0], points[4]});

        if (points[1].getY() < points[0].getY()) {
            ellipse = new EllipseDash(canvas, DrawMode.DEFAULT, color);
        } else {
            ellipse = new Ellipse(canvas, DrawMode.DEFAULT, color);
        }

        ellipse.setPoints(new Point2D[]{points[1], points[4], points[3]});

        lines[0].processDraw();
        lines[1].processDraw();
        ellipse.processDraw();

        addToListDraw(ellipse.getListDraw(), lines[0].getListDraw(), lines[1].getListDraw());
    }

    @Override
    public void setEndPoint(Point2D endPoint) {
        super.setEndPoint(endPoint);

        points[0] = startPoint;
        points[1] = new Point2D(points[0].getX(), endPoint.getY());

        int a = endPoint.getX() - points[0].getX();
        int h = Math.abs(points[0].getY() - points[1].getY());
        int b = Math.min(h / 5, 15);

        points[2] = new Point2D(points[1].getX() - a, points[1].getY());
        points[3] = new Point2D(points[1].getX(), points[1].getY() - b);
        points[4] = new Point2D(points[1].getX() + a, points[1].getY());
    }

    @Override
    public Point2D getCenterPoint() {
        return null;
    }
}
