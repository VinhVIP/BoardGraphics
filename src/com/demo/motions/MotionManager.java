package com.demo.motions;

import com.demo.DrawCanvas;

/**
 * Create by VinhIT
 * On 17/05/2021
 */

public class MotionManager extends Thread {
    private Motion motion;
    private Bike bike;
    private DrawCanvas canvas;

    public MotionManager(DrawCanvas canvas) {
        this.canvas = canvas;
//        bike = new Bike(canvas);
        motion = new Motion(canvas);
    }

    @Override
    public void run() {
        while (true) {
//            bike.run();
            motion.run();
            try {
                Thread.sleep(60);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
