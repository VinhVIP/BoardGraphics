package com.demo.listeners;

import com.demo.models.Point2D;

/**
 * Create by VinhIT
 * On 07/05/2021
 */

public interface DialogListener {
    void onPointRotate(int x, int y);
    void onPointReflect(int x, int y);
    void onTwoPointReflect(int x1, int y1, int x2, int y2);
    void onScale(Point2D root, double scaleX, double scaleY);
}
