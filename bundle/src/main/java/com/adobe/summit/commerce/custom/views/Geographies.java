/*
 * Copyright ?? 2014 Elastic Path Software Inc. All rights reserved.
 */
package com.adobe.summit.commerce.custom.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.contrib.jsonpath.annotation.JsonPath;
import com.elasticpath.rest.client.urlbuilding.annotations.Path;
import com.elasticpath.rest.client.urlbuilding.annotations.Uri;
import com.elasticpath.rest.client.urlbuilding.annotations.Zoom;
import com.elasticpath.rest.sdk.EntryPointUris;
import com.elasticpath.rest.sdk.components.Country;
import com.elasticpath.rest.sdk.components.Linkable;
import com.elasticpath.rest.sdk.components.Region;
import com.elasticpath.rest.sdk.components.RegionList;

/**
 * Geographies supported by Cortex which contains supported countries and regions.
 */
@Uri(EntryPointUris.COUNTRIES)
@Zoom({ @Path("element"),
		@Path("element:regions"),
		@Path("element:regions:element") })
public class Geographies extends Linkable {
	
	@JsonPath("$._element")
	private Iterable<Country> countries = new ArrayList<Country>();

	/**
	 * Country list supported by cortex.
	 */
	private List<String> availableCountries = new ArrayList<String>();

	/**
	 * Map having region list with key as country code.
	 */
	private Map<String, List<String>> regions = new HashMap<String, List<String>>();

	/**
	 * Countries resource.
	 * 
	 * @return countries
	 */
	public Iterable<Country> getCountries() {
		return countries;
	}

	/**
	 * Set countries.
	 * 
	 * @param countries the countriesList to set
	 */
	public void setCountries(final Iterable<Country> countries) {
		this.countries = countries;
	}

	/**
	 * Returns countries list supported by Cortex.
	 * 
	 * @return the countriesList
	 */
	public List<String> getAvailableCountries() {
		return availableCountries;
	}

	/**
	 * Set countries list.
	 * 
	 * @param availableCountries the countriesList to set
	 */
	public void setAvailableCountries(final List<String> availableCountries) {
		this.availableCountries = availableCountries;
	}

	/**
	 * Return region map having list of regions for the countries and country code as key.
	 * 
	 * @return the regions
	 */
	public Map<String, List<String>> getRegions() {
		return regions;
	}

	/**
	 * Sets the region list for the country code.
	 * 
	 * @param regions the regions to set
	 */
	public void setRegions(final Map<String, List<String>> regions) {
		this.regions = regions;
	}

	/**
	 * Initialize method to load country list and regions supported by Cortex.
	 */
	public void initialize() {
		for (final Country country : countries) {
			availableCountries.add(country.getCountryCode() + "=" + country.getCountryDisplayName());
			loadRegions(country.getCountryCode(), country.getRegionList());
		}
	}

	/**
	 * Populates regions for the countries in map having list of regions with country code as key.
	 * 
	 * @param countryCode String
	 * @param regionList Collection<RegionList>
	 */
	private void loadRegions(final String countryCode, final Collection<RegionList> regionList) {

		for (final RegionList regionElement : regionList) {
			if (regionElement.getRegions() != null) {
				List<String> regionListForCountry = new ArrayList<String>();
				for (Region region : regionElement.getRegions()) {
					regionListForCountry.add(region.getRegionCode() + "=" + region.getRegionDisplayName());
				}
				regions.put(countryCode, regionListForCountry);
			}
		}
	}
}