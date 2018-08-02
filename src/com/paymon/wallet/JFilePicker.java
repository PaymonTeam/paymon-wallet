package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class JFilePicker extends JPanel {
    private String textFieldLabel;
    private String buttonLabel;

    private JLabel label;
    public JTextField textField;
    public JButton button;

    private JFileChooser fileChooser;

    private int mode;
    public static final int MODE_OPEN = 1;
    public static final int MODE_SAVE = 2;

    public JFilePicker(JTextField textField, JButton button) {
        fileChooser = new JFileChooser();

        this.textField = textField;
        this.button = button;

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });


    }

    private void buttonActionPerformed(ActionEvent evt) {
        if (mode == MODE_OPEN) {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        } else if (mode == MODE_SAVE) {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
    }


    public void addFileTypeFilter(String extension, String description) {
        FileTypeFilter filter = new FileTypeFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getSelectedFilePath() {
        return textField.getText();
    }

    public File getSelectedFile() {
        return fileChooser.getSelectedFile();
    }

    public void setTextField(String text){
        if(text != null) {
            textField.setText(text);
        }else{
            System.out.println("Null string");
        }
    }
}
