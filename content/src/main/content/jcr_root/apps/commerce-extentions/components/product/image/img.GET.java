/*
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2012 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */

package libs.commerce.components.product.image;

import java.io.IOException;

import javax.jcr.RepositoryException;

import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.commons.AbstractImageServlet;
import com.day.cq.wcm.foundation.Image;
import com.day.image.Layer;
import com.day.text.Text;

/**
 * Resizes the product image to a destination size of 146x154 pixels using the original aspect ratio and fills the
 * missing pixels with white.
 */
public class img_GET extends AbstractImageServlet {

    @Override
    protected Layer createLayer(ImageContext c) throws RepositoryException, IOException {
        Image image = new Image(c.resource);
        if (!image.hasContent()) {
            return null;
        }
        // get style and set constraints
        Style style = getStyle(c);
        if (style!=null) {
            image.loadStyleData(style);
        }
        return image.getLayer(false, true, false);
    }

    private Style getStyle(ImageContext c) {
        String designId = "";
        String cellPath = "";
        String suffix = c.request.getRequestPathInfo().getSuffix();
        if (suffix != null && suffix.length() > 0) {
            if (!suffix.startsWith("/")) {
                suffix = "/" + suffix;
            }
            int idx = suffix.indexOf("/-/");
            if (idx >= 0) {
                designId = suffix.substring(0, idx);
                cellPath = suffix.substring(idx + 3, suffix.lastIndexOf("/cq_ck_"));
            } else {
                designId = suffix.substring(0, suffix.lastIndexOf("/cq_ck_"));
            }
        }

        // get design
        Design design = null;
        if (designId.length() > 0) {
            Designer d = c.resolver.adaptTo(Designer.class);
            design = d.getDesign(designId);
        }
        if (design == null) {
            return null;
        }
        if (cellPath.length() > 0) {
            return design.getStyle(cellPath);
        } else {
            return design.getStyle(Text.getName(c.resource.getPath()));
        }
    }


}