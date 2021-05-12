package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

import java.awt.*;
import java.util.List;

/**
 * Create by VinhIT
 * On 28/03/2021
 */

public class Rectangle extends Geometry {

    private int totalPoints = 4;

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

    @Override
    public Geometry copy() {
        Rectangle g = new Rectangle(canvas);
        g.setStartPoint(new Point2D(startPoint));
        g.setEndPoint(new Point2D(endPoint));
        g.setDrawMode(drawMode);

        g.points = new Point2D[totalPoints];
        for(int i=0; i<totalPoints; i++)
            g.points[i] = new Point2D(points[i]);

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        return g;
    }


    private void initPoints() {
        points = new Point2D[totalPoints];
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

            swapList();

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

            for (int i = 0; i < lines.length; i++) {
                listDraw.addAll(lines[i].getListDraw());
            }

//            for (Point2D p : listDraw) {
//                if (p.getComputerX() < 0 || p.getComputerY() < 0 || p.getComputerX() >= DrawCanvas.rowSize || p.getComputerY() >= DrawCanvas.colSize)
//                    continue;
//                shapeBoard[p.getComputerX()][p.getComputerY()] = color;
//            }
//
//            fillColor();
//
//            for(int i=0; i<shapeBoard.length;i++){
//                for(int j=0; j<shapeBoard[0].length; j++){
//                    if(shapeBoard[i][j] == color){
//                        Point2D p = Point2D.fromComputerCoordinate(i,j);
//                        p.setColor(color);
//                        listDraw.add(p);
//                    }
//                }
//            }
//
//            clearOldPoints();
//            drawNewPoints();
        }
    }

    @Override
    public void showPointsCoordinate() {

    }

    private void fillColor() {
        int centerX = (points[0].getX() + points[2].getX()) / 2;
        int centerY = (points[0].getY() + points[2].getY()) / 2;

        fillPoint(centerX, centerY, color);
    }

    private void fillPoint(int x, int y, int color) {
        Point2D p = new Point2D(x, y, color);

        if (p.getComputerX() < 0 || p.getComputerY() < 0 || p.getComputerX() >= DrawCanvas.rowSize || p.getComputerY() >= DrawCanvas.colSize)
            return;

        if (shapeBoard[p.getComputerX()][p.getComputerY()] == color) return;
        else shapeBoard[p.getComputerX()][p.getComputerY()] = color;

        for (int i = 0; i < spillX.length; i++) {
            fillPoint(x + spillX[i], y + spillY[i], color);
        }
    }

    @Override
    public void setDrawMode(DrawMode drawMode) {
        for (Line line : lines) {
            line.setDrawMode(drawMode);
        }
    }

    @Override
    public List<Point2D> getListDraw() {

        return listDraw;
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
