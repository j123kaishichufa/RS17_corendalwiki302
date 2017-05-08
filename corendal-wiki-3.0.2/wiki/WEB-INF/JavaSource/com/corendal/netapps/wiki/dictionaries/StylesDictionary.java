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
 * StylesDictionary is the class storing all identifier of styles used in the
 * program. Each constant refers to an id in the style table
 * 
 * @version $Id: StylesDictionary.java,v 1.3 2006/04/01 20:48:07 tdanard Exp $
 */
public final class StylesDictionary {
    /** Content path style. */
    public static final String CONTENT_PATH = "WIKI-STYL-3";

    /** Content description style. */
    public static final String CONTENT_DESCRIPTION = "WIKI-STYL-4";

    /** Content title style. */
    public static final String CONTENT_TITLE = "WIKI-STYL-5";

    /** Content body style. */
    public static final String CONTENT_BODY = "WIKI-STYL-6";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private StylesDictionary() {
        // this class is only used for storing constants
    }
}

// end StylesDictionary
