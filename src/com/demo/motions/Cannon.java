package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Circle;
import com.demo.shape.Ellipse;

import java.util.ArrayList;
import java.util.List;

public class Cannon {
    List<Circle> bombs;
    Circle bomb;
    Ellipse direction;
    List<Point2D> listDraw;
    DrawCanvas canvas;

    public Cannon(DrawCanvas canvas) {
        this.canvas = canvas;

        listDraw = new ArrayList<>();
        bombs = new ArrayList<>();

        direction = new Ellipse(canvas, null, null, DrawMode.DEFAULT, 0x999966, 0x999966, true, true);
        bomb = new Circle(canvas, null, null, DrawMode.DEFAULT, 0x999966, 0x999966, true, true);
        bomb.setPoints(new Point2D[]{new Point2D(30, -30), new Point2D(50, -30), new Point2D(50, -50), new Point2D(30, -50)});
    }

    public void run(Point2D aimPoint) {
        if (aimPoint != null) {

        } else {

        }
    }

    public void setListDraw() {

    }

    public List<Point2D> getListDraw() {
        return listDraw;
    }

}