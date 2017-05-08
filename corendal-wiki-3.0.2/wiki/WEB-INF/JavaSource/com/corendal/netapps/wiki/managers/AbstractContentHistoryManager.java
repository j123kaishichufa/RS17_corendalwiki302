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
package com.corendal.netapps.wiki.managers;

import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.managers.AbstractHistoryManager;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.SessionAttributeConstants;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentAccessLogFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentAccessLogFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;

/**
 * AbstractContentHistoryManager is the abstract class managing the access
 * history to all contents.
 * 
 * @version $Id: AbstractContentHistoryManager.java,v 1.1 2005/09/06 21:25:36
 *          tdanard Exp $
 */
public abstract class AbstractContentHistoryManager extends
        AbstractHistoryManager implements Cloneable, ContentHistoryManager {
    /** Maximum history size */
    private static final int MAX_HISTORY_SIZE = 20;

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractContentHistoryManager() {
        this.setMaxHistorySize(MAX_HISTORY_SIZE);
        this.setHistoryAttribute(SessionAttributeConstants.CONTENTS_HISTORY);
    }

    /**
     * Returns a clone. Overrides AbstractHistoryManager.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentHistoryManager) super.clone();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentHistoryManager#getHistoryWithArticlesOnly(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> getHistoryWithArticlesOnly() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the history
         */
        List<PrimaryKey> contents = this.getHistory();
        List<PrimaryKey> articles = new ArrayList<PrimaryKey>();

        /*
         * get the article type primary key
         */
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        /*
         * add only articles
         */
        for (PrimaryKey contentPk : contents) {
            StandardContent currentContent = (StandardContent) srf
                    .findByPrimaryKey(contentPk);

            if (currentContent.getIsFound()) {
                if (currentContent.getTypePrimaryKey().equals(articleTypePk)) {
                    articles.add(contentPk);
                }
            }
        }

        /*
         * return
         */
        return articles;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentHistoryManager#getToppedHistoryWithArticlesOnly(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> getToppedHistoryWithArticlesOnly(
            PrimaryKey topArticlePk) {
        /*
         * get the history
         */
        List<PrimaryKey> articles = this.getHistoryWithArticlesOnly();
        List<PrimaryKey> toppedArticles = new ArrayList<PrimaryKey>();

        /*
         * add the top article
         */
        toppedArticles.add(topArticlePk);

        /*
         * add the other articles
         */
        for (PrimaryKey articlePk : articles) {

            if (!(articlePk.equals(topArticlePk))) {
                toppedArticles.add(articlePk);
            }
        }

        /*
         * return
         */
        return toppedArticles;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentHistoryManager#getCleanHistory(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> getCleanHistory(PrimaryKey articlePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the content type
         */
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        /*
         * get the article
         */
        StandardArticle article = (StandardArticle) sdf
                .findByPrimaryKey(articlePk);

        /*
         * get the history
         */
        List<PrimaryKey> contents = this.getHistory();
        List<PrimaryKey> cleanContents = new ArrayList<PrimaryKey>();

        /*
         * verify each content is not the article itself. verify that each
         * content is not already in the article. verify that each content is
         * not a recursive parent article
         */
        for (PrimaryKey contentPk : contents) {

            if (!(contentPk.equals(articlePk))) {
                StandardContent currentContent = (StandardContent) srf
                        .findByPrimaryKey(contentPk);

                if (currentContent.getIsFound()) {
                    if (!(currentContent.getIsChildOf(articlePk))) {
                        if (articleTypePk.equals(currentContent
                                .getTypePrimaryKey())) {
                            StandardArticle currentArticle = (StandardArticle) sdf
                                    .findByPrimaryKey(contentPk);

                            if (!(currentArticle
                                    .getIsRecursiveDirectParentOf(article))) {
                                cleanContents.add(contentPk);
                            }
                        } else {
                            cleanContents.add(contentPk);
                        }
                    }
                }
            }
        }

        /*
         * return
         */
        return cleanContents;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.HistoryManager#add(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void add(PrimaryKey pk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentAccessLogFactory srf = (StandardContentAccessLogFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentAccessLogFactory.class);

        /*
         * get the primary key of the home page
         */
        PrimaryKey homePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ArticlesDictionary.HOME);

        /*
         * verify that this is not the home page
         */
        if (!(pk.equals(homePk))) {
            /*
             * create the content access log only if content is not in the
             * recent history
             */
            List<PrimaryKey> primaryKeys = this.getHistory();
            int pos = primaryKeys.indexOf(pk);

            if (pos == -1) {
                srf.create(pk);
            }

            /*
             * add the primary key to the history
             */
            super.add(pk);
        }
    }
}
