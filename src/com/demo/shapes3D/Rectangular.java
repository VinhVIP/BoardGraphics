package com.demo.shapes3D;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.models.Point3D;
import com.demo.shape.Geometry;
import com.demo.shape.Line;

/**
 * Create by Warriors Team
 * On 17/05/2021
 */

public class Rectangular extends Geometry {

    private int totalPoints = 8;
    private int dx, dy, dz;
    private Point3D[] point3Ds;

    private Line[] lines;

    public Rectangular(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color, int colorFill, boolean isFillColor, boolean is2DShape) {
        super(canvas, startPoint, endPoint, drawMode, color, colorFill, isFillColor, is2DShape);
        init();
    }

    public Rectangular(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color) {
        super(canvas, startPoint, endPoint, drawMode, color);
        init();
    }

    public Rectangular(DrawCanvas canvas, DrawMode drawMode, int color) {
        super(canvas, drawMode, color);
        init();
    }

    public Rectangular(DrawCanvas canvas, DrawMode drawMode, boolean isFillColor) {
        super(canvas, drawMode, isFillColor);
        init();
    }

    public Rectangular(DrawCanvas canvas) {
        super(canvas);
        init();
    }


    private void init() {
        // Đây là hình 3D
        is2DShape = false;

        initSizePoints(totalPoints);
        point3Ds = new Point3D[totalPoints];

        lines = new Line[12];

        lines[0] = new Line(canvas, DrawMode.DASH, color);
        lines[1] = new Line(canvas, DrawMode.DASH, color);
        lines[2] = new Line(canvas, DrawMode.DASH, color);

        lines[3] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[4] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[5] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[6] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[7] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[8] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[9] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[10] = new Line(canvas, DrawMode.DEFAULT, color);
        lines[11] = new Line(canvas, DrawMode.DEFAULT, color);
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        for (int i = 0; i < lines.length; i++) {
            lines[i].setColor(color);
        }
    }

    @Override
    public Geometry copy() {
        Rectangular g = new Rectangular(canvas, drawMode, color);
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

        lines[0].setPoints(new Point2D[]{points[5], points[4]});
        lines[1].setPoints(new Point2D[]{points[5], points[6]});
        lines[2].setPoints(new Point2D[]{points[5], points[1]});

        lines[3].setPoints(new Point2D[]{points[0], points[1]});
        lines[4].setPoints(new Point2D[]{points[1], points[2]});
        lines[5].setPoints(new Point2D[]{points[2], points[3]});
        lines[6].setPoints(new Point2D[]{points[0], points[3]});
        lines[7].setPoints(new Point2D[]{points[0], points[4]});
        lines[8].setPoints(new Point2D[]{points[4], points[7]});
        lines[9].setPoints(new Point2D[]{points[3], points[7]});
        lines[10].setPoints(new Point2D[]{points[2], points[6]});
        lines[11].setPoints(new Point2D[]{points[6], points[7]});


        for (int i = 0; i < lines.length; i++) {
            lines[i].processDraw();
        }

        for (int i = 0; i < lines.length; i++) {
            listDraw.addAll(lines[i].getListDraw());
        }
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

        point3Ds[5] = root;
        point3Ds[6] = new Point3D(point3Ds[5].getX() + dx, point3Ds[5].getY(), point3Ds[5].getZ());
        point3Ds[4] = new Point3D(point3Ds[5].getX(), point3Ds[5].getY() + dy, point3Ds[5].getZ());
        point3Ds[7] = new Point3D(point3Ds[5].getX() + dx, point3Ds[5].getY() + dy, point3Ds[5].getZ());

        point3Ds[1] = new Point3D(point3Ds[5].getX(), point3Ds[5].getY(), point3Ds[5].getZ() + dz);
        point3Ds[0] = new Point3D(point3Ds[5].getX(), point3Ds[5].getY() + dy, point3Ds[5].getZ() + dz);
        point3Ds[2] = new Point3D(point3Ds[5].getX() + dx, point3Ds[5].getY(), point3Ds[5].getZ() + dz);
        point3Ds[3] = new Point3D(point3Ds[5].getX() + dx, point3Ds[5].getY() + dy, point3Ds[5].getZ() + dz);

        for (int i = 0; i < totalPoints; i++) {
            points[i] = point3Ds[i].to2DPoint();
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
        points[0] = new Point2D(Math.min(startPoint.getX(), endPoint.getX()), Math.max(startPoint.getY(), endPoint.getY()));
        points[7] = new Point2D(Math.max(startPoint.getX(), endPoint.getX()), Math.min(startPoint.getY(), endPoint.getY()));

//        cd = Math.abs(points[7].getX() - points[0].getX());
        int cc = Math.abs(points[7].getY() - points[0].getY());

        points[3] = new Point2D(points[7].getX(), points[0].getY());
        points[4] = new Point2D(points[0].getX(), points[7].getY());

        points[1] = new Point2D(points[0].getX() + cc / 3, points[0].getY() + cc / 3);
        points[2] = new Point2D(points[7].getX() + cc / 3, points[1].getY());
        points[5] = new Point2D(points[1].getX(), points[1].getY() - cc);
        points[6] = new Point2D(points[2].getX(), points[5].getY());
    }

    @Override
    public Point2D getCenterPoint() {
        return null;
    }

    @Override
    public String toString() {
        if (point3Ds[5] != null)
            return String.format("Rectangular: %s dX=%d ; dY=%d ; dZ=%d", point3Ds[5].toString(), dx, dy, dz);
        return "Rectangular: preview";
    }

}
