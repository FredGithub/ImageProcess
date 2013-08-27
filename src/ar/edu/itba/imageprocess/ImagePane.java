package ar.edu.itba.imageprocess;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.edu.itba.imageprocess.utils.Log;
import ar.edu.itba.imageprocess.utils.StringUtils;

@SuppressWarnings("serial")
public class ImagePane extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

	public static final Color COLOR_UNSELECTED = new Color(0, 0, 0, 20);
	public static final Color COLOR_SOURCE = new Color(0, 255, 0, 30);
	public static final Color COLOR_DEST = new Color(0, 0, 255, 30);

	private MainController mController;
	private int mIndex;
	private Image mImage;
	private ArrayList<Image> mHistory;
	private int mHistoryIndex;
	private boolean mSource;
	private boolean mDest;
	private JPanel mImagePane;
	private JLabel mImageLabel;
	private JButton mPrevBtn;
	private JButton mNextBtn;
	private JButton mClearBtn;
	private JLabel mPixelLabel;

	public ImagePane(MainController controller, int index) {
		super(new GridBagLayout());

		mController = controller;
		mIndex = index;
		mImage = null;
		mHistory = new ArrayList<Image>();
		mHistoryIndex = -1;
		mSource = false;
		mDest = false;

		GridBagConstraints c;

		mImagePane = new JPanel(new GridBagLayout());
		mImagePane.setPreferredSize(ImageProcess.IMG_DIMENSION);
		mImagePane.setBackground(COLOR_UNSELECTED);
		mImagePane.addMouseListener(this);
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		add(mImagePane, c);

		mImageLabel = new JLabel();
		mImageLabel.addMouseListener(this);
		mImageLabel.addMouseMotionListener(this);
		mImagePane.add(mImageLabel);

		mPrevBtn = new JButton("Prev");
		mPrevBtn.addActionListener(this);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(8, 0, 0, 8);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(mPrevBtn, c);

		mNextBtn = new JButton("Next");
		mNextBtn.addActionListener(this);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(8, 0, 0, 8);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(mNextBtn, c);

		mClearBtn = new JButton("Clear");
		mClearBtn.addActionListener(this);
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.insets = new Insets(8, 0, 0, 8);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(mClearBtn, c);

		mPixelLabel = new JLabel("R=0 G=0 B=0");
		mPixelLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 1;
		c.insets = new Insets(8, 0, 0, 0);
		c.anchor = GridBagConstraints.LINE_END;
		add(mPixelLabel, c);

		setImageWithHistory(null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == mImagePane || e.getSource() == mImageLabel) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				mController.selectSourceImagePane(this);
				mController.selectDestImagePane(this);
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				mController.selectDestImagePane(this);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (mImage != null) {
			String red = StringUtils.rightPad(String.valueOf(mImage.getRed(e.getX(), e.getY())), 3);
			String green = StringUtils.rightPad(String.valueOf(mImage.getGreen(e.getX(), e.getY())), 3);
			String blue = StringUtils.rightPad(String.valueOf(mImage.getBlue(e.getX(), e.getY())), 3);
			mPixelLabel.setText("R=" + red + " G=" + green + " B=" + blue);
		} else {
			mPixelLabel.setText("---");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mPrevBtn) {
			historyMove(-1);
		} else if (e.getSource() == mNextBtn) {
			historyMove(+1);
		} else if (e.getSource() == mClearBtn) {
			setImageWithHistory(null);
		}
	}

	public int getIndex() {
		return mIndex;
	}

	public boolean getSource() {
		return mSource;
	}

	public void setSource(boolean source) {
		mSource = source;
		updateSelectionColor();
	}

	public boolean getDest() {
		return mDest;
	}

	public void setDest(boolean dest) {
		mDest = dest;
		updateSelectionColor();
	}

	public Image getImage() {
		return mImage;
	}

	public void setImage(Image image) {
		mImage = image;
		if (mImage != null) {
			mImageLabel.setIcon(new ImageIcon(mImage.getBufferedImage()));
		} else {
			mImageLabel.setIcon(null);
		}
		mClearBtn.setEnabled(mImage != null);
		mController.repaintMainFrame();
	}

	public void setImageWithHistory(Image image) {
		setImage(image);
		while (mHistoryIndex < mHistory.size() - 1) {
			mHistory.remove(mHistory.size() - 1);
		}
		mHistory.add(mImage);
		mHistoryIndex++;
		setHistoryButtonsState();
	}

	public void historyMove(int move) {
		int futureIndex = mHistoryIndex + move;
		if (futureIndex < 0) {
			futureIndex = 0;
		}
		if (futureIndex > mHistory.size() - 1) {
			futureIndex = mHistory.size() - 1;
		}
		if (futureIndex != mHistoryIndex) {
			Log.d("history move (" + futureIndex + ")");
			mHistoryIndex = futureIndex;
			setImage(mHistory.get(mHistoryIndex));
			setHistoryButtonsState();
		}
	}

	private void setHistoryButtonsState() {
		mPrevBtn.setEnabled(mHistoryIndex > 0);
		mNextBtn.setEnabled(mHistoryIndex < mHistory.size() - 1);
	}

	private void updateSelectionColor() {
		if (mSource) {
			mImagePane.setBackground(COLOR_SOURCE);
		} else if (mDest) {
			mImagePane.setBackground(COLOR_DEST);
		} else {
			mImagePane.setBackground(COLOR_UNSELECTED);
		}
	}
}
