package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.models.Point2D;

/**
 * Create by VinhIT
 * On 29/03/2021
 */

public class Pen extends Geometry {

    private Line line;

    public Pen(DrawCanvas canvas) {
        super(canvas);
        line = new Line(canvas);
    }

    @Override
    public Geometry copy() {
        return null;
    }

    @Override
    public void setupDraw() {
        // TODO: Thuật toán vẽ PEN lỗi, rảnh fix sau

        if (listDraw.size() >= 1) {
            if (endPoint != null && !endPoint.isSamePoint(listDraw.get(listDraw.size() - 1))) {
                listDraw.add(endPoint);

                line.setStartPoint(listDraw.get(listDraw.size() - 2));
                line.setEndPoint(listDraw.get(listDraw.size() - 1));
                line.drawLine();
                line.drawNewPoints();
            }
        }
    }

    @Override
    public void showPointsCoordinate() {
//        canvas.addPointsToDrawCoord(startPoint);
//        Graphics g = canvas.getGraphics();
//        g.setColor(Color.BLACK);
//
//        if (startPoint != null) {
//            g.drawString(String.format("(%d, %d)", startPoint.getX(), startPoint.getY()), startPoint.getComputerX() * 5 - 5, startPoint.getComputerY() * 5 - 5);
//        }
    }

    @Override
    public void setStartPoint(Point2D startPoint) {
        super.setStartPoint(startPoint);
        if (startPoint != null) {
            listDraw.add(startPoint);
            drawNewPoints();
            showPointsCoordinate();
        }
    }

    @Override
    public void clearAll() {
        super.clearAll();
        line.clearAll();
    }

}
