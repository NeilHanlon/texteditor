package com.neilhanlon.Logger;

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

    /**
     * Let's just assume there is no log file...
     */
    public Lumberjack() {
        createLogDirectory();
        createLogFile();
        openWriter();
    }
    public void write(String message)
    {
        this.write("NOTICE",message);
    }
    public void write(String logLevel, String message) {
        String date = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        try {
            writer.write("["+date+"] ["+logLevel+"] "+message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
    public void openWriter()
    {
        writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
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
        logDirectory = Paths.get(tempLoc, "TextEditor", "log");
        try {
            if (!Files.exists(logDirectory)) {
                Files.createDirectory(logDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLogFile() {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
            String directory = logDirectory.toString();
            logFile = Files.createFile(Paths.get(directory,date+".txt")).toFile();
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
}
