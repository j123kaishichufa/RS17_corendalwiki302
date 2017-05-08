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
package com.corendal.netapps.wiki.dictionaries;

/**
 * PropertiesDictionary is the class storing all identifier of properties used
 * in the program. Each constant refers to an id in the property table.
 * 
 * @version $Id: PropertiesDictionary.java,v 1.3 2006/04/01 20:48:07 tdanard Exp $
 */
public final class PropertiesDictionary {
    /** Weekly digest day. */
    public static final String WEEKLY_DIGEST_DAY = "wiki.weeklydigest.day";

    /** Help desk friendly address. */
    public static final String HELP_DESK_FRIENDLY_ADDRESS = "wiki.friendlyaddress.helpdesk";

    /** Release notes friendly address. */
    public static final String RELEASE_NOTES_FRIENDLY_ADDRESS = "wiki.friendlyaddress.releasenotes";

    /** online help friendly address. */
    public static final String ONLINE_HELP_FRIENDLY_ADDRESS = "wiki.friendlyaddress.onlinehelp";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private PropertiesDictionary() {
        // this class is only used for storing constants
    }
}

// end PropertiesDictionary
