package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Create by VinhIT
 * On 28/03/2021
 */

public abstract class Geometry {

    protected DrawCanvas canvas;
    protected List<Point2D> listDraw = new ArrayList<>(), listClear = new ArrayList<>();
    protected Point2D startPoint, endPoint;

    protected Point2D[] points;

    protected int defaultColorFill = 0xffffff;

    protected int color, colorFill = defaultColorFill;
    protected int[][] board = DrawCanvas.newDefaultBoard();

    protected DrawMode drawMode;

    int[] spillX = new int[]{0, 1, 0, -1};
    int[] spillY = new int[]{-1, 0, 1, 0};

    public Geometry(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, int color, DrawMode drawMode) {
        this.canvas = canvas;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.color = color;
        this.drawMode = drawMode;
    }

    public Geometry(DrawCanvas canvas, int color, DrawMode drawMode) {
        this(canvas, null, null, color, drawMode);
    }

    public Geometry(DrawCanvas canvas) {
        this(canvas, null, null, DrawCanvas.currentColor, DrawMode.DEFAULT);
    }

    public Geometry(DrawCanvas canvas, DrawMode drawMode) {
        this(canvas, null, null, DrawCanvas.currentColor, drawMode);
    }

    public abstract Geometry copy();

    protected void initSizePoints(int size) {
        points = new Point2D[size];
    }

    public void setupDraw() {
        processDraw();
        clearOldPoints();
        drawNewPoints();

        fillColor();
    }

    public abstract void processDraw();

    protected void fillColor() {
        if (listDraw.isEmpty()) return;
        int[][] tempBoardOutline = DrawCanvas.newDefaultBoard();
        int[][] tempBoard = DrawCanvas.newDefaultBoard();

        int outlineColor = color;

        for (Point2D p : listDraw) {
            if (p.insideScreen()) tempBoardOutline[p.getComputerX()][p.getComputerY()] = outlineColor;
        }

        Queue<Point2D> queue = new ArrayDeque<>();
        Queue<Point2D> tempQueue = new ArrayDeque<>();
        Queue<Point2D> tempQueue2 = new ArrayDeque<>();

        int bgColor = defaultColorFill;

        Point2D pt = getCenterPoint();

        if (pt.insideScreen() && tempBoardOutline[pt.getComputerX()][pt.getComputerY()] == defaultColorFill) {
            tempQueue.add(pt);
            tempBoard[pt.getComputerX()][pt.getComputerY()] = colorFill;
        }

        while (!tempQueue.isEmpty()) {
            Point2D point = tempQueue.remove();

            for (int i = 0; i < spillX.length; i++) {
                Point2D p = new Point2D(point.getX() + spillX[i], point.getY() + spillY[i], colorFill);

                if (p.insideScreen()) {
                    if (tempBoard[p.getComputerX()][p.getComputerY()] == bgColor && tempBoardOutline[p.getComputerX()][p.getComputerY()] != outlineColor) {
                        tempBoard[p.getComputerX()][p.getComputerY()] = colorFill;
                        tempQueue2.add(new Point2D(p));
                        queue.add(new Point2D(p));
                    }
                }

            }
            if (tempQueue.isEmpty()) {
                while (!tempQueue2.isEmpty()) {
                    tempQueue.add(new Point2D(tempQueue2.remove()));
                }
            }
        }

        Point2D point2D;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != tempBoard[i][j]) {
                    board[i][j] = tempBoard[i][j];
                    point2D = Point2D.fromComputerCoordinate(i, j);
                    point2D.setColor(board[i][j]);
                    if (point2D.insideScreen()) canvas.putPixel(point2D);
                }
            }
        }

    }

    /*
     * Lọc ra những điểm cần xóa và xóa nó
     */
    public final void clearOldPoints() {
        List<Point2D> list = new ArrayList<>();

        for (Point2D pc : listClear) {
            boolean del = true;
            for (Point2D pd : listDraw) {
                if (pc.isSamePoint(pd)) {
                    del = false;
                    break;
                }
            }
            if (del) list.add(pc);
        }
        canvas.clearDraw(list);
    }

    /*
     * Vẽ những điểm mới
     */
    public final void drawNewPoints() {
        canvas.applyDraw(listDraw);
    }


    /*
     * Đổ listDraw cho listClear
     */
    protected void swapList() {
        listClear.clear();
        listClear.addAll(listDraw);
        listDraw.clear();
    }

    /*
     * Hiển thị tọa độ 2 điểm start và end
     */
    public void showPointsCoordinate() {
        for (Point2D p : points) {
            canvas.drawPointsCoordinate(p);
        }
    }

    public void clearPointsCoordinate() {
        for (Point2D p : points) {
            canvas.reDrawPoints(p.getComputerX() - 2, p.getComputerY() - 4, p.getComputerX() + 10, p.getComputerY());
        }
    }

    public void setStartPoint(Point2D startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(Point2D endPoint) {
        this.endPoint = endPoint;
    }

    public Point2D getStartPoint() {
        return startPoint;
    }

    public Point2D getEndPoint() {
        return endPoint;
    }

    /*
     * Xóa toàn bộ dữ liệu
     */
    public void clearAll() {
        listDraw.clear();
        listClear.clear();
        startPoint = endPoint = null;
    }

    public final boolean isListDrawContain(Point2D point) {
        for (Point2D p : listDraw) {
            if (p.equals(point)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isShowPoint(int index) {
        switch (drawMode) {  //  chế độ vẽ
            case DEFAULT -> {           // Nét liền
                return true;
            }
            case DOT -> {               // Nét chấm
                return (index % 2) == 0;
            }
            case DASH -> {              // Nét gạch
                return (index % 5) < 3;
            }
            case DASH_DOT -> {          // Nét gạch chấm
                return (index % 6) < 3 || (index % 6) == 4;
            }
            case DASH_DOT_DOT -> {      // Nét gạch 2 chấm
                return (index % 12 < 4) || (index % 12) == 6 || (index % 12) == 9;
            }
        }
        return true;
    }

    public DrawMode getDrawMode() {
        return drawMode;
    }

    public List<Point2D> getListDraw() {
        return listDraw;
    }

    public void clearListDraw() {
        listDraw.clear();
    }

    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColorFill() {
        return colorFill;
    }

    public void setColorFill(int colorFill) {
        this.colorFill = colorFill;
    }

    public void translate(int translateX, int translateY) {
        startPoint.set(startPoint.getX() + translateX, startPoint.getY() + translateY);
        endPoint.set(endPoint.getX() + translateX, endPoint.getY() + translateY);
    }

    public Point2D[] getPoints() {
        return points;
    }

    public void setPoints(Point2D[] points) {
        this.points = points;
    }

    public abstract Point2D getCenterPoint();

    /*
     * Xoay đoạn thẳng hiện tại quanh tâm của đoạn thẳng
     */
    public void rotate(Point2D root, double angleRad) {
        for (int i = 0; i < points.length; i++) {
            points[i] = points[i].rotate(root, angleRad);
        }
    }

    /*
     * Di chuyển đoạn thẳng 1 đoạn mx, my
     */
    public void move(int mx, int my) {
        for (Point2D p : points) {
            p.set(p.getX() + mx, p.getY() + my);
        }
    }
}
