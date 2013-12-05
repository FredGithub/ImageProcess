package ar.edu.itba.imageprocess;

import javax.swing.JFrame;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class Final {

	public static void main(String[] args) {
		try {
			hough(args);
		} catch (Exception e) {
		}
	}

	public static void hough(String[] args) {
		String fileName = args.length >= 1 ? args[0] : "res/img/dist02.jpg"; // if no params provided, compute the defaut image
		IplImage src = cvLoadImage(fileName, 0);
		IplImage dst;
		IplImage colorDst;
		CvMemStorage storage = cvCreateMemStorage(0);
		CvSeq lines = new CvSeq();

		CanvasFrame source = new CanvasFrame("Source");
		CanvasFrame hough = new CanvasFrame("Hough");
		if (src == null) {
			System.out.println("Couldn't load source image.");
			return;
		}

		dst = cvCreateImage(cvGetSize(src), src.depth(), 1);
		colorDst = cvCreateImage(cvGetSize(src), src.depth(), 3);

		cvCanny(src, dst, 50, 200, 3);
		cvCvtColor(dst, colorDst, CV_GRAY2BGR);
		String method = "probabilistic";
		int max = 5;
		
		/*
		 * apply the probabilistic hough transform
		 * which returns for each line deteced two points ((x1, y1); (x2,y2))
		 * defining the detected segment
		 */
		if (method.equals("probabilistic")) {
			System.out.println("Using the Probabilistic Hough Transform");
			lines = cvHoughLines2(dst, storage, CV_HOUGH_PROBABILISTIC, 1, Math.PI / 180, 40, 50, 10);
			for (int i = 0; i < Math.min(max, lines.total()); i++) {
				// Based on JavaCPP, the equivalent of the C code:
				// CvPoint* line = (CvPoint*)cvGetSeqElem(lines,i);
				// CvPoint first=line[0], second=line[1]
				// is:
				Pointer line = cvGetSeqElem(lines, i);
				CvPoint pt1 = new CvPoint(line).position(0);
				CvPoint pt2 = new CvPoint(line).position(1);

				System.out.println("Line spotted: ");
				System.out.println("\t pt1: " + pt1);
				System.out.println("\t pt2: " + pt2);
				cvLine(colorDst, pt1, pt2, CV_RGB(255, 0, 0), 3, CV_AA, 0); // draw the segment on the image
			}
		}
		/*
		 * Apply the multiscale hough transform which returns for each line two float parameters (rho, theta)
		 * rho: distance from the origin of the image to the line
		 * theta: angle between the x-axis and the normal line of the detected line
		 */
		else if (method.equals("multiscale")) {
			System.out.println("Using the multiscale Hough Transform"); //
			lines = cvHoughLines2(dst, storage, CV_HOUGH_MULTI_SCALE, 1, Math.PI / 180, 40, 1, 1);
			for (int i = 0; i < Math.min(max, lines.total()); i++) {
				CvPoint2D32f point = new CvPoint2D32f(cvGetSeqElem(lines, i));

				float rho = point.x();
				float theta = point.y();

				double a = Math.cos((double) theta), b = Math.sin((double) theta);
				double x0 = a * rho, y0 = b * rho;
				CvPoint pt1 = new CvPoint((int) Math.round(x0 + 1000 * (-b)), (int) Math.round(y0 + 1000 * (a))), pt2 = new CvPoint((int) Math.round(x0 - 1000 * (-b)), (int) Math.round(y0 - 1000
						* (a)));
				System.out.println("Line spoted: ");
				System.out.println("\t rho= " + rho);
				System.out.println("\t theta= " + theta);
				cvLine(colorDst, pt1, pt2, CV_RGB(255, 0, 0), 3, CV_AA, 0);
			}
		}
		/*
		 * Default: apply the standard hough transform. Outputs: same as the multiscale output.
		 */
		else {
			System.out.println("Using the Standard Hough Transform");
			lines = cvHoughLines2(dst, storage, CV_HOUGH_STANDARD, 1, Math.PI / 180, 90, 0, 0);
			for (int i = 0; i < Math.min(max, lines.total()); i++) {
				CvPoint2D32f point = new CvPoint2D32f(cvGetSeqElem(lines, i));

				float rho = point.x();
				float theta = point.y();

				double a = Math.cos((double) theta), b = Math.sin((double) theta);
				double x0 = a * rho, y0 = b * rho;
				CvPoint pt1 = new CvPoint((int) Math.round(x0 + 1000 * (-b)), (int) Math.round(y0 + 1000 * (a)));
				CvPoint pt2 = new CvPoint((int) Math.round(x0 - 1000 * (-b)), (int) Math.round(y0 - 1000 * (a)));
				System.out.println("Line spotted: ");
				System.out.println("\t rho= " + rho);
				System.out.println("\t theta= " + theta);
				cvLine(colorDst, pt1, pt2, CV_RGB(255, 0, 0), 3, CV_AA, 0);
			}
		}
		source.showImage(src);
		hough.showImage(colorDst);

		source.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hough.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void motion() throws Exception {
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		grabber.start();

		IplImage frame = grabber.grab();
		IplImage image = null;
		IplImage prevImage = null;
		IplImage diff = null;

		CanvasFrame canvasFrame = new CanvasFrame("Some Title");
		canvasFrame.setCanvasSize(frame.width(), frame.height());

		CvMemStorage storage = CvMemStorage.create();

		while (canvasFrame.isVisible() && (frame = grabber.grab()) != null) {
			cvClearMemStorage(storage);

			cvSmooth(frame, frame, CV_GAUSSIAN, 9, 9, 2, 2);
			if (image == null) {
				image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
				cvCvtColor(frame, image, CV_RGB2GRAY);
			} else {
				prevImage = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
				prevImage = image;
				image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
				cvCvtColor(frame, image, CV_RGB2GRAY);
			}

			if (diff == null) {
				diff = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
			}

			if (prevImage != null) {
				// perform ABS difference
				cvAbsDiff(image, prevImage, diff);
				// do some threshold for wipe away useless details
				cvThreshold(diff, diff, 64, 255, CV_THRESH_BINARY);

				canvasFrame.showImage(diff);

				// recognize contours
				CvSeq contour = new CvSeq(null);
				cvFindContours(diff, storage, contour, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
			}
		}
		grabber.stop();
		canvasFrame.dispose();
	}
}
