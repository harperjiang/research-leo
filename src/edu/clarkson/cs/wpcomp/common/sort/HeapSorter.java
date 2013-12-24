package edu.clarkson.cs.wpcomp.common.sort;

import java.util.Comparator;
import java.util.List;

/**
 * <code>HeapSort</code> use max heap to sort the data.
 * 
 * @author Harper Jiang
 * 
 */

public class HeapSorter extends AbstractSorter {

	private int heapSize = 0;

	
	public HeapSorter() {
		super();
	}
	
	public HeapSorter(boolean inplace) {
		this();
		this.setInplace(inplace);
	}
	
	private void buildMaxHeap() {
		for (int i = (array.size() - 1) / 2; i >= 0; i--)
			maxHeapify(i);
	}

	private void maxHeapify(int i) {
		int left = left(i);
		int right = right(i);
		int largest = 0;
		if (left <= heapSize && (compare(array.get(i),array.get(left)) < 0))
			largest = left;
		else
			largest = i;

		if (right <= heapSize
				&& (compare(array.get(right), array.get(largest)) > 0))
			largest = right;
		if (largest != i) {
			SorterUtil.exchange(array, i, largest);
			maxHeapify(largest);
		}
	}

	private int left(int i) {
		return 2 * i + 1;
	}

	private int right(int i) {
		return 2 * (i + 1);
	}

	@SuppressWarnings("unchecked")
	public <T>List<T> sort(List<T> source, Comparator<T> comparator) {
		this.comparator = comparator;
		wrap(source);
		heapSize = array.size()-1;
		buildMaxHeap();
		for (int i = array.size() - 1; i > 0; i--) {
			SorterUtil.exchange(array, 0, i);
			heapSize--;
			maxHeapify(0);
		}
		return array;
	}
}
