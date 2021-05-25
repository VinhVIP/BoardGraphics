package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Ellipse;
import com.demo.shape.Rectangle;

import java.util.Arrays;
import java.util.List;

public class Motion {
    public Sun sun;
    private Explosion explosion;
    private Moon moon;
    private Ellipse cycle;
    private Rectangle land, sky;
    public static int cycleIndex;
    int[] colorSky = new int[]{0x99ccff, 0x80bfff, 0x66b3ff, 0x4da6ff, 0x3399ff, 0x1a8cff, 0x0080ff, 0x0073e6, 0x0066cc, 0x0059b3, 0x004d99, 0x004080, 0x003366};
    int[] colorSkyMove = new int[521];

    private DrawCanvas canvas;

    public Motion(DrawCanvas canvas) {
        this.canvas = canvas;

        Arrays.fill(colorSkyMove, 0, colorSkyMove.length, 0x003366);

        land = new Rectangle(canvas, null, null, DrawMode.DEFAULT, 0x996633, 0x996633, true, true);
        sky = new Rectangle(canvas, null, null, DrawMode.DEFAULT, 0x99ccff, 0x99ccff, true, true);
        cycle = new Ellipse(canvas, null, null, DrawMode.DEFAULT, 0x000000, 0x000000, true, true);
        land.setPoints(new Point2D[]{new Point2D(-100, 0), new Point2D(100, 0), new Point2D(100, -60), new Point2D(-100, -60)});
        sky.setPoints(new Point2D[]{new Point2D(-100, 60), new Point2D(100, 60), new Point2D(100, 0), new Point2D(-100, 0)});
        cycle.setPoints(new Point2D[]{new Point2D(0, 0), new Point2D(130, 0), new Point2D(0, 50)});
        cycle.processDraw();
        cycleIndex = cycle.getListDraw().size() - 1;

        sun = new Sun(canvas, cycle);
        moon = new Moon(canvas, sun.sun, cycle);

        explosion = new Explosion(canvas);

        land.processDraw();
        land.fillColor();

        sky.processDraw();
        sky.fillColor();
        setColorSky();
    }


    public void run() {
        int[][] b = DrawCanvas.newDefaultBoard();

        moon.run();
        sun.run();

        explosion.run();

        if (cycleIndex < cycle.getListDraw().size() - 1 && cycle.getListDraw().get(cycleIndex).getX() != cycle.getListDraw().get(cycleIndex + 1).getX()) {
            skyMove();
        }


        addToBoard(b,
                sky.getListDraw(),
                sun.getListDraw(),
                moon.getListDraw(),
                land.getListDraw(),
                explosion.getListDraw()
        );
        canvas.applyBoard(b);

        cycleIndex--;
        if (cycleIndex < 0) cycleIndex = cycle.getListDraw().size() - 1;
    }

    private void addToBoard(int[][] b, List<Point2D>... lists) {
        for (List<Point2D> list : lists) {
            for (Point2D p : list) {
                if (p.insideScreen()) {
                    b[p.getComputerX()][p.getComputerY()] = p.getColor();
                }
            }
        }
    }

//    private void setColorSky() {
//        for (int i = 0; i < sky.getListDraw().size(); i++) {
//            sky.getListDraw().get(i).setColor(colorSky[Math.max(0, 12 - (Math.abs(cycleIndex - 208) / 16))]);
//        }
//    }

    private void setColorSky() {
        for (int i = 0; i < sky.getListDraw().size(); i++) {
            sky.getListDraw().get(i).setColor(colorSky[Math.abs(sky.getListDraw().get(i).getX() / 10)]);
            colorSkyMove[sky.getListDraw().get(i).getX() + 260] = sky.getListDraw().get(i).getColor();
        }
    }

    private void skyMove() {
        int[] colorSkyMoveTemp = colorSkyMove.clone();
        int colorTemp = colorSkyMove[0];
        for (int i = 0; i < sky.getListDraw().size(); i++) {
            sky.getListDraw().get(i).setColor(colorSkyMove[sky.getListDraw().get(i).getX() + 261]);
        }
        for (int i = 0; i < colorSkyMove.length - 1; i++) {
            colorSkyMove[i] = colorSkyMoveTemp[i + 1];
        }
        colorSkyMove[colorSkyMove.length - 1] = colorTemp;
    }
}
