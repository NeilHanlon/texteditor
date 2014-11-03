package com.neilhanlon;

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Neil on 11/2/2014.
 */
public class FileInstancePanel extends JComponent implements Accessible {
    public FileInstance file;

    public FileInstancePanel(LayoutManager layout, boolean isDoubleBuffered) {
        setLayout(layout);
        setDoubleBuffered(isDoubleBuffered);
        updateUI();
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
}
