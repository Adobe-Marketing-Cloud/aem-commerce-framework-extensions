/*
 * Copyright ?? 2014 Elastic Path Software Inc. All rights reserved.
 */
package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;
import com.elasticpath.rest.sdk.components.Self;

/**
 * Address Form.
 */
@Uri(EntryPointUris.PROFILE)
@Zoom(
		@Path("addresses")
)
public class AddressForm {
	@JsonPath("_addresses[0].self")
	private Self self;

	public Self getSelf() {
		return self;
	}

	public void setSelf(final Self self) {
		this.self = self;
	}

}
