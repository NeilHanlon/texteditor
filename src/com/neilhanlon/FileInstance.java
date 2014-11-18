package com.neilhanlon;

import com.neilhanlon.Logger.Lumberjack;

import javax.swing.*;
import javax.xml.soap.Text;
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

    private Lumberjack logger = TextEditor.logger;

    private JTextArea textarea;
    private JScrollPane scrollpane;
    private FileInstancePanel panel;

    //private static Editor editor = new Editor();
    public FileInstance() {
        try {
            String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            this.file = Files.createTempFile(TextEditor.tempDirectory, date, ".txt").toFile();
            logger.write("Opened new temporary file at: " + file.getPath().toString());
        } catch (IOException e) {
            logger.write(e);
        }
        this.fileText = "";
        this.status = 5;
        logger.write("status for FileInstance " + file.hashCode() + " is now at " + status);

        textarea = new JTextArea();
        textarea.setText(fileText);

        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);

        scrollpane = new JScrollPane(textarea);
        scrollpane.setViewportView(textarea);

        panel = new FileInstancePanel();
        panel.setFileInstance(this);
        panel.setLayout(new BorderLayout());
        panel.add(scrollpane, BorderLayout.CENTER);

        String label = "newfile";

        TextEditor.editor.addTab(label, panel);
    }

    public FileInstance(File file) {
        this.fileText = openFile(file);
        this.file = file;
        this.status = 1;

        logger.write("status for FileInstance " + file.hashCode() + " is now at " + status);

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
                fileText += line + TextEditor.lineSeparator;
            }
            logger.write("read file " + file.getPath().toString() + " into FileInstance " + file.hashCode());
        } catch (IOException x) {
            logger.write(x);
        }
        return fileText;
    }

    public boolean save(String fileText) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file.toString()), "utf-8"
            ));
            writer.write(fileText);
            writer.flush();
            writer.close();
            logger.write("Wrote to file: "+ file.toString());
        } catch (IOException e) {
            logger.write(e);
            return false;
        } finally {
            return true;
        }
    }
    public boolean save(File file,String fileText)
    {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file.toString()), "utf-8"
            ));
            writer.write(fileText);
            writer.flush();
            writer.close();
            logger.write("Wrote to file: "+ file.toString());
        } catch (IOException e) {
            logger.write(e);
            return false;
        } finally {
            return true;
        }
    }

    public JTextArea getTextArea() {
        return textarea;
    }

    public void saveAs(String fileText) {
        final JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(this.file);
        int returnV = chooser.showSaveDialog(getParent());
        if(returnV == JFileChooser.APPROVE_OPTION)
        {
            File saveFile = chooser.getSelectedFile().toPath().toFile();
            save(saveFile,fileText);
        }
    }
    public void setStatus(int newStatus) { this.status = newStatus; }
    public int getStatus() { return this.status; }
}
