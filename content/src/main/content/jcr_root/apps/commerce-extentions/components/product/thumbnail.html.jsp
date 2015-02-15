<%@page session="false"%><%--
  ADOBE CONFIDENTIAL
  __________________

   Copyright 2014 Adobe Systems Incorporated
   All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.

  ==============================================================================

  Product thumbnail rendering script

  ==============================================================================

--%>
<%@ include file="/libs/foundation/global.jsp" %>
<%@ page contentType="text/html; charset=utf-8" import="
		com.adobe.cq.commerce.api.Product"
        %><%
    Product product = resource.adaptTo(Product.class);
    if (product == null) {
        return;
    }
    String selectorString = slingRequest.getRequestPathInfo().getSelectorString();
    // remove the "thumbnail" selector from the selector string (e.g.: "thumbnail.150" or "thumbnail")
    selectorString = selectorString.replace("thumbnail.", "");
    selectorString = selectorString.replace("thumbnail", "");
    String thumbnailUrl = product.getThumbnailUrl(selectorString);

%><img class="cq-commerce-productthumbnail" src="<%= xssAPI.getValidHref(thumbnailUrl) %>"/>


