package com.neilhanlon;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Created by Neil on 11/2/2014.
 */
public class FileInstance extends JPanel {

    private static int tabCount;

    private String fileText;
    private File file;

    private JTextArea textarea;
    private JScrollPane scrollpane;
    private FileInstancePanel panel;

    //private static Editor editor = new Editor();
    public FileInstance() {
        this.file = null;
        this.fileText = "";

        textarea = new JTextArea();
        textarea.setText(fileText);

        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);

        scrollpane = new JScrollPane(textarea);
        scrollpane.setViewportView(textarea);

        panel = new FileInstancePanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollpane, BorderLayout.CENTER);

        String label = "newfile";

        TextEditor.editor.addTab(label, panel);
    }

    public FileInstance(File file) {
        this.fileText = openFile(file);
        this.file = file;

        textarea = new JTextArea();
        textarea.setText(fileText);

        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);

        scrollpane = new JScrollPane(textarea);
        scrollpane.setViewportView(textarea);

        panel = new FileInstancePanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollpane, BorderLayout.CENTER);

        panel.setFileInstance(this);

        String label = getFileName(file);

        TextEditor.editor.addTab(label, panel);

    }

    private String getFileName(File file) {
        return file.toPath().getFileName().toString();
    }

    private String openFile(File file) {
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

    public boolean saveFile(File file, String fileText) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file.toString()), "utf-8"
            ));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            writer.write(fileText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                return true;
            }
        }
    }

    public static void main(String[] args) {

    }

    public boolean saveAsFile(FileInstance file) {
        return false;
    }
}
