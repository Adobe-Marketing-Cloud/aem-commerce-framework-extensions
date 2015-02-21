package com.adobe.summit.commerce.custom.rest.sdk.impl;

import java.util.Locale;

import javax.ws.rs.core.Response;

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.Product;
import com.adobe.summit.commerce.custom.impl.views.components.LineItem;
import com.adobe.summit.commerce.custom.views.AddToCartForm;
import com.adobe.summit.commerce.custom.views.GeometrixxCart;
import com.elasticpath.aem.commerce.ElasticPathCommerceService;
import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.client.jaxrs.JaxRsUtil;
import com.elasticpath.rest.sdk.Cart;
import com.elasticpath.rest.sdk.service.CartService;
import com.google.common.collect.ImmutableMap;

/**
 * A collaborator for the CommerceSession responsible for actually placing an order.
 */
public final class CartServiceImpl implements CartService {

	private final CortexClient cortexClient;
	private final ElasticPathCommerceService commerceService;

	/**
	 * The cached state of the cart.
	 */
	private GeometrixxCart cart;


	/**
	 * Constructor.
	 * @param cortexClient the cortex client.
	 * @param commerceService The commerce service.
	 */
	public CartServiceImpl(final CortexClient cortexClient, final ElasticPathCommerceService commerceService) {
		this.cortexClient = cortexClient;
		this.commerceService = commerceService;
	}

	@Override
	public Cart getCortexCart(final Locale userLocale) {
		lazyLoadCart(userLocale);
		return cart;
	}

	/**
	 * Refresh the shopping cart cache.
	 */
	private void lazyLoadCart(final Locale userLocale) {
		if (cart == null) {
			cart = cortexClient.get(GeometrixxCart.class).getCortexView();
			int entryIndex = 0;
			for (LineItem lineItem : cart.getLineItems()) {
				lineItem.setCommerceService(commerceService);
				lineItem.setLocale(userLocale);
				lineItem.setEntryIndex(entryIndex);
				entryIndex++;
			}
		}
	}

	@Override
	public void invalidateCart() {
		cart = null;
	}

	@Override
	public void addCartEntry(final Product product, final int quantity) throws CommerceException {
		String productSku = product.getSKU();

		AddToCartForm addToCartForm = cortexClient.post(ImmutableMap.of("code", productSku), AddToCartForm.class).getCortexView();

		Response.StatusType status = cortexClient.post(addToCartForm.getAddToCartActionUri(), ImmutableMap.of("quantity", quantity));

		if (JaxRsUtil.isNotSuccessful(status)) {
			throw new CommerceException("Failed to add cart entry: " + JaxRsUtil.getStatusString(status));
		}

		invalidateCart();
	}

	@Override
	public void modifyCartEntry(final int entryNumber, final int quantity, final Locale userLocale) throws CommerceException {
		lazyLoadCart(userLocale);
		LineItem lineItem = cart.getLineItems().get(entryNumber);
		cortexClient.put(lineItem.getSelf().getUri(), ImmutableMap.of("quantity", quantity));
		//TODO inspect response and throw exception
		invalidateCart();
	}

	@Override
	public void deleteCartEntry(final int entryNumber, final Locale userLocale) throws CommerceException {
		lazyLoadCart(userLocale);
		LineItem lineItem = cart.getLineItems().get(entryNumber);
		cortexClient.delete(lineItem.getSelf().getUri());
		//TODO inspect response and throw exception
		invalidateCart();
	}
}
