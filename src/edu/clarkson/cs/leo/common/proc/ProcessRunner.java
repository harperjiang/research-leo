package edu.clarkson.cs.leo.common.proc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessRunner {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String[] commands;

	private OutputHandler handler;

	private File currentDir;

	private transient Process process;

	public ProcessRunner(String... commands) {
		this.commands = commands;
	}

	/**
	 * Wait until the process ends
	 * 
	 * @throws InterruptedException
	 */
	public int runAndWait() throws InterruptedException, IOException {
		Logger logger = LoggerFactory.getLogger(getClass());

		ProcessBuilder builder = new ProcessBuilder(commands);
		builder.redirectErrorStream(true);
		if (null != currentDir) {
			builder.directory(currentDir);
		}

		process = builder.start();
		int result = process.waitFor();
		if (null != handler) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
			while (null != (line = br.readLine())) {
				if (logger.isDebugEnabled()) {
					logger.debug(line);
				}
				handler.output(line);
			}
		}
		return result;
	}

	public void runLater(final Callback callback) {
		new Thread() {
			public void run() {
				try {
					ProcessRunner.this.runAndWait();
					callback.done();
				} catch (Exception e) {
					logger.error("Exception occurred on asynchronuous task", e);
					callback.exception(e);
				} finally {
					callback.clean();
				}
			}
		}.start();
	}

	public Process getProcess() {
		return process;
	}

	public OutputHandler getHandler() {
		return handler;
	}

	public void setHandler(OutputHandler handler) {
		this.handler = handler;
	}

	public File getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(File currentDir) {
		this.currentDir = currentDir;
	}

	public static interface Callback {
		public void done();

		public void exception(Exception e);

		public void clean();
	}
}
