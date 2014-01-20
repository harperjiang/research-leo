package edu.clarkson.cs.wpcomp.img.desc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.desc.descriptor.HogSVMDescriptor;

public class NegativeSetGenerator {

	public static final int STATE_INIT = 1;

	public static final int STATE_LOAD_DONE = 2;

	public static final int STATE_PROCESS_DONE = 3;

	public static void main(String[] args) throws Exception {
		BlockingQueue<BufferedImage> imageQueue = new LinkedBlockingQueue<BufferedImage>(
				100);
		BlockingQueue<Feature> featureQueue = new LinkedBlockingQueue<Feature>(
				1000);
		HogSVMDescriptor hog = new HogSVMDescriptor(50, 1);
		AtomicInteger state = new AtomicInteger(1);

		InputThread inputThread = new InputThread(imageQueue, state);
		inputThread.start();

		OutputThread outputThread = new OutputThread(featureQueue, state);
		outputThread.start();

	}

	protected static class InputThread extends Thread {
		private Queue<BufferedImage> imageQueue;

		private AtomicInteger state;

		public InputThread(Queue<BufferedImage> imageQueue, AtomicInteger state) {
			super();
			this.imageQueue = imageQueue;
			this.state = state;
		}

		public void run() {
			File dir = new File("res/image/negative");
			for (File file : dir.listFiles()) {
				try {
					imageQueue.offer(ImageIO.read(file));
				} catch (IOException e) {
					System.out.println(e);
				}
			}
			state.set(STATE_LOAD_DONE);
		}
	}

	protected static class ProcessThread extends Thread {
		private Queue<BufferedImage> imageQueue;

		private Queue<Feature> featureQueue;

		private AtomicInteger state;

		public ProcessThread(Queue<BufferedImage> imageQueue,
				Queue<Feature> featureQueue, AtomicInteger state) {
			super();
			this.imageQueue = imageQueue;
			this.featureQueue = featureQueue;
			this.state = state;
		}

		@Override
		public void run() {
			while (!imageQueue.isEmpty() || state.get() != STATE_LOAD_DONE) {
				if (imageQueue.isEmpty()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
			}
		}
	}

	protected static class OutputThread extends Thread {

		private Queue<Feature> featureQueue;

		private AtomicInteger state;

		public OutputThread(Queue<Feature> featureQueue, AtomicInteger state) {
			super();
			this.featureQueue = featureQueue;
			this.state = state;
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
