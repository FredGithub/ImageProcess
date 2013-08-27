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

	private MainController mController;
	private JPanel mMainPanel;
	private ImagePane mImagePane1;
	private ImagePane mImagePane2;

	public MainFrame(MainController controller) {
		super(ImageProcess.APPLICATION_NAME);

		mController = controller;

		mMainPanel = new JPanel(new GridBagLayout());
		mMainPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
		GridBagConstraints c;

		MenuPane menuPane = new MenuPane(controller);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 16, 0);
		mMainPanel.add(menuPane, c);

		mImagePane1 = new ImagePane(controller, 0);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 16);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		mMainPanel.add(mImagePane1, c);

		mImagePane2 = new ImagePane(controller, 1);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		mMainPanel.add(mImagePane2, c);

		getContentPane().add(mMainPanel);
		pack();
		setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - getSize().width / 2, 20);
	}

	public void start() {
		mController.selectSourceImagePane(mImagePane1);
		mController.selectDestImagePane(mImagePane1);
		setVisible(true);
	}

	public ImagePane[] getImagePanes() {
		return new ImagePane[] { mImagePane1, mImagePane2 };
	}
}
