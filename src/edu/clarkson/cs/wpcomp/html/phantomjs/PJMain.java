package edu.clarkson.cs.wpcomp.html.phantomjs;

import java.io.File;

public class PJMain {

	public static void main(String[] args) {
		PJExecutor exec = new PJExecutor();
		exec.setCurrentDir(new File("workdir"));
		exec.execute(
				"screenshot",
				"http://www.ppinvestment.com/perl/cibc/Logon.php?VER=PreSignOn&_pageLabel=signonForm:208",
				"test.png");
	}
}
