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

import com.corendal.netapps.framework.core.blocks.AbstractStandardSearchResultsBlock;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ResultsBodyCell;
import com.corendal.netapps.framework.core.interfaces.ResultsHeaderCell;
import com.corendal.netapps.framework.core.interfaces.ResultsRow;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardField;
import com.corendal.netapps.framework.core.interfaces.StandardFieldFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardFieldFactory;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.ResultsBodyCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsHeaderCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsRowUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.interfaces.StandardContentAccessDay;

/**
 * AbstractContentAccessDayResultsBlock is the class describing and printing all
 * content access days used in the application.
 * 
 * @version $Id: AbstractContentAccessDayResultsBlock.java,v 1.1 2005/09/06
 *          21:25:27 tdanard Exp $
 */
public abstract class AbstractContentAccessDayResultsBlock extends
        AbstractStandardSearchResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractContentAccessDayResultsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardSearchResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentAccessDayResultsBlock) super.clone();
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
        PrimaryKey requestsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_ACCESS_DAYS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(requestsPk);
        this.setStandardEntity(entity);

        /*
         * get the fields
         */
        PrimaryKey reportDateFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ACCESS_DAY_REPORT_DATE);
        StandardField field1 = (StandardField) sff
                .findByPrimaryKey(reportDateFieldPk);
        PrimaryKey accessNumPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ACCESS_DAY_ACCESS_NUM);
        StandardField field2 = (StandardField) sff
                .findByPrimaryKey(accessNumPk);
        PrimaryKey accessRankPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ACCESS_DAY_RANK_NUM);
        StandardField field3 = (StandardField) sff
                .findByPrimaryKey(accessRankPk);

        /*
         * create the table header
         */
        ResultsHeaderCell rhc1 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field1);
        ResultsHeaderCell rhc2 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field2);
        ResultsHeaderCell rhc3 = ResultsHeaderCellUtil.getResultsHeaderCell(1,
                field3);
        this.addResultsHeaderCell(rhc1);
        this.addResultsHeaderCell(rhc2);
        this.addResultsHeaderCell(rhc3);

        /*
         * list all content access days found
         */
        List contentAccessDaysFound = this.getContentAccessDaysFound();

        /*
         * convert the list of content access days into a list of results row
         */
        for (int i = 0; i < contentAccessDaysFound.size(); i++) {
            /*
             * initialize the current row
             */
            ResultsRow rr = ResultsRowUtil.getResultsRow();

            /*
             * find the content access day
             */
            StandardContentAccessDay currentContentAccessDay = (StandardContentAccessDay) contentAccessDaysFound
                    .get(i);

            /*
             * add this content access day to the current row
             */
            ResultsBodyCell srbc1 = ResultsBodyCellUtil
                    .getResultsBodyCell(
                            1,
                            DateFormatUtil
                                    .getInternalFormattedDateText(currentContentAccessDay
                                            .getAccessDate()));
            srbc1.setValueXLS(DateFormatUtil
                    .getShortExcelFormattedDateText(currentContentAccessDay
                            .getAccessDate()));
            srbc1.setValueHTML(DateFormatUtil
                    .getShortFormattedDateText(currentContentAccessDay
                            .getAccessDate()));

            /*
             * add the associated access number
             */
            ResultsBodyCell srbc2 = ResultsBodyCellUtil.getResultsBodyCell(1,
                    String.valueOf(currentContentAccessDay.getAccessNum()));

            /*
             * add the access rank
             */
            ResultsBodyCell srbc3 = ResultsBodyCellUtil.getResultsBodyCell(1,
                    String.valueOf(currentContentAccessDay.getAccessRank()));

            /*
             * gather the cells into a row
             */
            rr.addResultsBodyCell(srbc1);
            rr.addResultsBodyCell(srbc2);
            rr.addResultsBodyCell(srbc3);

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
     * Returns the list of content access days to print.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    protected List getContentAccessDaysFound() {
        /*
         * build the list of content access days
         */
        List<StandardContentAccessDay> contentAccessDaysFound;

        if (this.getIsSearchFormUsed()) {
            contentAccessDaysFound = this.getResultsNoRedirect();
        } else {
            contentAccessDaysFound = new ArrayList<StandardContentAccessDay>();
        }

        /*
         * return
         */
        return contentAccessDaysFound;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#getSortColumn(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public int getSortColumn() {
        return 0; // use the default sort criteria
    }
}

// end AbstractContentAccessDayResultsBlock
