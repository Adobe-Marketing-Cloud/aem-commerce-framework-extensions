package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.FollowLocation;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;
import com.elasticpath.rest.sdk.components.Cost;

/**
 * Price zoomed from item.
 */
@Uri(EntryPointUris.ITEM_LOOKUP)
@Zoom(@Path("price"))
@FollowLocation
public class ProductPrice {

	/**
	 * The purchase price.
	 */
	@JsonPath("$._price[0].purchase-price[0]")
	private Cost purchasePrice;


	public Cost getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(final Cost purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

}
