package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Explosion {
    Random random = new Random();
    Rectangle exploseArea;
    List<Point2D> listDraw;
    DrawCanvas canvas;
    List<Explose> exploses = new ArrayList<>();
    private Point2D initPoint;
    int duration, exploseRange, exploseColor, exploseFillColor;

    public Explosion(DrawCanvas canvas, Point2D initPoint, int exploseRange, int exploseColor, int exploseFillColor, int duration) {
        this.canvas = canvas;
        this.initPoint = initPoint;
        this.exploseRange = exploseRange;
        this.exploseColor = exploseColor;
        this.exploseFillColor = exploseFillColor;
        this.duration = duration;

        listDraw = new ArrayList<>();
        exploseArea = new Rectangle(canvas, null, null, DrawMode.DEFAULT, 0x0000, 0x0000, true, true);
        exploseArea.setPoints(new Point2D[]{new Point2D(initPoint.getX() - exploseRange, initPoint.getY() - exploseRange),
                new Point2D(initPoint.getX() + exploseRange, initPoint.getY() - exploseRange),
                new Point2D(initPoint.getX() + exploseRange, initPoint.getY() + exploseRange),
                new Point2D(initPoint.getX() - exploseRange, initPoint.getY() + exploseRange)});
        exploseArea.processDraw();
        exploseArea.fillColor();
        for (Point2D p : exploseArea.getListDraw()) {
            exploses.add(new Explose(canvas, null, null, DrawMode.DEFAULT, exploseColor, exploseFillColor, true, true));
            exploses.get(exploses.size() - 1).setPoints(new Point2D[]{p, new Point2D(p.getX() + 1, p.getY())});
        }
    }

    public void run() {
        if (duration > 0) {
            List<Integer> indexs = new ArrayList<>();
            for (int i = 0; i < exploses.size(); i++) {
                if (!exploses.get(i).isExplose()) {
                    indexs.add(i);
                } else {
                    exploses.get(i).boom();
                }
            }
            if (!indexs.isEmpty()) {
                int index = indexs.get(random.nextInt(indexs.size()));
                exploses.get(index).boom();
                exploses.get(index).setExplose();

            }
            setListDraw();
            duration--;
        } else {
            listDraw.clear();
        }

    }

    public void setListDraw() {
        listDraw.clear();
        for (int i = 0; i < exploses.size(); i++) {
            if (exploses.get(i).isExplose()) {
                listDraw.addAll(exploses.get(i).getListDraw());
            }
        }
    }

    public List<Point2D> getListDraw() {
        return listDraw;
    }
}
