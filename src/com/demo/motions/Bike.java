package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.shape.Circle;
import com.demo.shape.Line;

/**
 * Create by VinhIT
 * On 15/05/2021
 */

public class Bike {
    private Circle wheel;
    private Line rim1, rim2;

    private DrawCanvas canvas;

    public Bike(DrawCanvas canvas) {
        this.canvas = canvas;

        wheel = new Circle(canvas, DrawCanvas.currentColor, DrawMode.DEFAULT);
        rim1 = new Line(canvas, DrawCanvas.currentColor, DrawMode.DEFAULT);
        rim2 = new Line(canvas, DrawCanvas.currentColor, DrawMode.DEFAULT);

        wheel.setPoints(new Point2D[]{new Point2D(0, 0), new Point2D(10, 0)});
        rim1.setPoints(new Point2D[]{new Point2D(-10, 0), new Point2D(10, 0)});
        rim2.setPoints(new Point2D[]{new Point2D(0, 10), new Point2D(0, -10)});
    }

    public void run() {
        // Di chuyển sang phải 1 đơn vị
        wheel.move(1, 0);
        rim1.move(1, 0);
        rim2.move(1, 0);

        // Quay quanh tâm đường thẳng
        rim1.rotate(rim1.getCenterPoint(), -0.2);
        rim2.rotate(rim2.getCenterPoint(), -0.2);

        // Xóa những điểm vẽ cũ
        canvas.clearDraw(wheel.getListDraw());
        canvas.clearDraw(rim1.getListDraw());
        canvas.clearDraw(rim2.getListDraw());

        // Thiết lập những điểm vẽ mới
        wheel.processDraw();
        rim1.processDraw();
        rim2.processDraw();

        // Vẽ những điểm vẽ mới
        canvas.applyDraw(wheel.getListDraw());
        canvas.applyDraw(rim1.getListDraw());
        canvas.applyDraw(rim2.getListDraw());


    }
}
