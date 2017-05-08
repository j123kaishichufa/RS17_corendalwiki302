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
package com.corendal.netapps.wiki.constants;

/**
 * SessionAttributeConstants is the class storing all identifier of session
 * attributes.
 * 
 * @version $Id: SessionAttributeConstants.java,v 1.1 2005/09/06 21:25:32
 *          tdanard Exp $
 */

public class SessionAttributeConstants {
    /** Name of the actual contents history attribute in the session. */
    public static final String CONTENTS_HISTORY = "CONTENTS_HISTORY_ATTR";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private SessionAttributeConstants() {
        // this class is only used for storing constants
    }

}
