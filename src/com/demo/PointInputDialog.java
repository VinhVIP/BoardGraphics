package com.demo;

import com.demo.listeners.DialogListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PointInputDialog extends JDialog {

    private DialogListener listener;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel dialogTitle;
    private JTextField tfX;
    private JTextField tfY;

    public PointInputDialog(DialogListener listener) {
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
            int x = Integer.parseInt(tfX.getText().trim());
            int y = Integer.parseInt(tfY.getText().trim());
            listener.onPoint(x, y);

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Tọa độ nhập không hợp lệ", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
