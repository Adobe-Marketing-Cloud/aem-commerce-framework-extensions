package com.adobe.summit.commerce.custom.rest.sdk.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.adobe.cq.commerce.api.CommerceConstants;
import com.adobe.cq.commerce.api.PriceInfo;
import com.adobe.cq.commerce.api.ShippingMethod;
import com.adobe.cq.commerce.api.promotion.PromotionInfo;
import com.adobe.summit.commerce.custom.rest.sdk.internal.OrderViewService;
import com.adobe.summit.commerce.custom.views.AppliedShippingPromotions;
import com.adobe.summit.commerce.custom.views.AvailableShippingMethods;
import com.adobe.summit.commerce.custom.views.Order;
import com.adobe.summit.commerce.custom.views.PreSubmitOrderDetails;
import com.adobe.summit.commerce.custom.views.Purchase;
import com.elasticpath.aem.commerce.util.ElasticPathCommerceUtil;
import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.sdk.Cart;
import com.elasticpath.rest.sdk.components.Address;
import com.elasticpath.rest.sdk.components.Name;
import com.elasticpath.rest.sdk.service.CartService;
import com.elasticpath.rest.sdk.service.OrderService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

/**
 * A collaborator for the CommerceSession responsible for actually placing an order.
 */
public final class OrderServiceImpl implements OrderService, OrderViewService {
	private static final String SEPARATOR = ".";
	/** The Constant DISPLAY_CC_NUM_VALUE_COUNT. */
	private static final int DISPLAY_CC_NUM_VALUE_COUNT = 4;
	/** The payment token display field. */
	static final String TOKEN_DISPLAY_FIELD = "display-name";
	/** The payment token display field. */
	static final String TOKEN_VALUE_FIELD = "token";
	/** The Constant PRIMARY_ACCOUNT_NUMBER. */
	private static final String PRIMARY_ACCOUNT_NUMBER = "primary-account-number";

	private final CortexClient cortexClient;

	private final CartService cartService;

	/** The cached state of the order. */
	private Order order;

	/** Cached shipping promos. */
	private AppliedShippingPromotions appliedShippingPromotions;

	/** The available shipping methods. */
	private AvailableShippingMethods availableShippingMethods;

	/**
	 * Creates an instance of this class currently this is request scoped service.
	 *
	 * @param cortexClient the cortex client.
	 * @param cartService The cart service.
	 */
	public OrderServiceImpl(final CortexClient cortexClient, final CartService cartService) {
		this.cortexClient = cortexClient;
		this.cartService = cartService;
	}

	@Override
	public String placeCortexOrder(final Map<String, Object> paymentData) {
		PreSubmitOrderDetails order = cortexClient.get(PreSubmitOrderDetails.class).getCortexView();

		String paymentTokenSubmitPath = order.getCreatePaymentTokenActionUri();
		addPaymentDetailsToOrder(paymentData, paymentTokenSubmitPath);

		String orderUri = order.getOrderUri();
		String purchaseSubmitPath = "/purchases" + orderUri;
		Purchase purchase = cortexClient.post(purchaseSubmitPath, null, Purchase.class).getCortexView();
		return purchase.getPurchaseNumber();
	}

	@Override
	public Map<String, String> getCortexOrderProperties(final Locale userLocale) {
		getOrder(userLocale);
		Map<String, String> orderDetails = new HashMap<String, String>();

		populateAddressData(CommerceConstants.BILLING_ADDRESS_PREDICATE, order.getBillingAddress(), order.getBillingAddressName(), orderDetails);
		populateAddressData(CommerceConstants.SHIPPING_ADDRESS_PREDICATE, order.getShippingAddress(), order.getShippingAddressName(), orderDetails);

		if (order.getShippingOption() != null) {
			orderDetails.put(CommerceConstants.SHIPPING_OPTION, "chosen");
		}

		return orderDetails;
	}

