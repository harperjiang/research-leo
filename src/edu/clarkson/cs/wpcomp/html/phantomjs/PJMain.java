package edu.clarkson.cs.wpcomp.html.phantomjs;

import java.io.File;

public class PJMain {

	public static void main(String[] args) {
		PJExecutor exec = new PJExecutor();
		exec.setCurrentDir(new File("workdir"));
		exec.execute("screenshot", "http://www.alsoelectronics.com/image/templates/Google/Google/index.htm", "phishing-ebay-2.png");
	}
}
