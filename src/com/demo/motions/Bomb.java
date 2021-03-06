package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Circle;
import com.demo.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class Bomb {

    public static final String TAG = "Bomb";

    Circle bomb;
    Line line1, line2, line1Temp, line2Temp;
    double scale1 = 1, dv1 = 0.5;
    double scale2 = 2, dv2 = 0.5;
    List<Point2D> listDraw;
    DrawCanvas canvas;
    private Point2D initPoint;

    private int radius = 3;

    public Bomb(DrawCanvas canvas, Point2D initPoint) {
        this.canvas = canvas;
        this.initPoint = initPoint;

        listDraw = new ArrayList<>();

        bomb = new Circle(canvas, null, null, DrawMode.DEFAULT, 0x000000, 0x000000, true, true);
        line1 = new Line(canvas, DrawMode.DEFAULT, 0xffffff);
        line2 = new Line(canvas, DrawMode.DEFAULT, 0xffffff);

        bomb.setPoints(new Point2D[]{initPoint, new Point2D(initPoint.getX() + radius, initPoint.getY())});
        line1.setPoints(new Point2D[]{new Point2D(initPoint.getX() - (radius + 1), initPoint.getY() + 1), new Point2D(initPoint.getX() - 2 * (radius + 1), initPoint.getY() + 1)});
        line2.setPoints(new Point2D[]{new Point2D(initPoint.getX() - (radius + 1), initPoint.getY() - 2), new Point2D(initPoint.getX() - 2 * radius, initPoint.getY() - 2)});
    }

    public void run() {
        bomb.move(5, 0);
        line1.move(5, 0);
        line2.move(5, 0);

        line1Temp = (Line) line1.copy();
        scale1 += dv1;
        if (scale1 > 2.0 || scale1 < 0.5) dv1 = -dv1;
        line1Temp.scale(line1Temp.getPoints()[0], scale1, scale1);
        line2Temp = (Line) line2.copy();
        scale2 += dv2;
        if (scale2 > 2.0 || scale2 < 0.5) dv2 = -dv2;
        line2Temp.scale(line2Temp.getPoints()[0], scale2, scale2);

        bomb.processDraw();
        bomb.fillColor();
        line1Temp.processDraw();
        line2Temp.processDraw();

        setListDraw();
    }

    public void setListDraw() {
        listDraw.clear();
        listDraw.addAll(bomb.getListDraw());
        listDraw.addAll(line1Temp.getListDraw());
        listDraw.addAll(line2Temp.getListDraw());
    }

    public List<Point2D> getListDraw() {
        return listDraw;
    }

    public void scale(double scale) {
        radius *= scale;

        bomb.setPoints(new Point2D[]{initPoint, new Point2D(initPoint.getX() + radius, initPoint.getY())});
        line1.setPoints(new Point2D[]{new Point2D(initPoint.getX() - (radius + 1), initPoint.getY() + 1), new Point2D(initPoint.getX() - 2 * (radius + 1), initPoint.getY() + 1)});
        line2.setPoints(new Point2D[]{new Point2D(initPoint.getX() - (radius + 1), initPoint.getY() - 2), new Point2D(initPoint.getX() - 2 * radius, initPoint.getY() - 2)});
    }

    public Bomb reflectByOx() {
        Point2D p = initPoint.reflect(new Point2D(0, 0), new Point2D(1, 0));
        Bomb bomb = new Bomb(canvas, p);

        return bomb;
    }

    @Override
    public String toString() {
        return String.format("Bomb: (%d, %d) R=%d", bomb.getCenterPoint().getX(), bomb.getCenterPoint().getY(), radius);
    }

    public String getIdentify() {
        return TAG + hashCode();
    }
}