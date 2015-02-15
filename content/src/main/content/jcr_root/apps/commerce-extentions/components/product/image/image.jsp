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

--%><%@ page import="
        com.day.cq.commons.Doctype,
        com.day.cq.wcm.api.components.DropTarget,
        com.day.cq.wcm.foundation.Image" %><%
%><%@include file="/libs/foundation/global.jsp"%><%

    final String PLACEHOLDER = "placeholder";

    Image image = new Image(resource);

    // drop target css class = dd prefix + name of the drop target in the edit config
    image.addCssClass(DropTarget.CSS_CLASS_PREFIX + "image");
    image.loadStyleData(currentStyle);
    image.setSelector(".img"); // use image script
    image.setDoctype(Doctype.fromRequest(request));

    // current suffix, if any, will be a cache-killer (the last-mod date)
    // we need to prefix it with the design and cellpath
    String cacheKiller = image.getSuffix();
    if (cacheKiller == null) {
        cacheKiller = "";
    } else if (cacheKiller.length() > 0) {
        cacheKiller = "/cq_ck_" + cacheKiller.substring(1);
    }
    String cellPath = (currentStyle != null)? currentStyle.getCell().getPath() : PLACEHOLDER;
    cellPath = cellPath.replace("/reference/product/image", "/product/image");
    String suffixId = (currentDesign != null)? currentDesign.getId() : PLACEHOLDER;
    image.setSuffix(suffixId + "/-/" + cellPath + cacheKiller);

    String divId = "cq-image-jsp-" + resource.getPath();
%><div id="<%= xssAPI.encodeForHTMLAttr(divId) %>"><% image.draw(out); %></div><%
%><cq:text property="jcr:description" placeholder="" tagName="small" escapeXml="true"/>

<%@include file="/libs/foundation/components/image/tracking-js.jsp"%>
