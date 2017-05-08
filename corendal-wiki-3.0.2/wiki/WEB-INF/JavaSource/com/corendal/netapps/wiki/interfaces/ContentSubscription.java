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
 * @version $Id: ContentSubscription.java,v 1.7 2007/10/17 21:28:58 tdanard Exp $
 */
public interface ContentSubscription extends WriteDataBean, Enabled {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentSubscription";

    /**
     * Returns the primary key of the content of this content subscription.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getContentPrimaryKey();

    /**
     * Stores the subscriber account of this content subscription.
     */
    public void storeSubscriberAccount(PrimaryKey subscriberPk);

    /**
     * Stores the subscriber group of this content subscription.
     */
    public void storeSubscriberGroup(PrimaryKey subscriberPk);

    /**
     * Removes this content subscription.
     */
    public void remove();

    /**
     * Returns the primary key of the mode of this content subscription.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getModePrimaryKey();

    /**
     * Returns the primary key of the type of this content subscription.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getTypePrimaryKey();
}

// end ContentSubscription
