package edu.clarkson.cs.wpcomp.img.desc.descriptor;

import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;

import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class GradientEMDDescriptorTest {

	@Test
	public void testDescribe() throws Exception {
		new GradientEMDDescriptor().describe(new ImageAccessor(ImageIO
				.read(new File("res/image/ebay_1.jpg"))));
	}

}
