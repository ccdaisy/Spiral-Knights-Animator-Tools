package xandragon.core.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.tree.*;

import xandragon.converter.BinaryParser;
import xandragon.core.ui.tree.CustomTreeCellRenderer;
import xandragon.core.ui.tree.DataTreePath;
import xandragon.core.ui.tree.TreeRenderer;
import xandragon.util.Logger;
import xandragon.util.filedata.DataPersistence;
import xandragon.util.filedata.FileValidator;
import xandragon.util.filedata.OpenFileFilter;

@SuppressWarnings("serial")
public class MainGui extends Frame implements ActionListener, WindowListener {
	
	// Objects for the UI.
	public TextArea UI_Label;
	protected Button openButton;
	protected Button saveButton;
	protected Button setRsrcButton;
	protected JFileChooser chooser;
	protected BinaryParser binaryParser;
	protected JTree dataTree;
	
	//File filters
	protected OpenFileFilter DAT = new OpenFileFilter("DAT", "Binary Spiral Knights asset file");
	protected OpenFileFilter DAE = new OpenFileFilter("DAE", "Collada DAE");
	protected OpenFileFilter XML = new OpenFileFilter("XML", "Spiral Spy XML");
	protected OpenFileFilter DIR = new OpenFileFilter(true);
	
	//Files.
	protected File INPUT_FILE;
	protected File OUTPUT_FILE;
	
	//Other
	protected Logger log;
	protected DataPersistence dataPersistence = null;
	protected SelectType fileMode = SelectType.NONE;
	
	// Main constructor.
	public MainGui (BinaryParser _parser) throws URISyntaxException {
		setLayout(null);
		setResizable(false);
		setTitle("Spiral Knights Animator Tools");
		setSize(800, 500);
		
		dataPersistence = new DataPersistence();
		chooser = new JFileChooser(dataPersistence.getSavedResourceDirectory());
		chooser.setAcceptAllFileFilterUsed(false);
		UI_Label = new TextArea("Welcome to Spiral Knights Animator Tools.\nPlease select a model.\n", 0, 0, TextArea.SCROLLBARS_NONE);
		dataTree = TreeRenderer.createBlankTree();
		openButton = new Button("Open a model...");
		saveButton = new Button("Save model as...");
		setRsrcButton = new Button("Set resource directory...");
		
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		dataTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		dataTree.setRootVisible(true);
		dataTree.setEditable(false);
		UI_Label.setEditable(false);
		
		saveButton.setEnabled(false);
		
		
		UI_Label.setBounds(5, 70, 415, 420);
		add(UI_Label);
		
		JScrollPane scroll = new JScrollPane(dataTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(420, 65, 380, 420);
		//dataTree.setBounds(420, 65, 380, 420);
		dataTree.setBounds(0, 0, 1000, 1000);
		add(scroll);
		
		openButton.setBounds(25, 35, 180, 30);
		add(openButton);
		
		saveButton.setBounds(230, 35, 180, 30);
		add(saveButton);
		
		setRsrcButton.setBounds(435, 35, 180, 30);
		add(setRsrcButton);
		
		addWindowListener(this);
		openButton.addActionListener(this);
		saveButton.addActionListener(this);
		setRsrcButton.addActionListener(this);
		chooser.addActionListener(this);
		
		setVisible(true);
		
		binaryParser = new BinaryParser(log);
	}
	
	// Methods
	/**Give the GUI a logger to append to. While this may seem redundant (Given the label is here already), it's also for the purpose of printing to system output.*/
	public void setLog(Logger _log) {
		log = _log;
		binaryParser.setLog(log);
	}
	
	protected void resetTree() {
		try {
			dataTree.setModel(TreeRenderer.createBlankTreePath());
			dataTree.setCellRenderer(new DefaultTreeCellRenderer());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void updateTree(DataTreePath dataTreePath) {
		try {
			dataTree.setModel(dataTreePath);
			dataTree.setCellRenderer(new CustomTreeCellRenderer());
			dataTree.treeDidChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == openButton) {
			//Open requested.
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileMode = SelectType.OPEN;
			chooser.addChoosableFileFilter(DAT);
			chooser.removeChoosableFileFilter(DAE);
			chooser.removeChoosableFileFilter(DIR);
			chooser.removeChoosableFileFilter(XML);
			
			chooser.showDialog(this, "Open model");
		} else if (evt.getSource() == saveButton) {
			//Save selected.
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileMode = SelectType.SAVE;
			chooser.removeChoosableFileFilter(DAT);
			chooser.removeChoosableFileFilter(DIR);
			chooser.addChoosableFileFilter(DAE);
			//chooser.addChoosableFileFilter(XML);
			
			chooser.showDialog(this, "Save model");
		} else if (evt.getSource() == setRsrcButton) {
			//Setting resource directory
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileMode = SelectType.SET;
			chooser.removeChoosableFileFilter(DAT);
			chooser.removeChoosableFileFilter(DAE);
			chooser.removeChoosableFileFilter(XML);
			chooser.addChoosableFileFilter(DIR);
			
			chooser.showOpenDialog(this);
		} else if (evt.getSource() == chooser) {
			if (evt.getActionCommand() == JFileChooser.APPROVE_SELECTION) {
				if (fileMode == SelectType.SAVE) {
					OUTPUT_FILE = chooser.getSelectedFile();
					try {
						log.ClearLog();
						OpenFileFilter current = (OpenFileFilter) chooser.getFileFilter();
						if (current == DAE) {
							if (!FileValidator.getFileExtension(OUTPUT_FILE).toLowerCase().matches("dae")) {
								OUTPUT_FILE = new File(OUTPUT_FILE.getPath() + ".dae");
							}
							binaryParser.saveDAE(OUTPUT_FILE);
							
							log.AppendLn("Conversion complete.");
						} else if (current == XML) {
							log.AppendLn("[WARNING] XML Exporting is not ready yet. Please save as another format.");
						}
					} catch (Exception e) {
						//NOTE: If we hit this point in the GUI, any errors that would occur during read would already be handled by preProcess.
						e.printStackTrace();
					}
				} else if (fileMode == SelectType.OPEN) {
					INPUT_FILE = chooser.getSelectedFile();
					if (INPUT_FILE.exists()) {
						log.ClearLog();
						
						try {
							File t = INPUT_FILE;
							while (t.getName().matches("rsrc") == false) {
								t = t.getParentFile();
							}
							binaryParser.setResourceDir(t.getPath() + File.separator);
							binaryParser.process(INPUT_FILE);
							updateTree(binaryParser.treeRen.getDataTreePath());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						saveButton.setEnabled(true);
					} else {
						saveButton.setEnabled(false);
					}
				} else if (fileMode == SelectType.SET) {
					dataPersistence.setSavedResourceDirectory(chooser.getSelectedFile());
				}
			} else if (evt.getActionCommand() == JFileChooser.CANCEL_SELECTION) {
				//Do I do anything here?
			}
		}
	}
	
	protected static enum DataType {
		STRIDE,
		OFFSET
	}
	
	protected static enum SelectType {
		NONE,
		OPEN,
		SAVE,
		SET
	}
	
	@Override
	public void windowClosing(WindowEvent evt) {
		System.exit(0);
	}
	
	// Not Used, but need to provide an empty body to compile.
	@Override public void windowOpened(WindowEvent evt) { }
	@Override public void windowClosed(WindowEvent evt) { }
	@Override public void windowIconified(WindowEvent evt) { }
	@Override public void windowDeiconified(WindowEvent evt) { }
	@Override public void windowActivated(WindowEvent evt) { }
	@Override public void windowDeactivated(WindowEvent evt) { }
}
