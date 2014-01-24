package edu.clarkson.cs.wpcomp.html.phantomjs;

import java.io.File;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.cs.wpcomp.common.proc.OutputHandler;
import edu.clarkson.cs.wpcomp.common.proc.ProcessRunner;

public class PJExecutor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private File currentDir;

	public File getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(File currentDir) {
		this.currentDir = currentDir;
	}

	public void execute(String command) {
		File cmdFile = new File(MessageFormat.format("res/phantomjs/{0}.js",
				command));
		ProcessRunner runner = new ProcessRunner(PJEnv.MAIN,
				cmdFile.getAbsolutePath());
		runner.setCurrentDir(currentDir);
		runner.setHandler(new OutputHandler() {
			@Override
			public void output(String input) {
				// Output the content to log file
				if (logger.isDebugEnabled()) {
					logger.debug(input);
				}
			}
		});

		try {
			runner.runAndWait();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
