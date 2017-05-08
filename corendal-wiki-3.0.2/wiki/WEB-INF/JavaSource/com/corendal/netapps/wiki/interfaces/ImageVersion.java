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
 * @version $Id: ImageVersion.java,v 1.7 2007/10/17 21:28:58 tdanard Exp $
 */
public interface ImageVersion extends WriteDataBean, Previewed, Enabled {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ImageVersion";

    /**
     * Returns the primary key of the content of this image version.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getContentPrimaryKey();

    /**
     * Returns the primary key of the file of this image version.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getFilePrimaryKey();

    /**
     * Returns the version number of this image version.
     * 
     * @return an int
     */
    public int getVersionNum();

    /**
     * Returns the primary key of the info of this image version.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getInfoPrimaryKey();

    /**
     * Returns the current flag of this image version.
     * 
     * @return a java.lang.String object
     */
    public String getCurrentFlag();

    /**
     * Stores the current flag of this image version.
     */
    public void storeCurrentFlag(String currentFlag);

    /**
     * Removes this image version.
     */
    public void remove();

    /**
     * Returns the comment of this image version.
     * 
     * @return a java.lang.String object
     */
    public String getComment();

    /**
     * Returns the primary key of the request of this image version.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getRequestPrimaryKey();

    /**
     * Returns the primary key of the author account of this image version.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getAuthorAccountPrimaryKey();

    /**
     * Returns the primary key of the rule of this image version.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getRuleTypePrimaryKey();
}

// end ImageVersion
