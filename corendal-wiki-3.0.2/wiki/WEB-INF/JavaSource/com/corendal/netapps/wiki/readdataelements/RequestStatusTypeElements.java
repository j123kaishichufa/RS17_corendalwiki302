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
package com.corendal.netapps.wiki.readdataelements;

/**
 * RequestStatusTypeElements is the class storing all request status type
 * element names.
 * 
 * @version $Id: RequestStatusTypeElements.java,v 1.1 2005/09/06 21:25:29
 *          tdanard Exp $
 */

public class RequestStatusTypeElements {
    /** Name of the collection element. */
    public static final String COLLECTION = "requeststatustypes";

    /** Name of the entity element. */
    public static final String ENTITY = "requeststatustype";

    /** Name of the name element. */
    public final static String NAME = "name";

    /** Name of the description element. */
    public final static String DESCRIPTION = "description";

    /** Name of the comment element. */
    public final static String COMMENT = "comment";

    /** Name of the order element. */
    public final static String ORDER = "order";

    /** Name of the enabled flag element. */
    public final static String ENABLED_FLAG = "enabled-flag";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private RequestStatusTypeElements() {
        // this class is only used for storing constants
    }
}
