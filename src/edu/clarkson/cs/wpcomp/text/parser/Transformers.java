package edu.clarkson.cs.wpcomp.text.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Transformers {

	private static List<Transformer> transformers;

	static {
		transformers = new ArrayList<Transformer>();
		transformers.add(new PluralTransformer());
		transformers.add(new VerbTransformer());
		transformers.add(new PossessiveTransformer());
	}

	public static String transform(String input) {
		String output = null;
		for (Transformer trans : transformers) {
			if (!StringUtils.isEmpty(output)) {
				break;
			}
			output = trans.transform(input);
		}
		return StringUtils.isEmpty(output) ? input : output;
	}
}
