package ar.edu.itba.imageprocess;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ar.edu.itba.imageprocess.utils.FileUtils;
import ar.edu.itba.imageprocess.utils.ImageFilter;
import ar.edu.itba.imageprocess.utils.Log;

@SuppressWarnings("serial")
public class MenuPane extends JPanel implements ActionListener {

	private static final int[] DEFAULT_GIRL = new int[] { 389, 164 };
	private static final int[] DEFAULT_GIRL2 = new int[] { 256, 256 };
	private static final int[] DEFAULT_BARCO = new int[] { 290, 207 };
	private static final int[] DEFAULT_LENA = new int[] { 256, 256 };
	private static final int[] DEFAULT_LENAX = new int[] { 256, 256 };
	private static final int[] DEFAULT_FRACTAL = new int[] { 200, 200 };

	private MainController mController;
	private JFileChooser mFileChooser;
	private HashMap<String, int[]> mDefaultDimensions;

	// file menu
	private JButton mLoadBtn;
	private JButton mSaveBtn;

	// simple process menu
	private JButton mGenerateWhite;
	private JButton mGenerateCircleBtn;
	private JButton mGenerateSquareBtn;
	private JButton mGenerateGradientBtn;
	private JButton mGenerateColorGradientBtn;
	private JButton mAddImages;
	private JButton mSubtractImages;
	private JButton mMultiplyScalar;
	private JButton mCompression;
	
	private JButton mFilterNegative;
	private JButton mFilterThreshold;

	// histogram menu
	private JButton mDesaturateBtn;
	private JButton mHistogramBtn;

	// noise and blur menu

