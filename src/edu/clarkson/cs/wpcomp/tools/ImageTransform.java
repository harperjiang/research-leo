package edu.clarkson.cs.wpcomp.tools;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.transform.ImageTransformer;

public class ImageTransform {

	public static void main(String[] args) throws Exception {
		// Read image
		File folder = new File("/home/harper/Downloads/negativeImages");
		for (File file : folder.listFiles()) {
			BufferedImage image = ImageIO.read(file);
			BufferedImage output = ImageTransformer.transform(image,
					AffineTransform.getScaleInstance(500d / image.getWidth(),
							500d / image.getHeight()));
			ImageIO.write(output, "jpg",
					new File("res/image/negative/" + file.getName()));
		}
	}
}
