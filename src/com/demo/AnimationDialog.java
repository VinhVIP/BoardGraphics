package com.demo;

import com.demo.motions.Config;

import javax.swing.*;
import java.awt.event.*;

public class AnimationDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfEnemyScale;
    private JTextField tfEnemySpeedX;
    private JTextField tfEnemySpeedX2;
    private JTextField tfBoomScale;
    private JTextField tfEnemyRotateAngle;

    public AnimationDialog() {
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


        setData();

        pack();
        setVisible(true);

    }

    private void setData() {
        tfEnemyScale.setText(Config.enemyScale + "");
        tfEnemySpeedX.setText(Config.enemySpeedX + "");
        tfEnemySpeedX2.setText(Config.enemySpeedX2 + "");
        tfEnemyRotateAngle.setText(Config.enemyRotate+"");

        tfBoomScale.setText(Config.bombScale + "");
    }

    private void onOK() {
        Config.enemyScale = Double.parseDouble(tfEnemyScale.getText());
        Config.enemySpeedX = Integer.parseInt(tfEnemySpeedX.getText());
        Config.enemySpeedX2 = Integer.parseInt(tfEnemySpeedX2.getText());
        Config.enemyRotate = Integer.parseInt(tfEnemyRotateAngle.getText());

        Config.bombScale = Double.parseDouble(tfBoomScale.getText());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
