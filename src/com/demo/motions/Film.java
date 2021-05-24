package com.demo.motions;

import com.demo.DrawCanvas;
import com.demo.models.Point2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.demo.DrawCanvas.*;

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
//            tools();
        }
    }

    public void playFilm() {
        int maxFrames = 1000;
        File file;
        String path = "C:\\Users\\Admin\\Documents\\film2\\";
        BufferedImage image = null;

        for (int frame = 1; frame <= maxFrames; frame++) {
            String imagePath = path + String.format("%04d", frame) + ".png";
            file = new File(imagePath);

            try {
                image = ImageIO.read(file);
                Graphics g = canvas.getGraphics();
                g.drawImage(image, 0, 0, null);

                image.flush();

                Thread.sleep(35);
            } catch (Exception e) {
//                e.printStackTrace();
            }

        }
        System.out.println("done");
    }

    public void tools() {
        int maxFrames = 500;
        File file;
        String path = "C:\\Users\\Admin\\Documents\\film0\\";
        String path2 = "C:\\Users\\Admin\\Documents\\film2\\";
        int[][] b = DrawCanvas.newDefaultBoard();
        BufferedImage image = null;

        for (int frame = 1; frame <= maxFrames; frame++) {
            String imagePath = path + String.format("%04d", frame) + ".jpg";
            String imagePath2 = path2 + String.format("%04d", frame+500) + ".png";
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

                saveFile(b, imagePath2);
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("done");
    }

    public void saveFile(int[][] b, String filePath) {
        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        Point2D p;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                g.setColor(new Color(b[i][j]));
                p = Point2D.fromComputerCoordinate(i, j);
                g.fillRect(p.getComputerX() * pixelSize, p.getComputerY() * pixelSize, pixelSize, pixelSize);
            }
        }
        try {
            ImageIO.write(image, "png", new File(filePath));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
