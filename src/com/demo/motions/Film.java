package com.demo.motions;

import com.demo.DrawCanvas;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Create by VinhIT
 * On 24/05/2021
 */

public class Film extends Thread {

    private DrawCanvas canvas;

    public Film(DrawCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void run() {
        while (true) {
            playFilm();
        }
    }

    public void playFilm() {
        int maxFrames = 250;
        File file;
        String path = "C:\\Users\\Admin\\Documents\\film\\";
        int[][] b = DrawCanvas.newDefaultBoard();
        BufferedImage image = null;

        for (int frame = 1; frame <= maxFrames; frame++) {
            String imagePath = path + String.format("%04d", frame) + ".jpg";
            file = new File(imagePath);

            try {
                image = ImageIO.read(file);

                for (int i = 2; i < image.getWidth(); i += 5) {
                    if (i >= DrawCanvas.canvasWidth) break;
                    for (int j = 2; j < image.getHeight(); j += 5) {
                        if (j >= DrawCanvas.canvasHeight) break;
                        b[i / 5][j / 5] = image.getRGB(i, j);
                    }
                }

                canvas.applyBoard(b);
                Thread.sleep(40);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
