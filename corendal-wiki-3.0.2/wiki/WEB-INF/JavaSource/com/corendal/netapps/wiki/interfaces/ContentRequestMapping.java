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
 * @version $Id: ContentRequestMapping.java,v 1.3 2006/04/01 19:37:34 tdanard
 *          Exp $
 */
public interface ContentRequestMapping extends MappingWithAlphanumericSingleId {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentRequestMapping";

    /**
     * Returns the value associated with the column: request_typ_id.
     * 
     * @return a java.lang.String object
     */
    public String getRequestTypeId();

    /**
     * Sets the value related to the column: request_typ_id.
     * 
     * @param requestTypeId
     *            request_typ_id value
     */
    public void setRequestTypeId(String requestTypeId);

    /**
     * Returns the value associated with the column: content_info_id.
     * 
     * @return a java.lang.String object
     */
    public String getContentInfoId();

    /**
     * Sets the value related to the column: content_info_id.
     * 
     * @param contentInfoId
     *            content_info_id value
     */
    public void setContentInfoId(String contentInfoId);

    /**
     * Returns the value associated with the column: content_typ_id.
     * 
     * @return a java.lang.String object
     */
    public String getContentTypeId();

    /**
     * Sets the value related to the column: content_typ_id.
     * 
     * @param contentTypeId
     *            content_typ_id value
     */
    public void setContentTypeId(String contentTypeId);

    /**
     * Returns the value associated with the column: status_typ_id.
     * 
     * @return a java.lang.String object
     */
    public String getStatusTypeId();

    /**
     * Sets the value related to the column: status_typ_id.
     * 
     * @param statusTypeId
     *            status_typ_id value
     */
    public void setStatusTypeId(String statusTypeId);

    /**
     * Returns the value associated with the column: parent_content_id.
     * 
     * @return a java.lang.String object
     */
    public String getParentContentId();

    /**
     * Sets the value related to the column: parent_content_id.
     * 
     * @param parentContentId
     *            parent_content_id value
     */
    public void setParentContentId(String parentContentId);

    /**
     * Returns the value associated with the column: child_content_id.
     * 
     * @return a java.lang.String object
     */
    public String getChildContentId();

    /**
     * Sets the value related to the column: child_content_id.
     * 
     * @param childContentId
     *            child_content_id value
     */
    public void setChildContentId(String childContentId);

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
     * Returns the value associated with the column: content_class_typ_id.
     * 
     * @return a java.lang.String object
     */
    public String getContentClassTypeId();

    /**
     * Sets the value related to the column: content_class_typ_id.
     * 
     * @param contentClassTypeId
     *            content_class_typ_id value
     */
    public void setContentClassTypeId(String contentClassTypeId);

    /**
     * Returns the value associated with the column: content_rule_typ_id.
     * 
     * @return a java.lang.String object
     */
    public String getContentRuleTypeId();

    /**
     * Sets the value related to the column: content_rule_typ_id.
     * 
     * @param contentRuleTypeId
     *            content_rule_typ_id value
     */
    public void setContentRuleTypeId(String contentRuleTypeId);

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
     * Returns the value associated with the column: order.
     * 
     * @return a java.lang.String object
     */
    public int getOrder();

    /**
     * Sets the value related to the column: order.
     * 
     * @param order
     *            order value
     */
    public void setOrder(int order);

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
