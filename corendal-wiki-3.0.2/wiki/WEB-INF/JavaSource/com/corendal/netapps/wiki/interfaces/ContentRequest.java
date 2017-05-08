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
 * @version $Id: ContentRequest.java,v 1.7 2007/10/17 21:28:58 tdanard Exp $
 */
public interface ContentRequest extends WriteDataBean, Classified, Enabled {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentRequest";

    /**
     * Returns the primary key of the direct preview icon of this content
     * request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getDirectPreviewIconPrimaryKey();

    /**
     * Returns the primary key of the approver of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getApproverAccountPrimaryKey();

    /**
     * Returns the primary key of the requestor of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getRequestorAccountPrimaryKey();

    /**
     * Returns the primary key of the editor of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getEditorAccountPrimaryKey();

    /**
     * Returns the primary key of the author of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getAuthorAccountPrimaryKey();

    /**
     * Returns the primary key of the parent content of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getParentContentPrimaryKey();

    /**
     * Returns the primary key of the child content of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getChildContentPrimaryKey();

    /**
     * Returns the primary key of the file of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getFilePrimaryKey();

    /**
     * Returns the primary key of the content info of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getContentInfoPrimaryKey();

    /**
     * Returns the primary key of the request type of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getRequestTypePrimaryKey();

    /**
     * Returns the primary key of the content type of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getContentTypePrimaryKey();

    /**
     * Returns the friendly address of this content request.
     * 
     * @return a java.lang.String object
     */
    public String getFriendlyAddress();

    /**
     * Returns the primary key of status type of this content request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getStatusTypePrimaryKey();

    /**
     * Approves this content request.
     */
    public void storeApproval();

    /**
     * Rejects this content request.
     */
    public void storeRejection();

    /**
     * Cancels this content request.
     */
    public void storeCancellation();

    /**
     * Removes this content request.
     */
    public void remove();

    /**
     * Stores the friendly address of this content request.
     */
    public void storeFriendlyAddress(String friendlyAddress);

    /**
     * Returns the comment of this content request.
     * 
     * @return a java.lang.String object
     */
    public String getComment();

    /**
     * Stores the comment of this content request.
     */
    public void storeComment(String comment);

    /**
     * Returns the body of this content request in the HTML format
     * 
     * @return a java.lang.String object
     */
    public String getBodyHTML();

    /**
     * Returns the body of this content request.
     * 
     * @return a java.lang.String object
     */
    public String getBodyText();

    /**
     * Returns the primary key of the indirect preview icon of this content
     * request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getIndirectPreviewIconPrimaryKey();

    /**
     * Returns the primary key of the associate editors group of this content
     * request.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getAssociateEditorsGroupPrimaryKey();
}

// end ContentRequest
