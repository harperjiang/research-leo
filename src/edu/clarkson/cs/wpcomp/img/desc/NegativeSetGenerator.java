package edu.clarkson.cs.wpcomp.img.desc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.descriptor.HogSVMDescriptor;

public class NegativeSetGenerator {

	public static void main(String[] args) throws Exception {
		BlockingQueue<BufferedImage> imageQueue = new LinkedBlockingQueue<BufferedImage>(
				100);
		BlockingQueue<Feature> featureQueue = new LinkedBlockingQueue<Feature>(
				1000);
		SVMDescriptor hog = new HogSVMDescriptor(50, 1);
		Semaphore outputLock = new Semaphore(0);

		int cpuCount = 4;
		OutputThread outputThread = new OutputThread(featureQueue, outputLock);
		outputThread.start();

		ProcessThread[] threadPool = new ProcessThread[cpuCount];
		for (int i = 0; i < cpuCount; i++) {
			threadPool[i] = new ProcessThread(imageQueue, featureQueue, hog);
			threadPool[i].start();
		}

		int counter = 0;
		File dir = new File("res/image/negative");
		for (File file : dir.listFiles()) {
			try {
				imageQueue.put(ImageIO.read(file));
				counter++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		outputLock.acquire(counter);
		for (int i = 0; i < cpuCount; i++) {
			threadPool[i].setExit(true);
		}
	}

	protected static class ProcessThread extends Thread {
		private BlockingQueue<BufferedImage> imageQueue;
		private BlockingQueue<Feature> featureQueue;
		private SVMDescriptor desc;
		private boolean exit = false;

		public ProcessThread(BlockingQueue<BufferedImage> imageQueue,
				BlockingQueue<Feature> featureQueue, SVMDescriptor desc) {
			super();
			setDaemon(true);
			this.imageQueue = imageQueue;
			this.featureQueue = featureQueue;
			this.desc = desc;
		}

		public void setExit(boolean t) {
			this.exit = t;
		}

		@Override
		public void run() {
			while (!exit) {
				try {
					BufferedImage image = imageQueue.poll(1, TimeUnit.SECONDS);
					if (image == null) {
						continue;
					} else {
						featureQueue.put(desc
								.describe(new ImageAccessor(image)));
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected static class OutputThread extends Thread {

		private BlockingQueue<Feature> featureQueue;

		private Semaphore lock;

		public OutputThread(BlockingQueue<Feature> featureQueue, Semaphore lock) {
			super();
			this.setDaemon(true);
			this.featureQueue = featureQueue;
			this.lock = lock;
		}

		public void run() {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream("negative"));
				while (true) {
					Feature feature;
					try {
						feature = featureQueue.take();
						pw.println(MessageFormat.format("{0} {1}", 0, feature));
						lock.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				pw.close();
			}
		}
	}
}
