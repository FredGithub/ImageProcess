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
	private JButton mLinearCompression;
	private JButton mCompression;

	// histogram menu
	private JButton mFilterNegative;
	private JButton mFilterThreshold;
	private JButton mGlobalThreshold;
	private JButton mOtsuThreshold;
	private JButton mOtsuColorThreshold;
	private JButton mHistogramBtn;
	private JButton mBlackAndWhiteBtn;
	private JButton mContrastBtn;
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

	// borders menu
	private JButton mRobertsBorders;
	private JButton mPrewittBorders;
	private JButton mSobelBorders;
	private JButton mSimpleBorders;
	private JButton mKirshBorders;
	private JButton mLaplacianBorders;
	private JButton mLaplacianGaussianBorders;
	private JButton mIsotropic;
	private JButton mAnisotropic;

	// borders 2 menu
	private JButton mNonMaximum;

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

		mLinearCompression = new JButton("Linear compression");
		mLinearCompression.addActionListener(this);
		menuShape.add(mLinearCompression);

		mCompression = new JButton("Compression");
		mCompression.addActionListener(this);
		menuShape.add(mCompression);

		// histogram menu

		JPanel menuHistogram = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Color process", menuHistogram);

		mFilterNegative = new JButton("Negative");
		mFilterNegative.addActionListener(this);
		menuHistogram.add(mFilterNegative);

		mFilterThreshold = new JButton("Threshold");
		mFilterThreshold.addActionListener(this);
		menuHistogram.add(mFilterThreshold);

		mGlobalThreshold = new JButton("Global threshold");
		mGlobalThreshold.addActionListener(this);
		menuHistogram.add(mGlobalThreshold);

		mOtsuThreshold = new JButton("Otsu");
		mOtsuThreshold.addActionListener(this);
		menuHistogram.add(mOtsuThreshold);

		mOtsuColorThreshold = new JButton("Otsu color");
		mOtsuColorThreshold.addActionListener(this);
		menuHistogram.add(mOtsuColorThreshold);

		mHistogramBtn = new JButton("Histogram");
		mHistogramBtn.addActionListener(this);
		menuHistogram.add(mHistogramBtn);

		mBlackAndWhiteBtn = new JButton("Black and white");
		mBlackAndWhiteBtn.addActionListener(this);
		menuHistogram.add(mBlackAndWhiteBtn);

		mContrastBtn = new JButton("Linear contrast");
		mContrastBtn.addActionListener(this);
		menuHistogram.add(mContrastBtn);

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

		mApplyMulExponential = new JButton("Mul Exp");
		mApplyMulExponential.addActionListener(this);
		menuNoise.add(mApplyMulExponential);

		mPepperAndSalt = new JButton("Pepper salt");
		mPepperAndSalt.addActionListener(this);
		menuNoise.add(mPepperAndSalt);

		mMaskAverage = new JButton("Avg mask");
		mMaskAverage.addActionListener(this);
		menuNoise.add(mMaskAverage);

		mMaskHighPass = new JButton("High pass mask");
		mMaskHighPass.addActionListener(this);
		menuNoise.add(mMaskHighPass);

		mMaskGaussian = new JButton("Gaussian mask");
		mMaskGaussian.addActionListener(this);
		menuNoise.add(mMaskGaussian);

		mMaskMedian = new JButton("Median mask");
		mMaskMedian.addActionListener(this);
		menuNoise.add(mMaskMedian);

		// borders menu

		JPanel menuBorders = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Borders", menuBorders);

		mRobertsBorders = new JButton("Roberts");
		mRobertsBorders.addActionListener(this);
		menuBorders.add(mRobertsBorders);

		mPrewittBorders = new JButton("Prewitt");
		mPrewittBorders.addActionListener(this);
		menuBorders.add(mPrewittBorders);

		mSobelBorders = new JButton("Sobel");
		mSobelBorders.addActionListener(this);
		menuBorders.add(mSobelBorders);

		mSimpleBorders = new JButton("Simple");
		mSimpleBorders.addActionListener(this);
		menuBorders.add(mSimpleBorders);

		mKirshBorders = new JButton("Kirsh");
		mKirshBorders.addActionListener(this);
		menuBorders.add(mKirshBorders);

		mLaplacianBorders = new JButton("Laplacian");
		mLaplacianBorders.addActionListener(this);
		menuBorders.add(mLaplacianBorders);

		mLaplacianGaussianBorders = new JButton("Laplacian gau.");
		mLaplacianGaussianBorders.addActionListener(this);
		menuBorders.add(mLaplacianGaussianBorders);

		mIsotropic = new JButton("Isotropic");
		mIsotropic.addActionListener(this);
		menuBorders.add(mIsotropic);

		mAnisotropic = new JButton("Anisotropic");
		mAnisotropic.addActionListener(this);
		menuBorders.add(mAnisotropic);

		// borders 2 menu

		JPanel menuBorders2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Borders 2", menuBorders2);

		mNonMaximum = new JButton("Non maximum");
		mNonMaximum.addActionListener(this);
		menuBorders2.add(mNonMaximum);

		// test menu

		JPanel menuTest = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("Tests", menuTest);

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
		} else if (e.getSource() == mLinearCompression) {
			mController.compressLinear();
		} else if (e.getSource() == mCompression) {
			mController.compress();
		} else if (e.getSource() == mFilterNegative) {
			mController.filterNegative();
		} else if (e.getSource() == mFilterThreshold) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_INTEGER, "threshold", "128"));
			if (params.ask()) {
				mController.filterThreshold(params.getInteger("threshold"));
			}
		} else if (e.getSource() == mGlobalThreshold) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "delta", 0, 100, "5"));
			if (params.ask()) {
				mController.globalThreshold(params.getDouble("delta"));
			}
		} else if (e.getSource() == mOtsuThreshold) {
			mController.otsuThreshold();
		} else if (e.getSource() == mOtsuColorThreshold) {
			mController.otsuColorThreshold();
		} else if (e.getSource() == mHistogramBtn) {
			mController.displayHistogram();
		} else if (e.getSource() == mBlackAndWhiteBtn) {
			mController.desaturate();
		} else if (e.getSource() == mContrastBtn) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_INTEGER, "r1", 0, 255, "80"));
			params.addParam(new Param(Param.TYPE_INTEGER, "r2", 0, 255, "180"));
			params.addParam(new Param(Param.TYPE_INTEGER, "s1", 0, 255, "40"));
			params.addParam(new Param(Param.TYPE_INTEGER, "s2", 0, 255, "220"));
			if (params.ask()) {
				mController.filterContrast(params.getInteger("r1"), params.getInteger("r2"), params.getInteger("s1"), params.getInteger("s2"));
			}
		} else if (e.getSource() == mEqualizeBtn) {
			mController.filterEqualize();
		} else if (e.getSource() == mApplyAddGaussian) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "spread", "5"));
			params.addParam(new Param(Param.TYPE_DOUBLE, "average", "10"));
			params.addParam(new Param(Param.TYPE_DOUBLE, "percentage", 0, 1, "0.2"));
			if (params.ask()) {
				mController.applyAddGaussianNoise(params.getDouble("spread"), params.getDouble("average"), params.getDouble("percentage"));
			}
		} else if (e.getSource() == mApplyMulRayleigh) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "p", "0.5"));
			params.addParam(new Param(Param.TYPE_DOUBLE, "percentage", 0, 1, "0.2"));
			if (params.ask()) {
				mController.applyMulRayleighNoise(params.getDouble("p"), params.getDouble("percentage"));
			}
		} else if (e.getSource() == mApplyMulExponential) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "p", "0.5"));
			params.addParam(new Param(Param.TYPE_DOUBLE, "percentage", 0, 1, "0.2"));
			if (params.ask()) {
				mController.applyMulExponentialNoise(params.getDouble("p"), params.getDouble("percentage"));
			}
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
			params.addParam(new Param(Param.TYPE_DOUBLE, "spread", "2"));
			params.addParam(new Param(Param.TYPE_INTEGER, "width", "0"));
			params.addParam(new Param(Param.TYPE_INTEGER, "height", "0"));
			if (params.ask()) {
				mController.applyGaussianMaskFilter(params.getInteger("width"), params.getInteger("height"), params.getDouble("spread"));
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
			params.addParam(new Param(Param.TYPE_DOUBLE, "p", "0.5"));
			if (params.ask()) {
				mController.displayRayleighChart(params.getDouble("p"));
			}
		} else if (e.getSource() == mExponentialTest) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "p", "0.5"));
			if (params.ask()) {
				mController.displayExponentialChart(params.getDouble("p"));
			}
		} else if (e.getSource() == mRobertsBorders) {
			mController.robertsBordersDetection();
		} else if (e.getSource() == mPrewittBorders) {
			mController.prewittBordersDetection();
		} else if (e.getSource() == mSobelBorders) {
			mController.sobelBordersDetection();
		} else if (e.getSource() == mSimpleBorders) {
			mController.simpleBordersDetection();
		} else if (e.getSource() == mKirshBorders) {
			mController.kirshBordersDetection();
		} else if (e.getSource() == mLaplacianBorders) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_INTEGER, "threshold", "30"));
			if (params.ask()) {
				mController.laplacianBordersDetection(params.getInteger("threshold"));
			}
		} else if (e.getSource() == mLaplacianGaussianBorders) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "sigma", "2"));
			params.addParam(new Param(Param.TYPE_INTEGER, "width", "13"));
			params.addParam(new Param(Param.TYPE_INTEGER, "height", "13"));
			params.addParam(new Param(Param.TYPE_INTEGER, "threshold", "30"));
			if (params.ask()) {
				mController.laplacianGaussianBordersDetection(params.getInteger("width"), params.getInteger("height"), params.getDouble("sigma"), params.getInteger("threshold"));
			}
		} else if (e.getSource() == mIsotropic) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_DOUBLE, "sigma", "1"));
			params.addParam(new Param(Param.TYPE_INTEGER, "width", "7"));
			params.addParam(new Param(Param.TYPE_INTEGER, "height", "7"));
			if (params.ask()) {
				mController.isotropicFilter(params.getInteger("width"), params.getInteger("height"), params.getDouble("sigma"));
			}
		} else if (e.getSource() == mAnisotropic) {
			ParamAsker params = new ParamAsker();
			params.addParam(new Param(Param.TYPE_INTEGER, "steps", "20"));
			params.addParam(new Param(Param.TYPE_DOUBLE, "sigma", "3"));
			params.addParam(new Param(Param.TYPE_INTEGER, "method", 1, 2, "1"));
			if (params.ask()) {
				mController.anisotropicFilter(params.getInteger("steps"), params.getDouble("sigma"), params.getInteger("method"));
			}
		} else if (e.getSource() == mNonMaximum) {
			mController.nonMaximum();
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
