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

import java.util.List;

import com.corendal.netapps.framework.core.interfaces.StandardWriteBeanFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: StandardImageVersionFactory.java,v 1.1 2005/09/06 21:25:29
 *          tdanard Exp $
 */
public interface StandardImageVersionFactory extends StandardWriteBeanFactory {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardImageVersionFactory";

    /**
     * Returns the image versions based upon a image primary key.
     * 
     * @param pk
     *            primary key of a image
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardImageVersion> findEnabledByImagePrimaryKey(PrimaryKey pk);
}

// end StandardImageVersionFactory
