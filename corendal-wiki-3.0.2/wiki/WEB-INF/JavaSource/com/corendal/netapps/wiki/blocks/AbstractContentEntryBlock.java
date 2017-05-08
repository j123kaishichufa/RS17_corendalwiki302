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
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.blocks.AbstractStandardEntryFormBlock;
import com.corendal.netapps.framework.core.constants.CanvasTypeConstants;
import com.corendal.netapps.framework.core.constants.LayoutTypeConstants;
import com.corendal.netapps.framework.core.dictionaries.IconsDictionary;
import com.corendal.netapps.framework.core.dictionaries.RolesDictionary;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormIconFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormMessageFactory;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.ConfigManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.LocaleManager;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
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
import com.corendal.netapps.framework.core.interfaces.StandardFormMessage;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLabel;
import com.corendal.netapps.framework.core.interfaces.StandardLocale;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.managers.DefaultLocaleManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardEmailFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;

/**
 * AbstractContentEntryBlock is the parent block common to all reference viewing
 * blocks.
 * 
 * @version $Id: AbstractContentEntryBlock.java,v 1.1 2005/09/06 21:25:27
 *          tdanard Exp $
 */
public abstract class AbstractContentEntryBlock extends
        AbstractStandardEntryFormBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractContentEntryBlock";

    /** Id of the content selected by the user */
    private String contentId;

    /** Ids of the contents selected by the user */
    private String[] contentIds;

    /** Approve request entered by the user */
    private String approveRequest;

    /** Article viewed through this form */
    private StandardArticle article;

    /**
     * Default class constructor this procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead
     */
    protected AbstractContentEntryBlock() {
        this.article = null;
    }

    /**
     * Returns a clone. Overrides AbstractStandardEntryFormBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentEntryBlock) super.clone();
    }

    /**
     * Builds the layout of the form. This procedure is used when removing a
     * content.
     * 
     * 
     * 
     */
    protected void buildRemoveRecord() {
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
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);
        StandardFormIconFactory sfif = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);
        StandardFormIconFactory sficonf = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

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

        /*
         * set the entity
         */
        PrimaryKey contentsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(contentsPk);
        this.setStandardEntity(entity);

        /*
         * get the current locale
         */
        StandardLocale currentLocale = lm.getPrimaryStandardLocale();

        /*
         * get the articles entity
         */
        PrimaryKey articlesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        StandardEntity articles = (StandardEntity) sef
                .findByPrimaryKey(articlesPk);

        /*
         * get the referenced in articles message
         */
        PrimaryKey referencedInArticlesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_REFERENCED_IN_ARTICLES);
        StandardMessage referencedInArticles = (StandardMessage) smf
                .findByPrimaryKey(referencedInArticlesPk);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * instantiate all icons
         */
        PrimaryKey spacerPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SPACER);
        StandardFormIcon spacer = (StandardFormIcon) sficonf
                .findByPrimaryKey(spacerPk);
        spacer.setSize(4, 1);

        /*
         * build the form
         */
        if (this.article != null) {
            /*
             * get the list of contents in the article
             */
            PrimaryKey articlePk = this.article.getPrimaryKey();
            List<StandardContent> contents = srf
                    .findEnabledByParentArticlePrimaryKey(articlePk);

            /*
             * check whether contents could be found
             */
            if ((this.article.getIsFound())
                    && (this.article.getIsVisible())
                    && (!(resm.getIsRequestAuthorized(sa.getPrimaryKey(),
                            this.article.getPrimaryKey())))) {
                this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                PrimaryKey cannotRequestArticleEditPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_REQUEST_ARTICLE_EDIT);
                StandardFormMessage cannotRequestArticleEditMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(cannotRequestArticleEditPk);
                this.addStandardFormObject(cannotRequestArticleEditMessage, 1,
                        1);
            } else if ((this.article.getIsFound())
                    && (this.article.getIsVisible()) && (contents.size() > 0)) {
                /*
                 * set the layout type to multiple to provide a tabbing where
                 * the submit button is the last object to reach
                 */
                this.setLayoutType(LayoutTypeConstants.BORDER);

                /*
                 * set the max sizes of the form
                 */
                this
                        .setMaxSizes(4, contents.size(),
                                CanvasTypeConstants.CENTER); // 4
                // columns,
                // ?
                // lines
                this.setMaxSizes(3, 1, CanvasTypeConstants.SOUTH); // 3

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

                PrimaryKey approveRequestFieldPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(FieldsDictionary.CONTENT_APPROVE_REQUEST);
                PrimaryKey approveRequestLabelPk = seflf.getPrimaryKey(
                        entityPk, approveRequestFieldPk);

                /*
                 * instantiate all entry fields
                 */
                StandardEntryFormField nameField = (StandardEntryFormField) sefff
                        .findByPrimaryKey(nameFieldPk);
                nameField.setCheckBoxType();

                StandardEntryFormField approveRequestField = (StandardEntryFormField) sefff
                        .findByPrimaryKey(approveRequestFieldPk);
                approveRequestField.setCheckBoxType();

                /*
                 * instantiate all entry labels
                 */
                StandardEntryFormLabel nameLabel = (StandardEntryFormLabel) seflf
                        .findByPrimaryKey(nameLabelPk);
                nameLabel.setIsNameBreakable(true);

                StandardEntryFormLabel approveRequestLabel = (StandardEntryFormLabel) seflf
                        .findByPrimaryKey(approveRequestLabelPk);

                /*
                 * associate them
                 */
                approveRequestLabel
                        .associateStandardFormField(approveRequestField);

                /*
                 * change the default alignments
                 */
                approveRequestField.setAlign(currentLocale.getRightAlign());

                /*
                 * fill with the default values. This default values will only
                 * be used if the form is printed for the first time
                 */
                approveRequestField.setFirstDefaultValue(this.approveRequest);

                /*
                 * add those objects
                 */
                this.setCurrentCanvasType(CanvasTypeConstants.CENTER);

                for (int i = 0; i < contents.size(); i++) {
                    /*
                     * get the content
                     */
                    StandardContent currentContent = (StandardContent) contents
                            .get(i);
                    PrimaryKey currentContentPk = currentContent
                            .getPrimaryKey();

                    /*
                     * verify that the content still exists
                     */
                    if ((currentContent.getIsFound())
                            && (currentContent.getIsVisible())) {
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
                         * clone the field and modify the field clone
                         */
                        StandardEntryFormField nameFieldClone = (StandardEntryFormField) nameField
                                .clone();
                        nameFieldClone.setOptionValueText(currentContentPk
                                .toString());

                        /*
                         * clone the label and modify the name of the label
                         */
                        StandardEntryFormLabel nameLabelClone = (StandardEntryFormLabel) nameLabel
                                .clone();
                        StandardLabel sl3 = nameLabelClone.getStandardLabel();

                        String currentNameText = currentContent.getNameText();
                        String currentNameEncoded = currentContent
                                .getNameEncoded();
                        String currentNameHTML = currentContent.getNameHTML();

                        /*
                         * add the number of references
                         */
                        if (currentContent.getIsDirectChildOf(articlePk)) {
                            List<StandardArticle> currentReferences = sdf
                                    .findEnabledByReferencedContentPrimaryKey(currentContent
                                            .getPrimaryKey());
                            int size = currentReferences.size();

                            if (size > 0) {
                                /*
                                 * get the count
                                 */
                                String articlesText = articles
                                        .getCountNameText(size);
                                String articlesEncoded = articles
                                        .getCountNameEncoded(size);
                                String articlesHTML = articles
                                        .getCountNameHTML(size);

                                /*
                                 * add this count to the message
                                 */
                                StandardMessage referenced = (StandardMessage) referencedInArticles
                                        .clone();
                                referenced.replaceMessageText(articlesText, 1);
                                referenced.replaceMessageEncoded(
                                        articlesEncoded, 1);
                                referenced.replaceMessageHTML(articlesHTML, 1);

                                /*
                                 * add this count to the name of the label
                                 */
                                currentNameText = ConcatUtil
                                        .getConcatWithParentheses(
                                                currentNameText,
                                                referenced
                                                        .getCurrentMessageText(),
                                                currentNameText,
                                                referenced
                                                        .getCurrentMessageText());
                                currentNameEncoded = ConcatUtil
                                        .getConcatWithParentheses(
                                                currentNameText,
                                                referenced
                                                        .getCurrentMessageEncoded(),
                                                currentNameEncoded,
                                                referenced
                                                        .getCurrentMessageEncoded());
                                currentNameHTML = ConcatUtil
                                        .getConcatWithParentheses(
                                                currentNameText,
                                                referenced
                                                        .getCurrentMessageHTML(),
                                                currentNameHTML,
                                                referenced
                                                        .getCurrentMessageHTML());
                            }
                        }

                        sl3.setCurrentNameText(currentNameText);
                        sl3.setCurrentNameEncoded(currentNameEncoded);
                        sl3.setCurrentNameHTML(currentNameHTML);

                        sl3.setCurrentShortDescriptionText(currentContent
                                .getShortDescriptionText());
                        sl3.setCurrentShortDescriptionEncoded(currentContent
                                .getShortDescriptionEncoded());
                        sl3.setCurrentShortDescriptionHTML(currentContent
                                .getShortDescriptionHTML());

                        /*
                         * associate the field with the label
                         */
                        nameLabelClone
                                .associateStandardFormField(nameFieldClone);

                        /*
                         * add these three objects
                         */
                        this.addStandardFormObject(nameFieldClone, 1, i + 1);
                        this.addStandardFormObject(previewFormIcon, 2, i + 1);
                        this.addStandardFormObject(spacer, 3, i + 1);
                        this.addStandardFormObject(nameLabelClone, 4, i + 1);
                    }
                }

                /*
                 * instantiate and add the submit button
                 */
                this.setCurrentCanvasType(CanvasTypeConstants.SOUTH);

                PrimaryKey sendAddRequestPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ButtonsDictionary.SEND_REMOVE_REQUEST);
                StandardEntryFormButton sendAddRequest = (StandardEntryFormButton) sefbf
                        .findByPrimaryKey(sendAddRequestPk);
                this.addStandardFormObject(sendAddRequest, 1, 1);

                if (resm.getIsRecursiveProxyOrAssociateEditor(sa
                        .getPrimaryKey(), articlePk)) {
                    this.addStandardFormObject(approveRequestField, 2, 1);
                    this.addStandardFormObject(approveRequestLabel, 3, 1);
                }

                /*
                 * extract the data entries from the form for validation
                 */
                this.contentIds = nameField.getRequestValues();

                this.approveRequest = approveRequestField.getRequestValue();
            } else {
                this.buildNoRecordFound();
            }
        } else {
            this.buildNoRecordFound();
        }
    }

    /**
     * Builds the layout of the form. This procedure is used when editing a
     * content.
     * 
     * 
     * 
     */
    protected void buildEditRecord() {
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
        StandardFormIconFactory sficonf = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * set the entity
         */
        PrimaryKey contentsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(contentsPk);
        this.setStandardEntity(entity);

        /*
         * instantiate all icons
         */
        PrimaryKey spacerPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SPACER);
        StandardFormIcon spacer = (StandardFormIcon) sficonf
                .findByPrimaryKey(spacerPk);
        spacer.setSize(4, 1);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * build the form
         */
        if (this.article != null) {
            /*
             * get the list of contents in the article
             */
            PrimaryKey articlePk = this.article.getPrimaryKey();
            List<StandardContent> contents = srf
                    .findEnabledByParentArticlePrimaryKey(articlePk);

            /*
             * check whether contents could be found
             */
            if ((this.article.getIsFound())
                    && (this.article.getIsVisible())
                    && (!(resm.getIsRequestAuthorized(sa.getPrimaryKey(),
                            this.article.getPrimaryKey())))) {
                this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER); // 1

                PrimaryKey cannotRequestArticleEditPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_REQUEST_ARTICLE_EDIT);
                StandardFormMessage cannotRequestArticleEditMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(cannotRequestArticleEditPk);
                this.addStandardFormObject(cannotRequestArticleEditMessage, 1,
                        1);
            } else if ((this.article.getIsFound())
                    && (this.article.getIsVisible()) && (contents.size() > 0)) {
                /*
                 * set the layout type to multiple to provide a tabbing where
                 * the submit button is the last object to reach
                 */
                this.setLayoutType(LayoutTypeConstants.BORDER);

                /*
                 * set the max sizes of the form
                 */
                this
                        .setMaxSizes(4, contents.size(),
                                CanvasTypeConstants.CENTER); // 4
                // columns,
                // ?
                // lines
                this.setMaxSizes(3, 1, CanvasTypeConstants.SOUTH); // 3

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
                nameField.setRadioButtonType();

                /*
                 * instantiate all entry labels
                 */
                StandardEntryFormLabel nameLabel = (StandardEntryFormLabel) seflf
                        .findByPrimaryKey(nameLabelPk);
                nameLabel.setIsNameBreakable(true);

                /*
                 * add those objects
                 */
                this.setCurrentCanvasType(CanvasTypeConstants.CENTER);

                for (int i = 0; i < contents.size(); i++) {
                    /*
                     * get the content
                     */
                    StandardContent currentContent = (StandardContent) contents
                            .get(i);
                    PrimaryKey currentContentPk = currentContent
                            .getPrimaryKey();

                    /*
                     * verify that the content still exists
                     */
                    if ((currentContent.getIsFound())
                            && (currentContent.getIsVisible())) {
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
                         * clone the field and modify the field clone
                         */
                        StandardEntryFormField nameFieldClone = (StandardEntryFormField) nameField
                                .clone();
                        nameFieldClone.setOptionValueText(currentContentPk
                                .toString());

                        /*
                         * clone the label and modify the name and description
                         * of the label
                         */
                        StandardEntryFormLabel nameLabelClone = (StandardEntryFormLabel) nameLabel
                                .clone();
                        StandardLabel sl3 = nameLabelClone.getStandardLabel();

                        sl3.setCurrentNameText(currentContent.getNameText());
                        sl3.setCurrentNameEncoded(currentContent
                                .getNameEncoded());
                        sl3.setCurrentNameHTML(currentContent.getNameHTML());
                        sl3.setCurrentShortDescriptionText(currentContent
                                .getShortDescriptionText());
                        sl3.setCurrentShortDescriptionEncoded(currentContent
                                .getShortDescriptionEncoded());
                        sl3.setCurrentShortDescriptionHTML(currentContent
                                .getShortDescriptionHTML());

                        /*
                         * associate the field with the label
                         */
                        nameLabelClone
                                .associateStandardFormField(nameFieldClone);

                        /*
                         * add these three objects
                         */
                        this.addStandardFormObject(nameFieldClone, 1, i + 1);
                        this.addStandardFormObject(previewFormIcon, 2, i + 1);
                        this.addStandardFormObject(spacer, 3, i + 1);
                        this.addStandardFormObject(nameLabelClone, 4, i + 1);
                    }
                }

                /*
                 * instantiate and add the submit button
                 */
                this.setCurrentCanvasType(CanvasTypeConstants.SOUTH);

                PrimaryKey nextButtonPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ButtonsDictionary.NEXT_FOR_CONTENT_EDIT);
                StandardEntryFormButton nextButton = (StandardEntryFormButton) sefbf
                        .findByPrimaryKey(nextButtonPk);
                this.addStandardFormObject(nextButton, 1, 1);

                /*
                 * extract the data entries from the form for validation
                 */
                this.contentId = nameField.getRequestValue();
            } else {
                this.buildNoRecordFound();
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
    public void validateExtraEditRecord() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * verify one content was selected (and this content was not recently
         * removed)
         */
        StandardContent content = null;
        PrimaryKey contentPk = null;

        if (this.getIsValid()) {
            if (StringUtil.getIsEmpty(this.contentId)) {
                this.setIsValid(false);

                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_CONTENT_TO_EDIT);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();
            } else {
                contentPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(this.contentId);
                content = (StandardContent) srf.findByPrimaryKey(contentPk);

                if (!(content.getIsFound())) {
                    this.setIsValid(false);

                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_CONTENT_TO_EDIT);
                    StandardMessage error = (StandardMessage) smf
                            .findByPrimaryKey(errorPk);
                    error.printBufferWithIconHTML();
                }
            }
        }

        /*
         * redirect to the appropriate page
         */
        if (this.getIsValid()) {
            /*
             * get all content types
             */
            PrimaryKey articleTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
            PrimaryKey imageTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);

            /*
             * redirect depending on the content type
             */
            PrimaryKey contentTypePk = content.getTypePrimaryKey();

            if (contentTypePk.equals(articleTypePk)) {
                /*
                 * get the page
                 */
                PrimaryKey articleEditPagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(PagesDictionary.EDIT_ARTICLE_PAGE);
                StandardPage articleEditPage = (StandardPage) spf
                        .findByPrimaryKey(articleEditPagePk);

                /*
                 * get the location
                 */
                String redirectURL = articleEditPage.getAbsoluteLocation();
                redirectURL = HTTPUtil
                        .getAddedParameterURL(redirectURL,
                                HTTPParameterConstants.ARTICLE_ID, contentPk
                                        .toString());

                /*
                 * do the redirect
                 */
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
            } else if (contentTypePk.equals(imageTypePk)) {
                /*
                 * get the page
                 */
                PrimaryKey imageEditPagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(PagesDictionary.EDIT_IMAGE_PAGE);
                StandardPage imageEditPage = (StandardPage) spf
                        .findByPrimaryKey(imageEditPagePk);

                /*
                 * get the location
                 */
                String redirectURL = imageEditPage.getAbsoluteLocation();
                redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                        HTTPParameterConstants.IMAGE_ID, contentPk.toString());

                /*
                 * do the redirect
                 */
                ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                        .commitAndSendAbsoluteRedirect(redirectURL);
            }
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
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
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
        ConfigManager cm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

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
         * get the approver
         */
        PrimaryKey articlePk = this.article.getPrimaryKey();
        StandardAccount approver = this.article.getEditorStandardAccount();

        /*
         * instantiate 2 content types
         */
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
        PrimaryKey imageTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);

        /*
         * save the name of the content
         */
        String[] saveContentsNameText = new String[this.contentIds.length];
        String[] saveContentsNameEncoded = new String[this.contentIds.length];
        String[] saveContentsNameHTML = new String[this.contentIds.length];

        /*
         * check if the at least one content id is checked. if no content id is
         * selected then set the stat to be invalid so no subsequent processings
         * will proceed.
         */
        if (this.contentIds.length == 0) {
            PrimaryKey errorPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_CONTENT_TO_REMOVE);
            StandardMessage error = (StandardMessage) smf
                    .findByPrimaryKey(errorPk);
            error.printBufferWithIconHTML();

            this.setIsValid(false);
        }

        /*
         * check each content selected. Validate each content selected before
         * processing.
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.contentIds.length; i++) {
                /*
                 * verify at least one content was selected
                 */
                if (this.getIsValid()) {
                    if (StringUtil.getIsEmpty(this.contentIds[i])) {
                        this.setIsValid(false);

                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_CONTENT_TO_REMOVE);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);
                        error.printBufferWithIconHTML();
                    } else {
                        PrimaryKey contentPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(this.contentIds[i]);
                        StandardContent content = (StandardContent) srf
                                .findByPrimaryKey(contentPk);

                        if (!(content.getIsFound())) {
                            this.setIsValid(false);

                            PrimaryKey errorPk = PrimaryKeyUtil
                                    .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_CONTENT_TO_REMOVE);
                            StandardMessage error = (StandardMessage) smf
                                    .findByPrimaryKey(errorPk);
                            error.printBufferWithIconHTML();
                        }
                    }
                }
            }
        }

        /*
         * check that each content can be edited
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.contentIds.length; i++) {
                if (this.getIsValid()) {
                    PrimaryKey contentPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(this.contentIds[i]);
                    StandardContent content = (StandardContent) srf
                            .findByPrimaryKey(contentPk);

                    if (!(resm.getIsRequestAuthorized(
                            requestor.getPrimaryKey(), contentPk))) {
                        this.setIsValid(false);

                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_REQUEST_CONTENT_REMOVAL);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);
                        error.replaceMessageText(content.getNameText(), 1);
                        error
                                .replaceMessageEncoded(
                                        content.getNameEncoded(), 1);
                        error.replaceMessageHTML(content.getNameHTML(), 1);
                        error.printBufferWithIconHTML();
                    }
                }
            }
        }

        /*
         * check, if the content is an article, that the article is empty
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.contentIds.length; i++) {
                if (this.getIsValid()) {
                    PrimaryKey contentPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(this.contentIds[i]);
                    StandardContent content = (StandardContent) srf
                            .findByPrimaryKey(contentPk);

                    PrimaryKey contentTypePk = content.getTypePrimaryKey();

                    if (articleTypePk.equals(contentTypePk)) {
                        if (content.getIsDirectChildOf(articlePk)) {
                            List<StandardContent> children = srf
                                    .findDirectEnabledByParentArticlePrimaryKey(contentPk);
                            this.setIsValid(children.size() == 0);

                            if (!(children.size() == 0)) {
                                PrimaryKey errorPk = PrimaryKeyUtil
                                        .getAlphanumericSingleKey(MessagesDictionary.ERR_ARTICLE_NOT_EMPTY);
                                StandardMessage error = (StandardMessage) smf
                                        .findByPrimaryKey(errorPk);
                                error.printBufferWithIconHTML();
                            }
                        }
                    }
                }
            }
        }

        /*
         * Save the name of the material and save it to be used in generating
         * browser message
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.contentIds.length; i++) {
                PrimaryKey resourcePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(this.contentIds[i]);
                StandardContent resource = (StandardContent) srf
                        .findByPrimaryKey(resourcePk);
                saveContentsNameText[i] = resource.getNameText();
                saveContentsNameEncoded[i] = resource.getNameEncoded();
                saveContentsNameHTML[i] = resource.getNameHTML();
            }
        }

        /*
         * try to create a content request for each content
         */
        if (this.getIsValid()) {
            for (int i = 0; (this.contentIds != null)
                    && (i < this.contentIds.length); i++) {
                PrimaryKey contentPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(this.contentIds[i]);
                StandardContent content = (StandardContent) srf
                        .findByPrimaryKey(contentPk);
                PrimaryKey contentTypePk = content.getTypePrimaryKey();

                StandardContentRequest contentRequest = null;

                /*
                 * create the direct request
                 */
                if (articleTypePk.equals(contentTypePk)) {
                    contentRequest = (StandardContentRequest) srrf
                            .createArticleRemovalRequest(contentPk, articlePk,
                                    requestor.getPrimaryKey(), approver
                                            .getPrimaryKey());
                } else {
                    contentRequest = (StandardContentRequest) srrf
                            .createImageRemovalRequest(contentPk, articlePk,
                                    requestor.getPrimaryKey(), approver
                                            .getPrimaryKey());
                }

                this.setIsValid(contentRequest.getIsDone());

                if (!(contentRequest.getIsDone())) {
                    MessageUtil
                            .printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                                    contentRequest.getStoreTrace());
                }

                /*
                 * try to approve the reference request
                 */
                if (this.getIsValid()) {
                    if (this.approveRequest.equals("Y")) {
                        if (articleTypePk.equals(contentTypePk)) {
                            contentRequest.approveArticleRequest();
                        } else {
                            contentRequest.approveImageRequest();
                        }

                        this.setIsValid(contentRequest.getIsDone());

                        if (!(contentRequest.getIsDone())) {
                            MessageUtil.printBufferInternalErrorHTML(
                                    ABSTRACT_CLASS_NAME, content
                                            .getStoreTrace());
                        }
                    }

                    // end if-else
                }

                /*
                 * got content request information, proceed to start the email
                 * creation.
                 */
                StandardEmail email = null;

                if (this.getIsValid()) {
                    if (StringUtil.getIsEmpty(this.approveRequest)) {
                        PrimaryKey subjectMessagePk = null;
                        PrimaryKey bodyMessagePk = null;

                        if (content.getTypePrimaryKey().equals(articleTypePk)) {
                            subjectMessagePk = PrimaryKeyUtil
                                    .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_REMOVAL_TO_APPROVE_SUBJECT);
                            bodyMessagePk = PrimaryKeyUtil
                                    .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_REMOVAL_TO_APPROVE_BODY);
                        } else if (content.getTypePrimaryKey().equals(
                                imageTypePk)) {
                            subjectMessagePk = PrimaryKeyUtil
                                    .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_REMOVAL_TO_APPROVE_SUBJECT);
                            bodyMessagePk = PrimaryKeyUtil
                                    .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_REMOVAL_TO_APPROVE_BODY);
                        }

                        StandardMessage subjectMessage = (StandardMessage) smf
                                .findByPrimaryKey(subjectMessagePk);
                        StandardMessage bodyMessage = (StandardMessage) smf
                                .findByPrimaryKey(bodyMessagePk);

                        String companyNameText = prop.getCompanyNameText();
                        String applicationNameText = prop
                                .getApplicationNameText();
                        String articleNameText = content.getNameText();
                        String articleDescriptionText = content
                                .getDescriptionText();
                        String parentArticleNameText = this.article
                                .getNameText();
                        String requestorNameText = requestor.getFullNameText();
                        String requestLocationText = contentRequest
                                .getAbsoluteLocation();

                        subjectMessage.replaceMessageText(articleNameText, 1);

                        bodyMessage.replaceMessageText(companyNameText, 1);
                        bodyMessage.replaceMessageText(applicationNameText, 2);
                        bodyMessage.replaceMessageText(requestorNameText, 3);
                        bodyMessage.replaceMessageText(articleNameText, 4);
                        bodyMessage
                                .replaceMessageText(parentArticleNameText, 5);
                        bodyMessage.replaceMessageText(articleDescriptionText,
                                6);
                        bodyMessage.replaceMessageText(requestLocationText, 7);

                        email = (StandardEmail) semf.create(subjectMessage
                                .getCurrentMessageText(), bodyMessage
                                .getCurrentMessageText());
                        this.setIsValid(email.getIsDone());

                        if (!(email.getIsDone())) {
                            MessageUtil.printBufferInternalErrorHTML(
                                    ABSTRACT_CLASS_NAME, contentRequest
                                            .getStoreTrace());
                        }
                    }

                    // end if
                }

                // end if

                /*
                 * try to set the sender
                 */
                if (this.getIsValid()) {
                    if (email != null) {
                        email.storeFromPrimaryKey(requestor.getPrimaryKey());

                        if (!(email.getIsDone())) {
                            MessageUtil.printBufferInternalErrorHTML(
                                    ABSTRACT_CLASS_NAME, contentRequest
                                            .getStoreTrace());
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
                        email
                                .addRecipient(requestor.getPrimaryKey(),
                                        emailCcPk);

                        if (!(email.getIsDone())) {
                            MessageUtil.printBufferInternalErrorHTML(
                                    ABSTRACT_CLASS_NAME, contentRequest
                                            .getStoreTrace());
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
            }

            // end for-loop
        }

        // end if this.getIsValid()

        /*
         * Create ONE validation message for the browser. If only one content is
         * selected, print message specific for that content. If more than one
         * content is selected, print a general message for the processing.
         */
        if (this.getIsValid()) {
            StandardMessage validation = null;
            PrimaryKey validationPk = null;

            if (this.approveRequest.equals("Y")) {
                /*
                 * immediate content removal
                 */
                if (this.contentIds.length == 1) { // immediate removal 1

                    // content

                    /*
                     * get the validation message
                     */
                    validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_CONTENT_REMOVED);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * add the name of the image to the message
                     */
                    validation.replaceMessageText(saveContentsNameText[0], 1);
                    validation.replaceMessageEncoded(
                            saveContentsNameEncoded[0], 1);
                    validation.replaceMessageHTML(saveContentsNameHTML[0], 1);
                } else { // immediate removal of multiple contents

                    /*
                     * get the validation message
                     */
                    validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_MULTIPLE_CONTENTS_REMOVED);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * insert the number (quantity) of contents removed.
                     */
                    String numberOfContents = String
                            .valueOf(this.contentIds.length);
                    validation.replaceMessageText(numberOfContents, 1);
                    validation.replaceMessageEncoded(numberOfContents, 1);
                    validation.replaceMessageHTML(numberOfContents, 1);
                }

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            } else {
                /*
                 * the request is sent to the approver.
                 */
                PrimaryKey myContentRequestsPagePK = null;
                StandardPage myContentRequestsPage = null;
                String myImageText = null;
                String myImageEncoded = null;
                String myImageHTML = null;

                if (this.contentIds.length == 1) { // request removal of single

                    // content

                    /*
                     * get the validation message
                     */
                    validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_CONTENT_REMOVAL_REQUEST_SENT);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * get the "my content requests" page
                     */
                    myContentRequestsPagePK = PrimaryKeyUtil
                            .getAlphanumericSingleKey(PagesDictionary.MY_CONTENT_REQUESTS);
                    myContentRequestsPage = (StandardPage) spf
                            .findByPrimaryKey(myContentRequestsPagePK);

                    /*
                     * add the name of the request approver
                     */
                    validation.replaceMessageText(approver
                            .getFullNameAndLoginText(), 1);
                    validation.replaceMessageEncoded(approver
                            .getFullNameAndLoginEncoded(), 1);
                    validation.replaceMessageHTML(approver
                            .getFullNameAndLoginHTMLWithLink(), 1);

                    /*
                     * create the image to that page
                     */
                    myImageText = myContentRequestsPage.getNameText();
                    myImageEncoded = myContentRequestsPage.getNameEncoded();
                    myImageHTML = lw.getBreakableLinkHTML(myContentRequestsPage
                            .getDefaultLocation(), null, myContentRequestsPage
                            .getNameHTML(), null);

                    /*
                     * add the image to the my requests page to the message
                     */
                    validation.replaceMessageText(myImageText, 2);
                    validation.replaceMessageEncoded(myImageEncoded, 2);
                    validation.replaceMessageHTML(myImageHTML, 2);
                } else { // request removal of multiple content

                    /*
                     * get the validation message
                     */
                    validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_MULTIPLE_CONTENT_REMOVAL_REQUESTS_SENT);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * get the "my content requests" page
                     */
                    myContentRequestsPagePK = PrimaryKeyUtil
                            .getAlphanumericSingleKey(PagesDictionary.MY_CONTENT_REQUESTS);
                    myContentRequestsPage = (StandardPage) spf
                            .findByPrimaryKey(myContentRequestsPagePK);

                    /*
                     * create the image to that ("my content requests") page
                     */
                    myImageText = myContentRequestsPage.getNameText();
                    myImageEncoded = myContentRequestsPage.getNameEncoded();
                    myImageHTML = lw.getBreakableLinkHTML(myContentRequestsPage
                            .getDefaultLocation(), null, myContentRequestsPage
                            .getNameHTML(), null);

                    /*
                     * insert the number (quantity) of contents removed.
                     */
                    String numberOfContents = String
                            .valueOf(this.contentIds.length);

                    validation.replaceMessageText(numberOfContents, 1);
                    validation.replaceMessageEncoded(numberOfContents, 1);
                    validation.replaceMessageHTML(numberOfContents, 1);

                    /*
                     * add the name of the request approver
                     */
                    validation.replaceMessageText(approver
                            .getFullNameAndLoginText(), 2);
                    validation.replaceMessageEncoded(approver
                            .getFullNameAndLoginEncoded(), 2);
                    validation.replaceMessageHTML(approver
                            .getFullNameAndLoginHTMLWithLink(), 2);

                    /*
                     * add the image to the my requests page to the message
                     */
                    validation.replaceMessageText(myImageText, 3);
                    validation.replaceMessageEncoded(myImageEncoded, 3);
                    validation.replaceMessageHTML(myImageHTML, 3);
                }

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            }

            // end for-loop
        }

        // end if this.getIsValid()
        if (this.getIsValid()) {
            /*
             * redirect to the article detail page for the current article
             */
            String redirectURL = this.article.getAbsoluteLocation();
            ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                    .commitAndSendAbsoluteRedirect(redirectURL);
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }

        // end of the for-loop
    }

    // end if this.getIsValid()

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
}

// end AbstractContentEntryBlock
