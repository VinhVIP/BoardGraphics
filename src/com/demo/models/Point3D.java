package com.demo.models;

/**
 * Create by VinhIT
 * On 17/05/2021
 */

public class Point3D {
    private int x, y, z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point2D to2DPoint() {
        int xP = (int) (x - z * Math.sqrt(2) / 2);
        int yP = (int) (y - z * Math.sqrt(2) / 2);
        return new Point2D(xP, yP);
    }

    public static Point2D to2DPoint(int px, int py, int pz) {
        Point3D point3D = new Point3D(px, py, pz);
        return point3D.to2DPoint();
    }

    public static Point2D to2DPoint(Point3D p) {
        Point3D point3D = new Point3D(p.x, p.y, p.z);
        return point3D.to2DPoint();
    }

}
