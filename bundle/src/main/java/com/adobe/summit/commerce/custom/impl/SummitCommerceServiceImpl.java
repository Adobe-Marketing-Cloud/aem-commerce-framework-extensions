/**
 * 
 */
package com.adobe.summit.commerce.custom.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.commerce.api.CommerceConstants;
import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.CommerceSession;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.api.ShippingMethod;
import com.adobe.cq.commerce.api.promotion.Voucher;
import com.adobe.cq.commerce.common.AbstractJcrCommerceService;
import com.adobe.cq.commerce.common.ServiceContext;
import com.adobe.cq.commerce.common.promotion.AbstractJcrVoucher;
import com.adobe.summit.commerce.custom.SummitCommerceService;
import com.adobe.summit.commerce.custom.SummitProduct;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;

public class SummitCommerceServiceImpl extends AbstractJcrCommerceService
		implements SummitCommerceService {

	private static final Logger log = LoggerFactory
			.getLogger(SummitCommerceServiceImpl.class);

	private Resource resource;

	public SummitCommerceServiceImpl(ServiceContext serviceContext,
			Resource resource) {
		super(serviceContext, resource);
		this.resource = resource;
	}

	/* Step 3 - Uncomment 
	
	@Override
	public CommerceSession login(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws CommerceException {
		return new SummitCommerceSessionImpl(this, request, response, resource);
	} 
	
	*/

	@Override
	public boolean isAvailable(String serviceType) {
		if (CommerceConstants.SERVICE_COMMERCE.equals(serviceType)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Product getProduct(final String path) throws CommerceException {
		/* Step 2 - Uncomment me
		
		Resource resource = resolver.getResource(path);
		if (resource != null && SummitProduct.isAProductOrVariant(resource)) {
			return new SummitProduct(resource);
		} 
		
		*/
		return null;
	}

	@Override
	public Voucher getVoucher(final String path) throws CommerceException {
		Resource resource = resolver.getResource(path);
		if (resource != null) {
			// JCR-based vouchers are cq:Pages
			Resource contentResource = resource
					.getChild(JcrConstants.JCR_CONTENT);
			if (contentResource != null
					&& contentResource
							.isResourceType(AbstractJcrVoucher.VOUCHER_RESOURCE_TYPE)) {
				return new AbstractJcrVoucher(resource);
			}
		}
		return null;
	}

	@Override
	public void catalogRolloutHook(Page blueprint, Page catalog) {

		log.info(MessageFormat.format(
				"Summit Catalog Rollout HOOK - blueprint: {0}, catalog: {1}",
				blueprint.getPath(), catalog.getPath()));

	}

	@Override
	public void sectionRolloutHook(Page blueprint, Page section) {

		log.info(MessageFormat.format(
				"Summit Section Rollout HOOK - blueprint: {0}, section: {1}",
				blueprint.getPath(), section.getPath()));

	}

	@Override
	public void productRolloutHook(Product productData, Page productPage,
			Product product) throws CommerceException {

		log.info(MessageFormat
				.format("Summit Product Rollout HOOK - productData: {0}, productPage: {1}, product: {2}",
						productData.getPath(), productPage.getPath(),
						product.getPath()));

	}

	@Override
	public List<String> getCountries() throws CommerceException {
		List<String> countries = new ArrayList<String>();

		// A true implementation would likely need to check with its payment
		// processing and/or
		// fulfillment services to determine what countries to accept. This
		// implementation
		// simply accepts them all.
		// Example:
		// countries.add("CA");
		// countries.add("US");
		countries.add("*");

		return countries;
	}

	@Override
	public List<String> getCreditCardTypes() throws CommerceException {
		List<String> ccTypes = new ArrayList<String>();

		// A true implementation would likely need to check with its payment
		// processing
		// service to determine what credit cards to accept. This implementation
		// simply
		// accepts them all.
		ccTypes.add("*");

		return ccTypes;
	}

	/**
	 * The base implementation does not support session- or order-specific
	 * shipping methods, so this is a straight pass-through to the commerce
	 * service.
	 */
	@Override
	public List<ShippingMethod> getAvailableShippingMethods()
			throws CommerceException {
		List<ShippingMethod> shippingMethods = new ArrayList<ShippingMethod>();
		return shippingMethods;
	}

	@Override
	public List<String> getOrderPredicates() throws CommerceException {
		List<String> predicates = new ArrayList<String>();
		predicates.add(CommerceConstants.OPEN_ORDERS_PREDICATE);
		return predicates;
	}

}
