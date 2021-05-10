package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

import java.util.List;

/**
 * Create by VinhIT
 * On 28/03/2021
 */

public class Rectangle extends Geometry {

    // Khai báo 4 đoạn thẳng của hình chữ nhật
    private Line[] lines = new Line[4];

    public Rectangle(DrawCanvas canvas, Point2D startPoint2D, Point2D endPoint2D) {
        super(canvas, startPoint2D, endPoint2D);
        init4Lines(canvas);
        initPoints();
    }

    public Rectangle(DrawCanvas canvas) {
        super(canvas);
        init4Lines(canvas);
        initPoints();
    }

    public Rectangle(DrawCanvas canvas, DrawMode drawMode) {
        super(canvas, drawMode);
        init4Lines(canvas);
        initPoints();
    }

    private void initPoints(){
        points = new Point2D[4];
    }

    /*
     * Khởi tạo 1 đường thẳng
     */
    private void init4Lines(DrawCanvas canvas) {
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line(canvas);
            lines[i].setDrawMode(drawMode);
        }
    }

    @Override
    public void setupDraw() {
        if (startPoint != null && endPoint != null) {

            for (int i = 0; i < 4; i++) {
                lines[i].setStartPoint(points[i % 4]);
                lines[i].setEndPoint(points[(i + 1) % 4]);
            }

            for (int i = 0; i < 4; i++) {
                lines[i].swapList();
                lines[i].drawLine();
                lines[i].clearOldPoints();
            }

            for (int i = 0; i < 4; i++) lines[i].drawNewPoints();

        }
    }

    @Override
    public void showPointsCoordinate() {

    }

    @Override
    public void setDrawMode(DrawMode drawMode) {
        for (Line line : lines) {
            line.setDrawMode(drawMode);
        }
    }

    @Override
    public List<Point2D> getListDraw() {
        listDraw.clear();
        for (int i = 0; i < lines.length; i++) {
            listDraw.addAll(lines[i].getListDraw());
        }
        return listDraw;
    }


//    public void rotate(Point2D root, double angle) {
//        for (int i = 0; i < 4; i++) {
//            points[i] = points[i].rotate(root, points[i], angle);
//        }
//    }


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
