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

import com.corendal.netapps.framework.core.interfaces.Manager;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: ContentManager.java,v 1.11 2008/11/19 17:12:04 tdanard Exp $
 */
public interface ContentManager extends Manager {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentManager";

    /**
     * Returns true if an account can approve a request.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsRequestApprovalAuthorized(PrimaryKey accountPk,
            PrimaryKey requestPk);

    /**
     * Returns true if an account can approve a request.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsRequestCancellationAuthorized(PrimaryKey accountPk,
            PrimaryKey requestPk);

    /**
     * Returns true if an account can edit a request.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsRequestEditingAuthorized(PrimaryKey accountPk,
            PrimaryKey requestPk);

    /**
     * Returns true if an account can edit an article without prior approval.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsRequestRequirementWaived(PrimaryKey accountPk,
            PrimaryKey articlePk);

    /**
     * Returns true if an account can submit a request.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsRequestAuthorized(PrimaryKey accountPk,
            PrimaryKey contentPk);

    /**
     * Returns true if an account is an editor or associate editor of an
     * article. Recursivity is applied.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsRecursiveProxyOrAssociateEditor(PrimaryKey accountPk,
            PrimaryKey articlePk);

    /**
     * Returns true if an account is the subscriber of a content or the proxy
     * read of a subscriber.
     * 
     * @param accountPk
     *            primary key of the account to query
     * @param contentPk
     *            primary key of the content to query
     * 
     * 
     * @return a boolean
     */
    public boolean getIsDirectTotalProxySubscriber(PrimaryKey accountPk,
            PrimaryKey contentPk);

    /**
     * Returns true if an account is the member of the denied access groups of a
     * content or is the total proxy of a member.
     * 
     * @param accountPk
     *            primary key of the account to query
     * @param contentPk
     *            primary key of the content to query
     * 
     * 
     * @return a boolean
     */
    public boolean getIsDeniedTotalProxyAccessGroupMember(PrimaryKey accountPk,
            PrimaryKey contentPk);

    /**
     * Returns true if a content has no allowed access group while normally
     * requiring one.
     * 
     * @param content
     *            content being checked
     * 
     * 
     * @return a boolean
     */
    public boolean getIsAccessGroupMissing(Searched content);

    /**
     * Returns true if an account is the member of the allowed access groups of
     * a content or is the total proxy of a member.
     * 
     * @param accountPk
     *            primary key of the account to query
     * @param contentPk
     *            primary key of the content to query
     * 
     * 
     * @return a boolean
     */
    public boolean getIsAllowedTotalProxyAccessGroupMember(
            PrimaryKey accountPk, PrimaryKey contentPk);

    /**
     * Returns true if an account is the member of the access groups of a
     * content or is the total proxy of a member.
     * 
     * @param accountPk
     *            primary key of the account to query
     * @param contentPk
     *            primary key of the content to query
     * 
     * 
     * @return a boolean
     */
    public boolean getIsTotalProxyAccessGroupMember(PrimaryKey accountPk,
            PrimaryKey contentPk);

    /**
     * Returns true if a content is subscribed by an individual.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param individualPk
     *            primary key of the individual to look at
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a boolean
     */
    public boolean getIsContentDirectlySubscribedByIndividual(
            PrimaryKey contentPk, PrimaryKey individualPk, PrimaryKey modePk);

    /**
     * Returns true if a content is subscribed by a member of a group.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param memberPk
     *            primary key of the member to query
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a boolean
     */
    public boolean getIsContentDirectlySubscribedByGroupMember(
            PrimaryKey contentPk, PrimaryKey memberPk, PrimaryKey modePk);

    /**
     * Returns true if an account is the subscriber of a content as an
     * individual or as a group member.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param accountPk
     *            primary key of the account to query
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a boolean
     */
    public boolean getIsContentDirectlySubscribedByAccount(
            PrimaryKey contentPk, PrimaryKey accountPk, PrimaryKey modePk);

    /**
     * Returns true if a content is subscribed by a group.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param groupPk
     *            primary key of the group to look at
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a boolean
     */
    public boolean getIsContentDirectlySubscribedByGroup(PrimaryKey contentPk,
            PrimaryKey groupPk, PrimaryKey modePk);

    /**
     * Returns true if a user can read all materials.
     * 
     * @param accountPk
     *            primary key of the account to query
     */
    public boolean getIsAllowedToReadAllContent(PrimaryKey accountPk);

    /**
     * Returns true if an article is visible.
     */
    public boolean getIsVisible(StandardArticle article);

    /**
     * Returns true if an article is accessible.
     */
    public boolean getIsAccessible(StandardArticle article);

    /**
     * Returns true if an article is accessible.
     */
    public boolean getHasPrivilegedAccess(StandardArticle article);

    /**
     * Returns true if an image is visible.
     */
    public boolean getIsVisible(StandardImage image);

    /**
     * Returns true if an image is accessible.
     */
    public boolean getIsAccessible(StandardImage image);

    /**
     * Returns true if an image is accessible.
     */
    public boolean getHasPrivilegedAccess(StandardImage image);

    /**
     * Returns true if content can be removed.
     */
    public boolean getIsContentRemovalAuthorized(PrimaryKey accountPk);
}

// end ContentManager
