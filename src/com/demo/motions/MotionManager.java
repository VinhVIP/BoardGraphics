package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.listeners.CanvasListener;

/**
 * Create by VinhIT
 * On 17/05/2021
 */

public class MotionManager extends Thread {
    private Motion motion;
    private Bike bike;
    private DrawCanvas canvas;

    private CanvasListener listener;

    public MotionManager(DrawCanvas canvas, CanvasListener listener) {
        this.canvas = canvas;
        this.listener = listener;
//        bike = new Bike(canvas);
        motion = new Motion(canvas, listener);
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
