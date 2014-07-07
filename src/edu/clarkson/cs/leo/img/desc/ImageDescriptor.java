package edu.clarkson.cs.leo.img.desc;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import edu.clarkson.cs.leo.img.accessor.ImageAccessor;
import edu.clarkson.cs.leo.img.desc.descriptor.HogSVMDescriptor;
import edu.clarkson.cs.leo.img.transform.ImageTransformer;

public class ImageDescriptor {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BufferedImage bw = ImageIO
				.read(new File(
						"/home/harper/Research/webpage-comparison/imageset_test/12014939095_0c6dd934d6_b.jpg"));
		System.out.println(bw.getType());
		System.out.println(bw.getColorModel().getNumComponents());
		System.out.println(bw.getSampleModel().getNumBands());
		System.out.println(bw.getSampleModel().getDataType());
		BufferedImage scaled = ImageTransformer.scale(bw, 500, 500);
		ImageIO.write(scaled, "png", new File("test.png"));
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

}
