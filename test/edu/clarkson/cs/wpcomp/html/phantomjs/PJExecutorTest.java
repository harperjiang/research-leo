package edu.clarkson.cs.wpcomp.html.phantomjs;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class PJExecutorTest {

	@Test
	public void testExecute() {
		PJExecutor exec = new PJExecutor();
		exec.setCurrentDir(new File("res/phantomjs"));

		Map<String, String> params = new HashMap<String, String>();
		params.put("url", "http://github.com");
		params.put("file", "github.png");

		exec.execute("screenshot", params);

		File output = new File("res/phantomjs/github.png");
		assertTrue(output.exists() && output.isFile());
	}

}
