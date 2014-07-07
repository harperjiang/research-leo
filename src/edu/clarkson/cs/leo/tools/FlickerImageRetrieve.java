package edu.clarkson.cs.leo.tools;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.clarkson.cs.leo.common.phantomjs.PJExecutor;

public class FlickerImageRetrieve {

	public static void main(String[] args) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		long start = df.parse("2013/12/30").getTime();
		for (int i = 0; i < 50; i++) {
			start -= 86400000;
			PJExecutor exec = new PJExecutor();
			exec.setCurrentDir(new File("workdir"));
			exec.execute("flickrdownload", df.format(new Date(start)));
		}
	}
}
