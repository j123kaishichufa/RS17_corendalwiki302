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
import com.corendal.netapps.wiki.interfaces.ArticleWriter;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.writers.DefaultArticleWriter;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;

/**
 * AbstractArticleReadBlock is the class describing and printing all contents of
 * an article.
 * 
 * @version $Id: AbstractArticleReadBlock.java,v 1.1 2005/09/06 21:25:28 tdanard
 *          Exp $
 */
public abstract class AbstractArticleReadBlock extends AbstractStandardBlock
        implements Cloneable, NotCached {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractArticleReadBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentsComplexResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractArticleReadBlock) super.clone();
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
         * find the article being viewed
         */
        StandardArticle article = this.getViewedArticle();

        /*
         * add the article found to the history and the navigation elements
         */
        if (article != null) {
            /*
             * add the article found to the history
             */
            hm.add(article.getPrimaryKey());

            /*
             * change the name and short description of the block
             */
            this.setCurrentNameText(article.getNameText());
            this.setCurrentNameEncoded(article.getNameEncoded());
            this.setCurrentNameHTML(article.getNameHTML());
            this.setCurrentShortDescriptionText(article
                    .getShortDescriptionText());
            this.setCurrentShortDescriptionEncoded(article
                    .getShortDescriptionEncoded());
            this.setCurrentShortDescriptionHTML(article
                    .getShortDescriptionHTML());
        }
    }

    /**
     * Returns the standard article to be viewed.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardArticle object
     */
    public StandardArticle getViewedArticle() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdocf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the primary key of the requested article
         */
        PrimaryKey articlePk = this.getRecordPrimaryKey();

        if (articlePk != null) {
            StandardArticle article = (StandardArticle) sdocf
                    .findByPrimaryKey(articlePk);

            if ((article.getIsFound()) && (article.getIsVisible())) {
                return article;
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
        String articleId = rm
                .getParameter(this.getRecordPrimaryDataParameter());

        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(articleId);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordPrimaryDataParameter(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordPrimaryDataParameter() {
        return HTTPParameterConstants.ARTICLE_ID;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordName(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordName() {
        StandardArticle sp = this.getViewedArticle();

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
        ArticleWriter aw = (ArticleWriter) pws
                .getWriter(DefaultArticleWriter.class);

        /*
         * get the article
         */
        StandardArticle article = this.getViewedArticle();

        /*
         * print the body
         */
        aw.printBodyHTMLWithoutName(article, this.getHomeArticlePrimaryKey(),
                this, false);
    }
}

// end AbstractArticleReadBlock
