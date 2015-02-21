package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;
import com.elasticpath.rest.sdk.components.Linkable;
import com.elasticpath.rest.sdk.components.PurchaseComponent;

/**
 * View for the list of placed orders.
 */
@Uri(EntryPointUris.PROFILE)
@Zoom({
		@Path("purchases"),
		@Path("purchases:element"),
})
public class PurchasesSearchView extends Linkable {

	/**
	 * The purchases.
	 */
	@JsonPath("$._purchases[0]._element")
	private Iterable<PurchaseComponent> purchases;

	public Iterable<PurchaseComponent> getPurchases() {
		return purchases;
	}
}
