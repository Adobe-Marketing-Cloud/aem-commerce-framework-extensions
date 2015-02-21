/**
 * 
 */
package com.adobe.summit.commerce.custom.impl;

import javax.ws.rs.client.Client;

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.Product;
import com.elasticpath.aem.commerce.AbstractElasticPathCommerceService;
import com.elasticpath.aem.commerce.cortex.CortexServiceContext;
import com.elasticpath.aem.commerce.impl.ElasticPathProductImpl;
import com.elasticpath.aem.commerce.service.UserPropertiesService;
import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.client.CortexClientFactory;
import com.elasticpath.rest.sdk.CortexSdkServiceFactory;

public class SummitCommerceServiceImpl extends AbstractElasticPathCommerceService {

	private static final Logger log = LoggerFactory
			.getLogger(SummitCommerceServiceImpl.class);

	public SummitCommerceServiceImpl(CortexServiceContext cortexConfig,
			CortexClientFactory cortexClientFactory, Client jaxRsClient,
			Resource resource, UserPropertiesService userPropertiesService) {
		super(cortexConfig, cortexClientFactory, jaxRsClient, resource,
				userPropertiesService);
	}

	
	public CortexSdkServiceFactory getServiceFactory(final CortexClient cortexClient) {
		return new SummitCortexSdkServiceFactory(cortexClient, this);
	}


	@Override
	public Product getProduct(final String path) throws CommerceException {
		Resource resource = resolver.getResource(path);
		if (resource != null && SummitProduct.isAProductOrVariant(resource)) {
			return new SummitProduct(resource);
		}
		return null;
	}


}
