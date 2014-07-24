package edu.clarkson.cs.leo.stat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class TextDistribution {

	public static void main(String[] args) throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"workdir/textdetect/text_histo"));

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("workdir/textdetect/text_positive_short")));
		//
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] datas = line.split(" ");
			for (int i = 0; i < 50; i++) {
				pw.println(MessageFormat.format("{0} {1}", i,
						Double.parseDouble(datas[i + 1].split(":")[1])));
			}
		}
		br.close();
		br = new BufferedReader(new InputStreamReader(new FileInputStream(
				"workdir/textdetect/text_positive")));

		line = null;
		while ((line = br.readLine()) != null) {
			String[] datas = line.split(" ");
			for (int i = 0; i < 50; i++) {
				pw.println(MessageFormat.format("{0} {1}", i,
						Double.parseDouble(datas[i + 1].split(":")[1])));
			}
			pw.println();
		}
		br.close();

		pw.close();
	}

}
