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

import java.util.List;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;

/**
 * AbstractTopTenArticlesViewBlock is the class describing and printing the top
 * 10 articles.
 * 
 * @version $Id: AbstractTopTenArticlesViewBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractTopTenArticlesViewBlock extends
        AbstractArticlesResultsBlock implements Cloneable {
    /**
     * Maximum number of rows to print.
     */
    protected int numRows = 10;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractTopTenArticlesViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractArticlesResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractTopTenArticlesViewBlock) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * initialize the standard results block
         */
        super.initStandardBlock();

        /*
         * disable the navigation print
         */
        this.setIsNavigationPrinted(false);
    }

    /**
     * Returns the list of articles to print. Overrides
     * AbstractArticlesResultsBlock.getArticlesFound.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    @Override
    protected List getArticlesFound() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * build the list of articles
         */
        List<StandardArticle> articlesFound = sdf.findEnabledByRank(10);

        /*
         * return
         */
        return articlesFound;
    }
}

// end AbstractTopTenArticlesViewBlock
