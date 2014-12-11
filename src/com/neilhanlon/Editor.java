package com.neilhanlon;

import com.neilhanlon.Logger.Lumberjack;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * Created by Neil on 11/1/2014.
 */
public class Editor extends JFrame implements ActionListener {
    private static int tabCount;
    private static JTabbedPane tabbedPane;
    private int count;
    private JMenuBar menu;
    private JMenu fileMenu, editMenu, viewMenu;
    private JMenuItem exitItem, cutItem, copyItem, pasteItem, selectItem, saveItem, openItem, statusItem, saveAsItem, newItem, openFolderItem;
    private String pad;
    private JToolBar toolBar;
    private JButton closeButton;
    private Container pane;
    private Lumberjack logger = TextEditor.logger;
    private JTree tree;
    private DefaultMutableTreeNode recent = new DefaultMutableTreeNode("Recent");
    private int nodeCount = 0;

    MouseListener ml = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            int selRow = tree.getRowForLocation(e.getX(), e.getY());
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if(selRow != -1) {
                if(e.getClickCount() == 1) {
                    //FileInstance.openFile(((TestFileNode)selPath.getLastPathComponent()).itemPath.toFile());
                }
                else if(e.getClickCount() == 2) {
                	new FileInstance(((TestFileNode)selPath.getLastPathComponent()));
                }
            }
        }
    };
    
    public Editor() {
        super("TextEditor");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane = getContentPane();
        pane.setLayout(new BorderLayout());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            logger.write("ERROR", e);
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
        openFolderItem = new JMenuItem("Open Folder");
        toolBar = new JToolBar();

        setJMenuBar(menu);
        menu.add(fileMenu);
        menu.add(editMenu);
        menu.add(viewMenu);

        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(openItem);
        //fileMenu.add(openFolderItem);
        fileMenu.add(exitItem);

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        //editMenu.add(selectItem);

        viewMenu.add(statusItem);

        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        selectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));


        pane.add(toolBar, BorderLayout.SOUTH);
        pane.add(tabbedPane, BorderLayout.CENTER);
        
        tree = new JTree(recent);
        getContentPane().add(tree, BorderLayout.WEST);
        tree.setBackground(Color.GRAY);
        tree.setPreferredSize(new Dimension (240,700));
        tree.setVisible(true);
        tree.addMouseListener(ml);
        tree.setShowsRootHandles(true);
        tree.setEditable(true);
        
        saveItem.addActionListener(this);
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        //exitItem.addActionListener(this);
        cutItem.addActionListener(this);
        copyItem.addActionListener(this);
        pasteItem.addActionListener(this);
        selectItem.addActionListener(this);
        statusItem.addActionListener(this);

        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeClicked(false);
            }
        };

        addWindowListener(exitListener);

        setVisible(true);
        logger.write("GUI Instantiated.");
        logger.write("Waiting for input...");
    }

  //  public static void main(String[] args) {
  //      new Editor();
  //  }

    public void addTab(String title, FileInstancePanel panel) {
        JPanel panelTab = new JPanel(new GridLayout(1, 2));
        panelTab.setOpaque(false);

        closeButton = new JButton("x");
        JLabel label = new JLabel(title);
        closeButton.addActionListener(new CloseTabActionHandler(title));
        closeButton.setMaximumSize(new Dimension(2, 2));

        /*GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        panelTab.add(label,gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelTab.add(closeButton,gbc);*/

        panelTab.add(label);
        panelTab.add(closeButton);

        tabbedPane.addTab(title, (FileInstancePanel) panel);

        tabbedPane.setTabComponentAt(tabCount, panelTab);

        tabCount++;

        logger.write("Added tab: " + title);
        logger.write("tabcount is " + tabCount);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void incrementTabCount() {
        tabCount++;
    }

    public void decrementTabCount() {
        tabCount--;
    }

    public int getTabCount() {
        return tabCount;
    }

    public void setTabCount(int newCount) {
        tabCount = newCount;
    }

    public JMenuBar getMenu() {
        return menu;
    }

    public JMenu getFileMenu() {
        return fileMenu;
    }

    public JMenu getEditMenu() {
        return editMenu;
    }

    public JMenu getViewMenu() {
        return viewMenu;
    }

    public JMenuItem getExitItem() {
        return exitItem;
    }

    public JMenuItem getCutItem() {
        return cutItem;
    }

    public JMenuItem getCopyItem() {
        return copyItem;
    }

    public JMenuItem getPasteItem() {
        return pasteItem;
    }

    public JMenuItem getSelectItem() {
        return selectItem;
    }

    public JMenuItem getSaveItem() {
        return saveItem;
    }

    public JMenuItem getOpenItem() {
        return openItem;
    }

    public JMenuItem getStatusItem() {
        return statusItem;
    }

    public JMenuItem getSaveAsItem() {
        return saveAsItem;
    }

    public JMenuItem getNewItem() {
        return newItem;
    }

    public JMenuItem getOpenFolderItem() {
        return openFolderItem;
    }

    public void closeClicked(boolean forceClose) {
        int confirm = (forceClose) ? JOptionPane.YES_OPTION : JOptionPane.showOptionDialog(pane,
                "Are You Sure to Close this Application?",
                "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                logger.write("Closing program.");
                logger.close();
            } catch (IOException e) {
                logger.write(e);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem clicked = (JMenuItem) e.getSource();
        new EditorAction(clicked);
    }
    
    public void addItem(TestFileNode node)
    {
    	//recent.add(node);
    	recent.insert(node, nodeCount);
    	nodeCount++;
    }
}
