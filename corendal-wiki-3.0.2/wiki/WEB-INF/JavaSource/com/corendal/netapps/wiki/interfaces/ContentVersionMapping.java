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
package com.corendal.netapps.wiki.interfaces;

import com.corendal.netapps.framework.modelaccess.interfaces.MappingWithAlphanumericSingleId;

/**
 * @version $Id: ContentVersionMapping.java,v 1.3 2006/04/01 19:37:34 tdanard
 *          Exp $
 */
public interface ContentVersionMapping extends MappingWithAlphanumericSingleId {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentVersionMapping";

    /**
     * Returns the value associated with the column: current_flag.
     * 
     * @return a java.lang.String object
     */
    public String getCurrentFlag();

    /**
     * Sets the value related to the column: current_flag.
     * 
     * @param currentFlag
     *            current_flag value
     */
    public void setCurrentFlag(String currentFlag);

    /**
     * Returns the value associated with the column: version_num.
     * 
     * @return a int object
     */
    public int getVersionNumber();

    /**
     * Sets the value related to the column: version_num.
     * 
     * @param versionNumber
     *            version_num value
     */
    public void setVersionNumber(int versionNumber);

    /**
     * Returns the value associated with the column: version_info_id.
     * 
     * @return a java.lang.String object
     */
    public String getVersionInfoId();

    /**
     * Sets the value related to the column: version_info_id.
     * 
     * @param versionInfoId
     *            version_info_id value
     */
    public void setVersionInfoId(String versionInfoId);

    /**
     * Returns the value associated with the column: content_id.
     * 
     * @return a java.lang.String object
     */
    public String getContentId();

    /**
     * Sets the value related to the column: content_id.
     * 
     * @param contentId
     *            content_id value
     */
    public void setContentId(String contentId);

    /**
     * Returns the value associated with the column: request_id.
     * 
     * @return a java.lang.String object
     */
    public String getRequestId();

    /**
     * Sets the value related to the column: request_id.
     * 
     * @param requestId
     *            request_id value
     */
    public void setRequestId(String requestId);

    /**
     * Returns the value associated with the column: friendly_address.
     * 
     * @return a java.lang.String object
     */
    public String getFriendlyAddress();

    /**
     * Sets the value related to the column: friendly_address.
     * 
     * @param friendlyAddress
     *            friendly_address value
     */
    public void setFriendlyAddress(String friendlyAddress);

    /**
     * Returns the value associated with the column: body.
     * 
     * @return a java.lang.String object
     */
    public String getBody();

    /**
     * Sets the value related to the column: body.
     * 
     * @param body
     *            body value
     */
    public void setBody(String body);

    /**
     * Returns the value associated with the column: cmnt.
     * 
     * @return a java.lang.String object
     */
    public String getComment();

    /**
     * Sets the value related to the column: cmnt.
     * 
     * @param comment
     *            cmnt value
     */
    public void setComment(String comment);

    /**
     * Returns the value associated with the column: enabled_flag.
     * 
     * @return a java.lang.String object
     */
    public String getEnabledFlag();

    /**
     * Sets the value related to the column: enabled_flag.
     * 
     * @param enabled_flag
     *            enabled_flag value
     */
    public void setEnabledFlag(String enabledFlag);
}
