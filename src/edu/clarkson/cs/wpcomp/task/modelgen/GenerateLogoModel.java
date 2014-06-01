package edu.clarkson.cs.wpcomp.task.modelgen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.wpcomp.task.imgdesc.PositiveSetGenerator;
import edu.clarkson.cs.wpcomp.task.imgdesc.PositiveSetGenerator.Input;

public class GenerateLogoModel {

	public static void main(String[] args) throws Exception {
		File dir = new File("res/image/logo");
		File[] logos = dir.listFiles();
		
		List<Input> inputs = new ArrayList<Input>();
		
		for(File logo: logos) {
			inputs.add(new Input(logo, inputs.size()+1));
		}
		
		PositiveSetGenerator.generate(inputs, new File("workdir/model/logo_positive"));
		
	}

}
