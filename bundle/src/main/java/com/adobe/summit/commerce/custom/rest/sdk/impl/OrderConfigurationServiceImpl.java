package com.adobe.summit.commerce.custom.rest.sdk.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

import com.adobe.cq.commerce.api.CommerceConstants;
import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.summit.commerce.custom.views.AddressForm;
import com.adobe.summit.commerce.custom.views.CreateAddress;
import com.adobe.summit.commerce.custom.views.CreateEmail;
import com.adobe.summit.commerce.custom.views.SelectorAddresses;
import com.elasticpath.aem.address.impl.AddressDetailBuilder;
import com.elasticpath.aem.commerce.constants.ElasticPathConstants;
import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.client.CortexResponse;
import com.elasticpath.rest.client.jaxrs.JaxRsUtil;
import com.elasticpath.rest.sdk.components.AddressInfo;
import com.elasticpath.rest.sdk.components.Choice;
import com.elasticpath.rest.sdk.components.Link;
import com.elasticpath.rest.sdk.service.OrderConfigurationService;
import com.google.common.collect.ImmutableMap;
import com.day.cq.personalization.UserPropertiesUtil;

/**
 * Order Service implementation. Update methods on this class are intelligent enough to only make calls to
 * the commerceProvider if the delta has changed.
 */
public class OrderConfigurationServiceImpl implements OrderConfigurationService {

	private static final String ADDRESS_PATH = "addressPath";

	private final CortexClient cortexClient;
	private AddressForm addressForm;

	/**
	 * Creates an instance of this class currently this is request scoped service.
	 *
	 * @param cortexClient the cortex client.
	 */
	public OrderConfigurationServiceImpl(final CortexClient cortexClient) {
		this.cortexClient = cortexClient;
	}

	@Override
	public void updateAddresses(final Map<String, Object> delta) throws CommerceException {
		Map<String, Object> billingAddress = new HashMap<String, Object>();
		Map<String, Object> shippingAddress = new HashMap<String, Object>();
		populateAddressesFromDelta(delta, billingAddress, shippingAddress);

		if (billingAddress.isEmpty() && shippingAddress.isEmpty()) {
			return;
		}

		createAddressInCortexIfNew(shippingAddress);
		createAddressInCortexIfNew(billingAddress);

		SelectorAddresses addresses = cortexClient.get(SelectorAddresses.class).getCortexView();

		selectAddress(addresses.getBillingAddressInfo(), (String) billingAddress.get(ADDRESS_PATH));
		selectAddress(addresses.getShippingAddressInfo(), (String) shippingAddress.get(ADDRESS_PATH));
	}

	@Override
	public void updateOrderEmailIfGuest(final SlingHttpServletRequest request, final Map<String, Object> delta) throws CommerceException {
		String billingEmail = (String) delta.get("email");

		if (UserPropertiesUtil.isAnonymous(request) && StringUtils.isNotBlank(billingEmail)) {
			CortexResponse<CreateEmail> responseWrapper =
					cortexClient.post(ImmutableMap.of("email", (Object) billingEmail), CreateEmail.class);
			if (JaxRsUtil.isNotSuccessful(responseWrapper.getStatusType())) {
				throw new CommerceException("Failed to add email for guest user: " + JaxRsUtil.getStatusString(responseWrapper.getStatusType()));
			}
		}
	}

	@Override
	public void updateShippingOption(final Map<String, Object> formData) throws CommerceException {
		// The full cortex uri of each shipping method (aka. shipping option) is embedded in the
		// client side form as the CommerceConstants.SHIPPING_OPTION property.  This little trick
		// allows us to post directly to the select the shipping option here.
		// See shippingmethods.jsp
		// Further, we also use a magic string, "chosen", when a shipping option has already been
		// selected on the order.  See ElasticPathCommerceSessionImpl.getOrder().
		String shippingOptionFormValue = (String) formData.get(CommerceConstants.SHIPPING_OPTION);
		if (StringUtils.isNotBlank(shippingOptionFormValue) && !"chosen".equals(shippingOptionFormValue)) {

			Response.StatusType status = cortexClient.post(shippingOptionFormValue, null);

			if (JaxRsUtil.isNotSuccessful(status)) {
				throw new CommerceException("Failed to change shipping option: " + JaxRsUtil.getStatusString(status));
			}
		}
	}

