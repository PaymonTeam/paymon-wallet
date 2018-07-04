package com.paymon.wallet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CreateNewWallet extends JFrame {
    private JButton oButton;
    private boolean oButtonIsClicked = true;
    private JPanel panel;
    public JButton createNewWalletButton;
    private JPasswordField password;
    private JLabel incorrectPassMessage;
    private JPanel firstPanel;
    private JPanel passFieldPanel;
    private JPanel buttPanel;
    private JPanel messagePanel;
    private JButton loadWalletButton;
    private JLabel createNewWalletLabel;
    private JLabel enterPasswordLabel;
    private JPanel imgPanel;

    public CreateNewWallet() {
        setContentPane(panel);
        setTitle("Wallet Creator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SetBackgroundImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        incorrectPassMessage.setForeground(Color.RED);
        createNewWalletLabel.setForeground(Color.WHITE);
        enterPasswordLabel.setForeground(Color.WHITE);
        if(imgPanel != null) {
            setSize(imgPanel.getWidth(), imgPanel.getHeight());
        }
        visibleSetter();
        oButtonHandler();

        setDefaultLookAndFeelDecorated(false);

        createNewWalletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passFieldHandler();
            }

        });
        oButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oButtonIsClicked = !oButtonIsClicked;
                oButtonHandler();
            }
        });
        loadWalletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO handle load of exist wallet
            }
        });
    }

    private void SetBackgroundImage() {
        imgPanel = new ImagePanel(new ImageIcon(this.getClass().getResource("/background.png")).getImage());
       // setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("/panel_icon.png")));

        if (imgPanel != null) {
            getContentPane().add(imgPanel);
        } else {
            System.out.println("incorrect background image, check the path for this image and if it exists.");
        }
    }
    private void oButtonHandler(){
        if (!oButtonIsClicked){
            Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButton.png")).getImage();
            oButton.setIcon(new ImageIcon(oButtonImage));
            password.setEchoChar((char)0);
        }else{
            Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButtonClicked.png")).getImage();
            oButton.setIcon(new ImageIcon(oButtonImage));
            password.setEchoChar('*');
        }
    }
    private void visibleSetter(){
        messagePanel.setVisible(false);
        incorrectPassMessage.setVisible(false);
        firstPanel.setOpaque(false);
        passFieldPanel.setOpaque(false);
        buttPanel.setOpaque(false);
        setVisible(true);
    }

    private void passFieldHandler(){
        //FIX getText()
        int len = password.getText().trim().length();
        if (len < 9) {
            messagePanel.setVisible(true);
            incorrectPassMessage.setVisible(true);
        }else {
            messagePanel.setVisible(false);
            incorrectPassMessage.setVisible(false);
        }
    }

}


