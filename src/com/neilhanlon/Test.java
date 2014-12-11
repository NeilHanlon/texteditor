package com.neilhanlon;

import com.neilhanlon.Logger.Lumberjack;

import java.io.File;
import java.io.IOException;

/**
 * Created by hanlonn on 12/9/2014.
 */
public class Test {
    private static File testFile;
    private static FileInstance testFileInstance;
    public static void main(String[] args)
    {
        TextEditor texteditor = new TextEditor();
        openFile();
        saveFile();

    }
    private static void openFile(){
        testFile = new File("c:\\users\\hanlonn\\test.txt");
        try {
            testFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        testFileInstance = new FileInstance(testFile);
        System.out.println(testFileInstance.getText());
    }
    private static void saveFile(){
        testFileInstance.setText("This is a test!");
        testFileInstance.save(testFile,testFileInstance.getText());
        FileInstance testInstance = new FileInstance(testFile);
        System.out.println(testInstance.getText());
    }
}
