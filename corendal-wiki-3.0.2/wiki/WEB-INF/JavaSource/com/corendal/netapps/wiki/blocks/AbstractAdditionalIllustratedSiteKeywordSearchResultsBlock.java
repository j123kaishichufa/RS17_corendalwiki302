/*   
 * Corendal NetApps DocSide - Web application for lesson management   
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

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.dictionaries.EntitiesDictionary;
import com.corendal.netapps.framework.core.dictionaries.FieldsDictionary;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardSearchFormFieldFactory;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.Matched;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormField;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormFieldFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.utils.ArticleUtil;

/**
 * AbstractAdditionalIllustratedSiteKeywordSearchResultsBlock is the class
 * describing and printing all materials of a domain.
 * 
 * @version $Id:
 *          AbstractAdditionalIllustratedSiteKeywordSearchResultsBlock.java,v
 *          1.1 2005/09/06 19:48:00 tdanard Exp $
 */
public abstract class AbstractAdditionalIllustratedSiteKeywordSearchResultsBlock
        extends AbstractGenericSearchResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractAdditionalIllustratedSiteKeywordSearchResultsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractGenericSearchResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractAdditionalIllustratedSiteKeywordSearchResultsBlock) super
                .clone();
    }

    /*
     * @see com.corendal.netapps.wiki.blocks.AbstractGenericSearchResultsBlock#getSimpleOrAdditionalResultsNoRedirect()
     */
    @Override
    protected List getSimpleOrAdditionalResultsNoRedirect() {
        return this.getAdditionalResultsNoRedirect();
    }

    /*
     * @see com.corendal.netapps.wiki.blocks.AbstractGenericSearchResultsBlock#getSimpleOrAdditionalResultsCountNoRedirect()
     */
    @Override
    protected int getSimpleOrAdditionalResultsCountNoRedirect() {
        return this.getAdditionalResultsCountNoRedirect();
    }

    /*
     * @see com.corendal.netapps.framework.core.blocks.AbstractStandardSearchResultsBlock#getIsSearchResultsAddedToTitle()
     */
    @Override
    protected boolean getIsSearchResultsAddedToTitle() {
        return false;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getIsVisible(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public boolean getIsVisible() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardSearchFormFieldFactory ssfff = (StandardSearchFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardSearchFormFieldFactory.class);

        /*
         * get the keyword field
         */
        PrimaryKey siteKeywordSearchFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.SITE_KEYWORD_SEARCH);
        StandardSearchFormField siteKeywordSearchField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(siteKeywordSearchFieldPk);

        /*
         * get the keyword entered
         */
        String keyword = siteKeywordSearchField.getRequestValue();

        /*
         * return
         */
        return (!(StringUtil.getIsEmpty(keyword)))
                && (this.getAdditionalResultsNoRedirect().size() > 0);
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
         * sort the rows by descending score
         */
        this.sortResultsRows();
        this.reverseResultsRows();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * set the entity
         */
        PrimaryKey matchesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.MATCHES);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(matchesPk);
        this.setStandardEntity(entity);
    }

    /**
     * Returns the description to print.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    @Override
    protected String getCustomizedDescriptionText(Matched matched) {
        if (matched instanceof StandardArticle) {
            StandardArticle article = (StandardArticle) matched;

            return ArticleUtil.getCustomizedDescriptionText(article, this
                    .getAreDeepDescriptionsNeeded());
        } else {
            return super.getCustomizedDescriptionText(matched);
        }
    }

    /**
     * Returns the name to print in the HTML format.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    @Override
    protected String getCustomizedNameHTML(String customizedNameText) {
        return ArticleUtil
                .getCustomizedNameOrDescriptionHTML(customizedNameText);
    }

    /**
     * Returns the description to print in the HTML format.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    @Override
    protected String getCustomizedDescriptionHTML(
            String customizedDescriptionText) {
        return ArticleUtil
                .getCustomizedNameOrDescriptionHTML(customizedDescriptionText);
    }

}

// end AbstractAdditionalIllustratedSiteKeywordSearchResultsBlock
