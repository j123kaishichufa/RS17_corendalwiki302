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
package com.corendal.netapps.wiki.pages;

import java.io.IOException;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.dictionaries.PagesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.BufferManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFile;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFileFactory;
import com.corendal.netapps.framework.core.managers.DefaultBufferManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardbeans.AbstractStandardPage;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardStoredFileFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageDownloadPage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.utils.ImageUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractStandardImageDownloadPage is the abstract class handling information
 * about each image download page of the application.
 * 
 * @version $Id: AbstractStandardImageDownloadPage.java,v 1.1 2005/09/06
 *          21:25:33 tdanard Exp $
 */
public abstract class AbstractStandardImageDownloadPage extends
        AbstractStandardPage implements Cloneable, StandardImageDownloadPage {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractStandardImageDownloadPage() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardPage.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardImageDownloadPage) super.clone();
    }

    /**
     * Returns the standard image to be viewed.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardImage object
     */
    public StandardImage getViewedImage() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardImageFactory sdocf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);

        /*
         * get the image
         */
        StandardImage image = (StandardImage) sdocf.findByPrimaryKey(this
                .getRecordPrimaryKey());

        /*
         * return
         */
        if ((image.getIsFound()) && (image.getIsVisible())) {
            return image;
        } else {
            return null;
        }
    }

    /**
     * Returns the primary key of the record of this image download page.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getRecordPrimaryKey() {
        return ImageUtil.getRequestedImagePrimaryKey();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardPage#print(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void print() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);
        StandardStoredFileFactory ssff = (StandardStoredFileFactory) pfs
                .getStandardBeanFactory(DefaultStandardStoredFileFactory.class);
        StandardMessageFactory smsgf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * get the managers used in alc procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentHistoryManager hm = (ContentHistoryManager) pms
                .getManager(DefaultContentHistoryManager.class);
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        BufferManager bm = (BufferManager) pms
                .getManager(DefaultBufferManager.class);

        /*
         * get the image being viewed
         */
        StandardImage image = this.getViewedImage();

        /*
         * redirect to "not found" page when the image was not found
         */
        if (image != null) {
            /*
             * redirect to the "forbidden" page when the image cannot be
             * accessed
             */
            if (image.getIsAccessible()) {
                /*
                 * find the associated file
                 */
                PrimaryKey storedFilePk = image.getFilePrimaryKey();
                StandardStoredFile storedFile = (StandardStoredFile) ssff
                        .findByPrimaryKey(storedFilePk);

                /*
                 * redirect to "not found" page when the file was not found
                 */
                if (storedFile.getIsFound()) {
                    /*
                     * add the content to the history
                     */
                    hm.add(image.getPrimaryKey());

                    /*
                     * verify the version number if requested
                     */
                    String checkVersionNumber = rm
                            .getParameter(HTTPParameterConstants.CHECK_VERSION_NUMBER);

                    if (!(StringUtil.getIsEmpty(checkVersionNumber))) {
                        int checkVersionNumberI = 0;

                        try {
                            checkVersionNumberI = Integer
                                    .parseInt(checkVersionNumber);
                        } catch (NumberFormatException e) {
                            // do not perform a version check if version number
                            // is incorrect
                        }

                        if (checkVersionNumberI != image.getVersionNum()) {
                            /*
                             * get the message
                             */
                            PrimaryKey validationPk = PrimaryKeyUtil
                                    .getAlphanumericSingleKey(MessagesDictionary.INF_CHECK_IMAGE_VERSION);
                            StandardMessage validation = (StandardMessage) smsgf
                                    .findByPrimaryKey(validationPk);

                            /*
                             * insert the version numbers
                             */
                            validation.replaceMessageText(""
                                    + checkVersionNumberI, 1);
                            validation.replaceMessageEncoded(""
                                    + checkVersionNumberI, 1);
                            validation.replaceMessageHTML(""
                                    + checkVersionNumberI, 1);

                            validation.replaceMessageText(""
                                    + image.getVersionNum(), 2);
                            validation.replaceMessageEncoded(""
                                    + image.getVersionNum(), 2);
                            validation.replaceMessageHTML(""
                                    + image.getVersionNum(), 2);

                            /*
                             * print the message on the next page
                             */
                            bm.startPageMessage();
                            validation.printBufferWithIconHTML();
                            bm.endPageMessage();

                            /*
                             * redirect
                             */
                            String redirectURL = image
                                    .getPropertiesAbsoluteLocation();
                            redirectURL = HTTPUtil
                                    .getAddedParameterURL(
                                            redirectURL,
                                            com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                            BlocksDictionary.IMAGE_VERSIONS);
                            redirectURL = HTTPUtil
                                    .getAddedParameterURL(
                                            redirectURL,
                                            HTTPParameterConstants.CHECK_VERSION_NUMBER,
                                            String.valueOf(checkVersionNumberI));
                            ((PortletOrServletLogicContext) AnyLogicContextGlobal
                                    .get())
                                    .commitAndSendAbsoluteRedirect(redirectURL);
                        }
                    }

                    /*
                     * streams the stored file
                     */
                    storedFile.stream(image.getLastEntryLogTimestamp());
                } else {
                    /*
                     * find the not found page
                     */
                    PrimaryKey notFoundPagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(PagesDictionary.NOT_FOUND);
                    StandardPage notFoundPage = (StandardPage) spf
                            .findByPrimaryKey(notFoundPagePk);

                    /*
                     * forward
                     */
                    String redirectURL = notFoundPage.getRelativeLocation();
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendRelativeForward(redirectURL);
                }
            } else {
                /*
                 * find the forbidden page
                 */
                PrimaryKey forbiddenPagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(PagesDictionary.FORBIDDEN);
                StandardPage forbiddenPage = (StandardPage) spf
                        .findByPrimaryKey(forbiddenPagePk);

                /*
                 * forward
                 */
                String redirectURL = forbiddenPage.getRelativeLocation();
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendRelativeForward(redirectURL);
            }
        } else {
            /*
             * find the not found page
             */
            PrimaryKey notFoundPagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.NOT_FOUND);
            StandardPage notFoundPage = (StandardPage) spf
                    .findByPrimaryKey(notFoundPagePk);

            /*
             * forward
             */
            String redirectURL = notFoundPage.getRelativeLocation();
            ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                    .commitAndSendRelativeForward(redirectURL);
        }
    }
}

// end AbstractStandardImageDownloadPage
