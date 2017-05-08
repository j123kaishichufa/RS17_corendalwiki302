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

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersion;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersionFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleVersionFactory;

/**
 * ArticleVersionUtil is the class for all articleVersion utilities.
 * 
 * @version $Id: ArticleVersionUtil.java,v 1.9 2007/09/03 19:42:01 tdanard Exp $
 */
public final class ArticleVersionUtil {
    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static methods should be used explicitly.
     */
    private ArticleVersionUtil() {
        // this class contains only static methods
    }

    /**
     * Returns the primary key of the articleVersion requested by the user.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public static final PrimaryKey getRequestedArticleVersionPrimaryKey() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleVersionFactory sdf = (StandardArticleVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleVersionFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get the article version id
         */
        String articleVersionId = rm
                .getParameter(HTTPParameterConstants.ARTICLE_VERSION_ID);

        if (!(StringUtil.getIsEmpty(articleVersionId))) {
            /*
             * find the requested article version
             */
            PrimaryKey articleVersionPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(articleVersionId);
            StandardArticleVersion requestedArticleVersion = (StandardArticleVersion) sdf
                    .findByPrimaryKey(articleVersionPk);

            /*
             * get the associated article
             */
            if (requestedArticleVersion.getIsFound()) {
                StandardArticle requestedArticle = requestedArticleVersion
                        .getStandardArticle();

                /*
                 * add the article id as virtual parameter
                 */
                rm.addVirtualParameter(HTTPParameterConstants.ARTICLE_ID,
                        requestedArticle.getPrimaryKey().toString());

                /*
                 * return
                 */
                return articleVersionPk;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
