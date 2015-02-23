/**
 * 
 */
package com.adobe.summit.commerce.custom;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.commerce.common.AbstractJcrProduct;

/**
 * Custom product
 * 
 * @author vvenkata
 *
 */
public class SummitProduct extends AbstractJcrProduct {
	public static final String PN_IDENTIFIER = "identifier"; //"sku"; //"identifier";
    
	public SummitProduct(Resource resource) {
		super(resource);
	}

	@Override
	public String getSKU() {
	     return getProperty(PN_IDENTIFIER, String.class);
	}	
	
	// Step 4
}
