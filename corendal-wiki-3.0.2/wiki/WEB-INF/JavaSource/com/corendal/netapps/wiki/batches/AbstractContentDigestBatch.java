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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.corendal.netapps.framework.core.dictionaries.RolesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.ConfigManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardEmail;
import com.corendal.netapps.framework.core.interfaces.StandardEmailFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.DateUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardEmailFactory;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupMembership;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupMembershipFactory;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupMembershipFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.framework.runtime.batches.AbstractDatabaseExecutableBatch;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionTypesDictionary;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractContentDigestBatch is the abstract class sending digest
 * notifications.
 * 
 * @version $Id: AbstractContentDigestBatch.java,v 1.1 2005/09/06 21:25:35
 *          tdanard Exp $
 */
public abstract class AbstractContentDigestBatch extends
        AbstractDatabaseExecutableBatch implements Cloneable {
    /**
     * Default class constructor.
     */
    public AbstractContentDigestBatch() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractDatabaseExecutableBatch.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentDigestBatch) super.clone();
    }

    /**
     * Returns the number of days to look at.
     * 
     * @return a long representing the number of days to look at
     */
    public abstract long getMaximumTimeGapInDays();

    /**
     * Returns the number of milliseconds to look at.
     * 
     * @return a long
     */
    public long getMaximumTimeGapInMilliseconds() {
        return this.getMaximumTimeGapInDays() * 1000 * 60 * 60 * 24;
    }

    /**
     * Returns the primary key of the subscription mode.
     * 
     * @return a PrimaryKey object
     */
    public abstract PrimaryKey getModePrimaryKey();

    /**
     * Adds one recipient to a list. Recipient can be individual or group.
     * 
     * @param recipients
     *            list of recipients being built
     * @param recipientPk
     *            primary key of the recipient to add
     */
    public void addRecipient(List<PrimaryKey> recipients, PrimaryKey recipientPk) {
        if (!(recipients.contains(recipientPk))) {
            recipients.add(recipientPk);
        }
    }

    /**
     * Returns the primary key of the individual subject message.
     * 
     * @return a PrimaryKey object
     */
    public abstract PrimaryKey getIndividualSubjectMessagePrimaryKey();

    /**
     * Returns the primary key of the individual body message.
     * 
     * @return a PrimaryKey object
     */
    public abstract PrimaryKey getIndividualBodyMessagePrimaryKey();

    /**
     * Returns the primary key of the group subject message.
     * 
     * @return a PrimaryKey object
     */
    public abstract PrimaryKey getGroupSubjectMessagePrimaryKey();

    /**
     * Returns the primary key of the group body message.
     * 
     * @return a PrimaryKey object
     */
    public abstract PrimaryKey getGroupBodyMessagePrimaryKey();

    /**
     * Returns the primary key of the page to lead to for individual
     * subscriptions.
     * 
     * @return a PrimaryKey object
     */
    public abstract PrimaryKey getIndividualPagePrimaryKey();

    /**
     * Returns the primary key of the page to lead to for group subscriptions.
     * 
     * @return a PrimaryKey object
     */
    public abstract PrimaryKey getGroupPagePrimaryKey();

    /**
     * Sends an email to all individuals.
     * 
     * @param today
     *            date the email is sent for
     * @param recipients
     *            list of recipients to notify
     * 
     * 
     */
    public void notifyIndividuals(Date today, List<PrimaryKey> recipients) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEmailFactory semf = (StandardEmailFactory) pfs
                .getStandardBeanFactory(DefaultStandardEmailFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ConfigManager cm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);

        /*
         * get the to recipient role primary key
         */
        PrimaryKey emailToPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.EMAIL_TO);

        /*
         * get the property set
         */
        StandardPropertySet prop = cm.getStandardPropertySet();

        /*
         * get the company name and the application name
         */
        String companyNameText = prop.getCompanyNameText();
        String applicationNameText = prop.getApplicationNameText();

        /*
         * get the primary keys of the messages
         */
        PrimaryKey subjectMessagePk = this
                .getIndividualSubjectMessagePrimaryKey();
        PrimaryKey bodyMessagePk = this.getIndividualBodyMessagePrimaryKey();

        /*
         * get the associated messages
         */
        StandardMessage subjectMessage = (StandardMessage) smf
                .findByPrimaryKey(subjectMessagePk);
        StandardMessage bodyMessage = (StandardMessage) smf
                .findByPrimaryKey(bodyMessagePk);

        /*
         * get the location of the screen to lead to
         */
        PrimaryKey pagePk = this.getIndividualPagePrimaryKey();
        StandardPage page = (StandardPage) spf.findByPrimaryKey(pagePk);
        String locationText = page.getAbsoluteLocation();

        /*
         * add the execution date
         */
        String dayText = DateFormatUtil.getInternalFormattedDayText(DateUtil
                .getRemovedDays(today, 1));
        locationText = HTTPUtil.getAddedParameterURL(locationText,
                HTTPParameterConstants.DAY, dayText);

        /*
         * fill the placeholders
         */
        bodyMessage.replaceMessageText(companyNameText, 1);
        bodyMessage.replaceMessageText(applicationNameText, 2);
        bodyMessage.replaceMessageText(locationText, 4);

        /*
         * notify each individual
         */
        for (/*
                 * get the individual
                 */
        PrimaryKey individualPk : recipients) {

            /*
             * create the email
             */
            StandardEmail email = (StandardEmail) semf.create(subjectMessage
                    .getCurrentMessageText(), bodyMessage
                    .getCurrentMessageText());

            /*
             * try to set the sender and recipient
             */
            if (email != null) {
                email.storeFromPrimaryKey(individualPk);
                email.addRecipient(individualPk, emailToPk);
            }

            /*
             * try to send the email immediately
             */
            if (email != null) {
                email.sendNow();
            }
        }
    }

    /**
     * Sends an email to all groups.
     * 
     * @param today
     *            date the email is sent for
     * @param notRecipients
     *            list of persons to exclude from notifications
     * @param recipients
     *            list of persons to include in notifications
     * 
     * 
     */
    public void notifyGroups(Date today, List<PrimaryKey> notRecipients,
            List<PrimaryKey> recipients) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEmailFactory semf = (StandardEmailFactory) pfs
                .getStandardBeanFactory(DefaultStandardEmailFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);
        StandardGroupMembershipFactory sgmf = (StandardGroupMembershipFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupMembershipFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ConfigManager cm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);

        /*
         * get the to recipient role primary key
         */
        PrimaryKey emailToPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.EMAIL_TO);

        /*
         * get the property set
         */
        StandardPropertySet prop = cm.getStandardPropertySet();

        /*
         * get the company name and the application name
         */
        String companyNameText = prop.getCompanyNameText();
        String applicationNameText = prop.getApplicationNameText();

        /*
         * get the primary keys of the messages
         */
        PrimaryKey subjectMessagePk = this.getGroupSubjectMessagePrimaryKey();
        PrimaryKey bodyMessagePk = this.getGroupBodyMessagePrimaryKey();

        /*
         * get the associated messages
         */
        StandardMessage subjectMessage = (StandardMessage) smf
                .findByPrimaryKey(subjectMessagePk);
        StandardMessage bodyMessage = (StandardMessage) smf
                .findByPrimaryKey(bodyMessagePk);

        /*
         * get the location of the screen to lead to
         */
        PrimaryKey pagePk = this.getGroupPagePrimaryKey();
        StandardPage page = (StandardPage) spf.findByPrimaryKey(pagePk);
        String locationText = page.getAbsoluteLocation();

        /*
         * add the execution date
         */
        String dayText = DateFormatUtil.getInternalFormattedDayText(DateUtil
                .getRemovedDays(today, 1));
        locationText = HTTPUtil.getAddedParameterURL(locationText,
                HTTPParameterConstants.DAY, dayText);

        /*
         * fill the placeholders
         */
        bodyMessage.replaceMessageText(companyNameText, 1);
        bodyMessage.replaceMessageText(applicationNameText, 2);
        bodyMessage.replaceMessageText(locationText, 4);

        /*
         * initialize the list of members to notify
         */
        List<StandardAccount> members = new ArrayList<StandardAccount>();

        /*
         * fill the list of members to notify
         */
        for (/*
                 * get the group
                 */
        PrimaryKey groupPk : recipients) {

            /*
             * notify the members of each group
             */
            List<StandardGroupMembership> memberships = sgmf
                    .findEnabledByGroupPrimaryKey(groupPk);

            /*
             * add each member
             */
            for (int j = 0; j < memberships.size(); j++) {
                StandardGroupMembership currentMembership = memberships.get(j);
                StandardAccount member = currentMembership.getStandardAccount();

                if (member.getIsFound()) {
                    if (!(members.contains(member))) {
                        if (!(notRecipients.contains(member))) {
                            members.add(member);
                        }
                    }
                }
            }
        }

        /*
         * notify each member
         */
        for (/*
                 * get the recipient
                 */
        StandardAccount currentAccount : members) {

            /*
             * create the email
             */
            StandardEmail email = (StandardEmail) semf.create(subjectMessage
                    .getCurrentMessageText(), bodyMessage
                    .getCurrentMessageText());

            /*
             * try to set the sender and recipient
             */
            if (email != null) {
                email.storeFromPrimaryKey(currentAccount.getPrimaryKey());
                email.addRecipient(currentAccount.getPrimaryKey(), emailToPk);
            }

            /*
             * try to send the email immediately
             */
            if (email != null) {
                email.sendNow();
            }
        }
    }

    /**
     * Sends an email to all individuals and groups.
     * 
     * @param today
     *            date the email is sent for
     * 
     * 
     */
    public void notifyIndividualsAndGroups(Date today) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardArticleFactory sdocf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardImageFactory slf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);

        /*
         * initialize the number of contents superseded
         */
        List<PrimaryKey> individualRecipients = new ArrayList<PrimaryKey>();
        List<PrimaryKey> groupRecipients = new ArrayList<PrimaryKey>();

        /*
         * get the different types of subscription
         */
        PrimaryKey individualTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.INDIVIDUAL_DISTRIBUTION);

        /*
         * find all daily or weekly subscriptions
         */
        PrimaryKey requiredModePk = this.getModePrimaryKey();
        List<StandardContentSubscription> foundSubscriptions = srsf
                .findEnabledByModePrimaryKey(requiredModePk);

        /*
         * evaluate each article subscription
         */
        for (/*
                 * get the content subcription
                 */
        StandardContentSubscription currentSubscription : foundSubscriptions) {

            /*
             * get the content being subscribed to
             */
            PrimaryKey contentPk = currentSubscription.getContentPrimaryKey();
            StandardArticle article = (StandardArticle) sdocf
                    .findByPrimaryKey(contentPk);

            /*
             * verify the last supersede date
             */
            if (article.getIsFound()) {
                Date lastSupersedeTimestamp = article
                        .getLastEntryLogTimestamp(today);

                if (lastSupersedeTimestamp != null) {
                    Date lastSupersedeDate = lastSupersedeTimestamp;
                    long timeGapinMilliseconds = DateUtil
                            .getTimeGapInMilliseconds(lastSupersedeDate, today);

                    if ((timeGapinMilliseconds > 0)
                            && (timeGapinMilliseconds <= this
                                    .getMaximumTimeGapInMilliseconds())) {
                        /*
                         * add the subscriber
                         */
                        if (currentSubscription.getTypePrimaryKey().equals(
                                individualTypesPk)) {
                            StandardAccount currentAccount = currentSubscription
                                    .getSubscriberStandardAccount();
                            this.addRecipient(individualRecipients,
                                    currentAccount.getPrimaryKey());
                        } else {
                            StandardGroup currentGroup = currentSubscription
                                    .getSubscriberStandardGroup();
                            this.addRecipient(groupRecipients, currentGroup
                                    .getPrimaryKey());
                        }
                    }
                }
            }
        }

        /*
         * evaluate each image subscription
         */
        for (/*
                 * get the content subcription
                 */
        StandardContentSubscription currentSubscription : foundSubscriptions) {

            /*
             * get the content being subscribed to
             */
            PrimaryKey contentPk = currentSubscription.getContentPrimaryKey();
            StandardImage image = (StandardImage) slf
                    .findByPrimaryKey(contentPk);

            /*
             * verify the last supersede date
             */
            if (image.getIsFound()) {
                Date lastSupersedeTimestamp = image
                        .getLastEntryLogTimestamp(today);

                if (lastSupersedeTimestamp != null) {
                    Date lastSupersedeDate = lastSupersedeTimestamp;
                    long timeGapinMilliseconds = DateUtil
                            .getTimeGapInMilliseconds(lastSupersedeDate, today);

                    if ((timeGapinMilliseconds > 0)
                            && (timeGapinMilliseconds <= this
                                    .getMaximumTimeGapInMilliseconds())) {
                        /*
                         * add the subscriber
                         */
                        if (currentSubscription.getTypePrimaryKey().equals(
                                individualTypesPk)) {
                            StandardAccount currentAccount = currentSubscription
                                    .getSubscriberStandardAccount();
                            this.addRecipient(individualRecipients,
                                    currentAccount.getPrimaryKey());
                        } else {
                            StandardGroup currentGroup = currentSubscription
                                    .getSubscriberStandardGroup();
                            this.addRecipient(groupRecipients, currentGroup
                                    .getPrimaryKey());
                        }
                    }
                }
            }
        }

        /*
         * notify all individuals
         */
        this.notifyIndividuals(today, individualRecipients);

        /*
         * notify all groups
         */
        this.notifyGroups(today, individualRecipients, groupRecipients);
    }
}
