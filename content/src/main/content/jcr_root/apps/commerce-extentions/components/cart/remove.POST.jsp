<%@ include file="../global.jsp" %><%
%><%@page session="false" import="
    com.adobe.cq.commerce.api.CommerceService,
    com.adobe.cq.commerce.api.CommerceSession" %><%

    String cartPage ="/content/geometrixx-outdoors/en/cart";

    CommerceService commerceService = resource.adaptTo(CommerceService.class);
    CommerceSession session = commerceService.login(slingRequest, slingResponse);

    int entry = Integer.parseInt(request.getParameter("entryIndex"));

    session.deleteCartEntry(entry);

    response.sendRedirect(cartPage + ".html");

%>