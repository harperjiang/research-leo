package edu.clarkson.cs.wpcomp.img.desc;

import java.util.ArrayList;
import java.util.List;

public class Feature {

	private List<Double> data;

	public Feature() {
		this.data = new ArrayList<Double>();
	}

	public Feature(double[] array) {
		this();
		for (int i = 0; i < array.length; i++)
			this.data.add(array[i]);
	}

	public Feature(List<Double> d) {
		this();
		this.data.addAll(d);
	}

	// L1-norm
	public void normalize() {
		double sum = 0;
		for (int i = 0; i < data.size(); i++)
			sum += data.get(i);
		for (int i = 0; i < data.size(); i++)
			data.set(i, Math.sqrt(data.get(i) / sum));
	}

	// L2-norm
	public void normalizeL2() {
		double sum = 0;
		for (int i = 0; i < data.size(); i++) {
			sum += Math.pow(data.get(i), 2);
		}
		sum = Math.sqrt(sum);
		for (int i = 0; i < data.size(); i++) {
			data.set(i, data.get(i) / sum);
		}
	}

	public Feature subVector(int from, int to) {
		return new Feature(data.subList(from, to));
	}

	public void join(Feature another) {
		this.data.addAll(another.data);
	}

	public int size() {
		return data.size();
	}
}
