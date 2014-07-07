package edu.clarkson.cs.leo.task;

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

import edu.clarkson.cs.leo.img.accessor.ImageAccessor;
import edu.clarkson.cs.leo.img.desc.Feature;
import edu.clarkson.cs.leo.img.desc.SVMDescriptor;
import edu.clarkson.cs.leo.img.desc.descriptor.HogSVMDescriptor;
import edu.clarkson.cs.leo.img.transform.ImageTransformer;

public class GenNegativeImgSet {

	public static void main(String[] args) throws Exception {
		BlockingQueue<ImageFile> imageQueue = new LinkedBlockingQueue<ImageFile>(
				100);
		BlockingQueue<Feature> featureQueue = new LinkedBlockingQueue<Feature>(
				1000);
		SVMDescriptor hog = new HogSVMDescriptor(25, 1);
		Semaphore outputLock = new Semaphore(0);

		int cpuCount = 4;
		OutputThread outputThread = new OutputThread(featureQueue, outputLock);
		outputThread.start();

		ProcessThread[] threadPool = new ProcessThread[cpuCount];
		for (int i = 0; i < cpuCount; i++) {
			threadPool[i] = new ProcessThread(imageQueue, featureQueue, hog);
			threadPool[i].start();
		}

		File dir = new File(
				"/home/harper/ResearchData/webpage-comparison/imageset_test");
		for (File file : dir.listFiles()) {
			try {
				imageQueue
						.put(new ImageFile(ImageIO.read(file), file.getName()));
			} catch (Exception e) {
				System.err.println(file.getName());
				e.printStackTrace();
			}
		}
		outputLock.acquire(imageQueue.size());
		for (int i = 0; i < cpuCount; i++) {
			threadPool[i].setExit(true);
		}
		outputThread.setExit(true);
	}

	protected static class ProcessThread extends Thread {
		private BlockingQueue<ImageFile> imageQueue;
		private BlockingQueue<Feature> featureQueue;
		private SVMDescriptor desc;
		private boolean exit = false;

		public ProcessThread(BlockingQueue<ImageFile> imageQueue,
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
					ImageFile imageFile = imageQueue.poll(1, TimeUnit.SECONDS);
					if (imageFile == null) {
						continue;
					} else {
						try {
							BufferedImage transformed = ImageTransformer.scale(
									imageFile.image, 500, 500);
							featureQueue.put(desc.describe(new ImageAccessor(
									transformed)));
						} catch (Exception e) {
							System.err.println("Error with " + imageFile.name);
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected static class OutputThread extends Thread {

		private BlockingQueue<Feature> featureQueue;

		private Semaphore lock;

		private boolean exit = false;

		public OutputThread(BlockingQueue<Feature> featureQueue, Semaphore lock) {
			super();
			this.featureQueue = featureQueue;
			this.lock = lock;
		}

		public void setExit(boolean exit) {
			this.exit = exit;
		}

		public void run() {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream(
						"workdir/model/imageset_test"));
				while (!exit) {
					Feature feature;
					try {
						feature = featureQueue.poll(1, TimeUnit.SECONDS);
						if (feature == null)
							continue;
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

	public static final class ImageFile {
		public BufferedImage image;
		public String name;

		public ImageFile(BufferedImage image, String name) {
			this.image = image;
			this.name = name;
		}
	}
}
