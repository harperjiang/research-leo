package edu.clarkson.cs.wpcomp.img.desc.descriptor;

import com.telmomenezes.jfastemd.Signature;

import edu.clarkson.cs.wpcomp.img.desc.accessor.RGBAccessor;

public interface EMDDescriptor {

	public Signature describe(RGBAccessor accessor);
}
