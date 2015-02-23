/**
 * 
 */
package com.adobe.summit.commerce.custom;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.commerce.api.CommerceService;
import com.adobe.cq.commerce.api.CommerceServiceFactory;

/**
 * Factory interface to extend
 * 
 * @author vvenkata
 *
 */
public interface SummitCommerceServiceFactory extends CommerceServiceFactory {

	/**
	 * Returns a new <code>CommerceService</code>.
	 * 
	 * @param resource Resource
	 * @return CommerceService- Commerce service specific to implementation.
	 */
	CommerceService getCommerceService(Resource resource);

}
