/*
 * Copyright ?? 2014 Elastic Path Software Inc. All rights reserved.
 */
package com.adobe.summit.commerce.custom.views;

import com.elasticpath.rest.client.urlbuilding.annotations.FollowLocation;
import com.elasticpath.rest.sdk.components.AddressEntry;

/**
 * For adding an address in Cortex and following the redirect in the response.
 */
@FollowLocation
public class CreateAddressView extends AddressEntry { }
