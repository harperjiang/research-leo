package edu.clarkson.cs.leo.perf.svm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.leo.img.desc.Feature;

public class SVMDataGen {

	public static void main(String[] args) throws Exception {
		int sizes[] = { 500, 700, 1000, 1200, 1500, 1800, 2000, 2200, 2500,
				3000 };
		for (int time = 0; time < sizes.length; time++) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("res/svm/img/train")));

			int size = sizes[time];
			String line = null;
			PrintWriter pw = new PrintWriter(new FileOutputStream(
					MessageFormat.format("res/svm/perf/perf_{0}",
							String.valueOf(size))));
			while ((line = br.readLine()) != null) {
				List<Double> data = new ArrayList<Double>();
				String[] val = line.split("\\s");

				for (int i = 1; i < val.length; i++) {
					String[] pair = val[i].split(":");
					data.add(Double.valueOf(pair[1]));
				}
				List<Double> res = new ArrayList<Double>();
				while (res.size() < size) {
					res.addAll(data);
				}
				while (res.size() > size) {
					res.remove(0);
				}
				pw.println(MessageFormat.format("{0} {1}", val[0], new Feature(
						res)));
			}
			pw.close();
			br.close();
		}
	}
}
