package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.models.Point2D;
import com.demo.shape.Geometry;

/**
 * Create by VinhIT
 * On 07/04/2021
 */

public class SinglePoint extends Geometry {

    public SinglePoint(DrawCanvas canvas) {
        super(canvas);
    }

    @Override
    public Geometry copy() {
        return null;
    }

    @Override
    public void draw() {
        if(startPoint != null){
            listDraw.add(startPoint);
            drawNewPoints();
            showPointsCoordinate();
        }
    }

    @Override
    public void processDraw() {

    }

    @Override
    public void showPointsCoordinate() {
    }

    @Override
    public void setEndPoint(Point2D endPoint) {
        setStartPoint(endPoint);
    }

    @Override
    public Point2D getCenterPoint() {
        return null;
    }
}
