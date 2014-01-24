package edu.clarkson.cs.wpcomp;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.html.phantomjs.PJExecutor;
import edu.clarkson.cs.wpcomp.img.CropHelper;
import edu.clarkson.cs.wpcomp.img.split.Split;

public class PhishMain {

	public static void main(String[] args) throws IOException {
		PJExecutor exec = new PJExecutor();
		exec.setCurrentDir(new File("workdir"));
		exec.execute("screenshot",
				"http://djyhdjhjdhjdhdjdhjhjhj.bugs3.com/Ymail.html",
				"phishing.png");

		BufferedImage input = ImageIO.read(new File("workdir/phishing.png"));
		Split split = new Split();
		List<Rectangle> ranges = split.split(input);

		int i = 0;
		for (Rectangle rect : ranges) {
			if (rect.width * rect.height >= 400) {
				String name = MessageFormat.format("range{0}.png", i++);
				BufferedImage crop = CropHelper.crop(input, rect);
				ImageIO.write(crop, "png", new File("workdir/" + name));
			}
		}
	}

}
