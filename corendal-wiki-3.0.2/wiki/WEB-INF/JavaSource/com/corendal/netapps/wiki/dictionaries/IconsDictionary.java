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
 * IconsDictionary is the class storing all identifier of icons used in the
 * program. Each constant refers to an id in the icon table
 * 
 * @version $Id: IconsDictionary.java,v 1.3 2006/04/01 20:48:07 tdanard Exp $
 */
public final class IconsDictionary {
    /** Article image. */
    public static final String DIRECT_ARTICLE = "WIKI-ICON-1";

    /** Unknown article format image. */
    public static final String DIRECT_UNKNOWN = "DOCS-ICON-2";

    /** Screen image. */
    public static final String SCREEN = "DOCS-ICON-51";

    /** Article image. */
    public static final String INDIRECT_ARTICLE = "WIKI-ICON-101";

    /** Unknown article format image. */
    public static final String INDIRECT_UNKNOWN = "WIKI-ICON-102";

    /** Properties image. */
    public static final String PROPERTIES = "WIKI-ICON-201";

    /** Secure article image. */
    public static final String SECURE_DIRECT_ARTICLE = "WIKI-ICON-501";

    /** Secure unknown article format image. */
    public static final String SECURE_DIRECT_UNKNOWN = "DOCS-ICON-502";

    /** Secure screen image. */
    public static final String SECURE_SCREEN = "WIKI-ICON-551";

    /** Secure article image. */
    public static final String SECURE_INDIRECT_ARTICLE = "WIKI-ICON-601";

    /** Secure unknown article format image. */
    public static final String SECURE_INDIRECT_UNKNOWN = "WIKI-ICON-602";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private IconsDictionary() {
        // this class is only used for storing constants
    }
}

// end IconsDictionary
