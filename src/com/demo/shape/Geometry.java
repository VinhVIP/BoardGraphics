package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by VinhIT
 * On 28/03/2021
 */

public abstract class Geometry {

    protected DrawCanvas canvas;
    protected List<Point2D> listDraw = new ArrayList<>(), listClear = new ArrayList<>();
    protected Point2D startPoint, endPoint;

    protected int color;

    protected DrawMode drawMode;


    public Geometry(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode) {
        this.canvas = canvas;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.drawMode = drawMode;

        this.color = DrawCanvas.currentColor;
    }

    public Geometry(DrawCanvas canvas, Point2D startPoint, Point2D endPoint) {
        this(canvas, startPoint, endPoint, DrawMode.DEFAULT);
    }

    public Geometry(DrawCanvas canvas) {
        this(canvas, DrawMode.DEFAULT);
    }

    public Geometry(DrawCanvas canvas, DrawMode drawMode) {
        this(canvas, null, null, drawMode);
    }

    public abstract void setupDraw();

    public abstract void showPointsCoordinate();

    /*
     * Lọc ra những điểm cần xóa và xóa nó
     */
    protected final void clearOldPoints() {
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
    protected final void drawNewPoints() {
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

    public void clearListDraw(){
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

    public void translate(int translateX, int translateY) {
        startPoint.set(startPoint.getX() + translateX, startPoint.getY() + translateY);
        endPoint.set(endPoint.getX() + translateX, endPoint.getY() + translateY);
    }
}
