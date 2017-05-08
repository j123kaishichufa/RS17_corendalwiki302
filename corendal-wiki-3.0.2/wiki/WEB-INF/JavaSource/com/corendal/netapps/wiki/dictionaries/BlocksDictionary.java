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
 * BlocksDictionary is the class storing all identifier of blocks used in the
 * program. Each constant refers to an id in the block table
 * 
 * @version $Id: BlocksDictionary.java,v 1.5 2007/06/11 14:30:11 tdanard Exp $
 */
public final class BlocksDictionary {
    /** Site keyword results. */
    public static final String SITE_KEYWORD_RESULTS = "WIKI-BLCK-32";

    /** Edit article block. */
    public static final String EDIT_ARTICLE = "WIKI-BLCK-38";

    /** Edit image block. */
    public static final String EDIT_IMAGE = "WIKI-BLCK-40";

    /** Article versions block. */
    public static final String ARTICLE_VERSIONS = "WIKI-BLCK-51";

    /** Image versions block. */
    public static final String IMAGE_VERSIONS = "WIKI-BLCK-52";

    /** Content advanced results block. */
    public static final String CONTENT_ADVANCED_RESULTS = "WIKI-BLCK-56";

    /** Article subscribers block. */
    public static final String ARTICLE_SUBSCRIBERS = "WIKI-BLCK-59";

    /** Image subscribers block. */
    public static final String IMAGE_SUBSCRIBERS = "WIKI-BLCK-60";

    /** Article access groups block. */
    public static final String ARTICLE_ACCESS_GROUPS = "WIKI-BLCK-77";

    /** Image access groups block. */
    public static final String IMAGE_ACCESS_GROUPS = "WIKI-BLCK-78";

    /** My daily subscriptions digest block. */
    public static final String MY_DAILY_SUBSCRIPTIONS_DIGEST = "WIKI-BLCK-101";

    /** My weekly subscriptions digest block. */
    public static final String MY_WEEKLY_SUBSCRIPTIONS_DIGEST = "WIKI-BLCK-102";

    /** Article whats related block. */
    public static final String ARTICLE_WHATS_RELATED = "WIKI-BLCK-115";

    /** Site keyword more results block. */
    public static final String SITE_KEYWORD_MORE_RESULTS = "WIKI-BLCK-117";

    /** Content advanced more results block. */
    public static final String CONTENT_ADVANCED_MORE_RESULTS = "WIKI-BLCK-118";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private BlocksDictionary() {
        // this class is only used for storing constants
    }
}

// end BlocksDictionary
