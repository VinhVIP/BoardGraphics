package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

/**
 * Create by Warriors Team
 * On 28/03/2021
 */

public class Rectangle extends Geometry {

    private int totalPoints = 4;

    // Khai báo 4 đoạn thẳng của hình chữ nhật
    private Line[] lines = new Line[4];

    public Rectangle(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color, int colorFill, boolean isFillColor, boolean is2DShape) {
        super(canvas, startPoint, endPoint, drawMode, color, colorFill, isFillColor, is2DShape);
        initPointsAndLines();
    }

    public Rectangle(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color) {
        super(canvas, startPoint, endPoint, drawMode, color);
        initPointsAndLines();
    }

    public Rectangle(DrawCanvas canvas, DrawMode drawMode, int color) {
        super(canvas, drawMode, color);
        initPointsAndLines();
    }

    public Rectangle(DrawCanvas canvas, DrawMode drawMode, boolean isFillColor) {
        super(canvas, drawMode, isFillColor);
        initPointsAndLines();
    }

    public Rectangle(DrawCanvas canvas) {
        super(canvas);
        initPointsAndLines();
    }


    @Override
    public Geometry copy() {
        Rectangle g = new Rectangle(canvas, new Point2D(startPoint), new Point2D(endPoint), drawMode, color, colorFill, isFillColor, is2DShape);

        for (int i = 0; i < totalPoints; i++)
            g.points[i] = new Point2D(points[i]);

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        return g;
    }


    private void initPointsAndLines() {
        initSizePoints(totalPoints);
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line(canvas, drawMode, color);
        }
    }


    @Override
    public void processDraw() {

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
    public void setEndPoint(Point2D endPoint) {
        super.setEndPoint(endPoint);

        Point2D pointB = new Point2D(endPoint.getX(), startPoint.getY());
        Point2D pointD = new Point2D(startPoint.getX(), endPoint.getY());

        points[0] = startPoint;
        points[1] = pointB;
        points[2] = endPoint;
        points[3] = pointD;
    }

    @Override
    public Point2D getCenterPoint() {
        return new Point2D((points[0].getX() + points[2].getX()) / 2, (points[0].getY() + points[2].getY()) / 2, points[0].getColor());
    }

    @Override
    public String toString() {
        try {
            return String.format("Rectangle: (%d, %d)  (%d, %d)  (%d, %d)  (%d, %d)",
                    points[0].getX(), points[0].getY(),
                    points[1].getX(), points[1].getY(),
                    points[2].getX(), points[2].getY(),
                    points[3].getX(), points[3].getY());
        } catch (Exception e) {
            return "";
        }
    }

}
