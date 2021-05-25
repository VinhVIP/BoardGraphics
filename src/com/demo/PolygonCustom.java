package com.demo;

import com.demo.listeners.DialogListener;
import com.demo.models.Point2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PolygonCustom extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfTotalVertex;
    private JButton btnGen;
    private JPanel inputPanel;

    private int totalVertex;
    private DialogListener listener;

    private final int MAX_POINTS = 8;

    private Panel[] panels = new Panel[MAX_POINTS];
    private JLabel[] labels = new JLabel[MAX_POINTS];
    private JTextField[] tfX = new JTextField[MAX_POINTS];
    private JTextField[] tfY = new JTextField[MAX_POINTS];

    public PolygonCustom(DialogListener listener) {
        this.listener = listener;

        setTitle("Polygon");
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


        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));


        for (int i = 0; i < panels.length; i++) {
            char c = (char) ('A' + i);
            labels[i] = new JLabel(c + "");
            tfX[i] = new JTextField(5);
            tfY[i] = new JTextField(5);

            panels[i] = new Panel(new FlowLayout(FlowLayout.CENTER));
            panels[i].add(labels[i]);
            panels[i].add(tfX[i]);
            panels[i].add(tfY[i]);
        }

        btnGen.addActionListener(e -> {
            try {
                int n = Integer.parseInt(tfTotalVertex.getText().trim());

                if (n < 1 || n > MAX_POINTS) {
                    JOptionPane.showMessageDialog(null, "Số đỉnh tối thiểu là 1, tối đa là 8", "Thông báo", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (n < totalVertex) {
                        while (inputPanel.getComponentCount() > n) {
                            inputPanel.remove(inputPanel.getComponentCount() - 1);
                        }
                    } else if (n > totalVertex) {
                        int m = totalVertex;
                        while (m < n) {
                            inputPanel.add(panels[m]);
                            m++;
                        }
                    }
                    totalVertex = n;
                    inputPanel.updateUI();
                    System.out.println(inputPanel.getComponentCount());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Giá trị nhập không hợp lệ", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }


        });


        setResizable(false);
        setLocation(500, 300);
        pack();
        setVisible(true);
    }

    private void onOK() {
        if (totalVertex > 0) {
            Point2D[] points = new Point2D[totalVertex];
            int x, y;
            try {
                System.out.println("again");
                for (int i = 0; i < totalVertex; i++) {
                    x = Integer.parseInt(tfX[i].getText().trim());
                    y = Integer.parseInt(tfY[i].getText().trim());
                    points[i] = new Point2D(x, y);
                }

                boolean isSame = false;
                char u = 'A', v = 'A';
                for (int i = 0; i < totalVertex - 1; i++) {
                    for (int j = i + 1; j < totalVertex; j++) {
                        if (points[i].equals(points[j])) {
                            isSame = true;
                            u += i;
                            v += j;
                            break;
                        }
                    }
                    if (isSame) break;
                }
                if (isSame) {
                    JOptionPane.showMessageDialog(null, "Điểm " + u + " và " + v + " trùng nhau!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                } else {
                    listener.onDrawPolygon(points);
                    dispose();
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Giá trị tọa độ nhập không hợp lệ", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
