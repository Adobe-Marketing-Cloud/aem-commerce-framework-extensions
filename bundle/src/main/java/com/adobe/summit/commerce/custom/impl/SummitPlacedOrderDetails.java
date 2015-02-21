package com.adobe.summit.commerce.custom.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.CommerceSession.CartEntry;
import com.adobe.cq.commerce.api.PlacedOrder;
import com.adobe.cq.commerce.api.PriceInfo;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.api.promotion.PromotionInfo;
import com.adobe.cq.commerce.api.promotion.VoucherInfo;
import com.adobe.summit.commerce.custom.views.PurchaseView;
import com.elasticpath.aem.commerce.ElasticPathCommerceSession;
import com.elasticpath.aem.commerce.impl.ElasticPathCartEntryImpl;
import com.elasticpath.rest.sdk.components.Address;
import com.elasticpath.rest.sdk.components.AddressEntry;
import com.elasticpath.rest.sdk.components.AppliedPromotions;
import com.elasticpath.rest.sdk.components.Coupon;
import com.elasticpath.rest.sdk.components.Name;
import com.elasticpath.rest.sdk.components.Promotion;
import com.elasticpath.rest.sdk.components.PurchaseLineItem;

/**
 * ElasticPathPlacedOrderImpl provides functions related to Cart elements and order details.
 */
public class SummitPlacedOrderDetails implements PlacedOrder {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(SummitPlacedOrderDetails.class);

	/** The CommerceSession. */
	private final ElasticPathCommerceSession commerceSession;

	/** The details map. */
	private Map<String, Object> details;

	/** The price list. */
	private List<PriceInfo> prices;

	/** The List for cart Entries. */
	private List<CartEntry> entries;

	/** The Constant PRICE_FILTER_TYPE. */
	private static final String PRICE_FILTER_TYPE = "com.adobe.cq.commerce.common.PriceFilter.types";

	/** The resource. */
	private final Resource resource;

	/** The purchaseView. */
	private final PurchaseView purchaseView;

	/**
	 * Instantiates ElasticPathPlacedOrderImpl.
	 *
	 * @param commerceSession ElasticPathCommerceSession
	 * @param purchaseView the geometrixx view of the Cortex purchase
	 */
	public SummitPlacedOrderDetails(final ElasticPathCommerceSession commerceSession, final PurchaseView purchaseView) {
		this.commerceSession = commerceSession;
		this.resource = commerceSession.getResource();
		this.purchaseView = purchaseView;
	}

	/**
	 * Gets the order details in a map.
	 *
	 * @return Map<String, Object>
	 * @throws com.adobe.cq.commerce.api.CommerceException commerceException
	 */
	@Override
	public Map<String, Object> getOrder() throws CommerceException {

		if (this.details == null) {
			this.details = new HashMap<String, Object>();
			if (purchaseView != null) {

				putPuchasedDetailsInMap(purchaseView);

			}
		}
		return this.details;
	}

	/**
	 * Put purchased details in the purchased detail map.
	 *
	 * @param purchaseView the purchase view
	 */
	private void putPuchasedDetailsInMap(final PurchaseView purchaseView) {
		final String purchaseDate = purchaseView.getDate();
		final SimpleDateFormat dtfrmt = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", commerceSession.getUserLocale());
		try {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(dtfrmt.parse(purchaseDate));
			this.details.put("orderPlaced", calendar);
			this.details.put("orderPlacedFormatted", dtfrmt.format(calendar.getTime()));
		} catch (final ParseException parseException) {
			this.details.put("orderPlaced", StringUtils.EMPTY);
			this.details.put("orderPlacedFormatted", StringUtils.EMPTY);
			// No need to throw the exception as the order may still have the remaining details populated. Just logging the error.
			LOG.error("Exception while parsing date {} ", purchaseDate, parseException);
		}
		this.details.put("orderId", purchaseView.getPurchaseNumber());
		this.details.put("orderStatus", purchaseView.getStatus());
		this.details.put("shippingStatus", purchaseView.getShippingStatus());
		this.details.put("shippingCarrier", purchaseView.getShippingOption().getCarrier());
		// Put billing address.
		putAddressInMap("billing", purchaseView.getBillingAddressEntry());
		// Put shipping address.
		putAddressInMap("shipping", purchaseView.getShippingAddressEntry());
	}

