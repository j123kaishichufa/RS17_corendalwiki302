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
import com.corendal.netapps.wiki.interfaces.ContentInfo;
import com.corendal.netapps.wiki.writedatabeans.DefaultContentInfo;

/**
 * ContentInfoFactory is the static class building new ContentInfo instances.
 * 
 * @version $Id: ContentInfoFactory.java,v 1.6 2007/09/02 21:56:31 tdanard Exp $
 */
public final class ContentInfoFactory {
    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private ContentInfoFactory() {
        // this class is only used for storing constants
    }

    /**
     * Returns an existing content info.
     * 
     * @param contentInfoPk
     *            primary key of a content info
     * 
     * 
     * @return a ContentInfo object
     */
    public static final ContentInfo findByPrimaryKey(PrimaryKey contentInfoPk) {
        DefaultContentInfo contentInfo = new DefaultContentInfo();
        contentInfo.setPrimaryKeyAndLoad(contentInfoPk);
        return contentInfo;
    }

    /**
     * Creates a new content info.
     * 
     * @param name
     *            name to set
     * @param description
     *            description to set
     * 
     * 
     * @return a ContentInfo object
     */
    public static final ContentInfo create(String name, String description) {
        DefaultContentInfo contentInfo = new DefaultContentInfo();
        contentInfo.add(name, description);

        return contentInfo;
    }

}

// end ContentInfoFactory
