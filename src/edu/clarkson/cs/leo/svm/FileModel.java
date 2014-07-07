package edu.clarkson.cs.leo.svm;

import java.io.File;

public class FileModel implements Model {

	private File file;

	public FileModel(File file) {
		super();
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public void save(File file) {

	}

	@Override
	public void load(File file) {

	}

}
