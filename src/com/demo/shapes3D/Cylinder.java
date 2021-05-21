package com.demo.shapes3D;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Ellipse;
import com.demo.shape.EllipseDash;
import com.demo.shape.Geometry;
import com.demo.shape.Line;

import java.util.List;

/**
 * Create by VinhIT
 * On 18/05/2021
 */

public class Cylinder extends Geometry {

    private Ellipse ellipseTop;
    private Ellipse ellipseBottom;
    private Line[] lines = new Line[2];

    public Cylinder(DrawCanvas canvas) {
        super(canvas);
        init();
    }

    private void init() {
        is2DShape = false;
        initSizePoints(8);

        lines[0] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[1] = new Line(canvas, DrawMode.DEFAULT, color);

        ellipseTop = new Ellipse(canvas, DrawMode.DEFAULT, color);
        ellipseBottom = new EllipseDash(canvas, DrawMode.DEFAULT, color);
    }

    @Override
    public Geometry copy() {
        return null;
    }

    @Override
    public void processDraw() {
        swapList();

        lines[0].setPoints(new Point2D[]{points[1], points[5]});
        lines[1].setPoints(new Point2D[]{points[3], points[7]});

        ellipseTop.setPoints(new Point2D[]{points[0], points[3], points[2]});
        ellipseBottom.setPoints(new Point2D[]{points[4], points[7], points[6]});

        lines[0].processDraw();
        lines[1].processDraw();
        ellipseTop.processDraw();
        ellipseBottom.processDraw();

        addToListDraw(
                ellipseBottom.getListDraw(),
                ellipseTop.getListDraw(),
                lines[0].getListDraw(),
                lines[1].getListDraw());
    }


    @Override
    public void setEndPoint(Point2D endPoint) {
        super.setEndPoint(endPoint);
        points[0] = startPoint;
        points[7] = endPoint;

        int a = points[7].getX() - points[0].getX();
        int b = 2 * a / 5;


        int disY = points[0].getY() - points[7].getY();

        points[1] = new Point2D(points[0].getX() - a, points[0].getY());
        points[2] = new Point2D(points[0].getX(), points[0].getY() - b);
        points[3] = new Point2D(points[7].getX(), points[0].getY());

        points[4] = new Point2D(points[0].getX(), points[0].getY() - disY);
        points[5] = new Point2D(points[1].getX(), points[1].getY() - disY);
        points[6] = new Point2D(points[2].getX(), points[2].getY() - disY);

        if (endPoint.getY() > startPoint.getY()) {
            Point2D temp;
            for (int i = 0; i <= 3; i++) {
                temp = new Point2D(points[i]);
                points[i] = points[i + 4];
                points[i + 4] = temp;
            }
        }

    }

    @Override
    public Point2D getCenterPoint() {
        return null;
    }
}
