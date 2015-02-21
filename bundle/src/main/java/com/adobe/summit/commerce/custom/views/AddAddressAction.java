/*
 * Copyright ?? 2014 Elastic Path Software Inc. All rights reserved.
 */
package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;


/**
 * Add address action.
 */
@Uri(EntryPointUris.PROFILE)
@Zoom(@Path("addresses:addressform"))
public class AddAddressAction {

	@JsonPath("$._addresses[0]._addressform[0].links[0].uri")
	private String actionUri;

	public String getActionUri() {
		return actionUri;
	}

	public void setActionUri(final String actionUri) {
		this.actionUri = actionUri;
	}
}
