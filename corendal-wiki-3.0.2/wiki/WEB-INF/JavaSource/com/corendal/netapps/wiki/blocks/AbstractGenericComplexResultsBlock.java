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

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.blocks.AbstractNoBorderSearchResultsBlock;
import com.corendal.netapps.framework.core.constants.BlockTypeConstants;
import com.corendal.netapps.framework.core.dictionaries.ExtensionsDictionary;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardSearchFormFieldFactory;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.ResultsBlockWriter;
import com.corendal.netapps.framework.core.interfaces.ResultsBodyCell;
import com.corendal.netapps.framework.core.interfaces.ResultsHeaderCell;
import com.corendal.netapps.framework.core.interfaces.ResultsRow;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardExtension;
import com.corendal.netapps.framework.core.interfaces.StandardExtensionFactory;
import com.corendal.netapps.framework.core.interfaces.StandardField;
import com.corendal.netapps.framework.core.interfaces.StandardFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormField;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormFieldFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardExtensionFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardFieldFactory;
import com.corendal.netapps.framework.core.utils.FullTextUtil;
import com.corendal.netapps.framework.core.utils.ResultsHeaderCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsRowUtil;
import com.corendal.netapps.framework.core.writers.DefaultResultsBlockWriter;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.interfaces.ContentWriter;
import com.corendal.netapps.wiki.writers.DefaultContentWriter;

