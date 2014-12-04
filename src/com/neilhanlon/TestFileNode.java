package com.neilhanlon;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Path;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.neilhanlon.*;
import com.neilhanlon.Logger.Lumberjack;

public class TestFileNode extends DefaultMutableTreeNode
{
	private Path itemPath;
	private String itemName;
	private Lumberjack logger = TextEditor.logger;
	public TestFileNode()
	{
		super("newfile");
		itemPath = null;
		itemName = "newfile";
	}
	
	public TestFileNode(File file)
	{
		super (file.getName());
		itemPath = file.toPath();
		itemName = file.getName();
		logger.write("Read file path :" + file.getPath().toString());
	}
	
	public Path getItemPath()
	{
		return itemPath;
	}
	
	public String getItemName()
	{
		return itemName;
	}
	
	public void setPath(File file)
	{
		itemPath = file.toPath();
	}
	public static void main(String[] args)
	{
		File newfile = new File("AnewFile");
		FileChooser openDialog = new FileChooser();
		System.out.println(newfile.toPath());
		System.out.println(newfile.getName());
	}
	
}
