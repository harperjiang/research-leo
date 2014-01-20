package edu.clarkson.cs.wpcomp.img.desc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.descriptor.HogSVMDescriptor;

public class NegativeSetGenerator {

	public static void main(String[] args) throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream("negative"));

		HogSVMDescriptor hog = new HogSVMDescriptor(50, 1);

		for (File file : new File("res/image/negative").listFiles()) {
			System.out.println(System.currentTimeMillis());
			BufferedImage image = ImageIO.read(file);
			Feature feature = hog.describe(new ImageAccessor(image));
			pw.println(feature);
		}
		pw.close();
	}
}
