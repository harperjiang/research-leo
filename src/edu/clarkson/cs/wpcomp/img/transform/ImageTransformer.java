package edu.clarkson.cs.wpcomp.img.transform;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ImageTransformer {

	public static BufferedImage transform(BufferedImage input,
			AffineTransform xform) {
		// BufferedImage copy = null;
		// if (input.getSampleModel().getNumBands() == 3) {
		// copy = new BufferedImage(input.getWidth(), input.getHeight(),
		// BufferedImage.TYPE_INT_RGB);
		// } else if (input.getType() == BufferedImage.TYPE_INT_ARGB
		// || input.getType() == BufferedImage.TYPE_INT_RGB
		// || input.getType() == BufferedImage.TYPE_BYTE_GRAY) {
		// copy = new BufferedImage(input.getWidth(), input.getHeight(),
		// input.getType());
		// } else {
		// throw new RuntimeException("Unrecognized Type:" + input.getType());
		// }
		// copy.setData(input.getData());
		AffineTransformOp op = new AffineTransformOp(xform,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

		Raster output = op.filter(input.getData(), null);

		BufferedImage oimage = new BufferedImage(output.getWidth(),
				output.getHeight(), input.getType());
		oimage.setData(output);
		return oimage;
	}

	public static BufferedImage scale(BufferedImage input, int width, int height) {
		AffineTransform xform = AffineTransform.getScaleInstance(
				((double) width) / input.getWidth(),
				((double) height) / input.getHeight());
		return transform(input, xform);
	}

	public static AffineTransform centerRotate(double centerx, double centery,
			double theta) {
		AffineTransform xform = AffineTransform.getTranslateInstance(centerx,
				centery);
		xform.concatenate(AffineTransform.getRotateInstance(theta));
		xform.concatenate(AffineTransform.getTranslateInstance(-centerx,
				-centery));
		return xform;
	}

	public static AffineTransform centerScale(double centerx, double centery,
			double scale) {
		AffineTransform xform = AffineTransform.getTranslateInstance(centerx,
				centery);
		xform.concatenate(AffineTransform.getScaleInstance(scale, scale));
		xform.concatenate(AffineTransform.getTranslateInstance(
				-centerx / scale, -centery / scale));
		return xform;
	}
}
