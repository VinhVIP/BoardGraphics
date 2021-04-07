package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.models.Point2D;
import com.demo.shape.Geometry;

/**
 * Create by VinhIT
 * On 07/04/2021
 */

public class SinglePoint extends Geometry {

    public SinglePoint(DrawCanvas canvas, Point2D startPoint, Point2D endPoint) {
        super(canvas, startPoint, endPoint);
    }

    public SinglePoint(DrawCanvas canvas) {
        super(canvas);
    }

    @Override
    public void setupDraw() {
        if(startPoint != null){
            listDraw.add(startPoint);
            drawNewPoints();
            showPointsCoordinate();
        }
    }

    @Override
    public void showPointsCoordinate() {
        canvas.addPointsToDrawCoord(startPoint);
    }

    @Override
    public void setEndPoint(Point2D endPoint) {
        setStartPoint(endPoint);
    }
}
