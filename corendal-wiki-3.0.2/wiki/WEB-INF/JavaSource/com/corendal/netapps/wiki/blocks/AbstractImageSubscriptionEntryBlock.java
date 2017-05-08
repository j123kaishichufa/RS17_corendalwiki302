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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.core.constants.AccessTypeConstants;
import com.corendal.netapps.framework.core.constants.CanvasTypeConstants;
import com.corendal.netapps.framework.core.constants.LayoutTypeConstants;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormMessageFactory;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.AccountHistoryManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardAccountFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormButton;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormField;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormLabel;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessage;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLookup;
import com.corendal.netapps.framework.core.interfaces.StandardLookupFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultAccountHistoryManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardLookupFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.helpdesk.dictionaries.LookupsDictionary;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionMode;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionModeFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentSubscriptionModeFactory;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractImageSubscriptionEntryBlock is the parent block common to all image
 * viewing blocks.
 * 
 * @version $Id: AbstractImageSubscriptionEntryBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractImageSubscriptionEntryBlock extends
        AbstractCommonContentEntryBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractImageSubscriptionEntryBlock";

    /** Subscriber entered by the user */
    private String subscriber;

    /** Subscription mode entered by the user */
    private String subscriptionMode;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractImageSubscriptionEntryBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractCommonContentEntryBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractImageSubscriptionEntryBlock) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * init the entry form block
         */
        super.initStandardBlock();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * set the entity
         */
        PrimaryKey contentSubscriptionsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_SUBSCRIPTIONS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(contentSubscriptionsPk);
        this.setStandardEntity(entity);
    }

    /**
     * Builds the layout of the form. This procedure is used when creating a
     * request.
     * 
     * 
     * 
     */
    private void build() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntryFormLabelFactory seflf = (StandardEntryFormLabelFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormLabelFactory.class);
        StandardEntryFormButtonFactory sefbf = (StandardEntryFormButtonFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormButtonFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardImageFactory sdocf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
        StandardLookupFactory slkf = (StandardLookupFactory) pfs
                .getStandardBeanFactory(DefaultStandardLookupFactory.class);
        StandardContentSubscriptionModeFactory srsmf = (StandardContentSubscriptionModeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionModeFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * get the entity of the form
         */
        StandardEntity entity = this.getStandardEntity();

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * find the image being subscribed to
         */
        String imageId = rm.getParameter(HTTPParameterConstants.IMAGE_ID);
        PrimaryKey imagePk = PrimaryKeyUtil.getAlphanumericSingleKey(imageId);
        StandardImage image = (StandardImage) sdocf.findByPrimaryKey(imagePk);

        /*
         * find the parent article
         */
        PrimaryKey parentArticlePk = image.getMainParentPrimaryKey();
        StandardArticle parentArticle = (StandardArticle) sdf
                .findByPrimaryKey(parentArticlePk);

        /*
         * set the layout type to multiple to provide a tabbing where the submit
         * button is the last object to reach
         */
        this.setLayoutType(LayoutTypeConstants.BORDER);

        /*
         * set the max sizes of the form
         */
        if (this.getAccessType().equals(AccessTypeConstants.NEW)) {
            this.setMaxSizes(2, 5, CanvasTypeConstants.CENTER); // 3

            // columns,
            // 4
            // lines
        } else {
            this.setMaxSizes(2, 4, CanvasTypeConstants.CENTER); // 3

            // columns,
            // 4
            // lines
        }

        this.setMaxSizes(2, 1, CanvasTypeConstants.SOUTH); // 2

        // columns,
        // 1 line

        /*
         * get the primary keys of the required instances
         */
        PrimaryKey entityPk = entity.getPrimaryKey();
        PrimaryKey nameFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
        PrimaryKey descriptionFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DESCRIPTION);
        PrimaryKey parentArticleFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PARENT_ARTICLE);
        PrimaryKey subscriptionModeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_SUBSCRIPTION_MODE);
        PrimaryKey subscriberFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_SUBSCRIBER);

        PrimaryKey nameLabelPk = seflf.getPrimaryKey(entityPk, nameFieldPk);
        PrimaryKey descriptionLabelPk = seflf.getPrimaryKey(entityPk,
                descriptionFieldPk);
        PrimaryKey parentArticleLabelPk = seflf.getPrimaryKey(entityPk,
                parentArticleFieldPk);
        PrimaryKey subscriptionModeLabelPk = seflf.getPrimaryKey(entityPk,
                subscriptionModeFieldPk);
        PrimaryKey subscriberLabelPk = seflf.getPrimaryKey(entityPk,
                subscriberFieldPk);

        /*
         * instantiate all lookups
         */
        PrimaryKey accountLookupPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(LookupsDictionary.ACCOUNT);
        StandardLookup accountLookup = (StandardLookup) slkf
                .findByPrimaryKey(accountLookupPk);

        /*
         * instantiate all entry fields
         */
        StandardEntryFormField nameField = (StandardEntryFormField) sefff
                .findByPrimaryKey(nameFieldPk);
        StandardEntryFormField descriptionField = (StandardEntryFormField) sefff
                .findByPrimaryKey(descriptionFieldPk);
        StandardEntryFormField parentArticleField = (StandardEntryFormField) sefff
                .findByPrimaryKey(parentArticleFieldPk);
        StandardEntryFormField subscriptionModeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(subscriptionModeFieldPk);
        StandardEntryFormField subscriberField = (StandardEntryFormField) sefff
                .findByPrimaryKey(subscriberFieldPk);

        if (this.getAccessType().equals(AccessTypeConstants.NEW)) {
            /*
             * set the list of content subscription modes
             */
            List<StandardReadBean> subscriptionModes = srsmf
                    .findByEnabledFlag("Y");
            ArrayList<String> optionValues = new ArrayList<String>();
            ArrayList<String> optionNames = new ArrayList<String>();

            for (int i = 0; i < subscriptionModes.size(); i++) {
                StandardContentSubscriptionMode currentSubscriptionMode = (StandardContentSubscriptionMode) subscriptionModes
                        .get(i);
                optionValues.add(currentSubscriptionMode.getPrimaryKey()
                        .toString());
                optionNames.add(currentSubscriptionMode.getNameText());
            }

            subscriptionModeField.setSelectType(optionValues, optionNames);

            /*
             * set the list of subscribers
             */
            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    parentArticlePk)) {
                subscriberField.setStandardLookup(accountLookup);
                subscriberField.setIsWildcardSubstituted(true);
            } else {
                List<StandardAccount> subscribersToAdd = ContentUtil
                        .getAccountsLeftToSubscribeByRequestor(sa, imagePk);

                ArrayList<String> values = new ArrayList<String>();

                for (int i = 0; i < subscribersToAdd.size(); i++) {
                    StandardAccount subscriber = (StandardAccount) subscribersToAdd
                            .get(i);
                    values.add(subscriber.getFullNameAndLoginText());
                }

                subscriberField.setSelectType(values, values);
            }
        } else {
            /*
             * set the list of subscribers
             */
            ArrayList<String> values = new ArrayList<String>();

            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    parentArticlePk)) {
                List<StandardAccount> subscribersToRemove = ContentUtil
                        .getAccountsLeftToUnsubscribeByEditor(imagePk);

                for (int i = 0; i < subscribersToRemove.size(); i++) {
                    StandardAccount subscriber = (StandardAccount) subscribersToRemove
                            .get(i);
                    values.add(subscriber.getFullNameAndLoginText());
                }
            } else {
                List<StandardAccount> subscribersToRemove = ContentUtil
                        .getAccountsLeftToUnsubscribeByRequestor(sa, imagePk);

                for (int i = 0; i < subscribersToRemove.size(); i++) {
                    StandardAccount subscriber = (StandardAccount) subscribersToRemove
                            .get(i);
                    values.add(subscriber.getFullNameAndLoginText());
                }
            }

            subscriberField.setSelectType(values, values);
        }

        /*
         * get the list of all accounts to subscribe or unsubscribe
         */
        List<StandardAccount> subscribers = null;

        if (this.getAccessType().equals(AccessTypeConstants.NEW)) {
            subscribers = ContentUtil.getAccountsLeftToSubscribeByRequestor(sa,
                    imagePk);
        } else {
            subscribers = ContentUtil.getAccountsLeftToUnsubscribeByRequestor(
                    sa, imagePk);
        }

        /*
         * instantiate all entry labels
         */
        StandardEntryFormLabel nameLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(nameLabelPk);
        StandardEntryFormLabel descriptionLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(descriptionLabelPk);
        StandardEntryFormLabel parentArticleLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(parentArticleLabelPk);
        StandardEntryFormLabel subscriptionModeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(subscriptionModeLabelPk);
        StandardEntryFormLabel subscriberLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(subscriberLabelPk);

        /*
         * associate them
         */
        nameLabel.associateStandardFormField(nameField);
        descriptionLabel.associateStandardFormField(descriptionField);
        parentArticleLabel.associateStandardFormField(parentArticleField);
        subscriptionModeLabel.associateStandardFormField(subscriptionModeField);
        subscriberLabel.associateStandardFormField(subscriberField);

        /*
         * set the mandatory objects
         */
        subscriberLabel.setAsMandatory();

        if (this.getAccessType().equals(AccessTypeConstants.NEW)) {
            subscriptionModeLabel.setAsMandatory();
            subscriptionModeField.setAsMandatory();
        }

        subscriberField.setAsMandatory();

        /*
         * set the appropriate fields in print only mode
         */
        nameField.setPrintOnlyType();
        descriptionField.setPrintOnlyType();
        parentArticleField.setPrintOnlyType();

        if (!(resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                parentArticlePk))) {
            if (subscribers.size() == 1) {
                subscriberField.setPrintOnlyType();
            }
        }

        /*
         * fill with default values
         */
        nameField.setFirstDefaultValue(image.getNameText());
        descriptionField.setFirstDefaultValue(image.getLongDescriptionText());
        parentArticleField.setFirstDefaultValue(parentArticle.getPathText());
        parentArticleField.setFirstDefaultValueHTML(parentArticle
                .getPathHTMLWithLink());
        subscriptionModeField.setFirstDefaultValue(this.subscriptionMode);
        subscriberField.setFirstDefaultValue(this.subscriber);

        if (!(resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                parentArticlePk))) {
            if (subscribers.size() == 1) {
                StandardAccount onlySubscriber = (StandardAccount) subscribers
                        .get(0);
                subscriberField.setFirstDefaultValue(onlySubscriber
                        .getFullNameAndLoginText());
            }
        }

        /*
         * set the current line number
         */
        int lineNumber = 0;

        /*
         * add those objects
         */
        this.setCurrentCanvasType(CanvasTypeConstants.CENTER);
        this.addStandardFormObject(nameLabel, 1, ++lineNumber);
        this.addStandardFormObject(nameField, 2, lineNumber);
        this.addStandardFormObject(descriptionLabel, 1, ++lineNumber);
        this.addStandardFormObject(descriptionField, 2, lineNumber);
        this.addStandardFormObject(parentArticleLabel, 1, ++lineNumber);
        this.addStandardFormObject(parentArticleField, 2, lineNumber);

        if (this.getAccessType().equals(AccessTypeConstants.NEW)) {
            this.addStandardFormObject(subscriptionModeLabel, 1, ++lineNumber);
            this.addStandardFormObject(subscriptionModeField, 2, lineNumber);
        }

        this.addStandardFormObject(subscriberLabel, 1, ++lineNumber);
        this.addStandardFormObject(subscriberField, 2, lineNumber);

        /*
         * instantiate and add the submit button
         */
        this.setCurrentCanvasType(CanvasTypeConstants.SOUTH);

        if (this.getAccessType().equals(AccessTypeConstants.NEW)) {
            PrimaryKey addSubscriptionPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.ADD_SUBSCRIPTION);
            StandardEntryFormButton addSubscription = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(addSubscriptionPk);
            this.addStandardFormObject(addSubscription, 1, 1);
        } else {
            PrimaryKey removeSubscriptionPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.REMOVE_SUBSCRIPTION);
            StandardEntryFormButton removeSubscription = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(removeSubscriptionPk);
            this.addStandardFormObject(removeSubscription, 1, 1);
        }

        /*
         * extract the data entries from the form for validation
         */
        this.subscriber = subscriberField.getRequestValue();
        this.subscriptionMode = subscriptionModeField.getRequestValue();
    }

    /**
     * Builds the layout of the form for a new record.
     * 
     * 
     * 
     */
    protected void buildNewRecord() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardImageFactory sdocf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * find the image being subscribed to
         */
        String imageId = rm.getParameter(HTTPParameterConstants.IMAGE_ID);
        PrimaryKey imagePk = PrimaryKeyUtil.getAlphanumericSingleKey(imageId);
        StandardImage image = (StandardImage) sdocf.findByPrimaryKey(imagePk);

        /*
         * verify that the image exists
         */
        if ((image.getIsFound()) && (image.getIsAccessible())) {
            /*
             * find the parent article primary key
             */
            PrimaryKey parentArticlePk = image.getMainParentPrimaryKey();

            /*
             * print the form
             */
            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    parentArticlePk)) {
                this.build();
            } else {
                /*
                 * get the list of all accounts to subscribe
                 */
                List<StandardAccount> subscribers = ContentUtil
                        .getAccountsLeftToSubscribeByRequestor(sa, image
                                .getPrimaryKey());

                /*
                 * verify whether the current user and its proxies have all
                 * subscribed
                 */
                if (subscribers.size() == 0) {
                    this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                    // column,
                    // 1
                    // line
                    PrimaryKey youAlreadyImageSubscriberPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_YOU_ALREADY_INDIVIDUAL_IMAGE_SUBSCRIBER);
                    StandardFormMessage youAlreadyImageSubscriberMessage = (StandardFormMessage) sfmf
                            .findByPrimaryKey(youAlreadyImageSubscriberPk);
                    StandardMessage imageMessage = youAlreadyImageSubscriberMessage
                            .getStandardMessage();
                    imageMessage.replaceMessageText(image.getNameText(), 1);
                    imageMessage.replaceMessageEncoded(image.getNameEncoded(),
                            1);
                    imageMessage.replaceMessageHTML(image.getNameHTML(), 1);
                    this.addStandardFormObject(
                            youAlreadyImageSubscriberMessage, 1, 1);
                } else {
                    this.build();
                }
            }
        } else {
            PrimaryKey contentsPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            StandardEntity entity = (StandardEntity) sef
                    .findByPrimaryKey(contentsPk);
            this.setStandardEntity(entity);
            this.buildNoRecordFound();
        }
    }

    /**
     * Builds the layout of the form for a record to remove.
     * 
     * 
     * 
     */
    protected void buildRemoveRecord() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardImageFactory sdocf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * find the image being subscribed to
         */
        String imageId = rm.getParameter(HTTPParameterConstants.IMAGE_ID);
        PrimaryKey imagePk = PrimaryKeyUtil.getAlphanumericSingleKey(imageId);
        StandardImage image = (StandardImage) sdocf.findByPrimaryKey(imagePk);

        /*
         * verify that the image exists
         */
        if ((image.getIsFound()) && (image.getIsAccessible())) {
            /*
             * find the parent article primary key
             */
            PrimaryKey parentArticlePk = image.getMainParentPrimaryKey();

            /*
             * print the form
             */
            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    parentArticlePk)) {
                /*
                 * get the list of subscriptions
                 */
                PrimaryKey individualTypesPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.INDIVIDUAL_DISTRIBUTION);
                List<StandardContentSubscription> contentSubscriptions = srsf
                        .findEnabledByContentAndTypePrimaryKeys(imagePk,
                                individualTypesPk);

                /*
                 * verify that the image has been subscribed to
                 */
                if (contentSubscriptions.size() > 0) {
                    this.build();
                } else {
                    this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                    // column,
                    // 1
                    // line
                    PrimaryKey noImageSubscriberPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_INDIVIDUAL_IMAGE_SUBSCRIBER);
                    StandardFormMessage noImageSubscriberMessage = (StandardFormMessage) sfmf
                            .findByPrimaryKey(noImageSubscriberPk);
                    StandardMessage imageMessage = noImageSubscriberMessage
                            .getStandardMessage();
                    imageMessage.replaceMessageText(image.getNameText(), 1);
                    imageMessage.replaceMessageEncoded(image.getNameEncoded(),
                            1);
                    imageMessage.replaceMessageHTML(image.getNameHTML(), 1);
                    this.addStandardFormObject(noImageSubscriberMessage, 1, 1);
                }
            } else {
                /*
                 * get the list of all accounts to unsubscribe
                 */
                List<StandardAccount> subscribers = ContentUtil
                        .getAccountsLeftToUnsubscribeByRequestor(sa, image
                                .getPrimaryKey());

                /*
                 * verify whether the current user or one its proxies has
                 * subscribed
                 */
                if (subscribers.size() == 0) {
                    this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                    // column,
                    // 1
                    // line
                    PrimaryKey noImageSubscriberPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_YOU_NOT_INDIVIDUAL_IMAGE_SUBSCRIBER);
                    StandardFormMessage noImageSubscriberMessage = (StandardFormMessage) sfmf
                            .findByPrimaryKey(noImageSubscriberPk);
                    StandardMessage imageMessage = noImageSubscriberMessage
                            .getStandardMessage();
                    imageMessage.replaceMessageText(image.getNameText(), 1);
                    imageMessage.replaceMessageEncoded(image.getNameEncoded(),
                            1);
                    imageMessage.replaceMessageHTML(image.getNameHTML(), 1);
                    this.addStandardFormObject(noImageSubscriberMessage, 1, 1);
                } else {
                    this.build();
                }
            }
        } else {
            PrimaryKey contentsPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            StandardEntity entity = (StandardEntity) sef
                    .findByPrimaryKey(contentsPk);
            this.setStandardEntity(entity);
            this.buildNoRecordFound();
        }
    }

    /**
     * Prints the validation specific to this form.
     * 
     * 
     * 
     * @throws IOException
     *             when a redirect occurs
     */
    public void validateExtraNewRecord() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardImageFactory sdocf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        AccountHistoryManager hm = (AccountHistoryManager) pms
                .getManager(DefaultAccountHistoryManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * find the image being subscribed to
         */
        String imageId = rm.getParameter(HTTPParameterConstants.IMAGE_ID);
        PrimaryKey imagePk = PrimaryKeyUtil.getAlphanumericSingleKey(imageId);
        StandardImage image = (StandardImage) sdocf.findByPrimaryKey(imagePk);

        /*
         * find the parent article
         */
        PrimaryKey parentArticlePk = image.getMainParentPrimaryKey();

        /*
         * check whether there is an account with that subscriber name
         */
        List<PrimaryKey> accounts = null;

        if (this.getIsValid()) {
            /*
             * check the subscriber
             */
            accounts = saf.findEnabledPrimaryKeysByNameOrLogin(this.subscriber);

            /*
             * verify only one account matches
             */
            int size = accounts.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no account could be found, print the "no found" message.
                 * otherwise, print the "too many accounts found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_ACCOUNT_FOR_SUBSCRIBER);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey subscriberPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_SUBSCRIBER);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(subscriberPk);
                    field.setFocusIfFirst(this.getFormName());
                } else {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_ACCOUNTS_FOR_SUBSCRIBER);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey subscriberPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_SUBSCRIBER);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(subscriberPk);
                    field.setFocusIfFirst(this.getFormName());
                }
            } else {
                /*
                 * add this account to the history
                 */
                PrimaryKey accountPk = (PrimaryKey) accounts.get(0);
                hm.add(accountPk);
            }
        }

        /*
         * verify that this account is not already subscribed
         */
        if (this.getIsValid()) {
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(image.getPrimaryKey());
            PrimaryKey subscriberPk = (PrimaryKey) accounts.get(0);

            if (content.getIsDirectlySubscribedByIndividual(subscriberPk)) {
                /*
                 * invalidate the form
                 */
                this.setIsValid(false);

                /*
                 * get the error message
                 */
                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_ALREADY_INDIVIDUAL_IMAGE_SUBSCRIBER);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);

                /*
                 * get the subscriber
                 */
                StandardAccount subscriber = (StandardAccount) saf
                        .findByPrimaryKey(subscriberPk);

                /*
                 * add the name of the subscriber to the message
                 */
                error.replaceMessageText(subscriber.getFullNameAndLoginText(),
                        1);
                error.replaceMessageEncoded(subscriber
                        .getFullNameAndLoginEncoded(), 1);
                error.replaceMessageHTML(subscriber
                        .getFullNameAndLoginHTMLWithLink(), 1);
                error.printBufferWithIconHTML();

                /*
                 * set the focus
                 */
                PrimaryKey subscriberFieldPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(FieldsDictionary.CONTENT_SUBSCRIBER);
                StandardEntryFormField field = (StandardEntryFormField) sefff
                        .findByPrimaryKey(subscriberFieldPk);
                field.setFocusIfFirst(this.getFormName());
            }
        }

        /*
         * try to create the content subscription
         */
        StandardContentSubscription contentSubscription = null;

        if (this.getIsValid()) {
            PrimaryKey individualTypesPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.INDIVIDUAL_DISTRIBUTION);
            PrimaryKey immediateModePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.subscriptionMode);
            contentSubscription = (StandardContentSubscription) srsf.create(
                    image.getPrimaryKey(), individualTypesPk, immediateModePk);
            this.setIsValid(contentSubscription.getIsDone());

            if (!(contentSubscription.getIsDone())) {
                MessageUtil.printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                        contentSubscription.getStoreTrace());
            }
        }

        /*
         * try to store the subscriber
         */
        StandardAccount subscriber = null;

        if (this.getIsValid()) {
            PrimaryKey subscriberPk = (PrimaryKey) accounts.get(0);
            subscriber = (StandardAccount) saf.findByPrimaryKey(subscriberPk);
            contentSubscription.storeSubscriberAccount(subscriberPk);
            this.setIsValid(contentSubscription.getIsDone());

            if (!(contentSubscription.getIsDone())) {
                MessageUtil.printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                        contentSubscription.getStoreTrace());
            }
        }

        /*
         * print the acknowledgment information
         */
        if (this.getIsValid()) {
            /*
             * get the validation message
             */
            PrimaryKey validationPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.VLD_NEW_INDIVIDUAL_SUBSCRIPTION_CREATED);
            StandardMessage validation = (StandardMessage) smf
                    .findByPrimaryKey(validationPk);

            /*
             * add the name of the image to the message
             */
            validation.replaceMessageText(subscriber.getFullNameAndLoginText(),
                    1);
            validation.replaceMessageEncoded(subscriber
                    .getFullNameAndLoginEncoded(), 1);
            validation.replaceMessageHTML(subscriber
                    .getFullNameAndLoginHTMLWithLink(), 1);

            /*
             * print the message
             */
            validation.printBufferWithIconHTML();
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect the user to the article detail page. otherwise,
         * rollback the current transaction
         */
        if (this.getIsValid()) {
            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    parentArticlePk)) {
                /*
                 * redirect to the same page
                 */
                String redirectURL = rm.getStandardPage().getAbsoluteLocation();
                redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                        HTTPParameterConstants.IMAGE_ID, imageId);
                redirectURL = HTTPUtil
                        .getAddedParameterURL(
                                redirectURL,
                                com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                this.getPrimaryKey().toString());
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
            } else {
                /*
                 * get the list of remaining account subscribers
                 */
                List<StandardAccount> accountsLeftToSubscribeByRequestor = ContentUtil
                        .getAccountsLeftToSubscribeByRequestor(sa, imagePk);

                /*
                 * redirect to the same page if accounts are left
                 */
                if (accountsLeftToSubscribeByRequestor.size() == 0) {
                    String redirectURL = image.getPropertiesAbsoluteLocation();
                    redirectURL = HTTPUtil
                            .getAddedParameterURL(
                                    redirectURL,
                                    com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                    BlocksDictionary.IMAGE_SUBSCRIBERS);
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                } else {
                    String redirectURL = rm.getStandardPage()
                            .getAbsoluteLocation();
                    redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                            HTTPParameterConstants.IMAGE_ID, imageId);
                    redirectURL = HTTPUtil
                            .getAddedParameterURL(
                                    redirectURL,
                                    com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                    this.getPrimaryKey().toString());
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                }
            }
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }
    }

    /**
     * Prints the validation specific to this form.
     * 
     * 
     * 
     * @throws IOException
     *             when a redirect occurs
     */
    public void validateExtraRemoveRecord() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardImageFactory sdocf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        AccountHistoryManager hm = (AccountHistoryManager) pms
                .getManager(DefaultAccountHistoryManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * find the image being subscribed to
         */
        String imageId = rm.getParameter(HTTPParameterConstants.IMAGE_ID);
        PrimaryKey imagePk = PrimaryKeyUtil.getAlphanumericSingleKey(imageId);
        StandardImage image = (StandardImage) sdocf.findByPrimaryKey(imagePk);

        /*
         * find the parent article
         */
        PrimaryKey parentArticlePk = image.getMainParentPrimaryKey();

        /*
         * check whether there is an account with that subscriber name
         */
        List<PrimaryKey> accounts = null;

        if (this.getIsValid()) {
            /*
             * check the subscriber
             */
            accounts = saf.findEnabledPrimaryKeysByNameOrLogin(this.subscriber);

            /*
             * verify only one account matches
             */
            int size = accounts.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no account could be found, print the "no found" message.
                 * otherwise, print the "too many accounts found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_ACCOUNT_FOR_SUBSCRIBER);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey subscriberPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_SUBSCRIBER);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(subscriberPk);
                    field.setFocusIfFirst(this.getFormName());
                } else {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_ACCOUNTS_FOR_SUBSCRIBER);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey subscriberPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_SUBSCRIBER);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(subscriberPk);
                    field.setFocusIfFirst(this.getFormName());
                }
            } else {
                /*
                 * add this account to the history
                 */
                PrimaryKey accountPk = (PrimaryKey) accounts.get(0);
                hm.add(accountPk);
            }
        }

        /*
         * verify that this account is already subscribed
         */
        if (this.getIsValid()) {
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(image.getPrimaryKey());
            PrimaryKey subscriberPk = (PrimaryKey) accounts.get(0);

            if (!(content.getIsDirectlySubscribedByIndividual(subscriberPk))) {
                /*
                 * invalidate the form
                 */
                this.setIsValid(false);

                /*
                 * get the error message
                 */
                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_NOT_INDIVIDUAL_IMAGE_SUBSCRIBER);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);

                /*
                 * get the subscriber
                 */
                StandardAccount subscriber = (StandardAccount) saf
                        .findByPrimaryKey(subscriberPk);

                /*
                 * add the name of the subscriber to the message
                 */
                error.replaceMessageText(subscriber.getFullNameAndLoginText(),
                        1);
                error.replaceMessageEncoded(subscriber
                        .getFullNameAndLoginEncoded(), 1);
                error.replaceMessageHTML(subscriber
                        .getFullNameAndLoginHTMLWithLink(), 1);
                error.printBufferWithIconHTML();

                /*
                 * set the focus
                 */
                PrimaryKey subscriberFieldPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(FieldsDictionary.CONTENT_SUBSCRIBER);
                StandardEntryFormField field = (StandardEntryFormField) sefff
                        .findByPrimaryKey(subscriberFieldPk);
                field.setFocusIfFirst(this.getFormName());
            }
        }

        /*
         * find the subscriber
         */
        StandardAccount subscriber = null;

        if (this.getIsValid()) {
            PrimaryKey subscriberPk = (PrimaryKey) accounts.get(0);
            subscriber = (StandardAccount) saf.findByPrimaryKey(subscriberPk);
        }

        /*
         * try to remove the content subscription(s)
         */
        if (this.getIsValid()) {
            List<StandardContentSubscription> contentSubscriptions = srsf
                    .findEnabledByContentAndIndividualSubscriberPrimaryKeys(
                            image.getPrimaryKey(), subscriber.getPrimaryKey());

            for (int i = 0; i < contentSubscriptions.size(); i++) {
                if (this.getIsValid()) {
                    StandardContentSubscription contentSubscription = (StandardContentSubscription) contentSubscriptions
                            .get(i);
                    contentSubscription.remove();
                    this.setIsValid(contentSubscription.getIsDone());

                    if (!(contentSubscription.getIsDone())) {
                        MessageUtil.printBufferInternalErrorHTML(
                                ABSTRACT_CLASS_NAME, contentSubscription
                                        .getStoreTrace());
                    }
                }
            }
        }

        /*
         * print the acknowledgment information
         */
        if (this.getIsValid()) {
            /*
             * get the validation message
             */
            PrimaryKey validationPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.VLD_INDIVIDUAL_SUBSCRIPTION_REMOVED);
            StandardMessage validation = (StandardMessage) smf
                    .findByPrimaryKey(validationPk);

            /*
             * add the name of the image to the message
             */
            validation.replaceMessageText(subscriber.getFullNameAndLoginText(),
                    1);
            validation.replaceMessageEncoded(subscriber
                    .getFullNameAndLoginEncoded(), 1);
            validation.replaceMessageHTML(subscriber
                    .getFullNameAndLoginHTMLWithLink(), 1);

            /*
             * print the message
             */
            validation.printBufferWithIconHTML();
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect the user to the article detail page. otherwise,
         * rollback the current transaction
         */
        if (this.getIsValid()) {
            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    parentArticlePk)) {
                /*
                 * get the list of remaining account subscribers
                 */
                List<StandardAccount> accountsLeftToUnsubscribeByEditor = ContentUtil
                        .getAccountsLeftToUnsubscribeByEditor(imagePk);

                /*
                 * redirect to the same page if accounts are left
                 */
                if (accountsLeftToUnsubscribeByEditor.size() == 0) {
                    String redirectURL = image.getPropertiesAbsoluteLocation();
                    redirectURL = HTTPUtil
                            .getAddedParameterURL(
                                    redirectURL,
                                    com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                    BlocksDictionary.IMAGE_SUBSCRIBERS);
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                } else {
                    String redirectURL = rm.getStandardPage()
                            .getAbsoluteLocation();
                    redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                            HTTPParameterConstants.IMAGE_ID, imageId);
                    redirectURL = HTTPUtil
                            .getAddedParameterURL(
                                    redirectURL,
                                    com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                    this.getPrimaryKey().toString());
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                }
            } else {
                /*
                 * get the list of remaining account subscribers
                 */
                List<StandardAccount> accountsLeftToUnsubscribeByRequestor = ContentUtil
                        .getAccountsLeftToUnsubscribeByRequestor(sa, imagePk);

                /*
                 * redirect to the same page if accounts are left
                 */
                if (accountsLeftToUnsubscribeByRequestor.size() == 0) {
                    String redirectURL = image.getPropertiesAbsoluteLocation();
                    redirectURL = HTTPUtil
                            .getAddedParameterURL(
                                    redirectURL,
                                    com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                    BlocksDictionary.IMAGE_SUBSCRIBERS);
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                } else {
                    String redirectURL = rm.getStandardPage()
                            .getAbsoluteLocation();
                    redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                            HTTPParameterConstants.IMAGE_ID, imageId);
                    redirectURL = HTTPUtil
                            .getAddedParameterURL(
                                    redirectURL,
                                    com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                    this.getPrimaryKey().toString());
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                }
            }
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }
    }

    /**
     * Sets the default subscriber to use when this form is printed for the
     * first time.
     * 
     * @param subscriber
     *            subscriber to set
     */
    public void setFirstTimeSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    /**
     * Sets the default subscription mode to use when this form is printed for
     * the first time.
     * 
     * @param subscriptionMode
     *            subscription mode to set
     */
    public void setFirstTimeSubscriptionMode(String subscriptionMode) {
        this.subscriptionMode = subscriptionMode;
    }
}

// end AbstractImageSubscriptionEntryBlock
