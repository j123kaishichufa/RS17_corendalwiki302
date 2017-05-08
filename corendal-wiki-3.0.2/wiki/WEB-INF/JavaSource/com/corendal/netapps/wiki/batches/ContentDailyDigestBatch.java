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
package com.corendal.netapps.wiki.batches;

import java.util.Date;

import com.corendal.netapps.framework.core.utils.DateUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionModesDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;

/**
 * ContentDailyDigestBatch is the class sending daily digest emails.
 * 
 * @version $Id: ContentDailyDigestBatch.java,v 1.1 2005/09/06 21:25:35 tdanard
 *          Exp $
 */
public class ContentDailyDigestBatch extends AbstractContentDigestBatch
        implements Cloneable {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.batches.ContentDailyDigestBatch";

    /**
     * Default class constructor.
     */
    public ContentDailyDigestBatch() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractDatabaseExecutableBatch.clone.
     */
    @Override
    public Object clone() {
        return (ContentDailyDigestBatch) super.clone();
    }

    /**
     * Returns the number of days to look at
     * 
     * @return a long
     */
    @Override
    public long getMaximumTimeGapInDays() {
        return 1;
    }

    /**
     * Returns the primary key of the subscription mode.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    @Override
    public PrimaryKey getModePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.DAILY);
    }

    /**
     * Returns the primary key of the individual subject message.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    @Override
    public PrimaryKey getIndividualSubjectMessagePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_DAILY_INDIVIDUAL_SUBSCRIPTION_SUBJECT);
    }

    /**
     * Returns the primary key of the individual body message.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    @Override
    public PrimaryKey getIndividualBodyMessagePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_DAILY_INDIVIDUAL_SUBSCRIPTION_BODY);
    }

    /**
     * Returns the primary key of the group subject message.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    @Override
    public PrimaryKey getGroupSubjectMessagePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_DAILY_GROUP_SUBSCRIPTION_SUBJECT);
    }

    /**
     * Returns the primary key of the group body message.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    @Override
    public PrimaryKey getGroupBodyMessagePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_DAILY_GROUP_SUBSCRIPTION_BODY);
    }

    /**
     * Returns the primary key of the page to lead to for individual
     * subscriptions.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    @Override
    public PrimaryKey getIndividualPagePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.SUBSCRIPTIONS_DAILY_DIGEST);
    }

    /**
     * Returns the primary key of the page to lead to for group subscriptions.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    @Override
    public PrimaryKey getGroupPagePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.SUBSCRIPTIONS_DAILY_DIGEST);
    }

    /**
     * Execution method. Implements abstract method
     * AbstractDatabaseExecutableBatch.answerExecution.
     */
    @Override
    public void answerExecution() {
        /*
         * get the current date
         */
        Date today = DateUtil.getTruncated(new Date());

        /*
         * execute
         */
        this.notifyIndividualsAndGroups(today);
    }
}
