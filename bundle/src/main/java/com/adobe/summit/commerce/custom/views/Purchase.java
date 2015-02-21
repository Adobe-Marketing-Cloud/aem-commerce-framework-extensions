package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.FollowLocation;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;

/**
 * The purchase retrieved after an order is processed.
 */
@Zoom(@Path("order"))
@FollowLocation
public class Purchase {

	/**
	 * The purchase number.
	 */
	@JsonPath("$.purchase-number")
	private String purchaseNumber;

	public String getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(final String purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}
}
