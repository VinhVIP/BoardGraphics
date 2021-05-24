package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

import java.util.List;

/**
 * Create by VinhIT
 * On 29/03/2021
 */

public class Pen extends Geometry {

    private Line line;
    private Point2D p1, p2;

    public Pen(DrawCanvas canvas) {
        super(canvas);
        line = new Line(canvas, DrawMode.DEFAULT, color);
    }

    public Pen(DrawCanvas canvas, DrawMode drawMode, int color) {
        super(canvas, drawMode, color);
        line = new Line(canvas, DrawMode.DEFAULT, color);
    }

    @Override
    public Geometry copy() {
        Pen g = new Pen(canvas, DrawMode.DEFAULT, color);

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        return g;
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        line.setColor(color);
    }

    @Override
    public void draw() {
        processDraw();
    }

    @Override
    public void processDraw() {
        if (listDraw.size() >= 1) {
            if (endPoint != null && !endPoint.isNear(listDraw.get(listDraw.size() - 1))) {
                endPoint.setColor(color);
                listDraw.add(endPoint);

                line.setStartPoint(listDraw.get(listDraw.size() - 2));
                line.setEndPoint(listDraw.get(listDraw.size() - 1));
                line.processDraw();
                line.drawNewPoints();

                for (int i = 1; i < line.getListDraw().size() - 1; i++) {
                    listDraw.add(listDraw.size() - 1, line.getListDraw().get(i));
                }
            }
        }
    }

    @Override
    public void showPointsCoordinate() {
    }

    @Override
    public void clearPointsCoordinate() {
    }

    @Override
    public void setStartPoint(Point2D startPoint) {
        super.setStartPoint(startPoint);
        if (startPoint != null) {
            startPoint.setColor(color);
            listDraw.add(startPoint);
            drawNewPoints();
            showPointsCoordinate();
        }
    }

    @Override
    public void setEndPoint(Point2D endPoint) {
        super.setEndPoint(endPoint);
    }

    public void setListDraw(List<Point2D> list) {
        listDraw.clear();
        listDraw.addAll(list);
    }

    @Override
    public void clearAll() {
        super.clearAll();
        line.clearAll();
    }

    @Override
    public Point2D getCenterPoint() {
        return null;
    }

    @Override
    public String toString() {
        return "Pen: " + listDraw.size() + " Points";
    }
}
