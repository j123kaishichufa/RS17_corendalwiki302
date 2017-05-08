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
 * RolesDictionary is the class storing all identifier of roles used in the
 * program. Each constant refers to an id in the role table
 * 
 * @version $Id: RolesDictionary.java,v 1.4 2008/11/19 17:12:04 tdanard Exp $
 */
public final class RolesDictionary {
    /** Editor role. */
    public static final String EDITOR = "WIKI-ROLE-1";

    /** Author role. */
    public static final String AUTHOR = "WIKI-ROLE-2";

    /** Approver role. */
    public static final String APPROVER = "WIKI-ROLE-3";

    /** Requestor role. */
    public static final String REQUESTOR = "WIKI-ROLE-4";

    /** Subscriber role. */
    public static final String SUBSCRIBER = "WIKI-ROLE-5";

    /** Role used to indicate an access group allowed. */
    public static final String ACCESS_GROUP_ALLOWED = "WIKI-ROLE-6";

    /** Role used to indicate an access group denied. */
    public static final String ACCESS_GROUP_DENIED = "WIKI-ROLE-7";

    /** Attachment role. */
    public static final String ATTACHMENT = "WIKI-ROLE-8";

    /** Content remover role. */
    public static final String CONTENT_REMOVER = "WIKI-ROLE-9";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private RolesDictionary() {
        // this class is only used for storing constants
    }
}

// end RolesDictionary
