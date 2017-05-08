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
 * FieldsDictionary is the class storing all identifier of fields used in the
 * program. Each constant refers to an id in the field table
 * 
 * @version $Id: FieldsDictionary.java,v 1.3 2006/04/01 20:48:07 tdanard Exp $
 */
public final class FieldsDictionary {
    /** Content name. */
    public static final String CONTENT_NAME = "WIKI-FILD-1";

    /** Content description. */
    public static final String CONTENT_DESCRIPTION = "WIKI-FILD-2";

    /** Content editor. */
    public static final String CONTENT_EDITOR = "WIKI-FILD-3";

    /** Content author. */
    public static final String CONTENT_AUTHOR = "WIKI-FILD-4";

    /** Content author. */
    public static final String CONTENT_FRIENDLY_ADDRESS = "WIKI-FILD-5";

    /** Content approve request. */
    public static final String CONTENT_APPROVE_REQUEST = "WIKI-FILD-6";

    /** Content file. */
    public static final String CONTENT_FILE = "WIKI-FILD-7";

    /** Content body. */
    public static final String CONTENT_BODY = "WIKI-FILD-8";

    /** Content path. */
    public static final String CONTENT_PATH = "WIKI-FILD-9";

    /** Content parent article. */
    public static final String CONTENT_PARENT_ARTICLE = "WIKI-FILD-10";

    /** Content request requestor. */
    public static final String CONTENT_REQUEST_REQUESTOR = "WIKI-FILD-11";

    /** Content request approver. */
    public static final String CONTENT_REQUEST_APPROVER = "WIKI-FILD-12";

    /** Content request type. */
    public static final String CONTENT_REQUEST_TYPE = "WIKI-FILD-13";

    /** Content request type. */
    public static final String CONTENT_REQUEST_STATUS = "WIKI-FILD-14";

    /** Content request referenced content. */
    public static final String CONTENT_REQUEST_REFERENCED_CONTENT = "WIKI-FILD-15";

    /** Content type. */
    public static final String CONTENT_TYPE = "WIKI-FILD-16";

    /** Content created. */
    public static final String CONTENT_CREATED = "WIKI-FILD-17";

    /** Content modified. */
    public static final String CONTENT_MODIFIED = "WIKI-FILD-18";

    /** Image size. */
    public static final String IMAGE_SIZE = "WIKI-FILD-19";

    /** Article size. */
    public static final String ARTICLE_SIZE = "WIKI-FILD-20";

    /** Content address. */
    public static final String CONTENT_ADDRESS = "WIKI-FILD-21";

    /** Content properties address. */
    public static final String CONTENT_PROPERTIES_ADDRESS = "WIKI-FILD-22";

    /** Content classification. */
    public static final String CONTENT_CLASSIFICATION = "WIKI-FILD-23";

    /** Content id. */
    public static final String CONTENT_ID = "WIKI-FILD-24";

    /** Content request created. */
    public static final String CONTENT_REQUEST_CREATED = "WIKI-FILD-25";

    /** Content request modified. */
    public static final String CONTENT_REQUEST_MODIFIED = "WIKI-FILD-26";

    /** Article version. */
    public static final String ARTICLE_VERSION = "WIKI-FILD-27";

    /** Image version. */
    public static final String IMAGE_VERSION = "WIKI-FILD-28";

    /** Content access day report date. */
    public static final String CONTENT_ACCESS_DAY_REPORT_DATE = "WIKI-FILD-29";

    /** Content access day access num. */
    public static final String CONTENT_ACCESS_DAY_ACCESS_NUM = "WIKI-FILD-30";

    /** Content access day rank num. */
    public static final String CONTENT_ACCESS_DAY_RANK_NUM = "WIKI-FILD-31";

    /** Content keyword. */
    public static final String CONTENT_KEYWORD = "WIKI-FILD-32";

    /** Content created from. */
    public static final String CONTENT_CREATED_FROM = "WIKI-FILD-33";

    /** Content created to. */
    public static final String CONTENT_CREATED_TO = "WIKI-FILD-34";

    /** Content modified from. */
    public static final String CONTENT_MODIFIED_FROM = "WIKI-FILD-35";

    /** Content modified to. */
    public static final String CONTENT_MODIFIED_TO = "WIKI-FILD-36";

    /** Content subscriber. */
    public static final String CONTENT_SUBSCRIBER = "WIKI-FILD-37";

    /** Content associate editors. */
    public static final String CONTENT_ASSOCIATE_EDITORS = "WIKI-FILD-38";

    /** Content subscription mode. */
    public static final String CONTENT_SUBSCRIPTION_MODE = "WIKI-FILD-39";

    /** Content subscription type. */
    public static final String CONTENT_SUBSCRIPTION_TYPE = "WIKI-FILD-40";

    /** Content subscriber last logged visit. */
    public static final String CONTENT_SUBSCRIBER_LAST_LOGGED_VISIT = "WIKI-FILD-41";

    /** Content subscriber type. */
    public static final String CONTENT_SUBSCRIBER_TYPE = "WIKI-FILD-42";

    /** Content subscriber mode. */
    public static final String CONTENT_SUBSCRIBER_MODE = "WIKI-FILD-43";

    /** Content deny access. */
    public static final String CONTENT_DENY_ACCESS = "WIKI-FILD-44";

    /** Content version notes. */
    public static final String CONTENT_VERSION_NOTES = "WIKI-FILD-45";

    /** Content rule. */
    public static final String CONTENT_RULE = "WIKI-FILD-46";

    /** Article version requestor. */
    public static final String ARTICLE_VERSION_REQUESTOR = "WIKI-FILD-47";

    /** Image version requestor. */
    public static final String IMAGE_VERSION_REQUESTOR = "WIKI-FILD-48";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private FieldsDictionary() {
        // this class is only used for storing constants
    }
}

// end FieldsDictionary
