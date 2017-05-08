/*  
 * Corendal NetApps Wiki - Web application for content management  
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
package com.corendal.netapps.wiki.utils;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.StandardContentInfo;
import com.corendal.netapps.wiki.interfaces.StandardContentInfoFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentInfoFactory;

/**
 * ContentInfoUtil is the class for all content info utilities.
 * 
 * @version $Id: ContentInfoUtil.java,v 1.9 2007/09/03 19:42:01 tdanard Exp $
 */
public final class ContentInfoUtil {
    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static methods should be used explicitly.
     */
    private ContentInfoUtil() {
        // this class contains only static methods
    }

    /**
     * Returns the name of a content or content request in the text format.
     * 
     * @param contentInfoPk
     *            primary key of the content info to use
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getNameText(PrimaryKey contentInfoPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentInfoFactory srif = (StandardContentInfoFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentInfoFactory.class);

        /*
         * find the associated content info and return the result
         */
        if (contentInfoPk != null) {
            StandardContentInfo sri = (StandardContentInfo) srif
                    .findByPrimaryKey(contentInfoPk);

            if (sri.getIsFound()) {
                return sri.getName();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the name of a content or content request in the Encoded format.
     * 
     * @param contentInfoPk
     *            primary key of the content info to use
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getNameEncoded(PrimaryKey contentInfoPk) {
        String nameText = ContentInfoUtil.getNameText(contentInfoPk);

        if (nameText != null) {
            return TextFormatUtil.getTextToEncoded(nameText);
        } else {
            return null;
        }
    }

    /**
     * Returns the name of a content or content request in the HTML format.
     * 
     * @param contentInfoPk
     *            primary key of the content info to use
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getNameHTML(PrimaryKey contentInfoPk) {
        String nameText = ContentInfoUtil.getNameText(contentInfoPk);

        if (nameText != null) {
            return TextFormatUtil.getTextToHTML(nameText);
        } else {
            return null;
        }
    }

    /**
     * Returns the description of a content or content request in the text
     * format.
     * 
     * @param contentInfoPk
     *            primary key of the content info to use
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getDescriptionText(PrimaryKey contentInfoPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentInfoFactory srif = (StandardContentInfoFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentInfoFactory.class);

        /*
         * find the associated content info and return the result
         */
        if (contentInfoPk != null) {
            StandardContentInfo sri = (StandardContentInfo) srif
                    .findByPrimaryKey(contentInfoPk);

            if (sri.getIsFound()) {
                return sri.getDescription();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the description of a content or content request in the Encoded
     * format.
     * 
     * @param contentInfoPk
     *            primary key of the content info to use
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getDescriptionEncoded(PrimaryKey contentInfoPk) {
        String descriptionText = ContentInfoUtil
                .getDescriptionText(contentInfoPk);

        if (descriptionText != null) {
            return TextFormatUtil.getTextToEncoded(descriptionText);
        } else {
            return null;
        }
    }

    /**
     * Returns the description of a content or content request in the HTML
     * format.
     * 
     * @param contentInfoPk
     *            primary key of the content info to use
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getDescriptionHTML(PrimaryKey contentInfoPk) {
        String descriptionText = ContentInfoUtil
                .getDescriptionText(contentInfoPk);

        if (descriptionText != null) {
            return TextFormatUtil.getTextToHTML(descriptionText);
        } else {
            return null;
        }
    }
}
