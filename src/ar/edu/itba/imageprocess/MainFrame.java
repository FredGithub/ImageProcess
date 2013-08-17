package ar.edu.itba.imageprocess;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel mMainPanel;

	public MainFrame() {
		super(ImageProcess.APPLICATION_NAME);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension((int) (screenSize.width * 0.8), (int) (screenSize.width * 0.8 / 1.6)));
		setResizable(false);
		setLocationRelativeTo(null);

		mMainPanel = new JPanel(new GridBagLayout());
		mMainPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
		GridBagConstraints c;

		MenuPane menuPane = new MenuPane();
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 16, 0);
		mMainPanel.add(menuPane, c);

		ImagePane pane1 = new ImagePane();
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 16);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		mMainPanel.add(pane1, c);

		ImagePane pane2 = new ImagePane();
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		mMainPanel.add(pane2, c);

		getContentPane().add(mMainPanel);
	}
}
