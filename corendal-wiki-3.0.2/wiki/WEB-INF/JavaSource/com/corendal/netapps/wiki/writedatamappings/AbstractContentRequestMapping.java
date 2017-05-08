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
package com.corendal.netapps.wiki.writedatamappings;

import com.corendal.netapps.framework.modelaccess.datamappings.AbstractMappingWithAlphanumericSingleId;
import com.corendal.netapps.wiki.interfaces.ContentRequestMapping;

/**
 * AbstractContentRequestMapping is the abstract class mapping the
 * notificationAction table.
 * 
 * @version $Id: AbstractContentRequestMapping.java,v 1.1 2005/09/09 17:08:40
 *          tdanard Exp $
 */
public abstract class AbstractContentRequestMapping extends
        AbstractMappingWithAlphanumericSingleId implements
        ContentRequestMapping {
    /** Request type id. */
    private String requestTypeId;

    /** Content info id. */
    private String contentInfoId;

    /** Content type id. */
    private String contentTypeId;

    /** Status type id. */
    private String statusTypeId;

    /** Parent content id. */
    private String parentContentId;

    /** Child content id. */
    private String childContentId;

    /** Friendly address. */
    private String friendlyAddress;

    /** Body. */
    private String body;

    /** Content class type id. */
    private String contentClassTypeId;

    /** Content rule type id. */
    private String contentRuleTypeId;

    /** Commetn. */
    private String comment;

    /** Order ord */
    private int order;

    /** Enabled flag. */
    private String enabledFlag;

    /**
     * Default class constructor.
     */
    public AbstractContentRequestMapping() {
        // parent constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractMappingWithAlphanumericSingleId.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentRequestMapping) super.clone();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getRequestTypeId()
     */
    public String getRequestTypeId() {
        return this.requestTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setRequestTypeId(java.lang.String)
     */
    public void setRequestTypeId(String requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getContentInfoId()
     */
    public String getContentInfoId() {
        return this.contentInfoId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setContentInfoId(java.lang.String)
     */
    public void setContentInfoId(String contentInfoId) {
        this.contentInfoId = contentInfoId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getContentTypeId()
     */
    public String getContentTypeId() {
        return this.contentTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setContentTypeId(java.lang.String)
     */
    public void setContentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getStatusTypeId()
     */
    public String getStatusTypeId() {
        return this.statusTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setStatusTypeId(java.lang.String)
     */
    public void setStatusTypeId(String statusTypeId) {
        this.statusTypeId = statusTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getParentContentId()
     */
    public String getParentContentId() {
        return this.parentContentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setParentContentId(java.lang.String)
     */
    public void setParentContentId(String parentContentId) {
        this.parentContentId = parentContentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getChildContentId()
     */
    public String getChildContentId() {
        return this.childContentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setChildContentId(java.lang.String)
     */
    public void setChildContentId(String childContentId) {
        this.childContentId = childContentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getFriendlyAddress()
     */
    public String getFriendlyAddress() {
        return this.friendlyAddress;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setFriendlyAddress(java.lang.String)
     */
    public void setFriendlyAddress(String friendlyAddress) {
        this.friendlyAddress = friendlyAddress;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getBody()
     */
    public String getBody() {
        return this.body;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setBody(java.lang.String)
     */
    public void setBody(String body) {
        this.body = body;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getContentClassTypeId()
     */
    public String getContentClassTypeId() {
        return this.contentClassTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setContentClassTypeId(java.lang.String)
     */
    public void setContentClassTypeId(String contentClassTypeId) {
        this.contentClassTypeId = contentClassTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getContentRuleTypeId()
     */
    public String getContentRuleTypeId() {
        return this.contentRuleTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setContentRuleTypeId(java.lang.String)
     */
    public void setContentRuleTypeId(String contentRuleTypeId) {
        this.contentRuleTypeId = contentRuleTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getComment()
     */
    public String getComment() {
        return this.comment;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setComment(java.lang.String)
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#getOrder()
     */
    public int getOrder() {
        return this.order;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestMapping#setOrder(int)
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentRequestMapping#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentRequestMapping#setEnabledFlag(java.lang.String)
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