	/**
	 * Put address in the purchased detail map.
	 *
	 * @param addressType the address type
	 * @param addressEntry the address entry
	 */
	private void putAddressInMap(final String addressType, final AddressEntry addressEntry) {
		Name name = addressEntry.getName();
		this.details.put(addressType + ".firstname", name.getGivenName());
		this.details.put(addressType + ".lastname", name.getFamilyName());

		Address address = addressEntry.getAddress();
		this.details.put(addressType + ".street1", address.getStreetAddress());
		this.details.put(addressType + ".street2", address.getExtendedAddress());
		this.details.put(addressType + ".city", address.getLocality());
		this.details.put(addressType + ".country", address.getCountryName());
		this.details.put(addressType + ".state", address.getRegion());
		this.details.put(addressType + ".zip", address.getPostalCode());
	}

	/**
	 * Loads the price info for cart.
	 */
	protected void lazyLoadPriceInfo() {
		this.prices = new ArrayList<PriceInfo>();
		final String cartCurrencyCode = purchaseView.getTotal().getCurrency();
		final Currency cartCurrency = Currency.getInstance(cartCurrencyCode);

		PriceInfo priceInfo = new PriceInfo(purchaseView.getShippingOptionCostNumeric(),
				commerceSession.getUserLocale(),
				cartCurrency);
		priceInfo.put(PRICE_FILTER_TYPE, new HashSet<Object>(Arrays.asList("SHIPPING", cartCurrencyCode)));
		this.prices.add(priceInfo);

		priceInfo = new PriceInfo(purchaseView.getOrderTotalPriceNumeric(),
				commerceSession.getUserLocale(),
				cartCurrency);
		priceInfo.put(PRICE_FILTER_TYPE, new HashSet<Object>(Arrays.asList("ORDER", "TOTAL", cartCurrencyCode)));
		this.prices.add(priceInfo);

		priceInfo = new PriceInfo(purchaseView.getOrderTotalTaxNumeric(),
				commerceSession.getUserLocale(),
				cartCurrency);
		priceInfo.put(PRICE_FILTER_TYPE, new HashSet<Object>(Arrays.asList("ORDER", "TAX", cartCurrencyCode)));
		this.prices.add(priceInfo);

		priceInfo = new PriceInfo(purchaseView.getOrderLineItemTotalNumeric(),
				commerceSession.getUserLocale(),
				cartCurrency);
		priceInfo.put(PRICE_FILTER_TYPE, new HashSet<Object>(Arrays.asList("ORDER", "PRE_TAX", cartCurrencyCode)));
		this.prices.add(priceInfo);

	}

	/**
	 * Loads the line item quantity, total price and display name.
	 *
	 * @throws com.adobe.cq.commerce.api.CommerceException commerceException
	 */
	protected void lazyLoadCartEntries() throws CommerceException {
		this.entries = new ArrayList<CartEntry>();
		Iterable<PurchaseLineItem> lineItems = purchaseView.getLineItems();

		int count = 0;
		for (PurchaseLineItem lineItem : lineItems) {
			final Map<String, Object> lineItemMap = new HashMap<String, Object>();
			final BigDecimal lineItemPriceVal = lineItem.getPriceNumeric();
			final Currency currencyCode = Currency.getInstance(lineItem.getCurrencyCode());
			final Product product = commerceSession.getProductByName(resource, lineItem.getName());
			String skuCode = product.getSKU();
			lineItemMap.put("productSkuCode", skuCode);
			if (skuCode != null) {
				if (skuCode.contains("-")) {
					skuCode = skuCode.substring(0, skuCode.indexOf('-'));
				}
				lineItemMap.put("code", skuCode);
			}
			final Locale locale = commerceSession.getUserLocale();
			this.entries.add(new ElasticPathCartEntryImpl(
					commerceSession.getService().getProductBySkuCode(resource, skuCode),
					count,
					lineItem.getQuantity(),
					StringUtils.EMPTY,
					Arrays.asList(
						new PriceInfo(lineItemPriceVal, locale, currencyCode),
						new PriceInfo(BigDecimal.ZERO, locale, currencyCode)),
					lineItemMap));
			count++;
		}
	}

	/**
	 * Loads the cart entries.
	 *
	 * @return List
	 * @throws com.adobe.cq.commerce.api.CommerceException CommerceException
	 */
	@Override
	public List<CartEntry> getCartEntries() throws CommerceException {
		if (this.entries == null) {
			lazyLoadCartEntries();
		}
		return this.entries;
	}

