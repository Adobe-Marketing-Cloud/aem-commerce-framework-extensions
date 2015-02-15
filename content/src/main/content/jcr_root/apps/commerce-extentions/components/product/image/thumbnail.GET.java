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

import java.awt.*;
import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;

import com.day.cq.commons.ImageHelper;
import com.day.cq.commons.ImageResource;
import com.day.cq.wcm.commons.AbstractImageServlet;
import com.day.cq.wcm.foundation.Image;
import com.day.image.Layer;

/**
 * Renders an image
 */
public class thumbnail_GET extends AbstractImageServlet {

    /**
     * destination width
     */
    private final static int D_WIDTH = 60;

    @Override
    protected Layer createLayer(ImageContext c)
            throws RepositoryException, IOException {
        // don't create the layer yet. handle everything later
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * Override default ImageResource creation to support assets
     */
    @Override
    protected ImageResource createImageResource(Resource resource) {
        return new Image(resource);
    }

    @Override
    protected void writeLayer(SlingHttpServletRequest req,
                              SlingHttpServletResponse resp,
                              ImageContext c, Layer layer)
            throws IOException, RepositoryException {

        Image image = new Image(c.resource);
        if (!image.hasContent()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // get style and set constraints
        image.loadStyleData(c.style);

        // get pure layer
        layer = image.getLayer(false, false, false);

        image.rotate(layer);
        image.crop(layer);

        String[] selectors = req.getRequestPathInfo().getSelectors();
        int width;
        try {
            width = Integer.parseInt(selectors[1]);
        } catch (Exception e) {
            width = D_WIDTH;
        }
        Layer resizedLayer = ImageHelper.resize(layer, new Dimension(width, 0), null, null);
        if (resizedLayer != null) {
            layer = resizedLayer;
        }
        String mimeType = image.getMimeType();
        if (ImageHelper.getExtensionFromType(mimeType) == null) {
            // get default mime type
            mimeType = "image/png";
        }

        if (selectors.length >= 3 && selectors[2].equals("transparent")) {
            mimeType = "image/gif";
            layer.setTransparency(Color.WHITE);
        }

        resp.setContentType(mimeType);
        layer.write(mimeType, mimeType.equals("image/gif") ? 255 : 1.0, resp.getOutputStream());

        resp.flushBuffer();
    }
}