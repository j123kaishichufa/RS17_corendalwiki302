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

import java.util.List;

import com.corendal.netapps.framework.core.interfaces.Enabled;
import com.corendal.netapps.framework.core.interfaces.WriteDataBean;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: Article.java,v 1.9 2007/10/17 21:28:58 tdanard Exp $
 */
public interface Article extends WriteDataBean, Previewed, Enabled, Versioned,
        Parented, Classified {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.Article";

    /**
     * Stores the parent content of this article.
     */
    public void storeParentContent(PrimaryKey parentContentPk);

    /**
     * Makes this article obsolete by creating a new version.
     * 
     * @param contentRequestPk
     *            primary key of a content request
     */
    public void supersede(PrimaryKey contentRequestPk);

    /**
     * Stores the info of this article.
     */
    public void storeInfo(PrimaryKey infoPk);

    /**
     * Removes this article.
     */
    public void remove(PrimaryKey referenceRequestPk);

    /**
     * Stores the classification type of this article.
     */
    public void storeClassificationType(PrimaryKey classificationTypePk);

    /**
     * Stores the rule type of this article.
     */
    public void storeRuleType(PrimaryKey ruleTypePk);

    /**
     * Adds an allowed access group to this article.
     */
    public void addAllowedAccessGroup(PrimaryKey groupPk);

    /**
     * Adds a denied group to this article.
     */
    public void addDeniedAccessGroup(PrimaryKey groupPk);

    /**
     * Removes an access group from this article.
     */
    public void removeAccessGroup(PrimaryKey groupPk);

    /**
     * Stores the what's new flag of this article.
     */
    public void storeWhatsNewFlag(String whatsNewFlag);

    /**
     * Returns the comment of this article.
     * 
     * @return a java.lang.String object
     */
    public String getComment();

    /**
     * Stores the comment of this article.
     */
    public void storeComment(String comment);

    /**
     * Stores the bodies of this article.
     */
    public void storeBodies(String rawBodyHTML, String bodyText, String bodyHTML);

    /**
     * Returns the primary key of the editor of this article.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getEditorAccountPrimaryKey();

    /**
     * Returns the primary key of the associate editors of this article.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getAssociateEditorsGroupPrimaryKey();

    /**
     * Returns the primary key of the last request of this article.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getLastRequestPrimaryKey();

    /**
     * Returns the primary key of the first request of this article.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getFirstRequestPrimaryKey();

    /**
     * Returns the body of this article in the text format.
     * 
     * @return a java.lang.String object
     */
    public String getBodyText();

    /**
     * Returns the body of this article in the HTML format. This method returns
     * the cached version, with links highlighted.
     * 
     * @return a java.lang.String object
     */
    public String getBodyHTML();

    /**
     * Returns the body of this article in the HTML format. This method returns
     * the actual version, as entered by the users.
     * 
     * @return a java.lang.String object
     */
    public String getRawBodyHTML();

    /**
     * Stores the author of this article.
     */
    public void storeAuthor(PrimaryKey authorPk);

    /**
     * Returns the friendly address of this article.
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
     * Stores the friendly address of this article.
     */
    public void storeFriendlyAddress(String friendlyAddress);

    /**
     * Stores the editor account of this article.
     */
    public void storeEditor(PrimaryKey editorPk);

    /**
     * Stores the associate editors group of this article.
     */
    public void storeAssociateEditors(PrimaryKey associateEditorsPk);

    /**
     * Loads this article based upon a friendly address.
     */
    public void loadByFriendlyAddress();

    /**
     * Returns the primary key of the current version of this article.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getVersionPrimaryKey();

    /**
     * Returns the primary key of the author account of this article.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getAuthorAccountPrimaryKey();

    /**
     * Stores the addresses of this article.
     */
    public void storeAddresses(List<String> addresses);
}

// end Article
