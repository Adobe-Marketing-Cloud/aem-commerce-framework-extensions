/*
 * Copyright ?? 2014 Elastic Path Software Inc. All rights reserved.
 */
package com.adobe.summit.commerce.custom.views;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.adobe.cq.commerce.api.ShippingMethod;
import com.google.common.collect.Lists;

import com.elasticpath.aem.commerce.constants.ElasticPathConstants;
import com.elasticpath.aem.commerce.impl.ElasticPathShippingMethodImpl;
import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.components.SelectorShippingOption;

/**
 * ShippingOptions.
 */
@Zoom({
		@Path("deliveries:element:shippingoptioninfo:selector:chosen"),
		@Path("deliveries:element:shippingoptioninfo:selector:chosen:description"),
		@Path("deliveries:element:shippingoptioninfo:selector:choice"),
		@Path("deliveries:element:shippingoptioninfo:selector:choice:description"),
})
public class AvailableShippingMethods {
	@JsonPath("_deliveries[0]._element[0]._shippingoptioninfo[0]._selector[0]._choice")
	private Iterable<SelectorShippingOption> shippingOptionChoices;
	@JsonPath("_deliveries[0]._element[0]._shippingoptioninfo[0]._selector[0]._chosen[0]")
	private SelectorShippingOption chosenShippingOption;

	/**
	 * Adapt the Shipping options into an AEM Shipping Method list.
	 * @return ShippingMethod list.
	 */
	public List<ShippingMethod> adaptToShippingMethodList() {
		List<ShippingMethod> shippingMethodList = Lists.newArrayList();

		
		if (shippingOptionChoices != null) {
			for (SelectorShippingOption shippingOption : shippingOptionChoices) {
				shippingMethodList.add(shippingOption.adaptToShippingMethod());
			}
		}

		if (chosenShippingOption != null) {
			ShippingMethod method = chosenShippingOption.adaptToShippingMethod();
			shippingMethodList.add(new ElasticPathShippingMethodImpl(
					"chosen", method.getTitle(), method.getDescription(), method.getProperty(ElasticPathConstants.AMOUNT, BigDecimal.class)));

		}

		Collections.sort(shippingMethodList, new Comparator<ShippingMethod>() {
			@Override
			public int compare(final ShippingMethod method1, final ShippingMethod method2) {
				if (method1 instanceof ElasticPathShippingMethodImpl) {
					if (method2 instanceof ElasticPathShippingMethodImpl) {
					 	ElasticPathShippingMethodImpl epShipping = (ElasticPathShippingMethodImpl) method2;
						return ((ElasticPathShippingMethodImpl) method1).getAmount().compareTo(epShipping.getAmount());
					}
					return 1;
				}
				return -1;
			}
		});

		return shippingMethodList;
	}
}