<%@page session="false"%><%--
  ADOBE CONFIDENTIAL
  __________________

   Copyright 2013 Adobe Systems Incorporated
   All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%><%
%><%@ include file="/libs/foundation/global.jsp" %><%
%><%@ page contentType="text/html; charset=utf-8" import="
		org.apache.commons.lang.StringUtils,
		com.adobe.cq.commerce.api.Product,
		com.adobe.cq.commerce.api.CommerceSession,
		com.adobe.cq.commerce.api.CommerceService,
		com.day.cq.i18n.I18n"
%><%
    final I18n i18n = new I18n(slingRequest);

    CommerceService commerceService = resource.adaptTo(CommerceService.class);
    CommerceSession session = commerceService.login(slingRequest, slingResponse);

    Product product = (Product) request.getAttribute("cq.commerce.product");
    String productId = product.getSKU();
%>
<p class="product-price" itemprop="price"><%= xssAPI.encodeForHTML(session.getProductPrice(product)) %></p>
<% if (StringUtils.isNotEmpty(productId)) { %>
<p class="product-item" itemprop='productID'><%= i18n.get("Item #{0}", null, xssAPI.encodeForHTML(productId)) %></p>
<% } %>
