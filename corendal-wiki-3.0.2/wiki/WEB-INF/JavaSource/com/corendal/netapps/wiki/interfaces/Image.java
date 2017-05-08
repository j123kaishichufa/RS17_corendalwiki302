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
 * @version $Id: Image.java,v 1.7 2007/10/17 21:28:57 tdanard Exp $
 */
public interface Image extends WriteDataBean, Previewed, Enabled, Versioned,
        Classified {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.Image";

    /**
     * Stores the parent content of this image.
     */
    public void storeParentContent(PrimaryKey parentContentPk);

    /**
     * Returns the primary key of the file of this image.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getFilePrimaryKey();

    /**
     * Stores the file of this image.
     */
    public void storeFilePrimaryKey(PrimaryKey filePk);

    /**
     * Makes this image obsolete by creating a new version.
     * 
     * @param contentRequestPk
     *            primary key of a content request
     */
    public void supersede(PrimaryKey contentRequestPk);

    /**
     * Stores the info of this image.
     */
    public void storeInfo(PrimaryKey infoPk);

    /**
     * Removes this image.
     */
    public void remove(PrimaryKey referenceRequestPk);

    /**
     * Stores the classification type of this image.
     */
    public void storeClassificationType(PrimaryKey classificationTypePk);

    /**
     * Stores the rule type of this image.
     */
    public void storeRuleType(PrimaryKey ruleTypePk);

    /**
     * Adds an allowed access group to this image.
     */
    public void addAllowedAccessGroup(PrimaryKey groupPk);

    /**
     * Adds a denied access group to this image.
     */
    public void addDeniedAccessGroup(PrimaryKey groupPk);

    /**
     * Removes an access group from this image.
     */
    public void removeAccessGroup(PrimaryKey groupPk);

    /**
     * Returns the comment of this image.
     * 
     * @return a java.lang.String object
     */
    public String getComment();

    /**
     * Stores the comment of this image.
     */
    public void storeComment(String comment);

    /**
     * Stores the author of this image.
     */
    public void storeAuthor(PrimaryKey authorPk);

    /**
     * Returns the primary key of the last request of this image.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getLastRequestPrimaryKey();

    /**
     * Returns the primary key of the first request of this image.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getFirstRequestPrimaryKey();

    /**
     * Stores the friendly address of this image.
     */
    public void storeFriendlyAddress(String friendlyAddress);

    /**
     * Returns the friendly address of this image.
     * 
     * @return a java.lang.String object
     */
    public String getFriendlyAddress();

    /**
     * Sets the friendly address and loads.
     * 
     * @param friendlyAddress
     *            friendly address to use
     */
    public void setFriendlyAddressAndLoad(String friendlyAddress);

    /**
     * Loads this image based upon a friendly address.
     */
    public void loadByFriendlyAddress();

    /**
     * Returns the primary of the current version of this image.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getVersionPrimaryKey();

    /**
     * Returns the primary of the author account of this image.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getAuthorAccountPrimaryKey();

    /**
     * Returns the primary of the main parent of this image.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getMainParentPrimaryKey();

}

// end Image
