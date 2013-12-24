package edu.clarkson.cs.wpcomp.common.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author Harper Jiang
 * 
 */
public abstract class AbstractSorter implements Sorter {

	@SuppressWarnings("unchecked")
	protected Comparator comparator;

	@SuppressWarnings("unchecked")
	protected int compare(Object a, Object b) {
		if (comparator == null)
			return ((Comparable<Object>) a).compareTo(b);
		return comparator.compare(a, b);
	}

	public synchronized <T extends Comparable<T>> List<T> sort(List<T> source) {
		return sort(source, new TrivalComparator<T>());
	}

	protected boolean inplace = false;

	protected void setInplace(boolean inplace) {
		this.inplace = inplace;
	}

	public boolean isInplace() {
		return inplace;
	}

	@SuppressWarnings("unchecked")
	List array;

	protected <T> void wrap(List<T> input) {
		if (inplace) {
			array = input;
			return;
		}
		ArrayList<T> container = new ArrayList<T>();
		container.addAll(input);
		array = container;
	}
}
