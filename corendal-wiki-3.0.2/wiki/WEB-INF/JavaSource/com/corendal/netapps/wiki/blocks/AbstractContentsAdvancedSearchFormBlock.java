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
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.CaseUtil;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.BlockUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;

/**
 * AbstractContentsAdvancedSearchFormBlock is the abstract block printing a form
 * allowing the advanced search of contents.
 * 
 * @version $Id: AbstractContentsAdvancedSearchFormBlock.java,v 1.1 2005/09/06
 *          21:25:27 tdanard Exp $
 */
public abstract class AbstractContentsAdvancedSearchFormBlock extends
        AbstractContentsAdvancedSearchBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead
     */
    protected AbstractContentsAdvancedSearchFormBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentsAdvancedSearchBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentsAdvancedSearchFormBlock) super.clone();
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
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * get the entity
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
        StandardEntity se = (StandardEntity) sef.findByPrimaryKey(entityPk);
        this.setStandardEntity(se);

        /*
         * set the page to call when the form is submitted
         */
        PrimaryKey advancedSearchResultsPagePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.CONTENTS_ADVANCED_SEARCH_RESULTS);
        StandardPage actionPage = (StandardPage) spf
                .findByPrimaryKey(advancedSearchResultsPagePk);
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
         * perform the regular validation
         */
        super.validateExtra();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * get the entity of the search form
         */
        PrimaryKey entityPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        StandardEntity entity = (StandardEntity) sef.findByPrimaryKey(entityPk);

        /*
         * find the list of currently selected block ids
         */
        List<PrimaryKey> currentSelectedBlockPrimaryKeys = BlockUtil
                .getCurrentSelectedBlockPrimaryKeys();

        /*
         * verify that the more results tab is not selected
         */
        PrimaryKey moreResultsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(BlocksDictionary.CONTENT_ADVANCED_MORE_RESULTS);

        if (!(currentSelectedBlockPrimaryKeys.contains(moreResultsPk))) {
            /*
             * verify whether no simple results were found but additional
             * results were found
             */
            List additionalResults = this.getAdditionalResults();
            int additionalSize = BlockUtil
                    .getSiteKeywordResultsSize(additionalResults);

            if (additionalSize > 0) {
                List simpleResults = this.getResults();
                int simpleSize = BlockUtil
                        .getSiteKeywordResultsSize(simpleResults);

                if (simpleSize == 0) {
                    /*
                     * get the x articles found message
                     */
                    String numEntitiesFoundText = CaseUtil
                            .getLowerCaseDeleteAccents(entity
                                    .getCountNameText(additionalSize));
                    String numEntitiesFoundEncoded = CaseUtil
                            .getLowerCaseDeleteAccents(entity
                                    .getCountNameEncoded(additionalSize));
                    String numEntitiesFoundHTML = CaseUtil
                            .getLowerCaseDeleteAccents(entity
                                    .getCountNameHTML(additionalSize));

                    /*
                     * get the more results exist message
                     */
                    PrimaryKey moreResultsExistPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.INF_MORE_RESULTS_EXIST);
                    StandardMessage moreResultsExist = (StandardMessage) smf
                            .findByPrimaryKey(moreResultsExistPk);

                    /*
                     * replace the placeholder
                     */
                    moreResultsExist
                            .replaceMessageText(numEntitiesFoundText, 1);
                    moreResultsExist.replaceMessageEncoded(
                            numEntitiesFoundEncoded, 1);
                    moreResultsExist
                            .replaceMessageHTML(numEntitiesFoundHTML, 1);

                    /*
                     * print that message
                     */
                    moreResultsExist.printBufferWithIconHTML();
                }
            }
        }
    }
}

// end AbstractContentsAdvancedSearchFormBlock
