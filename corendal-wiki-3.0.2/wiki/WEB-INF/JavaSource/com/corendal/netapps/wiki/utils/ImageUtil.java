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
package com.corendal.netapps.wiki.utils;

import javax.servlet.http.HttpServletRequest;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.ServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardURL;
import com.corendal.netapps.framework.core.interfaces.StandardURLFactory;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardURLFactory;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.AlphanumericSingleKey;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.LDAPUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * ImageUtil is the class for all image utilities.
 * 
 * @version $Id: ImageUtil.java,v 1.16 2007/10/17 21:29:10 tdanard Exp $
 */
public final class ImageUtil {
    /** Slash character */
    private static final String STRING_SLASH = "/";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static methods should be used explicitly.
     */
    private ImageUtil() {
        // this class contains only static methods
    }

    /**
     * Returns the friendly address of the image to be viewed. Returns null when
     * not applicable.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getImageFriendlyAddress() {
        /*
         * find the friendly address requested
         */
        HttpServletRequest req = ((ServletLogicContext) AnyLogicContextGlobal
                .get()).getHttpServletRequest();
        String friendlyAddress = HTTPUtil.getPathInfoWithoutVariantPrefix(req);

        /*
         * decode dangerous characters
         */
        friendlyAddress = LDAPUtil.getDecodedURL(friendlyAddress);

        /*
         * remove any starting slash
         */
        if (friendlyAddress.startsWith(STRING_SLASH)) {
            friendlyAddress = friendlyAddress.substring(1);
        }

        /*
         * remove any trailing slash
         */
        if (friendlyAddress.endsWith(STRING_SLASH)) {
            friendlyAddress = friendlyAddress.substring(0, friendlyAddress
                    .length() - 1);
        }

