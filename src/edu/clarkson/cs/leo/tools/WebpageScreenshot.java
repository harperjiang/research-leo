package edu.clarkson.cs.leo.tools;

import edu.clarkson.cs.leo.common.phantomjs.PJExecutor;

public class WebpageScreenshot {

	public static void main(String[] args) throws Exception {
		PJExecutor executor = new PJExecutor();

		executor.execute("screenshot",
				"http://www.pechbikeclub.com/images/stories/files/secure/index.html",
				"workdir/22/screenshot.png");
	}

}
