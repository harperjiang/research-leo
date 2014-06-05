package edu.clarkson.cs.wpcomp.svm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

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

	@Override
	public Iterator<Row> iterator() {
		return new Iterator<Row>() {

			BufferedReader reader;

			String nextLine;

			{
				try {
					reader = new BufferedReader(new InputStreamReader(
							new FileInputStream(getFile())));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public boolean hasNext() {
				if (nextLine == null) {
					try {
						nextLine = reader.readLine();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				if (StringUtils.isEmpty(nextLine)) {
					try {
						reader.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					return false;
				}
				return true;
			}

			@Override
			public Row next() {
				if (StringUtils.isEmpty(nextLine))
					return null;
				Row row = new DefaultRow(nextLine.split("\\s"));
				nextLine = null;
				return row;
			}

		};
	}
}
