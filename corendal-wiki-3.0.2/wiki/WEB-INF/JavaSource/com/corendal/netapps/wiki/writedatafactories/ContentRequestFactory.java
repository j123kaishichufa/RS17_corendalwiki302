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
import com.corendal.netapps.wiki.interfaces.ContentRequest;
import com.corendal.netapps.wiki.writedatabeans.DefaultContentRequest;

/**
 * ContentRequestFactory is the static class building new ContentRequest
 * instances.
 * 
 * @version $Id: ContentRequestFactory.java,v 1.1 2005/09/06 21:25:36 tdanard
 *          Exp $
 */
public final class ContentRequestFactory {
    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private ContentRequestFactory() {
        // this class is only used for storing constants
    }

    /**
     * Returns an existing content request.
     * 
     * @param contentRequestPk
     *            primary key of a content request
     * 
     * 
     * @return a ContentRequest object
     */
    public static final ContentRequest findByPrimaryKey(
            PrimaryKey contentRequestPk) {
        DefaultContentRequest contentRequest = new DefaultContentRequest();
        contentRequest.setPrimaryKeyAndLoad(contentRequestPk);
        return contentRequest;
    }

    /**
     * Creates a new image request.
     * 
     * @param contentInfoPk
     *            primary key of a content info
     * @param authorPk
     *            primary key of an author account
     * @param friendlyAddress
     *            a friendly address
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
     * @return a ContentRequest object
     */
    public static final ContentRequest createNewImageRequest(
            PrimaryKey contentInfoPk, PrimaryKey authorPk,
            String friendlyAddress, PrimaryKey filePk,
            PrimaryKey parentArticlePk, PrimaryKey classificationTypePk,
            PrimaryKey requestorPk, PrimaryKey approverPk,
            PrimaryKey ruleTypePk, String comment) {
        DefaultContentRequest contentRequest = new DefaultContentRequest();
        contentRequest.addNewImageRequest(contentInfoPk, authorPk,
                friendlyAddress, filePk, parentArticlePk, classificationTypePk,
                requestorPk, approverPk, ruleTypePk, comment);

        return contentRequest;
    }

    /**
     * Creates a new article request.
     * 
     * @param contentInfoPk
     *            primary key of a content info
     * @param body
     *            a String representing a body
     * @param authorPk
     *            primary key of an author account
     * @param editorPk
     *            primary key of an editor account
     * @param associateEditorsPk
     *            primary key of an associate editors group
     * @param friendlyAddress
     *            friendly address
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
     * @return a ContentRequest object
     */
    public static final ContentRequest createNewArticleRequest(
            PrimaryKey contentInfoPk, String body, PrimaryKey authorPk,
            PrimaryKey editorPk, PrimaryKey associateEditorsPk,
            String friendlyAddress, PrimaryKey parentArticlePk,
            PrimaryKey classificationTypePk, PrimaryKey requestorPk,
            PrimaryKey approverPk, PrimaryKey ruleTypePk, String comment) {
        DefaultContentRequest contentRequest = new DefaultContentRequest();
        contentRequest.addNewArticleRequest(contentInfoPk, body, authorPk,
                editorPk, associateEditorsPk, friendlyAddress, parentArticlePk,
                classificationTypePk, requestorPk, approverPk, ruleTypePk,
                comment);

        return contentRequest;
    }

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
     * @return a ContentRequest object
     */
    public static final ContentRequest createImageRemovalRequest(
            PrimaryKey childContentPk, PrimaryKey parentArticlePk,
            PrimaryKey requestorPk, PrimaryKey approverPk) {
        DefaultContentRequest contentRequest = new DefaultContentRequest();
        contentRequest.addImageRemovalRequest(childContentPk, parentArticlePk,
                requestorPk, approverPk);

        return contentRequest;
    }

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
     * @return a ContentRequest object
     */
    public static final ContentRequest createArticleRemovalRequest(
            PrimaryKey childContentPk, PrimaryKey parentArticlePk,
            PrimaryKey requestorPk, PrimaryKey approverPk) {
        DefaultContentRequest contentRequest = new DefaultContentRequest();
        contentRequest.addArticleRemovalRequest(childContentPk,
                parentArticlePk, requestorPk, approverPk);

        return contentRequest;
    }

    /**
     * Creates an article update request.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param contentInfoPk
     *            primary key of a content info
     * @param body
     *            a String representing a body
     * @param authorPk
     *            primary key of an author account
     * @param editorPk
     *            primary key of an editor account
     * @param associateEditorsPk
     *            primary key of an associate editors group
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
     * @param comment
     *            a comment
     * 
     * 
     * @return a ContentRequest object
     */
    public static final ContentRequest createArticleUpdateRequest(
            PrimaryKey childContentPk, PrimaryKey contentInfoPk, String body,
            PrimaryKey authorPk, PrimaryKey editorPk,
            PrimaryKey associateEditorsPk, String friendlyAddress,
            PrimaryKey parentArticlePk, PrimaryKey classificationTypePk,
            PrimaryKey requestorPk, PrimaryKey approverPk,
            PrimaryKey ruleTypePk, String comment) {
        DefaultContentRequest contentRequest = new DefaultContentRequest();
        contentRequest.addArticleUpdateRequest(childContentPk, contentInfoPk,
                body, authorPk, editorPk, associateEditorsPk, friendlyAddress,
                parentArticlePk, classificationTypePk, requestorPk, approverPk,
                ruleTypePk, comment);

        return contentRequest;
    }

    /**
     * Creates a image update request.
     * 
     * @param childContentPk
     *            primary key of a child content
     * @param contentInfoPk
     *            primary key of a content info
     * @param authorPk
     *            primary key of an author account
     * @param friendlyAddress
     *            a friendly address
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
     * @return a ContentRequest object
     */
    public static final ContentRequest createImageUpdateRequest(
            PrimaryKey childContentPk, PrimaryKey contentInfoPk,
            PrimaryKey authorPk, String friendlyAddress, PrimaryKey filePk,
            PrimaryKey parentArticlePk, PrimaryKey classificationTypePk,
            PrimaryKey requestorPk, PrimaryKey approverPk,
            PrimaryKey ruleTypePk, String comment) {
        DefaultContentRequest contentRequest = new DefaultContentRequest();
        contentRequest.addImageUpdateRequest(childContentPk, contentInfoPk,
                authorPk, friendlyAddress, filePk, parentArticlePk,
                classificationTypePk, requestorPk, approverPk, ruleTypePk,
                comment);

        return contentRequest;
    }

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
     * @return a ContentRequest object
     */
    public static final ContentRequest createNewReferenceRequest(
            PrimaryKey childContentPk, PrimaryKey parentArticlePk,
            PrimaryKey requestorPk, PrimaryKey approverPk) {
        DefaultContentRequest contentRequest = new DefaultContentRequest();
        contentRequest.addNewReferenceRequest(childContentPk, parentArticlePk,
                requestorPk, approverPk);

        return contentRequest;
    }

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
     * @return a ContentRequest object
     */
    public static final ContentRequest createReferenceRemovalRequest(
            PrimaryKey childContentPk, PrimaryKey parentArticlePk,
            PrimaryKey requestorPk, PrimaryKey approverPk) {
        DefaultContentRequest contentRequest = new DefaultContentRequest();
        contentRequest.addReferenceRemovalRequest(childContentPk,
                parentArticlePk, requestorPk, approverPk);

        return contentRequest;
    }

}

// end ContentRequestFactory
