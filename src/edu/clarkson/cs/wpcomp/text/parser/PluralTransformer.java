package edu.clarkson.cs.wpcomp.text.parser;

import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.wpcomp.text.processor.Dicts;
import edu.clarkson.cs.wpcomp.text.processor.WordDict;

public class PluralTransformer implements Transformer {

	@Override
	public String transform(String input) {
		if (input.endsWith("ies")) {
			String newword = input.substring(0, input.length() - 3) + "y";
			if (WordDict.getInstance().isWord(newword))
				return newword;
		}
		if (input.endsWith("es")) {
			String remain = input.substring(0, input.length() - 2);
			if (WordDict.getInstance().isWord(remain)) {
				return remain;
			}
		}
		if (input.endsWith("s")) {
			String remain = input.substring(0, input.length() - 1);
			if (WordDict.getInstance().isWord(remain)) {
				return remain;
			}
		}
		String irregular = Dicts.irrPluralDict.query(input, 0);
		if (!StringUtils.isEmpty(irregular))
			return irregular;
		return null;
	}
}
