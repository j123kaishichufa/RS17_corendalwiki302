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

import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardWriteBean;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: StandardArticle.java,v 1.10 2007/10/17 21:28:57 tdanard Exp $
 */
public interface StandardArticle extends StandardWriteBean, Article, Searched,
        Secured, Pathed {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardArticle";

    /**
     * Removes this image and all associated subscriptions.
     * 
     * 
     * 
     */
    public void remove(PrimaryKey referenceRequestPk);

    /**
     * Notifies all subscribers that a new version of this article has been
     * issued.
     * 
     * 
     * 
     */
    public void notifySubscribers();

    /**
     * Returns true if this article is the direct parent of another content.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsDirectParentOf(PrimaryKey contentPk);

    /**
     * Returns true if this article is the indirect parent of another content.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsIndirectParentOf(PrimaryKey contentPk);

    /**
     * Returns true if this article is the parent (direct or indirect) of
     * another content.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsParentOf(PrimaryKey contentPk);

    /**
     * Returns true if this article is a direct parent of another content.
     * Recursivity is applied.
     * 
     * @param child
     *            generic content to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getIsRecursiveDirectParentOf(Parented child);

    /**
     * Returns the account being author of this article.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.core.interfaces.StandardAccount
     *         object
     */
    public StandardAccount getAuthorStandardAccount();

    /**
     * Returns the account being editor of this article.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.core.interfaces.StandardAccount
     *         object
     */
    public StandardAccount getEditorStandardAccount();

    /**
     * Returns the group being associate editors of this article.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup
     *         object
     */
    public StandardGroup getAssociateEditorsStandardGroup();

    /**
     * Returns the number of content items in this article.
     * 
     * 
     * 
     * @return an int
     */
    public int getSize();

    /**
     * Returns the siblings of this article.
     * 
     * 
     * 
     */
    public List<StandardArticle> getSiblings();

    /**
     * Returns the first sibling of this article.
     * 
     * 
     * 
     */
    public StandardArticle getFirstSiblingStandardArticle();

    /**
     * Returns the previous sibling of this article.
     * 
     * 
     * 
     */
    public StandardArticle getPreviousSiblingStandardArticle();

    /**
     * Returns the next sibling of this article.
     * 
     * 
     * 
     */
    public StandardArticle getNextSiblingStandardArticle();

    /**
     * Returns the last sibling of this article.
     * 
     * 
     * 
     */
    public StandardArticle getLastSiblingStandardArticle();

    /**
     * Returns the relative location of the page printing this article as online
     * help.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public String getOnlineHelpRelativeLocation();

    /**
     * Returns the default location of the page printing this article as online
     * help.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public String getOnlineHelpDefaultLocation();

    /**
     * Returns the absolute location of the page printing this article as online
     * help.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public String getOnlineHelpAbsoluteLocation();

    /**
     * Returns the latest version of the body of this article.
     */
    public String getBodyHTML(boolean isOnlineHelp, PrimaryKey homeArticlePk);
}

// end StandardArticle
