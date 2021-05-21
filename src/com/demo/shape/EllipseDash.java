package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by VinhIT
 * On 21/04/2021
 */

public class EllipseDash extends Ellipse {


    public EllipseDash(DrawCanvas canvas, DrawMode drawMode, int color) {
        super(canvas, drawMode, color);
    }

    public EllipseDash(DrawCanvas canvas) {
        super(canvas);
    }

    @Override
    public void processDraw() {
        swapList();
        process(0);
    }

    @Override
    protected void choosePoints(int n) {
        n--;
        List<Point2D> listTmp = new ArrayList<>();

        int s = 3 * n;
        while (s++ < 5 * n) {
            if (s % 5 >= 2) {
                listTmp.add(listDraw.get(s % (4 * n)));
            }
        }
        if (n > 0) listTmp.addAll(listDraw.subList(n + 1, 3 * n));
        listDraw.clear();
        listDraw.addAll(listTmp);
        listTmp.clear();
    }
}
