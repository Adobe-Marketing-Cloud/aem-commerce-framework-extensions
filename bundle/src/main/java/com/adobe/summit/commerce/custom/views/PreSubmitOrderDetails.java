package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;

/**
 * Order details required before submitting a purchase, zoomed from the default cart.
 */
@Uri(EntryPointUris.CART)
@Zoom(@Path("order,order:paymentmethodinfo:paymenttokenform"))
public class PreSubmitOrderDetails {

	/**
	 * The order uri.
	 */
	@JsonPath("$._order[0].self.uri")
	private String orderUri;

	/**
	 * The payment token submit action uri.
	 */
	@JsonPath("$._order[0]._paymentmethodinfo[0]._paymenttokenform[0].links[0].uri")
	private String createPaymentTokenActionUri;


	public String getOrderUri() {
		return orderUri;
	}

	public void setOrderUri(final String orderUri) {
		this.orderUri = orderUri;
	}

	public String getCreatePaymentTokenActionUri() {
		return createPaymentTokenActionUri;
	}

	public void setCreatePaymentTokenActionUri(final String createPaymentTokenActionUri) {
		this.createPaymentTokenActionUri = createPaymentTokenActionUri;
	}
}
