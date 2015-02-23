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
    com.adobe.cq.commerce.api.Product"
%><%
    Product product = (Product) request.getAttribute("cq.commerce.product");
%>
<header>
    <p class="product-brand" itemprop="brand"><%= xssAPI.encodeForHTML(product.getProperty("brand", String.class)) %></p>
    <h1 class="product-title" itemprop="name"><%= xssAPI.encodeForHTML(product.getTitle()) %></h1>
    <h2 class="product-description"><%= xssAPI.encodeForHTML(product.getDescription()) %></h2>
    <%= product.getProperty("jcr:summit", String.class) %>
</header>
