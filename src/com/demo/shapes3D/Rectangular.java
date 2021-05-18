package com.demo.shapes3D;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.models.Point3D;
import com.demo.shape.Geometry;
import com.demo.shape.Line;

/**
 * Create by VinhIT
 * On 17/05/2021
 */

public class Rectangular extends Geometry {

    private int totalPoints = 8;

    private Line[] lines;
    private int x, y, z, cd, cr, cc;

    public Rectangular(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, int color, DrawMode drawMode) {
        super(canvas, startPoint, endPoint, color, drawMode);
        init();
    }

    public Rectangular(DrawCanvas canvas, int color, DrawMode drawMode) {
        super(canvas, color, drawMode);
        init();
    }

    public Rectangular(DrawCanvas canvas) {
        super(canvas);
        init();
    }

    public Rectangular(DrawCanvas canvas, DrawMode drawMode) {
        super(canvas, drawMode);
        init();
    }

    private void init() {
        initSizePoints(totalPoints);
        lines = new Line[12];

        x = y = z = 50;
        cd = cr = cc = 15;

        points[0] = Point3D.to2DPoint(x, y, z);
        points[1] = Point3D.to2DPoint(x + cd, y, z);
        points[2] = Point3D.to2DPoint(x + cd, y, z + cr);
        points[3] = Point3D.to2DPoint(x, y, z + cr);
        points[4] = Point3D.to2DPoint(x, y + cc, z);
        points[5] = Point3D.to2DPoint(x + cd, y + cc, z);
        points[6] = Point3D.to2DPoint(x + cd, y + cc, z + cr);
        points[7] = Point3D.to2DPoint(x, y + cc, z + cr);

        lines[0] = new Line(canvas, points[0], points[1], color, DrawMode.DOT);
        lines[1] = new Line(canvas, points[0], points[3], color, DrawMode.DOT);
        lines[2] = new Line(canvas, points[0], points[4], color, DrawMode.DOT);

        lines[3] = new Line(canvas, points[1], points[2], color, DrawMode.DEFAULT);
        lines[4] = new Line(canvas, points[2], points[3], color, DrawMode.DEFAULT);
        lines[5] = new Line(canvas, points[4], points[5], color, DrawMode.DEFAULT);
        lines[6] = new Line(canvas, points[5], points[6], color, DrawMode.DEFAULT);
        lines[7] = new Line(canvas, points[6], points[7], color, DrawMode.DEFAULT);
        lines[8] = new Line(canvas, points[4], points[7], color, DrawMode.DEFAULT);
        lines[9] = new Line(canvas, points[1], points[5], color, DrawMode.DEFAULT);
        lines[10] = new Line(canvas, points[2], points[6], color, DrawMode.DEFAULT);
        lines[11] = new Line(canvas, points[3], points[7], color, DrawMode.DEFAULT);

    }

    @Override
    public Geometry copy() {
        return null;
    }

    @Override
    public void setupDraw() {
        processDraw();
        for (int i = 0; i < lines.length; i++) lines[i].clearOldPoints();
        for (int i = 0; i < lines.length; i++) lines[i].drawNewPoints();
    }

    @Override
    public void processDraw() {
        swapList();

//        for (int i = 0; i < lines.length; i++) {
//            lines[i].setStartPoint(points[i % lines.length]);
//            lines[i].setEndPoint(points[(i + 1) % lines.length]);
//        }

        for (int i = 0; i < lines.length; i++) {
            lines[i].processDraw();
        }

        for (int i = 0; i < lines.length; i++) {
            listDraw.addAll(lines[i].getListDraw());
        }
    }

    @Override
    public Point2D getCenterPoint() {
        return null;
    }
}
