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
    private JComboBox cbChooseLine;
    private JCheckBox cbShowPointCoord;
    private JButton btnEllipse;

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
        btnRect.addActionListener(e ->
        {
            canvas.setDrawMode(DrawMode.RECTANGLE);
            labelDrawMode.setText("MODE: RECT");
        });
        btnPen.addActionListener(e ->
        {
            canvas.setDrawMode(DrawMode.PEN);
            labelDrawMode.setText("MODE: PEN");
        });

        btnCircle.addActionListener(e ->
        {
            canvas.setDrawMode(DrawMode.CIRCLE);
            labelDrawMode.setText("MODE: CIRCLE");
        });

        btnEllipse.addActionListener(e -> {
            canvas.setDrawMode(DrawMode.ELLIPSE);
            labelDrawMode.setText("MODE: ELLIPSE");
        });


        btnPoint.addActionListener(e ->
        {
            canvas.setDrawMode(DrawMode.POINT);
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

        cbChooseLine.addActionListener(e ->

        {
            String s = (String) cbChooseLine.getSelectedItem();
            if (s.equals("DEFAULT")) DrawCanvas.lineMode = LineMode.DEFAULT;
            else if (s.equals("DOT")) DrawCanvas.lineMode = LineMode.DOT;
            else if (s.equals("DASH")) DrawCanvas.lineMode = LineMode.DASH;
            else if (s.equals("DASHDOT")) DrawCanvas.lineMode = LineMode.DASH_DOT;
            else if (s.equals("DASHDOTDOT")) DrawCanvas.lineMode = LineMode.DASH_DOT_DOT;
            else if (s.equals("ARROW")) DrawCanvas.lineMode = LineMode.ARROW;
        });


        // Important
        add(rootPanel);


    }


    @Override
    public void mouseCoordinate(int x, int y) {
        labelCoordinate.setText(String.format("X:%d , Y:%d", x, y));
    }
}
