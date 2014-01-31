package edu.clarkson.cs.wpcomp.svm;

import java.io.File;

public class FileDataSet implements DataSet {

	private File file;

	public FileDataSet(File output) {
		super();
		this.setFile(output);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
