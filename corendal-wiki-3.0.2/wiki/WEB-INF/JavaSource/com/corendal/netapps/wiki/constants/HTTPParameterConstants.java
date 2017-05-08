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
package com.corendal.netapps.wiki.constants;

/**
 * HTTPParameterConstants is the class storing all identifier of HTTP
 * parameters.
 * 
 * @version $Id: HTTPParameterConstants.java,v 1.1 2005/09/06 21:25:32 tdanard
 *          Exp $
 */

public class HTTPParameterConstants {
    /** Parameter specifying which article to print. */
    public static final String ARTICLE_ID = "articleid";

    /** Parameter specifying which article version to print. */
    public static final String ARTICLE_VERSION_ID = "articleversionid";

    /** Parameter specifying which content to print. */
    public static final String CONTENT_ID = "contentid";

    /** Parameter specifying which friendly address to print. */
    public static final String FRIENDLY_ADDRESS = "friendlyaddress";

    /** Parameter specifying which name to use. */
    public static final String NAME = "name";

    /** Parameter specifying which content access day to print. */
    public static final String CONTENT_ACCESS_DAY_ID = "contentaccessdayid";

    /** Parameter specifying which content classification type to print. */
    public static final String CONTENT_CLASSIFICATION_TYPE_ID = "contentclassificationtypeid";

    /** Parameter specifying which content request to print. */
    public static final String CONTENT_REQUEST_ID = "cntrequestid";

    /** Parameter specifying which content request type to print. */
    public static final String CONTENT_REQUEST_TYPE_ID = "contentrequesttypeid";

    /** Parameter specifying which content rule type to print. */
    public static final String CONTENT_RULE_TYPE_ID = "contentruletypeid";

    /** Parameter specifying which content subscription to print. */
    public static final String CONTENT_SUBSCRIPTION_ID = "ressubscriptionid";

    /** Parameter indicating the day a subscription report must be printed for. */
    public static final String DAY = "day";

    /** Parameter specifying which content classification type to print. */
    public static final String CONTENT_SUBSCRIPTION_MODE_ID = "contentsubscriptionmodeid";

    /** Parameter specifying which content classification type to print. */
    public static final String CONTENT_SUBSCRIPTION_TYPE_ID = "contentsubscriptiontypeid";

    /** Parameter specifying which content type to print. */
    public static final String CONTENT_TYPE_ID = "contenttypeid";

    /** Parameter specifying which image to print. */
    public static final String IMAGE_ID = "imageid";

    /** Parameter specifying which image version to print. */
    public static final String IMAGE_VERSION_ID = "imageversionid";

    /** Parameter specifying which request status type to print. */
    public static final String REQUEST_STATUS_TYPE_ID = "requeststatustypeid";

    /** Parameter specifying which version number to check. */
    public static final String CHECK_VERSION_NUMBER = "checkversionnumber";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static references should be used explicitly.
     */
    private HTTPParameterConstants() {
        // this class is only used for storing constants
    }

}