	private void createAddressInCortexIfNew(final Map<String, Object> address)
			throws CommerceException {
		if (address.isEmpty() || StringUtils.isNotEmpty((String) address.get(ADDRESS_PATH))) {
			return;
		}
		AddressForm addressForm = lazyLoadAddressForm();
		Map<String, Object> cortexAddressFormFields = AddressDetailBuilder.newBuilder().withProfile(address).withAddress(address).build();

		CortexResponse<CreateAddress> responseWrapper =
				cortexClient.post(addressForm.getSelf().getUri(), cortexAddressFormFields, CreateAddress.class);

		if (JaxRsUtil.isNotSuccessful(responseWrapper.getStatusType())) {
			throw new CommerceException("Failed to add address : " + JaxRsUtil.getStatusString(responseWrapper.getStatusType()));
		}

		address.put(ADDRESS_PATH, responseWrapper.getCortexView().getSelf().getUri());
	}

	private void selectAddress(final AddressInfo addressInfo, final String addressPath) throws CommerceException {
		if (StringUtils.isEmpty(addressPath)) {
			return;
		}
		List<Choice> chosens = addressInfo.getSelectors().get(0).getChosens();
		if (chosens != null && !chosens.isEmpty() && addressPath.equals(chosens.get(0).getDescriptions().get(0).getSelf().getUri())) {
			return;
		}
		List<Choice> choices = addressInfo.getSelectors().get(0).getChoices();
		CollectionUtils.filter(choices, new AddressChoiceFilter(addressPath));
		if (choices.isEmpty()) {
			throw new CommerceException("Address not found in saved addresses");
		}
		chooseAddressChoice(choices.iterator().next());
	}

	private void chooseAddressChoice(final Choice choice) throws CommerceException {
		Iterable<Link> links = choice.getLinks();
		String selectActionUri = StringUtils.EMPTY;
		for (final Link link : links) {
			if (ElasticPathConstants.SELECTACTION.equals(link.getRel())) {
				selectActionUri = link.getUri();
			}
		}
		if (StringUtils.isEmpty(selectActionUri)) {
			throw new CommerceException("Unable to choose selected address");
		}
		Response.StatusType status = cortexClient.post(selectActionUri, null);
		if (JaxRsUtil.isNotSuccessful(status)) {
			throw new CommerceException("Failed to choose address : " + JaxRsUtil.getStatusString(status));
		}
	}

	private void populateAddressesFromDelta(final Map<String, Object> delta, final Map<String, Object> billingAddress,
											final Map<String, Object> shippingAddress) {

		for (Map.Entry<String, Object> entry : delta.entrySet()) {
			String key = entry.getKey();
			if (key.startsWith(CommerceConstants.BILLING_PREFIX)) {
				billingAddress.put(key.substring(CommerceConstants.BILLING_PREFIX.length()), entry.getValue());
			}
			if (key.startsWith(CommerceConstants.SHIPPING_PREFIX)) {
				shippingAddress.put(key.substring(CommerceConstants.SHIPPING_PREFIX.length()), entry.getValue());
			}
		}
		if (StringUtils.isNotEmpty((String) delta.get(CommerceConstants.SHIPPING_ADDR_SAME))) {
			shippingAddress.clear();
			shippingAddress.putAll(billingAddress);
		}
	}

	private AddressForm lazyLoadAddressForm() {
		if (addressForm == null) {
			addressForm = cortexClient.get(AddressForm.class).getCortexView();
		}
		return addressForm;
	}
}