	@Override
	public List<PriceInfo> getOrderPrices(final Locale userLocale) {
		List<PriceInfo> prices = Lists.newArrayList();
		prices.add(ElasticPathCommerceUtil.addPriceTypes(getOrder(userLocale).getOrderTotal(userLocale), "ORDER"));
		prices.add(ElasticPathCommerceUtil.addPriceTypes(getOrder(userLocale).getOrderTax(userLocale), "TAX"));

		if (order.getShippingOption() == null) {
			//null price info outputs an empty string when getFormattedString is called
			prices.add(ElasticPathCommerceUtil.addPriceTypes(new PriceInfo(null, userLocale), "SHIPPING"));
		} else {
			prices.add(ElasticPathCommerceUtil.addPriceTypes(getOrder(userLocale).getShippingOption().getShippingPrice(userLocale), "SHIPPING"));
		}

		return prices;
	}

	@Override
	public Order getOrder(final Locale userLocale) {
		if (order == null) {
			// The cart must be (re)loaded when the order is invalidated.
			Cart cortexCart = cartService.getCortexCart(userLocale);
			String orderUri = cortexCart.getOrderUri();
			order = cortexClient.get(orderUri, Order.class).getCortexView();
		}
		return order;
	}

	private void addPaymentDetailsToOrder(final Map<String, Object> paymentData, final String paymentTokenSubmitPath) {
		String accountNumber = paymentData.get(CommerceConstants.PAYMENT_PREFIX + PRIMARY_ACCOUNT_NUMBER).toString();
		String maskedAccountNumber = maskPaymentToken(accountNumber, DISPLAY_CC_NUM_VALUE_COUNT);
		Map<String, String> paymentTokenMap = ImmutableMap.of(
				TOKEN_DISPLAY_FIELD, maskedAccountNumber,
				TOKEN_VALUE_FIELD, accountNumber);

		cortexClient.post(paymentTokenSubmitPath, paymentTokenMap);
		// TODO: Check the response code...?
	}

	private String maskPaymentToken(final String stringToMask, final int charactersToDisplay) {
		String clearTextChars = stringToMask.substring(stringToMask.length() - charactersToDisplay);
		return StringUtils.leftPad(clearTextChars, stringToMask.length(), "x");
	}

	@Override
	public void invalidateOrder() {
		order = null;
		appliedShippingPromotions = null;
		availableShippingMethods = null;
	}

	@Override
	public Collection<PromotionInfo> getCortexOrderShippingPromotions(final Locale userLocale) {
		if (appliedShippingPromotions == null) {
			Cart cortexCart = cartService.getCortexCart(userLocale);
			String orderUri = cortexCart.getOrderUri();
			appliedShippingPromotions = cortexClient.get(orderUri, AppliedShippingPromotions.class).getCortexView();
		}

		if (getOrder(userLocale).getShippingOption() == null) {
			return Lists.newArrayList();
		} else {
			return appliedShippingPromotions.getShippingOption().getAppliedPromotions();
		}
	}

	@Override
	public List<ShippingMethod> getCortexOrderAvailableShippingMethods(final Locale userLocale) {
		if (availableShippingMethods == null) {
			Cart cortexCart = cartService.getCortexCart(userLocale);
			String orderUri = cortexCart.getOrderUri();
			availableShippingMethods = cortexClient.get(orderUri, AvailableShippingMethods.class).getCortexView();
		}

		return availableShippingMethods.adaptToShippingMethodList();
	}

	private void populateAddressData(final String prefix,
			final Address address,
			final Name addressName,
			final Map<String, String> orderDetails) {
		if (address != null && addressName != null) {
			String addressPrefix = prefix + SEPARATOR;
			orderDetails.put(addressPrefix + "country", address.getCountryName());
			orderDetails.put(addressPrefix + "street2", address.getExtendedAddress());
			orderDetails.put(addressPrefix + "city", address.getLocality());
			orderDetails.put(addressPrefix + "zip", address.getPostalCode());
			orderDetails.put(addressPrefix + "state", address.getRegion());
			orderDetails.put(addressPrefix + "street1", address.getStreetAddress());
			orderDetails.put(addressPrefix + "lastname", addressName.getFamilyName());
			orderDetails.put(addressPrefix + "firstname", addressName.getGivenName());
		}
	}
}
