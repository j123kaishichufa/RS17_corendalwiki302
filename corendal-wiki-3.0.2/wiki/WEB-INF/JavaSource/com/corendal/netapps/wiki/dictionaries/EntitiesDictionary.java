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
 * EntitiesDictionary is the class storing all identifier of entities used in
 * the program. Each constant refers to an id in the entity table
 * 
 * @version $Id: EntitiesDictionary.java,v 1.3 2006/04/01 20:48:07 tdanard Exp $
 */
public final class EntitiesDictionary {
    /** Articles entity. */
    public static final String ARTICLES = "WIKI-ENTY-1";

    /** Images entity. */
    public static final String IMAGES = "WIKI-ENTY-3";

    /** Requests entity. */
    public static final String CONTENT_REQUESTS = "WIKI-ENTY-4";

    /** Contents entity. */
    public static final String CONTENTS = "WIKI-ENTY-5";

    /** Content info entity. */
    public static final String CONTENT_INFOS = "WIKI-ENTY-6";

    /** Content map entity. */
    public static final String CONTENT_MAPS = "WIKI-ENTY-7";

    /** Request status types entity. */
    public static final String REQUEST_STATUS_TYPES = "WIKI-ENTY-8";

    /** Content request types entity. */
    public static final String CONTENT_REQUEST_TYPES = "WIKI-ENTY-9";

    /** Content types entity. */
    public static final String CONTENT_TYPES = "WIKI-ENTY-10";

    /** Content access logs entity. */
    public static final String CONTENT_ACCESS_LOGS = "WIKI-ENTY-11";

    /** Article versions entity. */
    public static final String ARTICLE_VERSIONS = "WIKI-ENTY-12";

    /** Image versions entity. */
    public static final String IMAGE_VERSIONS = "WIKI-ENTY-13";

    /** Access days entity. */
    public static final String CONTENT_ACCESS_DAYS = "WIKI-ENTY-14";

    /** Access periods entity. */
    public static final String CONTENT_ACCESS_PERIODS = "WIKI-ENTY-15";

    /** Content subscriptions entity. */
    public static final String CONTENT_SUBSCRIPTIONS = "WIKI-ENTY-16";

    /** Content classification types entity. */
    public static final String CONTENT_CLASSIFICATION_TYPES = "WIKI-ENTY-17";

    /** Content subscription modes entity. */
    public static final String CONTENT_SUBSCRIPTION_MODES = "WIKI-ENTY-18";

    /** Content subscription types entity. */
    public static final String CONTENT_SUBSCRIPTION_TYPES = "WIKI-ENTY-19";

    /** Content rule types entity. */
    public static final String CONTENT_RULE_TYPES = "WIKI-ENTY-20";

    /** Content bodies entity. */
    public static final String CONTENT_BODIES = "WIKI-ENTY-21";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private EntitiesDictionary() {
        // this class is only used for storing constants
    }
}

// end EntitiesDictionary
