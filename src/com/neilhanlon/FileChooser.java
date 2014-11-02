package com.neilhanlon;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Created by Neil on 11/2/2014.
 */
public class FileChooser extends JFileChooser implements ActionListener {
    FileInstance fileInstance = null;
    public FileChooser()
    {
        super("Open File");

        final JFileChooser fc = new JFileChooser();
        int returnV = fc.showOpenDialog(this);

        if (returnV == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            FileInstance fileInstance = new FileInstance(file);
        }

    }
    public void actionPerformed(ActionEvent e)
    {
        System.out.println(e.getSource());
    }
    public static void main(String[] args)
    {
        new FileChooser();
    }
}
