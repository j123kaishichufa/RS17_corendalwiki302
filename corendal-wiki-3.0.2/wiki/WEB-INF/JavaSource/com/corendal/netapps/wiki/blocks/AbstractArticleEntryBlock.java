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
import java.util.Arrays;
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.CaseUtil;
import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.constants.AccessTypeConstants;
import com.corendal.netapps.framework.core.constants.CanvasTypeConstants;
import com.corendal.netapps.framework.core.constants.LayoutTypeConstants;
import com.corendal.netapps.framework.core.dictionaries.RolesDictionary;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormIconFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormMessageFactory;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.AccountHistoryManager;
import com.corendal.netapps.framework.core.interfaces.ConfigManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.LocaleManager;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardAccountFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEmail;
import com.corendal.netapps.framework.core.interfaces.StandardEmailFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormButton;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormField;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormLabel;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.interfaces.StandardFormIcon;
import com.corendal.netapps.framework.core.interfaces.StandardFormIconFactory;
import com.corendal.netapps.framework.core.interfaces.StandardFormLabel;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessage;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLabel;
import com.corendal.netapps.framework.core.interfaces.StandardLocale;
import com.corendal.netapps.framework.core.interfaces.StandardLookup;
import com.corendal.netapps.framework.core.interfaces.StandardLookupFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultAccountHistoryManager;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.managers.DefaultLocaleManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardLookupFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.BlockUtil;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardEmailFactory;
import com.corendal.netapps.framework.helpdesk.dictionaries.LookupsDictionary;
import com.corendal.netapps.framework.helpdesk.interfaces.GroupHistoryManager;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupFactory;
import com.corendal.netapps.framework.helpdesk.managers.DefaultGroupHistoryManager;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentClassificationTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentRuleTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationType;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRuleType;
import com.corendal.netapps.wiki.interfaces.StandardContentRuleTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentType;
import com.corendal.netapps.wiki.interfaces.StandardContentTypeFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentRuleTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentTypeFactory;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;

/**
 * AbstractArticleEntryBlock is the parent block common to all article viewing
 * blocks.
 * 
 * @version $Id: AbstractArticleEntryBlock.java,v 1.2 2005/09/14 00:03:47
 *          tdanard Exp $
 */
