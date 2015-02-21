package com.adobe.summit.commerce.custom.rest.sdk.internal;

import java.util.Locale;

import com.adobe.summit.commerce.custom.views.Order;

/**
 * Order Service for use in other services. This is implementation specific because it uses
 * and implementation specific view.
 */
public interface OrderViewService {
	/**
	 * Get the order represented as an Order view.
	 * @param userLocale user locale.
	 * @return an order view.
	 */
	Order getOrder(Locale userLocale);
}
