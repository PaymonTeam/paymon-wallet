package com.paymon.wallet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CreateNewWallet extends JFrame {
    private JButton oButton;
    private boolean oButtonIsClicked = false;
    private JPanel panel;
    public JButton createNewWalletButton;
    private JPasswordField password;
    private JLabel incorrectPassMessage;
    private JPanel firstPanel;
    private JPanel passFieldPanel;
    private JPanel buttPanel;
    private JPanel messagePanel;
    private JButton loadWalletButton;
    private ImagePanel imgPanel;

    public CreateNewWallet() {
        setContentPane(panel);
        setTitle("Wallet Creator");
        incorrectPassMessage.setForeground(Color.red);
        incorrectPassMessage.setVisible(false);
        /*firstPanel.setVisible(false);
        passFieldPanel.setVisible(false);
        buttPanel.setVisible(false);*/
        if (!oButtonIsClicked){
            Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButton.png")).getImage();
            oButton.setIcon(new ImageIcon(oButtonImage));
        }else{
            Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButtonClicked.png")).getImage();
            oButton.setIcon(new ImageIcon(oButtonImage));
        }
        messagePanel.setVisible(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SetBackgroundImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSize(imgPanel.getWidth(), imgPanel.getHeight());
        setVisible(true);
        setDefaultLookAndFeelDecorated(false);

        createNewWalletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //FIX getText()
                int len = password.getText().trim().length();
                if (len < 9) {
                    messagePanel.setVisible(true);
                    incorrectPassMessage.setVisible(true);
                }
            }

        });
        oButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oButtonIsClicked = !oButtonIsClicked;
                if (!oButtonIsClicked){
                    Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButton.png")).getImage();
                    oButton.setIcon(new ImageIcon(oButtonImage));
                }else{
                    Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButtonClicked.png")).getImage();
                    oButton.setIcon(new ImageIcon(oButtonImage));
                }

            }
        });
        loadWalletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void SetBackgroundImage() {
        imgPanel = new ImagePanel(new ImageIcon(this.getClass().getResource("/paymon_1.png")).getImage());
        Container c = this.getContentPane();
        if (imgPanel != null) {
            c.add(imgPanel);
        } else {
            System.out.println("incorrect background image, check the path for this image and if it exists.");
        }
    }

}

