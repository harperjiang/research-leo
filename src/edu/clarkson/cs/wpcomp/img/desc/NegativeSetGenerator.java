package edu.clarkson.cs.wpcomp.img.desc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.descriptor.HogSVMDescriptor;

public class NegativeSetGenerator {

	public static final int STATE_INIT = 1;

	public static final int STATE_LOAD_DONE = 2;

	public static final int STATE_PROCESS_DONE = 3;

	public static void main(String[] args) throws Exception {
		BlockingQueue<Feature> featureQueue = new LinkedBlockingQueue<Feature>(
				1000);
		SVMDescriptor hog = new HogSVMDescriptor(50, 1);
		Semaphore outputLock = new Semaphore(0);
		int cpuCount = 4;
		ExecutorService threadPool = Executors.newFixedThreadPool(cpuCount);

		OutputThread outputThread = new OutputThread(featureQueue, outputLock);
		outputThread.start();

		int counter = 0;
		File dir = new File("res/image/negative");
		for (File file : dir.listFiles()) {
			try {
				threadPool.submit(new ProcessImage(ImageIO.read(file),
						featureQueue, hog));
				counter++;
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		outputLock.acquire(counter);
		threadPool.shutdown();
	}

	protected static class ProcessImage implements Runnable {
		private BufferedImage image;
		private Queue<Feature> featureQueue;

		private SVMDescriptor desc;

		public ProcessImage(BufferedImage image, Queue<Feature> featureQueue,
				SVMDescriptor desc) {
			super();
			this.image = image;
			this.featureQueue = featureQueue;
			this.desc = desc;
		}

		@Override
		public void run() {
			featureQueue.add(desc.describe(new ImageAccessor(image)));
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
