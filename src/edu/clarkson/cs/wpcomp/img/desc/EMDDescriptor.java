package edu.clarkson.cs.wpcomp.img.desc;

import com.telmomenezes.jfastemd.Signature;

import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;

public interface EMDDescriptor {

	public Signature describe(ColorAccessor accessor);
}
