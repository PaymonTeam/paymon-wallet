package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;


public class CreateNewWallet extends JFrame {

    public JPanel panel;
    private JPanel firstPanel;
    private JPanel passFieldPanel;
    private JPanel buttPanel;
    private JPanel messagePanel;

    private JPasswordField password;

    public JButton loadWalletButton;
    private JButton oButton;
    public JButton createNewWalletButton;

    private JLabel createNewWalletLabel;
    private JLabel enterPasswordLabel;
    private JLabel incorrectPassMessage;

    private Handler handler;

    private boolean oButtonIsClicked = true;

    public CreateNewWallet() {

        initComponents();
        visibleSetter();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //SetBackgroundImage();
        } catch (Exception e) {
            e.printStackTrace();
        }


        setDefaultLookAndFeelDecorated(false);

        oButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oButtonIsClicked = !oButtonIsClicked;
                handler.oButtonHandler(oButtonIsClicked, oButton, password);
            }
        });
    }

    private void visibleSetter() {
        // messagePanel.setVisible(true);
        incorrectPassMessage.setVisible(true);
        firstPanel.setOpaque(false);
        passFieldPanel.setOpaque(false);
        buttPanel.setOpaque(false);
        setVisible(true);
    }

    public void initComponents() {
        setContentPane(panel);
        handler = new Handler();
        setSize(480, 480);
        setTitle("Paymon Wallet");
        setFonts();
        incorrectPassMessage.setForeground(new Color(50, 50, 50));
        panel.setBackground(new Color(50, 50, 50));
        handler.oButtonHandler(oButtonIsClicked, oButton, password);
    }
    private void setFonts(){
        InputStream isArkhip = CreateNewWallet.class.getResourceAsStream("/fonts/Arkhip_font.ttf");
        InputStream isRoboto = CreateNewWallet.class.getResourceAsStream("/fonts/Roboto-Thin.ttf");
        try {
            Font arkhip = Font.createFont(Font.TRUETYPE_FONT, isArkhip);
            Font roboto = Font.createFont(Font.TRUETYPE_FONT, isRoboto);
            arkhip = arkhip.deriveFont(28f);
            roboto = roboto.deriveFont(20f);
            createNewWalletLabel.setFont(arkhip);
            createNewWalletButton.setFont(roboto);
            loadWalletButton.setFont(roboto);
            enterPasswordLabel.setFont(roboto);
            incorrectPassMessage.setFont(roboto);
        }catch (Exception ex){
            System.out.println("Incorrect font");
        }



    }
    public void repaintMainPanel() {
        getContentPane().repaint();
        getContentPane().revalidate();
    }

    public boolean createButtonHandler() {
        return handler.passFieldHandler(password, incorrectPassMessage, new Color(50, 50, 50));
    }

    public String getPassword() {
        return new String(password.getPassword());
    }
    public void clear(){
        password.setText("");
    }

}


