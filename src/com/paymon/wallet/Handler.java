package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;

public class Handler {
    public void oButtonHandler(boolean cliked, JButton button, JPasswordField field) {
        if (!cliked) {
            Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButton.png")).getImage();
            button.setIcon(new ImageIcon(oButtonImage));
            field.setEchoChar((char) 0);
        } else {
            Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButtonClicked.png")).getImage();
            button.setIcon(new ImageIcon(oButtonImage));
            field.setEchoChar('*');
        }
    }

    public void passFieldHandler(JPasswordField passwordField, JPanel messagePanel, JLabel incorrectPassMessage, JFrame frame) {
        //FIX getText()
        int len = passwordField.getText().trim().length();
        if (len < 9) {
            messagePanel.setVisible(true);
            incorrectPassMessage.setVisible(true);
            frame.pack();
        } else {
            messagePanel.setVisible(false);
            incorrectPassMessage.setVisible(false);
            frame.pack();
        }
    }

    public boolean fileIsCorrect(String path, String fileFormat) {
        if(path == null){
            return false;
        }
        String[] modules = path.split("/");
        for (String module : modules) {
            for (char c : module.toCharArray()) {
                if ((c >= 'А' && c <= 'я') || c == 'Ё' || c == 'ё' || c == 'і' || c == 'І' || c == 'ї' || c == 'Ї') {
                    return false;
                }
            }
        }
        String[] fileModules = modules[modules.length - 1].split("\\.");
        if (fileModules.length != 0) {
            if (fileModules[fileModules.length - 1].equals(fileFormat)) {
                return true;
            }
        }

        return false;
    }
}
