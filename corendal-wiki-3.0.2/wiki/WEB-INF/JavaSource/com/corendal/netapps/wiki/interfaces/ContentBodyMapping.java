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
 * @version $Id: ContentBodyMapping.java,v 1.3 2007/09/02 21:56:29 tdanard Exp $
 */
public interface ContentBodyMapping extends MappingWithAlphanumericSingleId {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentBodyMapping";

    /**
     * Returns the value associated with the column: content_version_id.
     * 
     * @return a java.lang.String object
     */
    public String getContentVersionId();

    /**
     * Sets the value related to the column: content_version_id.
     * 
     * @param contentVersionId
     *            contentVersionId value
     */
    public void setContentVersionId(String contentVersionId);

    /**
     * Returns the value associated with the column: body_text.
     * 
     * @return a java.lang.String object
     */
    public String getBodyText();

    /**
     * Sets the value related to the column: body_text.
     * 
     * @param bodyText
     *            bodyText value
     */
    public void setBodyText(String bodyText);

    /**
     * Returns the value associated with the column: body_html.
     * 
     * @return a java.lang.String object
     */
    public String getBodyHtml();

    /**
     * Sets the value related to the column: body_html.
     * 
     * @param bodyHtml
     *            bodyHtml value
     */
    public void setBodyHtml(String bodyHtml);

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