	public MenuPane(MainController controller) {
		super(new GridBagLayout());

		mController = controller;
		GridBagConstraints c;

		mFileChooser = new JFileChooser(ImageProcess.IMG_PATH);
		mFileChooser.setFileFilter(new ImageFilter());

		mDefaultDimensions = new HashMap<String, int[]>();
		mDefaultDimensions.put("girl.raw", DEFAULT_GIRL);
		mDefaultDimensions.put("girl2.raw", DEFAULT_GIRL2);
		mDefaultDimensions.put("barco.raw", DEFAULT_BARCO);
		mDefaultDimensions.put("lena.raw", DEFAULT_LENA);
		mDefaultDimensions.put("lenax.raw", DEFAULT_LENAX);
		mDefaultDimensions.put("fractal.raw", DEFAULT_FRACTAL);

		JTabbedPane tabbedPane = new JTabbedPane();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		add(tabbedPane, c);

		// file menu

		JPanel menuFile = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("File", menuFile);

		mLoadBtn = new JButton("Load");
		mLoadBtn.addActionListener(this);
		menuFile.add(mLoadBtn);

		mSaveBtn = new JButton("Save");
		mSaveBtn.addActionListener(this);
		menuFile.add(mSaveBtn);

		// simple process menu

		JPanel menuShape = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Simple process", menuShape);

		mGenerateWhite = new JButton("White");
		mGenerateWhite.addActionListener(this);
		menuShape.add(mGenerateWhite);
		
		mGenerateCircleBtn = new JButton("Circle");
		mGenerateCircleBtn.addActionListener(this);
		menuShape.add(mGenerateCircleBtn);

		mGenerateSquareBtn = new JButton("Square");
		mGenerateSquareBtn.addActionListener(this);
		menuShape.add(mGenerateSquareBtn);

		mGenerateGradientBtn = new JButton("Gradient");
		mGenerateGradientBtn.addActionListener(this);
		menuShape.add(mGenerateGradientBtn);

		mGenerateColorGradientBtn = new JButton("Color gradient");
		mGenerateColorGradientBtn.addActionListener(this);
		menuShape.add(mGenerateColorGradientBtn);
		
		mAddImages = new JButton("Add");
		mAddImages.addActionListener(this);
		menuShape.add(mAddImages);
		
		mSubtractImages = new JButton("Subtract");
		mSubtractImages.addActionListener(this);
		menuShape.add(mSubtractImages);
		
		mMultiplyScalar = new JButton("Multiply");
		mMultiplyScalar.addActionListener(this);
		menuShape.add(mMultiplyScalar);
		
		mCompression = new JButton("Compression");
		mCompression.addActionListener(this);
		menuShape.add(mCompression);
		
		// file menu

		JPanel menu3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Filter", menu3);
		
		mFilterNegative = new JButton("Negative");
		mFilterNegative.addActionListener(this);
		menu3.add(mFilterNegative);

		mFilterThreshold = new JButton("Threshold");
		mFilterThreshold.addActionListener(this);
		menu3.add(mFilterThreshold);

		// histogram menu

		JPanel menuHistogram = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Histogram", menuHistogram);

		mHistogramBtn = new JButton("Histogram");
		mHistogramBtn.addActionListener(this);
		menuHistogram.add(mHistogramBtn);

		mDesaturateBtn = new JButton("Desaturate");
		mDesaturateBtn.addActionListener(this);
		menuHistogram.add(mDesaturateBtn);

		// noise and blur menu

		JPanel menuNoise = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Noise and blur", menuNoise);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mLoadBtn) {
			loadImage();
		} else if (e.getSource() == mSaveBtn) {
			saveImage();
		} else if (e.getSource() == mGenerateWhite) {
			mController.generateWhiteImage();
		} else if (e.getSource() == mGenerateCircleBtn) {
			mController.generateCircle();
		} else if (e.getSource() == mGenerateSquareBtn) {
			mController.generateSquare();
		} else if (e.getSource() == mGenerateGradientBtn) {
			mController.generateGradient();
		} else if (e.getSource() == mGenerateColorGradientBtn) {
			mController.generateColorGradient();
		} else if (e.getSource() == mAddImages) {
			mController.addImages();
		} else if (e.getSource() == mSubtractImages) {
			mController.subtractImages();
		} else if (e.getSource() == mMultiplyScalar) {
			mController.multiplyScalar();
		} else if (e.getSource() == mCompression) {
			mController.compress();
		} else if (e.getSource() == mFilterNegative) {
			mController.filterNegative();
		} else if (e.getSource() == mFilterThreshold) {
			mController.filterThreshold();
		} else if (e.getSource() == mHistogramBtn) {
			mController.displayHistogram();
		} else if (e.getSource() == mDesaturateBtn) {
			mController.desaturate();
		}
	}

	private void loadImage() {
		int returnVal = mFileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = mFileChooser.getSelectedFile();
			if (FileUtils.getFileExtension(file).equals("raw")) {
				ArrayList<Integer> dimensions = askImageDimension(file);
				Log.d("dimensions: " + dimensions);
				if (dimensions != null) {
					mController.loadImage(file, dimensions.get(0), dimensions.get(1));
				}
			} else {
				mController.loadImage(file, 0, 0);
			}
		}
	}

	private void saveImage() {
		int returnVal = mFileChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = mFileChooser.getSelectedFile();
			mController.saveImage(file);
		}
	}

	private ArrayList<Integer> askImageDimension(File file) {
		String[] messages = new String[] { "Image width:", "Image height:" };
		String message = messages[0];
		ArrayList<Integer> dimensions = new ArrayList<Integer>();

		while (dimensions.size() != messages.length) {
			String input;
			if (mDefaultDimensions.containsKey(file.getName())) {
				input = JOptionPane.showInputDialog(message, mDefaultDimensions.get(file.getName())[dimensions.size()]);
			} else {
				input = JOptionPane.showInputDialog(message);
			}
			if (input == null) {
				return null;
			}
			try {
				int dim = Integer.parseInt(input);
				if (dim <= 0) {
					message = "Invalid input. " + messages[dimensions.size()];
				} else {
					dimensions.add(dim);
					if (dimensions.size() < messages.length) {
						message = messages[dimensions.size()];
					}
				}
			} catch (NumberFormatException e) {
				message = "Invalid input. " + messages[dimensions.size()];
			}
		}
		return dimensions;
	}
}