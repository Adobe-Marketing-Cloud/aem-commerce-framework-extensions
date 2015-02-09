<%@ include file="../global.jsp" %><%
%><%@page session="false" import="
    com.adobe.cq.commerce.api.CommerceService,
    com.adobe.cq.commerce.api.CommerceSession,
    com.adobe.cq.commerce.api.Product,
    org.apache.sling.api.resource.ResourceResolver" %><%

    ResourceResolver resolver = resource.getResourceResolver();

    String cartPage = "/content/summit-geometrixx-outdoors/en/cart";

    String productPath = request.getParameter("product");

    Resource productResource = resolver.getResource(productPath);
    Product product = productResource.adaptTo(Product.class);

    CommerceService commerceService = productResource.adaptTo(CommerceService.class);
    CommerceSession session = commerceService.login(slingRequest, slingResponse);

    session.addCartEntry(product, 1);

    response.sendRedirect(cartPage + ".html");
%>