/**
 * AbstractGenericComplexResultsBlock is the class describing and printing all
 * contents of an article.
 * 
 * @version $Id: AbstractGenericComplexResultsBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractGenericComplexResultsBlock extends
        AbstractNoBorderSearchResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractGenericComplexResultsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractNoBorderSearchResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractGenericComplexResultsBlock) super.clone();
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

        /*
         * set the entity
         */
        PrimaryKey contentsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(contentsPk);
        this.setStandardEntity(entity);

        /*
         * get the fields
         */
        PrimaryKey contentIdPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ID);
        StandardField field1 = (StandardField) sff
                .findByPrimaryKey(contentIdPk);
        PrimaryKey termNamePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.FieldsDictionary.MATCH_NAME);
        StandardField field2 = (StandardField) sff.findByPrimaryKey(termNamePk);
        PrimaryKey termDescriptionPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.FieldsDictionary.MATCH_DESCRIPTION);
        StandardField field3 = (StandardField) sff
                .findByPrimaryKey(termDescriptionPk);
        PrimaryKey contentAddressPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ADDRESS);
        StandardField field4 = (StandardField) sff
                .findByPrimaryKey(contentAddressPk);
        PrimaryKey contentCreatedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CREATED);
        StandardField field5 = (StandardField) sff
                .findByPrimaryKey(contentCreatedPk);
        PrimaryKey contentModifiedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_MODIFIED);
        StandardField field6 = (StandardField) sff
                .findByPrimaryKey(contentModifiedPk);
        PrimaryKey contentPropertyAddressPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PROPERTIES_ADDRESS);
        StandardField field7 = (StandardField) sff
                .findByPrimaryKey(contentPropertyAddressPk);
        PrimaryKey contentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_TYPE);
        StandardField field8 = (StandardField) sff
                .findByPrimaryKey(contentTypePk);

        PrimaryKey imageSizePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.IMAGE_SIZE);
        StandardField field9 = (StandardField) sff
                .findByPrimaryKey(imageSizePk);
        PrimaryKey articleFileNamePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FILE);
        StandardField field10 = (StandardField) sff
                .findByPrimaryKey(articleFileNamePk);
        PrimaryKey contentClassificationPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CLASSIFICATION);
        StandardField field11 = (StandardField) sff
                .findByPrimaryKey(contentClassificationPk);

        PrimaryKey articleSizePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.ARTICLE_SIZE);
        StandardField field12 = (StandardField) sff
                .findByPrimaryKey(articleSizePk);

        /*
         * create the table header
         */
        ResultsHeaderCell rhc1 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field1);
        ResultsHeaderCell rhc2 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field2);
        ResultsHeaderCell rhc3 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field3);
        ResultsHeaderCell rhc4 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field4);
        ResultsHeaderCell rhc5 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field5);
        ResultsHeaderCell rhc6 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field6);
        ResultsHeaderCell rhc7 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field7);
        ResultsHeaderCell rhc8 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field8);
        ResultsHeaderCell rhc9 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field9);
        ResultsHeaderCell rhc10 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field10);
        ResultsHeaderCell rhc11 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field11);
        ResultsHeaderCell rhc12 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field12);

        this.addResultsHeaderCell(rhc1);
        this.addResultsHeaderCell(rhc2);
        this.addResultsHeaderCell(rhc3);
        this.addResultsHeaderCell(rhc4);
        this.addResultsHeaderCell(rhc5);
        this.addResultsHeaderCell(rhc6);
        this.addResultsHeaderCell(rhc7);
        this.addResultsHeaderCell(rhc8);
        this.addResultsHeaderCell(rhc9);
        this.addResultsHeaderCell(rhc10);
        this.addResultsHeaderCell(rhc11);
        this.addResultsHeaderCell(rhc12);

        /*
         * fill this block with results row
         */
        if (this.getIsBodyPrinted()) {
            this.fillResultsRows();
        }

        /*
         * set the counts of this block
         */
        this.wrapUp(this.getResultsCount());
    }

    /**
     * Returns the count of records displayed by this block.
     */
    public abstract int getResultsCount();

    /**
     * Fills this block with results rows.
     * 
     * 
     * 
     */
    public abstract void fillResultsRows();

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#printTableRows(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void printTableRows() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardExtensionFactory sef = (StandardExtensionFactory) pfs
                .getStandardBeanFactory(DefaultStandardExtensionFactory.class);
        StandardSearchFormFieldFactory ssfff = (StandardSearchFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardSearchFormFieldFactory.class);

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
        ResultsBlockWriter rbw = (ResultsBlockWriter) pws
                .getWriter(DefaultResultsBlockWriter.class);

        /*
         * get the keyword fields
         */
        PrimaryKey siteKeywordFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.FieldsDictionary.SITE_KEYWORD_SEARCH);
        StandardSearchFormField siteKeywordField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(siteKeywordFieldPk);
        PrimaryKey contentKeywordFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_KEYWORD);
        StandardSearchFormField contentKeywordField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentKeywordFieldPk);

        /*
         * get the is search form used boolean
         */
        boolean isSearchFormUsed = this.getIsSearchFormUsed();

        /*
         * change the page size and page excess in case of search form
         */
        if (isSearchFormUsed) {
            this.setPageSize(10);
            this.setPageExcess(2);
        }

        /*
         * get the keyword
         */
        String currentKeyword = siteKeywordField.getRequestValue();

        if (StringUtil.getIsEmpty(currentKeyword)) {
            currentKeyword = contentKeywordField.getRequestValue();
        }

        /*
         * convert the keyword into a list of tokens
         */
        List<String> currentTokens = new ArrayList<String>();

        if (!(StringUtil.getIsEmpty(currentKeyword))) {
            currentTokens = FullTextUtil
                    .getUpperCaseDeleteAccentsTokens(currentKeyword);
        }

        /*
         * initialize the token match history
         */
        boolean isPreviousRecordMatched = true;

        if (currentTokens.size() <= 1) {
            isPreviousRecordMatched = false;
        }

        /*
         * get the HTML extension
         */
        PrimaryKey extHTMLPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ExtensionsDictionary.HTML);
        StandardExtension extHTML = (StandardExtension) sef
                .findByPrimaryKey(extHTMLPk);

        /*
         * print according the extension requested. Only the HTML extension has
         * special formatting
         */
        if (rm.getStandardExtension().equals(extHTML)) {
            /*
             * set the start and finish variables
             */
            int start = this.getIndexFirstPrinted();
            int finish = this.getIndexLastPrinted();

            /*
             * get the entity
             */
            StandardEntity entity = this.getStandardEntity();

            /*
             * print the rows
             */
            for (int i = start; i <= finish; i++) {
                /*
                 * get the name and the description
                 */
                ResultsRow resultsRow = this.getResultsRow(i);
                ResultsBodyCell idCell = resultsRow.getResultsBodyCell(0);
                ResultsBodyCell nameCell = resultsRow.getResultsBodyCell(1);
                ResultsBodyCell descriptionCell = resultsRow
                        .getResultsBodyCell(2);
                ResultsBodyCell addressCell = resultsRow.getResultsBodyCell(3);
                ResultsBodyCell createdCell = resultsRow.getResultsBodyCell(4);
                ResultsBodyCell modifiedCell = resultsRow.getResultsBodyCell(5);
                ResultsBodyCell propertiesAddressCell = resultsRow
                        .getResultsBodyCell(6);
                ResultsBodyCell typeCell = resultsRow.getResultsBodyCell(7);
                ResultsBodyCell imageSizeCell = resultsRow
                        .getResultsBodyCell(8);
                ResultsBodyCell articleFileNameCell = resultsRow
                        .getResultsBodyCell(9);
                ResultsBodyCell contentClassificationCell = resultsRow
                        .getResultsBodyCell(10);
                ResultsBodyCell articleSizeCell = resultsRow
                        .getResultsBodyCell(11);

                /*
                 * insert an empty line when the match type changed
                 */
                if (this.getType().equals(BlockTypeConstants.LARGE)) {
                    if ((start == 0) && (isPreviousRecordMatched)
                            && (i < finish)) {
                        String currentStringToMatch = nameCell.getValueText()
                                + " " + descriptionCell.getValueText();

                        if (i > start) {
                            if (!(FullTextUtil.getHasAllTokens(
                                    currentStringToMatch, currentTokens))) {
                                rbw.printSpaceHTML(1);
                                isPreviousRecordMatched = false;
                            }
                        } else {
                            isPreviousRecordMatched = FullTextUtil
                                    .getHasAllTokens(currentStringToMatch,
                                            currentTokens);
                        }
                    }
                }

                /*
                 * start the row (do not use any background color)
                 */
                ResultsRowUtil.printResultRowStart(-1, entity, extHTML);

                /*
                 * print the term
                 */
                dw.printHTML(idCell, nameCell, descriptionCell, addressCell,
                        createdCell, modifiedCell, imageSizeCell,
                        articleFileNameCell, contentClassificationCell,
                        propertiesAddressCell, typeCell, articleSizeCell,
                        isSearchFormUsed);

                /*
                 * end the row (do not use any background color)
                 */
                ResultsRowUtil.printResultRowEnd(-1, entity, extHTML);

                /*
                 * print a space
                 */
                rbw.printSpaceHTML(1);
            }
        } else {
            super.printTableRows();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#getSortColumn(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public int getSortColumn() {
        return 0; // use the default sort criteria
    }
}

// end AbstractGenericComplexResultsBlock
