package com.adobe.summit.commerce.custom.rest.sdk.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import com.adobe.cq.commerce.api.PriceInfo;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.common.PriceFilter;
import com.adobe.summit.commerce.custom.views.ProductPrice;
import com.elasticpath.aem.commerce.constants.ElasticPathConstants;
import com.elasticpath.aem.commerce.util.ElasticPathHelper;
import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.client.CortexResponse;
import com.elasticpath.rest.client.jaxrs.JaxRsUtil;
import com.elasticpath.rest.sdk.components.Cost;
import com.elasticpath.rest.sdk.service.ProductService;
import com.google.common.collect.ImmutableMap;

/**
 * Product Service is responsible for retrieving the price of a Product from Cortex.
 */
public final class ProductServiceImpl implements ProductService {

	private final CortexClient cortexClient;

	/**
	 * Constructor.
	 * @param cortexClient the cortex client.
	 */
	public ProductServiceImpl(final CortexClient cortexClient) {
		this.cortexClient = cortexClient;
	}

	@Override
	public List<PriceInfo> getProductPrice(final Product product, final Locale locale) {
		List<PriceInfo> prices = new ArrayList<PriceInfo>();
		if (product == null) {
			return prices;
		}
		String sku = ElasticPathHelper.getProductSku(product);
		if (sku == null) {
			return  prices;
		}

		CortexResponse<ProductPrice> responseWrapper = cortexClient.post(ImmutableMap.of("code", sku), ProductPrice.class);
		Cost price = responseWrapper.getCortexView().getPurchasePrice();

		PriceInfo purchasePriceInfo = new PriceInfo(price.getAmount(),
				locale,
				Currency.getInstance(price.getCurrency()));
		if (JaxRsUtil.isSuccessful(responseWrapper.getStatusType())) {
			purchasePriceInfo.put(PriceFilter.PN_TYPES, Collections.singleton(ElasticPathConstants.CQ_PRICE_UNIT));
			prices.add(purchasePriceInfo);
		}

		return prices;
	}
}
