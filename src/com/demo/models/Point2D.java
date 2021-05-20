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

    public Point2D(Point2D p) {
        this(p.x, p.y, p.color);
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

    /*
     * Xoay điểm point xung quanh điểm root 1 góc angleRad
     */
    public Point2D rotate(Point2D root, double angleRad) {
        Point2D p = new Point2D(this);

        // Di chuyển root và point về gốc tọa độ
        p.set(p.x - root.x, p.y - root.y);

        // Xoay point
        p = p.rotate(angleRad);

        // Di chuyển point về hệ tọa độ cũ
        p.set(p.x + root.x, p.y + root.y);

        return p;
    }

    /*
     * Lấy điểm đối xứng với this qua điểm root
     */
    public Point2D reflect(Point2D root) {
        Point2D p = new Point2D(this);
        // Di chuyển root về gốc tọa độ
        p.set(p.x - root.x, p.y - root.y);

        // Lấy đối xứng qua gốc O
        p.set(-p.x, -p.y);

        // Di chuyển trở về
        p.set(p.x + root.x, p.y + root.y);

        return p;
    }

    /*
     * Lấy điểm đối xứng qua đường thẳng đi qua 2 điểm p1, p2
     */
    public Point2D reflect(Point2D p1, Point2D p2) {
        Point2D p = new Point2D(this);

        int x = p1.x;
        int y = p1.y;

        // Dịch chuyển p1 về gốc O, các điểm khác di chuyển theo tương ứng
        p1.set(0, 0);
        p2.set(p2.x - x, p2.y - y);
        p.set(p.x - x, p.y - y);

        // Xác định góc xoay và xoay đường thẳng p1,p2 trùng với trục Ox
        Vector2D v12 = new Vector2D(p1, p2);
        double angle = v12.angleRadian(Vector2D.oX);

        if (p2.y > 0) angle = -angle;

        p2 = p2.rotate(angle);
        p = p.rotate(angle);

        System.out.println("p2.y=" + p2.y + " & angle=" + angle);

        // Lấy đối xứng p qua Ox
        p.set(p.x, -p.y);

        // Xoay về như cữ
        p = p.rotate(-angle);
        p2 = p2.rotate(-angle);

        // Di chuyển về lại
        p.set(p.x + x, p.y + y);
        p1.set(x, y);
        p2.set(p2.x + x, p2.y + y);


        return p;
    }

    /*
     * Thu phóng điểm this với tâm phóng là gốc O
     */
    public Point2D scale(double scaleX, double scaleY) {
        Point2D p = new Point2D(this);
        p.set((int) (p.x * scaleX), (int) (p.y * scaleY));
        return p;
    }

    /*
     * Thu phóng điểm this với tâm phóng là root
     */
    public Point2D scale(Point2D root, double scaleX, double scaleY) {
        Point2D p = new Point2D(this);
        int distanceX = p.x - root.x;
        int distanceY = p.y - root.y;
        p.set(root.x + (int) (distanceX * scaleX), root.y + (int) (distanceY * scaleY));
        return p;
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

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int distance(Point2D p) {
        return (int) Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
    }

    public boolean isNear(Point2D p) {
        int[] xx = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] yy = {1, 1, 1, 0, -1, -1, -1, 0};
        for (int i = 0; i < xx.length; i++) {
            if (p.x + xx[i] == x && p.y + yy[i] == y) return true;
        }
        return false;
    }

    public boolean insideScreen() {
        if (getComputerX() < 0 || getComputerX() >= DrawCanvas.rowSize || getComputerY() < 0 || getComputerY() >= DrawCanvas.colSize)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Point2D{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                '}';
    }

    @Override
    public int hashCode() {
        return x ^ y ^ color;
    }
}
