/*   
 * Corendal NetApps Wiki - Web application for content management   
 * Copyright (C) Thierry Danard   
 *   
 * This program is free software; you can redistribute it and|or   
 * modify it under the articles of the GNU General Public License   
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

import com.corendal.netapps.framework.core.blocks.AbstractGlossaryResultsBlock;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ResultsBodyCell;
import com.corendal.netapps.framework.core.interfaces.ResultsHeaderCell;
import com.corendal.netapps.framework.core.interfaces.ResultsRow;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardField;
import com.corendal.netapps.framework.core.interfaces.StandardFieldFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardFieldFactory;
import com.corendal.netapps.framework.core.utils.ResultsBodyCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsHeaderCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsRowUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.interfaces.ScoredPrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.utils.ArticleUtil;

/**
 * AbstractOnlineHelpKeywordSearchResultsBlock is the abstract block printing
 * all articles used in the application.
 * 
 * @version $Id: AbstractOnlineHelpKeywordSearchResultsBlock.java,v 1.1
 *          2005/09/06 21:25:28 tdanard Exp $
 */
public abstract class AbstractOnlineHelpKeywordSearchResultsBlock extends
        AbstractGlossaryResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead
     */
    protected AbstractOnlineHelpKeywordSearchResultsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractGlossaryResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractOnlineHelpKeywordSearchResultsBlock) super.clone();
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
        this.setMultiplePages();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);
        StandardFieldFactory sff = (StandardFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardFieldFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * set the entity
         */
        PrimaryKey articlesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(articlesPk);
        this.setStandardEntity(entity);

        /*
         * get the fields
         */
        PrimaryKey articleNamePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
        StandardField field1 = (StandardField) sff
                .findByPrimaryKey(articleNamePk);
        PrimaryKey articleDescriptionPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DESCRIPTION);
        StandardField field2 = (StandardField) sff
                .findByPrimaryKey(articleDescriptionPk);

        /*
         * create the table header
         */
        ResultsHeaderCell rhc1 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field1);
        ResultsHeaderCell rhc2 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field2);
        this.addResultsHeaderCell(rhc1);
        this.addResultsHeaderCell(rhc2);

        /*
         * list all entities and fields
         */
        List results = this.getResultsNoRedirect();

        /*
         * convert the list of entities into a list of results row
         */
        if (this.getIsBodyPrinted()) {
            for (int i = 0; i < results.size(); i++) {
                /*
                 * initialize the current row
                 */
                ResultsRow rr = ResultsRowUtil.getResultsRow();

                /*
                 * get the current article
                 */
                StandardArticle currentArticle = (StandardArticle) results
                        .get(i);

                /*
                 * set the float sort value
                 */
                PrimaryKey pk = currentArticle.getPrimaryKey();
                if (pk instanceof ScoredPrimaryKey) {
                    ScoredPrimaryKey scoredPk = (ScoredPrimaryKey) currentArticle
                            .getPrimaryKey();
                    rr.setSortValueFloat(scoredPk.getScore());
                } else {
                    rr.setSortValueFloat(0);
                }

                /*
                 * customize the description
                 */
                String descriptionText = ArticleUtil
                        .getCustomizedDescriptionText(currentArticle, false);

                /*
                 * add this article to the current row
                 */
                ResultsBodyCell srbc1 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, currentArticle.getNameText());
                ResultsBodyCell srbc2 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, descriptionText);
                ResultsBodyCell srbc3 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, descriptionText);
                rr.addResultsBodyCell(srbc1);
                rr.addResultsBodyCell(srbc2);
                rr.addResultsBodyCell(srbc3);

                /*
                 * customize the HTML versions
                 */
                String nameHTML = ArticleUtil
                        .getCustomizedNameOrDescriptionHTML(currentArticle
                                .getNameText());
                String descriptionHTML = ArticleUtil
                        .getCustomizedNameOrDescriptionHTML(descriptionText);

                /*
                 * associate the link to the first cell
                 */
                srbc1.setValueHTML(lw.getBreakableLinkHTML(currentArticle
                        .getOnlineHelpDefaultLocation(), null, nameHTML,
                        currentArticle.getAccessKeyEncoded()));

                /*
                 * associate the html description to the second cell
                 */
                srbc2.setValueHTML(descriptionHTML);

                /*
                 * add the current row
                 */
                this.addResultsRow(rr);
            }
        }

        /*
         * set the counts of this block
         */
        this.wrapUp(results.size());

        /*
         * sort the rows by descending score
         */
        this.sortResultsRows();
        this.reverseResultsRows();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#getSortColumn(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public int getSortColumn() {
        return 0;
    }
}

// end AbstractOnlineHelpKeywordSearchResultsBlock
