package com.neilhanlon.Logger;

import com.neilhanlon.TextEditor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hanlonn on 11/3/2014.
 */
public class Lumberjack {

    private Path logDirectory = null;
    private File logFile = null;
    private Writer writer;
    private Boolean debug = false;

    /**
     * Let's just assume there is no log file...
     */
    public Lumberjack() {
        this(false);
    }

    public Lumberjack(Boolean pleaseDebug) {
        this.debug = pleaseDebug;
        createLogDirectory();
        createLogFile();
        openWriter();
    }

    public void write(String message) {
        this.write("NOTICE", message);
    }
    public void write(Exception e) {
        this.write("ERROR",e);
    }
    public void write(String error, Exception e) {
        this.write(error,e.getStackTrace().toString());
    }
    public void error(String message){ this.write("ERROR",message);}
    public void error(Exception e) { this.write("ERROR",e); }

    public void write(String logLevel, String message) {
        String date = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String logLine = "[" + date + "] [" + logLevel + "] " + message;
        try {
            this.writer.write(logLine + TextEditor.lineSeparator);
            this.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (debug)
                System.out.println(logLine);
        }
    }

    public void openWriter() {
        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(logFile.toString()), "utf-8"
            ));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createLogDirectory() {
        String tempLoc = System.getProperty("java.io.tmpdir");
        File file = null;
        this.logDirectory = Paths.get(tempLoc, "TextEditor", "log");
        try {
            if (!Files.exists(logDirectory)) {
                Files.createDirectories(logDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLogFile() {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
            String directory = logDirectory.toString();
            this.logFile = Files.createFile(Paths.get(directory, date + ".txt")).toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    public void setLogDirectory(Path newDirectory) throws InvalidPathException {
        if (!Files.exists(newDirectory))
            throw new InvalidPathException(newDirectory.toString(), "Directory does not exist");
        this.logDirectory = newDirectory;
    }

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File newLogFile) {
        if (!Files.exists(
                Paths.get(logDirectory.toAbsolutePath().toString(),
                        newLogFile.toPath().toAbsolutePath().toString()
                ))) {
            throw new InvalidPathException(newLogFile.toString(), "File does not exist");
        }
        this.logFile = newLogFile;
    }

    public void close() throws IOException {
        this.writer.close();
    }
}