	/**
	 * Gets the filtered price from cart.
	 *
	 * @param filter the filter Predicate
	 * @return string.
	 * @throws com.adobe.cq.commerce.api.CommerceException CommerceException
	 */
	@Override
	public String getCartPrice(final Predicate filter) throws CommerceException {
		if (this.prices == null) {
			lazyLoadPriceInfo();
		}

		final PriceInfo price = (PriceInfo) CollectionUtils.find(this.prices, filter);
		if (price != null) {
			return price.getFormattedString();
		}
		return "";
	}

	/**
	 * Gets the filtered list of price from cart.
	 *
	 * @param filter the filter Predicate
	 * @return list.
	 * @throws com.adobe.cq.commerce.api.CommerceException CommerceException
	 */
	@Override
	public List<PriceInfo> getCartPriceInfo(final Predicate filter) throws CommerceException {
		if (this.prices == null) {
			lazyLoadPriceInfo();
		}
		final List<PriceInfo> filteredPrices = new ArrayList<PriceInfo>();
		CollectionUtils.select(this.prices, filter, filteredPrices);
		return filteredPrices;
	}

	/**
	 * Gets the orderId.
	 *
	 * @return order ID.
	 * @throws com.adobe.cq.commerce.api.CommerceException CommerceException
	 */
	@Override
	public String getOrderId() throws CommerceException {
		return purchaseView.getPurchaseNumber();
	}

	/**
	 * Gets the promotionsInfo list.
	 *
	 * @return list of PromotionsInfo
	 * @throws com.adobe.cq.commerce.api.CommerceException CommerceException
	 */
	@Override
	public List<PromotionInfo> getPromotions() throws CommerceException {
		List<PromotionInfo> promotionInfos = new ArrayList<PromotionInfo>();
		AppliedPromotions appliedPromotionsList = purchaseView.getAppliedPromotions();
		if (appliedPromotionsList == null) {
			return promotionInfos;
		}
		Iterable<Promotion> appliedPromotions = purchaseView.getAppliedPromotions().getPromotions();
		for (Promotion appliedPromotion : appliedPromotions) {
			promotionInfos.add(new PromotionInfo(appliedPromotion.getName(),
					appliedPromotion.getDisplayName(),
					PromotionInfo.PromotionStatus.FIRED,
					appliedPromotion.getDisplayDescription(),
					appliedPromotion.getDisplayDescription(),
					null));
		}
		return promotionInfos;
	}

	/**
	 * Gets the VoucherInfo list.
	 *
	 * @return list of VoucherInfo
	 * @throws com.adobe.cq.commerce.api.CommerceException CommerceException
	 */
	@Override
	public List<VoucherInfo> getVoucherInfos() throws CommerceException {
		List<VoucherInfo> voucherInfoList = new ArrayList<VoucherInfo>();
		Iterable<Coupon> coupons = purchaseView.getCoupons();
		for (Coupon coupon : coupons) {
			Promotion couponTriggeredPromo = coupon.getCouponTriggeredPromotions().iterator().next().getPromotions().iterator().next();
			voucherInfoList.add(
					new VoucherInfo(
							coupon.getCode(),
							coupon.getSelf().getUri(),
							null,
							couponTriggeredPromo.getDisplayDescription(),
							isValidCoupon(coupon),
							getVoucherDetailFromPromotions(coupon)));
		}
		return voucherInfoList;
	}


	private String getVoucherDetailFromPromotions(final Coupon coupon) {
		String promotions = StringUtils.EMPTY;
		if (coupon.getCouponTriggeredPromotions().iterator().hasNext()) {
			AppliedPromotions appliedPromotions = coupon.getCouponTriggeredPromotions().iterator().next();
			Iterable<Promotion> promos = appliedPromotions.getPromotions();
			for (Promotion promo : promos) {
				if (!promotions.isEmpty()) {
					promotions = promotions.concat(", ");
				}
				promotions = promotions.concat(promo.getDisplayName());
			}
		}
		return promotions;
	}

	private boolean isValidCoupon(final Coupon coupon) {
		if (coupon.getCouponTriggeredPromotions().iterator().hasNext()) {
			AppliedPromotions appliedPromotions = coupon.getCouponTriggeredPromotions().iterator().next();
			if (appliedPromotions.getPromotions().iterator().hasNext()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets order details.
	 *
	 * @return order details
	 */
	public Map<String, Object> getDetails() {
		return details;
	}

}