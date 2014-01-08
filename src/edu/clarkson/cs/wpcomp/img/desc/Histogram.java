package edu.clarkson.cs.wpcomp.img.desc;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Histogram {

	private Map<Object, Integer> values;

	private Feature buffer;

	public Histogram() {
		super();
		values = new HashMap<Object, Integer>();
	}

	public void put(Object key, Integer value) {
		values.put(key, value);
		buffer = null;
	}

	public void add(Object key, Integer value) {
		if (values.containsKey(key)) {
			values.put(key, values.get(key) + value);
		} else {
			values.put(key, value);
		}
		buffer = null;
	}

	public Integer get(Object key) {
		return values.get(key);
	}

	public Feature getData() {
		if (null != buffer)
			return buffer;
		double[] result = new double[values.size()];
		PriorityQueue<Object> order = new PriorityQueue<Object>();
		order.addAll(values.keySet());
		int counter = 0;
		while (!order.isEmpty()) {
			Object key = order.poll();
			result[counter++] = values.get(key);
		}
		buffer = new Feature(result);
		return buffer;
	}
}
