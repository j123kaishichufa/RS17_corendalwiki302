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

import com.corendal.netapps.framework.configuration.utils.CaseUtil;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionModesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionTypesDictionary;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionMode;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionModeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionType;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentSubscriptionModeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentSubscriptionTypeFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentSubscriptionFactory;

/**
 * AbstractSubscriptionsViewBlock is the class describing and printing all
 * subscriptions for a user.
 * 
 * @version $Id: AbstractSubscriptionsViewBlock.java,v 1.1 2005/09/06 21:25:27
 *          tdanard Exp $
 */
public abstract class AbstractSubscriptionsViewBlock extends
        AbstractContentsComplexResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractSubscriptionsViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentsComplexResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractSubscriptionsViewBlock) super.clone();
    }

    /**
     * Returns the standard account to view.
     * 
     * 
     * 
     * @return a StandardAccount object
     */
    public abstract StandardAccount getViewedAccount();

    /**
     * Returns the description of a content.
     * 
     * @return a java.lang.String object
     */
    @Override
    public String getContentDescriptionText(Searched content) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionTypeFactory srstf = (StandardContentSubscriptionTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionTypeFactory.class);
        StandardContentSubscriptionModeFactory srsmf = (StandardContentSubscriptionModeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionModeFactory.class);
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);

        /*
         * get the current account
         */
        StandardAccount sa = this.getViewedAccount();
        PrimaryKey accountPk = sa.getPrimaryKey();

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
         * get the list of types
         */
        PrimaryKey groupTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.GROUP_DISTRIBUTION);
        PrimaryKey individualTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.INDIVIDUAL_DISTRIBUTION);

        /*
         * get the individual and group subscriptions associated with the
         * content
         */
        List<StandardContentSubscription> groupContentSubscriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(
                        content.getPrimaryKey(), groupTypesPk);
        List<StandardContentSubscription> contentSubscriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(
                        content.getPrimaryKey(), individualTypesPk);

        /*
         * verify each individual subscription
         */
        List<PrimaryKey> individualModePrimaryKeys = new ArrayList<PrimaryKey>();
        for (int i = 0; i < contentSubscriptions.size(); i++) {
            StandardContentSubscription currentSubscription = (StandardContentSubscription) contentSubscriptions
                    .get(i);
            PrimaryKey currentModePk = currentSubscription.getModePrimaryKey();

            if (!(individualModePrimaryKeys.contains(currentModePk))) {
                StandardAccount subscriber = currentSubscription
                        .getSubscriberStandardAccount();

                if ((subscriber != null)
                        && (subscriber.getPrimaryKey().equals(accountPk))) {
                    individualModePrimaryKeys.add(currentModePk);
                }
            }
        }

        /*
         * verify each group subscription
         */
        List<PrimaryKey> groupModePrimaryKeys = new ArrayList<PrimaryKey>();
        for (int i = 0; i < groupContentSubscriptions.size(); i++) {
            StandardContentSubscription currentSubscription = (StandardContentSubscription) groupContentSubscriptions
                    .get(i);
            PrimaryKey currentModePk = currentSubscription.getModePrimaryKey();

            if (!(groupModePrimaryKeys.contains(currentModePk))) {
                StandardGroup currentSubscriber = currentSubscription
                        .getSubscriberStandardGroup();

                if ((currentSubscriber != null)
                        && (currentSubscriber.getIsFound())) {
                    if (currentSubscriber
                            .getHasRecentEffectiveMember(accountPk)) {
                        groupModePrimaryKeys.add(currentModePk);
                    }
                }
            }
        }

        /*
         * get the booleans
         */
        boolean b1 = individualModePrimaryKeys.contains(immediateModePk);
        boolean b2 = individualModePrimaryKeys.contains(dailyModePk);
        boolean b3 = individualModePrimaryKeys.contains(weeklyModePk);
        boolean b4 = groupModePrimaryKeys.contains(immediateModePk);
        boolean b5 = groupModePrimaryKeys.contains(dailyModePk);
        boolean b6 = groupModePrimaryKeys.contains(weeklyModePk);

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
         * get the original description
         */
        String result = super.getContentDescriptionText(content);

        /*
         * modify the description with the type
         */
        String typeText = null;

        if (typePk != null) {
            StandardContentSubscriptionType type = (StandardContentSubscriptionType) srstf
                    .findByPrimaryKey(typePk);
            typeText = CaseUtil.getLowerCaseDeleteAccents(type.getNameText());
        }

        /*
         * modify the description with the mode
         */
        String modeText = null;

        for (PrimaryKey modePk : modesFound) {
            StandardContentSubscriptionMode mode = (StandardContentSubscriptionMode) srsmf
                    .findByPrimaryKey(modePk);
            String currentModeText = CaseUtil.getLowerCaseDeleteAccents(mode
                    .getNameText());
            modeText = ConcatUtil.getConcatWithComma(modeText, currentModeText,
                    modeText, currentModeText);
        }

        /*
         * build the additional text
         */
        String additionalText = null;

        if ((typeText != null) && (modeText != null)) {
            additionalText = ConcatUtil.getConcatWithComma(typeText, modeText,
                    typeText, modeText);
        } else if (typeText != null) {
            additionalText = typeText;
        } else if (modeText != null) {
            additionalText = modeText;
        }

        /*
         * modify the text
         */
        result = ConcatUtil.getConcatWithBrackets(result, additionalText,
                result, additionalText);

        /*
         * return
         */
        return result;
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

        /*
         * find the subscriber being viewed
         */
        StandardAccount subscriber = this.getViewedAccount();

        /*
         * build the list of contents
         */
        List<StandardContent> contentsFound;

        if ((subscriber != null) && (subscriber.getIsFound())) {
            contentsFound = srf.findEnabledBySubscriberPrimaryKey(subscriber
                    .getPrimaryKey());
        } else {
            contentsFound = new ArrayList<StandardContent>();
        }

        /*
         * return
         */
        return contentsFound;
    }
}

// end AbstractSubscriptionsViewBlock
