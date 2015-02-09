<%@ include file="../global.jsp" %>

<%@ page contentType="text/html; charset=utf-8" import="
		java.util.Iterator,
        com.adobe.cq.commerce.api.CommerceService,
        com.adobe.cq.commerce.api.CommerceSession,
        com.adobe.cq.commerce.api.Product,
        com.adobe.cq.commerce.common.EnumerateAxisFilter"
        %>
<%
    String cartPage = "/content/summit-geometrixx-outdoors/en/cart";

    CommerceService commerceService = resource.adaptTo(CommerceService.class);
    CommerceSession session = commerceService.login(slingRequest, slingResponse);
    Node node = resource.adaptTo(Node.class);
    String productDataPath = node.getProperty("productData").getString();
    Product baseProduct = resourceResolver.resolve(productDataPath).adaptTo(Product.class);
%>
<div class=product>
    <h1><%= baseProduct.getTitle() %></h1>
     
    <div class="product-viewer" itemprop="image">
       <cq:include path="<%= baseProduct.getImageUrl() %>" resourceType="commerce/components/product/image"/>
    </div>
    <div><%= baseProduct.getDescription() %></div>

    <div style="clear:both;">
        <table>
            <tr>
                <%
                Iterator<String> axesIterator = baseProduct.getVariantAxes();
                String firstAxis = axesIterator.next(); %>
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
            Iterator<Product> firstAxisProductIterator = baseProduct.getVariants(new EnumerateAxisFilter(firstAxis));
            while (firstAxisProductIterator.hasNext()) {
                Product firstAxisProduct = firstAxisProductIterator.next();
                %>
                <tr>
                    <td>
                        <%= xssAPI.encodeForHTML(firstAxisProduct.getProperty(firstAxis, String.class)) %>
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
                                    String price = session.getProductPrice(secondAxisProduct);
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