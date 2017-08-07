package xandragon.core.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

import xandragon.converter.BinaryParser;
import xandragon.converter.BinaryParser.ModelData;
import xandragon.util.CustomTreeCellRenderer;
import xandragon.util.Logger;
import xandragon.util.exception.InvalidDatException;
import xandragon.util.filedata.DataPersistence;
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
	protected OpenFileFilter OBJ = new OpenFileFilter("OBJ", "Wavefront OBJ");
	protected OpenFileFilter XML = new OpenFileFilter("XML", "Spiral Spy XML");
	protected OpenFileFilter DIR = new OpenFileFilter("Directory", "Select a file directory");
	
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
		setSize(640, 500);
		
		dataPersistence = new DataPersistence();
		chooser = new JFileChooser(dataPersistence.getSavedResourceDirectory());
		chooser.setAcceptAllFileFilterUsed(false);
		UI_Label = new TextArea("Welcome to Spiral Knights Animator Tools.\nPlease select a model.\n", 0, 0, TextArea.SCROLLBARS_NONE);
		dataTree = new JTree(createNode(new Entry("(no model to view)")));
		openButton = new Button("Open a model...");
		saveButton = new Button("Save model as...");
		setRsrcButton = new Button("Set resource directory...");
		
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		dataTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		dataTree.setRootVisible(true);
		dataTree.setEditable(false);
		UI_Label.setEditable(false);
		
		saveButton.setEnabled(false);
		
		UI_Label.setBounds(5, 70, 360, 420);
		add(UI_Label);
		
		dataTree.setBounds(365, 65, 280, 420);
		add(dataTree);
		
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
	
	protected void updateTree(String name) {
		try {
			dataTree.setModel(new DataTreePath(createNode(new Entry(name))));
			dataTree.setCellRenderer(new DefaultTreeCellRenderer());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void updateTree(ModelData data, String name) {
		try {
			DefaultMutableTreeNode root = createNode(new Entry("Model \""+name+"\""));
			
			addNode(root, new Entry(data, "magic"));
			addNode(root, new Entry(data, "version"));
			addNode(root, new Entry(data, "compressed"));
			addNode(root, new Entry(data, "implementation"));
			addNode(root, new Entry(data, "saSize"));
			DefaultMutableTreeNode floatArray = addNode(root, new Entry(data, "faSize"));
			DefaultMutableTreeNode boneIndices = addNode(floatArray, new Entry(data, "boneIndicesCount"));
			addNode(boneIndices, new Entry(data, "boneIndicesCount", DataType.STRIDE));
			addNode(boneIndices, new Entry(data, "boneIndicesCount", DataType.OFFSET));
			DefaultMutableTreeNode boneWeights = addNode(floatArray, new Entry(data, "boneWeightsCount"));
			addNode(boneWeights, new Entry(data, "boneWeightsCount", DataType.STRIDE));
			addNode(boneWeights, new Entry(data, "boneWeightsCount", DataType.OFFSET));
			DefaultMutableTreeNode vertices = addNode(floatArray, new Entry(data, "vertexArrayCount"));
			addNode(vertices, new Entry(data, "vertexArrayCount", DataType.STRIDE));
			addNode(vertices, new Entry(data, "vertexArrayCount", DataType.OFFSET));
			DefaultMutableTreeNode normals = addNode(floatArray, new Entry(data, "normalArrayCount"));
			addNode(normals, new Entry(data, "normalArrayCount", DataType.STRIDE));
			addNode(normals, new Entry(data, "normalArrayCount", DataType.OFFSET));
			DefaultMutableTreeNode uvs = addNode(floatArray, new Entry(data, "uvArrayCount"));
			addNode(uvs, new Entry(data, "uvArrayCount", DataType.STRIDE));
			addNode(uvs, new Entry(data, "uvArrayCount", DataType.OFFSET));
			
			dataTree.setModel(new DataTreePath(root));
			dataTree.setCellRenderer(new CustomTreeCellRenderer());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected DefaultMutableTreeNode createNode(Entry entry) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(entry);
		return node;
	}
	
	protected DefaultMutableTreeNode addNode(DefaultMutableTreeNode in, Entry entry) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(entry);
		node.setUserObject(entry);
		in.add(node);
		return node;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == openButton) {
			//Open requested.
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileMode = SelectType.OPEN;
			chooser.addChoosableFileFilter(DAT);
			chooser.removeChoosableFileFilter(OBJ);
			chooser.removeChoosableFileFilter(DAE);
			chooser.removeChoosableFileFilter(DIR);
			
			chooser.showDialog(this, "Open model");
		} else if (evt.getSource() == saveButton) {
			//Save selected.
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileMode = SelectType.SAVE;
			chooser.removeChoosableFileFilter(DAT);
			chooser.removeChoosableFileFilter(DIR);
			chooser.addChoosableFileFilter(OBJ);
			chooser.addChoosableFileFilter(DAE);
			chooser.addChoosableFileFilter(XML);
			
			chooser.showDialog(this, "Save model");
		} else if (evt.getSource() == setRsrcButton) {
			//Setting resource directory
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileMode = SelectType.SET;
			chooser.removeChoosableFileFilter(DAT);
			chooser.removeChoosableFileFilter(OBJ);
			chooser.removeChoosableFileFilter(DAE);
			chooser.addChoosableFileFilter(DIR);
			
			chooser.showOpenDialog(this);
		} else if (evt.getSource() == chooser) {
			if (evt.getActionCommand() == JFileChooser.APPROVE_SELECTION) {
				if (fileMode == SelectType.SAVE) {
					OUTPUT_FILE = chooser.getSelectedFile();
					try {
						binaryParser.startProcessing(INPUT_FILE, OUTPUT_FILE);
					} catch (Exception e) {
						//NOTE: If we hit this point in the GUI, any errors that would occur during read would already be handled by preProcess.
						e.printStackTrace();
					}
				} else if (fileMode == SelectType.OPEN) {
					INPUT_FILE = chooser.getSelectedFile();
					if (INPUT_FILE.exists()) {
						log.ClearLog();
						try {
							updateTree(binaryParser.preProcess(INPUT_FILE), INPUT_FILE.getName());
							saveButton.setEnabled(true);
						} catch (IOException e) {
							log.AppendLn("A critical read exception occurred and conversion was not able to continue.");
							updateTree("(no model to view)");
							saveButton.setEnabled(false);
						} catch (InvalidDatException e) {
							log.AppendLn(e.getMessage());
							log.AppendLn("Reading is unable to continue.");
							updateTree("(no model to view)");
							saveButton.setEnabled(false);
						}
					} else {
						saveButton.setEnabled(false);
					}
				} else if (fileMode == SelectType.SET) {
					dataPersistence.setRsrcDir(chooser.getSelectedFile());
				}
			} else if (evt.getActionCommand() == JFileChooser.CANCEL_SELECTION) {
				//Do I do anything here?
			}
		}
	}
	
	public class Entry {
		public String entryName;
		public String idName;
		
		/**
		 * Construct a new Entry value
		 * @param value The display text for said entry.
		 */
		public Entry(String value) {
			entryName = value;
			idName = "NULL";
		}
		
		public Entry(ModelData data, String idx) {
			entryName = data.get(idx);
			idName = idx;
		}
		
		public Entry(ModelData data, String idx, DataType type) {
			if (type == DataType.STRIDE) {
				entryName = data.getStride(idx);
			} else if (type == DataType.OFFSET) {
				entryName = data.getOffset(idx);
			}
			idName = idx;
		}
		
		@Override
		public String toString() {
			return entryName;
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
	
	protected class DataTreePath implements TreeModel {
		protected DefaultMutableTreeNode root = null;
		
		public DataTreePath(DefaultMutableTreeNode _root) {
			root = _root;
		}
		
		@Override
		public void addTreeModelListener(TreeModelListener l) {}

		@Override
		public Object getChild(Object parent, int index) {
			DefaultMutableTreeNode par = (DefaultMutableTreeNode) parent;
			return par.getChildAt(index);
		}

		@Override
		public int getChildCount(Object parent) {
			DefaultMutableTreeNode par = (DefaultMutableTreeNode) parent;
			return par.getChildCount();
		}

		@Override
		public int getIndexOfChild(Object parent, Object child) {
			DefaultMutableTreeNode par = (DefaultMutableTreeNode) parent;
			return par.getIndex((TreeNode) child);
		}

		@Override
		public Object getRoot() {
			return root;
		}

		@Override
		public boolean isLeaf(Object node) {
			return ((DefaultMutableTreeNode) node).isLeaf();
		}

		@Override
		public void removeTreeModelListener(TreeModelListener l) {}

		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {}
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
