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

import com.corendal.netapps.framework.core.interfaces.Enabled;
import com.corendal.netapps.framework.core.interfaces.WriteDataBean;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: Content.java,v 1.8 2007/10/17 21:28:58 tdanard Exp $
 */
public interface Content extends WriteDataBean, Previewed, Enabled, Classified {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.Content";

    /**
     * Returns the version number of this content.
     */
    public int getVersionNum();

    /**
     * Returns the primary key of the info of this content.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getInfoPrimaryKey();

    /**
     * Returns the primary key of the last request of this content.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getLastRequestPrimaryKey();

    /**
     * Returns the primary key of the first request of this content.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getFirstRequestPrimaryKey();

    /**
     * Returns the primary key of the type of this content.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getTypePrimaryKey();

    /**
     * Stores the order of this content in a given parent article.
     */
    public void storeContentOrder(PrimaryKey parentArticlePk, int order);

    /**
     * Stores a reference to this content.
     * 
     * @param parentArticlePk
     *            primary key of a parent article
     * @param referenceRequestPk
     *            primary key of a reference request
     */
    public void storeReference(PrimaryKey parentArticlePk,
            PrimaryKey referenceRequestPk);

    /**
     * Removes a reference to this content.
     * 
     * @param parentArticlePk
     *            primary key of a parent article
     * @param referenceRequestPk
     *            primary key of a reference request
     */
    public void removeReference(PrimaryKey parentArticlePk,
            PrimaryKey referenceRequestPk);

    /**
     * Returns the primary key of the main parent of this content.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getMainParentPrimaryKey();
}

// end Content
