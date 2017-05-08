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

import java.util.Date;

import com.corendal.netapps.framework.core.interfaces.ReadDataBean;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: ContentAccessDay.java,v 1.6 2007/09/03 19:42:00 tdanard Exp $
 */
public interface ContentAccessDay extends ReadDataBean {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentAccessDay";

    /**
     * Returns the primary key of the content of this content access day.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getContentPrimaryKey();

    /**
     * Returns the date of this content access day.
     * 
     * @return a java.util.Date object
     */
    public Date getAccessDate();

    /**
     * Returns the access number of this content access day.
     * 
     * @return an int
     */
    public int getAccessNum();

    /**
     * Returns the access rank of this content access day.
     * 
     * @return an int
     */
    public int getAccessRank();
}

// end ContentAccessDay
