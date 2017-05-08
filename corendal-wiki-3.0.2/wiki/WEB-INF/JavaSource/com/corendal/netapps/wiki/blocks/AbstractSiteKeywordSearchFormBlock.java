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
import com.corendal.netapps.framework.core.constants.HTTPParameterConstants;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.utils.BlockUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.LocationUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.utils.ContentUtil;

/**
 * AbstractSiteKeywordSearchFormBlock is the class describing and printing all
 * contents of an article.
 * 
 * @version $Id: AbstractSiteKeywordSearchFormBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractSiteKeywordSearchFormBlock
        extends
        com.corendal.netapps.framework.core.blocks.AbstractSiteKeywordSearchFormBlock
        implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractSiteKeywordSearchFormBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractSiteKeywordSearchFormBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractSiteKeywordSearchFormBlock) super.clone();
    }

    /**
     * Filters out any unwanted search result.
     * 
     * @return a java.util.List object
     */
    @Override
    protected List getFilteredResults(List currentResults) {
        return ContentUtil.getFilteredContents(currentResults);
    }

    /**
     * Returns true if the additional keyword search should be performed.
     * Overrides
     * com.corendal.netapps.framework.core.blocks.AbstractSiteKeywordSearchFormBlock.getIsAdditionalKeywordSearchPerformed
     * 
     * @return a boolean
     */
    @Override
    protected boolean getIsAdditionalKeywordSearchPerformed() {
        return true;
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
         * count the number of results
         */
        List additionalResults = this.getAdditionalResults();
        int additionalResultsSize = BlockUtil
                .getSiteKeywordResultsSize(additionalResults);
        List simpleResults = this.getResults();
        int simpleResultsSize = BlockUtil
                .getSiteKeywordResultsSize(simpleResults);

        /*
         * verify that the more results tab is not selected
         */
        PrimaryKey resultsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(BlocksDictionary.SITE_KEYWORD_RESULTS);
        PrimaryKey moreResultsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(BlocksDictionary.SITE_KEYWORD_MORE_RESULTS);

        if (!(currentSelectedBlockPrimaryKeys.contains(moreResultsPk))) {
            /*
             * verify whether no simple results were found but additional
             * results were found
             */
            if (additionalResultsSize > 0) {

                if (simpleResultsSize == 0) {
                    /*
                     * get the x articles found message
                     */
                    String numEntitiesFoundText = CaseUtil
                            .getLowerCaseDeleteAccents(entity
                                    .getCountNameText(additionalResultsSize));
                    String numEntitiesFoundEncoded = CaseUtil
                            .getLowerCaseDeleteAccents(entity
                                    .getCountNameEncoded(additionalResultsSize));
                    String numEntitiesFoundHTML = CaseUtil
                            .getLowerCaseDeleteAccents(entity
                                    .getCountNameHTML(additionalResultsSize));

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

        /*
         * redirect in case of full text results matching
         */
        if ((simpleResultsSize == 0) && (additionalResultsSize > 0)) {
            if ((!(currentSelectedBlockPrimaryKeys.contains(resultsPk)))
                    && (!(currentSelectedBlockPrimaryKeys
                            .contains(moreResultsPk)))) {

                /*
                 * redirect to the current
                 */
                String redirectURL = LocationUtil.getCurrentAbsoluteLocation();

                /*
                 * add the full text results block id
                 */
                redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                        HTTPParameterConstants.SELECTED_BLOCK_ID,
                        BlocksDictionary.SITE_KEYWORD_MORE_RESULTS);

                /*
                 * do the redirect
                 */
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
            }
        }
    }
}

// end AbstractSiteKeywordSearchFormBlock
