package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Circle;
import com.demo.shape.Ellipse;
import com.demo.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class Sun {

    public static final String TAG = "Sun";

    Circle sun, sunTemp;
    Ellipse cycle;
    private List<Line> sunshines;
    List<Point2D> listDraw;
    private boolean isZoom = true, isVisible = true;
    private int countZoom = 2;
    private double scale = 1, dv = 0.1;
    private int mx, my;

    private DrawCanvas canvas;

    public Sun(DrawCanvas canvas, Ellipse cycle) {
        this.canvas = canvas;
        this.cycle = cycle;


        sun = new Circle(canvas, null, null, DrawMode.DEFAULT, 0xffcc00, 0xffff00, true, true);
        sun.setPoints(new Point2D[]{new Point2D(0, 39), new Point2D(10, 39)});
        sunshines = new ArrayList<>();
        listDraw = new ArrayList<>();

        Line sunshine1 = new Line(canvas, null, null, DrawMode.DEFAULT, 0xffff00, 0xffcc00, true, true);
        Line sunshine2 = new Line(canvas, null, null, DrawMode.DEFAULT, 0xffff00, 0xffcc00, true, true);
        Line sunshine3;
        Line sunshine4;

        sunshine1.setPoints(new Point2D[]{new Point2D(15, 39), new Point2D(30, 39)});
        sunshine2.setPoints(new Point2D[]{new Point2D(10, 29), new Point2D(15, 24)});
        sunshine3 = (Line) sunshine1.copy();
        sunshine4 = (Line) sunshine2.copy();
        sunshine3.rotate(sun.getCenterPoint(), -Math.PI / 2);
        sunshine4.rotate(sun.getCenterPoint(), -Math.PI / 2);
        sunshines.add(sunshine1);
        sunshines.add(sunshine2);
        sunshines.add(sunshine3);
        sunshines.add(sunshine4);
    }

    public void run() {

        mx = cycle.getListDraw().get(Motion.cycleIndex).getX() - sun.getCenterPoint().getX();
        my = cycle.getListDraw().get(Motion.cycleIndex).getY() - sun.getCenterPoint().getY();
        for (Line l : sunshines) {
            l.rotate(sun.getCenterPoint(), -Math.PI / 12);
            l.move(mx, my);
        }
        sun.move(mx, my);

        sunTemp = (Circle) sun.copy();
        scale += dv;
        if (scale > 1.5 || scale < 0.8) dv = -dv;
        sunTemp.scale(sun.getCenterPoint(), scale, scale);

        sun.processDraw();
        sunTemp.processDraw();
        for (Line l : sunshines) {
            l.processDraw();
        }
        sunTemp.fillColor();

        setListDraw();
    }

    public void setListDraw() {
        listDraw.clear();
        listDraw.addAll(sunTemp.getListDraw());
        for (Line l : sunshines) {
            listDraw.addAll(l.getListDraw());
            Line refLine = (Line) l.copy();
            refLine.reflect(sun.getCenterPoint());
            refLine.processDraw();
            listDraw.addAll(refLine.getListDraw());
        }
    }

    public List<Point2D> getListDraw() {
        return listDraw;
    }

    public Sun reflectByOy() {
        Sun reflectSun = new Sun(canvas, cycle);
        reflectSun.listDraw = new ArrayList<>();
        for (Point2D p : listDraw) {
            Point2D reflectPoint = new Point2D(p);
            reflectPoint = reflectPoint.reflect(new Point2D(0, 0), new Point2D(0, 1));
            reflectSun.listDraw.add(reflectPoint);
        }
        return reflectSun;
    }

    @Override
    public String toString() {
        return String.format("Sun: (%d, %d) R = %d", sun.getCenterPoint().getX(), sun.getCenterPoint().getY(), (int) (sun.getRadius() * scale));
    }

    public String getIdentify() {
        return TAG + hashCode();
    }
}
