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

import com.corendal.netapps.framework.core.blocks.AbstractStandardSearchResultsBlock;
import com.corendal.netapps.framework.core.dictionaries.EntitiesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ResultsBodyCell;
import com.corendal.netapps.framework.core.interfaces.ResultsHeaderCell;
import com.corendal.netapps.framework.core.interfaces.ResultsRow;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardAccountFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardField;
import com.corendal.netapps.framework.core.interfaces.StandardFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardSimpleAccount;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardFieldFactory;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.ResultsBodyCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsHeaderCellUtil;
import com.corendal.netapps.framework.core.utils.ResultsRowUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionModesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionTypesDictionary;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardContentAccessLog;
import com.corendal.netapps.wiki.interfaces.StandardContentAccessLogFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionMode;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionModeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionType;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentSubscriptionModeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentSubscriptionTypeFactory;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentAccessLogFactory;

/**
 * AbstractSubscribersBlock is the class describing and printing the list of
 * subscribers of a content.
 * 
 * @version $Id: AbstractSubscribersBlock.java,v 1.1 2005/09/06 21:25:27 tdanard
 *          Exp $
 */
public abstract class AbstractSubscribersBlock extends
        AbstractStandardSearchResultsBlock implements Cloneable {
    /** List of accounts found. */
    private ArrayList<StandardAccount> accountsFound;

    /** List of individual immediate accounts found. */
    private ArrayList<PrimaryKey> individualImmediateAccountsFound;

    /** List of individual daily accounts found. */
    private ArrayList<PrimaryKey> individualDailyAccountsFound;

    /** List of individual weekly accounts found. */
    private ArrayList<PrimaryKey> individualWeeklyAccountsFound;

    /** List of group immediate accounts found. */
    private ArrayList<PrimaryKey> groupImmediateAccountsFound;

    /** List of group daily accounts found. */
    private ArrayList<PrimaryKey> groupDailyAccountsFound;

    /** List of group weekly accounts found. */
    private ArrayList<PrimaryKey> groupWeeklyAccountsFound;

    /** Searched article or link. */
    private Searched searched;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractSubscribersBlock() {
        this.accountsFound = null;
        this.individualImmediateAccountsFound = null;
        this.individualDailyAccountsFound = null;
        this.individualWeeklyAccountsFound = null;
        this.groupImmediateAccountsFound = null;
        this.groupDailyAccountsFound = null;
        this.groupWeeklyAccountsFound = null;
        this.searched = null;
    }

    /**
     * Returns a clone. Overrides AbstractAccountSearchResultsBlock.clone.
     */
    @Override
    public Object clone() {
        AbstractSubscribersBlock result = (AbstractSubscribersBlock) super
                .clone();

        if (this.accountsFound != null) {
            result.accountsFound = (ArrayList<StandardAccount>) this.accountsFound
                    .clone();
        }

        if (this.individualImmediateAccountsFound != null) {
            result.individualImmediateAccountsFound = (ArrayList<PrimaryKey>) this.individualImmediateAccountsFound
                    .clone();
        }

        if (this.individualDailyAccountsFound != null) {
            result.individualDailyAccountsFound = (ArrayList<PrimaryKey>) this.individualDailyAccountsFound
                    .clone();
        }

        if (this.individualWeeklyAccountsFound != null) {
            result.individualWeeklyAccountsFound = (ArrayList<PrimaryKey>) this.individualWeeklyAccountsFound
                    .clone();
        }

        if (this.groupImmediateAccountsFound != null) {
            result.groupImmediateAccountsFound = (ArrayList<PrimaryKey>) this.groupImmediateAccountsFound
                    .clone();
        }

        if (this.groupDailyAccountsFound != null) {
            result.groupDailyAccountsFound = (ArrayList<PrimaryKey>) this.groupDailyAccountsFound
                    .clone();
        }

        if (this.groupWeeklyAccountsFound != null) {
            result.groupWeeklyAccountsFound = (ArrayList<PrimaryKey>) this.groupWeeklyAccountsFound
                    .clone();
        }

        if (this.searched != null) {
            result.searched = (Searched) this.searched.clone();
        }

        return result;
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

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardFieldFactory sff = (StandardFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardFieldFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * get the viewed searched
         */
        this.searched = this.getViewedSearched();

        /*
         * set the entity
         */
        PrimaryKey accountsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ACCOUNTS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(accountsPk);
        this.setStandardEntity(entity);

        /*
         * get the fields
         */
        PrimaryKey userLoginPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.FieldsDictionary.ACCOUNT_LOGIN);
        StandardField field1 = (StandardField) sff
                .findByPrimaryKey(userLoginPk);
        PrimaryKey userFullNamePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.FieldsDictionary.ACCOUNT_FULL_NAME);
        StandardField field2 = (StandardField) sff
                .findByPrimaryKey(userFullNamePk);
        PrimaryKey subscriptionTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.FieldsDictionary.CONTENT_SUBSCRIBER_TYPE);
        StandardField field3 = (StandardField) sff
                .findByPrimaryKey(subscriptionTypePk);
        PrimaryKey subscriptionModePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.FieldsDictionary.CONTENT_SUBSCRIBER_MODE);
        StandardField field4 = (StandardField) sff
                .findByPrimaryKey(subscriptionModePk);
        PrimaryKey subscriptionLastLoggedVisitPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.FieldsDictionary.CONTENT_SUBSCRIBER_LAST_LOGGED_VISIT);
        StandardField field5 = (StandardField) sff
                .findByPrimaryKey(subscriptionLastLoggedVisitPk);

        /*
         * create the results header cells
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
         * list all accounts
         */
        List accountsFound = this.getAccountsFound();

        /*
         * convert the list of entities into a list of results row
         */
        if (this.getIsBodyPrinted()) {
            for (int i = 0; i < accountsFound.size(); i++) {
                /*
                 * initialize the current row
                 */
                ResultsRow rr = ResultsRowUtil.getResultsRow();

                /*
                 * find the account
                 */
                StandardAccount currentAccount = (StandardAccount) accountsFound
                        .get(i);

                /*
                 * create the cells
                 */
                ResultsBodyCell rbc1 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, currentAccount.getLogin());
                ResultsBodyCell rbc2 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, currentAccount.getFullNameText());
                ResultsBodyCell rbc3 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell rbc4 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");
                ResultsBodyCell rbc5 = ResultsBodyCellUtil.getResultsBodyCell(
                        1, "");

                /*
                 * set the HTML value
                 */
                rbc1.setValueHTML(lw.getBreakableLinkHTML(currentAccount
                        .getDefaultLocation(), null, TextFormatUtil
                        .getTextToHTML(currentAccount.getLogin()), null));

                /*
                 * add these fields to the current row
                 */
                rr.addResultsBodyCell(rbc1);
                rr.addResultsBodyCell(rbc2);
                rr.addResultsBodyCell(rbc3);
                rr.addResultsBodyCell(rbc4);
                rr.addResultsBodyCell(rbc5);

                /*
                 * add the current row
                 */
                this.addResultsRow(rr);
            }

            /*
             * populate the table rows if needed
             */
            int reqSortColumn = this.getRequestedSortColumn();

            if ((reqSortColumn != 1) && (reqSortColumn != 2)) {
                if (reqSortColumn == 5) {
                    this.populateAllLastLoggedTableRows(0,
                            accountsFound.size() - 1);
                } else {
                    this.populateAllTypeOrModeTableRows(0,
                            accountsFound.size() - 1);
                }
            }
        }

        /*
         * set the counts of this block
         */
        this.wrapUp(accountsFound.size());
    }

    /**
     * Returns the content to be viewed.
     * 
     * 
     * 
     * @return an object implementing the Searched interface
     */
    public abstract Searched getViewedSearched();

    /**
     * Populate one results row with last log.
     * 
     * @param i
     *            index of the row to populate
     * @param accountPk
     *            primary key of the account to populate data for
     * 
     * 
     */
    public void populateOneLastLoggedTableRow(int i, PrimaryKey accountPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentAccessLogFactory sralf = (StandardContentAccessLogFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentAccessLogFactory.class);

        /*
         * get the body cells
         */
        ResultsRow resultsRow = this.getResultsRow(i);
        ResultsBodyCell rbc5 = resultsRow.getResultsBodyCell(4);
        PrimaryKey searchedPk = this.searched.getPrimaryKey();

        /*
         * get the date of the last logged visit
         */
        String lastLoggedVisitText = null;
        String lastLoggedVisitXLS = null;
        String lastLoggedVisitHTML = null;
        List<StandardContentAccessLog> accessLogs = sralf
                .findByContentAndAccountPrimaryKeys(searchedPk, accountPk);

        if (accessLogs.size() > 0) {
            StandardContentAccessLog lastContentAccessLog = (StandardContentAccessLog) accessLogs
                    .get(0);
            Date lastLog = lastContentAccessLog.getLastEntryLogTimestamp();
            lastLoggedVisitText = DateFormatUtil
                    .getInternalFormattedDateText(lastLog);
            lastLoggedVisitXLS = DateFormatUtil
                    .getLongExcelFormattedDateText(lastLog);
            lastLoggedVisitHTML = TextFormatUtil.getTextToHTML(DateFormatUtil
                    .getLongFormattedDateText(lastLog));
        }

        /*
         * associate the link to the first cell
         */
        rbc5.setValueText(lastLoggedVisitText);
        rbc5.setValueXLS(lastLoggedVisitXLS);
        rbc5.setValueHTML(lastLoggedVisitHTML);
    }

    /**
     * Populate one results row with type or mode.
     * 
     * @param i
     *            index of the row to populate
     * @param accountPk
     *            primary key of the account to populate data for
     * 
     * 
     */
    public void populateOneTypeOrModeTableRow(int i, PrimaryKey accountPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionTypeFactory srstf = (StandardContentSubscriptionTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionTypeFactory.class);
        StandardContentSubscriptionModeFactory srsmf = (StandardContentSubscriptionModeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionModeFactory.class);

        /*
         * get the body cells
         */
        ResultsRow resultsRow = this.getResultsRow(i);
        ResultsBodyCell rbc3 = resultsRow.getResultsBodyCell(2);
        ResultsBodyCell rbc4 = resultsRow.getResultsBodyCell(3);

        /*
         * get the list of modes
         */
        PrimaryKey immediateModePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.IMMEDIATE);
        PrimaryKey dailyModePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.DAILY);
        PrimaryKey weeklyModePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.WEEKLY);

        /*
         * get the booleans
         */
        boolean b1 = this.individualImmediateAccountsFound.contains(accountPk);
        boolean b2 = this.individualDailyAccountsFound.contains(accountPk);
        boolean b3 = this.individualWeeklyAccountsFound.contains(accountPk);
        boolean b4 = this.groupImmediateAccountsFound.contains(accountPk);
        boolean b5 = this.groupDailyAccountsFound.contains(accountPk);
        boolean b6 = this.groupWeeklyAccountsFound.contains(accountPk);

        /*
         * get the type of subscription
         */
        boolean isIndividual = b1 || b2 || b3;
        boolean isGroup = b4 || b5 || b6;

        /*
         * get the type and the mode of subscription
         */
        PrimaryKey typePk = null;
        List<PrimaryKey> modesFound = new ArrayList<PrimaryKey>();

        if ((isIndividual) && (isGroup)) {
            typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.BOTH_DISTRIBUTIONS);
        } else if (isIndividual) {
            typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.INDIVIDUAL_DISTRIBUTION);
        } else if (isGroup) {
            typePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.GROUP_DISTRIBUTION);
        }

        /*
         * get the mode of subscription
         */
        boolean isImmediate = b1 || b4;
        boolean isDaily = b2 || b5;
        boolean isWeekly = b3 || b6;

        /*
         * add the modes
         */
        if (isImmediate) {
            modesFound.add(immediateModePk);
        }

        if (isDaily) {
            modesFound.add(dailyModePk);
        }

        if (isWeekly) {
            modesFound.add(weeklyModePk);
        }

        /*
         * modify the description with the type
         */
        String typeText = null;

        if (typePk != null) {
            StandardContentSubscriptionType type = (StandardContentSubscriptionType) srstf
                    .findByPrimaryKey(typePk);
            typeText = type.getNameText();
        }

        /*
         * modify the description with the mode
         */
        String modeText = null;

        for (int j = 0; j < modesFound.size(); j++) {
            PrimaryKey modePk = modesFound.get(j);
            StandardContentSubscriptionMode mode = (StandardContentSubscriptionMode) srsmf
                    .findByPrimaryKey(modePk);
            String currentModeText = mode.getNameText();
            modeText = ConcatUtil.getConcatWithComma(modeText, currentModeText,
                    modeText, currentModeText);
        }

        /*
         * associate the link to the first cell
         */
        rbc3.setValueText(typeText);
        rbc4.setValueText(modeText);
    }

    /**
     * Populate results row when results rows are found.
     * 
     * @param start
     *            index of the first row to populate
     * @param finish
     *            index of the last row to populate
     * 
     * 
     */
    public void populateAllLastLoggedTableRows(int start, int finish) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);

        /*
         * fill the rows
         */
        for (int i = start; i <= finish; i++) {
            /*
             * get the body cells
             */
            ResultsRow resultsRow = this.getResultsRow(i);
            ResultsBodyCell rbc1 = resultsRow.getResultsBodyCell(0);

            /*
             * find the login
             */
            String login = rbc1.getValueText();

            /*
             * find the account
             */
            StandardSimpleAccount currentAccount = (StandardSimpleAccount) saf
                    .findByLogin(login);

            /*
             * populate for that account
             */
            populateOneLastLoggedTableRow(i, currentAccount.getPrimaryKey());
        }
    }

    /**
     * Populate results row when results rows are found.
     * 
     * @param start
     *            index of the first row to populate
     * @param finish
     *            index of the last row to populate
     * 
     * 
     */
    public void populateAllTypeOrModeTableRows(int start, int finish) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);

        /*
         * get the list of modes
         */
        PrimaryKey immediateModePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.IMMEDIATE);
        PrimaryKey dailyModePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.DAILY);
        PrimaryKey weeklyModePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.WEEKLY);

        /*
         * build the lists of subscribers
         */
        this.individualImmediateAccountsFound = (ArrayList<PrimaryKey>) ContentUtil
                .getRecursiveIndividualSubscribers(this.searched
                        .getPrimaryKey(), immediateModePk);
        this.individualDailyAccountsFound = (ArrayList<PrimaryKey>) ContentUtil
                .getRecursiveIndividualSubscribers(this.searched
                        .getPrimaryKey(), dailyModePk);
        this.individualWeeklyAccountsFound = (ArrayList<PrimaryKey>) ContentUtil
                .getRecursiveIndividualSubscribers(this.searched
                        .getPrimaryKey(), weeklyModePk);
        this.groupImmediateAccountsFound = (ArrayList<PrimaryKey>) ContentUtil
                .getRecursiveGroupSubscribers(this.searched.getPrimaryKey(),
                        immediateModePk);
        this.groupDailyAccountsFound = (ArrayList<PrimaryKey>) ContentUtil
                .getRecursiveGroupSubscribers(this.searched.getPrimaryKey(),
                        dailyModePk);
        this.groupWeeklyAccountsFound = (ArrayList<PrimaryKey>) ContentUtil
                .getRecursiveGroupSubscribers(this.searched.getPrimaryKey(),
                        weeklyModePk);

        /*
         * fill the rows
         */
        for (int i = start; i <= finish; i++) {
            /*
             * get the body cells
             */
            ResultsRow resultsRow = this.getResultsRow(i);
            ResultsBodyCell rbc1 = resultsRow.getResultsBodyCell(0);

            /*
             * find the login
             */
            String login = rbc1.getValueText();

            /*
             * find the account
             */
            StandardSimpleAccount currentAccount = saf.findByLogin(login);

            /*
             * populate for that account
             */
            populateOneTypeOrModeTableRow(i, currentAccount.getPrimaryKey());
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#printTableRows(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void printTableRows() {
        /*
         * set the start and finish variables
         */
        int start = this.getIndexFirstPrinted();
        int finish = this.getIndexLastPrinted();

        /*
         * populate the table rows if needed
         */
        int reqSortColumn = this.getRequestedSortColumn();

        if ((reqSortColumn != 1) && (reqSortColumn != 2)) {
            if (reqSortColumn == 5) {
                populateAllTypeOrModeTableRows(start, finish);
            } else {
                populateAllLastLoggedTableRows(start, finish);
            }
        } else {
            populateAllTypeOrModeTableRows(start, finish);
            populateAllLastLoggedTableRows(start, finish);
        }

        /*
         * call the print table rows method
         */
        super.printTableRows();
    }

    /**
     * Returns the list of accounts to print. Overrides
     * AbstractAccountSearchResultsBlock.getthis.accountFound.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    protected List getAccountsFound() {
        if (this.accountsFound == null) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardAccountFactory saf = (StandardAccountFactory) pfs
                    .getStandardBeanFactory(DefaultStandardAccountFactory.class);

            /*
             * build the list of subscribers
             */
            this.accountsFound = new ArrayList<StandardAccount>();

            if (this.searched != null) {
                List<PrimaryKey> primaryKeys = ContentUtil
                        .getRecursiveSubscribers(this.searched.getPrimaryKey(),
                                null);

                for (PrimaryKey accountPk : primaryKeys) {
                    StandardAccount currentAccount = (StandardAccount) saf
                            .findByPrimaryKey(accountPk);

                    if (currentAccount.getIsFound()) {
                        this.accountsFound.add(currentAccount);
                    }
                }
            }
        }

        /*
         * return
         */
        return this.accountsFound;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#getSortColumn(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public int getSortColumn() {
        return 1; // sort by login
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordName(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordName() {
        Searched sp = this.getViewedSearched();

        if (sp != null) {
            return sp.getNameText();
        } else {
            return null;
        }
    }
}

// end AbstractSubscribersBlock
