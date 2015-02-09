<%@ include file="../global.jsp" %><%
%><%@page session="false" import="
    com.adobe.cq.commerce.api.CommerceService,
    com.adobe.cq.commerce.api.CommerceSession" %><%

    String cartPage ="/content/geometrixx-outdoors/en/cart";
    try{

        CommerceService commerceService = resource.adaptTo(CommerceService.class);
        CommerceSession session = commerceService.login(slingRequest, slingResponse);

        int entry = Integer.parseInt(request.getParameter("entryIndex"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        session.modifyCartEntry(entry, quantity);
    }
    finally {
        response.sendRedirect(cartPage + ".html");
    }

%>