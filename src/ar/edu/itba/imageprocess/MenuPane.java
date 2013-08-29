package ar.edu.itba.imageprocess;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ar.edu.itba.imageprocess.ParamAsker.Param;
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
	private JButton mEqualizeBtn;

	// noise and mask menu
	private JButton mApplyAddGaussian;
	private JButton mApplyMulRayleigh;
	private JButton mApplyMulExponential;
	private JButton mPepperAndSalt;
	private JButton mMaskAverage;
	private JButton mMaskGaussian;
	private JButton mMaskHighPass;
	private JButton mMaskMedian;

	// test menu
	private JButton mGaussianTest;
	private JButton mRayleighTest;
	private JButton mExponentialTest;

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

		// filter menu

		JPanel menuFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Filter", menuFilter);

		mFilterNegative = new JButton("Negative");
		mFilterNegative.addActionListener(this);
		menuFilter.add(mFilterNegative);

		mFilterThreshold = new JButton("Threshold");
		mFilterThreshold.addActionListener(this);
		menuFilter.add(mFilterThreshold);

		// histogram menu

		JPanel menuHistogram = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Histogram", menuHistogram);

		mHistogramBtn = new JButton("Histogram");
		mHistogramBtn.addActionListener(this);
		menuHistogram.add(mHistogramBtn);

		mDesaturateBtn = new JButton("Desaturate");
		mDesaturateBtn.addActionListener(this);
		menuHistogram.add(mDesaturateBtn);

		mEqualizeBtn = new JButton("Equalize");
		mEqualizeBtn.addActionListener(this);
		menuHistogram.add(mEqualizeBtn);

		// noise and mask menu

		JPanel menuNoise = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Noise and mask", menuNoise);

		mApplyAddGaussian = new JButton("Add Gaussian");
		mApplyAddGaussian.addActionListener(this);
		menuNoise.add(mApplyAddGaussian);

		mApplyMulRayleigh = new JButton("Mul Rayleigh");
		mApplyMulRayleigh.addActionListener(this);
		menuNoise.add(mApplyMulRayleigh);

		mApplyMulExponential = new JButton("Mul Exponential");
		mApplyMulExponential.addActionListener(this);
		menuNoise.add(mApplyMulExponential);

		mPepperAndSalt = new JButton("Pepper and salt");
		mPepperAndSalt.addActionListener(this);
		menuNoise.add(mPepperAndSalt);

		mMaskAverage = new JButton("Average mask");
		mMaskAverage.addActionListener(this);
		menuNoise.add(mMaskAverage);

		mMaskGaussian = new JButton("Gaussian mask");
		mMaskGaussian.addActionListener(this);
		// menuNoise.add(mMaskGaussian);

		mMaskHighPass = new JButton("High pass mask");
		mMaskHighPass.addActionListener(this);
		menuNoise.add(mMaskHighPass);

		mMaskMedian = new JButton("Median mask");
		mMaskMedian.addActionListener(this);
		menuNoise.add(mMaskMedian);

		// noise and mask menu

		JPanel menuTest = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Tests", menuNoise);

		mGaussianTest = new JButton("Gaussian test");
		mGaussianTest.addActionListener(this);
		menuTest.add(mGaussianTest);

		mRayleighTest = new JButton("Rayleigh test");
		mRayleighTest.addActionListener(this);
		menuTest.add(mRayleighTest);

		mExponentialTest = new JButton("Exponential test");
		mExponentialTest.addActionListener(this);
		menuTest.add(mExponentialTest);
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
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "scalar", "1.5"));
			if (params.ask()) {
				mController.multiplyScalar(params.getDouble("scalar"));
			}
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
		} else if (e.getSource() == mEqualizeBtn) {
			mController.filterEqualize();
		} else if (e.getSource() == mApplyAddGaussian) {
			mController.applyAddGaussianNoise();
		} else if (e.getSource() == mApplyMulRayleigh) {
			mController.applyMulRayleighNoise();
		} else if (e.getSource() == mApplyMulExponential) {
			mController.applyMulExponentialNoise();
		} else if (e.getSource() == mPepperAndSalt) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "p0", 0, 1, "0.02"));
			params.addParam(new Param(Param.TYPE_DOUBLE, "p1", 0, 1, "0.98"));
			if (params.ask()) {
				mController.applyPepperAndSalt(params.getDouble("p0"), params.getDouble("p1"));
			}
		} else if (e.getSource() == mMaskAverage) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_INTEGER, "width", 1, 10, "3"));
			params.addParam(new Param(Param.TYPE_INTEGER, "height", 1, 10, "3"));
			if (params.ask()) {
				mController.applyFactorMaskFilter(params.getInteger("width"), params.getInteger("height"), Filters.MASK_FILTER_AVERAGE);
			}
		} else if (e.getSource() == mMaskGaussian) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_INTEGER, "width", 1, 10, "3"));
			params.addParam(new Param(Param.TYPE_INTEGER, "height", 1, 10, "3"));
			if (params.ask()) {
				mController.applyFactorMaskFilter(params.getInteger("width"), params.getInteger("height"), Filters.MASK_FILTER_GAUSSIAN);
			}
		} else if (e.getSource() == mMaskHighPass) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_INTEGER, "width", 1, 10, "3"));
			params.addParam(new Param(Param.TYPE_INTEGER, "height", 1, 10, "3"));
			if (params.ask()) {
				mController.applyFactorMaskFilter(params.getInteger("width"), params.getInteger("height"), Filters.MASK_FILTER_HIGH_PASS);
			}
		} else if (e.getSource() == mMaskMedian) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_INTEGER, "width", 1, 10, "3"));
			params.addParam(new Param(Param.TYPE_INTEGER, "height", 1, 10, "3"));
			if (params.ask()) {
				mController.applyMedianMaskFilter(params.getInteger("width"), params.getInteger("height"));
			}
		} else if (e.getSource() == mGaussianTest) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "spread", "1"));
			params.addParam(new Param(Param.TYPE_DOUBLE, "average", "0"));
			if (params.ask()) {
				mController.displayGaussianChart(params.getDouble("spread"), params.getDouble("average"));
			}
		} else if (e.getSource() == mRayleighTest) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "param", "0.5"));
			if (params.ask()) {
				mController.displayRayleighChart(params.getDouble("param"));
			}
		} else if (e.getSource() == mExponentialTest) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "param", "0.5"));
			if (params.ask()) {
				mController.displayExponentialChart(params.getDouble("param"));
			}
		}
	}

	private void loadImage() {
		int returnVal = mFileChooser.showOpenDialog(this);

		// if the user selected a file
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = mFileChooser.getSelectedFile();

			// if the file is a raw file, we need to ask the image size
			if (FileUtils.getFileExtension(file).equals("raw")) {
				int[] defaultSize = mDefaultDimensions.get(file.getName());
				ParamAsker params = new ParamAsker();
				// if the default size exists for that file
				if (defaultSize != null) {
					params.addParam(new Param(Param.TYPE_INTEGER, "width", 0, 5000, String.valueOf(defaultSize[0])));
					params.addParam(new Param(Param.TYPE_INTEGER, "height", 0, 5000, String.valueOf(defaultSize[1])));
				} else {
					params.addParam(new Param(Param.TYPE_INTEGER, "width", 0, 5000));
					params.addParam(new Param(Param.TYPE_INTEGER, "height", 0, 5000));
				}
				// if the user enters all the parameters
				if (params.ask()) {
					int width = params.getInteger("width");
					int height = params.getInteger("height");
					Log.d("dimensions: " + width + ", " + height);
					mController.loadImage(file, width, height);
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
}
