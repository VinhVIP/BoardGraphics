package com.demo;

import com.demo.listeners.DialogListener;
import com.demo.models.Point2D;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ScaleDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfScaleX;
    private JTextField tfScaleY;
    private JTextField tfX;
    private JTextField tfY;

    private DialogListener listener;

    public ScaleDialog(DialogListener listener) {
        this.listener = listener;

        setTitle("Dialog");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.setLocation(600, 400);
        this.pack();
        this.setVisible(true);
    }

    private void onOK() {
        try {
            int x = Integer.parseInt(tfX.getText());
            int y = Integer.parseInt(tfY.getText());
            double scaleX = Double.parseDouble(tfScaleX.getText().trim());
            double scaleY = Double.parseDouble(tfScaleY.getText().trim());

            listener.onScale(new Point2D(x, y), scaleX, scaleY);

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Thông tin nhập không hợp lệ", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
