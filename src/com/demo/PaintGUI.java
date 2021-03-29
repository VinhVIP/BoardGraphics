package com.demo;

import javax.swing.*;
import java.awt.*;

/**
 * Create by VinhIT
 * On 19/03/2021
 */

public class PaintGUI extends JFrame implements MouseCoordinateChangeListener {

    private JPanel rootPanel;
    private JButton btnPen;
    private JButton btnChooseColor;
    private JButton btnLine;
    private JButton btnClear;
    private JButton btnRect;
    private JButton btnCircle;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button10;
    private JButton button11;
    private JButton button12;
    private JPanel mainPanel;
    private JLabel labelDrawMode;
    private JLabel labelCoordinate;

    private DrawCanvas canvas;


    public PaintGUI() {
        setTitle("Paint");
        setSize(1200, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        canvas = new DrawCanvas(this);
        mainPanel.add(canvas);

        btnClear.addActionListener(e -> canvas.clearScreen());

        btnLine.addActionListener(e -> {
            canvas.setDrawMode(DrawMode.LINE);
            labelDrawMode.setText("MODE: LINE");
        });
        btnRect.addActionListener(e -> {
            canvas.setDrawMode(DrawMode.RECTANGLE);
            labelDrawMode.setText("MODE: RECT");
        });
        btnPen.addActionListener(e -> {
            canvas.setDrawMode(DrawMode.PEN);
            labelDrawMode.setText("MODE: PEN");
        });

        btnCircle.addActionListener(e->{
            canvas.setDrawMode(DrawMode.CIRCLE);
            labelDrawMode.setText("MODE: CIRCLE");
        });

        // Chọn màu vẽ
        btnChooseColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(null, "Choose Color", btnChooseColor.getBackground());
            if (color != null) {
                btnChooseColor.setBackground(color);
                DrawCanvas.currentColor = color.getRGB();
            }

        });

        add(rootPanel);


    }


    @Override
    public void mouseCoordinate(int x, int y) {
        labelCoordinate.setText(String.format("X:%d , Y:%d", x, y));
    }
}
