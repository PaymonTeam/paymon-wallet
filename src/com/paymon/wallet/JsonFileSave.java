package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class JsonFileSave extends JFrame {
    private JPanel panel;
    private JPanel titlePanel;
    private JPanel jsonSavePanel;
    private JPanel checkBoxPanel;
    private JPanel buttPanel;

    private JCheckBox agreeCheckBox;

    public JButton backButton;
    public JButton nextButton;

    private JLabel title1Label;
    private JLabel massageLabel;
    private JLabel pickFileLabel;

    private JButton browseButton;
    private JTextField filePath;

    private JFilePicker filePicker;

    private File json;

    private int backgroundColor = 0x323232;
    private int red = 0xe15754;
    private int labelColor = 0xc2c2c2;

    public JsonFileSave() {

        initComponents();
        visibleSetter();
        setResizable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);

    }

    private void initComponents() {
        setContentPane(panel);
        setSize(450, 650);
        setFileExplorer();
        setFonts();
        title1Label.setForeground(new Color(labelColor));

        agreeCheckBox.setForeground(new Color(labelColor));

        massageLabel.setForeground(new Color(backgroundColor));

        agreeCheckBox.setForeground(new Color(labelColor));

        panel.setBackground(new Color(backgroundColor));

    }

    private void visibleSetter() {

        titlePanel.setOpaque(false);

        buttPanel.setOpaque(false);

        jsonSavePanel.setOpaque(false);

        checkBoxPanel.setOpaque(false);

        massageLabel.setVisible(true);

    }

    private void setFonts() {
        InputStream isArkhip = CreateNewWallet.class.getResourceAsStream("/fonts/Arkhip_font.ttf");
        InputStream isRoboto = CreateNewWallet.class.getResourceAsStream("/fonts/Roboto-Thin.ttf");
        try {
            Font arkhip = Font.createFont(Font.TRUETYPE_FONT, isArkhip);
            Font roboto = Font.createFont(Font.TRUETYPE_FONT, isRoboto);
            arkhip = arkhip.deriveFont(22f);
            roboto = roboto.deriveFont(20f);
            title1Label.setFont(arkhip);
            browseButton.setFont(roboto);
            filePath.setFont(roboto);
            nextButton.setFont(roboto);
            backButton.setFont(roboto);
            pickFileLabel.setFont(roboto);
            massageLabel.setFont(roboto);
            agreeCheckBox.setFont(roboto);
        } catch (Exception ex) {
            System.out.println("Incorrect font");
        }
    }

    public boolean checkBoxHandler() {
        if (agreeCheckBox.isSelected()) {
            massageLabel.setForeground(new Color(backgroundColor));
        } else {
            massageLabel.setForeground(new Color(red));
        }
        return agreeCheckBox.isSelected();
    }

    public void showExceptionMessage(boolean flag, String message) {
        if (message != null) {
            massageLabel.setText(message);
        }
        if (flag) {
            massageLabel.setForeground(new Color(red));
        } else {
            massageLabel.setForeground(new Color(backgroundColor));
        }
    }

    public boolean filePathHandler() {
        String path = filePicker.getSelectedFilePath();
        if (path.equals("")) {
            return false;
        } else {
           /* Path isPath = Paths.get(path);
            if (Files.exists(isPath)) {
               /* File file = new File(path);
                return file.isDirectory();
               return true;
            } else {
                return false;
            }*/
            return true;
        }
    }

    private void setFileExplorer() {
        filePicker = new JFilePicker(filePath, browseButton);
        filePicker.setMode(JFilePicker.MODE_SAVE);
        filePicker.addFileTypeFilter(".json", "JSON Files");
        filePicker.setOpaque(true);
        filePicker.setBackground(new Color(backgroundColor));
    }


    public String getFilePath() {
        return filePicker.getSelectedFilePath();
    }

    public void writeFile() throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(json);
            os = new FileOutputStream(filePicker.getSelectedFile());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }
}
