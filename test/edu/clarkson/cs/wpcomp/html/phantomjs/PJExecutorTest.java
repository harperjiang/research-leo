package edu.clarkson.cs.wpcomp.html.phantomjs;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class PJExecutorTest {

	@Test
	public void testExecute() {
		PJExecutor exec = new PJExecutor();
		exec.setCurrentDir(new File("res/phantomjs"));

		exec.execute("screenshot", "http://www.github.com", "github.png");

		File output = new File("res/phantomjs/github.png");
		assertTrue(output.exists() && output.isFile());
	}

}
