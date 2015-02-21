/*
 * Copyright ?? 2014 Elastic Path Software Inc. All rights reserved.
 */
package com.adobe.summit.commerce.custom.views;

import java.util.List;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;
import com.elasticpath.rest.sdk.components.AddressEntry;
import com.elasticpath.rest.sdk.components.Linkable;

/**
 * Addresses representation.
 */
@Uri(EntryPointUris.PROFILE)
@Zoom({ @Path("addresses:element"),
		@Path("addresses:element:address"),
		@Path("addresses:element:name") })
public class Addresses extends Linkable {
	@JsonPath("$._addresses[0]._element")
	private List<AddressEntry> addressEntries;

	/**
	 * Returns the list of addresses.
	 * 
	 * @return the list of addresses
	 */
	public List<AddressEntry> getAddressEntries() {
		return addressEntries;
	}

	/**
	 * Sets the list of addresses.
	 * 
	 * @param addressEntries the list of zoomedAddresses
	 */
	public void setAddressEntries(final List<AddressEntry> addressEntries) {
		this.addressEntries = addressEntries;
	}

}
