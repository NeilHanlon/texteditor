package com.neilhanlon;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Neil on 11/2/2014.
 */
public class FileInstance extends JPanel {

    private static int tabCount;

    private String fileText;
    private File file;
    /*
     * 0 = uninitialized
     * 1 = opened real file unchanged
     * 2 = opened real file changed
     * 3 = ????
     * 4 = ????
     * 5 = temporary new file
     */
    private int status = 0;

    private JTextArea textarea;
    private JScrollPane scrollpane;
    private FileInstancePanel panel;

    //private static Editor editor = new Editor();
    public FileInstance() {
        try {
            String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            this.file = Files.createTempFile(TextEditor.tempDirectory,date,".txt").toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fileText = "";
        this.status = 5;

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
        this.status = 1;

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

    public boolean save(String fileText) {
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
    public JTextArea getTextArea()
    {
        return textarea;
    }

    public void saveAs(String fileText) {
        final JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(this.file);
        chooser.showSaveDialog(getParent());
    }
}
