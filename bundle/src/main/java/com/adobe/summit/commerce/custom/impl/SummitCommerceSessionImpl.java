package com.adobe.summit.commerce.custom.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.commerce.api.CommerceException;
import com.elasticpath.aem.commerce.AbstractElasticPathCommerceService;
import com.elasticpath.aem.commerce.impl.ElasticPathCommerceSessionImpl;
import com.elasticpath.rest.sdk.CortexSdkServiceFactory;

/**
 * CommerceSesession to be used for Custom Summit Commerce Service.
 *
 */
public class SummitCommerceSessionImpl extends ElasticPathCommerceSessionImpl {

	public SummitCommerceSessionImpl(
			AbstractElasticPathCommerceService commerceService,
			SlingHttpServletRequest request, SlingHttpServletResponse response,
			Resource resource, CortexSdkServiceFactory cortexSdkServiceFactory)
			throws CommerceException {
		super(commerceService, request, response, resource, cortexSdkServiceFactory);
	}

   
}
