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

import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;

/**
 * AbstractWhatsNewViewBlock is the class describing and printing all content
 * requests of a requestor.
 * 
 * @version $Id: AbstractWhatsNewViewBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractWhatsNewViewBlock extends
        AbstractContentsComplexResultsBlock implements Cloneable {
    /** Number of rows to print. */
    protected int numRowsPrinted = 5;

    /** Maximum number of unclassified rows to try. */
    protected int maxUnclassifiedRowsTried = 10;

    /** Maximum number of whats new rows to try. */
    protected int maxWhatsNewRowsTried = 25;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractWhatsNewViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractArticlesResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractWhatsNewViewBlock) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * initialize the standard foundArticless block
         */
        super.initStandardBlock();

        /*
         * disable the navigation print
         */
        this.setIsNavigationPrinted(false);
    }

    /**
     * Returns the list of articles to print.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    @Override
    protected List getContentsFound() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * initialize the found articles
         */
        List<StandardArticle> foundArticles = new ArrayList<StandardArticle>();

        /*
         * get the last 10 articles
         */
        List<PrimaryKey> recentUnclassified = sdf
                .findEnabledPrimaryKeysByCreationOrder(this.maxUnclassifiedRowsTried);

        /*
         * verify the existence and accessibility of these articles
         */
        int currentSize = foundArticles.size();

        int recentUnclassifiedSize = recentUnclassified.size();
        int j = 0;

        while ((j < recentUnclassifiedSize)
                && (currentSize < this.numRowsPrinted)) {
            PrimaryKey currentPk = (PrimaryKey) recentUnclassified.get(j);
            StandardArticle currentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(currentPk);

            if ((currentArticle.getIsFound())
                    && (currentArticle.getIsAccessible())) {
                foundArticles.add(currentArticle);
                currentSize = currentSize + 1;
            }

            j = j + 1;
        }

        /*
         * add the what's new articles
         */
        if (currentSize < this.numRowsPrinted) {
            /*
             * get the list of whats new articles
             */
            List<PrimaryKey> whatsNewPrimaryKeys = sdf
                    .findEnabledPrimaryKeysByWhatsNewFlag("Y");
            whatsNewPrimaryKeys.removeAll(recentUnclassified);

            /*
             * verify the accessibility of each article
             */
            int currentNumRowsFetched = 0;
            int currentWhatsNewRowsTried = 0;
            int nextIndex = 0;

            while (currentSize < this.numRowsPrinted) {
                /*
                 * expand the number of rows tried
                 */
                currentNumRowsFetched = currentNumRowsFetched
                        + (this.numRowsPrinted * 10);

                /*
                 * get the list of articles for that number of rows fetched
                 */
                List<PrimaryKey> recentClassified = sdf
                        .findEnabledPrimaryKeysByCreationOrder(currentNumRowsFetched);
                int recentClassifiedSize = recentClassified.size();

                /*
                 * only use the what's new articles
                 */
                recentClassified.retainAll(whatsNewPrimaryKeys);

                int retainedSize = recentClassified.size();

                /*
                 * add the new found articles
                 */
                int i = nextIndex;

                while ((i < retainedSize)
                        && (currentSize < this.numRowsPrinted)
                        && (currentWhatsNewRowsTried < this.maxWhatsNewRowsTried)) {
                    PrimaryKey currentPk = recentClassified.get(i);
                    StandardArticle currentArticle = (StandardArticle) sdf
                            .findByPrimaryKey(currentPk);

                    if (currentArticle.getIsAccessible()) {
                        foundArticles.add(currentArticle);
                        currentSize = currentSize + 1;
                    }

                    i = i + 1;
                    nextIndex = i;
                    currentWhatsNewRowsTried = currentWhatsNewRowsTried + 1;
                }

                /*
                 * exit when no more article could be found
                 */
                if (recentClassifiedSize < currentNumRowsFetched) {
                    currentSize = this.numRowsPrinted;
                }

                /*
                 * exit when the maximum number of whats new articles has been
                 * reached
                 */
                if (currentWhatsNewRowsTried == this.maxWhatsNewRowsTried) {
                    currentSize = this.numRowsPrinted;
                }
            }
        }

        /*
         * return
         */
        return foundArticles;
    }
}

// end AbstractWhatsNewViewBlock
