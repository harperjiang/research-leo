package edu.clarkson.cs.wpcomp;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.MarkHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.splitcombine.Combine;
import edu.clarkson.cs.wpcomp.img.splitcombine.Split;
import edu.clarkson.cs.wpcomp.img.splitcombine.filter.Filters;
import edu.clarkson.cs.wpcomp.img.splitcombine.filter.SizeFilter;

public class PhishingMark {

	public static void main(String[] args) throws Exception {
		File workdir = new File("workdir/24");
		BufferedImage input = ImageIO.read(new File(workdir.getAbsolutePath()
				+ File.separator + "screenshot.png"));

		Split split = new Split();
		Combine combine = new Combine();

		long start = System.currentTimeMillis();
		List<Rectangle> ranges = split.split(input);
		ranges = combine.combine(ranges);
		ranges = Filters.filter(split.getCenv(), new SizeFilter(), ranges);
		System.out.println(System.currentTimeMillis() - start);

		ImageAccessor accessor = new ImageAccessor(input);
		for (Rectangle com : ranges) {
			MarkHelper.redrect(com, accessor);
		}

		ImageIO.write(input, "png", new File(workdir.getAbsolutePath()
				+ File.separator + "split.png"));
	}

}
