<%@page session="false"%><%--
  ADOBE CONFIDENTIAL
  __________________

   Copyright 2012 Adobe Systems Incorporated
   All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%>
<%@ include file="/libs/foundation/global.jsp" %>
<%@ page contentType="text/html; charset=utf-8" import="
		org.apache.commons.lang.StringUtils,
		com.adobe.cq.commerce.api.CommerceConstants,
		com.adobe.cq.commerce.api.Product,
		com.adobe.cq.commerce.common.CommerceHelper"
        %><%

    String addToCartUrl = currentPage.getPath() + ".commerce.addcartentry.html";
    String redirect = CommerceHelper.mapPathToCurrentLanguage(currentPage, currentStyle.get("addToCartRedirect", ""));
    String errorRedirect = CommerceHelper.mapPathToCurrentLanguage(currentPage, currentStyle.get("cartErrorRedirect", ""));
    if (StringUtils.isEmpty(redirect) && request.getAttribute(CommerceConstants.REQ_ATTR_CARTPAGE) != null) {
        //
        // Legacy CQ5.6 sites won't have the redirects in the design, and instead rely on their parent
        // page adding request attributes for them (in userinfo.jsp in the Geometrixx Outdoors case).
        redirect = (String) request.getAttribute(CommerceConstants.REQ_ATTR_CARTPAGE);
        errorRedirect = (String) request.getAttribute(CommerceConstants.REQ_ATTR_PRODNOTFOUNDPAGE);
        //
        // Legacy sites might also be dependent on add-to-cart POST handlers defined on the cart component:
        addToCartUrl = (String) request.getAttribute(CommerceConstants.REQ_ATTR_CARTOBJECT) + ".add.html";
    }
    if (StringUtils.isEmpty(redirect) || redirect.equals(".")) {
        redirect = currentPage.getPath();
    }
    if (StringUtils.isEmpty(errorRedirect)) {
        errorRedirect = currentPage.getPath();
    }

    Product baseProduct;
    if (request.getAttribute("cq.commerce.productPageProxy") != null) {
        // Handle product-page proxies.
        //   In this case our resource is the (empty) product component on the product template page, and
        //   the baseProduct is supplied on the cq.commerce.product request attribute.
        %><div class="productPageProxyFlag"></div><%
        baseProduct = (Product) request.getAttribute("cq.commerce.product");
    } else {
        baseProduct = resource.adaptTo(Product.class);
    }

    String productTrackingPath = baseProduct.getProperty("productData", String.class);
    if (productTrackingPath == null) {
        productTrackingPath = baseProduct.getPagePath();
    }

    request.setAttribute("cq.commerce.addToCartUrl", addToCartUrl);
    request.setAttribute("cq.commerce.redirect", redirect);
    request.setAttribute("cq.commerce.errorRedirect", errorRedirect);
    request.setAttribute("cq.commerce.product", baseProduct);
    request.setAttribute("cq.commerce.productPagePath", baseProduct.getPagePath());
    request.setAttribute("cq.commerce.productTrackingPath", productTrackingPath);

%>