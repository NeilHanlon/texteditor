package com.neilhanlon;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Created by Neil on 11/1/2014.
 */
public class Editor extends JFrame implements ActionListener {
    private static int tabCount;
    private static JTabbedPane tabbedPane;
    private int count;
    private JMenuBar menu;
    private JMenu fileMenu,editMenu,viewMenu;
    private JMenuItem exitItem,cutItem,copyItem,pasteItem,selectItem,saveItem,openItem,statusItem, saveAsItem, newItem;
    private String pad;
    private JToolBar toolBar;
    public void addTab(String title,JPanel panel)
    {
        tabbedPane.add(title,panel);
        tabCount++;
    }
    public Editor()
    {
        super("TextEditor");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        count = 0;
        pad = " ";
        tabbedPane = new JTabbedPane();
        menu = new JMenuBar(); 
        fileMenu = new JMenu("File"); 
        editMenu = new JMenu("Edit"); 
        viewMenu = new JMenu("View");
        exitItem = new JMenuItem("Exit");
        cutItem = new JMenuItem("Cut");
        copyItem = new JMenuItem("Copy");
        pasteItem = new JMenuItem("Paste");
        selectItem = new JMenuItem("Select All"); 
        saveItem = new JMenuItem("Save"); 
        saveAsItem = new JMenuItem("Save As");
        openItem = new JMenuItem("Open");
        statusItem = new JMenuItem("Status");
        newItem = new JMenuItem("New");
        toolBar = new JToolBar();

        setJMenuBar(menu);
        menu.add(fileMenu);
        menu.add(editMenu);
        menu.add(viewMenu);

        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(openItem);
        fileMenu.add(exitItem);

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.add(selectItem);

        viewMenu.add(statusItem);

        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        selectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));


        pane.add(toolBar,BorderLayout.SOUTH);

        pane.add(tabbedPane,BorderLayout.CENTER);

        saveItem.addActionListener(this);
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        exitItem.addActionListener(this);
        cutItem.addActionListener(this);
        copyItem.addActionListener(this);
        pasteItem.addActionListener(this);
        selectItem.addActionListener(this);
        statusItem.addActionListener(this);

        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        JMenuItem clicked = (JMenuItem) e.getSource();
        if(clicked == openItem)
        {
            FileChooser openDialog = new FileChooser();
        }
        if(clicked == newItem)
        {
            FileInstance file = new FileInstance();
        }
    }

    public static void main(String[] args)
    {
        new Editor();
    }
}
