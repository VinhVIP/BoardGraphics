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
    private JButton btnPoint;
    private JPanel mainPanel;
    private JLabel labelDrawMode;
    private JLabel labelCoordinate;
    private JCheckBox cbShowGrid;
    private JCheckBox cbShowAxis;
    private JComboBox cbChooseDrawMode;
    private JCheckBox cbShowPointCoord;
    private JButton btnEllipse;
    private JButton btnEllipseDash;
    private JButton btnUndo;
    private JButton btnRedo;
    private JButton btnMove;

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
            canvas.setShapeMode(ShapeMode.LINE);
            labelDrawMode.setText("MODE: LINE");
        });
        btnRect.addActionListener(e ->
        {
            canvas.setShapeMode(ShapeMode.RECTANGLE);
            labelDrawMode.setText("MODE: RECT");
        });
        btnPen.addActionListener(e ->
        {
            canvas.setShapeMode(ShapeMode.PEN);
            labelDrawMode.setText("MODE: PEN");
        });

        btnCircle.addActionListener(e ->
        {
            canvas.setShapeMode(ShapeMode.CIRCLE);
            labelDrawMode.setText("MODE: CIRCLE");
        });

        btnEllipse.addActionListener(e -> {
            canvas.setShapeMode(ShapeMode.ELLIPSE);
            labelDrawMode.setText("MODE: ELLIPSE");
        });

        btnEllipseDash.addActionListener(e -> {
            canvas.setShapeMode(ShapeMode.ELLIPSE_DASH);
            labelDrawMode.setText("MODE: ELLIPSE DASH");
        });

        btnPoint.addActionListener(e ->
        {
            canvas.setShapeMode(ShapeMode.POINT);
            labelDrawMode.setText("MODE: POINT");
        });

        // Chọn màu vẽ
        btnChooseColor.addActionListener(e ->
        {
            Color color = JColorChooser.showDialog(null, "Choose Color", btnChooseColor.getBackground());
            if (color != null) {
                btnChooseColor.setBackground(color);
                DrawCanvas.currentColor = color.getRGB();
            }
        });

        cbShowAxis.addActionListener(e ->
        {
            boolean isSelected = cbShowAxis.isSelected();
            canvas.setShowAxis(isSelected);
        });

        cbShowGrid.addActionListener(e ->
        {
            boolean isSelected = cbShowGrid.isSelected();
            canvas.setShowGrid(isSelected);
        });

        cbShowPointCoord.addActionListener(e -> {
            canvas.setShowPointCoord(cbShowPointCoord.isSelected());
        });

        cbChooseDrawMode.addActionListener(e ->

        {
            String s = (String) cbChooseDrawMode.getSelectedItem();
            if (s.equals("DEFAULT")) canvas.setDrawMode(DrawMode.DEFAULT);
            else if (s.equals("DOT")) canvas.setDrawMode(DrawMode.DOT);
            else if (s.equals("DASH")) canvas.setDrawMode(DrawMode.DASH);
            else if (s.equals("DASHDOT")) canvas.setDrawMode(DrawMode.DASH_DOT);
            else if (s.equals("DASHDOTDOT")) canvas.setDrawMode(DrawMode.DASH_DOT_DOT);
            else if (s.equals("ARROW")) canvas.setDrawMode(DrawMode.ARROW);
        });

        btnUndo.addActionListener(e->canvas.undo());

        btnRedo.addActionListener(e->canvas.redo());

        btnMove.addActionListener(e->canvas.move());

        // Important
        add(rootPanel);


    }


    @Override
    public void mouseCoordinate(int x, int y) {
        labelCoordinate.setText(String.format("X:%d , Y:%d", x, y));
    }


}
