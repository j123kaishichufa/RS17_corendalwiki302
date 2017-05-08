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
import java.util.Date;
import java.util.List;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ResultsBodyCell;
import com.corendal.netapps.framework.core.interfaces.ResultsRow;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFile;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFileFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.ResultsBodyCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsRowUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardStoredFileFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationType;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentType;
import com.corendal.netapps.wiki.interfaces.StandardContentTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentTypeFactory;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractContentsComplexResultsBlock is the class describing and printing all
 * contents. of an article
 * 
 * @version $Id: AbstractContentsComplexResultsBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractContentsComplexResultsBlock extends
        AbstractGenericComplexResultsBlock implements Cloneable {
    /** List of contents to print. */
    private List<StandardContent> contentsFound;

    /** List of filtered contents to print. */
    private List filteredContentsFound;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractContentsComplexResultsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractGenericComplexResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentsComplexResultsBlock) super.clone();
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
         * count the number of contents found
         */
        int countOfContentsFound = this.getCountOfFilteredContentsFound();

        /*
         * initialize an empty set of body cells
         */
        ResultsBodyCell srbc1 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc2 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc3 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc4 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc5 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc6 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc7 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc8 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc9 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc10 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc11 = ResultsBodyCellUtil.getResultsBodyCell(1, "");
        ResultsBodyCell srbc12 = ResultsBodyCellUtil.getResultsBodyCell(1, "");

        /*
         * initialize an empty row
         */
        ResultsRow rr = ResultsRowUtil.getResultsRow();

        /*
         * convert the list of entities into a list of results row
         */
        for (int i = 0; i < countOfContentsFound; i++) {
            /*
             * create a row
             */
            ResultsRow rrclone = (ResultsRow) rr.clone();

            /*
             * gather the cells into a row
             */
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc1.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc2.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc3.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc4.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc5.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc6.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc7.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc8.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc9.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc10.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc11.clone());
            rrclone.addResultsBodyCell((ResultsBodyCell) srbc12.clone());

            /*
             * add the current row
             */
            this.addResultsRow(rrclone);
        }
    }

    /**
     * Returns the description of a content. Override this method to add
     * comments to the description printed.
     * 
     * @param content
     *            content to return a description for
     * 
     * 
     * @return a java.lang.String object
     */
    public String getContentDescriptionText(Searched content) {
        return content.getDescriptionText();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#printBodyWithoutAuthenticationCheck(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void printBodyWithoutAuthenticationCheck() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardStoredFileFactory ssff = (StandardStoredFileFactory) pfs
                .getStandardBeanFactory(DefaultStandardStoredFileFactory.class);
        StandardContentTypeFactory srtf = (StandardContentTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentTypeFactory.class);
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
         * list all contents found
         */
        List contentsFound = this.getFilteredContentsFound();

        /*
         * assign values to only the contents printed
         */
        for (int i = this.getIndexFirstPrinted(); i <= this
                .getIndexLastPrinted(); i++) {
            /*
             * get the current content
             */
            Searched currentContent = (Searched) contentsFound.get(i);

            /*
             * get the current results row
             */
            ResultsRow rr = this.getResultsRow(i);

            /*
             * get the body cells
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
             * get the governing content
             */
            Searched classificationSearched = currentContent
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
            StandardContentType currentContentType = (StandardContentType) srtf
                    .findByPrimaryKey(currentContent.getTypePrimaryKey());

            /*
             * add this content to the current row
             */
            srbc1.setValueText(currentContent.getPrimaryKey().toString());

            srbc2.setValueText(currentContent.getNameText());

            String image = lw.getBreakableLinkHTML(currentContent
                    .getDefaultLocation(), null, currentContent.getNameHTML(),
                    null);
            srbc2.setValueHTML(image);

            srbc4.setValueText(currentContent.getAbsoluteLocation());

            srbc7.setValueText(currentContent.getPropertiesAbsoluteLocation());

            srbc8.setValueText(currentContentType.getNameText());

            srbc11.setValueText(classificationType.getNameText());

            /*
             * set the description of the current content
             */
            srbc3.setValueText(this.getContentDescriptionText(currentContent));

            /*
             * find the info specific to article, image or article
             */
            if (currentContent.getTypePrimaryKey().equals(articleTypePk)) {
                StandardArticle currentArticle = (StandardArticle) sdf
                        .findByPrimaryKey(currentContent.getPrimaryKey());

                Date firstEntryLogDate = currentArticle
                        .getFirstEntryLogTimestamp();
                srbc5.setValueText(DateFormatUtil
                        .getInternalFormattedDateText(firstEntryLogDate));
                srbc5.setValueXLS(DateFormatUtil
                        .getLongExcelFormattedDateText(firstEntryLogDate));

                Date lastEntryLogDate = currentArticle
                        .getLastEntryLogTimestamp();
                srbc6.setValueText(DateFormatUtil
                        .getInternalFormattedDateText(lastEntryLogDate));
                srbc6.setValueXLS(DateFormatUtil
                        .getLongExcelFormattedDateText(lastEntryLogDate));

                srbc12.setValueText(String.valueOf(currentArticle.getSize()));
            } else if (currentContent.getTypePrimaryKey().equals(imageTypePk)) {
                StandardImage currentImage = (StandardImage) slf
                        .findByPrimaryKey(currentContent.getPrimaryKey());
                StandardStoredFile currentStoredFile = (StandardStoredFile) ssff
                        .findByPrimaryKey(currentImage.getFilePrimaryKey());

                Date firstEntryLogDate = currentImage
                        .getFirstEntryLogTimestamp();
                srbc5.setValueText(DateFormatUtil
                        .getInternalFormattedDateText(firstEntryLogDate));
                srbc5.setValueXLS(DateFormatUtil
                        .getLongExcelFormattedDateText(firstEntryLogDate));

                Date lastEntryLogDate = currentImage.getLastEntryLogTimestamp();
                srbc6.setValueText(DateFormatUtil
                        .getInternalFormattedDateText(lastEntryLogDate));
                srbc6.setValueXLS(DateFormatUtil
                        .getLongExcelFormattedDateText(lastEntryLogDate));

                srbc9.setValueText(String.valueOf(currentStoredFile.getSize()));
                srbc10
                        .setValueText(String.valueOf(currentStoredFile
                                .getName()));
            }
        }

        /*
         * call the parent print procedure
         */
        super.printBodyWithoutAuthenticationCheck();
    }

    /**
     * Returns the list of filtered contents to print.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    protected List getFilteredContentsFound() {
        /*
         * filter out the invisible contents
         */
        if (this.filteredContentsFound == null) {
            this.filteredContentsFound = ContentUtil.getFilteredContents(this
                    .getContentsFound());
        }

        /*
         * return
         */
        return this.filteredContentsFound;
    }

    /**
     * Returns the total number of filtered contents found.
     * 
     * 
     * 
     * @return an int
     */
    public int getCountOfFilteredContentsFound() {
        if (this.filteredContentsFound == null) {
            this.filteredContentsFound = this.getFilteredContentsFound();
        }

        return this.filteredContentsFound.size();
    }

    /**
     * Returns the list of contents to print.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    protected List getContentsFound() {
        if (this.contentsFound == null) {
            /*
             * build the list of contents
             */
            if (this.getIsSearchFormUsed()) {
                this.contentsFound = this.getResultsNoRedirect();
            } else {
                this.contentsFound = new ArrayList<StandardContent>();
            }
        }

        /*
         * return
         */
        return this.contentsFound;
    }

    /*
     * @see com.corendal.netapps.wiki.blocks.AbstractGenericComplexResultsBlock#getResultsCount()
     */
    @Override
    public int getResultsCount() {
        return this.getCountOfFilteredContentsFound();
    }
}

// end AbstractContentsComplexResultsBlock
