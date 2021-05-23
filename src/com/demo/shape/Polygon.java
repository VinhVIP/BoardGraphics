package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

/**
 * Create by VinhIT
 * On 22/05/2021
 */

public class Polygon extends Geometry{

    private int totalPoints;

    private Line[] lines;

    public Polygon(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color, int colorFill, boolean isFillColor, boolean is2DShape) {
        super(canvas, startPoint, endPoint, drawMode, color, colorFill, isFillColor, is2DShape);
    }

    public Polygon(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color) {
        super(canvas, startPoint, endPoint, drawMode, color);
    }

    public Polygon(DrawCanvas canvas, DrawMode drawMode, int color) {
        super(canvas, drawMode, color);
    }

    public Polygon(DrawCanvas canvas, DrawMode drawMode, boolean isFillColor) {
        super(canvas, drawMode, isFillColor);
    }

    public Polygon(DrawCanvas canvas) {
        super(canvas);
    }


    @Override
    public Geometry copy() {
        Polygon g = new Polygon(canvas, null, null, drawMode, color, colorFill, isFillColor, is2DShape);

        g.points = new Point2D[points.length];

        for (int i = 0; i < totalPoints; i++)
            g.points[i] = new Point2D(points[i]);

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        return g;
    }

    @Override
    public void setPoints(Point2D[] points) {
        super.setPoints(points);

        totalPoints = points.length;
        lines = new Line[totalPoints];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line(canvas, drawMode, color);
        }
    }

    @Override
    public void processDraw() {
        if(totalPoints > 0) {
            swapList();

            for (int i = 0; i < lines.length; i++) {
                lines[i].setStartPoint(points[i % lines.length]);
                lines[i].setEndPoint(points[(i + 1) % lines.length]);
            }

            for (int i = 0; i < lines.length; i++) {
                lines[i].processDraw();
            }

            for (int i = 0; i < lines.length; i++) {
                listDraw.addAll(lines[i].getListDraw());
            }
        }
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        for (int i = 0; i < lines.length; i++)
            lines[i].setColor(color);
    }

    @Override
    public void setDrawMode(DrawMode drawMode) {
        for (Line line : lines) {
            line.setDrawMode(drawMode);
        }
    }

    @Override
    public Point2D getCenterPoint() {
        return new Point2D((points[0].getX() + points[2].getX()) / 2, (points[0].getY() + points[2].getY()) / 2, points[0].getColor());
    }

    @Override
    public String toString() {
        String s = "Polygon: ";
        for(Point2D p:points){
            s += String.format("(%d, %d) ", p.getX(), p.getY());
        }
        return s;
    }
}
