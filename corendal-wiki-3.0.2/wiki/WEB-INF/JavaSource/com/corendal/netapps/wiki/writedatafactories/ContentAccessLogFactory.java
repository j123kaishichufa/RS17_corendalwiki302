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
import com.corendal.netapps.wiki.interfaces.ContentAccessLog;
import com.corendal.netapps.wiki.writedatabeans.DefaultContentAccessLog;

/**
 * ContentAccessLogFactory is the static class building new content access log
 * instances.
 * 
 * @version $Id: ContentAccessLogFactory.java,v 1.1 2005/09/06 21:25:36 tdanard
 *          Exp $
 */
public final class ContentAccessLogFactory {
    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private ContentAccessLogFactory() {
        // this class is only used for storing constants
    }

    /**
     * Returns an existing content access log.
     * 
     * @param contentAccessLogPk
     *            primary key of a content access log
     * 
     * 
     * @return a ContentAccessLog object
     */
    public static final ContentAccessLog findByPrimaryKey(
            PrimaryKey contentAccessLogPk) {
        DefaultContentAccessLog contentAccessLog = new DefaultContentAccessLog();
        contentAccessLog.setPrimaryKeyAndLoad(contentAccessLogPk);
        return contentAccessLog;
    }

    /**
     * Creates a new content access log.
     * 
     * @param contentPk
     *            primary key of the content
     * 
     * 
     * @return a ContentAccessLog object
     */
    public static ContentAccessLog create(PrimaryKey contentPk) {
        DefaultContentAccessLog contentAccessLog = new DefaultContentAccessLog();
        contentAccessLog.add(contentPk);

        return contentAccessLog;
    }

}
