package com.neilhanlon;

import com.neilhanlon.Logger.Lumberjack;
import syntaxhighlight.SyntaxHighlighter;
import syntaxhighlight.SyntaxHighlighterPane;
import syntaxhighlight.Theme;
import syntaxhighlighter.SyntaxHighlighterParser;
import syntaxhighlighter.brush.*;
import syntaxhighlighter.theme.ThemeDefault;
import syntaxhighlighter.theme.ThemeEmacs;
import syntaxhighlighter.theme.ThemeFadeToGrey;
import syntaxhighlighter.theme.ThemeRDark;

import javax.swing.*;
import javax.xml.soap.Text;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Neil on 11/2/2014.
 */
public class FileInstance extends JPanel implements Serializable {

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
    private SyntaxHighlighter scrollpane;
    private FileInstancePanel panel;
    private SyntaxHighlighterPane highlighterPane;

    //private static Editor editor = new Editor();
    public FileInstance() {
        this.file = getTempFile();
        this.fileText = "";
        this.status = 5;
        logger.write("status for FileInstance " + file.hashCode() + " is now at " + status);

        setupTextArea();

        String label = "newfile";
        TestFileNode newFileNode = new TestFileNode();
        TextEditor.editor.addItem(newFileNode);
        newFileNode = null;
        TextEditor.editor.addTab(label + TextEditor.editor.getTabCount(), panel);
    }

    public FileInstance(File file) {
        this.fileText = openFile(file);
        this.file = file;
        this.status = 1;

        logger.write("status for FileInstance " + file.hashCode() + " is now at " + status);

        setupTextArea();

        String label = getFileName(file);
        TestFileNode newFileNode = new TestFileNode(file);
        TextEditor.editor.addItem(newFileNode);
        newFileNode = null;
        TextEditor.editor.addTab(label, panel);
    }

    public FileInstance(TestFileNode node) {
        this.fileText = openFile(node.getItemPath().toFile());
        this.file = node.getItemPath().toFile();
        this.status = 1;

        logger.write("status for FileInstance " + file.hashCode() + " is now at " + status);

        setupTextArea();

        String label = getFileName(file);
        TextEditor.editor.addTab(label, panel);
    }

    public static void write(FileInstance[] open) {

        File file = null;
        try {
            file = File.createTempFile("lastOpen", ".texteditor");
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(open);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getTempFile() {
        File tfile = null;
        try {
            String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            tfile = Files.createTempFile(TextEditor.tempDirectory, date, ".txt").toFile();
            logger.write("Opened new temporary file at: " + file.getPath().toString());
        } catch (IOException e) {
            logger.write(e);
        } finally {
            return tfile;
        }
    }
    public void setLogger(Lumberjack logger){
        this.logger = logger;
    }
    private void setupTextArea() {
        textarea = new JTextArea();
        textarea.setText(fileText);

        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);

        scrollpane = setupHighlighting(fileText);

        panel = new FileInstancePanel();
        panel.setFileInstance(this);
        panel.setLayout(new BorderLayout());
        panel.add(scrollpane, BorderLayout.CENTER);
    }

    private SyntaxHighlighter setupHighlighting(String fileText) {
        this.highlighterPane = new SyntaxHighlighterPane();
        SyntaxHighlighterParser parser = new SyntaxHighlighterParser(new BrushPlain());
        parser.setHtmlScript(true);
        parser.setHTMLScriptBrushes(Arrays.asList(new BrushCss(), new BrushJScript(), new BrushPhp()));
        scrollpane = new SyntaxHighlighter(parser, new ThemeDefault(), highlighterPane);
        scrollpane.setViewportView(highlighterPane);
        scrollpane.setContent(fileText);
        return scrollpane;
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
            logger.write("read file " + file.getPath() + " into FileInstance " + file.hashCode());
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
            logger.write("Wrote to file: " + file.toString());
        } catch (IOException e) {
            logger.write(e);
            return false;
        } finally {
            return true;
        }
    }

    public boolean save(File file, String fileText) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file.toString()), "utf-8"
            ));
            writer.write(fileText);
            writer.flush();
            writer.close();
            logger.write("Wrote to file: " + file.toString());
        } catch (IOException e) {
            logger.write(e);
            return false;
        } finally {
            return true;
        }
    }

    public String getText() {
        return highlighterPane.getText();
    }

    public void setText(String newText) {
        highlighterPane.setText(newText);
    }

    public void saveAs(String fileText) {
        logger.write(fileText);
        final JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(this.file);
        int returnV = chooser.showSaveDialog(getParent());
        if (returnV == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile().toPath().toFile();
            save(saveFile, fileText);
        }
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int newStatus) {
        this.status = newStatus;
    }
}