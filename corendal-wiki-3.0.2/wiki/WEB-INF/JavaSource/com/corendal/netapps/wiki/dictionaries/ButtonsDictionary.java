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
 * ButtonsDictionary is the class storing all identifier of buttons used in the
 * program. Each constant refers to an id in the button table
 * 
 * @version $Id: ButtonsDictionary.java,v 1.3 2006/04/01 20:48:07 tdanard Exp $
 */
public final class ButtonsDictionary {
    /** Send add request button. */
    public static final String SEND_ADD_REQUEST = "WIKI-BUTN-1";

    /** Send remove request button. */
    public static final String SEND_REMOVE_REQUEST = "WIKI-BUTN-2";

    /** Approve request button. */
    public static final String APPROVE_REQUEST = "WIKI-BUTN-3";

    /** Reject request button. */
    public static final String REJECT_REQUEST = "WIKI-BUTN-4";

    /** Cancel request button. */
    public static final String CANCEL_REQUEST = "WIKI-BUTN-5";

    /** Update request button. */
    public static final String UPDATE_REQUEST = "WIKI-BUTN-6";

    /** Next for content edit button. */
    public static final String NEXT_FOR_CONTENT_EDIT = "WIKI-BUTN-7";

    /** Article reorganize button. */
    public static final String REORGANIZE_ARTICLE = "WIKI-BUTN-8";

    /** Send update image request button. */
    public static final String SEND_UPDATE_IMAGE_REQUEST = "WIKI-BUTN-10";

    /** Send update article request button. */
    public static final String SEND_UPDATE_ARTICLE_REQUEST = "WIKI-BUTN-11";

    /** Advanced search button. */
    public static final String ADVANCED_SEARCH = "WIKI-BUTN-12";

    /** Add subscription button. */
    public static final String ADD_SUBSCRIPTION = "WIKI-BUTN-13";

    /** Remove subscription button. */
    public static final String REMOVE_SUBSCRIPTION = "WIKI-BUTN-14";

    /** Sort contents alphabetically button. */
    public static final String SORT_CONTENTS_ALPHABETICALLY = "WIKI-BUTN-15";

    /** Add access group button. */
    public static final String ADD_ACCESS_GROUP = "WIKI-BUTN-16";

    /** Remove access group button. */
    public static final String REMOVE_ACCESS_GROUP = "WIKI-BUTN-17";

    /** Remove article version button. */
    public static final String REMOVE_ARTICLE_VERSION = "WIKI-BUTN-18";

    /** Remove image version button. */
    public static final String REMOVE_IMAGE_VERSION = "WIKI-BUTN-19";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private ButtonsDictionary() {
        // this class is only used for storing constants
    }
}

// end ButtonsDictionary
