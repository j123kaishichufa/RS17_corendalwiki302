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
package com.corendal.netapps.wiki.patches;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.managers.AbstractAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ContentClassificationTypesDictionary;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersion;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersionFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardImageVersion;
import com.corendal.netapps.wiki.interfaces.StandardImageVersionFactory;
import com.corendal.netapps.wiki.utils.ArticleUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleVersionFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageVersionFactory;

/**
 * CustomAccessManager is the concrete implementation of AbstractAccessWriter.
 * In this implementation, contents with restricted access redirect to the login
 * page when necessary
 * 
 * @version $Id: CustomAccessManager.java,v 1.10 2007/10/17 21:29:23 tdanard Exp $
 */
public final class CustomAccessManager extends AbstractAccessManager implements
        Cloneable {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.patches.CustomAccessManager";

    /**
     * Default class constructor.
     */
    public CustomAccessManager() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractAccessManager.clone.
     */
    @Override
    public Object clone() {
        return (CustomAccessManager) super.clone();
    }

    /**
     * Returns true if a page requires authentication.
     * 
     * 
     * 
     * @return a boolean
     */
    @Override
    public boolean getIsAuthRequired(StandardPage page) {
        /*
         * use the standard rules
         */
        boolean result = super.getIsAuthRequired(page);

        if (result) {
            return true;
        }

        /*
         * get the message and the page factories
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardArticleVersionFactory sdvf = (StandardArticleVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleVersionFactory.class);
        StandardImageVersionFactory slvf = (StandardImageVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageVersionFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * initialize the content primary key
         */
        PrimaryKey contentPk = null;

        /*
         * get the article id, image id, article version id or image version id
         */
        String contentId = null;
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);

        if (!(StringUtil.getIsEmpty(articleId))) {
            contentId = articleId;
        }

        String imageId = rm.getParameter(HTTPParameterConstants.IMAGE_ID);

        if (!(StringUtil.getIsEmpty(imageId))) {
            contentId = imageId;
        }

        String articleVersionId = rm
                .getParameter(HTTPParameterConstants.ARTICLE_VERSION_ID);

        if (!(StringUtil.getIsEmpty(articleVersionId))) {
            PrimaryKey articleVersionPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(articleVersionId);
            StandardArticleVersion articleVersion = (StandardArticleVersion) sdvf
                    .findByPrimaryKey(articleVersionPk);

            if (articleVersion.getIsFound()) {
                contentId = articleVersion.getContentPrimaryKey().toString();
            }
        }

        String imageVersionId = rm
                .getParameter(HTTPParameterConstants.IMAGE_VERSION_ID);

        if (!(StringUtil.getIsEmpty(imageVersionId))) {
            PrimaryKey imageVersionPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(imageVersionId);
            StandardImageVersion imageVersion = (StandardImageVersion) slvf
                    .findByPrimaryKey(imageVersionPk);

            if (imageVersion.getIsFound()) {
                contentId = imageVersion.getContentPrimaryKey().toString();
            }
        }

        if (contentId != null) {
            contentPk = PrimaryKeyUtil.getAlphanumericSingleKey(contentId);
        }

        /*
         * get the article id
         */
        if (contentPk == null) {
            contentPk = ArticleUtil.getRequestedArticlePrimaryKey();
        }

        /*
         * get the associated content
         */
        if (contentPk != null) {
            StandardContent requestedContent = (StandardContent) srf
                    .findByPrimaryKey(contentPk);

            if (requestedContent.getIsFound()) {
                /*
                 * get the primary key of the "no login required" classification
                 */
                PrimaryKey noLoginRequiredPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentClassificationTypesDictionary.NO_LOGIN_REQUIRED);

                /*
                 * get the governing content
                 */
                Searched requestedSearched = requestedContent
                        .getClassificationSearched();

                /*
                 * verify that this content requires no login
                 */
                if (!(requestedSearched.getClassificationTypePrimaryKey()
                        .equals(noLoginRequiredPk))) {
                    return (this.getProxyStandardAccount() == null);
                }
            }
        }

        /*
         * return
         */
        return false;
    }
}

// end CustomAccessManager
