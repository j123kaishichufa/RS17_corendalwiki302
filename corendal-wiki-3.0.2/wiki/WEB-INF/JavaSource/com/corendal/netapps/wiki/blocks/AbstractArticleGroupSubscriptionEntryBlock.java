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
import java.util.Collections;
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
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
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
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardLookupFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.helpdesk.dictionaries.LookupsDictionary;
import com.corendal.netapps.framework.helpdesk.interfaces.GroupHistoryManager;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupFactory;
import com.corendal.netapps.framework.helpdesk.managers.DefaultGroupHistoryManager;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionMode;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionModeFactory;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentSubscriptionModeFactory;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentSubscriptionFactory;

/**
 * AbstractArticleGroupSubscriptionEntryBlock is the parent block contexts to
 * all article viewing blocks.
 * 
 * @version $Id: AbstractArticleGroupSubscriptionEntryBlock.java,v 1.1
 *          2005/09/06 21:25:27 tdanard Exp $
 */
public abstract class AbstractArticleGroupSubscriptionEntryBlock extends
        AbstractCommonContentEntryBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractArticleGroupSubscriptionEntryBlock";

    /** Subscriber entered by the group */
    private String subscriber;

    /** Subscription mode entered by the group */
    private String subscriptionMode;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractArticleGroupSubscriptionEntryBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractCommonContentEntryBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractArticleGroupSubscriptionEntryBlock) super.clone();
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
        StandardArticleFactory sdocf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardLookupFactory slkf = (StandardLookupFactory) pfs
                .getStandardBeanFactory(DefaultStandardLookupFactory.class);
        StandardContentSubscriptionModeFactory srsmf = (StandardContentSubscriptionModeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionModeFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

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
         * get the entity of the form
         */
        StandardEntity entity = this.getStandardEntity();

        /*
         * find the article being subscribed to
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(articleId);
        StandardArticle article = (StandardArticle) sdocf
                .findByPrimaryKey(articlePk);

        /*
         * find the parent article
         */
        PrimaryKey parentArticlePk = article.getMainParentPrimaryKey();
        StandardArticle parentArticle = null;
        if (parentArticlePk != null) {
            parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(parentArticlePk);
        }

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
        PrimaryKey groupLookupPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(LookupsDictionary.GROUP);
        StandardLookup groupLookup = (StandardLookup) slkf
                .findByPrimaryKey(groupLookupPk);

        /*
         * add the "create group" message to the lookup
         */
        PrimaryKey newGroupMessagePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_GROUP);
        StandardMessage newGroupMessage = (StandardMessage) smf
                .findByPrimaryKey(newGroupMessagePk);

        PrimaryKey addNewGroupPagePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.ADD_NEW_GROUP);
        StandardPage addNewGroupPage = (StandardPage) spf
                .findByPrimaryKey(addNewGroupPagePk);
        String addNewGroupLocation = addNewGroupPage.getDefaultLocation();

        groupLookup.setAdditionalHTML(lw.getBreakableLinkHTML(
                addNewGroupLocation, null, newGroupMessage
                        .getCurrentMessageHTML(), null));

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
            subscriberField.setStandardLookup(groupLookup);
            subscriberField.setIsWildcardSubstituted(true);
        } else {
            /*
             * get the list of subscribers
             */
            List<StandardGroup> subscribersToRemove = ContentUtil
                    .getGroupsLeftToUnsubscribeByEditor(articlePk);
            Collections.sort(subscribersToRemove);

            /*
             * populate the values
             */
            ArrayList<String> values = new ArrayList<String>();

            for (StandardGroup currentGroup : subscribersToRemove) {
                values.add(currentGroup.getNameText());
            }

            subscriberField.setSelectType(values, values);
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

        /*
         * fill with default values
         */
        nameField.setFirstDefaultValue(article.getNameText());
        descriptionField.setFirstDefaultValue(article.getLongDescriptionText());
        if (parentArticle != null) {
            parentArticleField
                    .setFirstDefaultValue(parentArticle.getPathText());
            parentArticleField.setFirstDefaultValueHTML(parentArticle
                    .getPathHTMLWithLink());
        }
        subscriptionModeField.setFirstDefaultValue(this.subscriptionMode);
        subscriberField.setFirstDefaultValue(this.subscriber);

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
        StandardArticleFactory sdocf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
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
         * find the article being subscribed to
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(articleId);
        StandardArticle article = (StandardArticle) sdocf
                .findByPrimaryKey(articlePk);

        /*
         * verify that the article exists
         */
        if ((article.getIsFound()) && (article.getIsVisible())) {
            /*
             * verify that the current user is the editor
             */
            if (resm.getIsRecursiveProxyOrAssociateEditor(sa.getPrimaryKey(),
                    articlePk)) {
                this.build();
            } else {
                this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                // column,
                // 1
                // line
                PrimaryKey notAllowedPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_ONLY_EDITOR_CAN_EDIT_ARTICLE_GROUP_SUBSCRIPTIONS);
                StandardFormMessage notAllowedMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(notAllowedPk);
                this.addStandardFormObject(notAllowedMessage, 1, 1);
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
        StandardArticleFactory sdocf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
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
         * find the article being subscribed to
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(articleId);
        StandardArticle article = (StandardArticle) sdocf
                .findByPrimaryKey(articlePk);

        /*
         * verify that the article exists
         */
        if ((article.getIsFound()) && (article.getIsVisible())) {
            /*
             * verify that the current user is the editor
             */
            if (resm.getIsRecursiveProxyOrAssociateEditor(sa.getPrimaryKey(),
                    articlePk)) {
                /*
                 * get the list of subscribers
                 */
                ArrayList<String> values = new ArrayList<String>();
                PrimaryKey groupTypesPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.GROUP_DISTRIBUTION);
                List<StandardContentSubscription> contentSubcriptions = srsf
                        .findEnabledByContentAndTypePrimaryKeys(articlePk,
                                groupTypesPk);

                for (int i = 0; i < contentSubcriptions.size(); i++) {
                    StandardContentSubscription contentSubscription = (StandardContentSubscription) contentSubcriptions
                            .get(i);
                    StandardGroup subscriber = contentSubscription
                            .getSubscriberStandardGroup();

                    if ((subscriber != null) && (subscriber.getIsFound())) {
                        values.add(subscriber.getNameText());
                    }
                }

                /*
                 * print the form
                 */
                if (values.size() > 0) {
                    this.build();
                } else {
                    this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                    // column,
                    // 1
                    // line
                    PrimaryKey noGroupSubscriberPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_GROUP_ARTICLE_SUBSCRIBER);
                    StandardFormMessage noGroupSubscriber = (StandardFormMessage) sfmf
                            .findByPrimaryKey(noGroupSubscriberPk);
                    StandardMessage noGroupSubscriberMessage = noGroupSubscriber
                            .getStandardMessage();
                    noGroupSubscriberMessage.replaceMessageText(article
                            .getNameText(), 1);
                    noGroupSubscriberMessage.replaceMessageEncoded(article
                            .getNameEncoded(), 1);
                    noGroupSubscriberMessage.replaceMessageHTML(article
                            .getNameHTML(), 1);
                    this.addStandardFormObject(noGroupSubscriber, 1, 1);
                }
            } else {
                this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                // column,
                // 1
                // line
                PrimaryKey notAllowedPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_ONLY_EDITOR_CAN_EDIT_ARTICLE_GROUP_SUBSCRIPTIONS);
                StandardFormMessage notAllowedMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(notAllowedPk);
                this.addStandardFormObject(notAllowedMessage, 1, 1);
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
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardArticleFactory sdocf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        GroupHistoryManager hm = (GroupHistoryManager) pms
                .getManager(DefaultGroupHistoryManager.class);

        /*
         * find the article being subscribed to
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(articleId);
        StandardArticle article = (StandardArticle) sdocf
                .findByPrimaryKey(articlePk);

        /*
         * check whether there is an group with that subscriber name
         */
        List<PrimaryKey> groups = null;

        if (this.getIsValid()) {
            /*
             * check the subscriber
             */
            groups = sgf.findEnabledPrimaryKeysByName(this.subscriber);

            /*
             * verify only one group matches
             */
            int size = groups.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no group could be found, print the "no found" message.
                 * otherwise, print the "too many groups found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_GROUP_FOR_SUBSCRIBER);
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
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_GROUPS_FOR_SUBSCRIBER);
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
                 * add this group to the history
                 */
                PrimaryKey groupPk = groups.get(0);
                hm.add(groupPk);
            }
        }

        /*
         * verify that this group is not already subscribed
         */
        if (this.getIsValid()) {
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(article.getPrimaryKey());
            PrimaryKey subscriberPk = groups.get(0);

            if (content.getIsDirectlySubscribedByGroup(subscriberPk)) {
                /*
                 * invalidate the form
                 */
                this.setIsValid(false);

                /*
                 * get the error message
                 */
                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_GROUP_ALREADY_ARTICLE_SUBSCRIBER);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);

                /*
                 * get the subscriber
                 */
                StandardGroup subscriber = (StandardGroup) sgf
                        .findByPrimaryKey(subscriberPk);

                /*
                 * add the name of the subscriber to the message
                 */
                error.replaceMessageText(subscriber.getNameText(), 1);
                error.replaceMessageEncoded(subscriber.getNameEncoded(), 1);
                error.replaceMessageHTML(subscriber.getNameHTML(), 1);
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
            PrimaryKey groupTypesPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.GROUP_DISTRIBUTION);
            PrimaryKey immediateModePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.subscriptionMode);
            contentSubscription = (StandardContentSubscription) srsf.create(
                    article.getPrimaryKey(), groupTypesPk, immediateModePk);
            this.setIsValid(contentSubscription.getIsDone());

            if (!(contentSubscription.getIsDone())) {
                MessageUtil.printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                        contentSubscription.getStoreTrace());
            }
        }

        /*
         * try to store the subscriber
         */
        StandardGroup subscriber = null;

        if (this.getIsValid()) {
            PrimaryKey subscriberPk = groups.get(0);
            subscriber = (StandardGroup) sgf.findByPrimaryKey(subscriberPk);
            contentSubscription.storeSubscriberGroup(subscriberPk);
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
                    .getAlphanumericSingleKey(MessagesDictionary.VLD_NEW_GROUP_SUBSCRIPTION_CREATED);
            StandardMessage validation = (StandardMessage) smf
                    .findByPrimaryKey(validationPk);

            /*
             * add the name of the article to the message
             */
            validation.replaceMessageText(subscriber.getNameText(), 1);
            validation.replaceMessageEncoded(subscriber.getNameEncoded(), 1);
            validation.replaceMessageHTML(subscriber.getNameHTML(), 1);

            /*
             * print the message
             */
            validation.printBufferWithIconHTML();
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect the group to the article detail page. otherwise,
         * rollback the current transaction
         */
        if (this.getIsValid()) {
            String redirectURL = rm.getStandardPage().getAbsoluteLocation();
            redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                    HTTPParameterConstants.ARTICLE_ID, articleId);
            redirectURL = HTTPUtil
                    .getAddedParameterURL(
                            redirectURL,
                            com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                            this.getPrimaryKey().toString());
            ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                    .commitAndSendAbsoluteRedirect(redirectURL);
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
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardArticleFactory sdocf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        GroupHistoryManager hm = (GroupHistoryManager) pms
                .getManager(DefaultGroupHistoryManager.class);

        /*
         * find the article being subscribed to
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(articleId);
        StandardArticle article = (StandardArticle) sdocf
                .findByPrimaryKey(articlePk);

        /*
         * check whether there is an group with that subscriber name
         */
        List<PrimaryKey> groups = null;

        if (this.getIsValid()) {
            /*
             * check the subscriber
             */
            groups = sgf.findEnabledPrimaryKeysByName(this.subscriber);

            /*
             * verify only one group matches
             */
            int size = groups.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no group could be found, print the "no found" message.
                 * otherwise, print the "too many groups found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_GROUP_FOR_SUBSCRIBER);
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
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_GROUPS_FOR_SUBSCRIBER);
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
                 * add this group to the history
                 */
                PrimaryKey groupPk = groups.get(0);
                hm.add(groupPk);
            }
        }

        /*
         * verify that this group is already subscribed
         */
        if (this.getIsValid()) {
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(article.getPrimaryKey());
            PrimaryKey subscriberPk = groups.get(0);

            if (!(content.getIsDirectlySubscribedByGroup(subscriberPk))) {
                /*
                 * invalidate the form
                 */
                this.setIsValid(false);

                /*
                 * get the error message
                 */
                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_NOT_GROUP_ARTICLE_SUBSCRIBER);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);

                /*
                 * get the subscriber
                 */
                StandardGroup subscriber = (StandardGroup) sgf
                        .findByPrimaryKey(subscriberPk);

                /*
                 * add the name of the subscriber to the message
                 */
                error.replaceMessageText(subscriber.getNameText(), 1);
                error.replaceMessageEncoded(subscriber.getNameEncoded(), 1);
                error.replaceMessageHTML(subscriber.getNameHTML(), 1);
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
        StandardGroup subscriber = null;

        if (this.getIsValid()) {
            PrimaryKey subscriberPk = groups.get(0);
            subscriber = (StandardGroup) sgf.findByPrimaryKey(subscriberPk);
        }

        /*
         * try to remove the content subscription(s)
         */
        if (this.getIsValid()) {
            List<StandardContentSubscription> contentSubscriptions = srsf
                    .findEnabledByContentAndGroupSubscriberPrimaryKeys(article
                            .getPrimaryKey(), subscriber.getPrimaryKey());

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
                    .getAlphanumericSingleKey(MessagesDictionary.VLD_GROUP_SUBSCRIPTION_REMOVED);
            StandardMessage validation = (StandardMessage) smf
                    .findByPrimaryKey(validationPk);

            /*
             * add the name of the article to the message
             */
            validation.replaceMessageText(subscriber.getNameText(), 1);
            validation.replaceMessageEncoded(subscriber.getNameEncoded(), 1);
            validation.replaceMessageHTML(subscriber.getNameHTML(), 1);

            /*
             * print the message
             */
            validation.printBufferWithIconHTML();
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect. otherwise, rollback the current transaction
         */
        if (this.getIsValid()) {
            /*
             * get the list of remaining group subscribers
             */
            List<StandardGroup> groupsLeftToUnsubscribeByEditor = ContentUtil
                    .getGroupsLeftToUnsubscribeByEditor(articlePk);

            /*
             * redirect to the same page if groups are left
             */
            if (groupsLeftToUnsubscribeByEditor.size() == 0) {
                String redirectURL = article.getPropertiesAbsoluteLocation();
                redirectURL = HTTPUtil
                        .getAddedParameterURL(
                                redirectURL,
                                com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                BlocksDictionary.ARTICLE_SUBSCRIBERS);
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
            } else {
                String redirectURL = rm.getStandardPage().getAbsoluteLocation();
                redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                        HTTPParameterConstants.ARTICLE_ID, articleId);
                redirectURL = HTTPUtil
                        .getAddedParameterURL(
                                redirectURL,
                                com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                this.getPrimaryKey().toString());
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
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

// end AbstractArticleGroupSubscriptionEntryBlock
