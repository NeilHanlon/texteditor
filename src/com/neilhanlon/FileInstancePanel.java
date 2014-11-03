package com.neilhanlon;

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Neil on 11/2/2014.
 */
public class FileInstancePanel extends JComponent implements Accessible {
    private static int count = 0;
    private FileInstance file;
    private int id;
    public FileInstancePanel(LayoutManager layout, boolean isDoubleBuffered) {
        setLayout(layout);
        setDoubleBuffered(isDoubleBuffered);
        updateUI();
        id = count+1;
    }
    public FileInstancePanel(boolean isDoubleBuffered) {
        this(new FlowLayout(), isDoubleBuffered);
    }
    public FileInstancePanel(LayoutManager layout) { this(layout,true); }
    public FileInstancePanel(){ this(true); }

    public void setFileInstance(FileInstance fileToAttach)
    {
        this.file = fileToAttach;
    }
    public FileInstance getFileInstance(){
        return file;
    }
    public void setTabID(int newID)
    {
        id = newID;
    }
    public int getTabID(){ return id; }
}
