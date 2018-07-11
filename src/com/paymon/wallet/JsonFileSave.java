package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class JsonFileSave extends JFrame {
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

    private JFilePicker filePicker;

    private File json;

    public JsonFileSave() {

        initComponents();
        visibleSetter();

    }

    private void initComponents() {
        setContentPane(panel);
        setSize(480, 480);
        setFileExplorer();

        titleLabel.setForeground(Color.WHITE);

        agreeCheckBox.setForeground(Color.WHITE);

        massageLabel.setForeground(new Color(51, 181, 229));

        panel.setBackground(new Color(51, 181, 229));

    }

    private void visibleSetter() {

        titlePanel.setOpaque(false);

        jsonSavePanel.setOpaque(false);

        checkBoxPanel.setOpaque(false);

        massageLabel.setVisible(true);

    }

    public boolean checkBoxHandler() {
        if (agreeCheckBox.isSelected()) {

            massageLabel.setForeground(new Color(51, 181, 229));

        } else {
            massageLabel.setForeground(Color.RED);
        }
        return agreeCheckBox.isSelected();
    }

    private void setFileExplorer() {
        filePicker = new JFilePicker("Select a folder", "Browse...");
        filePicker.setMode(JFilePicker.MODE_SAVE);
        filePicker.addFileTypeFilter(".json", "JSON Files");
        filePicker.setOpaque(true);
        filePicker.setBackground(new Color(51, 181, 229));
        jsonSavePanel.add(filePicker, BorderLayout.CENTER);
    }

    public void setFile(File f) {
        json = f;
    }

    public File getFile() {
        return json;
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
