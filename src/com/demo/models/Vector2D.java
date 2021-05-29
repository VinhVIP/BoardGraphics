package com.demo.models;

/**
 * Create by Warriors Team
 * On 05/04/2021
 */

public class Vector2D {

    public static Vector2D oX = new Vector2D(1, 0);


    private int a, b;

    public Vector2D(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public Vector2D(Point2D pA, Point2D pB) {
        set(pA, pB);
    }

    public double angleRadian(Vector2D v) {
        return Math.acos((a * v.a + b * v.b) * 1.0 / (Math.sqrt(a * a + b * b) * Math.sqrt(v.a * v.a + v.b * v.b)));
    }

    public void set(Point2D pA, Point2D pB) {
        this.a = pB.getX() - pA.getX();
        this.b = pB.getY() - pA.getY();
    }

}
