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

import com.corendal.netapps.framework.core.blocks.AbstractIllustratedGlossaryResultsBlock;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.IconWriter;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ResultsBodyCell;
import com.corendal.netapps.framework.core.interfaces.ResultsHeaderCell;
import com.corendal.netapps.framework.core.interfaces.ResultsRow;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardField;
import com.corendal.netapps.framework.core.interfaces.StandardFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardIcon;
import com.corendal.netapps.framework.core.interfaces.StandardIconFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardFieldFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardIconFactory;
import com.corendal.netapps.framework.core.utils.ResultsBodyCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsHeaderCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsRowUtil;
import com.corendal.netapps.framework.core.writers.DefaultIconWriter;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;

/**
 * AbstractArticleResultsBlock is the class describing and printing all articles
 * used in the application.
 * 
 * @version $Id: AbstractArticleResultsBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractArticleResultsBlock extends
        AbstractIllustratedGlossaryResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractArticleResultsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractIllustratedGlossaryResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractArticleResultsBlock) super.clone();
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
        StandardFieldFactory sff = (StandardFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardFieldFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);
        IconWriter iw = (IconWriter) pws.getWriter(DefaultIconWriter.class);

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
        PrimaryKey articlePathPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PATH);

        /*
         * get the article type identifier
         */
        PrimaryKey referenceTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.REFERENCE);
        StandardField field1 = (StandardField) sff
                .findByPrimaryKey(articlePathPk);
        PrimaryKey contentDescriptionPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DESCRIPTION);
        StandardField field2 = (StandardField) sff
                .findByPrimaryKey(contentDescriptionPk);

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
         * list all articles found
         */
        List articlesFound = this.getArticlesFound();

        /*
         * convert the list of entities into a list of results row
         */
        if (this.getIsBodyPrinted()) {
            for (int i = 0; i < articlesFound.size(); i++) {
                /*
                 * initialize the current row
                 */
                ResultsRow rr = ResultsRowUtil.getResultsRow();

                /*
                 * find the content
                 */
                StandardArticle currentArticle = (StandardArticle) articlesFound
                        .get(i);

                /*
                 * add this content to the current row
                 */
                ResultsBodyCell srbc1 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, currentArticle.getPathText());
                String link = lw.getBreakableLinkHTML(currentArticle
                        .getDefaultLocation(), null, currentArticle
                        .getMatchNameHTML(), null);
                srbc1.setValueHTML(link);

                ResultsBodyCell srbc2 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, currentArticle.getDescriptionText());
                srbc2.setValueHTML(currentArticle.getDescriptionHTML());

                ResultsBodyCell srbc3 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, currentArticle.getDescriptionText());

                srbc3.setValueHTML(currentArticle.getDescriptionHTML());

                /*
                 * change the preview icon when the current article is not from
                 * the current article
                 */
                StandardIcon previewIcon = (StandardIcon) sif
                        .findByPrimaryKey(currentArticle
                                .getDirectPreviewIconPrimaryKey());

                if (currentArticle.getPrimaryKey().equals(referenceTypePk)) {
                    previewIcon = (StandardIcon) sif
                            .findByPrimaryKey(currentArticle
                                    .getIndirectPreviewIconPrimaryKey());
                }

                ResultsBodyCell srbc4 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, previewIcon.getDefaultLocation());
                srbc4.setValueHTML(lw.getBreakableLinkHTML(currentArticle
                        .getDefaultLocation(), "", iw.getHTML(previewIcon),
                        null));

                /*
                 * gather the cells into a row
                 */
                rr.addResultsBodyCell(srbc1);
                rr.addResultsBodyCell(srbc2);
                rr.addResultsBodyCell(srbc3);
                rr.addResultsBodyCell(srbc4);

                /*
                 * add the current row
                 */
                this.addResultsRow(rr);
            }
        }

        /*
         * set the counts of this block
         */
        this.wrapUp(articlesFound.size());
    }

    /**
     * Returns the list of articles to print.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    protected List getArticlesFound() {
        /*
         * build the list of contents
         */
        List articlesFound;

        if (this.getIsSearchFormUsed()) {
            articlesFound = this.getResultsNoRedirect();
        } else {
            articlesFound = new ArrayList<StandardArticleFactory>();
        }

        /*
         * return
         */
        return articlesFound;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#getSortColumn(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public int getSortColumn() {
        return 0; // use the default sort criteria
    }
}

// end AbstractArticleResultsBlock
