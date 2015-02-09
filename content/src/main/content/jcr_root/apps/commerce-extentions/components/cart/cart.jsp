<%@include file="../global.jsp"%>
<%@page session="false" import="
		com.adobe.cq.commerce.api.CommerceService,
		com.adobe.cq.commerce.api.CommerceSession,
		com.adobe.cq.commerce.common.PriceFilter"%>
<table width="100%" class="entries">
    <tbody>
        <tr>
            <th width="40">&nbsp;</th>
            <th align="left">Product</th>
            <th align="center" width="70">Size</th>
            <th align="center" width="120">Quantity</th>
            <th align="right">Price p. Unit</th>
            <th align="right">Price</th>
        </tr>
        <%
            CommerceService commerceService = resource.adaptTo(CommerceService.class);
            CommerceSession session = commerceService.login(slingRequest, slingResponse);

            for (CommerceSession.CartEntry entry : session.getCartEntries()) {
                %>
                    <tr style="padding: 3px; vertical-align: middle; border-bottom: 1px solid #cccccc;">
                        <td>
                            <form style="margin:0; padding:0;" method="POST" action="<%= xssAPI.getValidHref(resource.getPath() + ".remove.html") %>">
                                <input type="hidden" name="entryIndex" value="<%= entry.getEntryIndex()%>"/>
                                <input type="submit" value="X" title="Remove"/>
                            </form>
                        </td>
                        <td><%=entry.getProduct().getTitle() + " (" + entry.getProduct().getDescription() + ")"%></td>
                        <td align="center"><%=entry.getProduct().getProperty("size", String.class)%></td>
                        <td>
                            <form style="margin:0; padding:0;" method="POST" action="<%= xssAPI.getValidHref(resource.getPath() + ".modify.html") %>">
                                <input type="hidden" name="entryIndex" value="<%= entry.getEntryIndex()%>"/>
                                <input name="quantity" type="Text" size="1" style="margin:0;padding:0;" value="<%= entry.getQuantity()%>"/>
                                <input style="margin: 2px;" type="submit" value="Update"/>
                            </form>
                        </td>
                        <td align="right"><%=entry.getPrice(new PriceFilter("UNIT"))%></td>
                        <td align="right"><%=entry.getPrice(new PriceFilter("PRE_TAX"))%></td>
                    </tr>
                <%
            }
        %>
        <tr>
            <td>&nbsp;</td>
            <td><b>Tax</b></td>
            <td colspan="4" align="right"><%= session.getCartPrice(new PriceFilter("ORDER","TAX"))%></td>
        </tr>
        <tr style="border-bottom: 3px solid #cccccc; border-top: 1px solid #cccccc; padding: 2px 0; font-weight: bold;">
            <td>&nbsp;</td>
            <td>Total</td>
            <td colspan="4" align="right"><%= session.getCartPrice(new PriceFilter("ORDER", "TOTAL"))%></td>
        </tr>
    </tbody>
</table>