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
 * AbstractContentReferencesBlock is the class describing and printing all
 * articles of an editor.
 * 
 * @version $Id: AbstractContentReferencesBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractContentReferencesBlock extends
        AbstractArticleResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractContentReferencesBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractArticleResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentReferencesBlock) super.clone();
    }

    /**
     * Returns the primary key of the article to view.
     * 
     * 
     * 
     * @return a PrimaryKey object
     */
    public abstract PrimaryKey getContentPrimaryKey();

    /**
     * Returns the list of articles to print. Overrides
     * AbstractArticleResultsBlock.getArticlesFound.
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
         * find the article being viewed
         */
        PrimaryKey articlePk = this.getContentPrimaryKey();

        /*
         * build the list of articles
         */
        List<StandardArticle> articlesFound;

        if (articlePk != null) {
            articlesFound = sdf
                    .findEnabledByReferencedContentPrimaryKey(articlePk);
        } else {
            articlesFound = new ArrayList<StandardArticle>();
        }

        /*
         * change the visibility
         */
        this.setIsVisible(articlesFound.size() > 0);

        /*
         * return
         */
        return articlesFound;
    }
}

// end AbstractContentReferencesBlock
