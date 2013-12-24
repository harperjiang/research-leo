package edu.clarkson.cs.wpcomp.common.sort;

import java.util.Comparator;

public class TrivalComparator<T extends Comparable<T>> implements Comparator<T> {

	/**
	 * null value are considered to be not so important and are bigger than any
	 * valid object instance.
	 */
	public int compare(T o1, T o2) {
		if (o1 == o2 && o1 == null)
			return 0;
		if (o1 == null)
			return 1;
		if (o2 == null)
			return -1;
		return o1.compareTo(o2);
	}

}
