package com.adobe.summit.commerce.custom.views;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.components.Address;
import com.elasticpath.rest.sdk.components.Name;
import com.elasticpath.rest.sdk.components.ShippingOption;


/**
 * Order shipping information.
 */
@Zoom({
	@Path("deliveries:element:destinationinfo:destination"),
	@Path("deliveries:element:shippingoptioninfo:shippingoption"),
	@Path("deliveries:element:shippingoptioninfo:shippingoption:appliedpromotions:element"),
})
public class AppliedShippingPromotions {

	@JsonPath("$._deliveries[0]._element[0]._destinationinfo[0]._destination[0].address")
	private Address shippingAddress;
	@JsonPath("$._deliveries[0]._element[0]._destinationinfo[0]._destination[0].name")
	private Name shippingAddressName;
	@JsonPath("$._deliveries[0]._element[0]._shippingoptioninfo[0]._shippingoption[0]")
	private ShippingOption shippingOption;

	public ShippingOption getShippingOption() {
		return shippingOption;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public Name getShippingAddressName() {
		return shippingAddressName;
	}
}