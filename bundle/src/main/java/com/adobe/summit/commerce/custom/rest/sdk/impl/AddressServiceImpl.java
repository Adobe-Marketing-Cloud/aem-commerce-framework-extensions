package com.adobe.summit.commerce.custom.rest.sdk.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import com.adobe.cq.address.api.Address;
import com.adobe.cq.address.api.AddressException;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.adobe.summit.commerce.custom.views.AddAddressAction;
import com.adobe.summit.commerce.custom.views.Addresses;
import com.adobe.summit.commerce.custom.views.CreateAddressView;
import com.adobe.summit.commerce.custom.views.DefaultBillingAddress;
import com.adobe.summit.commerce.custom.views.DefaultShippingAddress;
import com.elasticpath.aem.address.constants.ElasticPathAddressConstants;
import com.elasticpath.aem.address.impl.AddressDetailBuilder;
import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.client.jaxrs.JaxRsUtil;
import com.elasticpath.rest.sdk.components.AddressEntry;
import com.elasticpath.rest.sdk.components.Name;
import com.elasticpath.rest.sdk.service.AddressService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import javax.ws.rs.core.Response;

public class AddressServiceImpl implements AddressService {

	private CortexClient client;

	public AddressServiceImpl(final CortexClient client) {
		this.client = client;
	}

	@Override
	public Address getAddress(final ResourceResolver resourceResolver, final String addressPath) throws AddressException {
		final AddressEntry addressEntry = client.get(addressPath, AddressEntry.class).getCortexView();

		return createAddressResource(resourceResolver, addressEntry);
	}

	@Override
	public List<Address> getAddressList(final ResourceResolver resourceResolver) throws AddressException {
		final Addresses addresses = client.get(Addresses.class).getCortexView();

		final List<Address> addressList = Lists.newArrayList();
		if (addresses.getAddressEntries() == null) {
			return addressList;
		} else {
			for (final AddressEntry addressEntry : addresses.getAddressEntries()) {
				addressList.add(createAddressResource(resourceResolver, addressEntry));
			}

			return addressList;
		}
	}

	@Override
	public Address createAddress(final ResourceResolver resourceResolver, final Map<String, Object> properties) throws AddressException {
		AddAddressAction addAddressAction = client.get(AddAddressAction.class).getCortexView();

		CreateAddressView addressEntry = client.post(
				addAddressAction.getActionUri(),
				AddressDetailBuilder.newBuilder().withProfile(properties).withAddress(properties).build(),
				CreateAddressView.class).getCortexView();

		return createAddressResource(resourceResolver, addressEntry);
	}

	@Override
	public Address updateAddress(final ResourceResolver resourceResolver, final Address address, final Map<String, Object> properties) throws AddressException {

		Map<String, Object> addressDetails = AddressDetailBuilder.newBuilder()
				.withProfile(properties)
				.withAddress(properties).build();

		Response.StatusType status = client.put(address.getPath(), addressDetails);

		if (JaxRsUtil.isSuccessful(status)) {
			ValueMapResource addressResource = new ValueMapResource(
					resourceResolver,
					address.getPath(),
					"",
					new ValueMapDecorator(addressDetails)
			);

			return new Address(addressResource);
		} else {
			throw new AddressException("Failed to update address, server responded with: "
					+ JaxRsUtil.getStatusString(status)
					+ " for the details: "
					+ Joiner.on(',').withKeyValueSeparator("=").join(addressDetails));
		}
	}

	@Override
	public void delete(final String path) {
		client.delete(path);
	}

	@Override
	public Address getDefaultBillingAddress(final ResourceResolver resourceResolver) throws AddressException {
		final DefaultBillingAddress billingAddress = client.get(DefaultBillingAddress.class).getCortexView();

		return createAddressResource(resourceResolver, billingAddress.getBillingAddress());
	}

	@Override
	public Address getDefaultShippingAddress(final ResourceResolver resourceResolver) throws AddressException {
		final DefaultShippingAddress shippingAddress = client.get(DefaultShippingAddress.class).getCortexView();

		return createAddressResource(resourceResolver, shippingAddress.getShippingAddress());
	}

	private Address createAddressResource(final ResourceResolver resourceResolver, final AddressEntry addressEntry) throws AddressException {
		ValueMapResource addressResource;
		if (addressEntry == null) {
			throw new AddressException("No Address found");
		} else {
			addressResource = new ValueMapResource(resourceResolver, addressEntry.getSelf().getUri(), "", getAddressDetailsMap(addressEntry));
		}
		return new Address(addressResource);
	}

	private ValueMap getAddressDetailsMap(final AddressEntry addressEntry) {
		final Map<String, Object> addressdetails = new HashMap<String, Object>();
		addressdetails.put(ElasticPathAddressConstants.PATH, addressEntry.getSelf().getUri());
		Name name = addressEntry.getName();
		addressdetails.put(ElasticPathAddressConstants.FIRST_NAME, name.getGivenName());
		addressdetails.put(ElasticPathAddressConstants.LAST_NAME, name.getFamilyName());

		com.elasticpath.rest.sdk.components.Address address = addressEntry.getAddress();
		addressdetails.put(ElasticPathAddressConstants.STREET1, address.getStreetAddress());
		addressdetails.put(ElasticPathAddressConstants.STREET2, address.getExtendedAddress());
		addressdetails.put(ElasticPathAddressConstants.CITY, address.getLocality());
		addressdetails.put(ElasticPathAddressConstants.STATE, address.getRegion());
		addressdetails.put(ElasticPathAddressConstants.COUNTRY, address.getCountryName());
		addressdetails.put(ElasticPathAddressConstants.ZIP, address.getPostalCode());
		return new ValueMapDecorator(addressdetails);
	}
}
