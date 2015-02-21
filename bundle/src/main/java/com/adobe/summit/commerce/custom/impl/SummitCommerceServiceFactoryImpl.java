/**
 * 
 */
package com.adobe.summit.commerce.custom.impl;

import javax.ws.rs.client.Client;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

import com.adobe.granite.crypto.CryptoSupport;
import com.elasticpath.aem.commerce.AbstractElasticPathCommerceServiceFactory;
import com.elasticpath.aem.commerce.ElasticPathCommerceService;
import com.elasticpath.aem.commerce.service.UserPropertiesService;
import com.elasticpath.aem.cookies.AemCortexContextManager;
import com.elasticpath.commerce.config.DemoPasswordConfiguration;
import com.elasticpath.rest.client.CortexClientFactory;

@Component(metatype = true, label = "Summit Commerce Factory")
@Service
@Properties(value = {
		@Property(name = "service.description", value = "Factory for Summit implementation of CommerceServiceFactory"),
		@Property(name = "commerceProvider", value = "summit-commerce") 
		})
public class SummitCommerceServiceFactoryImpl extends
	AbstractElasticPathCommerceServiceFactory {

	@Reference
	private CryptoSupport cryptoSupport;

	@Reference
	private DemoPasswordConfiguration handler;

	@Reference
	private AemCortexContextManager cookieManager;

	@Reference
	private CortexClientFactory cortexClientFactory;

	@Reference
	private Client jaxRsClient;

	@Reference
	private UserPropertiesService userPropertiesService;
	
	@Override
	public ElasticPathCommerceService getCommerceService(Resource resource) {
		return new SummitCommerceServiceImpl(
				getServiceContext(),
				getCortexClientFactory(),
				getJaxRsClient(),
				resource,
				getUserPropertiesService());
	}

}
