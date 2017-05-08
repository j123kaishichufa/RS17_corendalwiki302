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

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.ConfigManager;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.utils.DateUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionModesDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PropertiesDictionary;

/**
 * ContentDailyDigestBatch is the class sending weekly digest emails.
 * 
 * @version $Id: ContentWeeklyDigestBatch.java,v 1.1 2005/09/06 21:25:35 tdanard
 *          Exp $
 */
public class ContentWeeklyDigestBatch extends AbstractContentDigestBatch
        implements Cloneable {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.batches.ContentWeeklyDigestBatch";

    /**
     * Default class constructor.
     */
    public ContentWeeklyDigestBatch() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractDatabaseExecutableBatch.clone.
     */
    @Override
    public Object clone() {
        return (ContentWeeklyDigestBatch) super.clone();
    }

    /**
     * Returns the number of days to look at
     * 
     * @return a long
     */
    @Override
    public long getMaximumTimeGapInDays() {
        return 7;
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
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.WEEKLY);
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
                .getAlphanumericSingleKey(MessagesDictionary.DSP_WEEKLY_INDIVIDUAL_SUBSCRIPTION_SUBJECT);
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
                .getAlphanumericSingleKey(MessagesDictionary.DSP_WEEKLY_INDIVIDUAL_SUBSCRIPTION_BODY);
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
                .getAlphanumericSingleKey(MessagesDictionary.DSP_WEEKLY_GROUP_SUBSCRIPTION_SUBJECT);
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
                .getAlphanumericSingleKey(MessagesDictionary.DSP_WEEKLY_GROUP_SUBSCRIPTION_BODY);
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
                .getAlphanumericSingleKey(PagesDictionary.SUBSCRIPTIONS_WEEKLY_DIGEST);
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
                .getAlphanumericSingleKey(PagesDictionary.SUBSCRIPTIONS_WEEKLY_DIGEST);
    }

    /**
     * Execution method. Implements abstract method
     * AbstractDatabaseExecutableBatch.answerExecution.
     */
    @Override
    public void answerExecution() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ConfigManager cm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);

        /*
         * get the property set
         */
        StandardPropertySet prop = cm.getStandardPropertySet();

        /*
         * get the weekly digest day
         */
        PrimaryKey weeklyDigestDayPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PropertiesDictionary.WEEKLY_DIGEST_DAY);
        String weeklyDigestDayS = prop.getValueString(weeklyDigestDayPk);
        int weeklyDigestDay = Integer.parseInt(weeklyDigestDayS);

        /*
         * get the current date
         */
        Date today = DateUtil.getTruncated(new Date());

        /*
         * verify that today is the correct day of the week
         */
        if (DateUtil.getLastWeekDay(today, weeklyDigestDay).equals(today)) {
            this.notifyIndividualsAndGroups(today);
        }
    }
}