public abstract class AbstractArticleEntryBlock extends
        AbstractCommonContentEntryBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractArticleEntryBlock";

    /** Name of the hidden parameter detecting the button being pressed */
    private static final String BUTTON_DETECTOR_NAME = "buttondetector";

    /** Value of the hidden parameter when no button was pressed. */
    public static final String BUTTON_DETECTOR_NONE_VALUE = "NONE";

    /** Value of the hidden parameter when custom was pressed. */
    public static final String BUTTON_DETECTOR_CUSTOM_VALUE = "CUSTOM";

    /** Value of the hidden parameter when alphabetical was pressed. */
    public static final String BUTTON_DETECTOR_ALPHABETICAL_VALUE = "ALPHABETICAL";

    /** Body entered by the user */
    private String body;

    /** Version notes entered by the user */
    private String versionNotes;

    /** Author entered by the user */
    private String author;

    /** Editor entered by the user */
    private String editor;

    /** Associate editor entered by the user */
    private String associateEditors;

    /** Rule type entered by the user */
    private String ruleType;

    /** Classification type entered by the user */
    private String classificationType;

    /** Approve request entered by the user */
    private String approveRequest;

    /** Friendly address entered by the user */
    private String friendlyAddress;

    /** Article viewed through this form */
    private StandardArticle article;

    /** List of content orders entered through this form */
    private String[] contentOrders;

    /** Requested parent article selected through this form */
    private String requestedParentArticleId;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead
     */
    protected AbstractArticleEntryBlock() {
        this.article = null;
    }

    /**
     * Returns a clone. Overrides AbstractCommonContentEntryBlock.clone.
     */
    @Override
    public Object clone() {
        AbstractArticleEntryBlock result = (AbstractArticleEntryBlock) super
                .clone();

        if (this.article != null) {
            result.article = (StandardArticle) this.article.clone();
        }

        return result;
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
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardLookupFactory slkf = (StandardLookupFactory) pfs
                .getStandardBeanFactory(DefaultStandardLookupFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);
        StandardContentClassificationTypeFactory srctf = (StandardContentClassificationTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentClassificationTypeFactory.class);
        StandardContentRuleTypeFactory srrtf = (StandardContentRuleTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRuleTypeFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        LocaleManager lm = (LocaleManager) pms
                .getManager(DefaultLocaleManager.class);
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        ContentHistoryManager hm = (ContentHistoryManager) pms
                .getManager(DefaultContentHistoryManager.class);

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
         * get the current locale
         */
        StandardLocale currentLocale = lm.getPrimaryStandardLocale();

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * find the parent article being modified
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(articleId);

        /*
         * set the layout type to multiple to provide a tabbing where the submit
         * button is the last object to reach
         */
        this.setLayoutType(LayoutTypeConstants.BORDER);

        /*
         * set the max sizes of the form
         */
        if ((this.getAccessType().equals(AccessTypeConstants.VIEW))
                || (this.getAccessType().equals(AccessTypeConstants.EDIT))) {
            this.setMaxSizes(2, 11, CanvasTypeConstants.CENTER);
        } else {
            this.setMaxSizes(2, 10, CanvasTypeConstants.CENTER);
        }

        this.setMaxSizes(3, 1, CanvasTypeConstants.SOUTH);

        /*
         * get the primary keys of the required instances
         */
        PrimaryKey entityPk = entity.getPrimaryKey();
        PrimaryKey nameFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
        PrimaryKey descriptionFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DESCRIPTION);
        PrimaryKey bodyFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_BODY);
        PrimaryKey versionNotesFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_VERSION_NOTES);
        PrimaryKey parentArticleFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PARENT_ARTICLE);
        PrimaryKey authorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
        PrimaryKey editorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_EDITOR);
        PrimaryKey associateEditorsFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ASSOCIATE_EDITORS);
        PrimaryKey classificationTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CLASSIFICATION);
        PrimaryKey ruleTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_RULE);
        PrimaryKey friendlyAddressFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FRIENDLY_ADDRESS);
        PrimaryKey approveRequestFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_APPROVE_REQUEST);

        PrimaryKey nameLabelPk = seflf.getPrimaryKey(entityPk, nameFieldPk);
        PrimaryKey descriptionLabelPk = seflf.getPrimaryKey(entityPk,
                descriptionFieldPk);
        PrimaryKey bodyLabelPk = seflf.getPrimaryKey(entityPk, bodyFieldPk);
        PrimaryKey versionNotesLabelPk = seflf.getPrimaryKey(entityPk,
                versionNotesFieldPk);
        PrimaryKey parentArticleLabelPk = seflf.getPrimaryKey(entityPk,
                parentArticleFieldPk);
        PrimaryKey authorLabelPk = seflf.getPrimaryKey(entityPk, authorFieldPk);
        PrimaryKey editorLabelPk = seflf.getPrimaryKey(entityPk, editorFieldPk);
        PrimaryKey associateEditorsLabelPk = seflf.getPrimaryKey(entityPk,
                associateEditorsFieldPk);
        PrimaryKey classificationTypeLabelPk = seflf.getPrimaryKey(entityPk,
                classificationTypeFieldPk);
        PrimaryKey ruleTypeLabelPk = seflf.getPrimaryKey(entityPk,
                ruleTypeFieldPk);
        PrimaryKey friendlyAddressLabelPk = seflf.getPrimaryKey(entityPk,
                friendlyAddressFieldPk);
        PrimaryKey approveRequestLabelPk = seflf.getPrimaryKey(entityPk,
                approveRequestFieldPk);

        /*
         * instantiate all lookups
         */
        PrimaryKey accountLookupPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(LookupsDictionary.ACCOUNT);
        StandardLookup accountLookup = (StandardLookup) slkf
                .findByPrimaryKey(accountLookupPk);
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

        groupLookup.setAdditionalHTML(lw.getBreakableTargetLinkHTML("_blank",
                addNewGroupLocation, null, newGroupMessage
                        .getCurrentMessageHTML(), null));

        /*
         * instantiate all entry fields
         */
        StandardEntryFormField nameField = (StandardEntryFormField) sefff
                .findByPrimaryKey(nameFieldPk);
        StandardEntryFormField descriptionField = (StandardEntryFormField) sefff
                .findByPrimaryKey(descriptionFieldPk);
        descriptionField.setTextAreaType(60, 3);

        StandardEntryFormField bodyField = (StandardEntryFormField) sefff
                .findByPrimaryKey(bodyFieldPk);
        bodyField.setTextAreaType(80, 18);

        StandardEntryFormField versionNotesField = (StandardEntryFormField) sefff
                .findByPrimaryKey(versionNotesFieldPk);
        versionNotesField.setTextAreaType(60, 2);

        StandardEntryFormField parentArticleField = (StandardEntryFormField) sefff
                .findByPrimaryKey(parentArticleFieldPk);

        StandardEntryFormField authorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(authorFieldPk);
        authorField.setStandardLookup(accountLookup);
        authorField.setIsWildcardSubstituted(true);

        StandardEntryFormField editorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(editorFieldPk);
        editorField.setStandardLookup(accountLookup);
        editorField.setIsWildcardSubstituted(true);

        StandardEntryFormField associateEditorsField = (StandardEntryFormField) sefff
                .findByPrimaryKey(associateEditorsFieldPk);
        associateEditorsField.setStandardLookup(groupLookup);
        associateEditorsField.setIsWildcardSubstituted(true);

        StandardEntryFormField classificationTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(classificationTypeFieldPk);
        StandardEntryFormField ruleTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(ruleTypeFieldPk);
        StandardEntryFormField friendlyAddressField = (StandardEntryFormField) sefff
                .findByPrimaryKey(friendlyAddressFieldPk);
        StandardEntryFormField approveRequestField = (StandardEntryFormField) sefff
                .findByPrimaryKey(approveRequestFieldPk);
        approveRequestField.setCheckBoxType();

        /*
         * instantiate all entry labels
         */
        StandardEntryFormLabel nameLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(nameLabelPk);
        StandardEntryFormLabel descriptionLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(descriptionLabelPk);
        StandardEntryFormLabel bodyLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(bodyLabelPk);
        StandardEntryFormLabel versionNotesLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(versionNotesLabelPk);
        StandardEntryFormLabel authorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(authorLabelPk);
        StandardEntryFormLabel editorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(editorLabelPk);
        StandardEntryFormLabel associateEditorsLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(associateEditorsLabelPk);
        StandardEntryFormLabel classificationTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(classificationTypeLabelPk);
        StandardEntryFormLabel ruleTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(ruleTypeLabelPk);
        StandardEntryFormLabel friendlyAddressLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(friendlyAddressLabelPk);
        StandardEntryFormLabel parentArticleLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(parentArticleLabelPk);
        StandardEntryFormLabel approveRequestLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(approveRequestLabelPk);

        /*
         * associate them
         */
        nameLabel.associateStandardFormField(nameField);
        descriptionLabel.associateStandardFormField(descriptionField);
        bodyLabel.associateStandardFormField(bodyField);
        versionNotesLabel.associateStandardFormField(versionNotesField);
        authorLabel.associateStandardFormField(authorField);
        editorLabel.associateStandardFormField(editorField);
        associateEditorsLabel.associateStandardFormField(associateEditorsField);
        friendlyAddressLabel.associateStandardFormField(friendlyAddressField);
        approveRequestLabel.associateStandardFormField(approveRequestField);
        classificationTypeLabel
                .associateStandardFormField(classificationTypeField);
        ruleTypeLabel.associateStandardFormField(ruleTypeField);
        parentArticleLabel.associateStandardFormField(parentArticleField);

        /*
         * change the default alignments
         */
        approveRequestField.setAlign(currentLocale.getRightAlign());

        /*
         * set the mandatory objects
         */
        nameLabel.setAsMandatory();
        nameField.setAsMandatory();
        descriptionLabel.setAsMandatory();
        descriptionField.setAsMandatory();
        bodyLabel.setAsMandatory();
        bodyField.setAsMandatory();
        parentArticleLabel.setAsMandatory();
        parentArticleField.setAsMandatory();
        friendlyAddressLabel.setAsMandatory();
        friendlyAddressField.setAsMandatory();
        authorLabel.setAsMandatory();
        authorField.setAsMandatory();
        editorLabel.setAsMandatory();
        editorField.setAsMandatory();
        classificationTypeLabel.setAsMandatory();
        classificationTypeField.setAsMandatory();
        ruleTypeLabel.setAsMandatory();
        ruleTypeField.setAsMandatory();

        /*
         * disable the focus
         */
        bodyField.setIsFocusAllowed(false);
        versionNotesField.setIsFocusAllowed(false);
        parentArticleField.setIsFocusAllowed(false);
        friendlyAddressField.setIsFocusAllowed(false);
        authorField.setIsFocusAllowed(false);
        editorField.setIsFocusAllowed(false);
        associateEditorsField.setIsFocusAllowed(false);
        classificationTypeField.setIsFocusAllowed(false);
        ruleTypeField.setIsFocusAllowed(false);

        /*
         * get all rule types
         */
        PrimaryKey sameAsParentRulePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRuleTypesDictionary.SAME_AS_PARENT);
        PrimaryKey noRequestRequiredPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRuleTypesDictionary.NO_REQUEST_REQUIRED);

        /*
         * get the home article
         */
        PrimaryKey homePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ArticlesDictionary.HOME);

        /*
         * get all classification types
         */
        PrimaryKey sameAsParentClassificationPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);

        /*
         * fill with the default values. This default values will only be used
         * if the form is printed for the first time
         */
        PrimaryKey parentArticlePk = null;

        if ((this.getAccessType().equals(AccessTypeConstants.VIEW))
                || (this.getAccessType().equals(AccessTypeConstants.EDIT))) {
            nameField.setFirstDefaultValue(this.article.getNameText());
            descriptionField.setFirstDefaultValue(this.article
                    .getDescriptionText());

            StandardAccount authorStandardAccount = this.article
                    .getAuthorStandardAccount();
            authorField.setFirstDefaultValue(authorStandardAccount
                    .getFullNameAndLoginText());
            authorField.setFirstDefaultValueHTML(authorStandardAccount
                    .getFullNameAndLoginHTMLWithLink());
            friendlyAddressField.setFirstDefaultValue(this.article
                    .getFriendlyAddress());

            StandardAccount editorStandardAccount = this.article
                    .getEditorStandardAccount();
            editorField.setFirstDefaultValue(editorStandardAccount
                    .getFullNameAndLoginText());
            editorField.setFirstDefaultValueHTML(editorStandardAccount
                    .getFullNameAndLoginHTMLWithLink());

            bodyField.setFirstDefaultValue(this.article.getRawBodyHTML());
            versionNotesField.setFirstDefaultValue(this.article.getComment());

            StandardGroup associateEditorsStandardGroup = this.article
                    .getAssociateEditorsStandardGroup();

            if ((associateEditorsStandardGroup != null)
                    && (associateEditorsStandardGroup.getIsFound())) {
                String associateEditorsHTML = lw
                        .getBreakableLinkHTML(associateEditorsStandardGroup
                                .getDefaultLocation(), null,
                                associateEditorsStandardGroup.getNameHTML(),
                                null);
                associateEditorsField
                        .setFirstDefaultValueHTML(associateEditorsHTML);
            }

            if ((associateEditorsStandardGroup != null)
                    && (associateEditorsStandardGroup.getIsFound())) {
                associateEditorsField
                        .setFirstDefaultValue(associateEditorsStandardGroup
                                .getNameText());
            } else {
                associateEditorsField.setFirstDefaultValue("");
            }

            /*
             * populate the parent article field
             */
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(articlePk);

            if (!(articlePk.equals(homePk))) {
                parentArticlePk = content.getMainParentPrimaryKey();

                StandardArticle article = (StandardArticle) sdf
                        .findByPrimaryKey(parentArticlePk);
                parentArticleField.setFirstDefaultValue(article.getPrimaryKey()
                        .toString());
                parentArticleField.setFirstDefaultValueHTML(article
                        .getPathHTMLWithLink());
            }

            /*
             * populate the classification field
             */
            StandardContentClassificationType classificationType = (StandardContentClassificationType) srctf
                    .findByPrimaryKey(this.article
                            .getClassificationTypePrimaryKey());
            classificationTypeField.setFirstDefaultValue(classificationType
                    .getNameText());
            classificationTypeField.setFirstDefaultValueHTML(classificationType
                    .getNameHTML());

            /*
             * populate the rule field
             */
            StandardContentRuleType ruleType = (StandardContentRuleType) srrtf
                    .findByPrimaryKey(this.article.getRuleTypePrimaryKey());
            ruleTypeField.setFirstDefaultValue(ruleType.getNameText());
            ruleTypeField.setFirstDefaultValueHTML(ruleType.getNameHTML());

            /*
             * get the rule type of the governing article
             */
            Searched searched = this.article.getRuleSearched();
            PrimaryKey ruleTypePk = searched.getRuleTypePrimaryKey();

            /*
             * verify the governing rule type
             */
            if ((!(ruleTypePk.equals(noRequestRequiredPk)))
                    || (resm.getIsRecursiveProxyOrAssociateEditor(sa
                            .getPrimaryKey(), articlePk))) {
                /*
                 * change the type of fields when editable
                 */
                if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                        articlePk)) {
                    /*
                     * change the parent article field
                     */
                    if (parentArticlePk != null) {
                        List<PrimaryKey> articles = hm
                                .getToppedHistoryWithArticlesOnly(parentArticlePk);
                        if (!(articles.contains(homePk))) {
                            articles.add(homePk);
                        }
                        ArrayList<String> parentArticleValues = new ArrayList<String>();
                        ArrayList<String> parentArticleNames = new ArrayList<String>();

                        for (int i = 0; i < articles.size(); i++) {
                            PrimaryKey currentArticlePk = (PrimaryKey) articles
                                    .get(i);

                            if (!(currentArticlePk.equals(articlePk))) {
                                StandardArticle currentArticle = (StandardArticle) sdf
                                        .findByPrimaryKey(currentArticlePk);

                                if (!(this.article
                                        .getIsRecursiveDirectParentOf(currentArticle))) {
                                    parentArticleValues.add(currentArticlePk
                                            .toString());
                                    parentArticleNames.add(currentArticle
                                            .getPathText());
                                }
                            }
                        }

                        parentArticleField.setSelectType(parentArticleValues,
                                parentArticleNames);
                        parentArticleField.setFirstDefaultValue(articlePk
                                .toString());
                    }

                    /*
                     * change the classification field
                     */
                    List<StandardReadBean> classificationTypes = srctf
                            .findByEnabledFlag("Y");
                    ArrayList<String> classificationTypeValues = new ArrayList<String>();
                    ArrayList<String> classificationTypeNames = new ArrayList<String>();

                    for (int i = 0; i < classificationTypes.size(); i++) {
                        StandardContentClassificationType currentClassificationType = (StandardContentClassificationType) classificationTypes
                                .get(i);

                        if ((parentArticlePk != null)
                                || (!(currentClassificationType.getPrimaryKey()
                                        .equals(sameAsParentClassificationPk)))) {
                            classificationTypeValues
                                    .add(currentClassificationType
                                            .getPrimaryKey().toString());
                            classificationTypeNames
                                    .add(currentClassificationType
                                            .getNameText());
                        }
                    }

                    classificationTypeField.setSelectType(
                            classificationTypeValues, classificationTypeNames);
                    classificationTypeField
                            .setFirstDefaultValue(classificationType
                                    .getPrimaryKey().toString());

                    /*
                     * change the rule field
                     */
                    List<StandardReadBean> ruleTypes = srrtf
                            .findByEnabledFlag("Y");
                    ArrayList<String> ruleTypeValues = new ArrayList<String>();
                    ArrayList<String> ruleTypeNames = new ArrayList<String>();

                    for (int i = 0; i < ruleTypes.size(); i++) {
                        StandardContentRuleType currentRuleType = (StandardContentRuleType) ruleTypes
                                .get(i);

                        if ((parentArticlePk != null)
                                || (!(currentRuleType.getPrimaryKey()
                                        .equals(sameAsParentRulePk)))) {
                            ruleTypeValues.add(currentRuleType.getPrimaryKey()
                                    .toString());
                            ruleTypeNames.add(currentRuleType.getNameText());
                        }
                    }

                    ruleTypeField.setSelectType(ruleTypeValues, ruleTypeNames);
                    ruleTypeField.setFirstDefaultValue(ruleType.getPrimaryKey()
                            .toString());
                } else {
                    authorField.setPrintOnlyType();
                    editorField.setPrintOnlyType();
                    associateEditorsField.setPrintOnlyType();
                    parentArticleField.setPrintOnlyType();
                    classificationTypeField.setPrintOnlyType();
                    ruleTypeField.setPrintOnlyType();

                    /*
                     * add the classification type
                     */
                    PrimaryKey contentClassificationTypePk = this.article
                            .getClassificationTypePrimaryKey();
                    StandardContentClassificationType contentClassificationType = (StandardContentClassificationType) srctf
                            .findByPrimaryKey(contentClassificationTypePk);
                    String contentClassificationTypeNameText = contentClassificationType
                            .getNameText();
                    String contentClassificationTypeNameHTML = contentClassificationType
                            .getNameHTML();

                    Searched contentClassificationSearched = this.article
                            .getClassificationSearched();
                    PrimaryKey searchedContentClassificationTypePk = contentClassificationSearched
                            .getClassificationTypePrimaryKey();
                    StandardContentClassificationType searchedContentClassificationType = (StandardContentClassificationType) srctf
                            .findByPrimaryKey(searchedContentClassificationTypePk);
                    String searchedContentClassificationTypeNameText = searchedContentClassificationType
                            .getNameText();
                    String searchedContentClassificationTypeNameHTML = searchedContentClassificationType
                            .getNameHTML();

                    if (!(searchedContentClassificationTypeNameHTML
                            .equals(contentClassificationTypeNameHTML))) {
                        contentClassificationTypeNameHTML = ConcatUtil
                                .getConcatWithParentheses(
                                        contentClassificationTypeNameText,
                                        searchedContentClassificationTypeNameText,
                                        contentClassificationTypeNameHTML,
                                        searchedContentClassificationTypeNameHTML);
                    }

                    classificationTypeField
                            .setFirstDefaultValue(contentClassificationType
                                    .getPrimaryKey().toString());
                    classificationTypeField
                            .setFirstDefaultValueHTML(contentClassificationTypeNameHTML);

                    /*
                     * add the rule type
                     */
                    PrimaryKey contentRuleTypePk = this.article
                            .getRuleTypePrimaryKey();
                    StandardContentRuleType contentRuleType = (StandardContentRuleType) srrtf
                            .findByPrimaryKey(contentRuleTypePk);
                    String contentRuleTypeNameText = contentRuleType
                            .getNameText();
                    String contentRuleTypeNameHTML = contentRuleType
                            .getNameHTML();

                    Searched contentRuleSearched = this.article
                            .getRuleSearched();
                    PrimaryKey searchedContentRuleTypePk = contentRuleSearched
                            .getRuleTypePrimaryKey();
                    StandardContentRuleType searchedContentRuleType = (StandardContentRuleType) srrtf
                            .findByPrimaryKey(searchedContentRuleTypePk);
                    String searchedContentRuleTypeNameText = searchedContentRuleType
                            .getNameText();
                    String searchedContentRuleTypeNameHTML = searchedContentRuleType
                            .getNameHTML();

                    if (!(searchedContentRuleTypeNameHTML
                            .equals(contentRuleTypeNameHTML))) {
                        contentRuleTypeNameHTML = ConcatUtil
                                .getConcatWithParentheses(
                                        contentRuleTypeNameText,
                                        searchedContentRuleTypeNameText,
                                        contentRuleTypeNameHTML,
                                        searchedContentRuleTypeNameHTML);
                    }

                    ruleTypeField.setFirstDefaultValue(contentRuleType
                            .getPrimaryKey().toString());
                    ruleTypeField
                            .setFirstDefaultValueHTML(contentRuleTypeNameHTML);
                }
            } else {
                authorField.setPrintOnlyType();
                editorField.setPrintOnlyType();
                associateEditorsField.setPrintOnlyType();
                parentArticleField.setPrintOnlyType();
                classificationTypeField.setPrintOnlyType();
                ruleTypeField.setPrintOnlyType();

                /*
                 * add the classification type
                 */
                PrimaryKey contentClassificationTypePk = this.article
                        .getClassificationTypePrimaryKey();
                StandardContentClassificationType contentClassificationType = (StandardContentClassificationType) srctf
                        .findByPrimaryKey(contentClassificationTypePk);
                String contentClassificationTypeNameText = contentClassificationType
                        .getNameText();
                String contentClassificationTypeNameHTML = contentClassificationType
                        .getNameHTML();

                Searched contentClassificationSearched = this.article
                        .getClassificationSearched();
                PrimaryKey searchedContentClassificationTypePk = contentClassificationSearched
                        .getClassificationTypePrimaryKey();
                StandardContentClassificationType searchedContentClassificationType = (StandardContentClassificationType) srctf
                        .findByPrimaryKey(searchedContentClassificationTypePk);
                String searchedContentClassificationTypeNameText = searchedContentClassificationType
                        .getNameText();
                String searchedContentClassificationTypeNameHTML = searchedContentClassificationType
                        .getNameHTML();

                if (!(searchedContentClassificationTypeNameHTML
                        .equals(contentClassificationTypeNameHTML))) {
                    contentClassificationTypeNameHTML = ConcatUtil
                            .getConcatWithParentheses(
                                    contentClassificationTypeNameText,
                                    searchedContentClassificationTypeNameText,
                                    contentClassificationTypeNameHTML,
                                    searchedContentClassificationTypeNameHTML);
                }

                classificationTypeField
                        .setFirstDefaultValue(contentClassificationType
                                .getPrimaryKey().toString());
                classificationTypeField
                        .setFirstDefaultValueHTML(contentClassificationTypeNameHTML);

                /*
                 * add the rule type
                 */
                PrimaryKey contentRuleTypePk = this.article
                        .getRuleTypePrimaryKey();
                StandardContentRuleType contentRuleType = (StandardContentRuleType) srrtf
                        .findByPrimaryKey(contentRuleTypePk);
                String contentRuleTypeNameText = contentRuleType.getNameText();
                String contentRuleTypeNameHTML = contentRuleType.getNameHTML();

                Searched contentRuleSearched = this.article.getRuleSearched();
                PrimaryKey searchedContentRuleTypePk = contentRuleSearched
                        .getRuleTypePrimaryKey();
                StandardContentRuleType searchedContentRuleType = (StandardContentRuleType) srrtf
                        .findByPrimaryKey(searchedContentRuleTypePk);
                String searchedContentRuleTypeNameText = searchedContentRuleType
                        .getNameText();
                String searchedContentRuleTypeNameHTML = searchedContentRuleType
                        .getNameHTML();

                if (!(searchedContentRuleTypeNameHTML
                        .equals(contentRuleTypeNameHTML))) {
                    contentRuleTypeNameHTML = ConcatUtil
                            .getConcatWithParentheses(contentRuleTypeNameText,
                                    searchedContentRuleTypeNameText,
                                    contentRuleTypeNameHTML,
                                    searchedContentRuleTypeNameHTML);
                }

                ruleTypeField.setFirstDefaultValue(contentRuleType
                        .getPrimaryKey().toString());
                ruleTypeField.setFirstDefaultValueHTML(contentRuleTypeNameHTML);
            }
        } else { // this is the "new" access type
            /*
             * get the parent article
             */

            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(articlePk);

            /*
             * get the rule type of the governing article
             */
            Searched searched = parentArticle.getRuleSearched();
            PrimaryKey ruleTypePk = searched.getRuleTypePrimaryKey();

            /*
             * set the author field
             */
            authorField.setFirstDefaultValue(this.author);

            /*
             * verify the governing rule type
             */
            if ((!(ruleTypePk.equals(noRequestRequiredPk)))
                    || (resm.getIsRecursiveProxyOrAssociateEditor(sa
                            .getPrimaryKey(), articlePk))) {
                editorField.setFirstDefaultValue(this.editor);

                List<StandardReadBean> classificationTypes = srctf
                        .findByEnabledFlag("Y");
                ArrayList<String> classificationTypeValues = new ArrayList<String>();
                ArrayList<String> classificationTypeNames = new ArrayList<String>();

                for (int i = 0; i < classificationTypes.size(); i++) {
                    StandardContentClassificationType currentClassificationType = (StandardContentClassificationType) classificationTypes
                            .get(i);
                    classificationTypeValues.add(currentClassificationType
                            .getPrimaryKey().toString());
                    classificationTypeNames.add(currentClassificationType
                            .getNameText());
                }

                classificationTypeField.setSelectType(classificationTypeValues,
                        classificationTypeNames);
                classificationTypeField
                        .setFirstDefaultValue(this.classificationType);

                List<StandardReadBean> ruleTypes = srrtf.findByEnabledFlag("Y");
                ArrayList<String> ruleTypeValues = new ArrayList<String>();
                ArrayList<String> ruleTypeNames = new ArrayList<String>();

                for (int i = 0; i < ruleTypes.size(); i++) {
                    StandardContentRuleType currentRuleType = (StandardContentRuleType) ruleTypes
                            .get(i);
                    ruleTypeValues.add(currentRuleType.getPrimaryKey()
                            .toString());
                    ruleTypeNames.add(currentRuleType.getNameText());
                }

                ruleTypeField.setSelectType(ruleTypeValues, ruleTypeNames);
                ruleTypeField.setFirstDefaultValue(this.ruleType);
            } else {
                StandardAccount editor = parentArticle
                        .getEditorStandardAccount();
                editorField.setFirstDefaultValue(editor
                        .getFullNameAndLoginText());
                editorField.setFirstDefaultValueHTML(editor
                        .getFullNameAndLoginHTMLWithLink());

                editorField.setPrintOnlyType();
                associateEditorsField.setPrintOnlyType();
                classificationTypeField.setPrintOnlyType();
                ruleTypeField.setPrintOnlyType();

                /*
                 * add the classification type
                 */
                PrimaryKey contentClassificationTypePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);
                StandardContentClassificationType contentClassificationType = (StandardContentClassificationType) srctf
                        .findByPrimaryKey(contentClassificationTypePk);
                String contentClassificationTypeNameText = contentClassificationType
                        .getNameText();
                String contentClassificationTypeNameHTML = contentClassificationType
                        .getNameHTML();

                Searched contentClassificationSearched = parentArticle
                        .getClassificationSearched();
                PrimaryKey searchedContentClassificationTypePk = contentClassificationSearched
                        .getClassificationTypePrimaryKey();
                StandardContentClassificationType searchedContentClassificationType = (StandardContentClassificationType) srctf
                        .findByPrimaryKey(searchedContentClassificationTypePk);
                String searchedContentClassificationTypeNameText = searchedContentClassificationType
                        .getNameText();
                String searchedContentClassificationTypeNameHTML = searchedContentClassificationType
                        .getNameHTML();

                if (!(searchedContentClassificationTypeNameHTML
                        .equals(contentClassificationTypeNameHTML))) {
                    contentClassificationTypeNameHTML = ConcatUtil
                            .getConcatWithParentheses(
                                    contentClassificationTypeNameText,
                                    searchedContentClassificationTypeNameText,
                                    contentClassificationTypeNameHTML,
                                    searchedContentClassificationTypeNameHTML);
                }

                classificationTypeField
                        .setFirstDefaultValue(contentClassificationType
                                .getPrimaryKey().toString());
                classificationTypeField
                        .setFirstDefaultValueHTML(contentClassificationTypeNameHTML);

                /*
                 * add the rule type
                 */
                PrimaryKey contentRuleTypePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentRuleTypesDictionary.SAME_AS_PARENT);
                StandardContentRuleType contentRuleType = (StandardContentRuleType) srrtf
                        .findByPrimaryKey(contentRuleTypePk);
                String contentRuleTypeNameText = contentRuleType.getNameText();
                String contentRuleTypeNameHTML = contentRuleType.getNameHTML();

                Searched contentRuleSearched = parentArticle.getRuleSearched();
                PrimaryKey searchedContentRuleTypePk = contentRuleSearched
                        .getRuleTypePrimaryKey();
                StandardContentRuleType searchedContentRuleType = (StandardContentRuleType) srrtf
                        .findByPrimaryKey(searchedContentRuleTypePk);
                String searchedContentRuleTypeNameText = searchedContentRuleType
                        .getNameText();
                String searchedContentRuleTypeNameHTML = searchedContentRuleType
                        .getNameHTML();

                if (!(searchedContentRuleTypeNameHTML
                        .equals(contentRuleTypeNameHTML))) {
                    contentRuleTypeNameHTML = ConcatUtil
                            .getConcatWithParentheses(contentRuleTypeNameText,
                                    searchedContentRuleTypeNameText,
                                    contentRuleTypeNameHTML,
                                    searchedContentRuleTypeNameHTML);
                }

                ruleTypeField.setFirstDefaultValue(contentRuleType
                        .getPrimaryKey().toString());
                ruleTypeField.setFirstDefaultValueHTML(contentRuleTypeNameHTML);
            }

            nameField.setFirstDefaultValue(this.getContentName());
            friendlyAddressField.setFirstDefaultValue(this.friendlyAddress);
        }

        approveRequestField.setFirstDefaultValue(this.approveRequest);

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
        this.addStandardFormObject(bodyField, 1, ++lineNumber);
        this.addStandardFormObject(versionNotesLabel, 1, ++lineNumber);
        this.addStandardFormObject(versionNotesField, 2, lineNumber);

        if (parentArticlePk != null) {
            if ((this.getAccessType().equals(AccessTypeConstants.VIEW))
                    || (this.getAccessType().equals(AccessTypeConstants.EDIT))) {
                this.addStandardFormObject(parentArticleLabel, 1, ++lineNumber);
                this.addStandardFormObject(parentArticleField, 2, lineNumber);
            }
        }

        this.addStandardFormObject(authorLabel, 1, ++lineNumber);
        this.addStandardFormObject(authorField, 2, lineNumber);
        this.addStandardFormObject(editorLabel, 1, ++lineNumber);
        this.addStandardFormObject(editorField, 2, lineNumber);
        this.addStandardFormObject(associateEditorsLabel, 1, ++lineNumber);
        this.addStandardFormObject(associateEditorsField, 2, lineNumber);

        if ((this.getAccessType().equals(AccessTypeConstants.VIEW))
                || (this.getAccessType().equals(AccessTypeConstants.EDIT))) {
            if (parentArticlePk != null) {
                this.addStandardFormObject(friendlyAddressLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(friendlyAddressField, 2, lineNumber);
            }
        } else if (this.getAccessType().equals(AccessTypeConstants.NEW)) {
            this.addStandardFormObject(friendlyAddressLabel, 1, ++lineNumber);
            this.addStandardFormObject(friendlyAddressField, 2, lineNumber);
        }

        this.addStandardFormObject(classificationTypeLabel, 1, ++lineNumber);
        this.addStandardFormObject(classificationTypeField, 2, lineNumber);
        this.addStandardFormObject(ruleTypeLabel, 1, ++lineNumber);
        this.addStandardFormObject(ruleTypeField, 2, lineNumber);

        /*
         * instantiate and add the submit button
         */
        this.setCurrentCanvasType(CanvasTypeConstants.SOUTH);

        if ((this.getAccessType().equals(AccessTypeConstants.VIEW))
                || (this.getAccessType().equals(AccessTypeConstants.EDIT))) {
            PrimaryKey sendUpdateArticleRequestPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.SEND_UPDATE_ARTICLE_REQUEST);
            StandardEntryFormButton sendUpdateArticleRequest = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(sendUpdateArticleRequestPk);
            sendUpdateArticleRequest.setOnSubmitScript("updateRTE('"
                    + bodyField.getParameterName() + "');return true;");
            this.addStandardFormObject(sendUpdateArticleRequest, 1, 1);
        } else {
            PrimaryKey sendAddRequestPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.SEND_ADD_REQUEST);
            StandardEntryFormButton sendAddRequest = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(sendAddRequestPk);
            sendAddRequest.setOnSubmitScript("updateRTE('"
                    + bodyField.getParameterName() + "');return true;");
            this.addStandardFormObject(sendAddRequest, 1, 1);
        }

        if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(), articlePk)) {
            this.addStandardFormObject(approveRequestField, 2, 1);
            this.addStandardFormObject(approveRequestLabel, 3, 1);
        }

        /*
         * extract the data entries from the form for validation
         */
        this.setContentName(nameField.getRequestValue());
        this.setContentDescription(descriptionField.getRequestValue());
        this.body = bodyField.getRequestValue();
        this.versionNotes = versionNotesField.getRequestValue();
        this.editor = editorField.getRequestValue();
        this.author = authorField.getRequestValue();
        this.associateEditors = associateEditorsField.getRequestValue();
        this.classificationType = classificationTypeField.getRequestValue();
        this.ruleType = ruleTypeField.getRequestValue();
        this.friendlyAddress = ContentUtil
                .getCleanFriendlyAddress(friendlyAddressField.getRequestValue());
        this.requestedParentArticleId = parentArticleField.getRequestValue();
        this.approveRequest = approveRequestField.getRequestValue();
    }

    /**
     * Builds the layout of the form. This procedure is used when viewing a
     * article
     * 
     * 
     * 
     */
    private void buildAbout() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntryFormLabelFactory seflf = (StandardEntryFormLabelFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormLabelFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);

        /*
         * get the entity of the form
         */
        StandardEntity entity = this.getStandardEntity();

        /*
         * set the max sizes of the form
         */
        this.setMaxSizes(2, 2, CanvasTypeConstants.CENTER);

        /*
         * get the primary keys of the required instances
         */
        PrimaryKey entityPk = entity.getPrimaryKey();
        PrimaryKey contentEditorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_EDITOR);
        PrimaryKey contentEditorLabelPk = seflf.getPrimaryKey(entityPk,
                contentEditorFieldPk);
        PrimaryKey contentAuthorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
        PrimaryKey contentAuthorLabelPk = seflf.getPrimaryKey(entityPk,
                contentAuthorFieldPk);

        /*
         * instantiate all entry fields
         */
        StandardEntryFormField contentEditorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentEditorFieldPk);
        StandardEntryFormField contentAuthorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentAuthorFieldPk);

        /*
         * instantiate all entry labels
         */
        StandardFormLabel contentEditorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentEditorLabelPk);
        StandardFormLabel contentAuthorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentAuthorLabelPk);

        /*
         * associate them
         */
        contentEditorLabel.associateStandardFormField(contentEditorField);
        contentAuthorLabel.associateStandardFormField(contentAuthorField);

        /*
         * set the appropriate fields in print only mode
         */
        contentEditorField.setPrintOnlyType();
        contentAuthorField.setPrintOnlyType();

        /*
         * find the editor and author of the current article
         */
        StandardAccount editor = this.article.getEditorStandardAccount();
        StandardAccount author = this.article.getAuthorStandardAccount();

        /*
         * fill with the default values.
         */
        contentEditorField.setFirstDefaultValue(editor
                .getFullNameAndLoginText());
        contentEditorField.setFirstDefaultValueHTML(editor
                .getFullNameAndLoginHTMLWithLink());
        contentAuthorField.setFirstDefaultValue(author
                .getFullNameAndLoginText());
        contentAuthorField.setFirstDefaultValueHTML(author
                .getFullNameAndLoginHTMLWithLink());

        /*
         * set the current line number
         */
        int lineNumber = 0;

        /*
         * add those objects
         */
        this.addStandardFormObject(contentEditorLabel, 1, ++lineNumber);
        this.addStandardFormObject(contentEditorField, 2, lineNumber);
        this.addStandardFormObject(contentAuthorLabel, 1, ++lineNumber);
        this.addStandardFormObject(contentAuthorField, 2, lineNumber);
    }

    /**
     * Builds the layout of the form. This procedure is used when reordering
     * contents of an article
     * 
     * 
     * an AnyLogicContext object
     */
    protected void buildReorganizeArticle() {
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
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);
        StandardFormIconFactory sfif = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * set the entity
         */
        PrimaryKey contentsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(contentsPk);
        this.setStandardEntity(entity);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * find the parent article being modified
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(articleId);

        /*
         * build the form
         */
        if (articlePk != null) {
            /*
             * get the list of contents in the parent article
             */
            List<StandardContent> contents = srf
                    .findEnabledByParentArticlePrimaryKey(articlePk);

            /*
             * check whether contents could be found
             */
            if (contents.size() > 0) {
                /*
                 * verify the current user can reorganize this article
                 */
                if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                        articlePk)) {
                    /*
                     * create a list of values
                     */
                    int contentsSize = contents.size();
                    ArrayList<String> options = new ArrayList<String>();

                    for (int i = 0; i < contentsSize; i++) {
                        int orderValue = i + 1;
                        options.add(String.valueOf(orderValue));
                    }

                    /*
                     * set the layout type to multiple
                     */
                    this.setLayoutType(LayoutTypeConstants.BORDER);

                    /*
                     * set the max sizes of the form
                     */
                    this.setMaxSizes(3, contents.size(),
                            CanvasTypeConstants.CENTER); // 3
                    // columns,
                    // ?

                    // lines
                    this.setMaxSizes(2, 1, CanvasTypeConstants.SOUTH); // 2

                    // columns,
                    // 1
                    // line

                    /*
                     * get the primary keys of the required instances
                     */
                    PrimaryKey entityPk = entity.getPrimaryKey();
                    PrimaryKey nameFieldPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
                    PrimaryKey nameLabelPk = seflf.getPrimaryKey(entityPk,
                            nameFieldPk);

                    /*
                     * instantiate all entry fields
                     */
                    StandardEntryFormField nameField = (StandardEntryFormField) sefff
                            .findByPrimaryKey(nameFieldPk);
                    nameField.setSelectType(options, options);

                    /*
                     * instantiate all entry labels
                     */
                    StandardEntryFormLabel nameLabel = (StandardEntryFormLabel) seflf
                            .findByPrimaryKey(nameLabelPk);
                    nameLabel.setIsNameBreakable(true);

                    /*
                     * extract the data entries from the form for validation
                     */
                    this.contentOrders = rm.getParameterValues(nameField
                            .getParameterName());

                    int length = 0;

                    if (this.contentOrders != null) {
                        length = this.contentOrders.length;
                    }

                    /*
                     * add those objects
                     */
                    this.setCurrentCanvasType(CanvasTypeConstants.CENTER);

                    for (int i = 0; i < contentsSize; i++) {
                        /*
                         * get the content
                         */
                        StandardContent currentContent = (StandardContent) contents
                                .get(i);
                        PrimaryKey currentContentPk = currentContent
                                .getPrimaryKey();

                        /*
                         * get the preview icon
                         */
                        PrimaryKey previewIconPk = null;

                        if (currentContent.getIsDirectChildOf(articlePk)) {
                            previewIconPk = currentContent
                                    .getDirectPreviewIconPrimaryKey();
                        } else {
                            previewIconPk = currentContent
                                    .getIndirectPreviewIconPrimaryKey();
                        }

                        StandardFormIcon previewFormIcon = (StandardFormIcon) sfif
                                .findByPrimaryKey(previewIconPk);

                        /*
                         * add the hidden parameter
                         */
                        this.addConstantHiddenParameter(
                                HTTPParameterConstants.CONTENT_ID,
                                currentContentPk.toString());

                        /*
                         * clone the field and modify the field clone
                         */
                        StandardEntryFormField nameFieldClone = (StandardEntryFormField) nameField
                                .clone();
                        int orderField = i + 1;
                        nameFieldClone.setFirstDefaultValue(String
                                .valueOf(orderField));
                        nameFieldClone.setSecondDefaultValue(String
                                .valueOf(orderField));

                        if (length > 0) {
                            if (length == contentsSize) {
                                nameFieldClone
                                        .setSecondDefaultValue(this.contentOrders[i]);
                            }
                        }

                        /*
                         * modify the focus name of the field
                         */
                        if (contentsSize > 1) {
                            nameFieldClone.setFocusParameterName(nameField
                                    .getParameterName()
                                    + "[" + i + "]");
                        }

                        /*
                         * clone the label and modify the name of the label
                         */
                        StandardEntryFormLabel nameLabelClone = (StandardEntryFormLabel) nameLabel
                                .clone();
                        StandardLabel sl3 = nameLabelClone.getStandardLabel();

                        sl3.setCurrentNameText(currentContent.getNameText());
                        sl3.setCurrentNameEncoded(currentContent
                                .getNameEncoded());
                        sl3.setCurrentNameHTML(currentContent.getNameHTML());

                        /*
                         * add these three objects
                         */
                        this.addStandardFormObject(nameFieldClone, 1, i + 1);
                        this.addStandardFormObject(previewFormIcon, 2, i + 1);
                        this.addStandardFormObject(nameLabelClone, 3, i + 1);
                    }

                    /*
                     * instantiate and add the submit button
                     */
                    this.setCurrentCanvasType(CanvasTypeConstants.SOUTH);

                    PrimaryKey nextButtonPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(ButtonsDictionary.REORGANIZE_ARTICLE);
                    StandardEntryFormButton nextButton = (StandardEntryFormButton) sefbf
                            .findByPrimaryKey(nextButtonPk);
                    nextButton.setDetectorNameAndValue(BUTTON_DETECTOR_NAME,
                            BUTTON_DETECTOR_CUSTOM_VALUE);
                    this.addStandardFormObject(nextButton, 1, 1);

                    PrimaryKey alphabeticalButtonPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(ButtonsDictionary.SORT_CONTENTS_ALPHABETICALLY);
                    StandardEntryFormButton alphabeticalButton = (StandardEntryFormButton) sefbf
                            .findByPrimaryKey(alphabeticalButtonPk);
                    alphabeticalButton.setDetectorNameAndValue(
                            BUTTON_DETECTOR_NAME,
                            BUTTON_DETECTOR_ALPHABETICAL_VALUE);
                    this.addStandardFormObject(alphabeticalButton, 2, 1);
                } else {
                    /*
                     * get the error message
                     */
                    PrimaryKey onlyEditorCanReorganizeMessagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_ONLY_EDITOR_CAN_REORGANIZE);
                    StandardFormMessage onlyEditorCanReorganizeMessage = (StandardFormMessage) sfmf
                            .findByPrimaryKey(onlyEditorCanReorganizeMessagePk);

                    /*
                     * set the max sizes of the form
                     */
                    this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                    // columns,
                    // 1
                    // line
                    /*
                     * add those objects
                     */
                    this.setCurrentCanvasType(CanvasTypeConstants.CENTER);
                    this.addStandardFormObject(onlyEditorCanReorganizeMessage,
                            1, 1);
                }
            } else {
                this.buildNoRecordFound();
            }
        } else {
            this.buildNoRecordFound();
        }
    }

    /**
     * Builds the layout of the form for a record to view.
     * 
     * 
     * 
     */
    protected void buildAboutRecord() {
        if (this.getStandardArticle() != null) {
            this.buildAbout();
        } else {
            this.buildNoRecordFound();
        }
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
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * find the parent article
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(articleId);
        StandardArticle sd = (StandardArticle) sdf.findByPrimaryKey(articlePk);

        /*
         * build
         */
        if ((sd.getIsFound()) && (sd.getIsVisible())) {
            if (resm.getIsRequestAuthorized(sa.getPrimaryKey(), sd
                    .getPrimaryKey())) {
                this.build();
            } else {
                this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                PrimaryKey cannotRequestArticleEditPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_REQUEST_ARTICLE_EDIT);
                StandardFormMessage cannotRequestArticleEditMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(cannotRequestArticleEditPk);
                this.addStandardFormObject(cannotRequestArticleEditMessage, 1,
                        1);
            }
        } else {
            this.buildNoRecordFound();
        }
    }

    /**
     * Builds the layout of the form for an existing record.
     * 
     * 
     * an AnyLogicContext object
     */
    protected void buildEditRecord() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * build the form
         */
        if (this.getStandardArticle() != null) {
            PrimaryKey homePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ArticlesDictionary.HOME);

            if (this.getStandardArticle().getPrimaryKey().equals(homePk)) {
                if (resm.getIsRequestAuthorized(sa.getPrimaryKey(), this
                        .getStandardArticle().getPrimaryKey())) {
                    this.build();
                } else {
                    this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER);

                    PrimaryKey noHomePageEditingPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_HOME_PAGE_EDITING);
                    StandardFormMessage noHomePageEditingMessage = (StandardFormMessage) sfmf
                            .findByPrimaryKey(noHomePageEditingPk);
                    this.addStandardFormObject(noHomePageEditingMessage, 1, 1);
                }
            } else if (!(resm.getIsRequestAuthorized(sa.getPrimaryKey(), this
                    .getStandardArticle().getPrimaryKey()))) {
                this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER);

                PrimaryKey cannotRequestArticleEditPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_REQUEST_ARTICLE_EDIT);
                StandardFormMessage cannotRequestArticleEditMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(cannotRequestArticleEditPk);
                this.addStandardFormObject(cannotRequestArticleEditMessage, 1,
                        1);
            } else {
                this.build();
            }
        } else {
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
    public void validateExtraReorganizeArticle() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * find the parent article being modified
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(articleId);

        /*
         * get the list of contents
         */
        String[] contentIds = rm
                .getParameterValues(HTTPParameterConstants.CONTENT_ID);

        /*
         * create the list of contents
         */
        List<StandardContent> contents = new ArrayList<StandardContent>();

        for (int i = 0; i < contentIds.length; i++) {
            /*
             * get the content
             */
            PrimaryKey contentPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(contentIds[i]);
            StandardContent currentContent = (StandardContent) srf
                    .findByPrimaryKey(contentPk);

            /*
             * add this content to the list
             */
            contents.add(currentContent);
        }

        /*
         * sort alphabetically
         */
        String sortOption = this.getFormButtonDetectorValue(
                BUTTON_DETECTOR_NAME, BUTTON_DETECTOR_NONE_VALUE);

        if (sortOption.equals(BUTTON_DETECTOR_ALPHABETICAL_VALUE)) {
            /*
             * fill an array with content names
             */
            String[] contentNames = new String[contentIds.length];

            for (int i = 0; i < contentIds.length; i++) {
                /*
                 * get the content
                 */
                StandardContent currentContent = contents.get(i);

                /*
                 * fill the content name
                 */
                contentNames[i] = currentContent.getNameText();
            }

            /*
             * sort this array (ascending order)
             */
            Arrays.sort(contentNames);

            /*
             * assign content orders
             */
            for (int i = 0; i < contentIds.length; i++) {
                /*
                 * get the content
                 */
                StandardContent currentContent = contents.get(i);

                /*
                 * get the name of the content
                 */
                String currentContentName = currentContent.getNameText();

                /*
                 * find the order of this name
                 */
                int currentOrder = 0;

                for (int j = 0; j < contentIds.length; j++) {
                    String currentFoundName = contentNames[j];

                    if (currentFoundName.equals(currentContentName)) {
                        currentOrder = j;
                    }
                }

                /*
                 * assign this order in the content orders array
                 */
                this.contentOrders[i] = String.valueOf(currentOrder);
            }
        }

        /*
         * update all contents
         */
        for (int i = 0; i < contentIds.length; i++) {
            if (this.getIsValid()) {
                /*
                 * get the content
                 */
                StandardContent currentContent = contents.get(i);

                /*
                 * get the order
                 */
                int currentOrder = Integer.parseInt(this.contentOrders[i]);

                /*
                 * change the order
                 */
                currentContent.storeContentOrder(articlePk, currentOrder);
                this.setIsValid(currentContent.getIsDone());

                if (!(currentContent.getIsDone())) {
                    MessageUtil
                            .printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                                    currentContent.getStoreTrace());
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
                    .getAlphanumericSingleKey(MessagesDictionary.VLD_CONTENTS_REORDERED);
            StandardMessage validation = (StandardMessage) smf
                    .findByPrimaryKey(validationPk);

            /*
             * print the message
             */
            validation.printBufferWithIconHTML();
        }

        /*
         * if everything went ok, redirect the user to the same page. otherwise,
         * rollback the current transaction
         */
        if (this.getIsValid()) {
            /*
             * redirect to the current page
             */
            String redirectURL = rm.getStandardPage().getAbsoluteLocation();
            redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                    HTTPParameterConstants.ARTICLE_ID, articleId);

            /*
             * add all current selected block ids parameters to the location
             */
            List<PrimaryKey> currentSelectedBlockIds = BlockUtil
                    .getCurrentSelectedBlockPrimaryKeys();

            for (int i = 0; i < currentSelectedBlockIds.size(); i++) {
                PrimaryKey pk = (PrimaryKey) currentSelectedBlockIds.get(i);
                redirectURL = HTTPUtil
                        .getAddedParameterURL(
                                redirectURL,
                                com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                pk.toString());
            }

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
    public void validateExtraEditRecord() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardEmailFactory semf = (StandardEmailFactory) pfs
                .getStandardBeanFactory(DefaultStandardEmailFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);
        AccountHistoryManager hm = (AccountHistoryManager) pms
                .getManager(DefaultAccountHistoryManager.class);
        GroupHistoryManager ghm = (GroupHistoryManager) pms
                .getManager(DefaultGroupHistoryManager.class);
        ConfigManager cm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * get the property set
         */
        StandardPropertySet prop = cm.getStandardPropertySet();

        /*
         * get the requestor
         */
        StandardAccount requestor = am.getProxyStandardAccount();

        /*
         * get the content for the current article
         */
        PrimaryKey contentPrimaryKey = this.article.getPrimaryKey();
        StandardContent content = (StandardContent) srf
                .findByPrimaryKey(contentPrimaryKey);

        /*
         * remember the article body before the change
         */
        String oldBodyHTML = this.article.getRawBodyHTML();

        /*
         * verify that the name of the content is not all uppercase
         */
        if (this.getIsValid()) {
            if (CaseUtil.getIsTextualAndAllUpperCase(this.getContentName())) {
                this.setIsValid(false);

                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_NAME_CANNOT_BE_ALL_UPPERCASE);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();

                PrimaryKey namePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
                StandardEntryFormField field = (StandardEntryFormField) sefff
                        .findByPrimaryKey(namePk);
                field.setFocusIfFirst(this.getFormName());
            }
        }

        /*
         * verify that the description of the content is not all uppercase
         */
        if (this.getIsValid()) {
            if (CaseUtil.getIsTextualAndAllUpperCase(this
                    .getContentDescription())) {
                this.setIsValid(false);

                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_DESCRIPTION_CANNOT_BE_ALL_UPPERCASE);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();

                PrimaryKey descriptionPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DESCRIPTION);
                StandardEntryFormField field = (StandardEntryFormField) sefff
                        .findByPrimaryKey(descriptionPk);
                field.setFocusIfFirst(this.getFormName());
            }
        }

        /*
         * check whether there is an account with that author name
         */
        List<PrimaryKey> authors = null;

        if (this.getIsValid()) {
            /*
             * check the author
             */
            authors = saf.findEnabledPrimaryKeysByNameOrLogin(this.author);

            /*
             * verify only one account matches
             */
            int size = authors.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no account could be found, print the "no found" message.
                 * otherwise, print the "too many authors found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_ACCOUNT_FOR_AUTHOR);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey authorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(authorPk);
                    field.setFocusIfFirst(this.getFormName());
                } else {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_ACCOUNTS_FOR_AUTHOR);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey authorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(authorPk);
                    field.setFocusIfFirst(this.getFormName());
                }
            } else {
                /*
                 * add this account to the history
                 */
                PrimaryKey accountPk = (PrimaryKey) authors.get(0);
                hm.add(accountPk);
            }
        }

        /*
         * check whether there is an account with that editor name
         */
        List<PrimaryKey> editors = null;

        if (this.getIsValid()) {
            /*
             * check the editor
             */
            editors = saf.findEnabledPrimaryKeysByNameOrLogin(this.editor);

            /*
             * verify only one account matches
             */
            int size = editors.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no account could be found, print the "no found" message.
                 * otherwise, print the "too many editors found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_ACCOUNT_FOR_EDITOR);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey editorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_EDITOR);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(editorPk);
                    field.setFocusIfFirst(this.getFormName());
                } else {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_ACCOUNTS_FOR_EDITOR);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey editorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_EDITOR);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(editorPk);
                    field.setFocusIfFirst(this.getFormName());
                }
            } else {
                /*
                 * add this account to the history
                 */
                PrimaryKey accountPk = (PrimaryKey) editors.get(0);
                hm.add(accountPk);
            }
        }

        /*
         * check whether there is a group with that associate editors name
         */
        List<PrimaryKey> groups = null;

        if ((this.getIsValid())
                && (!(StringUtil.getIsEmpty(this.associateEditors)))) {
            /*
             * check the associateEditors
             */
            groups = sgf.findEnabledPrimaryKeysByName(this.associateEditors);

            /*
             * verify only one groups matches
             */
            int size = groups.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no groups could be found, print the "no found" message.
                 * otherwise, print the "too many groups found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_GROUP_FOR_ASSOCIATE_EDITORS);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey associateEditorsPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ASSOCIATE_EDITORS);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(associateEditorsPk);
                    field.setFocusIfFirst(this.getFormName());
                } else {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_GROUPS_FOR_ASSOCIATE_EDITORS);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey associateEditorsPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ASSOCIATE_EDITORS);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(associateEditorsPk);
                    field.setFocusIfFirst(this.getFormName());
                }
            } else {
                /*
                 * add this groups to the history
                 */
                PrimaryKey groupsPk = groups.get(0);
                ghm.add(groupsPk);
            }
        }

        /*
         * check whether the friendly address is already in use
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.friendlyAddress))) {
                StandardArticle friendlyArticle = sdf
                        .findByFriendlyAddress(this.friendlyAddress);

                if (friendlyArticle.getIsFound()) {
                    this.setIsValid(friendlyArticle.equals(this.article));

                    if (!(friendlyArticle.equals(this.article))) {
                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_FRIENDLY_ADDRESS_ALREADY_USED);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);
                        error.printBufferWithIconHTML();
                    }
                }
            }
        }

        /*
         * find the approver
         */
        StandardArticle requestedParentArticle = null;
        PrimaryKey requestedParentArticlePk = null;
        StandardArticle currentParentArticle = null;
        StandardAccount approver = this.article.getEditorStandardAccount();

        if ((this.getIsValid())
                && (!(StringUtil.getIsEmpty(this.requestedParentArticleId)))) {
            /*
             * get the requested parent article
             */
            requestedParentArticlePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.requestedParentArticleId);
            requestedParentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(requestedParentArticlePk);

            /*
             * get the current parent article
             */
            currentParentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(content.getMainParentPrimaryKey());

            if (resm.getIsRequestRequirementWaived(requestor.getPrimaryKey(),
                    this.article.getPrimaryKey())) {
                /*
                 * modify the approver if the parent article changes
                 */
                if (!(requestedParentArticle.equals(currentParentArticle))) {
                    approver = requestedParentArticle
                            .getEditorStandardAccount();
                }
            }
        }

        /*
         * verify that the editor of the future parent article is the same as
         * the current user if effective immediately requested
         */
        if ((this.getIsValid())
                && (!(StringUtil.getIsEmpty(this.requestedParentArticleId)))) {
            if (!(requestedParentArticle.equals(currentParentArticle))) {
                if (this.approveRequest.equals("Y")) {
                    if (!(resm.getIsRequestRequirementWaived(requestor
                            .getPrimaryKey(), requestedParentArticlePk))) {
                        /*
                         * invalidate the submission
                         */
                        this.setIsValid(false);

                        /*
                         * get the error message
                         */
                        PrimaryKey notImmediateArticleRequestMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_NOT_IMMEDIATE_ARTICLE_REQUEST);
                        StandardMessage notImmediateArticleRequestMessage = (StandardMessage) smf
                                .findByPrimaryKey(notImmediateArticleRequestMessagePk);

                        /*
                         * print the error message
                         */
                        notImmediateArticleRequestMessage
                                .printBufferWithIconHTML();

                        PrimaryKey approveRequestFieldPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_APPROVE_REQUEST);
                        StandardEntryFormField approveRequestField = (StandardEntryFormField) sefff
                                .findByPrimaryKey(approveRequestFieldPk);
                        approveRequestField.setFocusIfFirst(this.getFormName());
                    }
                }
            }
        }

        /*
         * save the friendly address
         */
        String oldFriendlyAddress = this.article.getFriendlyAddress();

        /*
         * try to create the content request
         */
        StandardContentRequest contentRequest = null;

        if (this.getIsValid()) {
            PrimaryKey authorPk = (PrimaryKey) authors.get(0);
            PrimaryKey editorPk = (PrimaryKey) editors.get(0);
            PrimaryKey associateEditorsPk = null;

            if ((groups != null) && (groups.size() == 1)) {
                associateEditorsPk = groups.get(0);
            }

            PrimaryKey classificationTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.classificationType);
            PrimaryKey ruleTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.ruleType);

            contentRequest = (StandardContentRequest) srrf
                    .createArticleUpdateRequest(this.article.getPrimaryKey(),
                            this.getContentName(),
                            this.getContentDescription(), this.body, authorPk,
                            editorPk, associateEditorsPk, this.friendlyAddress,
                            requestedParentArticlePk, classificationTypePk,
                            requestor.getPrimaryKey(),
                            approver.getPrimaryKey(), ruleTypePk,
                            this.versionNotes);
            this.setIsValid(contentRequest.getIsDone());

            if (!(contentRequest.getIsDone())) {
                MessageUtil.printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                        contentRequest.getStoreTrace());
            }
        }

        /*
         * try to approve the content request
         */
        if (this.getIsValid()) {
            if (this.approveRequest.equals("Y")) {
                contentRequest.approveArticleRequest();
                this.setIsValid(contentRequest.getIsDone());

                if (!(contentRequest.getIsDone())) {
                    MessageUtil
                            .printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                                    contentRequest.getStoreTrace());
                }
            }
        }

        /*
         * try to create an email
         */
        StandardEmail email = null;

        if ((this.getIsValid()) && (currentParentArticle != null)) {
            if (StringUtil.getIsEmpty(this.approveRequest)) {
                PrimaryKey subjectMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_UPDATE_TO_APPROVE_SUBJECT);
                StandardMessage subjectMessage = (StandardMessage) smf
                        .findByPrimaryKey(subjectMessagePk);

                PrimaryKey bodyMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_UPDATE_TO_APPROVE_BODY);
                StandardMessage bodyMessage = (StandardMessage) smf
                        .findByPrimaryKey(bodyMessagePk);

                String companyNameText = prop.getCompanyNameText();
                String applicationNameText = prop.getApplicationNameText();
                String articleNameText = this.article.getNameText();
                String articleDescriptionText = this.article
                        .getDescriptionText();
                String requestorNameText = requestor.getFullNameText();
                String requestLocationText = contentRequest
                        .getAbsoluteLocation();

                subjectMessage.replaceMessageText(articleNameText, 1);

                bodyMessage.replaceMessageText(companyNameText, 1);
                bodyMessage.replaceMessageText(applicationNameText, 2);
                bodyMessage.replaceMessageText(requestorNameText, 3);
                bodyMessage.replaceMessageText(articleNameText, 4);
                bodyMessage.replaceMessageText(articleNameText, 5);
                bodyMessage.replaceMessageText(articleDescriptionText, 6);
                bodyMessage.replaceMessageText(requestLocationText, 7);

                email = (StandardEmail) semf.create(subjectMessage
                        .getCurrentMessageText(), bodyMessage
                        .getCurrentMessageText());
                this.setIsValid(email.getIsDone());

                if (!(email.getIsDone())) {
                    MessageUtil
                            .printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                                    contentRequest.getStoreTrace());
                }
            }
        }

        /*
         * try to set the sender
         */
        if ((this.getIsValid()) && (currentParentArticle != null)) {
            if (email != null) {
                email.storeFromPrimaryKey(requestor.getPrimaryKey());

                if (!(email.getIsDone())) {
                    MessageUtil
                            .printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                                    contentRequest.getStoreTrace());
                }
            }
        }

        /*
         * try to add recipients
         */
        if ((this.getIsValid()) && (currentParentArticle != null)) {
            if (email != null) {
                PrimaryKey emailToPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(RolesDictionary.EMAIL_TO);
                PrimaryKey emailCcPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(RolesDictionary.EMAIL_CC);
                email.addRecipient(approver.getPrimaryKey(), emailToPk);
                email.addRecipient(requestor.getPrimaryKey(), emailCcPk);

                if (!(email.getIsDone())) {
                    MessageUtil
                            .printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                                    contentRequest.getStoreTrace());
                }
            }
        }

        /*
         * try to send the email immediately
         */
        if ((this.getIsValid()) && (currentParentArticle != null)) {
            if (email != null) {
                email.sendNow();
            }
        }

        /*
         * print the acknowledgment information
         */
        if (this.getIsValid()) {
            if (this.approveRequest.equals("Y")) {
                /*
                 * get the validation message
                 */
                StandardMessage validation = null;

                if (!(resm.getIsAccessGroupMissing(this.article))) {
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_ARTICLE_UPDATED);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);
                } else {
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_ARTICLE_UPDATED_REVIEW_ACCESS_GROUPS);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);
                }

                /*
                 * add the name of the parent article to the message
                 */
                validation.replaceMessageText(this.article.getNameText(), 1);
                validation.replaceMessageEncoded(this.article.getNameEncoded(),
                        1);
                validation.replaceMessageHTML(this.article.getNameHTML(), 1);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            } else {
                /*
                 * get the validation message
                 */
                PrimaryKey validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_ARTICLE_UPDATE_REQUEST_SENT);
                StandardMessage validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * add the name of the approver to the message
                 */
                validation.replaceMessageText(approver
                        .getFullNameAndLoginText(), 1);
                validation.replaceMessageEncoded(approver
                        .getFullNameAndLoginEncoded(), 1);
                validation.replaceMessageHTML(approver
                        .getFullNameAndLoginHTMLWithLink(), 1);

                /*
                 * get the "my content requests" page
                 */
                PrimaryKey myContentRequestsPagePK = PrimaryKeyUtil
                        .getAlphanumericSingleKey(PagesDictionary.MY_CONTENT_REQUESTS);
                StandardPage myContentRequestsPage = (StandardPage) spf
                        .findByPrimaryKey(myContentRequestsPagePK);

                /*
                 * create the link to that page
                 */
                String myLinkText = myContentRequestsPage.getNameText();
                String myLinkEncoded = myContentRequestsPage.getNameEncoded();
                String myLinkHTML = lw.getBreakableLinkHTML(
                        myContentRequestsPage.getDefaultLocation(), "",
                        myContentRequestsPage.getNameHTML(), null);

                /*
                 * add the link to the my requests page to the message
                 */
                validation.replaceMessageText(myLinkText, 2);
                validation.replaceMessageEncoded(myLinkEncoded, 2);
                validation.replaceMessageHTML(myLinkHTML, 2);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            }
        }

        /*
         * force a reload as the friendly address might have changed
         */
        if (this.getIsValid()) {
            this.article.load();
        }

        /*
         * print the friendly address info
         */
        if (this.getIsValid()) {
            if (this.approveRequest.equals("Y")) {
                /*
                 * save the friendly address
                 */
                String newFriendlyAddress = this.article.getFriendlyAddress();

                if (!(StringUtil.getIsIdentical(oldFriendlyAddress,
                        newFriendlyAddress))) {
                    /*
                     * get the validation message
                     */
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.INF_ARTICLE_FRIENDLY_ADDRESS);
                    StandardMessage validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * get the new location
                     */
                    String newLocationText = this.article.getAbsoluteLocation();
                    String newLocationEncoded = TextFormatUtil
                            .getTextToEncoded(newLocationText);
                    String newLocationHTML = TextFormatUtil
                            .getTextToHTML(newLocationText);

                    /*
                     * add the name of the article to the message
                     */
                    validation.replaceMessageText(newLocationText, 1);
                    validation.replaceMessageEncoded(newLocationEncoded, 1);
                    validation.replaceMessageHTML(newLocationHTML, 1);

                    /*
                     * print the message
                     */
                    validation.printBufferWithIconHTML();
                }
            }
        }

        /*
         * print the subcribers message
         */
        if (this.getIsValid()) {
            if (this.approveRequest.equals("Y")) {
                if (!(this.article.getRawBodyHTML().equals(oldBodyHTML))) {
                    ContentUtil.printSubscriptionMessage(content
                            .getPrimaryKey(), content.getTypePrimaryKey());
                }
            }
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect the user to the parent article detail page.
         * otherwise, rollback the current transaction
         */
        if (this.getIsValid()) {
            /*
             * redirect to the article
             */
            String redirectURL = this.article.getAbsoluteLocation();
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
    public void validateExtraNewRecord() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);
        StandardEmailFactory semf = (StandardEmailFactory) pfs
                .getStandardBeanFactory(DefaultStandardEmailFactory.class);

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
        AccountHistoryManager hm = (AccountHistoryManager) pms
                .getManager(DefaultAccountHistoryManager.class);
        GroupHistoryManager ghm = (GroupHistoryManager) pms
                .getManager(DefaultGroupHistoryManager.class);
        ConfigManager cm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * get the property set
         */
        StandardPropertySet prop = cm.getStandardPropertySet();

        /*
         * get the primary key of the parent article
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(articleId);

        /*
         * get the parent article
         */
        StandardArticle article = (StandardArticle) sdf
                .findByPrimaryKey(articlePk);

        /*
         * get the requestor
         */
        StandardAccount requestor = am.getProxyStandardAccount();

        /*
         * get the approver
         */
        StandardAccount approver = article.getEditorStandardAccount();

        /*
         * verify that the name of the content is not all uppercase
         */
        if (this.getIsValid()) {
            if (CaseUtil.getIsTextualAndAllUpperCase(this.getContentName())) {
                this.setIsValid(false);

                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_NAME_CANNOT_BE_ALL_UPPERCASE);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();

                PrimaryKey namePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
                StandardEntryFormField field = (StandardEntryFormField) sefff
                        .findByPrimaryKey(namePk);
                field.setFocusIfFirst(this.getFormName());
            }
        }

        /*
         * verify that the description of the content is not all uppercase
         */
        if (this.getIsValid()) {
            if (CaseUtil.getIsTextualAndAllUpperCase(this
                    .getContentDescription())) {
                this.setIsValid(false);

                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_DESCRIPTION_CANNOT_BE_ALL_UPPERCASE);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();

                PrimaryKey descriptionPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DESCRIPTION);
                StandardEntryFormField field = (StandardEntryFormField) sefff
                        .findByPrimaryKey(descriptionPk);
                field.setFocusIfFirst(this.getFormName());
            }
        }

        /*
         * check whether there is an account with that author name
         */
        List<PrimaryKey> authors = null;

        if (this.getIsValid()) {
            /*
             * check the author
             */
            authors = saf.findEnabledPrimaryKeysByNameOrLogin(this.author);

            /*
             * verify only one account matches
             */
            int size = authors.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no account could be found, print the "no found" message.
                 * otherwise, print the "too many authors found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_ACCOUNT_FOR_AUTHOR);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey authorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(authorPk);
                    field.setFocusIfFirst(this.getFormName());
                } else {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_ACCOUNTS_FOR_AUTHOR);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey authorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(authorPk);
                    field.setFocusIfFirst(this.getFormName());
                }
            } else {
                /*
                 * add this account to the history
                 */
                PrimaryKey accountPk = (PrimaryKey) authors.get(0);
                hm.add(accountPk);
            }
        }

        /*
         * check whether there is an account with that editor name
         */
        List<PrimaryKey> editors = null;

        if (this.getIsValid()) {
            /*
             * check the editor
             */
            editors = saf.findEnabledPrimaryKeysByNameOrLogin(this.editor);

            /*
             * verify only one account matches
             */
            int size = editors.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no account could be found, print the "no found" message.
                 * otherwise, print the "too many editors found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_ACCOUNT_FOR_EDITOR);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey editorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_EDITOR);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(editorPk);
                    field.setFocusIfFirst(this.getFormName());
                } else {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_ACCOUNTS_FOR_EDITOR);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey editorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_EDITOR);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(editorPk);
                    field.setFocusIfFirst(this.getFormName());
                }
            } else {
                /*
                 * add this account to the history
                 */
                PrimaryKey accountPk = (PrimaryKey) editors.get(0);
                hm.add(accountPk);
            }
        }

        /*
         * check whether there is a group with that associate editors name
         */
        List<PrimaryKey> groups = null;

        if ((this.getIsValid())
                && (!(StringUtil.getIsEmpty(this.associateEditors)))) {
            /*
             * check the associateEditors
             */
            groups = sgf.findEnabledPrimaryKeysByName(this.associateEditors);

            /*
             * verify only one groups matches
             */
            int size = groups.size();

            if (size != 1) {
                this.setIsValid(false);

                /*
                 * if no groups could be found, print the "no found" message.
                 * otherwise, print the "too many groups found" message
                 */
                if (size == 0) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_GROUP_FOR_ASSOCIATE_EDITORS);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey associateEditorsPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ASSOCIATE_EDITORS);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(associateEditorsPk);
                    field.setFocusIfFirst(this.getFormName());
                } else {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_TOO_MANY_GROUPS_FOR_ASSOCIATE_EDITORS);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();

                    PrimaryKey associateEditorsPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ASSOCIATE_EDITORS);
                    StandardEntryFormField field = (StandardEntryFormField) sefff
                            .findByPrimaryKey(associateEditorsPk);
                    field.setFocusIfFirst(this.getFormName());
                }
            } else {
                /*
                 * add this groups to the history
                 */
                PrimaryKey groupsPk = groups.get(0);
                ghm.add(groupsPk);
            }
        }

        /*
         * check whether the friendly address is already in use
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.friendlyAddress))) {
                StandardArticle friendlyArticle = sdf
                        .findByFriendlyAddress(this.friendlyAddress);
                this.setIsValid(!(friendlyArticle.getIsFound()));

                if (friendlyArticle.getIsFound()) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_FRIENDLY_ADDRESS_ALREADY_USED);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();
                }
            }
        }

        /*
         * try to create the content request
         */
        StandardContentRequest contentRequest = null;

        if (this.getIsValid()) {
            PrimaryKey authorPk = (PrimaryKey) authors.get(0);
            PrimaryKey editorPk = (PrimaryKey) editors.get(0);
            PrimaryKey associateEditorsPk = null;

            if ((groups != null) && (groups.size() == 1)) {
                associateEditorsPk = groups.get(0);
            }

            PrimaryKey classificationTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.classificationType);
            PrimaryKey ruleTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.ruleType);

            contentRequest = (StandardContentRequest) srrf
                    .createNewArticleRequest(this.getContentName(), this
                            .getContentDescription(), this.body, authorPk,
                            editorPk, associateEditorsPk, this.friendlyAddress,
                            articlePk, classificationTypePk, requestor
                                    .getPrimaryKey(), approver.getPrimaryKey(),
                            ruleTypePk, this.versionNotes);
            this.setIsValid(contentRequest.getIsDone());

            if (!(contentRequest.getIsDone())) {
                MessageUtil.printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                        contentRequest.getStoreTrace());
            }
        }

        /*
         * try to approve the content request
         */
        StandardArticle newArticle = null;

        if (this.getIsValid()) {
            if (this.approveRequest.equals("Y")) {
                newArticle = contentRequest.approveArticleRequest();
                this.setIsValid(contentRequest.getIsDone());

                if ((newArticle != null) && (!(newArticle.getIsDone()))) {
                    MessageUtil.printBufferInternalErrorHTML(
                            ABSTRACT_CLASS_NAME, newArticle.getStoreTrace());
                }
            }
        }

        /*
         * try to create an email
         */
        StandardEmail email = null;

        if (this.getIsValid()) {
            if (StringUtil.getIsEmpty(this.approveRequest)) {
                PrimaryKey subjectMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_ARTICLE_TO_APPROVE_SUBJECT);
                StandardMessage subjectMessage = (StandardMessage) smf
                        .findByPrimaryKey(subjectMessagePk);

                PrimaryKey bodyMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_ARTICLE_TO_APPROVE_BODY);
                StandardMessage bodyMessage = (StandardMessage) smf
                        .findByPrimaryKey(bodyMessagePk);

                String companyNameText = prop.getCompanyNameText();
                String applicationNameText = prop.getApplicationNameText();
                String articleNameText = contentRequest.getNameText();
                String articleDescriptionText = contentRequest
                        .getDescriptionText();
                String parentArticleNameText = article.getNameText();
                String requestorNameText = requestor.getFullNameText();
                String requestLocationText = contentRequest
                        .getAbsoluteLocation();

                subjectMessage.replaceMessageText(articleNameText, 1);

                bodyMessage.replaceMessageText(companyNameText, 1);
                bodyMessage.replaceMessageText(applicationNameText, 2);
                bodyMessage.replaceMessageText(requestorNameText, 3);
                bodyMessage.replaceMessageText(articleNameText, 4);
                bodyMessage.replaceMessageText(parentArticleNameText, 5);
                bodyMessage.replaceMessageText(articleDescriptionText, 6);
                bodyMessage.replaceMessageText(requestLocationText, 7);

                email = (StandardEmail) semf.create(subjectMessage
                        .getCurrentMessageText(), bodyMessage
                        .getCurrentMessageText());
                this.setIsValid(email.getIsDone());

                if (!(email.getIsDone())) {
                    MessageUtil
                            .printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                                    contentRequest.getStoreTrace());
                }
            }
        }

        /*
         * try to set the sender
         */
        if (this.getIsValid()) {
            if (email != null) {
                email.storeFromPrimaryKey(requestor.getPrimaryKey());

                if (!(email.getIsDone())) {
                    MessageUtil
                            .printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                                    contentRequest.getStoreTrace());
                }
            }
        }

        /*
         * try to add recipients
         */
        if (this.getIsValid()) {
            if (email != null) {
                PrimaryKey emailToPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(RolesDictionary.EMAIL_TO);
                PrimaryKey emailCcPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(RolesDictionary.EMAIL_CC);
                email.addRecipient(approver.getPrimaryKey(), emailToPk);
                email.addRecipient(requestor.getPrimaryKey(), emailCcPk);

                if (!(email.getIsDone())) {
                    MessageUtil
                            .printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                                    contentRequest.getStoreTrace());
                }
            }
        }

        /*
         * try to send the email immediately
         */
        if (this.getIsValid()) {
            if (email != null) {
                email.sendNow();
            }
        }

        /*
         * print the acknowledgment information
         */
        if (this.getIsValid()) {
            if (this.approveRequest.equals("Y")) {
                /*
                 * get the validation message
                 */
                StandardMessage validation = null;

                if (!(resm.getIsAccessGroupMissing(newArticle))) {
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_NEW_ARTICLE_CREATED);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);
                } else {
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_NEW_ARTICLE_CREATED_ADD_ACCESS_GROUPS);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);
                }

                /*
                 * add the name of the parent article to the message
                 */
                validation.replaceMessageText(newArticle.getNameText(), 1);
                validation
                        .replaceMessageEncoded(newArticle.getNameEncoded(), 1);
                validation.replaceMessageHTML(newArticle.getNameHTML(), 1);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            } else {
                /*
                 * get the validation message
                 */
                PrimaryKey validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_NEW_ARTICLE_REQUEST_SENT);
                StandardMessage validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * add the name of the approver to the message
                 */
                validation.replaceMessageText(approver
                        .getFullNameAndLoginText(), 1);
                validation.replaceMessageEncoded(approver
                        .getFullNameAndLoginEncoded(), 1);
                validation.replaceMessageHTML(approver
                        .getFullNameAndLoginHTMLWithLink(), 1);

                /*
                 * get the "my content requests" page
                 */
                PrimaryKey myContentRequestsPagePK = PrimaryKeyUtil
                        .getAlphanumericSingleKey(PagesDictionary.MY_CONTENT_REQUESTS);
                StandardPage myContentRequestsPage = (StandardPage) spf
                        .findByPrimaryKey(myContentRequestsPagePK);

                /*
                 * create the link to that page
                 */
                String myLinkText = myContentRequestsPage.getNameText();
                String myLinkEncoded = myContentRequestsPage.getNameEncoded();
                String myLinkHTML = lw.getBreakableLinkHTML(
                        myContentRequestsPage.getDefaultLocation(), "",
                        myContentRequestsPage.getNameHTML(), null);

                /*
                 * add the link to the my requests page to the message
                 */
                validation.replaceMessageText(myLinkText, 2);
                validation.replaceMessageEncoded(myLinkEncoded, 2);
                validation.replaceMessageHTML(myLinkHTML, 2);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            }
        }

        /*
         * print the friendly address info
         */
        if (this.getIsValid()) {
            if (this.approveRequest.equals("Y")) {
                /*
                 * get the validation message
                 */
                PrimaryKey validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.INF_ARTICLE_FRIENDLY_ADDRESS);
                StandardMessage validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * get the new location
                 */
                String newLocationText = newArticle.getAbsoluteLocation();
                String newLocationEncoded = TextFormatUtil
                        .getTextToEncoded(newLocationText);
                String newLocationHTML = TextFormatUtil
                        .getTextToHTML(newLocationText);

                /*
                 * add the name of the article to the message
                 */
                validation.replaceMessageText(newLocationText, 1);
                validation.replaceMessageEncoded(newLocationEncoded, 1);
                validation.replaceMessageHTML(newLocationHTML, 1);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            }
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect the user to the parent article detail page.
         * otherwise, rollback the current transaction
         */
        if (this.getIsValid()) {
            /*
             * redirect
             */
            if (this.approveRequest.equals("Y")) {
                /*
                 * redirect to the parent article detail page for the new
                 * article
                 */
                if (!(resm.getIsAccessGroupMissing(newArticle))) {
                    String redirectURL = newArticle.getAbsoluteLocation();
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                } else {
                    /*
                     * get the "article edit" page
                     */
                    PrimaryKey editArticlePagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(PagesDictionary.ARTICLE_PROPERTIES);
                    StandardPage editArticlePage = (StandardPage) spf
                            .findByPrimaryKey(editArticlePagePk);

                    /*
                     * add the parent article id and the access groups block id
                     */
                    PrimaryKey newArticlePk = newArticle.getPrimaryKey();
                    String redirectURL = editArticlePage.getAbsoluteLocation();
                    redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                            HTTPParameterConstants.ARTICLE_ID, newArticlePk
                                    .toString());
                    redirectURL = HTTPUtil
                            .getAddedParameterURL(
                                    redirectURL,
                                    com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                    BlocksDictionary.ARTICLE_ACCESS_GROUPS);

                    /*
                     * do the redirect
                     */
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                }
            } else {
                /*
                 * redirect to the parent article detail page for the current
                 * article
                 */
                String redirectURL = article.getAbsoluteLocation();
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
            }
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }
    }

    /**
     * Builds the layout of the form for summary information.
     * 
     * 
     * 
     */
    protected void buildSummary() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntryFormLabelFactory seflf = (StandardEntryFormLabelFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormLabelFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);
        StandardContentTypeFactory srtf = (StandardContentTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentTypeFactory.class);
        StandardContentClassificationTypeFactory srctf = (StandardContentClassificationTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentClassificationTypeFactory.class);
        StandardContentRuleTypeFactory srrtf = (StandardContentRuleTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRuleTypeFactory.class);

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
         * set the max sizes of the form
         */
        this.setMaxSizes(2, 14, CanvasTypeConstants.CENTER);

        /*
         * get the primary keys of the required instances
         */
        PrimaryKey entityPk = entity.getPrimaryKey();
        PrimaryKey articleNameFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
        PrimaryKey articleDescriptionFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DESCRIPTION);
        PrimaryKey contentTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_TYPE);
        PrimaryKey articleAddressFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ADDRESS);
        PrimaryKey articleAuthorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
        PrimaryKey articleEditorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_EDITOR);
        PrimaryKey articleAssociateEditorsFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ASSOCIATE_EDITORS);
        PrimaryKey articleClassificationTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CLASSIFICATION);
        PrimaryKey articleRuleTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_RULE);
        PrimaryKey articlePathFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PATH);
        PrimaryKey articleCreatedFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CREATED);
        PrimaryKey articleModifiedFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_MODIFIED);
        PrimaryKey articleFriendlyAddressFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FRIENDLY_ADDRESS);
        PrimaryKey articleVersionFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.ARTICLE_VERSION);

        PrimaryKey articleNameLabelPk = seflf.getPrimaryKey(entityPk,
                articleNameFieldPk);
        PrimaryKey articleDescriptionLabelPk = seflf.getPrimaryKey(entityPk,
                articleDescriptionFieldPk);
        PrimaryKey contentTypeLabelPk = seflf.getPrimaryKey(entityPk,
                contentTypeFieldPk);
        PrimaryKey articleAddressLabelPk = seflf.getPrimaryKey(entityPk,
                articleAddressFieldPk);
        PrimaryKey articleAuthorLabelPk = seflf.getPrimaryKey(entityPk,
                articleAuthorFieldPk);
        PrimaryKey articleEditorLabelPk = seflf.getPrimaryKey(entityPk,
                articleEditorFieldPk);
        PrimaryKey articleAssociateEditorsLabelPk = seflf.getPrimaryKey(
                entityPk, articleAssociateEditorsFieldPk);
        PrimaryKey articleClassificationTypeLabelPk = seflf.getPrimaryKey(
                entityPk, articleClassificationTypeFieldPk);
        PrimaryKey articleRuleTypeLabelPk = seflf.getPrimaryKey(entityPk,
                articleRuleTypeFieldPk);
        PrimaryKey articlePathLabelPk = seflf.getPrimaryKey(entityPk,
                articlePathFieldPk);
        PrimaryKey articleCreatedLabelPk = seflf.getPrimaryKey(entityPk,
                articleCreatedFieldPk);
        PrimaryKey articleModifiedLabelPk = seflf.getPrimaryKey(entityPk,
                articleModifiedFieldPk);
        PrimaryKey articleFriendlyAddressLabelPk = seflf.getPrimaryKey(
                entityPk, articleFriendlyAddressFieldPk);
        PrimaryKey articleVersionLabelPk = seflf.getPrimaryKey(entityPk,
                articleVersionFieldPk);

        /*
         * instantiate all entry fields
         */
        StandardEntryFormField articleNameField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleNameFieldPk);
        StandardEntryFormField articleDescriptionField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleDescriptionFieldPk);
        StandardEntryFormField contentTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentTypeFieldPk);
        StandardEntryFormField articleAddressField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleAddressFieldPk);
        StandardEntryFormField articleAuthorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleAuthorFieldPk);
        StandardEntryFormField articleEditorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleEditorFieldPk);
        StandardEntryFormField articleAssociateEditorsField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleAssociateEditorsFieldPk);
        StandardEntryFormField articleClassificationTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleClassificationTypeFieldPk);
        StandardEntryFormField articleRuleTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleRuleTypeFieldPk);
        StandardEntryFormField articlePathField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articlePathFieldPk);
        StandardEntryFormField articleCreatedField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleCreatedFieldPk);
        StandardEntryFormField articleModifiedField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleModifiedFieldPk);
        StandardEntryFormField articleFriendlyAddressField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleFriendlyAddressFieldPk);
        StandardEntryFormField articleVersionField = (StandardEntryFormField) sefff
                .findByPrimaryKey(articleVersionFieldPk);

        /*
         * instantiate all entry labels
         */
        StandardFormLabel articleNameLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleNameLabelPk);
        StandardEntryFormLabel articleDescriptionLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleDescriptionLabelPk);
        StandardEntryFormLabel contentTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentTypeLabelPk);
        StandardEntryFormLabel articleAddressLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleAddressLabelPk);
        StandardEntryFormLabel articleAuthorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleAuthorLabelPk);
        StandardEntryFormLabel articleEditorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleEditorLabelPk);
        StandardEntryFormLabel articleAssociateEditorsLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleAssociateEditorsLabelPk);
        StandardEntryFormLabel articleClassificationTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleClassificationTypeLabelPk);
        StandardEntryFormLabel articleRuleTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleRuleTypeLabelPk);
        StandardEntryFormLabel articlePathLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articlePathLabelPk);
        StandardEntryFormLabel articleCreatedLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleCreatedLabelPk);
        StandardEntryFormLabel articleModifiedLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleModifiedLabelPk);
        StandardEntryFormLabel articleFriendlyAddressLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleFriendlyAddressLabelPk);
        StandardEntryFormLabel articleVersionLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(articleVersionLabelPk);

        /*
         * associate them
         */
        articleNameLabel.associateStandardFormField(articleNameField);
        articleDescriptionLabel
                .associateStandardFormField(articleDescriptionField);
        contentTypeLabel.associateStandardFormField(contentTypeField);
        articleAuthorLabel.associateStandardFormField(articleAuthorField);
        articleEditorLabel.associateStandardFormField(articleEditorField);
        articleAssociateEditorsLabel
                .associateStandardFormField(articleAssociateEditorsField);
        articleClassificationTypeLabel
                .associateStandardFormField(articleClassificationTypeField);
        articleRuleTypeLabel.associateStandardFormField(articleRuleTypeField);
        articleAddressLabel.associateStandardFormField(articleAddressField);
        articlePathLabel.associateStandardFormField(articlePathField);
        articleCreatedLabel.associateStandardFormField(articleCreatedField);
        articleModifiedLabel.associateStandardFormField(articleModifiedField);
        articleFriendlyAddressLabel
                .associateStandardFormField(articleFriendlyAddressField);
        articleVersionLabel.associateStandardFormField(articleVersionField);

        /*
         * set the appropriate fields in print only mode
         */
        articleNameField.setPrintOnlyType();
        articleDescriptionField.setPrintOnlyType();
        contentTypeField.setPrintOnlyType();
        articleAuthorField.setPrintOnlyType();
        articleEditorField.setPrintOnlyType();
        articleAssociateEditorsField.setPrintOnlyType();
        articleClassificationTypeField.setPrintOnlyType();
        articleRuleTypeField.setPrintOnlyType();
        articleAddressField.setPrintOnlyType();
        articlePathField.setPrintOnlyType();
        articleCreatedField.setPrintOnlyType();
        articleModifiedField.setPrintOnlyType();
        articleFriendlyAddressField.setPrintOnlyType();
        articleVersionField.setPrintOnlyType();

        /*
         * fill with the default values.
         */
        articleNameField.setFirstDefaultValue(this.article.getNameText());
        articleNameField.setFirstDefaultValueHTML(this.article.getNameHTML());
        articleVersionField.setFirstDefaultValue(""
                + this.article.getVersionNum());
        articleDescriptionField.setFirstDefaultValue(this.article
                .getLongDescriptionText());
        articleDescriptionField.setFirstDefaultValueHTML(this.article
                .getLongDescriptionHTML());

        /*
         * add the content type
         */
        PrimaryKey contentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
        StandardContentType contentType = (StandardContentType) srtf
                .findByPrimaryKey(contentTypePk);
        contentTypeField.setFirstDefaultValue(contentType.getNameText());

        /*
         * add the author
         */
        StandardAccount authorStandardAccount = this.article
                .getAuthorStandardAccount();

        if (authorStandardAccount.getIsFound()) {
            articleAuthorField.setFirstDefaultValue(authorStandardAccount
                    .getFullNameAndLoginText());
            articleAuthorField.setFirstDefaultValueHTML(authorStandardAccount
                    .getFullNameAndLoginHTMLWithLink());
        } else {
            articleAuthorField.setFirstDefaultValue("");
        }

        /*
         * add the editor
         */
        StandardAccount editorStandardAccount = this.article
                .getEditorStandardAccount();

        if (editorStandardAccount.getIsFound()) {
            articleEditorField.setFirstDefaultValue(editorStandardAccount
                    .getFullNameAndLoginText());
            articleEditorField.setFirstDefaultValueHTML(editorStandardAccount
                    .getFullNameAndLoginHTMLWithLink());
        } else {
            articleEditorField.setFirstDefaultValue("");
        }

        /*
         * add the associate editors
         */
        StandardGroup associateEditorsStandardGroup = this.article
                .getAssociateEditorsStandardGroup();

        if ((associateEditorsStandardGroup != null)
                && (associateEditorsStandardGroup.getIsFound())) {
            articleAssociateEditorsField
                    .setFirstDefaultValue(associateEditorsStandardGroup
                            .getNameText());

            String associateEditorsHTML = lw.getBreakableLinkHTML(
                    associateEditorsStandardGroup.getDefaultLocation(), "",
                    associateEditorsStandardGroup.getNameHTML(), null);
            articleAssociateEditorsField
                    .setFirstDefaultValueHTML(associateEditorsHTML);
        } else {
            articleAssociateEditorsField.setFirstDefaultValue("");
        }

        /*
         * add the classification type
         */
        PrimaryKey contentClassificationTypePk = this.article
                .getClassificationTypePrimaryKey();
        StandardContentClassificationType contentClassificationType = (StandardContentClassificationType) srctf
                .findByPrimaryKey(contentClassificationTypePk);
        String contentClassificationTypeNameText = contentClassificationType
                .getNameText();

        Searched contentClassificationSearched = this.article
                .getClassificationSearched();
        PrimaryKey searchedContentClassificationTypePk = contentClassificationSearched
                .getClassificationTypePrimaryKey();
        StandardContentClassificationType searchedContentClassificationType = (StandardContentClassificationType) srctf
                .findByPrimaryKey(searchedContentClassificationTypePk);
        String searchedContentClassificationTypeNameText = searchedContentClassificationType
                .getNameText();

        if (!(searchedContentClassificationTypeNameText
                .equals(contentClassificationTypeNameText))) {
            contentClassificationTypeNameText = ConcatUtil
                    .getConcatWithParentheses(
                            contentClassificationTypeNameText,
                            searchedContentClassificationTypeNameText,
                            contentClassificationTypeNameText,
                            searchedContentClassificationTypeNameText);
        }

        articleClassificationTypeField
                .setFirstDefaultValue(contentClassificationTypeNameText);

        /*
         * add the rule type
         */
        PrimaryKey contentRuleTypePk = this.article.getRuleTypePrimaryKey();
        StandardContentRuleType contentRuleType = (StandardContentRuleType) srrtf
                .findByPrimaryKey(contentRuleTypePk);
        String contentRuleTypeNameText = contentRuleType.getNameText();

        Searched contentRuleSearched = this.article.getRuleSearched();
        PrimaryKey searchedContentRuleTypePk = contentRuleSearched
                .getRuleTypePrimaryKey();
        StandardContentRuleType searchedContentRuleType = (StandardContentRuleType) srrtf
                .findByPrimaryKey(searchedContentRuleTypePk);
        String searchedContentRuleTypeNameText = searchedContentRuleType
                .getNameText();

        if (!(searchedContentRuleTypeNameText.equals(contentRuleTypeNameText))) {
            contentRuleTypeNameText = ConcatUtil.getConcatWithParentheses(
                    contentRuleTypeNameText, searchedContentRuleTypeNameText,
                    contentRuleTypeNameText, searchedContentRuleTypeNameText);
        }

        articleRuleTypeField.setFirstDefaultValue(contentRuleTypeNameText);

        /*
         * add the other fields
         */
        articleAddressField.setFirstDefaultValue(this.article
                .getAbsoluteLocation());
        articlePathField.setFirstDefaultValue(this.article.getPathText());
        articlePathField.setFirstDefaultValueHTML(this.article
                .getPathHTMLWithLink());
        articleFriendlyAddressField.setFirstDefaultValue(this.article
                .getFriendlyAddress());
        articleCreatedField.setFirstDefaultValue(DateFormatUtil
                .getLongFormattedDateText(this.article
                        .getFirstEntryLogTimestamp()));
        articleModifiedField.setFirstDefaultValue(DateFormatUtil
                .getLongFormattedDateText(this.article
                        .getLastEntryLogTimestamp()));

        /*
         * get the first request
         */
        StandardContentRequest firstContentRequest = null;

        if (this.article.getFirstRequestPrimaryKey() != null) {
            firstContentRequest = (StandardContentRequest) srrf
                    .findByPrimaryKey(this.article.getFirstRequestPrimaryKey());

            if (firstContentRequest.getIsFound()) {
                articleCreatedField.setFirstDefaultValueHTML(lw
                        .getBreakableLinkHTML(firstContentRequest
                                .getDefaultLocation(), null, DateFormatUtil
                                .getLongFormattedDateText(this.article
                                        .getFirstEntryLogTimestamp()), null));
            }
        }

        /*
         * get the last request
         */
        StandardContentRequest lastContentRequest = null;

        if (this.article.getLastRequestPrimaryKey() != null) {
            lastContentRequest = (StandardContentRequest) srrf
                    .findByPrimaryKey(this.article.getLastRequestPrimaryKey());

            if (lastContentRequest.getIsFound()) {
                articleModifiedField.setFirstDefaultValueHTML(lw
                        .getBreakableLinkHTML(lastContentRequest
                                .getDefaultLocation(), null, DateFormatUtil
                                .getLongFormattedDateText(this.article
                                        .getLastEntryLogTimestamp()), null));
            }
        }

        /*
         * set the current line number
         */
        int lineNumber = 0;

        /*
         * add those objects
         */
        this.addStandardFormObject(articleNameLabel, 1, ++lineNumber);
        this.addStandardFormObject(articleNameField, 2, lineNumber);
        this.addStandardFormObject(articleDescriptionLabel, 1, ++lineNumber);
        this.addStandardFormObject(articleDescriptionField, 2, lineNumber);
        this.addStandardFormObject(contentTypeLabel, 1, ++lineNumber);
        this.addStandardFormObject(contentTypeField, 2, lineNumber);
        this.addStandardFormObject(articleVersionLabel, 1, ++lineNumber);
        this.addStandardFormObject(articleVersionField, 2, lineNumber);
        this.addStandardFormObject(articlePathLabel, 1, ++lineNumber);
        this.addStandardFormObject(articlePathField, 2, lineNumber);
        this.addStandardFormObject(articleAuthorLabel, 1, ++lineNumber);
        this.addStandardFormObject(articleAuthorField, 2, lineNumber);
        this.addStandardFormObject(articleEditorLabel, 1, ++lineNumber);
        this.addStandardFormObject(articleEditorField, 2, lineNumber);
        this.addStandardFormObject(articleAssociateEditorsLabel, 1,
                ++lineNumber);
        this.addStandardFormObject(articleAssociateEditorsField, 2, lineNumber);
        this
                .addStandardFormObject(articleFriendlyAddressLabel, 1,
                        ++lineNumber);
        this.addStandardFormObject(articleFriendlyAddressField, 2, lineNumber);
        this.addStandardFormObject(articleAddressLabel, 1, ++lineNumber);
        this.addStandardFormObject(articleAddressField, 2, lineNumber);
        this.addStandardFormObject(articleClassificationTypeLabel, 1,
                ++lineNumber);
        this.addStandardFormObject(articleClassificationTypeField, 2,
                lineNumber);
        this.addStandardFormObject(articleRuleTypeLabel, 1, ++lineNumber);
        this.addStandardFormObject(articleRuleTypeField, 2, lineNumber);
        this.addStandardFormObject(articleCreatedLabel, 1, ++lineNumber);
        this.addStandardFormObject(articleCreatedField, 2, lineNumber);
        this.addStandardFormObject(articleModifiedLabel, 1, ++lineNumber);
        this.addStandardFormObject(articleModifiedField, 2, lineNumber);
    }

    /**
     * Returns the article viewed by this form.
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardArticle object
     * @see #setStandardArticle
     */
    public StandardArticle getStandardArticle() {
        return this.article;
    }

    /**
     * Sets the article viewed by this form.
     * 
     * @param sr
     *            standard article viewed by this form
     * @see #getStandardArticle
     */
    public void setStandardArticle(StandardArticle sr) {
        this.article = sr;
    }

    /**
     * Builds the layout of the form for a record summary to view.
     * 
     * 
     * 
     */
    protected void buildSummaryRecord() {
        if (this.getStandardArticle() != null) {
            this.buildSummary();
        } else {
            this.buildNoRecordFound();
        }
    }

    /**
     * Sets the default classification type to use when this form is printed for
     * the first time.
     * 
     * @param classificationType
     *            classification type to set
     */
    public void setFirstTimeClassificationType(String classificationType) {
        this.classificationType = classificationType;
    }

    /**
     * Sets the default editor to use when this form is printed for the first
     * time.
     * 
     * @param editor
     *            editor to set
     */
    public void setFirstTimeEditor(String editor) {
        this.editor = editor;
    }

    /**
     * Sets the default author to use when this form is printed for the first
     * time.
     * 
     * @param author
     *            author to set
     */
    public void setFirstTimeAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets the default name to use when this form is printed for the first
     * time.
     * 
     * @param name
     *            name to set
     */
    public void setFirstTimeName(String name) {
        this.setContentName(ContentUtil.getCleanSuggestedName(name));
    }

    /**
     * Sets the default friendly address to use when this form is printed for
     * the first time.
     * 
     * @param friendlyAddress
     *            friendly address to set
     */
    public void setFirstTimeFriendlyAddress(String friendlyAddress) {
        this.friendlyAddress = ContentUtil
                .getCleanFriendlyAddress(friendlyAddress);
    }

    /**
     * Sets the default approve request to use when this form is printed for the
     * first time.
     * 
     * @param approveRequest
     *            approve request to set
     */
    public void setFirstTimeApproveRequest(String approveRequest) {
        this.approveRequest = approveRequest;
    }
}

// end AbstractArticleEntryBlock
