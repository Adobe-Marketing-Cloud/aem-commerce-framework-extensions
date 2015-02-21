package com.adobe.summit.commerce.custom.rest.sdk.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.PlacedOrder;
import com.adobe.summit.commerce.custom.impl.SummitPlacedOrderDetails;
import com.adobe.summit.commerce.custom.views.PurchaseView;
import com.adobe.summit.commerce.custom.views.PurchasesListView;
import com.adobe.summit.commerce.custom.views.PurchasesSearchView;
import com.elasticpath.aem.commerce.ElasticPathCommerceSession;
import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.sdk.components.PurchaseComponent;
import com.elasticpath.rest.sdk.service.PurchaseService;
import com.google.common.base.Optional;

/**
 * Purchase Service is responsible for retrieving completed purchase information from Cortex, or PlacedOrders in AEM speak.
 */
public class PurchaseServiceImpl implements PurchaseService {

	private final CortexClient cortexClient;

	/**
	 * Constructor.
	 * @param cortexClient the cortex client.
	 */
	public PurchaseServiceImpl(final CortexClient cortexClient) {
		this.cortexClient = cortexClient;
	}


	@Override
	public List<PlacedOrder> getPlacedOrders(final ElasticPathCommerceSession commerceSession, final String predicate) {
		PurchasesListView purchasesListView = cortexClient.get(PurchasesListView.class).getCortexView();

		Iterable<PurchaseComponent> purchaseComponents = purchasesListView.getPurchases();
		List<PlacedOrder> placedOrderList = new ArrayList<PlacedOrder>();
		for (PurchaseComponent purchaseComponent : purchaseComponents) {
			if (StringUtils.isEmpty(predicate) || predicate.equalsIgnoreCase(purchaseComponent.getStatus())) {
				PurchaseView purchaseView = new PurchaseView(purchaseComponent);
				placedOrderList.add(new SummitPlacedOrderDetails(commerceSession, purchaseView));
			}
		}

		return placedOrderList;
	}

	@Override
	public PlacedOrder getPlacedOrder(final String orderId, final ElasticPathCommerceSession commerceSession) throws CommerceException {
		Optional<String> purchaseUri = findPurchaseUriForOrderId(orderId);
		if (purchaseUri.isPresent()) {
			PurchaseView view = cortexClient.get(purchaseUri.get(), PurchaseView.class).getCortexView();
			return new SummitPlacedOrderDetails(commerceSession, view);
		}
		throw new CommerceException("No Cortex Purchase Found for OrderId: " + orderId);
	}

	private Optional<String> findPurchaseUriForOrderId(final String orderId) {
		PurchasesSearchView purchasesSearchView = cortexClient.get(PurchasesSearchView.class).getCortexView();
		for (PurchaseComponent purchaseComponent : purchasesSearchView.getPurchases()) {
			if (orderId.equals(purchaseComponent.getPurchaseNumber())) {
				return Optional.of(purchaseComponent.getSelf().getUri());
			}
		}
		return Optional.absent();
	}
}
