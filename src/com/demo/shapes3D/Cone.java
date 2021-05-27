package com.demo.shapes3D;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.models.Point3D;
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

    private int totalPoints = 5;
    private Point3D[] point3Ds;

    private int dx, dy, dz;

    public Cone(DrawCanvas canvas) {
        super(canvas);
        init();
    }

    public Cone(DrawCanvas canvas, DrawMode drawMode, int color) {
        super(canvas, drawMode, color);
        init();
    }

    private void init() {
        is2DShape = false;
        initSizePoints(totalPoints);
        point3Ds = new Point3D[totalPoints];

        lines[0] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[1] = new Line(canvas, DrawMode.DEFAULT, color);

    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        lines[0].setColor(color);
        lines[1].setColor(color);
    }

    @Override
    public Geometry copy() {
        Cone g = new Cone(canvas, drawMode, color);
        g.is2DShape = false;

        for (int i = 0; i < totalPoints; i++) {
            g.points[i] = new Point2D(points[i]);
            if (point3Ds[i] != null)
                g.point3Ds[i] = new Point3D(point3Ds[i]);
        }

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        g.dx = dx;
        g.dy = dy;
        g.dz = dz;

        return g;
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
    public void showPointsCoordinate() {
        for (Point3D p : point3Ds) {
            canvas.drawPointsCoordinate(p);
        }
    }

    public void set(Point3D root, int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        dz *= Math.sqrt(2) / 2.0;

        point3Ds[1] = root;
        point3Ds[2] = new Point3D(point3Ds[1].getX() - dx, point3Ds[1].getY(), point3Ds[1].getZ());
        point3Ds[3] = new Point3D(point3Ds[1].getX(), point3Ds[1].getY(), point3Ds[1].getZ() - dz);
        point3Ds[4] = new Point3D(point3Ds[1].getX() + dx, point3Ds[1].getY(), point3Ds[1].getZ());
        point3Ds[0] = new Point3D(point3Ds[1].getX(), point3Ds[1].getY() + dy, point3Ds[1].getZ());

        for (int i = 0; i < totalPoints; i++) {
            points[i] = point3Ds[i].to2DPoint();
        }
    }

    @Override
    public void setEndPoint(Point2D endPoint) {
        super.setEndPoint(endPoint);

        points[0] = startPoint;
        points[1] = new Point2D(points[0].getX(), endPoint.getY());

        int a = endPoint.getX() - points[0].getX();
        int h = Math.abs(points[0].getY() - points[1].getY());
        int b = Math.min(h / 5, 10);

        points[2] = new Point2D(points[1].getX() - a, points[1].getY());
        points[3] = new Point2D(points[1].getX(), points[1].getY() - b);
        points[4] = new Point2D(points[1].getX() + a, points[1].getY());
    }

    @Override
    public Point2D getCenterPoint() {
        return null;
    }

    @Override
    public String toString() {
        if (point3Ds[1] != null)
            return String.format("Cone: %s dX=%d ; dY=%d ; dZ=%d", point3Ds[1].toString(), dx, dy, dz);
        return "Rectangular: preview";
    }
}
