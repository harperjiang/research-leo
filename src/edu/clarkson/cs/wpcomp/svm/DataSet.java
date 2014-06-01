package edu.clarkson.cs.wpcomp.svm;

import edu.clarkson.cs.wpcomp.svm.DataSet.Row;


public interface DataSet extends Iterable<Row> {

	public interface Row {
		public Object get(int column);
	}

}
