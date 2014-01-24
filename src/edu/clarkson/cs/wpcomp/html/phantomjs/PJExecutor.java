package edu.clarkson.cs.wpcomp.html.phantomjs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;

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

	public void execute(String command, String... params) {
		// Template File
		File cmdFile = new File(MessageFormat.format("res/phantomjs/{0}.js",
				command));
		String[] newparam = new String[(params == null || params.length == 0) ? 2
				: params.length + 2];
		newparam[0] = PJEnv.MAIN;
		newparam[1] = cmdFile.getAbsolutePath();
		if (params != null && params.length != 0) {
			System.arraycopy(params, 0, newparam, 2, params.length);
		}
		ProcessRunner runner = new ProcessRunner(newparam);
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

	protected File replace(File templateFile, Map<String, String> params) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(templateFile)));
			File outputFile = new File(templateFile.getAbsolutePath() + ".js");

			StringBuilder buffer = new StringBuilder();

			String line = null;
			while (null != (line = br.readLine())) {
				buffer.append(line).append("\n");
			}
			br.close();

			String output = buffer.toString();
			for (Entry<String, String> entry : params.entrySet()) {
				output = output.replaceAll("\\{" + entry.getKey() + "\\}",
						entry.getValue());
			}

			PrintWriter pw = new PrintWriter(new FileOutputStream(outputFile));
			pw.print(output);
			pw.close();
			return outputFile;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
