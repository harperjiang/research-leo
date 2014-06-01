package edu.clarkson.cs.wpcomp.img.transform;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;

public class ImageTransformer {

	private static BufferedImage adjust(BufferedImage input) {
		if (input.getSampleModel() instanceof PixelInterleavedSampleModel) {
			// Copy this image to a new buffer
			BufferedImage copy = new BufferedImage(input.getWidth(),
					input.getHeight(), BufferedImage.TYPE_INT_ARGB);
			copy.createGraphics().drawImage(input, 0, 0, null);
			return copy;
		}
		return input;
	}

	public static BufferedImage transform(BufferedImage input,
			AffineTransform xform) {
		// Java bug http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4723021
		// If PixelInterleavedSampleModel is used, create a new BufferedImage
		// and copy data into it
		BufferedImage adjusted = adjust(input);
		AffineTransformOp op = new AffineTransformOp(xform,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

		Raster output = op.filter(adjusted.getData(), null);

		BufferedImage oimage = new BufferedImage(output.getWidth(),
				output.getHeight(), adjusted.getType());
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
