package com.demo.shapes3D;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Geometry;
import com.demo.shape.Line;

/**
 * Create by VinhIT
 * On 17/05/2021
 */

public class Rectangular extends Geometry {

    private int totalPoints = 8;

    private Line[] lines;

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
        // Đây là hình 3D
        is2DShape = false;

        initSizePoints(totalPoints);
        lines = new Line[12];

        lines[0] = new Line(canvas, color, DrawMode.DASH);
        lines[1] = new Line(canvas, color, DrawMode.DASH);
        lines[2] = new Line(canvas, color, DrawMode.DASH);

        lines[3] = new Line(canvas, color, DrawMode.DEFAULT);
        lines[4] = new Line(canvas, color, DrawMode.DEFAULT);
        lines[5] = new Line(canvas, color, DrawMode.DEFAULT);
        lines[6] = new Line(canvas, color, DrawMode.DEFAULT);
        lines[7] = new Line(canvas, color, DrawMode.DEFAULT);
        lines[8] = new Line(canvas, color, DrawMode.DEFAULT);
        lines[9] = new Line(canvas, color, DrawMode.DEFAULT);
        lines[10] = new Line(canvas, color, DrawMode.DEFAULT);
        lines[11] = new Line(canvas, color, DrawMode.DEFAULT);

    }

    @Override
    public Geometry copy() {
        return null;
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
}
