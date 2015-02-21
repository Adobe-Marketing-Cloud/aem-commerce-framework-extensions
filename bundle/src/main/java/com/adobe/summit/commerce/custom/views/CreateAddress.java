/*
 * Copyright ?? 2014 Elastic Path Software Inc. All rights reserved.
 */
package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.FollowLocation;
import com.elasticpath.rest.sdk.components.Self;

/**
 * Address Self.
 */
@FollowLocation
public class CreateAddress {

	@JsonPath("$.self")
	private Self self;

	public Self getSelf() {
		return self;
	}

	public void setSelf(final Self self) {
		this.self = self;
	}

}
