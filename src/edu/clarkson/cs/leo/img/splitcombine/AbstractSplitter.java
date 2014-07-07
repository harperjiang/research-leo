package edu.clarkson.cs.leo.img.splitcombine;

import edu.clarkson.cs.leo.img.accessor.ColorAccessor;

public class AbstractSplitter {

	protected SplitCore core;

	public AbstractSplitter(SplitCore core) {
		super();
		this.core = core;
	}

	public ColorAccessor getAccessor() {
		return core.getAccessor();
	}

}
