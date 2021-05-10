package com.demo;

import com.demo.listeners.DialogListener;

import javax.swing.*;
import java.awt.event.*;

public class ReflectionDialog extends JDialog implements ActionListener{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton radioPoint;
    private JRadioButton radioLine;
    private JTextField tfX;
    private JTextField tfY;
    private JTextField tfX1;
    private JTextField tfY1;
    private JTextField tfX2;
    private JTextField tfY2;

    private DialogListener listener;
    private ButtonGroup radioGroup;

    public ReflectionDialog(DialogListener listener) {
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

        radioPoint.addActionListener(this);
        radioLine.addActionListener(this);

        radioGroup = new ButtonGroup();
        radioGroup.add(radioPoint);
        radioGroup.add(radioLine);

        this.setLocation(600, 400);
        this.pack();
        this.setVisible(true);
    }

    private void onOK() {
        try {
            if(radioPoint.isSelected()){
                System.out.println("dm1");
                int x = Integer.parseInt(tfX.getText().trim());
                int y = Integer.parseInt(tfY.getText().trim());
                listener.onPointReflect(x, y);
            }else{
                System.out.println("dm2");
                int x1 = Integer.parseInt(tfX1.getText().trim());
                int y1 = Integer.parseInt(tfY1.getText().trim());
                int x2 = Integer.parseInt(tfX2.getText().trim());
                int y2 = Integer.parseInt(tfY2.getText().trim());
                listener.onTwoPointReflect(x1, y1, x2, y2);
            }

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Tọa độ nhập không hợp lệ", "Thông báo", JOptionPane.WARNING_MESSAGE);
        e.printStackTrace();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(radioPoint.isSelected()){
            tfX.setEnabled(true);
            tfY.setEnabled(true);

            tfX1.setEnabled(false);
            tfX2.setEnabled(false);
            tfY1.setEnabled(false);
            tfY2.setEnabled(false);
        }else{
            tfX.setEnabled(false);
            tfY.setEnabled(false);

            tfX1.setEnabled(true);
            tfX2.setEnabled(true);
            tfY1.setEnabled(true);
            tfY2.setEnabled(true);
        }
    }
}
