package com.adobe.summit.commerce.custom.impl.views.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.CommerceSession;
import com.adobe.cq.commerce.api.PriceInfo;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.api.promotion.PromotionInfo;
import com.elasticpath.aem.commerce.ElasticPathCommerceService;
import com.elasticpath.rest.sdk.components.AppliedPromotions;
import com.elasticpath.rest.sdk.components.Cost;
import com.elasticpath.rest.sdk.components.Item;
import com.elasticpath.rest.sdk.components.Linkable;
import com.elasticpath.rest.sdk.components.Promotion;
import com.elasticpath.rest.sdk.components.Total;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

/**
 * Line item representation.
 */
public class LineItem extends Linkable implements CommerceSession.CartEntry   {

	private int entryIndex;
	private int quantity;
	private ElasticPathCommerceService commerceService;
	private Locale locale;

	@JsonProperty("_item")
	private Iterable<Item> items;
	@JsonProperty("_total")
	private Collection<Total> totals;
	@JsonProperty("_appliedpromotions")
	private Collection<AppliedPromotions> lineItemPromotions;

	public int getEntryIndex() {
		return entryIndex;
	}
	
	public void setEntryIndex(final int entryIndex) {
		this.entryIndex = entryIndex;
	}

	/**
	 * Get {@link com.adobe.cq.commerce.api.Product}.
	 *
	 * @return the {@link com.adobe.cq.commerce.api.Product}
	 * @throws CommerceException if product can not be retrieved
	 */
	public Product getProduct() throws CommerceException {
		Item item = this.items.iterator()
							  .next();
		String productSkuCode = item.getMappings()
							 .iterator().next()
							 .getSkuCode();
		return commerceService.getProductBySkuCode(commerceService.getResource(), productSkuCode);
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(final int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets total line item price information.
	 *
	 * @param predicate {@link org.apache.commons.collections.Predicate} used to filter pricing information
	 * @return pricing information for cart line item
	 * @throws CommerceException if price information can not be retrieved
	 */
	public List<PriceInfo> getPriceInfo(final Predicate predicate) throws CommerceException {
		Cost price = getTotalPriceAmount();
		final PriceInfo totalPrice = new PriceInfo(price.getAmount(),
				locale,
				Currency.getInstance(price.getCurrency()));
		return Lists.newArrayList(totalPrice);
	}

	/**
	 * Sets the zoomed items.
	 * @param items the zoomed items
	 */
	public void setZoomedItems(final Collection<Item> items) {
		this.items = items;
	}
	
	/**
	 * Sets the zoomed totals.
	 * @param totals the zoomed totals
	 */
	public void setZoomedTotal(final Collection<Total> totals) {
		this.totals = totals;
	}

	/**
	 * Sets the applied line item promotions.
	 *
	 * @param lineItemPromotions  the applied line item promotions
	 */
	public void setAppliedPromotions(final Collection<AppliedPromotions> lineItemPromotions) {
		this.lineItemPromotions = lineItemPromotions;
	}


	/**
	 * Gets the applied line item promotions.
	 *
	 * @return the applied line item promotions
	 */
	public Collection<PromotionInfo> getAppliedPromotions() {
		Collection<PromotionInfo> promotionInfos = new ArrayList<PromotionInfo>();

		if (lineItemPromotions != null && !lineItemPromotions.isEmpty()) {
			for (Promotion promotion : lineItemPromotions.iterator().next().getPromotions()) {
				promotionInfos.add(new PromotionInfo(promotion.getName(), promotion.getDisplayName(), PromotionInfo.PromotionStatus.FIRED,
													 promotion.getDisplayDescription(), promotion.getDisplayDescription(), entryIndex));
			}
		}

		return promotionInfos;
	}

	/**
	 * Get property.
	 *
	 * @param propertyName the property name.
	 * @param tClass the class to adapt property to.
	 * @param <T> the type parameter
	 * @return the property
	 */
	public <T> T getProperty(final String propertyName, final Class<T> tClass) {
		return (T) Collections.emptyList();
	}

	/**
	 * Get price.
	 *
	 * @param predicate predicate to filter prices on
	 * @return the price
	 * @throws CommerceException if price can not be retrieved
	 */
	public String getPrice(final Predicate predicate) throws CommerceException {
		Cost total = getTotalPriceAmount();
		final PriceInfo totalPrice = new PriceInfo(total.getAmount(),
				locale,
				Currency.getInstance(total.getCurrency()));
		return totalPrice.getFormattedString();
	}

	public String getUnitPrice() {
		return null;
	}

	public String getPreTaxPrice() {
		return null;
	}

	public String getTax() {
		return null;
	}

	public String getTotalPrice() {
		return null;
	}

	public void setCommerceService(final ElasticPathCommerceService commerceService) {
		this.commerceService = commerceService;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public boolean equals(final Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * Sets the user locale.
	 *
	 * @param locale user locale
	 */
	public void setLocale(final Locale locale) {
		this.locale = locale;
	}

	private Cost getTotalPriceAmount() {
		final Total total = totals.iterator().next();
		return total.getCosts().iterator().next();
	}
}
