package com.demo;

import com.demo.shape.Geometry;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Create by VinhIT
 * On 05/05/2021
 */

public class Paint extends JFrame implements CanvasListener {

    private JButton btnRotate;
    private JButton btnMove;
    private JButton btnScale;
    private JButton btnSymmetry;
    private JButton btnRedo;
    private JButton btnClear;
    private JButton btnUndo;
    private JButton btnChooseColor;
    private JButton btnLine;
    private JButton btnRect;
    private JButton btnCircle;
    private JButton btnEllipse;
    private JList listShape;
    private JCheckBox cbShowAxis;
    private JCheckBox cbShowGrid;
    private JCheckBox cbShowPointCoord;
    private JComboBox cbChooseDrawMode;
    private JPanel mainPanel;
    private JLabel labelDrawMode;
    private JLabel labelCoordinate;
    private JPanel rootPanel;
    private JButton btnDeseleted;


    private DrawCanvas canvas;
    DefaultListModel listModel = new DefaultListModel();

    public Paint() {
        setTitle("Paint");
        setSize(1400, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        canvas = new DrawCanvas(this);

        mainPanel.add(canvas);


        btnClear.addActionListener(e -> {
            int[] indexes = listShape.getSelectedIndices();
            if (indexes.length > 0) {
                canvas.clearShapes(indexes);
                Arrays.sort(indexes);
                for (int i = indexes.length - 1; i >= 0; i--) {
                    System.out.print(indexes[i] + " ");
                    listModel.remove(indexes[i]);
                }
                System.out.println();

            } else {
                canvas.clearScreen();
            }
        });

        btnLine.addActionListener(e -> {
            canvas.setShapeMode(ShapeMode.LINE);
            labelDrawMode.setText("MODE: LINE");
        });
        btnRect.addActionListener(e ->
        {
            canvas.setShapeMode(ShapeMode.RECTANGLE);
            labelDrawMode.setText("MODE: RECT");
        });

//        btnPen.addActionListener(e ->
//        {
//            canvas.setShapeMode(ShapeMode.PEN);
//            labelDrawMode.setText("MODE: PEN");
//        });

        btnCircle.addActionListener(e ->
        {
            canvas.setShapeMode(ShapeMode.CIRCLE);
            labelDrawMode.setText("MODE: CIRCLE");
        });

        btnEllipse.addActionListener(e -> {
            canvas.setShapeMode(ShapeMode.ELLIPSE);
            labelDrawMode.setText("MODE: ELLIPSE");
        });

//        btnEllipseDash.addActionListener(e -> {
//            canvas.setShapeMode(ShapeMode.ELLIPSE_DASH);
//            labelDrawMode.setText("MODE: ELLIPSE DASH");
//        });

//        btnPoint.addActionListener(e ->
//        {
//            canvas.setShapeMode(ShapeMode.POINT);
//            labelDrawMode.setText("MODE: POINT");
//        });

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

        btnUndo.addActionListener(e -> canvas.undo());

        btnRedo.addActionListener(e -> canvas.redo());

        btnMove.addActionListener(e -> {
            int[] indexMove = listShape.getSelectedIndices();
            Arrays.sort(indexMove);
            for (int i : indexMove) {
                System.out.print(i + " ");
            }
            System.out.println();
            canvas.move(indexMove);
        });

        btnDeseleted.addActionListener(e -> listShape.clearSelection());


        listShape.setModel(listModel);


        // Important
        add(rootPanel);


    }

    @Override
    public void mouseCoordinate(int x, int y) {
        labelCoordinate.setText(String.format("X:%d , Y:%d", x, y));
    }

    @Override
    public void notifyShapeInserted(String shapeTitle) {
        listModel.addElement(shapeTitle);
    }

    @Override
    public void notifyDataSetChanged(List listShape) {
        listModel.clear();
        for (int i = 0; i < listShape.size(); i++) {
            Geometry g = (Geometry) listShape.get(i);
            listModel.addElement(g.toString());
        }
    }

    @Override
    public void notifyShapeChanged(int position, String newTitle) {
        listModel.set(position, newTitle);
    }

    @Override
    public void notifyDeselectedAllItems() {
        listShape.clearSelection();
    }

    @Override
    public void clear() {
        listModel.clear();
    }

}
