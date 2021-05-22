package com.demo.listeners;

import com.demo.models.Point2D;
import com.demo.models.Point3D;

/**
 * Create by VinhIT
 * On 07/05/2021
 */

public interface DialogListener {
    void onPointRotate(int x, int y);

    void onPointReflect(int x, int y);

    void onTwoPointReflect(int x1, int y1, int x2, int y2);

    void onScale(Point2D root, double scaleX, double scaleY);

    void onDrawRectangular(Point3D root, int dx, int dy, int dz);

    void onDrawCylinder(Point3D root, int dx, int dy, int dz);

    void onDrawCone(Point3D root, int dx, int dy, int dz);
}
