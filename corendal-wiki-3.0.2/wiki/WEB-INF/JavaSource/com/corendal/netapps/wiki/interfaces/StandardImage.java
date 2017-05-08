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

import com.corendal.netapps.framework.core.interfaces.StandardWriteBean;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: StandardImage.java,v 1.8 2007/09/03 19:42:01 tdanard Exp $
 */
public interface StandardImage extends StandardWriteBean, Image, Authored,
        Searched, Secured, Pathed {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardImage";

    /**
     * Removes this image and all associated subscriptions.
     * 
     * 
     * 
     */
    public void remove(PrimaryKey referenceRequestPk);

    /**
     * Notifies all subscribers that a new version of this image has been
     * issued.
     * 
     * 
     * 
     */
    public void notifySubscribers();

    /**
     * Returns the relative location of the page printing this image as online
     * help.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public String getOnlineHelpRelativeLocation();

    /**
     * Returns the default location of the page printing this image as online
     * help.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public String getOnlineHelpDefaultLocation();

    /**
     * Returns the absolute location of the page printing this image as online
     * help.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public String getOnlineHelpAbsoluteLocation();
}

// end StandardImage
