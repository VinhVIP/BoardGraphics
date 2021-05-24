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

    private boolean[][] board = new boolean[DrawCanvas.rowSize][DrawCanvas.colSize];

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
        drawNewPoints();
    }

    @Override
    public void processDraw() {
        listDraw.clear();
        Point2D p;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j]) {
                    p = Point2D.fromComputerCoordinate(i, j);
                    p.setColor(color);
                    listDraw.add(p);
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
            p2 = startPoint;
            board[startPoint.getComputerX()][startPoint.getComputerY()] = true;
        }
    }

    @Override
    public void setEndPoint(Point2D endPoint) {
        super.setEndPoint(endPoint);
        if (endPoint != null) {
            p1 = p2;
            p2 = endPoint;
            board[p2.getComputerX()][p2.getComputerY()] = true;
            if (!p1.isNear(p2)) {
                line.setStartPoint(p1);
                line.setEndPoint(p2);
                line.processDraw();
                for (Point2D p : line.getListDraw()) {
                    board[p.getComputerX()][p.getComputerY()] = true;
                }
            }
        }
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
