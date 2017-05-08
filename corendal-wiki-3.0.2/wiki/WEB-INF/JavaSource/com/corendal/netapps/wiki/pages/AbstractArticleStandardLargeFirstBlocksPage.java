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

import com.corendal.netapps.framework.core.dictionaries.ExtensionsDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardExtension;
import com.corendal.netapps.framework.core.interfaces.StandardExtensionFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLargeFirstBlocksPage;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.pages.AbstractStandardLargeFirstBlocksPage;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardExtensionFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.interfaces.ContentWriter;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.utils.ArticleUtil;
import com.corendal.netapps.wiki.writers.DefaultContentWriter;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;

/**
 * AbstractArticleStandardLargeFirstBlocksPage is the abstract class handling
 * information about each page made of blocks of the application. Large blocks
 * are printed first, small blocks are printed last.
 * 
 * @version $Id: AbstractArticleStandardLargeFirstBlocksPage.java,v 1.1
 *          2005/09/06 21:25:33 tdanard Exp $
 */
public abstract class AbstractArticleStandardLargeFirstBlocksPage extends
        AbstractStandardLargeFirstBlocksPage implements Cloneable,
        StandardLargeFirstBlocksPage {
    /** Primary key of the requested article */
    private PrimaryKey requestedArticlePk;

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractArticleStandardLargeFirstBlocksPage() {
        this.requestedArticlePk = ArticleUtil.getRequestedArticlePrimaryKey();
    }

    /**
     * Returns a clone. Overrides AbstractStandardLargeFirstBlocksPage.clone.
     */
    @Override
    public Object clone() {
        AbstractArticleStandardLargeFirstBlocksPage result = (AbstractArticleStandardLargeFirstBlocksPage) super
                .clone();

        if (this.requestedArticlePk != null) {
            result.requestedArticlePk = (PrimaryKey) this.requestedArticlePk
                    .clone();
        }

        return result;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlocksPage#printLargeBlockSets(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void printLargeBlockSets() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardExtensionFactory sef = (StandardExtensionFactory) pfs
                .getStandardBeanFactory(DefaultStandardExtensionFactory.class);
        StandardEntityFactory senf = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        ContentWriter dw = (ContentWriter) pws
                .getWriter(DefaultContentWriter.class);

        /*
         * get the HTML extension
         */
        PrimaryKey extHTMLPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ExtensionsDictionary.HTML);
        StandardExtension extHTML = (StandardExtension) sef
                .findByPrimaryKey(extHTMLPk);

        /*
         * get the extension requested by the user
         */
        StandardExtension extension = rm.getStandardExtension();

        /*
         * find the requested directory and print the path
         */
        if (extension.equals(extHTML)) {
            if (this.requestedArticlePk != null) {
                /*
                 * find the requested article
                 */
                StandardArticle requestedArticle = (StandardArticle) sdf
                        .findByPrimaryKey(this.requestedArticlePk);

                /*
                 * print the path
                 */
                if ((requestedArticle.getIsFound())
                        && (requestedArticle.getIsVisible())) {
                    /*
                     * find the current page
                     */
                    StandardPage currentPage = rm.getStandardPage();

                    /*
                     * find the articles entity
                     */
                    PrimaryKey articlesPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.ARTICLES);
                    StandardEntity articles = (StandardEntity) senf
                            .findByPrimaryKey(articlesPk);

                    /*
                     * find the page printing the article detail
                     */
                    PrimaryKey detailPagePk = articles
                            .getDetailPagePrimaryKey();

                    /*
                     * print the path
                     */
                    PrimaryKey currentPagePk = currentPage.getPrimaryKey();

                    if (currentPagePk.equals(detailPagePk)) {
                        dw.printPathHTMLNoLastLink(requestedArticle);
                    } else {
                        PrimaryKey gotoArticlePagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_ARTICLE);

                        if (currentPagePk.equals(gotoArticlePagePk)) {
                            dw.printPathHTMLNoLastLink(requestedArticle);
                        } else {
                            dw.printPathHTML(requestedArticle);
                        }
                    }
                }
            }
        }

        /*
         * print the rest of the block
         */
        super.printLargeBlockSets();
    }
}

// end AbstractArticleStandardLargeFirstBlocksPage
