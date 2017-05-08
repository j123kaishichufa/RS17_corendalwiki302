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
import com.corendal.netapps.framework.core.blocks.AbstractStandardSearchResultsBlock;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.ResultsBodyCell;
import com.corendal.netapps.framework.core.interfaces.ResultsHeaderCell;
import com.corendal.netapps.framework.core.interfaces.ResultsRow;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardField;
import com.corendal.netapps.framework.core.interfaces.StandardFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFile;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFileFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardFieldFactory;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.ResultsBodyCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsHeaderCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsRowUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardStoredFileFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.interfaces.StandardImageVersion;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;

/**
 * AbstractImageVersionResultsBlock is the class describing and printing all
 * image versions used in the application.
 * 
 * @version $Id: AbstractImageVersionResultsBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractImageVersionResultsBlock extends
        AbstractStandardSearchResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractImageVersionResultsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardSearchResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractImageVersionResultsBlock) super.clone();
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
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);
        StandardStoredFileFactory ssff = (StandardStoredFileFactory) pfs
                .getStandardBeanFactory(DefaultStandardStoredFileFactory.class);

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
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * set the entity
         */
        PrimaryKey requestsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(requestsPk);
        this.setStandardEntity(entity);

        /*
         * get the fields
         */
        PrimaryKey versionNumFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.IMAGE_VERSION);
        StandardField field1 = (StandardField) sff
                .findByPrimaryKey(versionNumFieldPk);
        PrimaryKey fileFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FILE);
        StandardField field2 = (StandardField) sff
                .findByPrimaryKey(fileFieldPk);
        PrimaryKey requestorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.IMAGE_VERSION_REQUESTOR);
        StandardField field3 = (StandardField) sff
                .findByPrimaryKey(requestorFieldPk);
        PrimaryKey createdFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CREATED);
        StandardField field4 = (StandardField) sff
                .findByPrimaryKey(createdFieldPk);
        PrimaryKey versionNotesFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_VERSION_NOTES);
        StandardField field5 = (StandardField) sff
                .findByPrimaryKey(versionNotesFieldPk);

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
        this.addResultsHeaderCell(rhc1);
        this.addResultsHeaderCell(rhc2);
        this.addResultsHeaderCell(rhc3);
        this.addResultsHeaderCell(rhc4);
        this.addResultsHeaderCell(rhc5);

        /*
         * list all image versions found
         */
        List imageVersionsFound = this.getImageVersionsFound();

        /*
         * convert the list of image versions into a list of results row
         */
        for (int i = 0; i < imageVersionsFound.size(); i++) {
            /*
             * initialize the current row
             */
            ResultsRow rr = ResultsRowUtil.getResultsRow();

            /*
             * find the image version
             */
            StandardImageVersion currentImageVersion = (StandardImageVersion) imageVersionsFound
                    .get(i);

            /*
             * add this image version to the current row
             */
            ResultsBodyCell srbc1 = ResultsBodyCellUtil.getResultsBodyCell(1,
                    String.valueOf(currentImageVersion.getVersionNum()));

            /*
             * add the associated file
             */
            ResultsBodyCell srbc2 = ResultsBodyCellUtil.getResultsBodyCell(1,
                    "");
            PrimaryKey filePk = currentImageVersion.getFilePrimaryKey();

            if (filePk != null) {
                StandardStoredFile file = (StandardStoredFile) ssff
                        .findByPrimaryKey(filePk);

                if (file.getIsFound()) {
                    String fileText = file.getName();
                    String fileHTML = lw.getBreakableLinkHTML(
                            currentImageVersion.getDefaultLocation(), "",
                            fileText, null);
                    srbc2.setValueText(fileText);
                    srbc2.setValueHTML(fileHTML);
                }
            }

            /*
             * add the associated requestor
             */
            ResultsBodyCell srbc3 = ResultsBodyCellUtil.getResultsBodyCell(1,
                    "");

            /*
             * find the content request
             */
            ResultsBodyCell srbc4 = ResultsBodyCellUtil.getResultsBodyCell(1,
                    "");
            PrimaryKey contentRequestPk = currentImageVersion
                    .getRequestPrimaryKey();

            if (contentRequestPk != null) {
                StandardContentRequest request = (StandardContentRequest) srrf
                        .findByPrimaryKey(contentRequestPk);

                if (request.getIsFound()) {
                    StandardAccount requestor = request
                            .getRequestorStandardAccount();

                    if ((requestor != null) && (requestor.getIsFound())) {
                        srbc3.setValueText(requestor.getFullNameAndLoginText());
                        srbc3.setValueHTML(requestor
                                .getFullNameAndLoginHTMLWithLink());
                    }

                    String dateText = DateFormatUtil
                            .getInternalFormattedDateText(request
                                    .getLastEntryLogTimestamp());
                    String dateXLS = DateFormatUtil
                            .getLongExcelFormattedDateText(request
                                    .getLastEntryLogTimestamp());
                    String dateHTML = lw.getBreakableLinkHTML(request
                            .getDefaultLocation(), null, DateFormatUtil
                            .getLongFormattedDateText(request
                                    .getLastEntryLogTimestamp()), null);
                    srbc4.setValueText(dateText);
                    srbc4.setValueXLS(dateXLS);
                    srbc4.setValueHTML(dateHTML);
                }
            }

            /*
             * add the version notes
             */
            ResultsBodyCell srbc5 = ResultsBodyCellUtil.getResultsBodyCell(1,
                    currentImageVersion.getComment());

            /*
             * gather the cells into a row
             */
            rr.addResultsBodyCell(srbc1);
            rr.addResultsBodyCell(srbc2);
            rr.addResultsBodyCell(srbc3);
            rr.addResultsBodyCell(srbc4);
            rr.addResultsBodyCell(srbc5);

            /*
             * change the color of the row
             */
            String checkVersionNumber = rm
                    .getParameter(HTTPParameterConstants.CHECK_VERSION_NUMBER);

            if (!(StringUtil.getIsEmpty(checkVersionNumber))) {
                String currentVersionNumber = ""
                        + currentImageVersion.getVersionNum();

                if (checkVersionNumber.equals(currentVersionNumber)) {
                    rr.setIsHighlightedInYellow(true);
                }
            }

            /*
             * add the current row
             */
            this.addResultsRow(rr);
        }

        /*
         * set the counts of this block
         */
        this.wrapUp(this.getResultsRows().size());
    }

    /**
     * Returns the list of image versions to print.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    protected List getImageVersionsFound() {
        /*
         * build the list of image versions
         */
        List<StandardImageVersion> imageVersionsFound;

        if (this.getIsSearchFormUsed()) {
            imageVersionsFound = this.getResultsNoRedirect();
        } else {
            imageVersionsFound = new ArrayList<StandardImageVersion>();
        }

        /*
         * return
         */
        return imageVersionsFound;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#getSortColumn(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public int getSortColumn() {
        return 0; // use the default sort criteria
    }
}

// end AbstractImageVersionResultsBlock
