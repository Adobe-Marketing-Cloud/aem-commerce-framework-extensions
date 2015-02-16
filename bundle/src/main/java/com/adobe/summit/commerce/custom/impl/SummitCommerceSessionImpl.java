package com.adobe.summit.commerce.custom.impl;

import java.math.BigDecimal;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.common.AbstractJcrCommerceService;
import com.adobe.cq.commerce.common.AbstractJcrCommerceSession;
import com.adobe.summit.commerce.custom.SummitCommerceSession;

/**
 * CommerceSesession to be used for Custom Summit Commerce Service.
 *
 */
public class SummitCommerceSessionImpl extends AbstractJcrCommerceSession implements SummitCommerceSession {

    public SummitCommerceSessionImpl(AbstractJcrCommerceService commerceService,
                                  SlingHttpServletRequest request,
                                  SlingHttpServletResponse response,
                                  Resource resource) throws CommerceException {
        super(commerceService, request, response, resource);
    }
    
    /**
	 * Returns shipping cost.
	 *
	 * @param method String
	 * @return BigDecimal
	 */
	@Override
	protected BigDecimal getShipping(final String method) {
		return BigDecimal.ZERO;
	}
}
