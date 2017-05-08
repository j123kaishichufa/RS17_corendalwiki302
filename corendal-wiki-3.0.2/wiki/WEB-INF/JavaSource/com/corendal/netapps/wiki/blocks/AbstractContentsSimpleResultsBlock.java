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

import com.corendal.netapps.framework.configuration.utils.StringUtil;
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
import com.corendal.netapps.framework.core.interfaces.StandardStoredFile;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFileFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardFieldFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardIconFactory;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.ResultsBodyCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsHeaderCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsRowUtil;
import com.corendal.netapps.framework.core.writers.DefaultIconWriter;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardStoredFileFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;

/**
 * AbstractContentsSimpleResultsBlock is the class describing and printing all
 * contents used in the application.
 * 
 * @version $Id: AbstractContentsSimpleResultsBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractContentsSimpleResultsBlock extends
        AbstractIllustratedGlossaryResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractContentsSimpleResultsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractIllustratedGlossaryResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentsSimpleResultsBlock) super.clone();
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
        com.corendal.netapps.wiki.interfaces.StandardImageFactory sdocf = (com.corendal.netapps.wiki.interfaces.StandardImageFactory) pfs
                .getStandardBeanFactory(com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardStoredFileFactory ssff = (StandardStoredFileFactory) pfs
                .getStandardBeanFactory(DefaultStandardStoredFileFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);
        IconWriter iw = (IconWriter) pws.getWriter(DefaultIconWriter.class);

        /*
         * get the article and icon types primary key
         */
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
        PrimaryKey iconTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);

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
        PrimaryKey contentNamePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
        StandardField field1 = (StandardField) sff
                .findByPrimaryKey(contentNamePk);
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
         * list all contents found
         */
        List contentsFound = this.getFilteredContentsFound();

        /*
         * convert the list of entities into a list of results row
         */
        if (this.getIsBodyPrinted()) {
            for (int i = 0; i < contentsFound.size(); i++) {
                /*
                 * initialize the current row
                 */
                ResultsRow rr = ResultsRowUtil.getResultsRow();

                /*
                 * find the content
                 */
                StandardContent currentContent = (StandardContent) contentsFound
                        .get(i);

                /*
                 * get the governing content
                 */
                Searched classificationSearched = currentContent
                        .getClassificationSearched();
                PrimaryKey classificationTypePk = classificationSearched
                        .getClassificationTypePrimaryKey();

                /*
                 * add this content to the current row
                 */
                ResultsBodyCell srbc1 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, currentContent.getNameText());
                String icon = lw.getBreakableLinkHTML(currentContent
                        .getDefaultLocation(), null, currentContent
                        .getNameHTML(), null);
                srbc1.setValueHTML(icon);

                ResultsBodyCell srbc2 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, currentContent.getDescriptionText());
                srbc2.setValueHTML(currentContent.getDescriptionHTML());

                ResultsBodyCell srbc3 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, currentContent.getDescriptionText());

                srbc3.setValueHTML(currentContent.getDescriptionHTML());

                /*
                 * add the preview icon
                 */
                StandardIcon previewIcon = null;

                /*
                 * get the associated file in case of article
                 */
                if (currentContent.getTypePrimaryKey().equals(iconTypePk)) {
                    /*
                     * get the associated article
                     */
                    com.corendal.netapps.wiki.interfaces.StandardImage currentIcon = (com.corendal.netapps.wiki.interfaces.StandardImage) sdocf
                            .findByPrimaryKey(currentContent.getPrimaryKey());

                    /*
                     * get the associated file
                     */
                    PrimaryKey storedFilePk = currentIcon.getFilePrimaryKey();
                    StandardStoredFile storedFile = (StandardStoredFile) ssff
                            .findByPrimaryKey(storedFilePk);

                    /*
                     * get the associated file name
                     */
                    String fileName = storedFile.getName();
                    String fileNameText = "";

                    if (!(StringUtil.getIsEmpty(fileName))) {
                        fileNameText = fileName;
                    }

                    /*
                     * get the associated file size
                     */
                    int fileSize = storedFile.getSize();
                    String fileSizeText = "";

                    if (fileSize > 0) {
                        fileSizeText = String.valueOf(fileSize);
                    }

                    /*
                     * get the associated modified date
                     */
                    Date modifiedDate = currentIcon.getLastEntryLogTimestamp();
                    String modifiedDateText = "";

                    if (modifiedDate != null) {
                        modifiedDateText = DateFormatUtil
                                .getInternalFormattedDateText(modifiedDate);
                    }

                    /*
                     * get the preview icon
                     */
                    previewIcon = ContentUtil.getDirectPreviewIcon(currentIcon,
                            classificationTypePk, fileNameText, null,
                            fileSizeText, modifiedDateText);
                } else if (currentContent.getTypePrimaryKey().equals(
                        articleTypePk)) {
                    /*
                     * get the associated article
                     */
                    StandardArticle currentArticle = (StandardArticle) sdf
                            .findByPrimaryKey(currentContent.getPrimaryKey());

                    /*
                     * get the associated size
                     */
                    int size = currentArticle.getSize();

                    /*
                     * get the preview icon
                     */
                    previewIcon = ContentUtil.getDirectPreviewIcon(
                            currentArticle, classificationTypePk, null, ""
                                    + size, null, "");
                } else {
                    previewIcon = ContentUtil.getDirectPreviewIcon(
                            currentContent, classificationTypePk, null, "",
                            null, "");
                }

                /*
                 * get the fourth cell
                 */
                ResultsBodyCell srbc4 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, previewIcon.getDefaultLocation());
                srbc4.setValueHTML(lw.getBreakableLinkHTML(currentContent
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
        this.wrapUp(contentsFound.size());
    }

    /**
     * Returns the preview icon to use for a content.
     * 
     * @param currentContent
     *            a StandardContent object
     * 
     * 
     * @return a com.corendal.netapps.framework.core.interfaces.StandardIcon object
     */
    protected StandardIcon getPreviewIcon(StandardContent currentContent) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);

        /*
         * return
         */
        return (StandardIcon) sif.findByPrimaryKey(currentContent
                .getDirectPreviewIconPrimaryKey());
    }

    /**
     * Returns the list of filtered contents to print.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    protected List getFilteredContentsFound() {
        return ContentUtil.getFilteredContents(this.getContentsFound());
    }

    /**
     * Returns the list of contents to print.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    protected List getContentsFound() {
        /*
         * build the list of contents
         */
        List contentsFound;

        if (this.getIsSearchFormUsed()) {
            contentsFound = this.getResultsNoRedirect();
        } else {
            contentsFound = new ArrayList<StandardContent>();
        }

        /*
         * return
         */
        return contentsFound;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#getSortColumn(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public int getSortColumn() {
        return 0; // use the default sort criteria
    }
}

// end AbstractContentsSimpleResultsBlock
