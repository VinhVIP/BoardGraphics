package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Circle;
import com.demo.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy {

    public static final String TAG = "Enemy";

    Random random = new Random();
    Circle wheel;
    private Line rim1, rim2;
    private Point2D initPoint;
    private int speedX, speedY;
    List<Point2D> listDraw;

    private DrawCanvas canvas;

    private int radius = 5;

    public Enemy(DrawCanvas canvas, Point2D initPoint) {
        this.canvas = canvas;
        this.initPoint = initPoint;
        listDraw = new ArrayList<>();


//        speedX = random.nextInt(5) + 1;
        speedX = 10;
        speedY = 0;

        wheel = new Circle(canvas, null, null, DrawMode.DEFAULT, 0xffff00, 0xff00ff, true, true);
        rim1 = new Line(canvas, DrawMode.DEFAULT, 0x000);
        rim2 = new Line(canvas, DrawMode.DEFAULT, 0x000);

        wheel.setPoints(new Point2D[]{initPoint, new Point2D(initPoint.getX() - radius, initPoint.getY())});
        rim1.setPoints(new Point2D[]{new Point2D(initPoint.getX(), initPoint.getY() - radius), new Point2D(initPoint.getX(), initPoint.getY() + radius)});
        rim2.setPoints(new Point2D[]{new Point2D(initPoint.getX() - radius, initPoint.getY()), new Point2D(initPoint.getX() + radius, initPoint.getY())});
    }

    public void run() {

        // TODO: enemy speed XY
//        speedX = random.nextInt(Math.min(Config.enemySpeedX, Config.enemySpeedX2)) + Math.abs(Config.enemySpeedX - Config.enemySpeedX2) + 1;
//        speedY = Config.enemySpeedY;

        wheel.move(-speedX, speedY);
        rim1.move(-speedX, speedY);
        rim2.move(-speedX, speedY);

        rim1.rotate(wheel.getCenterPoint(), Math.PI / Config.enemyRotate);
        rim2.rotate(rim2.getCenterPoint(), Math.PI / Config.enemyRotate);

        wheel.processDraw();
        rim1.processDraw();
        rim2.processDraw();

        wheel.fillColor();

        setListDraw();

    }

    public void setListDraw() {
        listDraw.clear();
        listDraw.addAll(wheel.getListDraw());
        listDraw.addAll(rim1.getListDraw());
        listDraw.addAll(rim2.getListDraw());
    }

    public List<Point2D> getListDraw() {
        return listDraw;
    }

    public void scale(double scale) {
        radius *= scale;

        wheel.setPoints(new Point2D[]{initPoint, new Point2D(initPoint.getX() - radius, initPoint.getY())});
        rim1.setPoints(new Point2D[]{new Point2D(initPoint.getX(), initPoint.getY() - radius), new Point2D(initPoint.getX(), initPoint.getY() + radius)});
        rim2.setPoints(new Point2D[]{new Point2D(initPoint.getX() - radius, initPoint.getY()), new Point2D(initPoint.getX() + radius, initPoint.getY())});
    }

    public void move(int moveX, int moveY) {
        speedX = moveX;
        speedY = moveY;
    }

    public Enemy reflectByOx() {
        Point2D p = initPoint.reflect(new Point2D(0, 0), new Point2D(1, 0));
        Enemy enemy = new Enemy(canvas, p);

        return enemy;
    }

    @Override
    public String toString() {
        return String.format("Enemy: (%d, %d) R=%d, Speed=%d, Angle=%.2f", wheel.getCenterPoint().getX(), wheel.getCenterPoint().getY(), radius, speedX, Math.PI / Config.enemyRotate);
    }

    public String getIdentify() {
        return TAG + hashCode();
    }
}
