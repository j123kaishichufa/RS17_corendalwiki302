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

import com.corendal.netapps.framework.configuration.utils.CaseUtil;
import com.corendal.netapps.framework.configuration.utils.FileUtil;
import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.constants.AccessTypeConstants;
import com.corendal.netapps.framework.core.constants.CanvasTypeConstants;
import com.corendal.netapps.framework.core.constants.EncodingTypeConstants;
import com.corendal.netapps.framework.core.constants.LayoutTypeConstants;
import com.corendal.netapps.framework.core.dictionaries.RolesDictionary;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormLabelFactory;
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
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormButton;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormField;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormLabel;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.interfaces.StandardFormLabel;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessage;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLocale;
import com.corendal.netapps.framework.core.interfaces.StandardLookup;
import com.corendal.netapps.framework.core.interfaces.StandardLookupFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFile;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFileFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultAccountHistoryManager;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.managers.DefaultLocaleManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardLookupFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardEmailFactory;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardStoredFileFactory;
import com.corendal.netapps.framework.helpdesk.dictionaries.LookupsDictionary;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
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
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentRuleTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentTypeFactory;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractImageEntryBlock is the parent block common to all image viewing
 * blocks.
 * 
 * @version $Id: AbstractImageEntryBlock.java,v 1.1 2005/09/06 21:25:28 tdanard
 *          Exp $
 */
