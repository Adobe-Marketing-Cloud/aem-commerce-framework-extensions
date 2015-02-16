/**
 * 
 */
package com.adobe.summit.commerce.custom;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.commerce.api.CommerceService;
import com.adobe.cq.commerce.api.CommerceServiceFactory;

public interface SummitCommerceServiceFactory extends CommerceServiceFactory {

	/**
	 * Returns a new <code>ElasticPathCommerceService</code>.
	 * 
	 * @param resource Resource
	 * @return ElasticPathCommerceService- Commerce service specific to Elastic path.
	 */
	CommerceService getCommerceService(Resource resource);

}
