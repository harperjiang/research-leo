package edu.clarkson.cs.leo.task.textdetect;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import edu.clarkson.cs.leo.img.GradientHelper;
import edu.clarkson.cs.leo.img.accessor.ImageAccessor;
import edu.clarkson.cs.leo.img.desc.Feature;
import edu.clarkson.cs.leo.img.textdetect.TextImageDescriptor;
import edu.clarkson.cs.leo.img.transform.ImageTransformer;

public class GenNegativeSetForText {

	public static void main(String[] args) throws Exception {

		File folder = new File(
				"/home/harper/ResearchData/webpage-comparison/imageset_test");

		BlockingQueue<BufferedImage> inputQueue = new LinkedBlockingQueue<BufferedImage>(
				100);
		BlockingQueue<Feature> outputQueue = new LinkedBlockingQueue<Feature>(
				1000);
		Semaphore lock = new Semaphore(0);
		int count = 0;

		int cpuCount = 4;

		List<ExecutionThread> threadPool = new ArrayList<ExecutionThread>();
		for (int i = 0; i < cpuCount; i++) {
			ExecutionThread thread = new ExecutionThread(inputQueue,
					outputQueue);
			threadPool.add(thread);
			thread.start();
		}
		OutputThread outputThread = new OutputThread(outputQueue, lock);
		outputThread.start();

		for (File file : folder.listFiles()) {
			try {
				inputQueue.put(ImageIO.read(file));
				count++;
			} catch (Exception e) {
				// Ignore sliently
			}
		}

		lock.acquire(count);

		for (int i = 0; i < threadPool.size(); i++) {
			threadPool.get(i).setExit(true);
		}
		outputThread.setExit(true);
	}

	private static TextImageDescriptor desc = new TextImageDescriptor();

	private static Feature generateFeature(BufferedImage image)
			throws IOException {
		BufferedImage gradient = GradientHelper.gradientImage(image, 20);
		BufferedImage scaled = ImageTransformer.scale(gradient,
				(int) (50 * (double) gradient.getWidth() / (double) gradient
						.getHeight()), 50);
		return desc.describe(new ImageAccessor(scaled));
	}

	public static class OutputThread extends Thread {
		private boolean exit;
		private BlockingQueue<Feature> output;

		private Semaphore lock;

		public OutputThread(BlockingQueue<Feature> output, Semaphore lock) {
			super();
			this.output = output;
			this.lock = lock;
		}

		public boolean isExit() {
			return exit;
		}

		public void setExit(boolean exit) {
			this.exit = exit;
		}

		public void run() {
			try {
				PrintWriter pw = new PrintWriter(new FileOutputStream(
						"workdir/textdetect/text_negative"));
				while (!exit) {
					Feature feature = output.poll(1, TimeUnit.SECONDS);
					if (null == feature)
						continue;
					pw.println(MessageFormat.format("{0} {1}", 0, feature));
					lock.release();
				}
				pw.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class ExecutionThread extends Thread {
		private BlockingQueue<BufferedImage> input;

		private BlockingQueue<Feature> output;

		private boolean exit;

		public ExecutionThread(BlockingQueue<BufferedImage> input,
				BlockingQueue<Feature> output) {
			this.input = input;
			this.output = output;
		}

		public boolean isExit() {
			return exit;
		}

		public void setExit(boolean exit) {
			this.exit = exit;
		}

		public void run() {
			while (!exit) {
				try {
					BufferedImage image = input.poll(1, TimeUnit.SECONDS);
					if (null == image)
						continue;
					output.put(generateFeature(image));
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (Exception e) {
					// Ignore
					e.printStackTrace();
				}
			}
		}
	}
}
