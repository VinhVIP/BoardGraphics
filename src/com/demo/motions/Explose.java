package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Circle;

public class Explose extends Circle {
    private Circle exTemp;
    private double scale = 1, dv = 0.5;
    private boolean isExplose = false;

    //<editor-fold defaultstate="collapsed" desc="Construtor">
    public Explose(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color, int colorFill, boolean isFillColor, boolean is2DShape) {
        super(canvas, startPoint, endPoint, drawMode, color, colorFill, isFillColor, is2DShape);
    }

    public Explose(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, DrawMode drawMode, int color) {
        super(canvas, startPoint, endPoint, drawMode, color);
    }

    public Explose(DrawCanvas canvas, DrawMode drawMode, int color) {
        super(canvas, drawMode, color);
    }

    public Explose(DrawCanvas canvas, DrawMode drawMode, boolean isFillColor) {
        super(canvas, drawMode, isFillColor);
    }

    public Explose(DrawCanvas canvas) {
        super(canvas);
    }
    //</editor-fold>

    public void boom() {
        exTemp = (Circle) this.copy();
        scale += dv;
        exTemp.scale(this.getCenterPoint(), scale, scale);
        exTemp.processDraw();
        exTemp.fillColor();
        this.listDraw = exTemp.getListDraw();

        if (scale > 4) {
            scale = 1;
            isExplose = false;
        }
    }

    public boolean isExplose() {
        return isExplose;
    }

    public void setExplose() {
        this.isExplose = !isExplose;
    }
}
