package com.demo.shapes3D;

import com.demo.DrawCanvas;
import com.demo.models.Point2D;
import com.demo.shape.Geometry;

/**
 * Create by VinhIT
 * On 18/05/2021
 */

public class Cylinder extends Geometry {
    
    public Cylinder(DrawCanvas canvas) {
        super(canvas);
    }

    @Override
    public Geometry copy() {
        return null;
    }

    @Override
    public void processDraw() {

    }

    @Override
    public Point2D getCenterPoint() {
        return null;
    }
}
