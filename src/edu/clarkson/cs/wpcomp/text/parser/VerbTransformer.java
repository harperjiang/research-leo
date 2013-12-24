package edu.clarkson.cs.wpcomp.text.parser;

import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.wpcomp.text.processor.Dicts;
import edu.clarkson.cs.wpcomp.text.processor.WordDict;

public class VerbTransformer implements Transformer {

	@Override
	public String transform(String input) {
		if (input.endsWith("ing")) {
			String remain = input.substring(0, input.length() - 3);
			if (WordDict.getInstance().isWord(remain)) {
				return remain;
			}
		}
		if (input.endsWith("ed")) {
			String remainEd = input.substring(0, input.length() - 2);
			String remainD = input.substring(0, input.length() - 1);
			if (WordDict.getInstance().isWord(remainEd)) {
				return remainEd;
			}
			if (WordDict.getInstance().isWord(remainD)) {
				return remainD;
			}
		}
		String past = Dicts.irrVerbDict.query(input, 0);
		if (!StringUtils.isEmpty(past))
			return past;
		String pastParticiple = Dicts.irrVerbDict.query(input, 1);
		if (!StringUtils.isEmpty(pastParticiple))
			return pastParticiple;
		return null;
	}

}
