package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;

public class LoadExistWallet extends JFrame {
    private JPanel panel;
    private boolean oButtonIsClicked = false;
    private JButton loadButton;
    private JPanel passFieldPanel;
    private JButton oButton;
    private JPasswordField password;
    private ImagePanel imgPanel;

    public LoadExistWallet() {
        setContentPane(panel);
        setTitle("Wallet Load");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (!oButtonIsClicked){
            Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButton.png")).getImage();
            oButton.setIcon(new ImageIcon(oButtonImage));
        }else{
            Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButtonClicked.png")).getImage();
            oButton.setIcon(new ImageIcon(oButtonImage));
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SetBackgroundImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSize(imgPanel.getWidth(), imgPanel.getHeight());
        setDefaultLookAndFeelDecorated(false);
        //pack();
    }

    public void SetBackgroundImage() {
        imgPanel = new ImagePanel(new ImageIcon(this.getClass().getResource("/paymon_1.png")).getImage());
        Container c = this.getContentPane();
        if (imgPanel != null) {
            c.add(imgPanel);
        } else {
            System.out.println("incorrect background image, check the path for this image and if it exists.");
        }
    }

}


