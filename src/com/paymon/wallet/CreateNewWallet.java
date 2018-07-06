package com.paymon.wallet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CreateNewWallet extends JFrame {
    private JFrame frame = this;
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

    private Handler handler;

    public CreateNewWallet() {
        setContentPane(panel);
        handler = new Handler();
        setContentPane(panel);
        setTitle("Paymon Wallet");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //SetBackgroundImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        incorrectPassMessage.setForeground(Color.RED);
        createNewWalletLabel.setForeground(Color.WHITE);
        enterPasswordLabel.setForeground(Color.WHITE);
        panel.setBackground(new Color(51,181,229));
        if(imgPanel != null) {
            setSize(imgPanel.getWidth(), imgPanel.getHeight());
        }
        visibleSetter();

        handler.oButtonHandler(oButtonIsClicked, oButton, password);

        setDefaultLookAndFeelDecorated(false);

        createNewWalletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.passFieldHandler(password, messagePanel, incorrectPassMessage, frame);
            }

        });
        oButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oButtonIsClicked = !oButtonIsClicked;
                handler.oButtonHandler(oButtonIsClicked, oButton, password);
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

    private void visibleSetter(){
        messagePanel.setVisible(false);
        incorrectPassMessage.setVisible(false);
        firstPanel.setOpaque(false);
        passFieldPanel.setOpaque(false);
        buttPanel.setOpaque(false);
        setVisible(true);
    }


}


