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

import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionModesDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;

/**
 * AbstractMyDailySubscriptionsDigestBlock is the class describing and printing
 * all recently modified contents of a subscriber. Only modifications that have
 * occured within the last 24 hours are taken into account.
 * 
 * @version $Id: AbstractMyDailySubscriptionsDigestBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractMyDailySubscriptionsDigestBlock extends
        AbstractMySubscriptionsDigestBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractMyDailySubscriptionsDigestBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentsComplexResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractMyDailySubscriptionsDigestBlock) super.clone();
    }

    /**
     * Returns the number of days to look at.
     * 
     * @return an int
     */
    @Override
    public int getMaximumTimeGapInDays() {
        return 1;
    }

    /**
     * Returns the primary key of the no record found message.
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    @Override
    public PrimaryKey getNoRecordFoundPrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.INF_NO_DAILY_SUBSCRIBED_CONTENT_UPDATED);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscription#getModePrimaryKey()
     */
    @Override
    public PrimaryKey getModePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.DAILY);
    }
}

// end AbstractMyDailySubscriptionsDigestBlock
