package com.adobe.summit.commerce.custom.impl;

import com.adobe.summit.commerce.custom.rest.sdk.impl.AddressServiceImpl;
import com.adobe.summit.commerce.custom.rest.sdk.impl.CartServiceImpl;
import com.adobe.summit.commerce.custom.rest.sdk.impl.GeographyServiceImpl;
import com.adobe.summit.commerce.custom.rest.sdk.impl.OrderConfigurationServiceImpl;
import com.adobe.summit.commerce.custom.rest.sdk.impl.OrderServiceImpl;
import com.adobe.summit.commerce.custom.rest.sdk.impl.ProductServiceImpl;
import com.adobe.summit.commerce.custom.rest.sdk.impl.PromotionServiceImpl;
import com.adobe.summit.commerce.custom.rest.sdk.impl.PurchaseServiceImpl;
import com.elasticpath.aem.commerce.ElasticPathCommerceService;
import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.sdk.CortexSdkServiceFactory;
import com.elasticpath.rest.sdk.service.AddressService;
import com.elasticpath.rest.sdk.service.CartService;
import com.elasticpath.rest.sdk.service.GeographyService;
import com.elasticpath.rest.sdk.service.LogoutService;
import com.elasticpath.rest.sdk.service.OrderConfigurationService;
import com.elasticpath.rest.sdk.service.OrderService;
import com.elasticpath.rest.sdk.service.ProductService;
import com.elasticpath.rest.sdk.service.PromotionService;
import com.elasticpath.rest.sdk.service.PurchaseService;
import com.elasticpath.rest.sdk.service.impl.LogoutServiceImpl;

/**
 * A factory for creating objects to perform different Cortex commerce calls using the Cortex-JaxRs-Client.
 */
public class SummitCortexSdkServiceFactory implements CortexSdkServiceFactory {

	private final CartService cartService;
	private final OrderService orderService;
	private final OrderConfigurationService orderConfigurationService;
	private final PromotionService promotionService;
	private final ProductService productService;
	private final GeographyService geographyService;
	private final LogoutService logoutService;
	private final PurchaseService purchaseService;
	private final AddressService addressService;

	/**
	 * Constructor.
	 *
	 * @param cortexClient the pre-configured Cortex Client with current shopper credentials.
	 * @param commerceService The commerce service.
	 */
	public SummitCortexSdkServiceFactory(final CortexClient cortexClient,
			final ElasticPathCommerceService commerceService) {
		String scope = commerceService.getCortexScope();
		this.cartService = new CartServiceImpl(cortexClient, commerceService);
		OrderServiceImpl orderServiceImpl = new OrderServiceImpl(cortexClient, cartService);
		this.orderService = orderServiceImpl;
		this.promotionService = new PromotionServiceImpl(cortexClient, orderServiceImpl);
		this.orderConfigurationService = new OrderConfigurationServiceImpl(cortexClient);
		this.productService = new ProductServiceImpl(cortexClient);
		this.geographyService = new GeographyServiceImpl(cortexClient);
		this.logoutService = new LogoutServiceImpl(scope, cortexClient);
		this.purchaseService = new PurchaseServiceImpl(cortexClient);
		this.addressService = new AddressServiceImpl(cortexClient);
	}

	/**
	 * Gets the CortexCartService.
	 * @return the cart service.
	 */
	@Override
	public CartService getCartService() {
		return cartService;
	}

	/**
	 * Get the Cortex Order Processor service.
	 * @return the order processor.
	 */
	@Override
	public OrderService getOrderService() {
		return orderService;
	}

	/**
	 * Get the Cortex Order Address Management service.
	 * @return the address management service.
	 */
	@Override
	public OrderConfigurationService getOrderConfigurationService() {
		return orderConfigurationService;
	}

	/**
	 * Gets the Cortex promotion service.
	 * @return The promotion service.
	 */
	@Override
	public PromotionService getPromotionService() {
		return promotionService;
	}

	/**
	 * Gets the Cortex product service.
	 * @return The product service.
	 */
	@Override
	public ProductService getProductService() {
		return productService;
	}

	/**
	 * Gets the Cortex geography service.
	 * @return the Cortex geography service.
	 */
	@Override
	public GeographyService getGeographyService() {
		return geographyService;
	}

	/**
	 * Gets the Cortex logout Service.
	 * @return the Cortex logout service.
	 */
	@Override
	public LogoutService getLogoutService() {
		return logoutService;
	}

	/**
	 * Gets the Cortex purchase Service.
	 * @return the Cortex purchase service.
	 */
	@Override
	public PurchaseService getPurchaseService() {
		return purchaseService;
	}

	@Override
	public AddressService getAddressService() {
		return addressService;
	}
}
