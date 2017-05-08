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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.core.blocks.AbstractKeywordSearchBlock;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.utils.ArticleUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;

/**
 * AbstractScreenKeywordSearchFormBlock is the abstract block printing and
 * handling a form allowing the search for online help using keywords.
 * 
 * @version $Id: AbstractOnlineHelpKeywordSearchFormBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractOnlineHelpKeywordSearchFormBlock extends
        AbstractKeywordSearchBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractOnlineHelpKeywordSearchFormBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractKeywordSearchBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractOnlineHelpKeywordSearchFormBlock) super.clone();
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
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        StandardEntity se = (StandardEntity) sef.findByPrimaryKey(entityPk);
        this.setStandardEntity(se);

        /*
         * set the page to call when the form is submitted
         */
        PrimaryKey actionPagePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.ONLINE_HELP_SEARCH_RESULTS);
        StandardPage actionPage = (StandardPage) spf
                .findByPrimaryKey(actionPagePk);
        this.setActionPage(actionPage);

        /*
         * build the layout
         */
        if (this.getIsBodyPrinted()) {
            this.build();
        }

        /*
         * close the definition
         */
        this.wrapUp();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardFormBlock#validateExtra(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void validateExtra() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory scf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardArticleFactory saf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the online help article
         */
        StandardArticle onlineHelp = ArticleUtil.getOnlineHelpStandardArticle();

        /*
         * verify that the article could be found
         */
        if (onlineHelp.getIsFound()) {
            /*
             * get the list of online help article primary keys
             */
            List<PrimaryKey> contentPrimaryKeys = scf
                    .findRecursiveEnabledPrimaryKeysByParentArticlePrimaryKey(onlineHelp
                            .getPrimaryKey());
            contentPrimaryKeys.add(onlineHelp.getPrimaryKey());

            /*
             * build the list of results
             */
            List<PrimaryKey> articlePrimaryKeys = saf
                    .findEnabledPrimaryKeysByDeepKeyword(this.getKeyword(),
                            false);

            /*
             * keep only the articles in both lists
             */
            articlePrimaryKeys.retainAll(contentPrimaryKeys);

            /*
             * turn the primary keys into articles
             */
            ArrayList<StandardArticle> result = new ArrayList<StandardArticle>();

            for (PrimaryKey currentArticlePk : articlePrimaryKeys) {
                StandardArticle currentArticle = (StandardArticle) saf
                        .findByPrimaryKey(currentArticlePk);
                result.add(currentArticle);
            }

            /*
             * set the list of results
             */
            this.setResults(result, result.size());
        } else {
            /*
             * set the list of results
             */
            this.setResults(new ArrayList<Object>(), 0);
        }
    }
}

// end AbstractOnlineHelpKeywordSearchFormBlock
