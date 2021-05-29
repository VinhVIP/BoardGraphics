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
 * Create by Warriors Team
 * On 18/05/2021
 */

public class Cylinder extends Geometry {

    private Ellipse ellipseTop;
    private Ellipse ellipseBottom;
    private Line[] lines = new Line[5];

    private int totalPoints = 8;
    private Point3D[] point3Ds;

    private int dx, dy, dz;

    public Cylinder(DrawCanvas canvas) {
        super(canvas);
        init();
    }

    public Cylinder(DrawCanvas canvas, DrawMode drawMode, int color) {
        super(canvas, drawMode, color);
        init();
    }

    private void init() {
        is2DShape = false;
        initSizePoints(totalPoints);
        point3Ds = new Point3D[totalPoints];

        lines[0] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[1] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[2] = new Line(canvas, DrawMode.DASH, color);
        lines[3] = new Line(canvas, DrawMode.DASH, color);
        lines[4] = new Line(canvas, DrawMode.DEFAULT, color);

        ellipseTop = new Ellipse(canvas, DrawMode.DEFAULT, color);
        ellipseBottom = new EllipseDash(canvas, DrawMode.DEFAULT, color);
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        for (int i = 0; i < lines.length; i++)
            lines[i].setColor(color);
        ellipseTop.setColor(color);
        ellipseBottom.setColor(color);
    }

    @Override
    public Geometry copy() {
        Cylinder g = new Cylinder(canvas, drawMode, color);
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

        lines[0].setPoints(new Point2D[]{points[1], points[5]});
        lines[1].setPoints(new Point2D[]{points[3], points[7]});

        lines[2].setPoints(new Point2D[]{points[0], points[4]});
        lines[3].setPoints(new Point2D[]{points[4], points[7]});

        lines[4].setPoints(new Point2D[]{points[0], points[3]});

        ellipseTop.setPoints(new Point2D[]{points[0], points[3], points[2]});
        ellipseBottom.setPoints(new Point2D[]{points[4], points[7], points[6]});

        for (int i = 0; i < lines.length; i++)
            lines[i].processDraw();

        ellipseTop.processDraw();
        ellipseBottom.processDraw();

        addToListDraw(
                ellipseBottom.getListDraw(),
                ellipseTop.getListDraw(),
                lines[0].getListDraw(),
                lines[1].getListDraw(),
                lines[2].getListDraw(),
                lines[3].getListDraw(),
                lines[4].getListDraw());
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

        point3Ds[4] = root;
        point3Ds[5] = new Point3D(point3Ds[4].getX() - dx, point3Ds[4].getY(), point3Ds[4].getZ());
        point3Ds[6] = new Point3D(point3Ds[4].getX(), point3Ds[4].getY(), point3Ds[4].getZ() - dz);
        point3Ds[7] = new Point3D(point3Ds[4].getX() + dx, point3Ds[4].getY(), point3Ds[4].getZ());

        point3Ds[0] = new Point3D(point3Ds[4].getX(), point3Ds[4].getY() + dy, point3Ds[4].getZ());
        point3Ds[1] = new Point3D(point3Ds[5].getX(), point3Ds[5].getY() + dy, point3Ds[5].getZ());
        point3Ds[2] = new Point3D(point3Ds[6].getX(), point3Ds[6].getY() + dy, point3Ds[6].getZ());
        point3Ds[3] = new Point3D(point3Ds[7].getX(), point3Ds[7].getY() + dy, point3Ds[7].getZ());

        for (int i = 0; i < totalPoints; i++) {
            points[i] = point3Ds[i].to2DPoint();
        }
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

    @Override
    public String toString() {
        if (point3Ds[4] != null)
            return String.format("Cylinder: %s R=%d ; H=%d", point3Ds[4].toString(), dx, dy);
        return "Cylinder: preview";
    }
}
