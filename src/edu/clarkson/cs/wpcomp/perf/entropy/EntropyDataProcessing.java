package edu.clarkson.cs.wpcomp.perf.entropy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class EntropyDataProcessing {

	public static void main(String[] args) throws Exception {
		BufferedReader br1 = new BufferedReader(new InputStreamReader(
				new FileInputStream("res/svm/perf/entropy/entropy_image")));
		BufferedReader br2 = new BufferedReader(new InputStreamReader(
				new FileInputStream("res/svm/perf/entropy/entropy_text")));
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"res/svm/perf/entropy/entropy_dist"));

		double step = 0.05;
		double[] bucketsA = new double[200];
		double[] bucketsB = new double[200];
		for (int i = 0; i < bucketsA.length; i++) {
			bucketsA[i] = 0;
			bucketsB[i] = 0;
		}
		String line = null;
		while ((line = br1.readLine()) != null) {
			Double value = Double.valueOf(line.trim());
			int bucket = (int) Math.floor(value / step);
			bucketsA[bucket]++;
		}
		while ((line = br2.readLine()) != null) {
			Double value = Double.valueOf(line.trim());
			int bucket = (int) Math.floor(value / step);
			bucketsB[bucket]++;
		}

		normalize(bucketsA);
		normalize(bucketsB);

		for (int i = 0; i < bucketsA.length; i++) {
			pw.println(MessageFormat.format(
					"{0}-{1} {2,number,###0.0000} {3,number,###0.0000}", step * i, step
							* (i + 1), bucketsA[i], bucketsB[i]));
		}

		br1.close();
		br2.close();
		pw.close();
	}

	public static void normalize(double[] array) {
		double sum = 0;
		for (double val : array) {
			sum += Math.pow(val, 2);
		}
		sum = Math.sqrt(sum);

		for (int i = 0; i < array.length; i++) {
			array[i] = array[i] / sum;
		}
	}
}
