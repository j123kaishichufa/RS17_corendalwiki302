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
import com.corendal.netapps.wiki.interfaces.ContentVersionMapping;

/**
 * AbstractContentVersionMapping is the abstract class mapping the
 * notificationAction table.
 * 
 * @version $Id: AbstractContentVersionMapping.java,v 1.1 2005/09/09 17:08:40
 *          tdanard Exp $
 */
public abstract class AbstractContentVersionMapping extends
        AbstractMappingWithAlphanumericSingleId implements
        ContentVersionMapping {
    /** Current Flag. */
    private String currentFlag;

    /** Version number. */
    private int versionNumber;

    /** Version info id. */
    private String versionInfoId;

    /** Content id. */
    private String contentId;

    /** Request id. */
    private String requestId;

    /** Friendly address. */
    private String friendlyAddress;

    /** Body. */
    private String body;

    /** Comment. */
    private String comment;

    /** Enabled flag. */
    private String enabledFlag;

    /**
     * Default class constructor.
     */
    public AbstractContentVersionMapping() {
        // parent constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractMappingWithAlphanumericSingleId.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentVersionMapping) super.clone();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#getCurrentFlag()
     */
    public String getCurrentFlag() {
        return this.currentFlag;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#setCurrentFlag(java.lang.String)
     */
    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#getVersionNumber()
     */
    public int getVersionNumber() {
        return this.versionNumber;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#setVersionNumber(int)
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#getVersionInfoId()
     */
    public String getVersionInfoId() {
        return this.versionInfoId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#setVersionInfoId(java.lang.String)
     */
    public void setVersionInfoId(String versionInfoId) {
        this.versionInfoId = versionInfoId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#getContentId()
     */
    public String getContentId() {
        return this.contentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#setContentId(java.lang.String)
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#getRequestId()
     */
    public String getRequestId() {
        return this.requestId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#setRequestId(java.lang.String)
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#getFriendlyAddress()
     */
    public String getFriendlyAddress() {
        return this.friendlyAddress;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#setFriendlyAddress(java.lang.String)
     */
    public void setFriendlyAddress(String friendlyAddress) {
        this.friendlyAddress = friendlyAddress;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#getBody()
     */
    public String getBody() {
        return this.body;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#setBody(java.lang.String)
     */
    public void setBody(String body) {
        this.body = body;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#getComment()
     */
    public String getComment() {
        return this.comment;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentVersionMapping#setComment(java.lang.String)
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentVersionMapping#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentVersionMapping#setEnabledFlag(java.lang.String)
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
