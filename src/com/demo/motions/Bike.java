package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Circle;
import com.demo.shape.Line;
import com.demo.shape.Rectangle;

/**
 * Create by VinhIT
 * On 15/05/2021
 */

public class Bike {
    private Circle wheel;
    private Line rim1, rim2;
    private Rectangle bg;

    private DrawCanvas canvas;

    public Bike(DrawCanvas canvas) {
        this.canvas = canvas;

        wheel = new Circle(canvas, null, null, DrawMode.DEFAULT, 0xffff00, 0xff00ff, true, true);
        rim1 = new Line(canvas, DrawMode.DEFAULT, DrawCanvas.currentColor);
        rim2 = new Line(canvas, DrawMode.DEFAULT, DrawCanvas.currentColor);
        bg = new Rectangle(canvas, null, null, DrawMode.DEFAULT, 0xffff00, 0xffff00, true, true);

        wheel.setPoints(new Point2D[]{new Point2D(0, 0), new Point2D(10, 0)});
        rim1.setPoints(new Point2D[]{new Point2D(0, 10), new Point2D(0, -10)});
        rim2.setPoints(new Point2D[]{new Point2D(-20, 0), new Point2D(20, 0)});
        bg.setPoints(new Point2D[]{new Point2D(-40, 0), new Point2D(90, 0), new Point2D(90, -40), new Point2D(-40, -40)});
    }

    public void run() {
        // Di chuyển sang phải 1 đơn vị
        wheel.move(1, 0);
        rim1.move(1, 0);
        rim2.move(1, 0);

        // Quay quanh tâm đường thẳng
        rim1.rotate(rim1.getCenterPoint(), -Math.PI / 12);
        rim2.rotate(rim2.getCenterPoint(), -Math.PI / 12);

        // Xóa những điểm vẽ cũ
        canvas.clearDraw(wheel.getListDraw());
        canvas.clearDraw(rim1.getListDraw());
        canvas.clearDraw(rim2.getListDraw());

        // Thiết lập những điểm vẽ mới
        wheel.processDraw();
        rim1.processDraw();
        rim2.processDraw();
//        wheel.draw();
//        rim1.draw();
//        rim2.draw();
        bg.processDraw();

        wheel.fillColor();
        bg.fillColor();
//        bg.draw();

        int[][] b = DrawCanvas.newDefaultBoard();
        for (Point2D p : wheel.getListDraw()) {
            if (p.insideScreen()) {
                b[p.getComputerX()][p.getComputerY()] = p.getColor();
            }
        }
        for (Point2D p : rim1.getListDraw()) {
            if (p.insideScreen()) {
                b[p.getComputerX()][p.getComputerY()] = p.getColor();
            }
        }
        for (Point2D p : rim2.getListDraw()) {
            if (p.insideScreen()) {
                b[p.getComputerX()][p.getComputerY()] = p.getColor();
            }
        }
        for (Point2D p : bg.getListDraw()) {
            if (p.insideScreen()) {
                b[p.getComputerX()][p.getComputerY()] = p.getColor();
            }
        }
        canvas.applyBoard(b);

        // Vẽ những điểm vẽ mới
//        canvas.applyDraw(wheel.getListDraw());
//        canvas.applyDraw(rim1.getListDraw());
//        canvas.applyDraw(rim2.getListDraw());
//        canvas.applyDraw(bg.getListDraw());


    }
}
