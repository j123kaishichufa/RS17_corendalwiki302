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

import com.corendal.netapps.framework.core.interfaces.StandardBean;
import com.corendal.netapps.framework.core.interfaces.StandardWriteBeanFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: StandardContentRequestFactory.java,v 1.1 2005/09/06 21:25:29
 *          tdanard Exp $
 */
public interface StandardContentRequestFactory extends StandardWriteBeanFactory {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory";

    /**
     * Creates a new image request.
     * 
     * @param name
     *            name of a content
     * @param description
     *            description of a content
     * @param authorPk
     *            primary key of an author account
     * @param filePk
     *            primary key of a stored file
     * @param parentArticlePk
     *            primary key of a parent article
     * @param classificationTypePk
     *            primary key of a classification type
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * @param ruleTypePk
     *            primary key of a rule type
     * @param comment
     *            a comment
     * 
     * 
     * @return a StandardBean object
     */
    public StandardBean createNewImageRequest(String name, String description,
            PrimaryKey authorPk, String friendlyAddress, PrimaryKey filePk,
            PrimaryKey parentArticlePk, PrimaryKey classificationTypePk,
            PrimaryKey requestorPk, PrimaryKey approverPk,
            PrimaryKey ruleTypePk, String comment);

    /**
     * Creates a new article request.
     * 
     * @param name
     *            name of a content
     * @param description
     *            description of a content
     * @param body
     *            body of a content
     * @param authorPk
     *            primary key of an author account
     * @param editorPk
     *            primary key of an editor account
     * @param associateEditorsPk
     *            primary key of an associate editor group
     * @param friendlyAddress
     *            a String representing a friendly address
     * @param parentArticlePk
     *            primary key of a parent article
     * @param classificationTypePk
     *            primary key of a classification type
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * @param ruleTypePk
     *            primary key of a rule type
     * 
     * 
     * @return a StandardBean object
     */
    public StandardBean createNewArticleRequest(String name,
            String description, String body, PrimaryKey authorPk,
            PrimaryKey editorPk, PrimaryKey associateEditorsPk,
            String friendlyAddress, PrimaryKey parentArticlePk,
            PrimaryKey classificationTypePk, PrimaryKey requestorPk,
            PrimaryKey approverPk, PrimaryKey ruleTypePk, String comment);

    /**
     * Creates a image removal request.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param parentArticlePk
     *            primary key of a parent article
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * 
     * 
     * @return a StandardBean object
     */
    public StandardBean createImageRemovalRequest(PrimaryKey childContentPk,
            PrimaryKey parentArticlePk, PrimaryKey requestorPk,
            PrimaryKey approverPk);

    /**
     * Creates an article removal request.
     * 
     * @param childContentPk
     *            primary key of a content
     * @param parentArticlePk
     *            primary key of a parent article
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * 
     * 
     * @return a StandardBean object
     */
    public StandardBean createArticleRemovalRequest(PrimaryKey childContentPk,
            PrimaryKey parentArticlePk, PrimaryKey requestorPk,
            PrimaryKey approverPk);

    /**
     * Creates an article update request.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param name
     *            name of a content
     * @param description
     *            description of a content
     * @param body
     *            a String representing a body
     * @param parentArticlePk
     *            primary key of a parent article
     * @param classificationTypePk
     *            primary key of a classification type
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * @param ruleTypePk
     *            primary key of a rule type
     * @param comment
     *            a comment
     * 
     * 
     * @return a StandardBean object
     */
    public StandardBean createArticleUpdateRequest(PrimaryKey childContentPk,
            String name, String description, String body, PrimaryKey authorPk,
            PrimaryKey editorPk, PrimaryKey associateEditorsPk,
            String friendlyAddress, PrimaryKey parentArticlePk,
            PrimaryKey classificationTypePk, PrimaryKey requestorPk,
            PrimaryKey approverPk, PrimaryKey ruleTypePk, String comment);

    /**
     * Creates a image update request.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param name
     *            name of a content
     * @param description
     *            description of a content
     * @param authorPk
     *            primary key of an author account
     * @param filePk
     *            primary key of a stored file
     * @param parentArticlePk
     *            primary key of a parent article
     * @param classificationTypePk
     *            primary key of a classification type
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * @param ruleTypePk
     *            primary key of a rule type
     * @param comment
     *            a comment
     * 
     * 
     * @return a StandardBean object
     */
    public StandardBean createImageUpdateRequest(PrimaryKey childContentPk,
            String name, String description, PrimaryKey authorPk,
            String friendlyAddress, PrimaryKey filePk,
            PrimaryKey parentArticlePk, PrimaryKey classificationTypePk,
            PrimaryKey requestorPk, PrimaryKey approverPk,
            PrimaryKey ruleTypePk, String comment);

    /**
     * Creates a new reference request.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param parentArticlePk
     *            primary key of a parent article
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * 
     * 
     * @return a StandardBean object
     */
    public StandardBean createNewReferenceRequest(PrimaryKey childContentPk,
            PrimaryKey parentArticlePk, PrimaryKey requestorPk,
            PrimaryKey approverPk);

    /**
     * Creates a reference removal request.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param parentArticlePk
     *            primary key of a parent article
     * @param requestorPk
     *            primary key of a requestor account
     * @param approverPk
     *            primary key of an approver account
     * 
     * 
     * @return a StandardBean object
     */
    public StandardBean createReferenceRemovalRequest(
            PrimaryKey childContentPk, PrimaryKey parentArticlePk,
            PrimaryKey requestorPk, PrimaryKey approverPk);

    /**
     * Returns the list of standard content requests for a requestor primary
     * key.
     * 
     * @param pk
     *            primary key of the requestor
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentRequest> findEnabledByRequestorPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard content requests for an approver primary
     * key.
     * 
     * @param pk
     *            primary key of the requestor
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentRequest> findEnabledByApproverPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard content requests for a requestor primary
     * key.
     * 
     * @param pk
     *            primary key of the requestor
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByRequestorPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard content request primary keys for an approver
     * primary key.
     * 
     * @param pk
     *            primary key of the requestor
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByApproverPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of pending standard content requests for a content
     * primary key.
     * 
     * @param pk
     *            primary key of the content
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentRequest> findEnabledPendingByContentPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of pending standard content requests for an article
     * primary key.
     * 
     * @param pk
     *            primary key of the content
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentRequest> findEnabledPendingByParentArticlePrimaryKey(
            PrimaryKey pk);
}

// end StandardContentRequestFactory
