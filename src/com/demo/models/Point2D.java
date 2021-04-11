package com.demo.models;

import com.demo.DrawCanvas;

/**
 * Create by VinhIT
 * On 27/03/2021
 */

public class Point2D {

    // Tọa độ Descartes của điểm
    private int x, y;

    // Màu vẽ của điểm
    private int color;


    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getComputerX() {
        return x + DrawCanvas.rowSize / 2;
    }

    public int getComputerY() {
        return DrawCanvas.colSize / 2 - y;
    }

    public Point2D getComputerCoordinate() {
        return new Point2D(x + DrawCanvas.rowSize / 2, DrawCanvas.colSize / 2 - y);
    }


    /*
     * xm, ym là tọa độ trên màn hình máy tính
     * Ta cần chuyển nó về hệ tọa độ Descartes để sử dụng
     */
    public static Point2D fromComputerCoordinate(int xm, int ym) {
        return new Point2D(-DrawCanvas.rowSize / 2 + xm, DrawCanvas.colSize / 2 - ym);
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    /*
     * Kiểm tra 2 điểm có giống nhau hay không
     */
    public boolean isSamePoint(Point2D point) {
        return x == point.x && y == point.y && color == point.color;
    }

    public Point2D translate(int translateX, int translateY) {
        return new Point2D(x + translateX, y + translateY, color);
    }

    /*
     * angleRad là góc xoay, tính bằng radian
     */
    public Point2D rotate(double angleRad) {
        return new Point2D((int) Math.round(Math.cos(angleRad) * x - Math.sin(angleRad) * y),
                (int) Math.round(Math.sin(angleRad) * x + Math.cos(angleRad) * y),
                color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point2D point2D = (Point2D) o;
        return x == point2D.x &&
                y == point2D.y &&
                color == point2D.color;
    }


}
