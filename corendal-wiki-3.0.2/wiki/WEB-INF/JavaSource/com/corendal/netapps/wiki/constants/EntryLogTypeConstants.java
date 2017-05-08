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
 * EntryLogTypeConstants is the class storing all identifier of entry log types.
 * 
 * @version $Id: EntryLogTypeConstants.java,v 1.1 2005/09/06 21:25:32 tdanard
 *          Exp $
 */

public class EntryLogTypeConstants {
    /** Type of entry log for supersede actions. */
    public static final String SUPERSEDE = "SUPERSEDE";

    /** Type of entry log for approvals. */
    public static final String APPROVAL = "APPROVAL";

    /** Type of entry log for rejections. */
    public static final String REJECTION = "REJECTION";

    /** Type of entry log for cancellations. */
    public static final String CANCELLATION = "CANCELLATION";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private EntryLogTypeConstants() {
        // this class is only used for storing constants
    }

}
