package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;
import com.elasticpath.rest.sdk.components.AddressInfo;
import com.elasticpath.rest.sdk.components.Linkable;

/**
 * Representation for billing and shipping addresses.
 */
@Uri(EntryPointUris.CART)
@Zoom({@Path("order:billingaddressinfo"),
	    @Path("order:billingaddressinfo:selector"),
	    @Path("order:billingaddressinfo:selector:choice"),
		@Path("order:billingaddressinfo:selector:chosen"),
		@Path("order:billingaddressinfo:selector:choice:description"),
		@Path("order:billingaddressinfo:selector:chosen:description"),
		@Path("order:deliveries:element:destinationinfo"),
		@Path("order:deliveries:element:destinationinfo:selector"),
	    @Path("order:deliveries:element:destinationinfo:selector:choice"),
		@Path("order:deliveries:element:destinationinfo:selector:chosen"),
		@Path("order:deliveries:element:destinationinfo:selector:choice:description"),
		@Path("order:deliveries:element:destinationinfo:selector:chosen:description")})
public class SelectorAddresses extends Linkable {
	@JsonPath("$._order[0]._billingaddressinfo[0]")
	private AddressInfo billingAddressInfo;
	
	@JsonPath("$._order[0]._deliveries[0]._element[0]._destinationinfo[0]")
	private AddressInfo shippingAddressInfo;

	public AddressInfo getBillingAddressInfo() {
		return billingAddressInfo;
	}

	public void setBillingAddressInfo(final AddressInfo billingAddressInfo) {
		this.billingAddressInfo = billingAddressInfo;
	}

	public AddressInfo getShippingAddressInfo() {
		return shippingAddressInfo;
	}

	public void setShippingAddressInfo(final AddressInfo shippingAddressInfo) {
		this.shippingAddressInfo = shippingAddressInfo;
	}
}
