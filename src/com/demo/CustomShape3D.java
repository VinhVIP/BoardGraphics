package com.demo;

import com.demo.listeners.DialogListener;
import com.demo.models.Point3D;

import javax.swing.*;
import java.awt.event.*;

public class CustomShape3D extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfX;
    private JTextField tfY;
    private JTextField tfZ;
    private JTextField tfDX;
    private JTextField tfDY;
    private JTextField tfDZ;
    private JButton btnImage;
    private JButton btnChooseRectangular;
    private JButton btnChooseCone;
    private JButton btnChooseCylinder;
    private JPanel panel;
    private JPanel panelInput;

    private Mode shape3DMode;
    private DialogListener listener;

    public CustomShape3D(DialogListener listener) {
        this.listener = listener;

        setContentPane(contentPane);
        setTitle("Custom 3D Shape");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        shape3DMode = Mode.RECTANGULAR;
        btnImage.setVisible(false);

        btnChooseRectangular.addActionListener(e -> {
//            ImageIcon icon = new ImageIcon(getClass().getResource("assets/icons/preview_rectangular.jpg"));
//            btnImage.setIcon(icon);
            shape3DMode = Mode.RECTANGULAR;
        });
        btnChooseCylinder.addActionListener(e -> {
//            ImageIcon icon = new ImageIcon(getClass().getResource("com/demo/icons/preview_cylinder.jpg"));
//            btnImage.setIcon(icon);
            shape3DMode = Mode.CYLINDER;
        });
        btnChooseCone.addActionListener(e -> {
//            ImageIcon icon = new ImageIcon(getClass().getResource("assets/icons/preview_cone.jpg"));
//            btnImage.setIcon(icon);
            shape3DMode = Mode.CONE;
        });


        this.setLocation(600, 250);
        this.pack();
        this.setVisible(true);
    }

    private void onOK() {
        try {
            int x = Integer.parseInt(tfX.getText().trim());
            int y = Integer.parseInt(tfY.getText().trim());
            int z = Integer.parseInt(tfZ.getText().trim());

            int dx = Integer.parseInt(tfDX.getText().trim());
            int dy = Integer.parseInt(tfDY.getText().trim());
            int dz = Integer.parseInt(tfDZ.getText().trim());

            switch (shape3DMode) {
                case RECTANGULAR -> listener.onDrawRectangular(new Point3D(x, y, z), dx, dy, dz);
                case CYLINDER -> listener.onDrawCylinder(new Point3D(x, y, z), dx, dy, dz);
                case CONE -> listener.onDrawCone(new Point3D(x, y, z), dx, dy, dz);
            }

            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Thông tin nhập không hợp lệ", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
