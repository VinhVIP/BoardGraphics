package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Circle;
import com.demo.shape.Line;
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

    public Explosion(DrawCanvas canvas) {
        this.canvas = canvas;

        listDraw = new ArrayList<>();
        exploseArea = new Rectangle(canvas,null, null, DrawMode.DEFAULT, 0xffffff, 0xffffff, true, true);
        exploseArea.setPoints(new Point2D[]{new Point2D(30, -30), new Point2D(50, -30), new Point2D(50, -50), new Point2D(30, -50)});
        exploseArea.processDraw();
        exploseArea.fillColor();
        for(Point2D p : exploseArea.getListDraw()){
            exploses.add(new Explose(canvas,null, null, DrawMode.DEFAULT, 0xff6600, 0xff9933, true, true));
            exploses.get(exploses.size()-1).setPoints(new Point2D[]{p, new Point2D(p.getX()+1, p.getY())});
        }
    }

    public void run() {
        List<Integer> indexs = new ArrayList<>();
        for(int i=0; i<exploses.size(); i++){
            if(!exploses.get(i).isExplose()){
                indexs.add(i);
            }else{
                exploses.get(i).boom();
            }
        }
        if(!indexs.isEmpty()){
            int index = indexs.get(random.nextInt(indexs.size()));
            exploses.get(index).boom();
            exploses.get(index).setExplose();

        }setListDraw();
    }

    public void setListDraw(){
        listDraw.clear();
        for(int i=0; i<exploses.size(); i++){
            if(exploses.get(i).isExplose()){
                listDraw.addAll(exploses.get(i).getListDraw());
            }
        }
    }

    public List<Point2D> getListDraw(){
        return listDraw;
    }
}
