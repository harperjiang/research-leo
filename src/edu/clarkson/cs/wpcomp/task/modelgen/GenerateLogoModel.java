package edu.clarkson.cs.wpcomp.task.modelgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class GenerateLogoModel {

	public static void main(String[] args) throws Exception {
		File dir = new File("res/image/logo");
		File[] logos = dir.listFiles();

		PrintWriter modelFile = new PrintWriter(new FileOutputStream(
				"workdir/model/logo_positive"));
	}

}
