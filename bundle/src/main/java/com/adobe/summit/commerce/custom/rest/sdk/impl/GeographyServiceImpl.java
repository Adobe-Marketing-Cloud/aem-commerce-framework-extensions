package com.adobe.summit.commerce.custom.rest.sdk.impl;

import java.util.List;
import java.util.Map;

import com.adobe.summit.commerce.custom.views.Geographies;
import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.sdk.service.GeographyService;

/**
 * Responsible for looking up Geographic information from Cortex.
 */
public final class GeographyServiceImpl implements GeographyService {

	private final CortexClient cortexClient;
	private Geographies geographies;

	/**
	 * Constructor.
	 * @param cortexClient the cortex client.
	 */
	public GeographyServiceImpl(final CortexClient cortexClient) {
		this.cortexClient = cortexClient;
	}


	@Override
	public List<String> getAvailableCountries() {
		lazyLoadGeographies();
		return geographies.getAvailableCountries();
	}

	@Override
	public Map<String, List<String>> getRegions() {
		lazyLoadGeographies();
		return geographies.getRegions();
	}

	private void lazyLoadGeographies() {
		if (geographies == null) {
			geographies = cortexClient.get(Geographies.class).getCortexView();
			geographies.initialize();
		}
	}

}
