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
 * PagesDictionary is the class storing all identifier of pages used in the
 * program. Each constant refers to an id in the page table
 * 
 * @version $Id: PagesDictionary.java,v 1.5 2006/04/01 20:48:07 tdanard Exp $
 */
public final class PagesDictionary {
    /** Advanced search results page. */
    public static final String CONTENTS_ADVANCED_SEARCH_RESULTS = "WIKI-PAGE-5";

    /** My content requests page. */
    public static final String MY_CONTENT_REQUESTS = "WIKI-PAGE-6";

    /** Goto article page. */
    public static final String GOTO_ARTICLE = "WIKI-PAGE-11";

    /** New article page. */
    public static final String NEW_ARTICLE = "WIKI-PAGE-16";

    /** Goto image page. */
    public static final String GOTO_IMAGE = "WIKI-PAGE-18";

    /** Add image page. */
    public static final String UPLOAD_IMAGE_TO_ADD = "WIKI-PAGE-19";

    /** Content request download image page. */
    public static final String CONTENT_REQUEST_DOWNLOAD_IMAGE = "WIKI-PAGE-22";

    /** Edit article page. */
    public static final String EDIT_ARTICLE_PAGE = "WIKI-PAGE-24";

    /** Edit image page. */
    public static final String EDIT_IMAGE_PAGE = "WIKI-PAGE-25";

    /** Edit image page. */
    public static final String UPLOAD_IMAGE_TO_EDIT = "WIKI-PAGE-26";

    /** Article properties page. */
    public static final String ARTICLE_PROPERTIES = "WIKI-PAGE-39";

    /** Image properties page. */
    public static final String IMAGE_PROPERTIES = "WIKI-PAGE-41";

    /** Article unsubscribe page. */
    public static final String ARTICLE_UNSUBSCRIBE = "WIKI-PAGE-44";

    /** Image unsubscribe page. */
    public static final String IMAGE_UNSUBSCRIBE = "WIKI-PAGE-45";

    /** My subscriptions page. */
    public static final String MY_SUBSCRIPTIONS = "WIKI-PAGE-48";

    /** Susbcriptions daily digest. */
    public static final String SUBSCRIPTIONS_DAILY_DIGEST = "WIKI-PAGE-60";

    /** Susbcriptions weekly digest. */
    public static final String SUBSCRIPTIONS_WEEKLY_DIGEST = "WIKI-PAGE-61";

    /** Article mark as hot. */
    public static final String ARTICLE_MARK_AS_HOT = "WIKI-PAGE-62";

    /** Image mark as hot. */
    public static final String IMAGE_MARK_AS_HOT = "WIKI-PAGE-63";

    /** Goto online help article page. */
    public static final String GOTO_ONLINE_HELP_ARTICLE = "WIKI-PAGE-66";

    /** Goto online help image page. */
    public static final String GOTO_ONLINE_HELP_IMAGE = "WIKI-PAGE-67";

    /** online help search results page. */
    public static final String ONLINE_HELP_SEARCH_RESULTS = "WIKI-PAGE-68";

    /** Add new group page. */
    public static final String ADD_NEW_GROUP = "WIKI-PAGE-179";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private PagesDictionary() {
        // this class is only used for storing constants
    }
}

// end PagesDictionary
