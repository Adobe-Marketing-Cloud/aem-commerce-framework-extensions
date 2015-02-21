package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;
import com.elasticpath.rest.sdk.components.PurchaseComponent;

/**
 * View for the list of placed orders.
 */
@Uri(EntryPointUris.PROFILE)
@Zoom({
		@Path("purchases"),
		@Path("purchases:element"),
		@Path("purchases:element:appliedPromotions:element"),
		@Path("purchases:element:coupons"),
		@Path("purchases:element:coupons:element"),
		@Path("purchases:element:coupons:element:appliedPromotions"),
		@Path("purchases:element:coupons:element:appliedPromotions:element"),
		@Path("purchases:element:lineitems"),
		@Path("purchases:element:lineitems:element"),
		@Path("purchases:element:billingaddress"),
		@Path("purchases:element:shipments:element"),
		@Path("purchases:element:shipments:element:destination"),
		@Path("purchases:element:shipments:element:shippingoption")
})
public class PurchasesListView {


	/**
	 * The purchases.
	 */
	@JsonPath("$._purchases[0]._element")
	private Iterable<PurchaseComponent> purchases;

	public Iterable<PurchaseComponent> getPurchases() {
		return purchases;
	}
}
