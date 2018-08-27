package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class LoadExistWallet extends JFrame {
    private JPanel panel;
    private JPanel fileExplorerPanel;
    private JPanel messagePanel;
    private JPanel buttPanel;
    private JPanel messageFileExplorerPanel;
    private JPanel firstPanel;
    private JPanel passFieldPanel;
    private JPanel fieldsPanel;
    public JPanel processPanel;

    private JButton oButton;
    public JButton loadButton;
    public JButton backButton;
    private JButton browseButton;

    private JPasswordField password;

    private JLabel passwordLable;
    private JLabel enterPasswordLabel;
    private JLabel loadWalletLabel;
    private JLabel processLabel;
    private JLabel pickFileLabel;
    private JLabel exceptionLabel;

    private JTextField filePath;

    private JFilePicker filePicker;

    public JProgressBar progressBar;

    private Handler handler;

    private boolean oButtonIsClicked = true;

    private int backgroundColor = 0x323232;
    private int red = 0xe15754;
    private int labelColor = 0xc2c2c2;

    public LoadExistWallet() {

        initComponents();
        visibleSetter();
        setResizable(false);

        oButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oButtonIsClicked = !oButtonIsClicked;
                handler.oButtonHandler(oButtonIsClicked, oButton, password);
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);
    }

    private void visibleSetter() {
        messagePanel.setVisible(true);
        messagePanel.setOpaque(false);

        messageFileExplorerPanel.setVisible(true);
        messageFileExplorerPanel.setOpaque(false);

        exceptionLabel.setVisible(true);

        fileExplorerPanel.setOpaque(false);

        firstPanel.setOpaque(false);

        passFieldPanel.setOpaque(false);

        buttPanel.setOpaque(false);

        fileExplorerPanel.setOpaque(false);

        fieldsPanel.setOpaque(false);
    }

    private void initComponents() {
        handler = new Handler();
        setContentPane(panel);
        setTitle("Wallet Load");
        setSize(450, 650);

        panel.setBackground(new Color(backgroundColor));

        filePicker = new JFilePicker(filePath, browseButton);
        filePicker.setMode(JFilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(".json", "JSON Files");
        filePicker.setOpaque(true);
        filePicker.setBackground(new Color(backgroundColor));

        exceptionLabel.setForeground(new Color(backgroundColor));


        loadWalletLabel.setForeground(new Color(labelColor));

        enterPasswordLabel.setForeground(new Color(labelColor));

        handler.oButtonHandler(oButtonIsClicked, oButton, password);

        showLoadPane();

        setFonts();

    }

    private void setFonts() {
        InputStream isArkhip = CreateNewWallet.class.getResourceAsStream("/fonts/Arkhip_font.ttf");
        InputStream isRoboto = CreateNewWallet.class.getResourceAsStream("/fonts/Roboto-Thin.ttf");
        try {
            Font arkhip = Font.createFont(Font.TRUETYPE_FONT, isArkhip);
            Font roboto = Font.createFont(Font.TRUETYPE_FONT, isRoboto);
            arkhip = arkhip.deriveFont(28f);
            roboto = roboto.deriveFont(20f);
            loadWalletLabel.setFont(arkhip);
            filePath.setFont(roboto);
            password.setFont(roboto);
            browseButton.setFont(roboto);
            loadButton.setFont(roboto);
            backButton.setFont(roboto);
            pickFileLabel.setFont(roboto);
            enterPasswordLabel.setFont(roboto);
            exceptionLabel.setFont(roboto);
            if (processLabel != null) {
                processLabel.setFont(roboto);
            } else {
                System.out.println("Nullable processLabel");
            }
        } catch (Exception ex) {
            System.out.println("Incorrect font");
        }
    }

    public boolean loadButtonHandler() {

        boolean fileExplorerIsOk;

        if (handler.fileIsCorrect(filePicker.getSelectedFilePath(), "json")) {
            File f = new File(filePicker.getSelectedFilePath());
            if (f.exists() && !f.isDirectory()) {
                System.out.println("Selected .json file: " + filePicker.getSelectedFilePath());
                exceptionLabel.setForeground(new Color(backgroundColor));
                fileExplorerIsOk = true;
            } else {
                exceptionLabel.setText("File in selected path does not exist");
                exceptionLabel.setForeground(new Color(red));
                fileExplorerIsOk = false;
            }
        } else {
            System.out.println("Wrong file selected");
            exceptionLabel.setText("Wrong file type selected");
            exceptionLabel.setForeground(new Color(red));
            fileExplorerIsOk = false;
        }
        return fileExplorerIsOk;
    }

    public String getPassword() {
        return new String(password.getPassword());
    }

    public String getPath() {
        return filePicker.getSelectedFilePath();
    }

    public void showExceptionMessage(boolean flag, String message) {
        if (message != null) {
            exceptionLabel.setText(message);
        }
        if (flag) {
            exceptionLabel.setForeground(new Color(red));
        } else {
            exceptionLabel.setForeground(panel.getForeground());
        }
    }

    public void saveFilePath(String path) {
        try (FileOutputStream fos = new FileOutputStream("jsonPath")) {
            byte[] buffer = path.getBytes();

            fos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String loadFilePath() {

        try (FileInputStream fin = new FileInputStream("jsonPath")) {
            byte[] buffer = new byte[fin.available()];

            fin.read(buffer, 0, fin.available());

            return new String(buffer);
        } catch (IOException ex) {
            return null;
        }
    }

    public void setFileExplorerText(String text) {
        if (text != null) {
            filePicker.setTextField(text);
        } else {
            System.out.println("Null string");
        }
    }

    public void clear() {
        password.setText("");
        exceptionLabel.setForeground(new Color(backgroundColor));
    }

    public void showLoadPane() {
        processPanel = new JPanel(new GridBagLayout()) {
            public void paintComponent(Graphics g) {
                g.setColor(new Color(50, 50, 50, 200));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        processPanel.setOpaque(false);

        processLabel = new JLabel("Please wait, it may take a few moments");
        processLabel.setForeground(new Color(labelColor));
        processLabel.setBackground(new Color(0x4A4A4A));

        processPanel.add(processLabel);
        processPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                me.consume();
            }
        });
        progressBar = new JProgressBar();
        processPanel.add(progressBar);
    }

    public void repaintMainPanel() {
        getContentPane().repaint();
        getContentPane().revalidate();
    }
}


