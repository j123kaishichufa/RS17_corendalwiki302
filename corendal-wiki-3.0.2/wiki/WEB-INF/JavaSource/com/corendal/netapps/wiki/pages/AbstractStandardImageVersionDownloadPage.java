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

import com.corendal.netapps.framework.core.dictionaries.PagesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFile;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFileFactory;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardbeans.AbstractStandardPage;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardStoredFileFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.interfaces.StandardImageVersion;
import com.corendal.netapps.wiki.interfaces.StandardImageVersionDownloadPage;
import com.corendal.netapps.wiki.interfaces.StandardImageVersionFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageVersionFactory;

/**
 * AbstractStandardImageVersionDownloadPage is the abstract class handling
 * information about each imageVersion download page of the application.
 * 
 * @version $Id: AbstractStandardImageVersionDownloadPage.java,v 1.1 2005/09/06
 *          21:25:33 tdanard Exp $
 */
public abstract class AbstractStandardImageVersionDownloadPage extends
        AbstractStandardPage implements Cloneable,
        StandardImageVersionDownloadPage {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractStandardImageVersionDownloadPage() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardPage.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardImageVersionDownloadPage) super.clone();
    }

    /**
     * Returns the standard image version to be viewed.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardImageVersion object
     */
    public StandardImageVersion getViewedImageVersion() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardImageVersionFactory sdocf = (StandardImageVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageVersionFactory.class);
        StandardImageFactory sdf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);

        /*
         * get the image version
         */
        StandardImageVersion imageVersion = (StandardImageVersion) sdocf
                .findByPrimaryKey(this.getRecordPrimaryKey());

        /*
         * return
         */
        if (imageVersion.getIsFound()) {
            PrimaryKey imagePk = imageVersion.getContentPrimaryKey();
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(imagePk);

            if ((image.getIsFound()) && (image.getIsVisible())) {
                return imageVersion;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the primary data parameter of the record of this image version
     * download page.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getRecordPrimaryKey() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * return
         */
        String imageVersionId = rm.getParameter(this
                .getRecordPrimaryDataParameter());

        return PrimaryKeyUtil.getAlphanumericSingleKey(imageVersionId);
    }

    /**
     * Returns the primary data parameter of the record of this image version
     * download page.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public String getRecordPrimaryDataParameter() {
        return HTTPParameterConstants.IMAGE_VERSION_ID;
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
        StandardImageFactory sdf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);

        /*
         * get the managers used in alc procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentHistoryManager hm = (ContentHistoryManager) pms
                .getManager(DefaultContentHistoryManager.class);

        /*
         * get the image version being viewed
         */
        StandardImageVersion imageVersion = this.getViewedImageVersion();

        /*
         * redirect to "not found" page when the image version was not found
         */
        if (imageVersion != null) {
            /*
             * get the associated image
             */
            PrimaryKey imagePk = imageVersion.getContentPrimaryKey();
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(imagePk);

            /*
             * redirect to the "forbidden" page when the image cannot be
             * accessed
             */
            if (image.getIsAccessible()) {
                /*
                 * find the associated file
                 */
                PrimaryKey storedFilePk = imageVersion.getFilePrimaryKey();
                StandardStoredFile storedFile = (StandardStoredFile) ssff
                        .findByPrimaryKey(storedFilePk);

                /*
                 * redirect to "not found" page when the file was not found
                 */
                if (storedFile.getIsFound()) {
                    /*
                     * add the content to the history
                     */
                    hm.add(imageVersion.getContentPrimaryKey());

                    /*
                     * streams the stored file
                     */
                    storedFile.stream(storedFile.getLastEntryLogTimestamp());
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

// end AbstractStandardImageVersionDownloadPage
