/*
 * Copyright ?? 2014 Elastic Path Software Inc. All rights reserved.
 */
package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;
import com.elasticpath.rest.sdk.components.AddressEntry;
import com.elasticpath.rest.sdk.components.Linkable;

/**
 * Default Billing address representation.
 */
@Uri(EntryPointUris.PROFILE)
@Zoom(@Path("addresses:billingaddresses:default"))
public class DefaultBillingAddress extends Linkable {

	@JsonPath("$._addresses[0]._billingaddresses[0]._default[0]")
	private AddressEntry billingAddress;

	/**
	 * Returns the zoomed billing address.
	 * 
	 * @return zoomedAddress the zoomed billing address
	 */
	public AddressEntry getBillingAddress() {
		return this.billingAddress;
	}

	/**
	 * Sets the zoomed billing address.
	 * 
	 * @param billingAddress the zoomed billing address
	 */
	public void setBillingAddress(final AddressEntry billingAddress) {
		this.billingAddress = billingAddress;
	}

}
