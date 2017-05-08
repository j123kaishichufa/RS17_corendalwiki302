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
import com.corendal.netapps.wiki.interfaces.Content;
import com.corendal.netapps.wiki.writedatabeans.DefaultContent;

/**
 * ContentFactory is the static class building new Content instances.
 * 
 * @version $Id: ContentFactory.java,v 1.6 2007/09/02 21:56:31 tdanard Exp $
 */
public final class ContentFactory {
    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private ContentFactory() {
        // this class is only used for storing constants
    }

    /**
     * Returns an existing content.
     * 
     * @param contentPk
     *            primary key of a content
     * 
     * 
     * @return a Content object
     */
    public static final Content findByPrimaryKey(PrimaryKey contentPk) {
        DefaultContent content = new DefaultContent();
        content.setPrimaryKeyAndLoad(contentPk);
        return content;
    }

}

// end ContentFactory
