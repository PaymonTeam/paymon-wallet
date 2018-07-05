package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;

public class Handler {
    public void oButtonHandler(boolean cliked, JButton button, JPasswordField field){
        if (!cliked){
            Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButton.png")).getImage();
            button.setIcon(new ImageIcon(oButtonImage));
            field.setEchoChar((char)0);
        }else{
            Image oButtonImage = new ImageIcon(this.getClass().getResource("/oButtonClicked.png")).getImage();
            button.setIcon(new ImageIcon(oButtonImage));
            field.setEchoChar('*');
        }
    }
    public void setCenterAlignment(JComponent c){

    }
}
