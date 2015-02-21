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
 * Default Shipping address representation.
 */
@Uri(EntryPointUris.PROFILE)
@Zoom(@Path("addresses:shippingaddresses:default"))
public class DefaultShippingAddress extends Linkable {

	@JsonPath("$._addresses[0]._shippingaddresses[0]._default[0]")
	private AddressEntry shippingAddress;
	
	/**
	 * Returns the zoomed shipping address.
	 *
	 * @return zoomedAddress the zoomed shipping address
	 */
	public AddressEntry getShippingAddress() {
		return this.shippingAddress;
	}
	
	/**
	 * Sets the zoomed shipping address.
	 * 
	 * @param shippingAddress the zoomed shipping address
	 */
	public void setShippingAddress(final AddressEntry shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
}
