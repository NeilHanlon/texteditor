package com.neilhanlon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Neil on 11/2/2014.
 */
public class CloseTabActionHandler implements ActionListener {
    private String tabName;

    public CloseTabActionHandler(String tabName) {
        this.tabName = tabName;
    }

    public String getTabName() {
        return tabName;
    }

    public void actionPerformed(ActionEvent evt) {
        JTabbedPane tabbedPane = TextEditor.editor.getTabbedPane();
        int index = tabbedPane.indexOfTab(getTabName());
        if (index >= 0) {
            tabbedPane.removeTabAt(index);
            TextEditor.editor.decrementTabCount();
        }
        TextEditor.logger.write("Tab removed. tabcount is now: "+TextEditor.editor.getTabCount());

    }
}
