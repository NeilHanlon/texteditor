package com.neilhanlon;

import com.neilhanlon.Logger.Lumberjack;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Neil on 11/2/2014.
 */
public class TextEditor {

    public static Editor editor;
    public static Path tempDirectory;
    public static Lumberjack logger = new Lumberjack();

    public static void main(String[] args)
    {
        logger.write("Starting up...");
        createTempDirectory();
        logger.write("Launching editor");
        editor = new Editor();
    }
    public static void createTempDirectory()
    {
        String tempLoc = System.getProperty("java.io.tmpdir");
        File file = null;
        tempDirectory = Paths.get(tempLoc,"TextEditor");
        try {
            if(!Files.exists(tempDirectory))
            {
                Files.createDirectory(tempDirectory);
                logger.write("NOTICE","Created directory " + tempDirectory.toString());
            }
            else
            {
                logger.write("NOTICE","Directory already created.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
