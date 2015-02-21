package com.adobe.summit.commerce.custom.views;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.components.AddressEntry;
import com.elasticpath.rest.sdk.components.AppliedPromotions;
import com.elasticpath.rest.sdk.components.Cost;
import com.elasticpath.rest.sdk.components.Coupon;
import com.elasticpath.rest.sdk.components.Price;
import com.elasticpath.rest.sdk.components.PurchaseComponent;
import com.elasticpath.rest.sdk.components.PurchaseLineItem;
import com.elasticpath.rest.sdk.components.ShippingOption;

/**
 * Purchase.
 */
@Zoom({
		@Path("appliedPromotions:element"),
		@Path("coupons"),
		@Path("coupons:element"),
		@Path("coupons:element:appliedPromotions"),
		@Path("coupons:element:appliedPromotions:element"),
		@Path("lineitems"),
		@Path("lineitems:element"),
		@Path("billingaddress"),
		@Path("shipments:element"),
		@Path("shipments:element:destination"),
		@Path("shipments:element:shippingoption")
})
public class PurchaseView {

	@JsonPath("$._billingaddress[0]")
	private AddressEntry billingAddressEntry;

	@JsonPath("$._shipments[0]._element[0]._destination[0]")
	private AddressEntry shippingAddressEntry;

	@JsonPath("$._coupons[0]._element")
	private Iterable<Coupon> coupons;

	@JsonPath("$._lineitems[0]._element")
	private Iterable<PurchaseLineItem> lineItems;

	@JsonPath("$._shipments[0]._element[0]._shippingoption")
	private Iterable<ShippingOption> shippingOptions;

	@JsonPath("$._appliedpromotions[0]")
	private AppliedPromotions appliedPromotions;

	@JsonProperty("monetary-total")
	private Iterable<Price> total;

	@JsonProperty("tax-total")
	private Price tax;

	@JsonProperty("purchase-number")
	private String purchaseNumber;

	@JsonPath("$.purchase-date.display-value")
	private String date;

	@JsonProperty("status")
	private String status;

	@JsonPath("$._shipments[0]._element[0].status.code")
	private String shippingStatus;

	/**
	 * default constructor for unmarshaling purposes.
	 */
	public PurchaseView() {
		//default constructor for unmarshaling purposes.
	}

	/**
	 * construct a PurchaseView out of a PurchaseComponent.
	 *
	 * @param purchaseComponent PurchaseComponent
	 */
	public PurchaseView(final PurchaseComponent purchaseComponent) {
		billingAddressEntry = purchaseComponent.getFirstBillingAddress();
		if (purchaseComponent.getFirstShipmentList() != null && purchaseComponent.getFirstShipmentList().getFirstShipment() != null) {
			shippingAddressEntry = purchaseComponent.getFirstShipmentList().getFirstShipment().getFirstShippingAddressEntry();
			shippingOptions = purchaseComponent.getFirstShipmentList().getFirstShipment().getShippingOptions();
			shippingStatus = purchaseComponent.getFirstShipmentList().getFirstShipment().getShippingStatusCode();
		}
		if (purchaseComponent.getCoupons() != null) {
			coupons = purchaseComponent.getCoupons().getCoupons();
		}
		if (purchaseComponent.getPurchaseLineItemList() != null) {
			lineItems = purchaseComponent.getPurchaseLineItemList().getPurchaseLineItems();
		}
		if (purchaseComponent.getAppliedPromotions() != null && purchaseComponent.getAppliedPromotions().iterator().hasNext()) {
			appliedPromotions = purchaseComponent.getFirstAppliedPromotions();
		}
		total = purchaseComponent.getTotal();
		tax = purchaseComponent.getTax();
		purchaseNumber = purchaseComponent.getPurchaseNumber();
		date = purchaseComponent.getPurchaseDate();
		status = purchaseComponent.getStatus();
	}

	public Iterable<PurchaseLineItem> getLineItems() {
		return lineItems;
	}

	public BigDecimal getOrderTotalTaxNumeric() {
		return getTax().getAmount();
	}

	/**
	 * Calculate line items totals.
	 *
	 * @return line items total.
	 */
	public BigDecimal getOrderLineItemTotalNumeric() {
		BigDecimal lineItemTotal = BigDecimal.ZERO;

		for (PurchaseLineItem lineItem : lineItems) {
			lineItemTotal = lineItemTotal.add(lineItem.getPriceNumeric());
		}

		return lineItemTotal;
	}

	public BigDecimal getOrderTotalPriceNumeric() {
		return getTotal().getAmount();
	}

	/**
	 * Shipping Cost.
	 *
	 * @return the shipping cost.
	 */
	public BigDecimal getShippingOptionCostNumeric() {
		Cost shippingCost = getShippingOption().getCosts().iterator().next();
		return shippingCost.getAmount();
	}

	public Iterable<Coupon> getCoupons() {
		return coupons;
	}

	public ShippingOption getShippingOption() {
		return shippingOptions.iterator().next();
	}

	public Price getTotal() {
		return total.iterator().next();
	}

	public Price getTax() {
		return tax;
	}

	public String getDate() {
		return date;
	}

	public AppliedPromotions getAppliedPromotions() {
		return appliedPromotions;
	}

	public String getPurchaseNumber() {
		return purchaseNumber;
	}

	public String getStatus() {
		return status;
	}

	public String getShippingStatus() {
		return shippingStatus;
	}

	public AddressEntry getBillingAddressEntry() {
		return billingAddressEntry;
	}

	public AddressEntry getShippingAddressEntry() {
		return shippingAddressEntry;
	}
}
