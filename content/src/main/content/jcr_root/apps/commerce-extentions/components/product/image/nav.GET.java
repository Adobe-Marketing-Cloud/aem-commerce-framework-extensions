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
import com.day.cq.wcm.commons.AbstractImageServlet;
import com.day.cq.wcm.foundation.Image;
import com.day.image.Layer;

/**
 * Resizes the product image to a destination size of 146x154 pixels using the original aspect ratio and fills the
 * missing pixels with white.
 */
public class nav_GET extends AbstractImageServlet {

    /**
     * destination width
     */
    private final static int D_WIDTH = 160;

    /**
     * destination height
     */
    private final static int D_HEIGHT = 120;

    @Override
    protected Layer createLayer(ImageContext c) throws RepositoryException, IOException {
        Image image = new Image(c.resource);
        if (!image.hasContent()) {
            return null;
        }
        // get style and set constraints
        image.loadStyleData(c.style);

        // get pure layer
        Layer layer = image.getLayer(false, false, false);

        image.rotate(layer);
        image.crop(layer);
        int w = layer.getWidth();
        int h = layer.getHeight();
        int tw = (w * Math.min(h, D_HEIGHT)) / h;
        int th = (h * Math.min(w, D_WIDTH)) / w;
        Dimension newSize;
        if (tw > D_WIDTH) {
            newSize = new Dimension(D_WIDTH, th);
        } else if (th > D_HEIGHT) {
            newSize = new Dimension(tw, D_HEIGHT);
        } else {
            // choose best fit
            int dw = D_WIDTH - tw;
            int dh = D_HEIGHT - th;
            if (dw < dh) {
                newSize = new Dimension(tw, D_HEIGHT);
            } else {
                newSize = new Dimension(D_WIDTH, th);
            }
        }
        layer.resize(newSize.width, newSize.height);

        // Center scaled layer in a 146x154 layer
        if (newSize.width < D_WIDTH) {
            layer.setX(((D_WIDTH - newSize.width) / 2));
        } else if (newSize.height < D_HEIGHT) {
            layer.setY(((D_HEIGHT - newSize.height) / 2));
        }
        Layer bg = new Layer(D_WIDTH, D_HEIGHT, Color.white);
        bg.merge(layer);
        return bg;
    }

}