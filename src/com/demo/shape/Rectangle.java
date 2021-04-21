package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

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
    }

    public Rectangle(DrawCanvas canvas) {
        super(canvas);
        init4Lines(canvas);
    }

    public Rectangle(DrawCanvas canvas, DrawMode drawMode) {
        super(canvas, drawMode);
        init4Lines(canvas);
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
//            swapList();
//
//            drawRectangle(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
//
//            clearOldPoints();
//            drawNewPoints();


            // Sử dụng 4 đoạn thẳng thì chạy lâu hơn

            Point2D pointB = new Point2D(endPoint.getX(), startPoint.getY());
            Point2D pointD = new Point2D(startPoint.getX(), endPoint.getY());

            lines[0].setStartPoint(startPoint);
            lines[0].setEndPoint(pointB);

            lines[1].setStartPoint(pointB);
            lines[1].setEndPoint(endPoint);

            lines[2].setStartPoint(endPoint);
            lines[2].setEndPoint(pointD);

            lines[3].setStartPoint(pointD);
            lines[3].setEndPoint(startPoint);

            for(int i=0; i<4; i++){
                lines[i].swapList();
                lines[i].drawLine();
                lines[i].clearOldPoints();
            }

            for (int i=0; i<4; i++) lines[i].drawNewPoints();

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

    private void drawRectangle(int x1, int y1, int x2, int y2) {
        int xUnit = 1, yUnit = 1;
        if (x2 < x1) xUnit = -xUnit;
        if (y2 < y1) yUnit = -yUnit;

        int x = x1, y = y1;
        Point2D pt;

        pt = new Point2D(x2, y2, DrawCanvas.currentColor);
        listDraw.add(pt);

        while (x != x2) {
            pt = new Point2D(x, y1, DrawCanvas.currentColor);
            listDraw.add(pt);
            pt = new Point2D(x, y2, DrawCanvas.currentColor);
            listDraw.add(pt);
            x += xUnit;
        }
        while (y != y2) {
            pt = new Point2D(x1, y, DrawCanvas.currentColor);
            listDraw.add(pt);
            pt = new Point2D(x2, y, DrawCanvas.currentColor);
            listDraw.add(pt);
            y += yUnit;
        }
    }

}