public abstract class AbstractImageEntryBlock extends
        AbstractCommonContentEntryBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractImageEntryBlock";

    /** Author entered by the user */
    private String author;

    /** Classification type entered by the user */
    private String classificationType;

    /** Rule type entered by the user */
    private String ruleType;

    /** Approve request entered by the user */
    private String approveRequest;

    /** Image to be viewed */
    private StandardImage image;

    /** Requested parent article selected through this form */
    private String requestedParentArticleId;

    /** Friendly address entered by the user */
    private String friendlyAddress;

    /** Version notes entered by the user */
    private String versionNotes;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractImageEntryBlock() {
        this.image = null;
    }

    /**
     * Returns a clone. Overrides AbstractCommonContentEntryBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractImageEntryBlock) super.clone();
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
         * get the home article
         */
        PrimaryKey homePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ArticlesDictionary.HOME);

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
         * find the image being modified
         */
        String imageId = rm.getParameter(HTTPParameterConstants.IMAGE_ID);
        PrimaryKey imagePk = PrimaryKeyUtil.getAlphanumericSingleKey(imageId);

        /*
         * find the parent article
         */
        String parentArticleId = rm
                .getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey parentArticlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(parentArticleId);

        if ((this.getAccessType().equals(AccessTypeConstants.VIEW))
                || (this.getAccessType().equals(AccessTypeConstants.EDIT))) {
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(imagePk);
            parentArticlePk = content.getMainParentPrimaryKey();
        }

        /*
         * modify the encoding type to multipart
         */
        this.setEncodingType(EncodingTypeConstants.MULTIPART);

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
            this.setMaxSizes(3, 9, CanvasTypeConstants.CENTER);
            this.setMaxSizes(3, 1, CanvasTypeConstants.SOUTH);
        } else {
            this.setMaxSizes(3, 8, CanvasTypeConstants.CENTER);
            this.setMaxSizes(3, 1, CanvasTypeConstants.SOUTH);
        }

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
        PrimaryKey authorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
        PrimaryKey classificationTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CLASSIFICATION);
        PrimaryKey ruleTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_RULE);
        PrimaryKey fileFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FILE);
        PrimaryKey versionNotesFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_VERSION_NOTES);
        PrimaryKey friendlyAddressFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FRIENDLY_ADDRESS);
        PrimaryKey approveRequestFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_APPROVE_REQUEST);

        PrimaryKey nameLabelPk = seflf.getPrimaryKey(entityPk, nameFieldPk);
        PrimaryKey descriptionLabelPk = seflf.getPrimaryKey(entityPk,
                descriptionFieldPk);
        PrimaryKey parentArticleLabelPk = seflf.getPrimaryKey(entityPk,
                parentArticleFieldPk);
        PrimaryKey authorLabelPk = seflf.getPrimaryKey(entityPk, authorFieldPk);
        PrimaryKey classificationTypeLabelPk = seflf.getPrimaryKey(entityPk,
                classificationTypeFieldPk);
        PrimaryKey ruleTypeLabelPk = seflf.getPrimaryKey(entityPk,
                ruleTypeFieldPk);
        PrimaryKey fileLabelPk = seflf.getPrimaryKey(entityPk, fileFieldPk);
        PrimaryKey versionNotesLabelPk = seflf.getPrimaryKey(entityPk,
                versionNotesFieldPk);
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

        /*
         * instantiate all entry fields
         */
        StandardEntryFormField nameField = (StandardEntryFormField) sefff
                .findByPrimaryKey(nameFieldPk);
        StandardEntryFormField descriptionField = (StandardEntryFormField) sefff
                .findByPrimaryKey(descriptionFieldPk);
        descriptionField.setTextAreaType(60, 4);

        StandardEntryFormField parentArticleField = (StandardEntryFormField) sefff
                .findByPrimaryKey(parentArticleFieldPk);
        StandardEntryFormField authorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(authorFieldPk);
        authorField.setStandardLookup(accountLookup);
        authorField.setIsWildcardSubstituted(true);

        StandardEntryFormField classificationTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(classificationTypeFieldPk);
        StandardEntryFormField ruleTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(ruleTypeFieldPk);

        StandardEntryFormField fileField = (StandardEntryFormField) sefff
                .findByPrimaryKey(fileFieldPk);
        fileField.setFileType();

        StandardEntryFormField versionNotesField = (StandardEntryFormField) sefff
                .findByPrimaryKey(versionNotesFieldPk);
        versionNotesField.setTextAreaType(60, 4);

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
        StandardEntryFormLabel parentArticleLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(parentArticleLabelPk);
        StandardEntryFormLabel authorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(authorLabelPk);
        StandardEntryFormLabel classificationTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(classificationTypeLabelPk);
        StandardEntryFormLabel ruleTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(ruleTypeLabelPk);
        StandardEntryFormLabel fileLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(fileLabelPk);
        StandardEntryFormLabel versionNotesLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(versionNotesLabelPk);
        StandardEntryFormLabel friendlyAddressLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(friendlyAddressLabelPk);
        StandardEntryFormLabel approveRequestLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(approveRequestLabelPk);

        /*
         * associate them
         */
        nameLabel.associateStandardFormField(nameField);
        descriptionLabel.associateStandardFormField(descriptionField);
        authorLabel.associateStandardFormField(authorField);
        fileLabel.associateStandardFormField(fileField);
        approveRequestLabel.associateStandardFormField(approveRequestField);
        parentArticleLabel.associateStandardFormField(parentArticleField);
        classificationTypeLabel
                .associateStandardFormField(classificationTypeField);
        ruleTypeLabel.associateStandardFormField(ruleTypeField);
        friendlyAddressLabel.associateStandardFormField(friendlyAddressField);
        versionNotesLabel.associateStandardFormField(versionNotesField);

        /*
         * change the default alignments
         */
        approveRequestField.setAlign(currentLocale.getRightAlign());

        /*
         * set the mandatory objects. fileField is not mandatory, its absence
         * will be handled in validateExtra
         */
        nameLabel.setAsMandatory();
        nameField.setAsMandatory();
        descriptionLabel.setAsMandatory();
        descriptionField.setAsMandatory();
        parentArticleLabel.setAsMandatory();
        parentArticleField.setAsMandatory();
        authorLabel.setAsMandatory();
        authorField.setAsMandatory();
        classificationTypeLabel.setAsMandatory();
        classificationTypeField.setAsMandatory();
        ruleTypeLabel.setAsMandatory();
        ruleTypeField.setAsMandatory();
        friendlyAddressLabel.setAsMandatory();
        friendlyAddressField.setAsMandatory();

        if (this.getAccessType().equals(AccessTypeConstants.NEW)) {
            fileLabel.setAsMandatory();
        }

        /*
         * fill with the default values. This default values will only be used
         * if the form is printed for the first time
         */
        if ((this.getAccessType().equals(AccessTypeConstants.VIEW))
                || (this.getAccessType().equals(AccessTypeConstants.EDIT))) {
            nameField.setFirstDefaultValue(this.image.getNameText());
            descriptionField.setFirstDefaultValue(this.image
                    .getDescriptionText());
            versionNotesField.setFirstDefaultValue(this.image.getComment());

            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(parentArticlePk);
            parentArticleField.setFirstDefaultValue(parentArticle
                    .getPrimaryKey().toString());
            parentArticleField.setFirstDefaultValueHTML(parentArticle
                    .getPathHTMLWithLink());
            friendlyAddressField.setFirstDefaultValue(this.image
                    .getFriendlyAddress());

            StandardAccount authorStandardAccount = this.image
                    .getAuthorStandardAccount();
            authorField.setFirstDefaultValue(authorStandardAccount
                    .getFullNameAndLoginText());
            authorField.setFirstDefaultValueHTML(authorStandardAccount
                    .getFullNameAndLoginHTMLWithLink());

            StandardContentClassificationType classificationType = (StandardContentClassificationType) srctf
                    .findByPrimaryKey(this.image
                            .getClassificationTypePrimaryKey());
            StandardContentRuleType ruleType = (StandardContentRuleType) srrtf
                    .findByPrimaryKey(this.image.getRuleTypePrimaryKey());

            /*
             * change the type of fields when editable
             */
            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    parentArticlePk)) {
                /*
                 * change the parent article field
                 */
                List<PrimaryKey> articles = hm
                        .getToppedHistoryWithArticlesOnly(parentArticlePk);
                if (!(articles.contains(homePk))) {
                    articles.add(homePk);
                }
                ArrayList<String> parentArticleValues = new ArrayList<String>();
                ArrayList<String> parentArticleNames = new ArrayList<String>();

                for (int i = 0; i < articles.size(); i++) {
                    PrimaryKey currentArticlePk = (PrimaryKey) articles.get(i);
                    StandardArticle currentArticle = (StandardArticle) sdf
                            .findByPrimaryKey(currentArticlePk);
                    parentArticleValues.add(currentArticlePk.toString());
                    parentArticleNames.add(currentArticle.getPathText());
                }

                parentArticleField.setSelectType(parentArticleValues,
                        parentArticleNames);
                parentArticleField.setFirstDefaultValue(parentArticlePk
                        .toString());

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
                    classificationTypeValues.add(currentClassificationType
                            .getPrimaryKey().toString());
                    classificationTypeNames.add(currentClassificationType
                            .getNameText());
                }

                classificationTypeField.setSelectType(classificationTypeValues,
                        classificationTypeNames);
                classificationTypeField.setFirstDefaultValue(classificationType
                        .getPrimaryKey().toString());

                /*
                 * change the rule field
                 */
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
                ruleTypeField.setFirstDefaultValue(ruleType.getPrimaryKey()
                        .toString());
            } else {
                authorField.setPrintOnlyType();
                parentArticleField.setPrintOnlyType();
                classificationTypeField.setPrintOnlyType();
                ruleTypeField.setPrintOnlyType();

                /*
                 * add the classification type
                 */
                PrimaryKey contentClassificationTypePk = this.image
                        .getClassificationTypePrimaryKey();
                StandardContentClassificationType contentClassificationType = (StandardContentClassificationType) srctf
                        .findByPrimaryKey(contentClassificationTypePk);
                String contentClassificationTypeNameText = contentClassificationType
                        .getNameText();
                String contentClassificationTypeNameHTML = contentClassificationType
                        .getNameHTML();

                Searched contentClassificationSearched = this.image
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
                PrimaryKey contentRuleTypePk = this.image
                        .getRuleTypePrimaryKey();
                StandardContentRuleType contentRuleType = (StandardContentRuleType) srrtf
                        .findByPrimaryKey(contentRuleTypePk);
                String contentRuleTypeNameText = contentRuleType.getNameText();
                String contentRuleTypeNameHTML = contentRuleType.getNameHTML();

                Searched contentRuleSearched = this.image.getRuleSearched();
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
        } else {
            authorField.setFirstDefaultValue(this.author);
            friendlyAddressField.setFirstDefaultValue(this.friendlyAddress);

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
                ruleTypeValues.add(currentRuleType.getPrimaryKey().toString());
                ruleTypeNames.add(currentRuleType.getNameText());
            }

            ruleTypeField.setSelectType(ruleTypeValues, ruleTypeNames);
            ruleTypeField.setFirstDefaultValue(this.ruleType);
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

        if ((this.getAccessType().equals(AccessTypeConstants.VIEW))
                || (this.getAccessType().equals(AccessTypeConstants.EDIT))) {
            this.addStandardFormObject(parentArticleLabel, 1, ++lineNumber);
            this.addStandardFormObject(parentArticleField, 2, lineNumber);
        }

        this.addStandardFormObject(authorLabel, 1, ++lineNumber);
        this.addStandardFormObject(authorField, 2, lineNumber);
        this.addStandardFormObject(fileLabel, 1, ++lineNumber);
        this.addStandardFormObject(fileField, 2, lineNumber);
        this.addStandardFormObject(versionNotesLabel, 1, ++lineNumber);
        this.addStandardFormObject(versionNotesField, 2, lineNumber);
        this.addStandardFormObject(friendlyAddressLabel, 1, ++lineNumber);
        this.addStandardFormObject(friendlyAddressField, 2, lineNumber);
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
                    .getAlphanumericSingleKey(ButtonsDictionary.SEND_UPDATE_IMAGE_REQUEST);
            StandardEntryFormButton sendUpdateArticleRequest = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(sendUpdateArticleRequestPk);
            this.addStandardFormObject(sendUpdateArticleRequest, 1, 1);
        } else {
            PrimaryKey sendAddRequestPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.SEND_ADD_REQUEST);
            StandardEntryFormButton sendAddRequest = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(sendAddRequestPk);
            this.addStandardFormObject(sendAddRequest, 1, 1);
        }

        if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                parentArticlePk)) {
            this.addStandardFormObject(approveRequestField, 2, 1);
            this.addStandardFormObject(approveRequestLabel, 3, 1);
        }

        /*
         * extract the data entries from the form for validation
         */
        this.setContentName(nameField.getRequestValue());
        this.setContentDescription(descriptionField.getRequestValue());
        this.requestedParentArticleId = parentArticleField.getRequestValue();
        this.author = authorField.getRequestValue();
        this.classificationType = classificationTypeField.getRequestValue();
        this.ruleType = ruleTypeField.getRequestValue();
        this.versionNotes = versionNotesField.getRequestValue();
        this.approveRequest = approveRequestField.getRequestValue();
        this.friendlyAddress = ContentUtil
                .getCleanFriendlyAddress(friendlyAddressField.getRequestValue());
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
        String parentArticleId = rm
                .getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey parentArticlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(parentArticleId);
        StandardArticle sd = (StandardArticle) sdf
                .findByPrimaryKey(parentArticlePk);

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
     * 
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
        if (this.getStandardImage() != null) {
            if (!(resm.getIsRequestAuthorized(sa.getPrimaryKey(), this
                    .getStandardImage().getPrimaryKey()))) {
                this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER);

                PrimaryKey cannotRequestImageEditPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_REQUEST_IMAGE_EDIT);
                StandardFormMessage cannotRequestImageEditMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(cannotRequestImageEditPk);
                this.addStandardFormObject(cannotRequestImageEditMessage, 1, 1);
            } else {
                this.build();
            }
        } else {
            this.buildNoRecordFound();
        }
    }

    /**
     * Builds the layout of the form. This procedure is used when creating a
     * image summary.
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
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardStoredFileFactory ssff = (StandardStoredFileFactory) pfs
                .getStandardBeanFactory(DefaultStandardStoredFileFactory.class);
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
        this.setMaxSizes(2, 15, CanvasTypeConstants.CENTER);

        /*
         * get the primary keys of the required instances
         */
        PrimaryKey entityPk = entity.getPrimaryKey();
        PrimaryKey imageNameFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
        PrimaryKey imageDescriptionFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DESCRIPTION);
        PrimaryKey imageVersionFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.IMAGE_VERSION);
        PrimaryKey contentTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_TYPE);
        PrimaryKey imageSizeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.IMAGE_SIZE);
        PrimaryKey imageFileFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FILE);
        PrimaryKey imageAddressFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ADDRESS);
        PrimaryKey imageAuthorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
        PrimaryKey imageParentArticleFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PARENT_ARTICLE);
        PrimaryKey imageClassificationTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CLASSIFICATION);
        PrimaryKey imageRuleTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_RULE);
        PrimaryKey imageCreatedFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CREATED);
        PrimaryKey imageModifiedFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_MODIFIED);
        PrimaryKey imageVersionNotesFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_VERSION_NOTES);
        PrimaryKey imageFriendlyAddressFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FRIENDLY_ADDRESS);

        PrimaryKey imageNameLabelPk = seflf.getPrimaryKey(entityPk,
                imageNameFieldPk);
        PrimaryKey imageDescriptionLabelPk = seflf.getPrimaryKey(entityPk,
                imageDescriptionFieldPk);
        PrimaryKey imageVersionLabelPk = seflf.getPrimaryKey(entityPk,
                imageVersionFieldPk);
        PrimaryKey contentTypeLabelPk = seflf.getPrimaryKey(entityPk,
                contentTypeFieldPk);
        PrimaryKey imageSizeLabelPk = seflf.getPrimaryKey(entityPk,
                imageSizeFieldPk);
        PrimaryKey imageFileLabelPk = seflf.getPrimaryKey(entityPk,
                imageFileFieldPk);
        PrimaryKey imageAddressLabelPk = seflf.getPrimaryKey(entityPk,
                imageAddressFieldPk);
        PrimaryKey imageAuthorLabelPk = seflf.getPrimaryKey(entityPk,
                imageAuthorFieldPk);
        PrimaryKey imageParentArticleLabelPk = seflf.getPrimaryKey(entityPk,
                imageParentArticleFieldPk);
        PrimaryKey imageClassificationTypeLabelPk = seflf.getPrimaryKey(
                entityPk, imageClassificationTypeFieldPk);
        PrimaryKey imageRuleTypeLabelPk = seflf.getPrimaryKey(entityPk,
                imageRuleTypeFieldPk);
        PrimaryKey imageCreatedLabelPk = seflf.getPrimaryKey(entityPk,
                imageCreatedFieldPk);
        PrimaryKey imageModifiedLabelPk = seflf.getPrimaryKey(entityPk,
                imageModifiedFieldPk);
        PrimaryKey imageVersionNotesLabelPk = seflf.getPrimaryKey(entityPk,
                imageVersionNotesFieldPk);
        PrimaryKey imageFriendlyAddressLabelPk = seflf.getPrimaryKey(entityPk,
                imageFriendlyAddressFieldPk);

        /*
         * instantiate all entry fields
         */
        StandardEntryFormField imageNameField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageNameFieldPk);
        StandardEntryFormField imageDescriptionField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageDescriptionFieldPk);
        StandardEntryFormField imageVersionField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageVersionFieldPk);
        StandardEntryFormField contentTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentTypeFieldPk);
        StandardEntryFormField imageSizeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageSizeFieldPk);
        StandardEntryFormField imageFileField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageFileFieldPk);
        StandardEntryFormField imageAddressField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageAddressFieldPk);
        StandardEntryFormField imageAuthorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageAuthorFieldPk);
        StandardEntryFormField imageParentArticleField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageParentArticleFieldPk);
        StandardEntryFormField imageClassificationTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageClassificationTypeFieldPk);
        StandardEntryFormField imageRuleTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageRuleTypeFieldPk);
        StandardEntryFormField imageCreatedField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageCreatedFieldPk);
        StandardEntryFormField imageModifiedField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageModifiedFieldPk);
        StandardEntryFormField imageVersionNotesField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageVersionNotesFieldPk);
        StandardEntryFormField imageFriendlyAddressField = (StandardEntryFormField) sefff
                .findByPrimaryKey(imageFriendlyAddressFieldPk);

        /*
         * instantiate all entry labels
         */
        StandardFormLabel imageNameLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageNameLabelPk);
        StandardEntryFormLabel imageDescriptionLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageDescriptionLabelPk);
        StandardEntryFormLabel imageVersionLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageVersionLabelPk);
        StandardEntryFormLabel contentTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentTypeLabelPk);
        StandardFormLabel imageSizeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageSizeLabelPk);
        StandardEntryFormLabel imageFileLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageFileLabelPk);
        StandardEntryFormLabel imageAddressLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageAddressLabelPk);
        StandardEntryFormLabel imageAuthorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageAuthorLabelPk);
        StandardEntryFormLabel imageParentArticleLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageParentArticleLabelPk);
        StandardEntryFormLabel imageClassificationTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageClassificationTypeLabelPk);
        StandardEntryFormLabel imageRuleTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageRuleTypeLabelPk);
        StandardEntryFormLabel imageCreatedLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageCreatedLabelPk);
        StandardEntryFormLabel imageModifiedLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageModifiedLabelPk);
        StandardEntryFormLabel imageVersionNotesLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageVersionNotesLabelPk);
        StandardEntryFormLabel imageFriendlyAddressLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(imageFriendlyAddressLabelPk);

        /*
         * associate them
         */
        imageNameLabel.associateStandardFormField(imageNameField);
        imageDescriptionLabel.associateStandardFormField(imageDescriptionField);
        imageVersionLabel.associateStandardFormField(imageVersionField);
        contentTypeLabel.associateStandardFormField(contentTypeField);
        imageSizeLabel.associateStandardFormField(imageSizeField);
        imageFileLabel.associateStandardFormField(imageFileField);
        imageAuthorLabel.associateStandardFormField(imageAuthorField);
        imageAddressLabel.associateStandardFormField(imageAddressField);
        imageParentArticleLabel
                .associateStandardFormField(imageParentArticleField);
        imageClassificationTypeLabel
                .associateStandardFormField(imageClassificationTypeField);
        imageRuleTypeLabel.associateStandardFormField(imageRuleTypeField);
        imageCreatedLabel.associateStandardFormField(imageCreatedField);
        imageModifiedLabel.associateStandardFormField(imageModifiedField);
        imageVersionNotesLabel
                .associateStandardFormField(imageVersionNotesField);
        imageFriendlyAddressLabel
                .associateStandardFormField(imageFriendlyAddressField);

        /*
         * set the appropriate fields in print only mode
         */
        imageNameField.setPrintOnlyType();
        imageDescriptionField.setPrintOnlyType();
        imageVersionField.setPrintOnlyType();
        contentTypeField.setPrintOnlyType();
        imageSizeField.setPrintOnlyType();
        imageFileField.setPrintOnlyType();
        imageAuthorField.setPrintOnlyType();
        imageAddressField.setPrintOnlyType();
        imageParentArticleField.setPrintOnlyType();
        imageClassificationTypeField.setPrintOnlyType();
        imageRuleTypeField.setPrintOnlyType();
        imageCreatedField.setPrintOnlyType();
        imageModifiedField.setPrintOnlyType();
        imageVersionNotesField.setPrintOnlyType();
        imageFriendlyAddressField.setPrintOnlyType();

        /*
         * get the parent article
         */
        StandardArticle parentArticle = (StandardArticle) sdf
                .findByPrimaryKey(this.image.getMainParentPrimaryKey());

        /*
         * get the file
         */
        StandardStoredFile storedFile = (StandardStoredFile) ssff
                .findByPrimaryKey(this.image.getFilePrimaryKey());

        /*
         * fill with the default values.
         */
        imageNameField.setFirstDefaultValue(this.image.getNameText());
        imageDescriptionField.setFirstDefaultValue(this.image
                .getLongDescriptionText());
        imageVersionNotesField.setFirstDefaultValue(this.image.getComment());
        imageVersionField.setFirstDefaultValue(String.valueOf(this.image
                .getVersionNum()));
        imageSizeField.setFirstDefaultValue(FileUtil
                .getFormattedSizeText(storedFile.getSize()));
        imageFileField.setFirstDefaultValue(storedFile.getName());
        imageFileField.setFirstDefaultValueHTML(lw.getBreakableLinkHTML(
                this.image.getDefaultLocation(), null, storedFile.getName(),
                null));
        imageFriendlyAddressField.setFirstDefaultValue(this.image
                .getFriendlyAddress());

        /*
         * add the content type
         */
        PrimaryKey contentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);
        StandardContentType contentType = (StandardContentType) srtf
                .findByPrimaryKey(contentTypePk);
        contentTypeField.setFirstDefaultValue(contentType.getNameText());

        /*
         * add the author
         */
        StandardAccount authorStandardAccount = this.image
                .getAuthorStandardAccount();

        if (authorStandardAccount.getIsFound()) {
            imageAuthorField.setFirstDefaultValue(authorStandardAccount
                    .getFullNameAndLoginText());
            imageAuthorField.setFirstDefaultValueHTML(authorStandardAccount
                    .getFullNameAndLoginHTMLWithLink());
        } else {
            imageAuthorField.setFirstDefaultValue("");
        }

        /*
         * add the classification type
         */
        PrimaryKey contentClassificationTypePk = this.image
                .getClassificationTypePrimaryKey();
        StandardContentClassificationType contentClassificationType = (StandardContentClassificationType) srctf
                .findByPrimaryKey(contentClassificationTypePk);
        String contentClassificationTypeNameText = contentClassificationType
                .getNameText();

        Searched contentClassificationSearched = this.image
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

        imageClassificationTypeField
                .setFirstDefaultValue(contentClassificationTypeNameText);

        /*
         * add the rule type
         */
        PrimaryKey contentRuleTypePk = this.image.getRuleTypePrimaryKey();
        StandardContentRuleType contentRuleType = (StandardContentRuleType) srrtf
                .findByPrimaryKey(contentRuleTypePk);
        String contentRuleTypeNameText = contentRuleType.getNameText();

        Searched contentRuleSearched = this.image.getRuleSearched();
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

        imageRuleTypeField.setFirstDefaultValue(contentRuleTypeNameText);

        /*
         * add the other fields
         */
        imageAddressField
                .setFirstDefaultValue(this.image.getAbsoluteLocation());
        imageParentArticleField.setFirstDefaultValue(parentArticle
                .getPrimaryKey().toString());
        imageParentArticleField.setFirstDefaultValueHTML(parentArticle
                .getPathHTMLWithLink());
        imageCreatedField.setFirstDefaultValue(DateFormatUtil
                .getLongFormattedDateText(this.image
                        .getFirstEntryLogTimestamp()));
        imageModifiedField
                .setFirstDefaultValue(DateFormatUtil
                        .getLongFormattedDateText(this.image
                                .getLastEntryLogTimestamp()));

        /*
         * get the first request
         */
        StandardContentRequest firstContentRequest = null;

        if (this.image.getFirstRequestPrimaryKey() != null) {
            firstContentRequest = (StandardContentRequest) srrf
                    .findByPrimaryKey(this.image.getFirstRequestPrimaryKey());

            if (firstContentRequest.getIsFound()) {
                imageCreatedField.setFirstDefaultValueHTML(lw
                        .getBreakableLinkHTML(firstContentRequest
                                .getDefaultLocation(), null, DateFormatUtil
                                .getLongFormattedDateText(this.image
                                        .getFirstEntryLogTimestamp()), null));
            }
        }

        /*
         * get the last request
         */
        StandardContentRequest lastContentRequest = null;

        if (this.image.getLastRequestPrimaryKey() != null) {
            lastContentRequest = (StandardContentRequest) srrf
                    .findByPrimaryKey(this.image.getLastRequestPrimaryKey());

            if (lastContentRequest.getIsFound()) {
                imageModifiedField.setFirstDefaultValueHTML(lw
                        .getBreakableLinkHTML(lastContentRequest
                                .getDefaultLocation(), null, DateFormatUtil
                                .getLongFormattedDateText(this.image
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
        this.addStandardFormObject(imageNameLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageNameField, 2, lineNumber);
        this.addStandardFormObject(imageDescriptionLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageDescriptionField, 2, lineNumber);
        this.addStandardFormObject(contentTypeLabel, 1, ++lineNumber);
        this.addStandardFormObject(contentTypeField, 2, lineNumber);
        this.addStandardFormObject(imageParentArticleLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageParentArticleField, 2, lineNumber);
        this.addStandardFormObject(imageAuthorLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageAuthorField, 2, lineNumber);
        this.addStandardFormObject(imageVersionLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageVersionField, 2, lineNumber);
        this.addStandardFormObject(imageSizeLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageSizeField, 2, lineNumber);
        this.addStandardFormObject(imageFileLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageFileField, 2, lineNumber);
        this.addStandardFormObject(imageVersionNotesLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageVersionNotesField, 2, lineNumber);
        this.addStandardFormObject(imageFriendlyAddressLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageFriendlyAddressField, 2, lineNumber);
        this.addStandardFormObject(imageAddressLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageAddressField, 2, lineNumber);
        this.addStandardFormObject(imageClassificationTypeLabel, 1,
                ++lineNumber);
        this.addStandardFormObject(imageClassificationTypeField, 2, lineNumber);
        this.addStandardFormObject(imageRuleTypeLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageRuleTypeField, 2, lineNumber);
        this.addStandardFormObject(imageCreatedLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageCreatedField, 2, lineNumber);
        this.addStandardFormObject(imageModifiedLabel, 1, ++lineNumber);
        this.addStandardFormObject(imageModifiedField, 2, lineNumber);
    }

    /**
     * Builds the layout of the form for a record summary to view.
     * 
     * 
     * 
     */
    protected void buildSummaryRecord() {
        if (this.getStandardImage() != null) {
            this.buildSummary();
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
    public void validateExtraEditRecord() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardImageFactory sif = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
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
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);
        AccountHistoryManager hm = (AccountHistoryManager) pms
                .getManager(DefaultAccountHistoryManager.class);
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
        PrimaryKey contentPrimaryKey = this.image.getPrimaryKey();
        StandardContent content = (StandardContent) srf
                .findByPrimaryKey(contentPrimaryKey);

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
        List<PrimaryKey> accounts = null;

        if (this.getIsValid()) {
            /*
             * check the author
             */
            accounts = saf.findEnabledPrimaryKeysByNameOrLogin(this.author);

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
                PrimaryKey accountPk = (PrimaryKey) accounts.get(0);
                hm.add(accountPk);
            }
        }

        /*
         * find the approver
         */
        StandardArticle requestedParentArticle = null;
        PrimaryKey requestedParentArticlePk = null;
        StandardArticle currentParentArticle = null;
        StandardAccount approver = null;

        if (this.getIsValid()) {
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

            /*
             * get the approver from the current parent article
             */
            approver = currentParentArticle.getEditorStandardAccount();

            if (resm.getIsRequestRequirementWaived(requestor.getPrimaryKey(),
                    currentParentArticle.getPrimaryKey())) {
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
        if (this.getIsValid()) {
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
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_NOT_IMMEDIATE_IMAGE_REQUEST);
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
         * check whether the friendly address is already in use
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.friendlyAddress))) {
                StandardImage friendlyImage = sif
                        .findByFriendlyAddress(this.friendlyAddress);

                if (friendlyImage.getIsFound()) {
                    this.setIsValid(friendlyImage.equals(this.image));

                    if (!(friendlyImage.equals(this.image))) {
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
         * get the primary key of the file that was uploaded
         */
        PrimaryKey filePk = null;

        if (this.getIsValid()) {
            List<PrimaryKey> storedFilesPrimaryKeys = rm
                    .getStoredFilesPrimaryKeys();

            if (storedFilesPrimaryKeys.size() == 1) {
                filePk = (PrimaryKey) storedFilesPrimaryKeys.get(0);
            } else {
                filePk = this.image.getFilePrimaryKey();
            }
        }

        /*
         * save the friendly address
         */
        String oldFriendlyAddress = this.image.getFriendlyAddress();

        /*
         * try to create the content request
         */
        StandardContentRequest contentRequest = null;

        if (this.getIsValid()) {
            PrimaryKey authorPk = (PrimaryKey) accounts.get(0);
            PrimaryKey classificationTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.classificationType);
            PrimaryKey ruleTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.ruleType);

            contentRequest = (StandardContentRequest) srrf
                    .createImageUpdateRequest(this.image.getPrimaryKey(), this
                            .getContentName(), this.getContentDescription(),
                            authorPk, this.friendlyAddress, filePk,
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
                contentRequest.approveImageRequest();
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

        if (this.getIsValid()) {
            if (StringUtil.getIsEmpty(this.approveRequest)) {
                PrimaryKey subjectMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_UPDATE_TO_APPROVE_SUBJECT);
                StandardMessage subjectMessage = (StandardMessage) smf
                        .findByPrimaryKey(subjectMessagePk);

                PrimaryKey bodyMessagePk = null;

                if (StringUtil.getIsEmpty(this.versionNotes)) {
                    bodyMessagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_UPDATE_TO_APPROVE_BODY);
                } else {
                    bodyMessagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_WITH_NOTES_UPDATE_TO_APPROVE_BODY);
                }

                StandardMessage bodyMessage = (StandardMessage) smf
                        .findByPrimaryKey(bodyMessagePk);

                String companyNameText = prop.getCompanyNameText();
                String applicationNameText = prop.getApplicationNameText();
                String imageNameText = this.image.getNameText();
                String imageDescriptionText = this.image.getDescriptionText();
                String articleNameText = currentParentArticle.getNameText();
                String requestorNameText = requestor.getFullNameText();
                String requestLocationText = contentRequest
                        .getAbsoluteLocation();
                String versionNotesText = this.versionNotes;

                subjectMessage.replaceMessageText(imageNameText, 1);

                bodyMessage.replaceMessageText(companyNameText, 1);
                bodyMessage.replaceMessageText(applicationNameText, 2);
                bodyMessage.replaceMessageText(requestorNameText, 3);
                bodyMessage.replaceMessageText(imageNameText, 4);
                bodyMessage.replaceMessageText(articleNameText, 5);
                bodyMessage.replaceMessageText(imageDescriptionText, 6);
                bodyMessage.replaceMessageText(requestLocationText, 7);
                bodyMessage.replaceMessageText(versionNotesText, 8);

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

                if (!(resm.getIsAccessGroupMissing(this.image))) {
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_IMAGE_UPDATED);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);
                } else {
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_IMAGE_UPDATED_REVIEW_ACCESS_GROUPS);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);
                }

                /*
                 * add the name of the article to the message
                 */
                validation.replaceMessageText(this.image.getNameText(), 1);
                validation
                        .replaceMessageEncoded(this.image.getNameEncoded(), 1);
                validation.replaceMessageHTML(this.image.getNameHTML(), 1);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            } else {
                /*
                 * get the validation message
                 */
                PrimaryKey validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_IMAGE_UPDATE_REQUEST_SENT);
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
            this.image.load();
        }

        /*
         * print the friendly address info
         */
        if (this.getIsValid()) {
            if (this.approveRequest.equals("Y")) {
                /*
                 * save the friendly address
                 */
                String newFriendlyAddress = this.image.getFriendlyAddress();

                if (!(StringUtil.getIsIdentical(oldFriendlyAddress,
                        newFriendlyAddress))) {
                    /*
                     * get the validation message
                     */
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.INF_IMAGE_FRIENDLY_ADDRESS);
                    StandardMessage validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * get the new location
                     */
                    String newLocationText = this.image.getAbsoluteLocation();
                    String newLocationEncoded = TextFormatUtil
                            .getTextToEncoded(newLocationText);
                    String newLocationHTML = TextFormatUtil
                            .getTextToHTML(newLocationText);

                    /*
                     * add the name of the image to the message
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
                List<PrimaryKey> storedFilesPrimaryKeys = rm
                        .getStoredFilesPrimaryKeys();

                if (storedFilesPrimaryKeys.size() == 1) {
                    ContentUtil.printSubscriptionMessage(content
                            .getPrimaryKey(), content.getTypePrimaryKey());
                }
            }
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect the user to the parent article page. otherwise,
         * rollback the current transaction
         */
        if (this.getIsValid()) {
            /*
             * redirect
             */
            if (this.approveRequest.equals("Y")) {
                /*
                 * redirect to the parent article page for the new image
                 */
                String redirectURL = requestedParentArticle
                        .getAbsoluteLocation();
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
            } else {
                /*
                 * redirect to the parent article page for the new image
                 */
                String redirectURL = requestedParentArticle
                        .getAbsoluteLocation();
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
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
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardImageFactory sif = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
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
        String parentArticleId = rm
                .getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey parentArticlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(parentArticleId);

        /*
         * get the parent article
         */
        StandardArticle parentArticle = (StandardArticle) sdf
                .findByPrimaryKey(parentArticlePk);

        /*
         * get the requestor
         */
        StandardAccount requestor = am.getProxyStandardAccount();

        /*
         * get the approver
         */
        StandardAccount approver = parentArticle.getEditorStandardAccount();

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
        List<PrimaryKey> accounts = null;

        if (this.getIsValid()) {
            /*
             * check the author
             */
            accounts = saf.findEnabledPrimaryKeysByNameOrLogin(this.author);

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
                PrimaryKey accountPk = (PrimaryKey) accounts.get(0);
                hm.add(accountPk);
            }
        }

        /*
         * check whether the friendly address is already in use
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.friendlyAddress))) {
                StandardImage friendlyImage = sif
                        .findByFriendlyAddress(this.friendlyAddress);
                this.setIsValid(!(friendlyImage.getIsFound()));

                if (friendlyImage.getIsFound()) {
                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_FRIENDLY_ADDRESS_ALREADY_USED);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();
                }
            }
        }

        /*
         * verify a file was uploaded
         */
        PrimaryKey filePk = null;

        if (this.getIsValid()) {
            List<PrimaryKey> storedFilesPrimaryKeys = rm
                    .getStoredFilesPrimaryKeys();
            this.setIsValid(storedFilesPrimaryKeys.size() == 1);

            if (storedFilesPrimaryKeys.size() != 1) {
                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_FILE_ENTERED);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();

                PrimaryKey fileFieldPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FILE);
                StandardEntryFormField fileField = (StandardEntryFormField) sefff
                        .findByPrimaryKey(fileFieldPk);
                fileField.setFocusIfFirst(this.getFormName());
            } else {
                filePk = (PrimaryKey) storedFilesPrimaryKeys.get(0);
            }
        }

        /*
         * try to create the content request
         */
        StandardContentRequest contentRequest = null;

        if (this.getIsValid()) {
            PrimaryKey authorPk = (PrimaryKey) accounts.get(0);
            PrimaryKey classificationTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.classificationType);
            PrimaryKey ruleTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.ruleType);

            contentRequest = (StandardContentRequest) srrf
                    .createNewImageRequest(this.getContentName(), this
                            .getContentDescription(), authorPk,
                            this.friendlyAddress, filePk, parentArticlePk,
                            classificationTypePk, requestor.getPrimaryKey(),
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
        StandardImage newImage = null;

        if (this.getIsValid()) {
            if (this.approveRequest.equals("Y")) {
                newImage = contentRequest.approveImageRequest();
                this.setIsValid(contentRequest.getIsDone());

                if ((newImage != null) && (!(newImage.getIsDone()))) {
                    MessageUtil.printBufferInternalErrorHTML(
                            ABSTRACT_CLASS_NAME, newImage.getStoreTrace());
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
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_IMAGE_TO_APPROVE_SUBJECT);
                StandardMessage subjectMessage = (StandardMessage) smf
                        .findByPrimaryKey(subjectMessagePk);

                PrimaryKey bodyMessagePk = null;

                if (StringUtil.getIsEmpty(this.versionNotes)) {
                    bodyMessagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_IMAGE_TO_APPROVE_BODY);
                } else {
                    bodyMessagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_IMAGE_WITH_NOTES_TO_APPROVE_BODY);
                }

                StandardMessage bodyMessage = (StandardMessage) smf
                        .findByPrimaryKey(bodyMessagePk);

                String companyNameText = prop.getCompanyNameText();
                String applicationNameText = prop.getApplicationNameText();
                String imageNameText = contentRequest.getNameText();
                String imageDescriptionText = contentRequest
                        .getDescriptionText();
                String articleNameText = parentArticle.getNameText();
                String requestorNameText = requestor.getFullNameText();
                String requestLocationText = contentRequest
                        .getAbsoluteLocation();
                String versionNotesText = this.versionNotes;

                subjectMessage.replaceMessageText(imageNameText, 1);

                bodyMessage.replaceMessageText(companyNameText, 1);
                bodyMessage.replaceMessageText(applicationNameText, 2);
                bodyMessage.replaceMessageText(requestorNameText, 3);
                bodyMessage.replaceMessageText(imageNameText, 4);
                bodyMessage.replaceMessageText(articleNameText, 5);
                bodyMessage.replaceMessageText(imageDescriptionText, 6);
                bodyMessage.replaceMessageText(requestLocationText, 7);
                bodyMessage.replaceMessageText(versionNotesText, 8);

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

                if (!(resm.getIsAccessGroupMissing(newImage))) {
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_NEW_IMAGE_CREATED);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);
                } else {
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_NEW_IMAGE_CREATED_ADD_ACCESS_GROUPS);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);
                }

                /*
                 * add the name of the image to the message
                 */
                validation.replaceMessageText(newImage.getNameText(), 1);
                validation.replaceMessageEncoded(newImage.getNameEncoded(), 1);
                validation.replaceMessageHTML(newImage.getNameHTML(), 1);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            } else {
                /*
                 * get the validation message
                 */
                PrimaryKey validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_NEW_IMAGE_REQUEST_SENT);
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
                        .getAlphanumericSingleKey(MessagesDictionary.INF_IMAGE_FRIENDLY_ADDRESS);
                StandardMessage validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * get the new location
                 */
                String newLocationText = newImage.getAbsoluteLocation();
                String newLocationEncoded = TextFormatUtil
                        .getTextToEncoded(newLocationText);
                String newLocationHTML = TextFormatUtil
                        .getTextToHTML(newLocationText);

                /*
                 * add the name of the image to the message
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
         * buffer and redirect the user to the parent article page. otherwise,
         * rollback the current transaction
         */
        if (this.getIsValid()) {
            /*
             * redirect
             */
            if (this.approveRequest.equals("Y")) {
                /*
                 * redirect to the parent article page for the new image
                 */
                if (!(resm.getIsAccessGroupMissing(newImage))) {
                    String redirectURL = parentArticle.getAbsoluteLocation();
                    redirectURL = HTTPUtil
                            .getAddedParameterURL(
                                    redirectURL,
                                    com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                    BlocksDictionary.ARTICLE_WHATS_RELATED);
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                } else {
                    /*
                     * get the "image edit" page
                     */
                    PrimaryKey editImagePagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(PagesDictionary.EDIT_IMAGE_PAGE);
                    StandardPage editImagePage = (StandardPage) spf
                            .findByPrimaryKey(editImagePagePk);

                    /*
                     * add the image id and the access groups block id
                     */
                    PrimaryKey newImagePk = newImage.getPrimaryKey();
                    String redirectURL = editImagePage.getAbsoluteLocation();
                    redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                            HTTPParameterConstants.IMAGE_ID, newImagePk
                                    .toString());
                    redirectURL = HTTPUtil
                            .getAddedParameterURL(
                                    redirectURL,
                                    com.corendal.netapps.framework.core.constants.HTTPParameterConstants.SELECTED_BLOCK_ID,
                                    BlocksDictionary.IMAGE_ACCESS_GROUPS);

                    /*
                     * do the redirect
                     */
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                }
            } else {
                /*
                 * redirect to the parent article page for the new image
                 */
                String redirectURL = parentArticle.getAbsoluteLocation();
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
            }
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }
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
     * Sets the default approve request to use when this form is printed for the
     * first time.
     * 
     * @param approveRequest
     *            approve request to set
     */
    public void setFirstTimeApproveRequest(String approveRequest) {
        this.approveRequest = approveRequest;
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
     * Sets the image viewed by this form.
     * 
     * @param sd
     *            standard image viewed by this form
     * @see #getStandardImage
     */
    public void setStandardImage(StandardImage sd) {
        this.image = sd;
    }

    /**
     * Returns the image viewed by this form.
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardImage object
     * @see #setStandardImage
     */
    public StandardImage getStandardImage() {
        return this.image;
    }
}

// end AbstractImageEntryBlock
