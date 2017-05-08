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

import java.util.Date;
import java.util.List;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.Matched;
import com.corendal.netapps.framework.core.interfaces.ResultsBodyCell;
import com.corendal.netapps.framework.core.interfaces.ResultsRow;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFile;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFileFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.utils.BlockUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.ResultsBodyCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsRowUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardStoredFileFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.interfaces.ScoredPrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationType;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentType;
import com.corendal.netapps.wiki.interfaces.StandardContentTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentTypeFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractGenericSearchResultsBlock is the class describing and printing all
 * contents of an article.
 * 
 * @version $Id: AbstractGenericSearchResultsBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractGenericSearchResultsBlock extends
        AbstractGenericComplexResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractGenericSearchResultsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractGenericComplexResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractGenericSearchResultsBlock) super.clone();
    }

    /**
     * Fills this block with results rows. Implements abstract method
     * AbstractGenericComplexResultsBlock.fillResultsRows.
     * 
     * 
     * 
     */
    @Override
    public void fillResultsRows() {

        /*
         * find out whether this block is actually displayed
         */
        boolean isFillRequired = this.getIsSearchResultsAddedToTitle();
        if (!(isFillRequired)) {
            isFillRequired = BlockUtil.getCurrentSelectedBlockPrimaryKeys()
                    .contains(this.getPrimaryKey());
        }

        /*
         * list all matches
         */
        List results = this.getSimpleOrAdditionalResultsNoRedirect();

        /*
         * convert the list of matches into a list of results row
         */
        for (int h = 0; h < results.size(); h++) {
            /*
             * get the current list of matches
             */
            List matchesFound = (List) results.get(h);

            /*
             * add all these matches to the list of current rows
             */
            for (int i = 0; i < matchesFound.size(); i++) {
                /*
                 * initialize the current row
                 */
                ResultsRow rr = ResultsRowUtil.getResultsRow();
                rr.setSortValueFloat(0);

                /*
                 * instantiate the other cells
                 */
                ResultsBodyCell srbc1 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell srbc2 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell srbc3 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell srbc4 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell srbc5 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell srbc6 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell srbc7 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell srbc8 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell srbc9 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell srbc10 = ResultsBodyCellUtil
                        .getResultsBodyCell(1, "");
                ResultsBodyCell srbc11 = ResultsBodyCellUtil
                        .getResultsBodyCell(1, "");
                ResultsBodyCell srbc12 = ResultsBodyCellUtil
                        .getResultsBodyCell(1, "");

                /*
                 * check that the data needs to be filled
                 */
                if (isFillRequired) {
                    /*
                     * find the resource
                     */
                    Matched currentMatch = (Matched) matchesFound.get(i);

                    /*
                     * set the float sort value
                     */
                    if (currentMatch.getPrimaryKey() instanceof ScoredPrimaryKey) {
                        ScoredPrimaryKey pk = (ScoredPrimaryKey) currentMatch
                                .getPrimaryKey();
                        rr.setSortValueFloat(pk.getScore());
                    } else {
                        rr.setSortValueFloat(0);
                    }

                    /*
                     * remember that match
                     */
                    rr.setInternalObject(currentMatch);
                }

                /*
                 * gather the cells into a row
                 */
                rr.addResultsBodyCell(srbc1);
                rr.addResultsBodyCell(srbc2);
                rr.addResultsBodyCell(srbc3);
                rr.addResultsBodyCell(srbc4);
                rr.addResultsBodyCell(srbc5);
                rr.addResultsBodyCell(srbc6);
                rr.addResultsBodyCell(srbc7);
                rr.addResultsBodyCell(srbc8);
                rr.addResultsBodyCell(srbc9);
                rr.addResultsBodyCell(srbc10);
                rr.addResultsBodyCell(srbc11);
                rr.addResultsBodyCell(srbc12);

                /*
                 * add the current row
                 */
                this.addResultsRow(rr);
            }
        }
    }

    @Override
    public void printTableRows() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentTypeFactory srtf = (StandardContentTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentTypeFactory.class);
        StandardStoredFileFactory ssff = (StandardStoredFileFactory) pfs
                .getStandardBeanFactory(DefaultStandardStoredFileFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardImageFactory slf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
        StandardContentClassificationTypeFactory srctf = (StandardContentClassificationTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentClassificationTypeFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * get all content types
         */
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
        PrimaryKey imageTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);

        /*
         * get the list of results
         */
        List<ResultsRow> resultsRows = this.getResultsRows();

        /*
         * fill the rows that are displayed
         */
        int start = this.getIndexFirstPrinted();
        int finish = this.getIndexLastPrinted();

        /*
         * populate each displayed cell
         */
        for (int i = start; i <= finish; i++) {
            /*
             * get the current results row
             */
            ResultsRow rr = resultsRows.get(i);

            /*
             * get the current match
             */
            Matched currentMatch = (Matched) (rr.getInternalObject());

            /*
             * get the current cells
             */
            ResultsBodyCell srbc1 = rr.getResultsBodyCell(0);
            ResultsBodyCell srbc2 = rr.getResultsBodyCell(1);
            ResultsBodyCell srbc3 = rr.getResultsBodyCell(2);
            ResultsBodyCell srbc4 = rr.getResultsBodyCell(3);
            ResultsBodyCell srbc5 = rr.getResultsBodyCell(4);
            ResultsBodyCell srbc6 = rr.getResultsBodyCell(5);
            ResultsBodyCell srbc7 = rr.getResultsBodyCell(6);
            ResultsBodyCell srbc8 = rr.getResultsBodyCell(7);
            ResultsBodyCell srbc9 = rr.getResultsBodyCell(8);
            ResultsBodyCell srbc10 = rr.getResultsBodyCell(9);
            ResultsBodyCell srbc11 = rr.getResultsBodyCell(10);
            ResultsBodyCell srbc12 = rr.getResultsBodyCell(11);

            /*
             * add the name to the current row
             */
            String customizedNameText = currentMatch.getMatchNameText();
            String customizedNameHTML = this
                    .getCustomizedNameHTML(customizedNameText);
            srbc2.setValueText(customizedNameText);

            /*
             * associate the image to the first cell
             */
            String image = lw.getBreakableLinkHTML(currentMatch
                    .getDefaultLocation(), null, customizedNameHTML, null);
            srbc2.setValueHTML(image);

            /*
             * add the description to the current row
             */
            String customizedLongDescriptionText = this
                    .getCustomizedDescriptionText(currentMatch);
            String customizedLongDescriptionHTML = this
                    .getCustomizedDescriptionHTML(customizedLongDescriptionText);
            srbc3.setValueText(customizedLongDescriptionText);
            srbc3.setValueHTML(customizedLongDescriptionHTML);

            /*
             * associate the absolute location
             */
            srbc4.setValueText(currentMatch.getAbsoluteLocation());

            /*
             * find whether the result is a content
             */
            if (currentMatch instanceof Searched) {
                Searched currentSearched = (Searched) currentMatch;

                /*
                 * get the governing content
                 */
                Searched classificationSearched = currentSearched
                        .getClassificationSearched();
                PrimaryKey classificationTypePk = classificationSearched
                        .getClassificationTypePrimaryKey();

                /*
                 * find the classification type
                 */
                StandardContentClassificationType classificationType = (StandardContentClassificationType) srctf
                        .findByPrimaryKey(classificationTypePk);

                /*
                 * find the content type
                 */
                StandardContentType currentSearchedType = (StandardContentType) srtf
                        .findByPrimaryKey(currentSearched.getTypePrimaryKey());

                /*
                 * populate some generic cells
                 */
                srbc1.setValueText(currentMatch.getPrimaryKey().toString());

                srbc7.setValueText(currentSearched
                        .getPropertiesAbsoluteLocation());

                srbc8.setValueText(currentSearchedType.getNameText());

                srbc11.setValueText(classificationType.getNameText());

                /*
                 * find the info specific to article, image or article
                 */
                if (currentSearched.getTypePrimaryKey().equals(articleTypePk)) {
                    StandardArticle currentArticle = (StandardArticle) sdf
                            .findByPrimaryKey(currentSearched.getPrimaryKey());

                    Date firstEntryLogDate = currentArticle
                            .getFirstEntryLogTimestamp();
                    srbc5.setValueText(DateFormatUtil
                            .getInternalFormattedDateText(firstEntryLogDate));
                    srbc5.setValueXLS(DateFormatUtil
                            .getLongExcelFormattedDateText(firstEntryLogDate));

                    srbc12.setValueText(String
                            .valueOf(currentArticle.getSize()));
                } else if (currentSearched.getTypePrimaryKey().equals(
                        imageTypePk)) {
                    StandardImage currentImage = (StandardImage) slf
                            .findByPrimaryKey(currentSearched.getPrimaryKey());
                    StandardStoredFile currentStoredFile = (StandardStoredFile) ssff
                            .findByPrimaryKey(currentImage.getFilePrimaryKey());

                    Date firstEntryLogDate = currentImage
                            .getFirstEntryLogTimestamp();
                    srbc5.setValueText(DateFormatUtil
                            .getInternalFormattedDateText(firstEntryLogDate));
                    srbc5.setValueXLS(DateFormatUtil
                            .getLongExcelFormattedDateText(firstEntryLogDate));

                    Date lastEntryLogDate = currentImage
                            .getLastEntryLogTimestamp();
                    srbc6.setValueText(DateFormatUtil
                            .getInternalFormattedDateText(lastEntryLogDate));
                    srbc6.setValueXLS(DateFormatUtil
                            .getLongExcelFormattedDateText(lastEntryLogDate));

                    srbc9.setValueText(String.valueOf(currentStoredFile
                            .getSize()));
                    srbc10.setValueText(String.valueOf(currentStoredFile
                            .getName()));
                }
            }
        }

        /*
         * call the parent method
         */
        super.printTableRows();
    }

    /**
     * Returns the name to print in the HTML format. Override this method if you
     * want to include the highlight some words in the description.
     * 
     * @param customizedNameText
     *            name to convert in HTML
     * @return a java.lang.String object@param alc current tier context
     */
    protected String getCustomizedNameHTML(String customizedNameText) {
        return TextFormatUtil.getTextToHTML(customizedNameText);
    }

    /**
     * Returns the description to print in the text format. Override this method
     * if you want to include the content of your article instead of the
     * description.
     * 
     * @param matched
     *            entity to get a description from
     * 
     * 
     * @return a java.lang.String object
     */
    protected String getCustomizedDescriptionText(Matched matched) {
        return matched.getLongDescriptionText();
    }

    /**
     * Returns the description to print in the HTML format. Override this method
     * if you want to include the highlight some words in the description.
     * 
     * @param customizedDescriptionText
     *            description to convert in HTML
     * @return a java.lang.String object@param alc current tier context
     */
    protected String getCustomizedDescriptionHTML(
            String customizedDescriptionText) {
        return TextFormatUtil.getTextToHTML(customizedDescriptionText);
    }

    /**
     * Returns the list of results to print. This method is overriden by the
     * additional results block.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    protected List getSimpleOrAdditionalResultsNoRedirect() {
        return this.getResultsNoRedirect();
    }

    /**
     * Returns the list of results to print. This method is overriden by the
     * additional results block.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    protected int getSimpleOrAdditionalResultsCountNoRedirect() {
        return this.getResultsCountNoRedirect();
    }

    /*
     * @see com.corendal.netapps.wiki.blocks.AbstractGenericComplexResultsBlock#getResultsCount()
     */
    @Override
    public int getResultsCount() {
        return this.getSimpleOrAdditionalResultsCountNoRedirect();
    }
}

// end AbstractGenericSearchResultsBlock
