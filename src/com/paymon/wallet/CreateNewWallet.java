package com.paymon.wallet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CreateNewWallet extends JFrame {

    private JPanel panel;
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

        createNewWalletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.passFieldHandler(password, incorrectPassMessage, new Color(51,181,229));
            }

        });
        oButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oButtonIsClicked = !oButtonIsClicked;
                handler.oButtonHandler(oButtonIsClicked, oButton, password);
            }
        });
    }

    private void visibleSetter(){
        messagePanel.setVisible(true);
        incorrectPassMessage.setVisible(true);
        firstPanel.setOpaque(false);
        passFieldPanel.setOpaque(false);
        buttPanel.setOpaque(false);
        setVisible(true);
    }
    public void initComponents(){
        setContentPane(panel);
        handler = new Handler();
        setSize(480, 480);
        setTitle("Paymon Wallet");
        incorrectPassMessage.setForeground(new Color(51,181,229));
        createNewWalletLabel.setForeground(Color.WHITE);
        enterPasswordLabel.setForeground(Color.WHITE);
        panel.setBackground(new Color(51,181,229));
        handler.oButtonHandler(oButtonIsClicked, oButton, password);
    }


}


