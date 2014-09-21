package main.de.jezlynn.medialist;

import main.de.jezlynn.medialist.helper.FileChooser;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Michael on 24.08.2014.
 */
public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("");
        FileChooser panel = new FileChooser();
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
        frame.getContentPane().add(panel, "Center");
        frame.setSize(panel.getPreferredSize());
        frame.setVisible(true);
    }
}
