package com.neilhanlon;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Neil on 11/2/2014.
 */
public class FileInstance extends JPanel {

    private static int tabCount;

    private String fileText;
    private File file;

    private JTextArea textarea;
    private JScrollPane scrollpane;
    private JPanel panel;

    //private static Editor editor = new Editor();
    public FileInstance()
    {
        this.file = null;
        this.fileText = "";

        textarea = new JTextArea();
        textarea.setText(fileText);

        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);

        scrollpane = new JScrollPane(textarea);
        scrollpane.setViewportView(textarea);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollpane, BorderLayout.CENTER);

        String label = "newfile";

        TextEditor.editor.addTab(label, panel);
    }
    public FileInstance(File file)
    {
        this.fileText = openFile(file);
        this.file = file;

        textarea = new JTextArea();
        textarea.setText(fileText);

        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);

        scrollpane = new JScrollPane(textarea);
        scrollpane.setViewportView(textarea);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollpane, BorderLayout.CENTER);

        String label = getFileName(file);

        TextEditor.editor.addTab(label, panel);

    }
    private String getFileName(File file)
    {
        return file.toPath().getFileName().toString();
    }
    private String openFile(File file)
    {
        Charset charset = Charset.forName("UTF-8");
        String fileText = "";
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileText += line + "\n";
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return fileText;
    }
    public static void main(String[] args)
    {

    }
}
