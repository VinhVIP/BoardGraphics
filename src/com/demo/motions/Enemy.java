package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Circle;
import com.demo.shape.Line;
import com.demo.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Enemy {
    private Circle wheel;
    private Line rim1, rim2;
    private Point2D initPoint;
    List<Point2D> listDraw;

    private DrawCanvas canvas;

    public Enemy(DrawCanvas canvas, Point2D initPoint) {
        this.canvas = canvas;
        this.initPoint = initPoint;
        listDraw = new ArrayList<>();

        wheel = new Circle(canvas, null, null, DrawMode.DEFAULT, 0xffff00, 0xff00ff, true, true);
        rim1 = new Line(canvas, DrawMode.DEFAULT, 0x000);
        rim2 = new Line(canvas, DrawMode.DEFAULT, 0x000);

        wheel.setPoints(new Point2D[]{initPoint, new Point2D(initPoint.getX()+5, initPoint.getY())});
        rim1.setPoints(new Point2D[]{new Point2D(initPoint.getX(), initPoint.getY()-5), new Point2D(initPoint.getX(), initPoint.getY()+5)});
        rim2.setPoints(new Point2D[]{new Point2D(initPoint.getX()-5, initPoint.getY()), new Point2D(initPoint.getX()+5, initPoint.getY())});
    }

    public void run() {
        wheel.move(-1, 0);
        rim1.move(-1, 0);
        rim2.move(-1, 0);

        rim1.rotate(wheel.getCenterPoint(), Math.PI / 12);
        rim2.rotate(rim2.getCenterPoint(), Math.PI / 12);

        wheel.processDraw();
        rim1.processDraw();
        rim2.processDraw();

        wheel.fillColor();

        setListDraw();

    }
    public void setListDraw(){
        listDraw.clear();
        listDraw.addAll(wheel.getListDraw());
        listDraw.addAll(rim1.getListDraw());
        listDraw.addAll(rim2.getListDraw());
    }
    public List<Point2D> getListDraw(){
        return listDraw;
    }

}
