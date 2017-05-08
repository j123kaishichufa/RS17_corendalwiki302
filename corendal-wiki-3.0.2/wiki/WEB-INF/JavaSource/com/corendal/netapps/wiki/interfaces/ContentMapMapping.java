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
 * @version $Id: ContentMapMapping.java,v 1.2 2007/09/02 21:56:29 tdanard Exp $
 */
public interface ContentMapMapping extends MappingWithAlphanumericSingleId {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentMapMapping";

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
     * Returns the value associated with the column: main_flag.
     * 
     * @return a java.lang.String object
     */
    public String getMainFlag();

    /**
     * Sets the value related to the column: main_flag.
     * 
     * @param mainFlag
     *            main_flag value
     */
    public void setMainFlag(String mainFlag);

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
