package com.adobe.summit.commerce.custom.views;

import java.util.Currency;
import java.util.Locale;

import com.adobe.cq.commerce.api.PriceInfo;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.components.Linkable;
import com.elasticpath.rest.sdk.components.Self;
import com.elasticpath.rest.sdk.components.Address;
import com.elasticpath.rest.sdk.components.Cost;
import com.elasticpath.rest.sdk.components.Coupon;
import com.elasticpath.rest.sdk.components.ShippingOption;
import com.elasticpath.rest.sdk.components.Tax;
import com.elasticpath.rest.sdk.components.Total;
import com.elasticpath.rest.sdk.components.Name;

/**
 * Order.
 */
@Zoom({
		@Path("billingaddressinfo"),
		@Path("billingaddressinfo:billingaddress"),
		@Path("deliveries:element:destinationinfo"),
		@Path("deliveries:element:destinationinfo:destination"),
		@Path("deliveries:element:shippingoptioninfo:shippingoption"),
		@Path("deliveries:element:shippingoptioninfo:shippingoption:appliedpromotions:element"),
		@Path("total"),
		@Path("tax"),
		@Path("couponinfo:coupon"),
		@Path("couponinfo:coupon:appliedpromotions:element")
	  })
public class Order extends Linkable {

	@JsonPath("$._billingaddressinfo[0].self")
	private Self billingSelf;
	@JsonPath("$._billingaddressinfo[0]._billingaddress[0].address")
	private Address billingAddress;
	@JsonPath("$._billingaddressinfo[0]._billingaddress[0].name")
	private Name billingAddressName;

	@JsonPath("_deliveries[0]._element[0]._destinationinfo[0].self")
	private Self shippingSelf;
	@JsonPath("$._deliveries[0]._element[0]._destinationinfo[0]._destination[0].address")
	private Address shippingAddress;
	@JsonPath("$._deliveries[0]._element[0]._destinationinfo[0]._destination[0].name")
	private Name shippingAddressName;

	// TODO: This is too restrictive.  We need a collection of shipping options, not just the first one.
	@JsonPath("$._deliveries[0]._element[0]._shippingoptioninfo[0]._shippingoption[0]")
	private ShippingOption shippingOption;

	@JsonPath("$._total[0]")
	private Total orderTotal;
	@JsonPath("$._tax[0]")
	private Tax orderTax;

	@JsonPath("$._couponinfo[0]._coupon")
	private Iterable<Coupon> coupons;

	public Self getBillingSelf() {
		return billingSelf;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public Name getBillingAddressName() {
		return billingAddressName;
	}

	public Self getShippingSelf() {
		return shippingSelf;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public Name getShippingAddressName() {
		return shippingAddressName;
	}

	public ShippingOption getShippingOption() {
		return shippingOption;
	}

	public Iterable<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * Get order total.
	 *
	 * @param locale the locale
	 * @return the order total
	 */
	public PriceInfo getOrderTotal(final Locale locale) {
		Cost orderTotalCost = orderTotal.getCosts().iterator().next();
		Currency currency = Currency.getInstance(orderTotalCost.getCurrency());
		return new PriceInfo(orderTotalCost.getAmount(), locale, currency);
	}

	/**
	 * Get order tax.
	 *
	 * @param locale the locale
	 * @return the order tax
	 */
	public PriceInfo getOrderTax(final Locale locale) {
		Cost orderTotalTaxCost = orderTax.getTotal();
		Currency currency = Currency.getInstance(orderTotalTaxCost.getCurrency());
		return new PriceInfo(orderTotalTaxCost.getAmount(), locale, currency);
	}
}
