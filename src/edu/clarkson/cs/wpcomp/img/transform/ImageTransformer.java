package edu.clarkson.cs.wpcomp.img.transform;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ImageTransformer {

	public static BufferedImage transform(BufferedImage input,
			AffineTransform xform) {
		int type = input.getSampleModel().getNumBands() == 3 ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage copy = new BufferedImage(input.getWidth(),
				input.getHeight(), type);
		copy.setData(input.getData());
		AffineTransformOp op = new AffineTransformOp(xform,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

		Raster output = op.filter(copy.getData(), null);

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
