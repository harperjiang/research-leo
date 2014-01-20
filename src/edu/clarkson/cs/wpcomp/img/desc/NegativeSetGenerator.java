package edu.clarkson.cs.wpcomp.img.desc;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.descriptor.HogSVMDescriptor;

public class NegativeSetGenerator {

	public static void main(String[] args) throws Exception {
		final BlockingQueue<BufferedImage> imageQueue = new LinkedBlockingQueue<BufferedImage>(
				100);
		final BlockingQueue<Feature> featureQueue = new LinkedBlockingQueue<Feature>(
				1000);
		final HogSVMDescriptor hog = new HogSVMDescriptor(50, 1);

		int cpuCount = 4;

		for (int i = 0; i < cpuCount; i++) {
			Thread thread = new Thread() {
				public void run() {
					BufferedImage image = imageQueue.poll();
					Feature feature = hog.describe(new ImageAccessor(image));
					featureQueue.offer(feature);
				}
			};
			thread.start();
		}

		OutputThread outputThread = new OutputThread(featureQueue);
		outputThread.start();

	}

	protected static class OutputThread extends Thread {

		private Queue<Feature> featureQueue;

		private boolean inputDone;

		public OutputThread(Queue<Feature> featureQueue) {
			super();
			this.featureQueue = featureQueue;
		}

		public boolean isInputDone() {
			return inputDone;
		}

		public void setInputDone(boolean inputDone) {
			this.inputDone = inputDone;
		}

		public void run() {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream("negative"));
				while (!(inputDone && featureQueue.isEmpty())) {
					Feature feature = featureQueue.poll();
					pw.println(MessageFormat.format("{0} {1}", 0, feature));
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				pw.close();
			}
		}
	}
}
