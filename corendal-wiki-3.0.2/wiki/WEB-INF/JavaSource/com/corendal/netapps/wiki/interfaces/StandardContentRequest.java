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
package com.corendal.netapps.wiki.interfaces;

import com.corendal.netapps.framework.core.interfaces.Described;
import com.corendal.netapps.framework.core.interfaces.DescribedLong;
import com.corendal.netapps.framework.core.interfaces.DescribedShort;
import com.corendal.netapps.framework.core.interfaces.Located;
import com.corendal.netapps.framework.core.interfaces.Matched;
import com.corendal.netapps.framework.core.interfaces.Named;
import com.corendal.netapps.framework.core.interfaces.StandardWriteBean;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: StandardContentRequest.java,v 1.1 2005/09/06 21:25:29 tdanard
 *          Exp $
 */
public interface StandardContentRequest extends StandardWriteBean,
        ContentRequest, Located, Edited, Authored, Approved, Requested, Named,
        Described, DescribedShort, DescribedLong, Matched, Secured {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardContentRequest";

    /**
     * Approves this image request.
     * 
     * 
     * 
     * @return a StandardImage object
     */
    public StandardImage approveImageRequest();

    /**
     * Approves this article request.
     * 
     * 
     * 
     * @return a StandardArticle object
     */
    public StandardArticle approveArticleRequest();

    /**
     * Returns the primary key of the preview icon of this content request.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getPreviewIconPrimaryKey();

    /**
     * Stored the info of this content request.
     * 
     * @param name
     *            a String representing a name
     * @param description
     *            a String representing a description
     * 
     * 
     */
    public void storeInfo(String name, String description);
}

// end StandardContentRequest
