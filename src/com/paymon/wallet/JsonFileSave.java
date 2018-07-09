package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;

public class JsonFileSave extends JFrame{
    private JPanel panel;
    private JPanel titlePanel;
    private JPanel jsonSavePanel;
    private JPanel checkBoxPanel;
    private JPanel navigationPanel;

    private JCheckBox agreeCheckBox;

    public JButton backButton;
    public JButton nextButton;

    private JLabel titleLabel;
    private JLabel massageLabel;

    public JsonFileSave(){

        initComponents();
        visibleSetter();

    }
    private void initComponents(){
        setContentPane(panel);
        setSize(480, 480);
    }
    private void visibleSetter(){

        titlePanel.setOpaque(false);

        jsonSavePanel.setOpaque(false);

        checkBoxPanel.setOpaque(false);

        navigationPanel.setOpaque(false);

        titleLabel.setForeground(Color.WHITE);

        agreeCheckBox.setForeground(Color.WHITE);

        massageLabel.setForeground(new Color(51, 181, 229));
        massageLabel.setVisible(true);

        panel.setBackground(new Color(51, 181, 229));
    }
    public boolean checkBoxHandler(){
        if(agreeCheckBox.isSelected()){

            massageLabel.setForeground(new Color(51, 181, 229));

        }else{
            massageLabel.setForeground(Color.RED);
        }
        return agreeCheckBox.isSelected();
    }
}
