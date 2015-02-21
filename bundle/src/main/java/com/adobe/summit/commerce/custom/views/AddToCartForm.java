package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.FollowLocation;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;

/**
 * Add to cart action.
 */
@Uri(EntryPointUris.ITEM_LOOKUP)
@Zoom(@Path("addtocartform"))
@FollowLocation
public class AddToCartForm {

	@JsonPath("$._addtocartform[0].links[?(@.rel == 'addtodefaultcartaction')].uri")
	private Iterable<String> addToCartActionUri;

	public String getAddToCartActionUri() {
		return addToCartActionUri.iterator().next();
	}
}
