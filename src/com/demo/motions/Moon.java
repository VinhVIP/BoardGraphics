package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Circle;
import com.demo.shape.Ellipse;
import com.demo.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Moon {
    Circle moon;
    Circle sun;
    Ellipse cycle;
    List<Point2D> listDraw;
    Map<Integer, Integer> listRemove;
    Point2D p1, p2;
    private int mx, my;

    private DrawCanvas canvas;
    public Moon(DrawCanvas canvas, Circle sun, Ellipse cycle){
        this.canvas = canvas;
        this.sun = sun;
        this.cycle = cycle;

        listRemove = new HashMap<>();
        listDraw = new ArrayList<>();

        moon = new Circle(canvas,null, null, DrawMode.DEFAULT, 0xd9d9d9, 0xbfbfbf, true, true);
        moon.setPoints(new Point2D[]{sun.getPoints()[0].reflect(cycle.getCenterPoint()), sun.getPoints()[1].reflect(cycle.getCenterPoint())});


    }

    public void run(){
        mx = cycle.getListDraw().get(Motion.cycleIndex).getX()-sun.getCenterPoint().getX();
        my = cycle.getListDraw().get(Motion.cycleIndex).getY()-sun.getCenterPoint().getY();
        moon.move(-mx, -my);

        moon.processDraw();
        moon.fillColor();
        drawCrescentMoon();
        setListDraw();
    }

    public List<Point2D> getListDraw(){
        return listDraw;
    }

    private void drawCrescentMoon(){
        p1 = moon.getEndPoint().rotate(moon.getCenterPoint(), Math.PI/3);
        p2 = moon.getEndPoint().rotate(moon.getCenterPoint(), -Math.PI/3);
        boolean stop = true;

        for(int index = 0; index<moon.getListDraw().size(); index++){
            Point2D p = moon.getListDraw().get(index);
            if(p.getX() == p1.getX() && p.getY() == p1.getY()){
                stop = false;
            }else if(p.getX() == p2.getX() && p.getY() == p2.getY()){
                moon.getListDraw().set(index ,p.reflect(new Point2D(p1.getX(), (p1.getY()+p2.getY())/2)));
                stop = true;
            }
            if(!stop){
                moon.getListDraw().set(index ,p.reflect(new Point2D(p1.getX(), (p1.getY()+p2.getY())/2)));
                listRemove.put(moon.getListDraw().get(index).getY(), moon.getListDraw().get(index).getX());
            }
        }
    }

    public void setListDraw(){
        listDraw.clear();
        for(Point2D p : moon.getListDraw()){
            if(isAdd(p)){
                listDraw.add(p);
            }
        }
        listRemove.clear();
    }

    private boolean isAdd(Point2D p){
        if(!listRemove.containsKey(p.getY())) return true;
        return (p.getX()-listRemove.get(p.getY()) >= 0);
    }
}
