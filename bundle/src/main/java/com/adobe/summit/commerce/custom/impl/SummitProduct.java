/**
 * 
 */
package com.adobe.summit.commerce.custom.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.elasticpath.aem.commerce.impl.ElasticPathProductImpl;

/**
 * @author vvenkata
 *
 */
public class SummitProduct extends ElasticPathProductImpl {
	public static final String PN_IDENTIFIER = "identifier"; //"sku"; //"identifier";
    
	/** The Constant TITLE. */
	public static final String SUMMIT_DESCRIPTION = "SummitDescription";

	
	public SummitProduct(Resource resource) {
		super(resource);
	}

	/**
	 * Method to return the description.
	 * 
	 * @return description
	 */
	/* Step 1 - Start
	@Override
	public String getDescription() {
		String description = getProperty(SUMMIT_DESCRIPTION, String.class);
		if (StringUtils.isEmpty(description)) {
			return StringUtils.EMPTY;
		}
		return description;
	}
	Step 1 - End
	*/
}
