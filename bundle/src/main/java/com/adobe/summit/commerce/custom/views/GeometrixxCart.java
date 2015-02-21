package com.adobe.summit.commerce.custom.views;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.adobe.cq.commerce.api.PriceInfo;
import com.adobe.cq.commerce.api.promotion.PromotionInfo;
import com.adobe.summit.commerce.custom.impl.views.components.LineItem;
import com.elasticpath.aem.commerce.util.ElasticPathCommerceUtil;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.Cart;
import com.elasticpath.rest.sdk.EntryPointUris;
import com.elasticpath.rest.sdk.components.CartDiscount;
import com.elasticpath.rest.sdk.components.Cost;
import com.elasticpath.rest.sdk.components.Discount;
import com.elasticpath.rest.sdk.components.Linkable;
import com.elasticpath.rest.sdk.components.Promotion;
import com.elasticpath.rest.sdk.components.Self;
import com.elasticpath.rest.sdk.components.Total;
import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.google.common.collect.Lists;

/**
 * Cart representation.
 */
@Uri(EntryPointUris.CART)
@Zoom({
		@Path("lineitems:element"),
		@Path("lineitems:element:item:price"),
		@Path("lineitems:element:item:code"),
		@Path("lineitems:element:total"),
		@Path("total"),
		@Path("appliedpromotions:element"),
		@Path("lineitems:element:appliedpromotions:element"),
		@Path("discount")
})
public class GeometrixxCart extends Linkable implements Cart {
	@JsonPath("$._lineitems[0]._element")
	private List<LineItem> lineItems;
	@JsonPath("$.total-quantity")
	private int totalQuantity;
	@JsonPath("$._total[0]")
	private Total total;
	@JsonPath("$._appliedpromotions[0]._element")
	private Iterable<Promotion> appliedPromotions;
	@JsonPath("$._discount[0]")
	private CartDiscount cartDiscount;
	@JsonPath("$.self")
	private Self theSelf;
	@JsonPath("$.links[?(@.rel=='order')].uri")
	private Iterable<String> orderUri;

	@Override
	public String getOrderUri() {
		return orderUri.iterator().next();
	}

	/**
	 * Gets cart line items.
	 *
	 * @return the cart line items
	 */
	@Override
	public List<LineItem> getLineItems() {
		return lineItems;
	}

	@Override
	public List<PromotionInfo> getLineItemPromotions() {
		final List<PromotionInfo> promotionInfos = Lists.newArrayList();
		for (final LineItem cartEntry : lineItems) {
			promotionInfos.addAll(cartEntry.getAppliedPromotions());
		}
		return promotionInfos;
	}

	/**
	 * Gets the cart id.
	 * @return the cart id.
	 */
	@Override
	public String getId() {
		// We assume that the carts URI path is always in the format "/carts/scope/id"
		UriBuilder cartUri = UriBuilder.fromUri(theSelf.getUri());
		String cartsUriPath = cartUri.build().getPath();
		StringUtils.split(cartsUriPath, "/");
		String[] split = cartsUriPath.split("/");
		// CHECKSTYLE:OFF
		return (split != null && split.length == 3) ? split[2] : "default";
		// CHECKSTYLE:ON
	}

	/**
	 * Get cart subtotal.
	 *
	 * @param locale the locale
	 * @return the cart subtotal
	 */
	@Override
	public PriceInfo getSubTotal(final Locale locale) {
		Cost subTotalCost = total.getCosts().iterator().next();
		return new PriceInfo(subTotalCost.getAmount(), locale, Currency.getInstance(subTotalCost.getCurrency()));
	}

	/**
	 * Get discounted cart total.
	 *
	 * @param locale the locale
	 * @return the discounted cart total
	 */
	@Override
	public PriceInfo getDiscountedTotal(final Locale locale) {
		Cost cost = total.getCosts().iterator().next();
		Currency currency = Currency.getInstance(cost.getCurrency());
		BigDecimal discountedTotal = cost.getAmount();

		for (Discount discount : cartDiscount.getDiscounts()) {
			discountedTotal = discountedTotal.subtract(discount.getAmount());
		}

		return new PriceInfo(discountedTotal.setScale(2, RoundingMode.CEILING), locale, currency);
	}

	/**
	 * Get applied promotions on cart.
	 *
	 * @return applied promotions on cart
	 */
	@Override
	public List<PromotionInfo> getAppliedPromotions() {
		List<PromotionInfo> promotionInfos = new ArrayList<PromotionInfo>();

		if (appliedPromotions != null) {
			for (Promotion promotion : appliedPromotions) {
				promotionInfos.add(new PromotionInfo(promotion.getName(), promotion.getDisplayName(), PromotionInfo.PromotionStatus.FIRED,
													 promotion.getDisplayDescription(), promotion.getDisplayDescription(), null));
			}
		}

		return promotionInfos;
	}

	/**
	 * Gets all the cart related PriceInfo data points.
	 * @param userLocale The user locale.
	 * @return PriceInfo list.
	 */
	@Override
	public List<PriceInfo> getAllCartPriceInfo(final Locale userLocale) {
		List<PriceInfo> prices = new ArrayList<PriceInfo>();
		prices.add(ElasticPathCommerceUtil.addPriceTypes(getSubTotal(userLocale), "PRE_TAX"));
		prices.add(ElasticPathCommerceUtil.addPriceTypes(getDiscountedTotal(userLocale), "ORDER", "SUB_TOTAL"));
		// PB-221 this is here as a work around for submitorder.jsp which is the only know usage of this filter,
		// the jsp's logic for retrieving this price is broken but we do not want to commit to supporting it
		prices.add(ElasticPathCommerceUtil.addPriceTypes(new PriceInfo(BigDecimal.ZERO, userLocale), "DISCOUNT"));
		return prices;
	}

	@Override
	public boolean equals(final Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public int getTotalQuantity() {
		return totalQuantity;
	}
}
