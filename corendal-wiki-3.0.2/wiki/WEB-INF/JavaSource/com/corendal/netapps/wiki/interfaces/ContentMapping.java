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
 * @version $Id: ContentMapping.java,v 1.2 2007/09/02 21:56:29 tdanard Exp $
 */
public interface ContentMapping extends MappingWithAlphanumericSingleId {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentMapping";

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
     * Returns the value associated with the column: whats_new_flag.
     * 
     * @return a java.lang.String object
     */
    public String getWhatsNewFlag();

    /**
     * Sets the value related to the column: whats_new_flag.
     * 
     * @param whatsNewFlag
     *            whats_new_flag value
     */
    public void setWhatsNewFlag(String whatsNewFlag);

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
