package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

import java.util.List;

/**
 * Create by VinhIT
 * On 10/05/2021
 */

public class Triangle extends Geometry{

    private int totalPoints = 3;

    private Line[] lines = new Line[3];

    public Triangle(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, int color, DrawMode drawMode) {
        super(canvas, startPoint, endPoint, color, drawMode);
        initPointsAndLines();
    }

    public Triangle(DrawCanvas canvas, int color, DrawMode drawMode) {
        super(canvas, color, drawMode);
        initPointsAndLines();
    }

    public Triangle(DrawCanvas canvas) {
        super(canvas);
        initPointsAndLines();
    }

    public Triangle(DrawCanvas canvas, DrawMode drawMode) {
        super(canvas, drawMode);
        initPointsAndLines();
    }


    @Override
    public Geometry copy() {
        Triangle g = new Triangle(canvas, new Point2D(startPoint), new Point2D(endPoint), color, drawMode);

        for(int i=0; i<totalPoints; i++)
            g.points[i] = new Point2D(points[i]);

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        return g;
    }

    private void initPointsAndLines() {
        initSizePoints(totalPoints);
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line(canvas, color, drawMode);
        }
    }

    @Override
    public void setupDraw() {
        if(endPoint != null){
            for (int i = 0; i < lines.length; i++) {
                lines[i].setStartPoint(points[i % lines.length]);
                lines[i].setEndPoint(points[(i + 1) % lines.length]);
            }

            for (int i = 0; i < lines.length; i++) {
                lines[i].swapList();
                lines[i].drawLine();
                lines[i].clearOldPoints();
            }

            for (int i = 0; i < lines.length; i++) lines[i].drawNewPoints();
        }
    }

    @Override
    public void setDrawMode(DrawMode drawMode) {
        for (Line line : lines) {
            line.setDrawMode(drawMode);
        }
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        for (int i=0; i<lines.length; i++)
            lines[i].setColor(color);
    }

    @Override
    public List<Point2D> getListDraw() {
        listDraw.clear();
        for (int i = 0; i < lines.length; i++) {
            listDraw.addAll(lines[i].getListDraw());
        }
        return listDraw;
    }

    @Override
    public void setStartPoint(Point2D startPoint) {
        super.setStartPoint(startPoint);
        points[0] = startPoint;
    }

    @Override
    public void setEndPoint(Point2D endPoint) {
        super.setEndPoint(endPoint);
        points[1] = endPoint;
        points[2] = new Point2D(endPoint);
        points[2].setX(points[0].getX()-(points[1].getX()-points[0].getX()));
    }

    @Override
    public void setPoints(Point2D[] points) {
        super.setPoints(points);
        startPoint = points[0];
        endPoint = points[1];
    }

    @Override
    public String toString() {
        try {
            return String.format("Triangle: (%d, %d)  (%d, %d)  (%d, %d)",
                    points[0].getX(), points[0].getY(),
                    points[1].getX(), points[1].getY(),
                    points[2].getX(), points[2].getY());
        } catch (Exception e) {
            return "";
        }
    }

}
