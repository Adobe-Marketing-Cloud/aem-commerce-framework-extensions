<%@ include file="../global.jsp" %>

<%@ page contentType="text/html; charset=utf-8" import="
		java.util.Iterator,
        com.adobe.cq.commerce.api.CommerceService,
        com.adobe.cq.commerce.api.CommerceSession,
        com.adobe.cq.commerce.api.Product,
        com.adobe.cq.commerce.common.EnumerateAxisFilter"
        %>
<%
    String cartPage = "/content/geometrixx-outdoors/en/cart";

    CommerceService commerceService = null; // Task 1
    CommerceSession session = null; // Task 2
    Node node = resource.adaptTo(Node.class);
    String productDataPath = null; // Task 3
    Product baseProduct = null; // Task 3
%>
<div class=product>
    <h1><%= /* Task 4 Title*/ %></h1>

    <div class="product-viewer" itemprop="image">
       <cq:include path="<%= baseProduct.getImageUrl() %>" resourceType="commerce/components/product/image"/>
    </div>
    <div><%= /* Task 4 Description*/ %></div>

    <div style="clear:both;">
        <table>
            <tr>
                <%
                Iterator<String> axesIterator = baseProduct.getVariantAxes();
                String firstAxis = null; // Task 5 %>
                <th width="120"><%= firstAxis%></th>
                <%
                    String secondAxis = null;
                    if (axesIterator.hasNext()) {
                    secondAxis = axesIterator.next();
                        %>
                        <th><%= secondAxis%></th>
                        <%
                    }
                 %>
            </tr>
            <%
            while (/* Task 6 Iterate over the first axis products */){
                Product firstAxisProduct = firstAxisProductIterator.next();
                %>
                <tr>
                    <td>
                        <%= /* Task 7 the first axis value*/ %>
                    </td>
                    <% if (secondAxis == null) {%>
                        <td>
                            <form style="padding:0; margin:0;" method="POST" action="<%=xssAPI.getValidHref(cartPage + "/_jcr_content/par/cart.push.html")%>">
                                <input type="hidden" name="product" value="<%= xssAPI.encodeForHTMLAttr(firstAxisProduct.getPath())%>"/>
                                <input type="submit" value="Add to cart"/>
                            </form>
                        </td>
                    <% } %>
                    <% if (secondAxis != null)  {%>
                        <td>
                            <%
                                Iterator<Product> secondAxisProductIterator = firstAxisProduct.getVariants(new EnumerateAxisFilter(secondAxis));
                                while (secondAxisProductIterator.hasNext()) {
                                    Product secondAxisProduct = secondAxisProductIterator.next();
                                    String price = null; // Task 8
                                %>
                                        <form style="padding:0 5px; margin:0; float:left;" method="POST" action="<%=xssAPI.getValidHref(cartPage + "/_jcr_content/par/cart.push.html")%>">
                                            <input type="hidden" name="product" value="<%= secondAxisProduct.getPath()%>"/>
                                            <input type="submit" title="Add to cart" style="width: 120px;" value="<%= xssAPI.encodeForHTMLAttr(secondAxisProduct.getProperty(secondAxis, String.class)) + " ("+price+")" %>"/>
                                        </form>
                                <%}%>
                        </td>
                    <% } %>
                </tr>
            <%}%>
        </table>
    </div>
</div>