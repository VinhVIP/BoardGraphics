package com.demo;

import com.demo.listeners.CanvasListener;
import com.demo.listeners.DialogListener;
import com.demo.models.Point2D;
import com.demo.shape.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

/**
 * Create by VinhIT
 * On 05/05/2021
 */

public class Paint extends JFrame implements CanvasListener, DialogListener, ActionListener {

    private JButton btnRotate;
    private JButton btnMove;
    private JButton btnScale;
    private JButton btnReflect;
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
    private JButton btnDeselected;
    private JButton btnTriangle;
    private JButton btnPolygon;
    private JButton btnCopy;
    private JButton btnFillColor;
    private JButton btnMotion;
    private JButton btnRectangular;
    private JButton btnCylinder;
    private JButton btnCone;
    private JRadioButton radio2D;
    private JRadioButton radio3D;

    private ButtonGroup radioGroup;


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
//                Arrays.sort(indexes);
//                for (int i = indexes.length - 1; i >= 0; i--) {
//                    System.out.print(indexes[i] + " ");
//                    listModel.remove(indexes[i]);
//                }
//                System.out.println();

            } else {
                canvas.clearScreen();
            }
        });

        btnLine.addActionListener(e -> {
            canvas.setMode(Mode.LINE);
            labelDrawMode.setText("MODE: LINE");
        });
        btnRect.addActionListener(e ->
        {
            canvas.setMode(Mode.RECTANGLE);
            labelDrawMode.setText("MODE: RECT");
        });

//        btnPen.addActionListener(e ->
//        {
//            canvas.setShapeMode(ShapeMode.PEN);
//            labelDrawMode.setText("MODE: PEN");
//        });

        btnCircle.addActionListener(e ->
        {
            canvas.setMode(Mode.CIRCLE);
            labelDrawMode.setText("MODE: CIRCLE");
        });

        btnEllipse.addActionListener(e -> {
            canvas.setMode(Mode.ELLIPSE);
            labelDrawMode.setText("MODE: ELLIPSE");
        });

        btnTriangle.addActionListener(e -> {
            canvas.setMode(Mode.TRIANGLE);
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
            if (indexMove.length == 0) {
                JOptionPane.showMessageDialog(null, "Chưa có hình nào được chọn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Arrays.sort(indexMove);
            for (int i : indexMove) {
                System.out.print(i + " ");
            }
            System.out.println();
            canvas.move(indexMove);

            canvas.setMode(Mode.MOVE);
        });

        btnDeselected.addActionListener(e -> listShape.clearSelection());

        btnRotate.addActionListener(e -> {
            int[] indexMove = listShape.getSelectedIndices();
            if (indexMove.length == 0) {
                JOptionPane.showMessageDialog(null, "Chưa có hình nào được chọn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            RotationDialog dialog = new RotationDialog(this);
            canvas.setMode(Mode.ROTATE);
        });

        btnReflect.addActionListener(e -> {
            int[] indexMove = listShape.getSelectedIndices();
            if (indexMove.length == 0) {
                JOptionPane.showMessageDialog(null, "Chưa có hình nào được chọn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ReflectionDialog dialog = new ReflectionDialog(this);
            canvas.setMode(Mode.REFLECT);
        });

        btnScale.addActionListener(e -> {
            int[] indexMove = listShape.getSelectedIndices();
            if (indexMove.length == 0) {
                JOptionPane.showMessageDialog(null, "Chưa có hình nào được chọn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ScaleDialog dialog = new ScaleDialog(this);
            canvas.setMode(Mode.SCALE);

        });

        btnCopy.addActionListener(e -> {
            int[] selectedIndex = listShape.getSelectedIndices();
            if (selectedIndex.length == 0) {
                JOptionPane.showMessageDialog(null, "Chưa có hình nào được chọn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            } else {
                canvas.copyShapes(selectedIndex);
            }
        });

        btnFillColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(null, "Choose Fill Color", btnChooseColor.getBackground());
            if (color != null) {
                btnFillColor.setBackground(color);
                DrawCanvas.currentFillColor = color.getRGB();
            }
        });

        btnMotion.addActionListener(e -> {
            canvas.setShowMotions(!canvas.isShowMotions());
        });

        listShape.setModel(listModel);


        radio2D.addActionListener(this);
        radio3D.addActionListener(this);

        radioGroup = new ButtonGroup();
        radioGroup.add(radio2D);
        radioGroup.add(radio3D);

        stateButtons();

        btnRectangular.addActionListener(e -> {
            canvas.setMode(Mode.RECTANGULAR);
        });

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
        System.out.println("list shape model: " + listShape.size());
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
    public void notifyShapeModeChanged(Mode MODE) {
        labelDrawMode.setText("MODE: " + MODE);
    }

    @Override
    public void clear() {
        listModel.clear();
    }

    @Override
    public void onPointRotate(int x, int y) {
        int[] indexMove = listShape.getSelectedIndices();
        Arrays.sort(indexMove);
        canvas.rotate(indexMove, new Point2D(x, y));
    }

    @Override
    public void onPointReflect(int x, int y) {
        int[] indexMove = listShape.getSelectedIndices();
        Arrays.sort(indexMove);
        canvas.reflect(indexMove, new Point2D(x, y), null);
    }

    @Override
    public void onTwoPointReflect(int x1, int y1, int x2, int y2) {
        int[] indexMove = listShape.getSelectedIndices();
        Arrays.sort(indexMove);
        canvas.reflect(indexMove, new Point2D(x1, y1), new Point2D(x2, y2));
    }

    @Override
    public void onScale(Point2D root, double scaleX, double scaleY) {
        int[] indexMove = listShape.getSelectedIndices();
        Arrays.sort(indexMove);
        canvas.scale(indexMove, root, scaleX, scaleY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (canvas.isIs2DCoordinates() != radio2D.isSelected()) {
            stateButtons();
            canvas.setIs2DCoordinates(radio2D.isSelected());
            canvas.setMode(Mode.NONE);
            canvas.clearScreen();
        }
    }

    private void stateButtons() {
        btnLine.setEnabled(radio2D.isSelected());
        btnRect.setEnabled(radio2D.isSelected());
        btnTriangle.setEnabled(radio2D.isSelected());
        btnCircle.setEnabled(radio2D.isSelected());
        btnEllipse.setEnabled(radio2D.isSelected());
        btnPolygon.setEnabled(radio2D.isSelected());

        btnRotate.setEnabled(radio2D.isSelected());
        btnReflect.setEnabled(radio2D.isSelected());
        btnMove.setEnabled(radio2D.isSelected());
        btnScale.setEnabled(radio2D.isSelected());

        btnRectangular.setEnabled(radio3D.isSelected());
        btnCone.setEnabled(radio3D.isSelected());
        btnCylinder.setEnabled(radio3D.isSelected());
    }
}
