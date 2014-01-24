package edu.clarkson.cs.wpcomp.html.phantomjs;

import org.junit.Test;

public class PJExecutorTest {

	@Test
	public void testExecute() {
		PJExecutor exec = new PJExecutor();
		exec.execute("screenshot");
	}

}
