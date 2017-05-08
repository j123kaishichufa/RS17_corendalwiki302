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
 * ContentClassificationTypesDictionary is the class storing all identifier of
 * content classification types used in the program. Each constant refers to an
 * id in the content_classification_typ table
 * 
 * @version $Id: ContentClassificationTypesDictionary.java,v 1.1 2005/09/06
 *          21:25:31 tdanard Exp $
 */
public final class ContentClassificationTypesDictionary {
    /** Same as parent article. */
    public static final String SAME_AS_PARENT = "WIKI-RESC-1";

    /** No login required type. */
    public static final String NO_LOGIN_REQUIRED = "WIKI-RESC-2";

    /** Login required. */
    public static final String LOGIN_REQUIRED = "WIKI-RESC-3";

    /** Access limited. */
    public static final String ACCESS_LIMITED = "WIKI-RESC-4";

    /** Visibility and access limited. */
    public static final String VISIBILITY_AND_ACCESS_LIMITED = "WIKI-RESC-5";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private ContentClassificationTypesDictionary() {
        // this class is only used for storing constants
    }
}

// end ContentClassificationTypesDictionary
