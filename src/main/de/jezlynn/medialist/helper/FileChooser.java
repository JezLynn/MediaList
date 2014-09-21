package main.de.jezlynn.medialist.helper;

import main.de.jezlynn.medialist.handler.FileHandler;
import main.de.jezlynn.medialist.handler.RTFHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Created by Michael on 24.08.2014.
 */
public class FileChooser extends JPanel implements ActionListener {
    JButton go;
    JLabel selected;

    JFileChooser chooser;
    String chooserTitle;

    public FileChooser() {
        selected = new JLabel("None selected directory");
        add(selected);

        go = new JButton("Select Directory");
        go.addActionListener(this);
        add(go);
    }

    public static void main(String s[]) {
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

    public void actionPerformed(ActionEvent e) {
        int result;

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(chooserTitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            LogHelper.info("getSelectedFile() : "
                    + chooser.getSelectedFile());
            FileHandler handler = new FileHandler(chooser.getSelectedFile());
            selected.setText(chooser.getSelectedFile().getAbsolutePath());
            LogHelper.info(handler);
            RTFHandler rtfHandler = new RTFHandler();
            rtfHandler.createDocument(handler.getDirectorys());
            try {
                rtfHandler.writeFile();
            } catch (IOException e1) {
                LogHelper.error(e);
            }
        } else {
            LogHelper.info("No Selection ");
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }

}
