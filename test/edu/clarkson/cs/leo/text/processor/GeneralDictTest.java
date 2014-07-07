package edu.clarkson.cs.leo.text.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.clarkson.cs.leo.text.common.GeneralDict;

public class GeneralDictTest {

	@Test
	public void testLoad() {
		GeneralDict dict = new GeneralDict(
				"edu/clarkson/cs/wpcomp/text/processor/irrverb", 3,
				new int[][] { new int[] { 1, 0 }, new int[] { 2, 0 } });
		assertEquals("draw", dict.query("drawn", 1));
		assertEquals("be", dict.query("was", 0));
		assertEquals("be", dict.query("were", 0));
	}

	@Test
	public void testQuery() {
		fail("Not yet implemented");
	}

}
