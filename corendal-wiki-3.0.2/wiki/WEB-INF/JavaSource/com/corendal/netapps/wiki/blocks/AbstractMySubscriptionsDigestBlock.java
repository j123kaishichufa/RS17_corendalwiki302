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
import com.corendal.netapps.framework.core.dictionaries.MessagesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.DateUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractMyDailySubscriptionsDigestBlock is the class describing and printing
 * all recently modified contents of a subscriber.
 * 
 * @version $Id: AbstractMySubscriptionsDigestBlock.java,v 1.1 2005/09/06
 *          21:25:27 tdanard Exp $
 */
public abstract class AbstractMySubscriptionsDigestBlock extends
        AbstractContentsComplexResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractMySubscriptionsDigestBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentsComplexResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractMySubscriptionsDigestBlock) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * initialize the content complex results block
         */
        super.initStandardBlock();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * get the lower bound and the upper bound
         */
        Date today = this.getToday();
        Date upperBound = this.getUpperBound(today);
        Date lowerBound = this.getLowerBound(upperBound);

        /*
         * get today's date
         */
        String todayText = DateFormatUtil.getShortFormattedDateText(today);
        String todayEncoded = TextFormatUtil.getTextToEncoded(todayText);
        String todayHTML = TextFormatUtil.getTextToHTML(todayText);

        /*
         * get yesterday's date
         */
        String yesterdayText = DateFormatUtil
                .getShortFormattedDateText(lowerBound);
        String yesterdayEncoded = TextFormatUtil
                .getTextToEncoded(yesterdayText);
        String yesterdayHTML = TextFormatUtil.getTextToHTML(yesterdayText);

        /*
         * get the message
         */
        PrimaryKey messagePk = this.getNoRecordFoundPrimaryKey();
        StandardMessage noRecordFoundMessage = (StandardMessage) smf
                .findByPrimaryKey(messagePk);

        /*
         * replace in the message all placeholders
         */
        if (this.getMaximumTimeGapInDays() == 1) {
            noRecordFoundMessage.replaceMessageText(todayText, 1);
            noRecordFoundMessage.replaceMessageEncoded(todayEncoded, 1);
            noRecordFoundMessage.replaceMessageHTML(todayHTML, 1);
        } else {
            noRecordFoundMessage.replaceMessageText(yesterdayText, 1);
            noRecordFoundMessage.replaceMessageEncoded(yesterdayEncoded, 1);
            noRecordFoundMessage.replaceMessageHTML(yesterdayHTML, 1);

            noRecordFoundMessage.replaceMessageText(todayText, 2);
            noRecordFoundMessage.replaceMessageEncoded(todayEncoded, 2);
            noRecordFoundMessage.replaceMessageHTML(todayHTML, 2);
        }

        /*
         * set the message
         */
        this.setNoRecordFoundMessage(noRecordFoundMessage);
    }

    /**
     * Returns the number of days to look at.
     * 
     * @return an int representing the number of days to look at
     */
    public abstract int getMaximumTimeGapInDays();

    /**
     * Returns the primary key of the no record found message.
     * 
     * @return a PrimaryKey object
     */
    public abstract PrimaryKey getNoRecordFoundPrimaryKey();

    /**
     * Returns the primary key of mode.
     * 
     * @return a PrimaryKey object
     */
    public abstract PrimaryKey getModePrimaryKey();

    /**
     * Returns the day by default when no date is specified.
     * 
     * 
     * 
     * @return a java.util.Date object
     */
    public Date getDefaultDay() {
        return DateUtil.getRemovedDays(DateUtil.getTruncated(new Date()), 1);
    }

    /**
     * Returns the day requested by the user.
     * 
     * 
     * 
     * @return a java.util.Date object
     */
    public Date getRequestedDay() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * use the date in the http request
         */
        Date result = null;
        String requestedDateS = rm.getParameter(HTTPParameterConstants.DAY);

        if (!(StringUtil.getIsEmpty(requestedDateS))) {
            Date requestedDate = DateFormatUtil
                    .getParsedInternalFormattedDay(requestedDateS);

            if (requestedDate != null) {
                result = requestedDate;
            }
        }

        /*
         * return
         */
        return result;
    }

    /**
     * Returns the day requested by the user.
     * 
     * 
     * 
     * @return a java.util.Date object
     */
    public Date getToday() {
        /*
         * get the default day
         */
        Date result = this.getDefaultDay();

        /*
         * get the requested date
         */
        Date requestedDate = this.getRequestedDay();

        if (requestedDate != null) {
            result = requestedDate;
        }

        /*
         * return
         */
        return result;
    }

    /**
     * Returns the upper bound.
     * 
     * @param today
     *            date the email is sent for
     * 
     * 
     * @return a java.util.Date object
     */
    public Date getUpperBound(Date today) {
        return DateUtil.getAddedDays(today, 1);
    }

    /**
     * Returns the lower bound.
     * 
     * @param upperBound
     *            upper bound associated with the lower bound to return
     * 
     * 
     * @return a java.util.Date object
     */
    public Date getLowerBound(Date upperBound) {
        return DateUtil.getRemovedDays(upperBound, this
                .getMaximumTimeGapInDays());
    }

    /**
     * Returns the list of contents to print. Overrides
     * AbstractContentsComplexResultsBlock.getContentsFound.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    @Override
    protected List getContentsFound() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardArticleFactory sdocf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardImageFactory slf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * get all content types
         */
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
        PrimaryKey imageTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);

        /*
         * find the subscriber being viewed
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * get the lower bound and the upper bound
         */
        Date today = this.getToday();
        Date upperBound = this.getUpperBound(today);
        Date lowerBound = this.getLowerBound(upperBound);

        /*
         * get the current block title
         */
        String blockTitleText = this.getCurrentNameText();
        String blockTitleEncoded = this.getCurrentNameText();
        String blockTitleHTML = this.getCurrentNameText();

        /*
         * get today's date
         */
        String todayText = DateFormatUtil.getShortFormattedDateText(today);
        String todayEncoded = TextFormatUtil.getTextToEncoded(todayText);
        String todayHTML = TextFormatUtil.getTextToHTML(todayText);

        /*
         * change the block title
         */
        long timeGapInDays = DateUtil.getTimeGapInDays(lowerBound, upperBound);

        if (timeGapInDays == 1) {
            /*
             * add this date to the title
             */
            blockTitleText = ConcatUtil.getConcatWithBrackets(blockTitleText,
                    todayText, blockTitleText, todayText);
            blockTitleEncoded = ConcatUtil.getConcatWithBrackets(
                    blockTitleText, todayText, blockTitleEncoded, todayEncoded);
            blockTitleHTML = ConcatUtil.getConcatWithBrackets(blockTitleText,
                    todayText, blockTitleHTML, todayHTML);

            /*
             * set the title
             */
            this.setCurrentNameText(blockTitleText);
            this.setCurrentNameEncoded(blockTitleEncoded);
            this.setCurrentNameHTML(blockTitleHTML);
        } else {
            /*
             * get yesterday's date
             */
            String yesterdayText = DateFormatUtil
                    .getShortFormattedDateText(lowerBound);
            String yesterdayEncoded = TextFormatUtil
                    .getTextToEncoded(yesterdayText);
            String yesterdayHTML = TextFormatUtil.getTextToHTML(yesterdayText);

            /*
             * get the from to message
             */
            PrimaryKey fromToPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_FROM_TO);
            StandardMessage fromTo = (StandardMessage) smf
                    .findByPrimaryKey(fromToPk);

            /*
             * replace the placeholders
             */
            fromTo.replaceMessageText(todayText, 2);
            fromTo.replaceMessageText(yesterdayText, 1);
            fromTo.replaceMessageEncoded(todayEncoded, 2);
            fromTo.replaceMessageEncoded(yesterdayEncoded, 1);
            fromTo.replaceMessageHTML(todayHTML, 2);
            fromTo.replaceMessageHTML(yesterdayHTML, 1);

            /*
             * add this message to the title
             */
            blockTitleText = ConcatUtil.getConcatWithSpace(blockTitleText,
                    fromTo.getCurrentMessageText(), blockTitleText, fromTo
                            .getCurrentMessageText());
            blockTitleEncoded = ConcatUtil.getConcatWithSpace(blockTitleText,
                    fromTo.getCurrentMessageEncoded(), blockTitleEncoded,
                    fromTo.getCurrentMessageEncoded());
            blockTitleHTML = ConcatUtil.getConcatWithSpace(blockTitleText,
                    fromTo.getCurrentMessageHTML(), blockTitleHTML, fromTo
                            .getCurrentMessageHTML());

            /*
             * set the title
             */
            this.setCurrentNameText(blockTitleText);
            this.setCurrentNameEncoded(blockTitleEncoded);
            this.setCurrentNameHTML(blockTitleHTML);
        }

        /*
         * initialize the list of contents
         */
        List<StandardContent> contentsFound = new ArrayList<StandardContent>();

        /*
         * keep only the contents subscribed by the current user
         */
        List<StandardContent> contentsSubscribed = srf
                .findEnabledBySubscriberAndModePrimaryKeys(sa.getPrimaryKey(),
                        this.getModePrimaryKey());

        for (int i = 0; i < contentsSubscribed.size(); i++) {
            StandardContent currentContent = (StandardContent) contentsSubscribed
                    .get(i);

            /*
             * get the last time stamp
             */
            if (currentContent.getTypePrimaryKey().equals(articleTypePk)) {
                StandardArticle currentArticle = (StandardArticle) sdocf
                        .findByPrimaryKey(currentContent.getPrimaryKey());
                Date lastEntryLogTimestamp = currentArticle
                        .getLastEntryLogTimestamp(upperBound);

                if ((lastEntryLogTimestamp != null)
                        && (lastEntryLogTimestamp.compareTo(lowerBound) >= 0)) {
                    if (!(contentsFound.contains(currentContent))) {
                        contentsFound.add(currentContent);
                    }
                }
            } else if (currentContent.getTypePrimaryKey().equals(imageTypePk)) {
                StandardImage currentImage = (StandardImage) slf
                        .findByPrimaryKey(currentContent.getPrimaryKey());
                Date lastEntryLogTimestamp = currentImage
                        .getLastEntryLogTimestamp(upperBound);

                if ((lastEntryLogTimestamp != null)
                        && (lastEntryLogTimestamp.compareTo(lowerBound) >= 0)) {
                    if (!(contentsFound.contains(currentContent))) {
                        contentsFound.add(currentContent);
                    }
                }
            }
        }

        /*
         * return
         */
        return contentsFound;
    }
}

// end AbstractMySubscriptionsDigestBlock
