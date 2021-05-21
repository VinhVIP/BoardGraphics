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
    private JTextField tfLength;
    private JTextField tfWidth;
    private JTextField tfHeight;
    private JButton btnImage;
    private JButton btnChooseRectangular;
    private JButton btnChooseCone;
    private JButton btnChooseCylinder;
    private JPanel panel;
    private JPanel panelInput;

    private DialogListener listener;

    public CustomShape3D(DialogListener listener) {
        this.listener = listener;

        setContentPane(contentPane);
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

        btnImage.setIcon(btnChooseRectangular.getIcon());

        btnChooseRectangular.addActionListener(e -> {
            btnImage.setIcon(btnChooseRectangular.getIcon());
        });
        btnChooseCylinder.addActionListener(e -> {
            btnImage.setIcon(btnChooseCylinder.getIcon());
        });
        btnChooseCone.addActionListener(e -> {
            btnImage.setIcon(btnChooseCone.getIcon());
        });


        this.setLocation(600, 400);
        this.pack();
        this.setVisible(true);
    }

    private void onOK() {
        try {
            int x = Integer.parseInt(tfX.getText().trim());
            int y = Integer.parseInt(tfY.getText().trim());
            int z = Integer.parseInt(tfZ.getText().trim());

            int a = Integer.parseInt(tfLength.getText().trim());
            int b = Integer.parseInt(tfWidth.getText().trim());
            int h = Integer.parseInt(tfHeight.getText().trim());

            if (btnImage.getIcon() == btnChooseRectangular.getIcon()) {
                System.out.println("rect");
                listener.onDrawRectangular(new Point3D(x, y, z), a, b, h);
            } else if (btnImage.getIcon() == btnChooseCylinder.getIcon()) {
                System.out.println("cylinder");
                listener.onDrawCylinder(new Point3D(x, y, z), a, b, h);
            } else if (btnImage.getIcon() == btnChooseCone.getIcon()) {
                System.out.println("cone");
                listener.onDrawCone(new Point3D(x, y, z), a, b, h);
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
