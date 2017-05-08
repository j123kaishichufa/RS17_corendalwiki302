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

import com.corendal.netapps.framework.core.interfaces.StandardWriteBean;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: StandardContent.java,v 1.8 2007/09/03 19:42:00 tdanard Exp $
 */
public interface StandardContent extends StandardWriteBean, Content, Searched,
        Secured, Pathed {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardContent";

    /**
     * Returns true if this content is the direct child of an article.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsDirectChildOf(PrimaryKey articlePk);

    /**
     * Returns true if this content is the indirect child of an article.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsIndirectChildOf(PrimaryKey articlePk);

    /**
     * Returns true if this content is the child (direct or indirect) of an
     * article.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsChildOf(PrimaryKey articlePk);

    /**
     * Returns true if this content is subscribed by an individual.
     * 
     * @param individualPk
     *            primary key of the individual to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getIsDirectlySubscribedByIndividual(PrimaryKey individualPk);

    /**
     * Returns true if this content is subscribed by a member of a group.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsDirectlySubscribedByGroupMember(PrimaryKey memberPk);

    /**
     * Returns true if an account is the subscriber of a content as an
     * individual or as a group member.
     * 
     * 
     * 
     * @return a boolean
     */
    public boolean getIsDirectlySubscribedByAccount(PrimaryKey accountPk);

    /**
     * Returns true if this content is subscribed by a group.
     * 
     * @param groupPk
     *            primary key of the group to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getIsDirectlySubscribedByGroup(PrimaryKey groupPk);

    /**
     * Returns true if this content uses an access group as allowed access
     * group.
     * 
     * @param groupPk
     *            primary key of the group to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getHasAccessGroup(PrimaryKey groupPk);

    /**
     * Returns true if this content uses an access group as allowed access
     * group.
     * 
     * @param groupPk
     *            primary key of the group to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getHasAllowedAccessGroup(PrimaryKey groupPk);

    /**
     * Returns true if this content uses an access group as denied access group.
     * 
     * @param groupPk
     *            primary key of the group to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getHasDeniedAccessGroup(PrimaryKey groupPk);

    /**
     * Approves a new reference request to this content.
     * 
     * 
     * 
     */
    public void approveNewReferenceRequest(PrimaryKey parentArticlePk,
            ContentRequest contentRequest);

    /**
     * Approves a reference removal request to this content.
     * 
     * 
     * 
     */
    public void approveReferenceRemovalRequest(PrimaryKey parentArticlePk,
            ContentRequest contentRequest);
}

// end StandardContent