        /*
         * return
         */
        return friendlyAddress;
    }

    /**
     * Returns the friendly address of the online help image to be viewed.
     * returns null when not applicable.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getOnlineHelpImageFriendlyAddress() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardURLFactory suf = (StandardURLFactory) pfs
                .getStandardBeanFactory(DefaultStandardURLFactory.class);

        /*
         * find the friendly address requested
         */
        HttpServletRequest req = ((ServletLogicContext) AnyLogicContextGlobal
                .get()).getHttpServletRequest();
        String friendlyAddress = HTTPUtil.getPathInfoWithoutVariantPrefix(req);

        /*
         * get the goto page
         */
        PrimaryKey gotoPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.GOTO_ONLINE_HELP_IMAGE);

        /*
         * get the URL of that page
         */
        StandardURL gotoURL = (StandardURL) suf
                .findEnabledByPagePrimaryKey(gotoPk);

        /*
         * decode dangerous characters
         */
        friendlyAddress = LDAPUtil.getDecodedURL(friendlyAddress);

        /*
         * remove the base
         */
        String base = gotoURL.getBase();

        if (friendlyAddress.startsWith(base)) {
            friendlyAddress = friendlyAddress.substring(base.length());
        }

        /*
         * remove any starting slash
         */
        if (friendlyAddress.startsWith(STRING_SLASH)) {
            friendlyAddress = friendlyAddress.substring(1);
        }

        /*
         * remove any trailing slash
         */
        if (friendlyAddress.endsWith(STRING_SLASH)) {
            friendlyAddress = friendlyAddress.substring(0, friendlyAddress
                    .length() - 1);
        }

        /*
         * return
         */
        return friendlyAddress;
    }

    /**
     * Returns the primary key of the image requested by the user.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public static final PrimaryKey getRequestedImagePrimaryKey() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get the image id
         */
        String imageId = rm.getParameter(HTTPParameterConstants.IMAGE_ID);

        /*
         * use the friendly address if no image id specified
         */
        if (StringUtil.getIsEmpty(imageId)) {
            /*
             * get the current page
             */
            StandardPage page = rm.getStandardPage();
            PrimaryKey pagePk = page.getPrimaryKey();

            /*
             * verify whether this is the goto page
             */
            PrimaryKey gotoPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.GOTO_IMAGE);

            if (gotoPk.equals(pagePk)) {
                /*
                 * test whether we are using a friendly address
                 */
                String friendlyAddress = ImageUtil.getImageFriendlyAddress();

                if (!(StringUtil.getIsEmpty(friendlyAddress))) {
                    /*
                     * get the factories of the instances used in this procedure
                     */
                    FactorySet pfs = AnyLogicContextGlobal.get()
                            .getFactorySet();
                    StandardImageFactory sdf = (StandardImageFactory) pfs
                            .getStandardBeanFactory(DefaultStandardImageFactory.class);

                    /*
                     * get the image for that friendly address
                     */
                    StandardImage sd = sdf
                            .findByFriendlyAddress(friendlyAddress);

                    if (sd.getIsFound()) {
                        /*
                         * add the image id as virtual parameter. This will
                         * allow this part of the code to be called only once
                         */
                        rm.addVirtualParameter(HTTPParameterConstants.IMAGE_ID,
                                ((AlphanumericSingleKey) sd.getPrimaryKey())
                                        .toString());

                        /*
                         * return
                         */
                        return sd.getPrimaryKey();
                    } else {
                        /*
                         * the associated image has been removed, let's create a
                         * phony primary key that will trigger the image to be
                         * "not found"
                         */
                        return PrimaryKeyUtil
                                .getAlphanumericSingleKey("Unknown friendly address: "
                                        + friendlyAddress);
                    }
                } else {
                    /*
                     * the associated image has been removed, let's create a
                     * phony primary key that will trigger the image to be "not
                     * found"
                     */
                    return PrimaryKeyUtil
                            .getAlphanumericSingleKey("Unknown friendly address: "
                                    + friendlyAddress);
                }
            } else {
                return null;
            }
        } else {
            return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(imageId);
        }
    }

    /**
     * Returns the primary key of the image requested by the user.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public static final PrimaryKey getRequestedOnlineHelpImagePrimaryKey() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get the image id
         */
        String imageId = rm.getParameter(HTTPParameterConstants.IMAGE_ID);

        /*
         * use the friendly address if no image id specified
         */
        if (StringUtil.getIsEmpty(imageId)) {
            /*
             * get the current page
             */
            StandardPage page = rm.getStandardPage();
            PrimaryKey pagePk = page.getPrimaryKey();

            /*
             * verify whether this is the goto page
             */
            PrimaryKey gotoPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.GOTO_ONLINE_HELP_IMAGE);

            if (gotoPk.equals(pagePk)) {
                /*
                 * test whether we are using a friendly address
                 */
                String friendlyAddress = ImageUtil
                        .getOnlineHelpImageFriendlyAddress();

                if (!(StringUtil.getIsEmpty(friendlyAddress))) {
                    /*
                     * get the factories of the instances used in this procedure
                     */
                    FactorySet pfs = AnyLogicContextGlobal.get()
                            .getFactorySet();
                    StandardImageFactory sdf = (StandardImageFactory) pfs
                            .getStandardBeanFactory(DefaultStandardImageFactory.class);

                    /*
                     * get the image for that friendly address
                     */
                    StandardImage sd = sdf
                            .findByFriendlyAddress(friendlyAddress);

                    if (sd.getIsFound()) {
                        /*
                         * add the image id as virtual parameter. This will
                         * allow this part of the code to be called only once
                         */
                        rm.addVirtualParameter(HTTPParameterConstants.IMAGE_ID,
                                ((AlphanumericSingleKey) sd.getPrimaryKey())
                                        .toString());

                        /*
                         * return
                         */
                        return sd.getPrimaryKey();
                    } else {
                        /*
                         * the associated image has been removed, let's create a
                         * phony primary key that will trigger the image to be
                         * "not found"
                         */
                        return PrimaryKeyUtil
                                .getAlphanumericSingleKey("Unknown friendly address: "
                                        + friendlyAddress);
                    }
                } else {
                    /*
                     * the associated image has been removed, let's create a
                     * phony primary key that will trigger the image to be "not
                     * found"
                     */
                    return PrimaryKeyUtil
                            .getAlphanumericSingleKey("Unknown friendly address: "
                                    + friendlyAddress);
                }
            } else {
                return null;
            }
        } else {
            return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(imageId);
        }
    }

    /**
     * Returns true if a location points to an image.
     * 
     * @param location
     *            location to query
     * 
     * 
     * @return a boolean
     */
    public static final boolean getIsImageLocation(String location) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the image goto page
         */
        PrimaryKey gotoPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.GOTO_IMAGE);
        StandardPage gotoPage = (StandardPage) spf.findByPrimaryKey(gotoPk);

        /*
         * get the location prefixes
         */
        if (gotoPage.getIsFound()) {
            String absoluteGotoArticlePrefix = gotoPage.getAbsoluteLocation();
            String relativeGotoArticlePrefix = gotoPage.getRelativeLocation();

            /*
             * return
             */
            return ((absoluteGotoArticlePrefix != null) && (location
                    .startsWith(absoluteGotoArticlePrefix)))
                    || ((relativeGotoArticlePrefix != null) && (location
                            .startsWith(relativeGotoArticlePrefix)));
        } else {
            return false;
        }
    }

    /**
     * Returns the friendly address embedded in an aticle location.
     * 
     * @param location
     *            location to query
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getFriendlyAddressFromLocation(String location) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the image goto page
         */
        PrimaryKey gotoPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.GOTO_IMAGE);
        StandardPage gotoPage = (StandardPage) spf.findByPrimaryKey(gotoPk);

        /*
         * verify that the page was found
         */
        if (gotoPage.getIsFound()) {
            /*
             * remove anything after the sharp sign
             */
            String modifiedLocation = location;
            int pos = modifiedLocation.indexOf("#");
            if (pos >= 0) {
                modifiedLocation = modifiedLocation.substring(0, pos);
            }

            /*
             * get the location prefixes
             */
            String absoluteGotoImagePrefix = gotoPage.getAbsoluteLocation();
            String relativeGotoImagePrefix = gotoPage.getRelativeLocation();

            /*
             * verify that the location requested starts with one of these
             * locations
             */
            if (modifiedLocation.startsWith(absoluteGotoImagePrefix)) {
                /*
                 * remove the prefix
                 */
                String friendlyAddress = modifiedLocation
                        .substring(absoluteGotoImagePrefix.length());

                /*
                 * remove the suffix
                 */
                if (friendlyAddress.endsWith(STRING_SLASH)) {
                    friendlyAddress = friendlyAddress.substring(0,
                            friendlyAddress.length() - 1);
                }

                /*
                 * decode the special characters
                 */
                friendlyAddress = LDAPUtil.getDecodedURL(friendlyAddress);

                /*
                 * return
                 */
                return ContentUtil.getCleanFriendlyAddress(friendlyAddress);
            } else if (modifiedLocation.startsWith(relativeGotoImagePrefix)) {
                /*
                 * remove the prefix
                 */
                String friendlyAddress = modifiedLocation
                        .substring(relativeGotoImagePrefix.length());

                /*
                 * remove the suffix
                 */
                if (friendlyAddress.endsWith(STRING_SLASH)) {
                    friendlyAddress = friendlyAddress.substring(0,
                            friendlyAddress.length() - 1);
                }

                /*
                 * decode the special characters
                 */
                friendlyAddress = LDAPUtil.getDecodedURL(friendlyAddress);

                /*
                 * return
                 */
                return ContentUtil.getCleanFriendlyAddress(friendlyAddress);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
