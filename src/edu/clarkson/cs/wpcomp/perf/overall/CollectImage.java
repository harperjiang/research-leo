package edu.clarkson.cs.wpcomp.perf.overall;

import java.text.DecimalFormat;

import edu.clarkson.cs.wpcomp.html.phantomjs.PJExecutor;

public class CollectImage {

	public static void main(String[] args) throws Exception {
		PJExecutor executor = new PJExecutor();

		executor.execute("screenshot",
				"http://www.pechbikeclub.com/images/stories/files/secure/index.html",
				"workdir/22/screenshot.png");
	}

}
