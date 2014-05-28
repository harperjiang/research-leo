package edu.clarkson.cs.wpcomp.img.desc;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.descriptor.HogSVMDescriptor;
import edu.clarkson.cs.wpcomp.img.transform.ImageTransformer;

public class ImageDescMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		describeGradient();
	}

	protected static void descripeHog() throws Exception {
		BufferedImage img = ImageIO.read(new File("res/sample.jpg"));
		ImageAccessor accessor = new ImageAccessor(img);

		System.out.println(System.currentTimeMillis());
		HogSVMDescriptor descriptor = new HogSVMDescriptor();
		Feature result = descriptor.describe(accessor);
		System.out.println(result.size());
		System.out.println(System.currentTimeMillis());
	}

	protected static void describeGradient() throws Exception {
		BufferedImage ebay1 = ImageTransformer.scale(
				ImageIO.read(new File("res/image/ebay_1.jpg")), 300, 300);
		BufferedImage ebay1_trans = ImageTransformer.scale(
				ImageTransformer.transform(ebay1,
						AffineTransform.getTranslateInstance(1, 1)), 300, 300);
		BufferedImage ebay2 = ImageTransformer.scale(
				ImageIO.read(new File("res/image/ebay_2.jpg")), 300, 300);
		BufferedImage ebay3 = ImageTransformer.scale(
				ImageIO.read(new File("res/image/ebay_3.png")), 300, 300);
		BufferedImage google = ImageTransformer.scale(
				ImageIO.read(new File("res/image/logo11w.png")), 300, 300);

		// ImageIO.write(GradientHelper.gradientImage(ebay1), "png", new File(
		// "res/output/ebay1.png"));
		// ImageIO.write(GradientHelper.gradientImage(ebay1_trans), "png",
		// new File("res/output/ebay1_trans.png"));
		// ImageIO.write(GradientHelper.gradientImage(ebay2), "png", new File(
		// "res/output/ebay2.png"));
		// ImageIO.write(GradientHelper.gradientImage(ebay3), "png", new File(
		// "res/output/ebay3.png"));
		// ImageIO.write(GradientHelper.gradientImage(google), "png", new File(
		// "res/output/google.png"));

	}
}
