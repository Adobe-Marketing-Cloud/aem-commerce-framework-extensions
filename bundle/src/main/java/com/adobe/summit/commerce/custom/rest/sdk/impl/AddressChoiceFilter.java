package com.adobe.summit.commerce.custom.rest.sdk.impl;

import com.elasticpath.rest.sdk.components.Choice;

/**
 * Filter for selecting choice based on description uri.
 */
public class AddressChoiceFilter implements org.apache.commons.collections.Predicate {

	private final String addressPath;

	/**
	 * @param addressPath URI of the choice description to select
	 */
	AddressChoiceFilter(final String addressPath) {
		this.addressPath = addressPath;
	}

	@Override
	public boolean evaluate(final Object choiceObj) {
		return addressPath.equals(((Choice) choiceObj).getDescriptions().get(0).getSelf().getUri());
	}

}
