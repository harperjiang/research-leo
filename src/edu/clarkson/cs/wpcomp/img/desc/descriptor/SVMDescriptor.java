package edu.clarkson.cs.wpcomp.img.desc.descriptor;

import edu.clarkson.cs.wpcomp.img.desc.Vector;
import edu.clarkson.cs.wpcomp.img.desc.accessor.RGBAccessor;

/**
 * SVMDescriptor generate a vector that can be used as input of SVMs.
 * 
 * @author harper
 * @since webpage-comparison 1.0
 * @version 1.0
 * 
 * 
 */
public interface SVMDescriptor {

	public Vector describe(RGBAccessor input);
}
