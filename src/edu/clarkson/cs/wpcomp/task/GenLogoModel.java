/*
 * Generate models based on the logo files in res/image/logo. This model will be used for recognition of phishing website.
 */

package edu.clarkson.cs.wpcomp.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.wpcomp.task.Input.FileInput;

public class GenLogoModel {

	public static void main(String[] args) throws Exception {
		File dir = new File("res/logo");
		File[] logos = dir.listFiles();

		List<Input> inputs = new ArrayList<Input>();

		for (File logo : logos) {
			inputs.add(new FileInput(logo, inputs.size() + 1));
		}

		GenPositiveSet
				.generate(inputs, new File("workdir/model/logo_positive"));

	}

}
