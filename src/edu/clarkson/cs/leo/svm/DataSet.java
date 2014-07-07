package edu.clarkson.cs.leo.svm;

import edu.clarkson.cs.leo.svm.DataSet.Row;


public interface DataSet extends Iterable<Row> {

	public interface Row {
		public Object get(int column);
	}

}
