package com.neilhanlon;

import com.neilhanlon.Logger.Lumberjack;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by hanlonn on 12/1/2014.
 */
public class EditorAction {
    private JMenuItem action;
    private Editor editor = TextEditor.editor;
    private FileInstance file = null;
    private FileInstancePanel basePanel = null;
    private Lumberjack logger = TextEditor.logger;

    public EditorAction(JMenuItem clicked) {
        this.action = clicked;
        this.doAction();
    }

    /**
     * Should only be called when the editor is closing...
     * @param close
     */
    public EditorAction(boolean close){
        if(close)
            this.doClose();
    }
    private void doClose(){
        int confirm = JOptionPane.showOptionDialog(editor.getRootPane(),
                "Are You Sure to Close this Application?",
                "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                /*FileInstance[] open = new FileInstance[tabCount];
                for(int i = 0; i < tabCount; ++i)
                {
                    FileInstancePanel basepanel = (FileInstancePanel) tabbedPane.getComponentAt(i);
                    open[i] = basepanel.getFileInstance();
                }
                FileInstance.write(open);*/
                logger.write("Closing program.");
                logger.close();
            } catch (IOException e) {
                logger.write(e);
            }
        }
    }

    private void doOpenItem() {
        new FileChooser();
        logger.write("Open dialog called");
    }

    private void doNewItem() {
        new FileInstance();
        logger.write("new item created");
    }

    private void doSaveItem() {
        String fileText = "";
        try {
            basePanel = (FileInstancePanel) editor.getTabbedPane().getSelectedComponent();
            file = basePanel.getFileInstance();
            fileText = file.getText();
            if (file == null) {
                return;
            }
        } catch (Exception exception) {
            logger.write("ERROR", exception);
        }
        //if is new temp. file.. this should be replaced with a buffer check... maybe
        if (file.getStatus() == 5) {
            file.saveAs(fileText);
        } else {
            file.save(fileText);
        }
    }

    private void doSaveAsItem() {
        basePanel = (FileInstancePanel) editor.getTabbedPane().getSelectedComponent();
        file = basePanel.getFileInstance();
        logger.write(file.getText());
        file.saveAs(file.getText());
    }

    private void doOpenFolder() {
        FolderChooser chooser = new FolderChooser();
    }

    private void doAction() {
        if (action == editor.getOpenItem()) this.doOpenItem();
        if (action == editor.getNewItem()) this.doNewItem();
        if (action == editor.getSaveItem()) this.doSaveItem();
        if (action == editor.getSaveAsItem()) this.doSaveAsItem();
        if (action == editor.getOpenFolderItem()) this.doOpenFolder();
    }
}
