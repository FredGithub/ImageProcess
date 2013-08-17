package ar.edu.itba.imageprocess;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class MenuPane extends JPanel {
	public MenuPane() {
		super(new GridBagLayout());

		GridBagConstraints c;

		JTabbedPane tabbedPane = new JTabbedPane();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		add(tabbedPane, c);

		JPanel menu1 = new JPanel();
		tabbedPane.addTab("Menu 1", menu1);

		JPanel menu2 = new JPanel();
		tabbedPane.addTab("Menu 2", menu2);
	}
}
