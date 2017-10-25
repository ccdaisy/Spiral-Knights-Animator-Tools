package xandragon.core.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class ErrorGui extends Frame implements ActionListener, WindowListener {
	
	public ErrorGui() {
		JOptionPane.showMessageDialog(this, "ERROR! ProjectX Code cannot be found!\nPlease put this JAR into the Spiral Knights root directory.");
	}
	
	@Override public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == this.getComponents()[0]) {
			
		}
	}
	
	
	@Override public void windowActivated(WindowEvent arg0) {}
	@Override public void windowClosed(WindowEvent arg0) {}
	@Override public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}
	@Override public void windowDeactivated(WindowEvent arg0) {}
	@Override public void windowDeiconified(WindowEvent arg0) {}
	@Override public void windowIconified(WindowEvent arg0) {}
	@Override public void windowOpened(WindowEvent arg0) {}
}
