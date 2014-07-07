package edu.clarkson.cs.leo.common.sort;

import java.util.Comparator;
import java.util.List;

/**
 * Provide sorting utils based on arbitrary <code>java.util.Comparators</code>
 * 
 * @author Harper Jiang
 * @version 1.1 - Support to Generic type.
 * @since Commmon 1.0
 * @see java.lang.Comparable
 */
public interface Sorter {
	/**
	 * Sort the give list in ascending order using objects' compareTo Method.
	 * 
	 * @param source
	 * @return a sorted copy of the original list.
	 */
	public <X extends Comparable<X>> List<X> sort(List<X> source);

	/**
	 * Sort the list using the given comparator.
	 * 
	 * @param <T>
	 * @param source
	 * @param comparator
	 * @return
	 */
	public <T> List<T> sort(List<T> source, Comparator<T> comparator);

}
