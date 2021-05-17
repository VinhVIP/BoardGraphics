package com.demo.motions;

import com.demo.DrawCanvas;

/**
 * Create by VinhIT
 * On 17/05/2021
 */

public class MotionManager extends Thread {
    private Bike bike;
    private DrawCanvas canvas;

    public MotionManager(DrawCanvas canvas) {
        this.canvas = canvas;
        bike = new Bike(canvas);
    }

    @Override
    public void run() {
        while (true) {
            bike.run();
            try {
                Thread.sleep(80);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
