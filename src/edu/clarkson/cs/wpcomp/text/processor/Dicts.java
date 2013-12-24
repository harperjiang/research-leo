package edu.clarkson.cs.wpcomp.text.processor;

import edu.clarkson.cs.wpcomp.text.common.GeneralDict;
import edu.clarkson.cs.wpcomp.text.common.SetDict;

public class Dicts {

	public static GeneralDict irrVerbDict = new GeneralDict(
			"edu/clarkson/cs/wpcomp/text/processor/irrverb", 3, new int[][] {
					new int[] { 1, 0 }, new int[] { 2, 0 } });

	public static GeneralDict irrPluralDict = new GeneralDict(
			"edu/clarkson/cs/wpcomp/text/processor/irrplural", 2,
			new int[][] { new int[] { 1, 0 } });

	public static SetDict wordList = new SetDict(
			"edu/clarkson/cs/wpcomp/text/processor/words");

	public static SetDict verbList = new SetDict(
			"edu/clarkson/cs/wpcomp/text/processor/verb");
}
