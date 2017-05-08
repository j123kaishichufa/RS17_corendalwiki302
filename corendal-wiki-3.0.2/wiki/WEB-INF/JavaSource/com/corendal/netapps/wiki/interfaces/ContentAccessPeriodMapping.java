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

import java.util.Date;

import com.corendal.netapps.framework.modelaccess.interfaces.MappingWithAlphanumericSingleId;

/**
 * @version $Id: ContentAccessPeriodMapping.java,v 1.3 2006/04/01 19:37:34
 *          tdanard Exp $
 */
public interface ContentAccessPeriodMapping extends
        MappingWithAlphanumericSingleId {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentAccessPeriodMapping";

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
     * Returns the value associated with the column: access_date.
     * 
     * @return a java.util.Date object
     */
    public Date getAccessDate();

    /**
     * Sets the value related to the column: access_date.
     * 
     * @param accessDate
     *            access_date value
     */
    public void setAccessDate(Date accessDate);

    /**
     * Returns the value associated with the column: access_num.
     * 
     * @return a int object
     */
    public int getAccessNumber();

    /**
     * Sets the value related to the column: access_num.
     * 
     * @param accessNumber
     *            access_num value
     */
    public void setAccessNumber(int accessNumber);

    /**
     * Returns the value associated with the column: access_rank.
     * 
     * @return a int object
     */
    public int getAccessRank();

    /**
     * Sets the value related to the column: access_rank.
     * 
     * @param accessRank
     *            access_rank value
     */
    public void setAccessRank(int accessRank);

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
