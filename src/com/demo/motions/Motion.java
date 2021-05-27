package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.listeners.CanvasListener;
import com.demo.models.Point2D;
import com.demo.shape.Circle;
import com.demo.shape.Ellipse;
import com.demo.shape.Rectangle;
import com.demo.shape.Triangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Motion {
    Random random = new Random();

    public Sun sun;
    private List<Explosion> explosions;
    private List<Bomb> bombs;
    private List<Enemy> enemies;
    private List<Point2D> listCannonsDraw;
    private List<Point2D> listLandDraw;
    private Moon moon;
    private Ellipse cycle;
    private Rectangle sky;
    public static int cycleIndex;
    int[] colorSky = new int[]{0x99ccff, 0x80bfff, 0x66b3ff, 0x4da6ff, 0x3399ff, 0x1a8cff, 0x0080ff, 0x0073e6, 0x0066cc, 0x0059b3, 0x004d99, 0x004080, 0x003366};
    int[] colorSkyMove = new int[521];
    int[] yCoordinate = new int[]{-10, -25, -40, -55};

    private DrawCanvas canvas;
    private CanvasListener listener;

    public Motion(DrawCanvas canvas, CanvasListener listener) {
        this.canvas = canvas;
        this.listener = listener;

        Arrays.fill(colorSkyMove, 0, colorSkyMove.length, 0x003366);

        Rectangle land = new Rectangle(canvas, null, null, DrawMode.DEFAULT, 0x009933, 0x009933, true, true);
        Triangle moutain1 = new Triangle(canvas, null, null, DrawMode.DEFAULT, 0x4d3319, 0x4d3319, true, true);
        Triangle moutain2 = new Triangle(canvas, null, null, DrawMode.DEFAULT, 0x734d26, 0x734d26, true, true);
        Triangle moutain3 = new Triangle(canvas, null, null, DrawMode.DEFAULT, 0x996633, 0x996633, true, true);
        sky = new Rectangle(canvas, null, null, DrawMode.DEFAULT, 0x99ccff, 0x99ccff, true, true);
        cycle = new Ellipse(canvas, null, null, DrawMode.DEFAULT, 0x000000, 0x000000, true, true);
        land.setPoints(new Point2D[]{new Point2D(-100, 0), new Point2D(100, 0), new Point2D(100, -60), new Point2D(-100, -60)});
        moutain1.setPoints(new Point2D[]{new Point2D(21, 20), new Point2D(41, 0), new Point2D(1, 0)});
        moutain2.setPoints(new Point2D[]{new Point2D(69, 34), new Point2D(111, 0), new Point2D(37, 0)});
        moutain3.setPoints(new Point2D[]{new Point2D(-49, 34), new Point2D(14, 0), new Point2D(-112, 0)});
        sky.setPoints(new Point2D[]{new Point2D(-100, 60), new Point2D(100, 60), new Point2D(100, 0), new Point2D(-100, 0)});
        cycle.setPoints(new Point2D[]{new Point2D(0, 0), new Point2D(130, 0), new Point2D(0, 40)});
        cycle.processDraw();
        cycleIndex = cycle.getListDraw().size() - 1;

        sun = new Sun(canvas, cycle);
        moon = new Moon(canvas, sun.sun, cycle);

        enemies = new ArrayList<>();
        explosions = new ArrayList<>();
        bombs = new ArrayList<>();
        listCannonsDraw = new ArrayList<>();
        listLandDraw = new ArrayList<>();

        for (int i = 0; i < yCoordinate.length; i++) {
            Rectangle cannon = new Rectangle(canvas, null, null, DrawMode.DEFAULT, 0x666666, 0x666666, true, true);
            Circle wheel = new Circle(canvas, null, null, DrawMode.DEFAULT, 0x000000, 0x000000, true, true);
            cannon.setPoints(new Point2D[]{new Point2D(-100, yCoordinate[i] - 4), new Point2D(-90, yCoordinate[i] - 4),
                    new Point2D(-90, yCoordinate[i] + 4), new Point2D(-100, yCoordinate[i] + 4)});
            wheel.setPoints(new Point2D[]{new Point2D(-96, yCoordinate[i] - 4), new Point2D(-100, yCoordinate[i] - 4)});

            cannon.processDraw();
            cannon.fillColor();
            wheel.processDraw();
            wheel.fillColor();
            listCannonsDraw.addAll(cannon.getListDraw());
            listCannonsDraw.addAll(wheel.getListDraw());
        }

        land.processDraw();
        land.fillColor();
        moutain1.processDraw();
        moutain1.fillColor();
        moutain2.processDraw();
        moutain2.fillColor();
        moutain3.processDraw();
        moutain3.fillColor();

        listLandDraw.addAll(land.getListDraw());
        listLandDraw.addAll(moutain1.getListDraw());
        listLandDraw.addAll(moutain2.getListDraw());
        listLandDraw.addAll(moutain3.getListDraw());


        sky.processDraw();
        sky.fillColor();
        setColorSky();
    }


    public void run() {
        int[][] b = DrawCanvas.newDefaultBoard();

        moon.run();
        sun.run();
        Sun reflectSun = sun.reflectByOy();

        List<Point2D> listEnemyDraw = new ArrayList<>();
        List<Point2D> listBombsDraw = new ArrayList<>();
        List<Point2D> listExplosionDraw = new ArrayList<>();
        int y = yCoordinate[random.nextInt(yCoordinate.length)];
        if (cycleIndex % 50 == 0) {
            Enemy enemy = new Enemy(canvas, new Point2D(100, y));
            // TODO: Thu phóng enemy
            enemy.scale(Config.enemyScale);

            int speedRanX = Math.abs(random.nextInt()) % Math.abs(Config.enemySpeedX - Config.enemySpeedX2) + Math.min(Config.enemySpeedX, Config.enemySpeedX2);
            enemy.move(speedRanX, Config.enemySpeedY);
            enemies.add(enemy);

            if (Config.isReflectEAB) {
                Enemy reflectEnemy = enemy.reflectByOx();
                reflectEnemy.scale(Config.enemyScale);
                reflectEnemy.move(speedRanX, Config.enemySpeedY);
                enemies.add(reflectEnemy);
            }

            Bomb bomb = new Bomb(canvas, new Point2D(-100, y));
            // TODO: Thu phóng bomb
            bomb.scale(Config.bombScale);
            bombs.add(bomb);

            if (Config.isReflectEAB) {
                Bomb reflectBomb = bomb.reflectByOx();
                reflectBomb.scale(Config.bombScale);
                bombs.add(reflectBomb);
            }

            explosions.add(new Explosion(canvas, new Point2D(new Point2D(-90, y)), 1,
                    0xffffff, 0xffffff, 10));
        }

        for (int i = 0; i < enemies.size(); i++) {
            bombs.get(i).run();
            if (boomboomboom(bombs.get(i), enemies.get(i))) {
                explosions.add(new Explosion(canvas, new Point2D(enemies.get(i).wheel.getCenterPoint()), 10,
                        0xff8000, 0xff9933, 100));
                bombs.remove(i);
                enemies.remove(i);

            } else {
                enemies.get(i).run();
                listEnemyDraw.addAll(enemies.get(i).getListDraw());
                listBombsDraw.addAll(bombs.get(i).getListDraw());
            }
        }
        for (int i = 0; i < explosions.size(); i++) {
            if (explosions.get(i).duration == 0) {
                explosions.remove(i);
            } else {
                explosions.get(i).run();
                listExplosionDraw.addAll(explosions.get(i).getListDraw());
            }

        }


        if (cycleIndex < cycle.getListDraw().size() - 1 && cycle.getListDraw().get(cycleIndex).getX() != cycle.getListDraw().get(cycleIndex + 1).getX()) {
            skyMove();
        }


        addToBoard(b,
                sky.getListDraw(),
                sun.getListDraw(),
                Config.isReflectSun ? reflectSun.getListDraw() : new ArrayList<>(),
                moon.getListDraw(),
                listLandDraw,
                listEnemyDraw,
                listBombsDraw,
                listCannonsDraw,
                listExplosionDraw
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

    private boolean boomboomboom(Bomb bomb, Enemy enemy) {
        for (Point2D p : bomb.getListDraw()) {
            for (Point2D q : enemy.getListDraw()) {
                if (p.equals(q)) return true;
            }
//            if(p.equals(enemy.wheel.getCenterPoint())){
//                return true;
//            }
        }
        return false;
    }
}
