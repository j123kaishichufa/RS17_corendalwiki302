/*  
 * Corendal NetApps Framework - Java framework for web applications  
 * Copyright (C) Thierry Danard  
 *  
 * This program is free software; you can redistribute it and|or  
 * modify it under the terms of the GNU General Public License  
 * as published by the Free Software Foundation; either version 2  
 * of the License, or (at your option) any later version.  
 *  
 * This program is distributed in the hope that it will be useful,  
 * but WITHOUT ANY WARRANTY; without even the implied warranty of  
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  
 * GNU General Public License for more details.  
 *  
 * You should have received a copy of the GNU General Public License  
 * along with this program; if not, write to the Free Software  
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.  
 */
package com.corendal.netapps.wiki.writedatafactories;

import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.Image;
import com.corendal.netapps.wiki.writedatabeans.DefaultImage;

/**
 * ImageFactory is the static class building new Image instances.
 * 
 * @version $Id: ImageFactory.java,v 1.6 2007/09/02 21:56:31 tdanard Exp $
 */
public final class ImageFactory {
    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private ImageFactory() {
        // this class is only used for storing constants
    }

    /**
     * Returns an existing image.
     * 
     * @param imagePk
     *            primary key of a image
     * 
     * 
     * @return a Image object
     */
    public static final Image findByPrimaryKey(PrimaryKey imagePk) {
        DefaultImage image = new DefaultImage();
        image.setPrimaryKeyAndLoad(imagePk);
        return image;
    }

    /**
     * Creates a new image.
     * 
     * @param contentInfoPk
     *            primary key of a content info
     * @param authorPk
     *            primary key of an author account
     * @param filePk
     *            primary key of a stored file
     * @param parentContentPk
     *            primary key of a parent article
     * @param contentRequestPk
     *            primary key of a content request
     * @param classificationTypePk
     *            primary key of a classification type
     * @param ruleTypePk
     *            primary key of a rule type
     * @param comment
     *            a comment
     * 
     * 
     * @return a Image object
     */
    public static final Image create(PrimaryKey contentInfoPk,
            PrimaryKey authorPk, PrimaryKey filePk, PrimaryKey parentContentPk,
            PrimaryKey contentRequestPk, PrimaryKey classificationTypePk,
            PrimaryKey ruleTypePk, String comment) {
        DefaultImage image = new DefaultImage();
        image.add(contentInfoPk, authorPk, filePk, parentContentPk,
                contentRequestPk, classificationTypePk, ruleTypePk, comment);

        return image;
    }

}

// end ImageFactory
