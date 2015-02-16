/**
 * 
 */
package com.adobe.summit.commerce.custom.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.settings.SlingSettingsService;

import com.adobe.cq.commerce.api.CommerceService;
import com.adobe.cq.commerce.api.CommerceServiceFactory;
import com.adobe.cq.commerce.common.AbstractJcrCommerceServiceFactory;
import com.adobe.cq.commerce.common.CommerceSearchProviderManager;
import com.adobe.granite.security.user.UserPropertiesService;
import com.adobe.summit.commerce.custom.SummitCommerceService;
import com.day.cq.wcm.api.LanguageManager;

@Component(metatype = true, label = "Summit Commerce Factory")
@Service
@Properties(value = {
		@Property(name = "service.description", value = "Factory for Summit implementation of CommerceServiceFactory"),
		@Property(name = "commerceProvider", value = "summit-commerce") 
		})
public class DefaultSummitCommerceServiceFactory extends
		AbstractJcrCommerceServiceFactory implements
		CommerceServiceFactory {

	@Reference
    protected LanguageManager languageManager;

    @Reference
    protected UserPropertiesService userPropertiesService;

    @Reference
    protected SlingSettingsService slingSettingsService;

    @Reference
    protected SlingRepository slingRepository;

    @Reference
    protected CommerceSearchProviderManager searchProviderManager;

	@Reference
	private SlingRepository repository;

	/**
	 * Returns a new <code>SummitCommerceService</code>.
	 * 
	 * @param resource
	 *            Resource
	 * @return SummitCommerceService- Commerce service specific to Elastic path.
	 */
	public CommerceService getCommerceService(Resource resource) {
		return new SummitCommerceServiceImpl(getServiceContext(), resource);
	}

}
