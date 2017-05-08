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
package com.corendal.netapps.wiki.blocks;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardbeans.AbstractStandardBlock;
import com.corendal.netapps.framework.modelaccess.interfaces.NotCached;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.interfaces.ArticleVersionWriter;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersion;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersionFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.writers.DefaultArticleVersionWriter;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleVersionFactory;

/**
 * AbstractArticleVersionViewBlock is the class describing and printing all
 * contents of an articleVersion.
 * 
 * @version $Id: AbstractArticleVersionViewBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractArticleVersionViewBlock extends
        AbstractStandardBlock implements Cloneable, NotCached {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractArticleVersionViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentsComplexResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractArticleVersionViewBlock) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * initialize the account block
         */
        super.initStandardBlock();

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentHistoryManager hm = (ContentHistoryManager) pms
                .getManager(DefaultContentHistoryManager.class);

        /*
         * find the articleVersion being viewed
         */
        StandardArticleVersion articleVersion = this.getViewedArticleVersion();

        /*
         * add the article found to the history
         */
        if ((articleVersion != null) && (articleVersion.getIsFound())) {
            hm.add(articleVersion.getContentPrimaryKey());
        }
    }

    /**
     * Returns the standard articleVersion to be viewed.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardArticleVersion object
     */
    public StandardArticleVersion getViewedArticleVersion() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleVersionFactory sdocf = (StandardArticleVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleVersionFactory.class);

        /*
         * get the primary key of the requested article version
         */
        PrimaryKey articleVersionPk = this.getRecordPrimaryKey();

        if (articleVersionPk != null) {
            StandardArticleVersion articleVersion = (StandardArticleVersion) sdocf
                    .findByPrimaryKey(articleVersionPk);

            /*
             * get the associated article
             */
            if (articleVersion.getIsFound()) {
                StandardArticle article = articleVersion.getStandardArticle();

                if ((article.getIsFound()) && (article.getIsVisible())) {
                    return articleVersion;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordPrimaryKey(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
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
        String articleVersionId = rm.getParameter(this
                .getRecordPrimaryDataParameter());

        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(articleVersionId);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordPrimaryDataParameter(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordPrimaryDataParameter() {
        return HTTPParameterConstants.ARTICLE_VERSION_ID;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordName(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordName() {
        StandardArticleVersion sp = this.getViewedArticleVersion();

        if (sp != null) {
            return sp.getNameText();
        } else {
            return null;
        }
    }

    /**
     * Returns the primary key of the home article.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getHomeArticlePrimaryKey() {
        return PrimaryKeyUtil.getAlphanumericSingleKey(ArticlesDictionary.HOME);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#printBodyWithoutAuthenticationCheck(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void printBodyWithoutAuthenticationCheck() {
        /*
         * get the managers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        ArticleVersionWriter avw = (ArticleVersionWriter) pws
                .getWriter(DefaultArticleVersionWriter.class);

        /*
         * get the article
         */
        StandardArticleVersion articleVersion = this.getViewedArticleVersion();

        /*
         * print the body
         */
        avw.printBodyHTML(articleVersion, this.getHomeArticlePrimaryKey(),
                this, false);
    }
}

// end AbstractArticleVersionViewBlock
