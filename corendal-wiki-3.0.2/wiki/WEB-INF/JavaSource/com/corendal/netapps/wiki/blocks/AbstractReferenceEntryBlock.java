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
import com.corendal.netapps.framework.core.interfaces.RequestManager;
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
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardEmailFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;

/**
 * AbstractReferenceEntryBlock is the parent block common to all reference
 * viewing blocks.
 * 
 * @version $Id: AbstractReferenceEntryBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractReferenceEntryBlock extends
        AbstractStandardEntryFormBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractReferenceEntryBlock";

    /** Id of the content selected by the user */
    private String[] contentIds;

    /** Approve request entered by the user */
    private String approveRequest;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractReferenceEntryBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardEntryFormBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractReferenceEntryBlock) super.clone();
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
        PrimaryKey contentsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(contentsPk);
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
         * find the article being modified
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(articleId);

        /*
         * get the list of contents in the history
         */
        List<PrimaryKey> contents = hm.getCleanHistory(articlePk);

        /*
         * check whether contents could be found
         */
        if (contents.size() > 0) {
            /*
             * set the layout type to multiple to provide a tabbing where the
             * submit button is the last object to reach
             */
            this.setLayoutType(LayoutTypeConstants.BORDER);

            /*
             * set the max sizes of the form
             */
            this.setMaxSizes(4, contents.size(), CanvasTypeConstants.CENTER); // 4
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
            PrimaryKey pathFieldPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PATH);
            PrimaryKey pathLabelPk = seflf.getPrimaryKey(entityPk, pathFieldPk);

            PrimaryKey approveRequestFieldPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(FieldsDictionary.CONTENT_APPROVE_REQUEST);
            PrimaryKey approveRequestLabelPk = seflf.getPrimaryKey(entityPk,
                    approveRequestFieldPk);

            /*
             * instantiate all entry fields
             */
            StandardEntryFormField pathField = (StandardEntryFormField) sefff
                    .findByPrimaryKey(pathFieldPk);
            pathField.setCheckBoxType();

            StandardEntryFormField approveRequestField = (StandardEntryFormField) sefff
                    .findByPrimaryKey(approveRequestFieldPk);
            approveRequestField.setCheckBoxType();

            /*
             * instantiate all entry labels
             */
            StandardEntryFormLabel pathLabel = (StandardEntryFormLabel) seflf
                    .findByPrimaryKey(pathLabelPk);
            pathLabel.setIsNameBreakable(true);

            StandardEntryFormLabel approveRequestLabel = (StandardEntryFormLabel) seflf
                    .findByPrimaryKey(approveRequestLabelPk);

            /*
             * associate them
             */
            approveRequestLabel.associateStandardFormField(approveRequestField);

            /*
             * change the default alignments
             */
            approveRequestField.setAlign(currentLocale.getRightAlign());

            /*
             * fill with the default values. This default values will only be
             * used if the form is printed for the first time
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
                PrimaryKey currentContentPk = (PrimaryKey) contents.get(i);
                StandardContent currentContent = (StandardContent) srf
                        .findByPrimaryKey(currentContentPk);

                /*
                 * verify that the content still exists
                 */
                if ((currentContent.getIsFound())
                        && (currentContent.getIsVisible())) {
                    /*
                     * get the preview icon
                     */
                    PrimaryKey previewIconPk = currentContent
                            .getDirectPreviewIconPrimaryKey();
                    StandardFormIcon previewFormIcon = (StandardFormIcon) sfif
                            .findByPrimaryKey(previewIconPk);

                    /*
                     * clone the field and modify the field clone
                     */
                    StandardEntryFormField pathFieldClone = (StandardEntryFormField) pathField
                            .clone();
                    pathFieldClone.setOptionValueText(currentContentPk
                            .toString());

                    /*
                     * clone the label and modify the name of the label
                     */
                    StandardEntryFormLabel pathLabelClone = (StandardEntryFormLabel) pathLabel
                            .clone();
                    StandardLabel sl3 = pathLabelClone.getStandardLabel();

                    sl3.setCurrentNameText(currentContent.getPathText());
                    sl3.setCurrentNameEncoded(currentContent.getPathEncoded());
                    sl3
                            .setCurrentNameHTML(currentContent
                                    .getPathHTMLWithLink());
                    sl3.setCurrentShortDescriptionText(currentContent
                            .getShortDescriptionText());
                    sl3.setCurrentShortDescriptionEncoded(currentContent
                            .getShortDescriptionEncoded());
                    sl3.setCurrentShortDescriptionHTML(currentContent
                            .getShortDescriptionHTML());

                    /*
                     * associate the field with the label
                     */
                    pathLabelClone.associateStandardFormField(pathFieldClone);

                    /*
                     * add these three objects
                     */
                    this.addStandardFormObject(pathFieldClone, 1, i + 1);
                    this.addStandardFormObject(previewFormIcon, 2, i + 1);
                    this.addStandardFormObject(spacer, 3, i + 1);
                    this.addStandardFormObject(pathLabelClone, 4, i + 1);
                }
            }

            /*
             * instantiate and add the submit button
             */
            this.setCurrentCanvasType(CanvasTypeConstants.SOUTH);

            PrimaryKey sendAddRequestPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.SEND_ADD_REQUEST);
            StandardEntryFormButton sendAddRequest = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(sendAddRequestPk);
            this.addStandardFormObject(sendAddRequest, 1, 1);

            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    articlePk)) {
                this.addStandardFormObject(approveRequestField, 2, 1);
                this.addStandardFormObject(approveRequestLabel, 3, 1);
            }

            /*
             * extract the data entries from the form for validation
             */
            this.contentIds = pathField.getRequestValues();
            this.approveRequest = approveRequestField.getRequestValue();
        } else {
            /*
             * get the error message
             */
            PrimaryKey noApplicableContentPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.ERR_NO_APPLICABLE_CONTENT);
            StandardFormMessage noApplicableContentMessage = (StandardFormMessage) sfmf
                    .findByPrimaryKey(noApplicableContentPk);

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
            this.addStandardFormObject(noApplicableContentMessage, 1, 1);
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
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
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
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);
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
         * Check if the at least one content id is checked. if no content id is
         * selected then set the stat to be invalid so no subsequent processings
         * will proceed.
         */
        if (this.contentIds.length == 0) {
            PrimaryKey errorPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_CONTENT_TO_ADD);
            StandardMessage error = (StandardMessage) smf
                    .findByPrimaryKey(errorPk);
            error.printBufferWithIconHTML();

            this.setIsValid(false);
        }

        /*
         * Check each content selected. Validate each content selected before
         * processing.
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.contentIds.length; i++) {
                if (StringUtil.getIsEmpty(this.contentIds[i])) {
                    this.setIsValid(false);

                    PrimaryKey errorPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_CONTENT_TO_ADD);
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
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_CONTENT_TO_ADD);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);
                        error.printBufferWithIconHTML();
                    }

                }

            }

        }

        /*
         * try to create the content request and approve the reference request
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.contentIds.length; i++) {
                /*
                 * get content
                 */
                PrimaryKey contentPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(this.contentIds[i]);
                StandardContent content = (StandardContent) srf
                        .findByPrimaryKey(contentPk);

                /*
                 * try to create the content request
                 */
                PrimaryKey childContentPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(this.contentIds[i]);

                StandardContentRequest contentRequest = null;
                contentRequest = (StandardContentRequest) srrf
                        .createNewReferenceRequest(childContentPk,
                                parentArticlePk, requestor.getPrimaryKey(),
                                approver.getPrimaryKey());
                this.setIsValid(contentRequest.getIsDone());

                if (!(contentRequest.getIsDone())) {
                    MessageUtil
                            .printBufferInternalErrorHTML(ABSTRACT_CLASS_NAME,
                                    contentRequest.getStoreTrace());
                }

                // end if

                /*
                 * try to approve the reference request
                 */
                if (this.getIsValid()) {
                    if (this.approveRequest.equals("Y")) {
                        content.approveNewReferenceRequest(parentArticlePk,
                                contentRequest);
                        this.setIsValid(content.getIsDone());

                        if (!(content.getIsDone())) {
                            MessageUtil.printBufferInternalErrorHTML(
                                    ABSTRACT_CLASS_NAME, content
                                            .getStoreTrace());
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
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_REFERENCE_TO_APPROVE_SUBJECT);
                        StandardMessage subjectMessage = (StandardMessage) smf
                                .findByPrimaryKey(subjectMessagePk);

                        PrimaryKey bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_REFERENCE_TO_APPROVE_BODY);
                        StandardMessage bodyMessage = (StandardMessage) smf
                                .findByPrimaryKey(bodyMessagePk);

                        String companyNameText = prop.getCompanyNameText();
                        String applicationNameText = prop
                                .getApplicationNameText();
                        String referenceNameText = contentRequest.getNameText();
                        String referenceDescriptionText = contentRequest
                                .getDescriptionText();
                        String articleNameText = parentArticle.getNameText();
                        String requestorNameText = requestor.getFullNameText();
                        String requestLocationText = contentRequest
                                .getAbsoluteLocation();

                        subjectMessage.replaceMessageText(referenceNameText, 1);

                        bodyMessage.replaceMessageText(companyNameText, 1);
                        bodyMessage.replaceMessageText(applicationNameText, 2);
                        bodyMessage.replaceMessageText(requestorNameText, 3);
                        bodyMessage.replaceMessageText(referenceNameText, 4);
                        bodyMessage.replaceMessageText(articleNameText, 5);
                        bodyMessage.replaceMessageText(
                                referenceDescriptionText, 6);
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
                }

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

        }

        // end - try to create an email

        /*
         * print the acknowledgment information
         */
        if (this.getIsValid()) {
            // just the first content to get the content

            /*
             * get content. use the first one.
             */
            PrimaryKey contentPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(this.contentIds[0]);
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(contentPk);

            /*
             * get the validation message
             */
            PrimaryKey validationPk = null;
            StandardMessage validation = null;

            if (this.approveRequest.equals("Y")) {
                if (this.contentIds.length == 1) { // one reference created

                    /*
                     * get the validation message
                     */
                    validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_NEW_REFERENCE_CREATED);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * add the name of the link to the message
                     */
                    validation.replaceMessageText(content.getNameText(), 1);
                    validation.replaceMessageEncoded(content.getNameEncoded(),
                            1);
                    validation.replaceMessageHTML(content.getNameHTML(), 1);
                } else {
                    /*
                     * get the validation message
                     */
                    validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_MULTIPLE_NEW_REFERENCES_CREATED);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * indicate the number of content created
                     */
                    String numberOfReferences = String
                            .valueOf(this.contentIds.length);
                    validation.replaceMessageText(numberOfReferences, 1);
                    validation.replaceMessageEncoded(numberOfReferences, 1);
                    validation.replaceMessageHTML(numberOfReferences, 1);
                }
            } else {
                if (this.contentIds.length == 1) { // one request created

                    /*
                     * get the validation message
                     */
                    validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_NEW_REFERENCE_REQUEST_SENT);
                    validation = (StandardMessage) smf
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
                    String myLinkEncoded = myContentRequestsPage
                            .getNameEncoded();
                    String myLinkHTML = lw.getBreakableLinkHTML(
                            myContentRequestsPage.getDefaultLocation(), "",
                            myContentRequestsPage.getNameHTML(), null);

                    /*
                     * add the link to the my requests page to the message
                     */
                    validation.replaceMessageText(myLinkText, 2);
                    validation.replaceMessageEncoded(myLinkEncoded, 2);
                    validation.replaceMessageHTML(myLinkHTML, 2);
                } else { // more than one requests sent

                    /*
                     * get the validation message
                     */
                    validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_MULTIPLE_NEW_REFERENCE_REQUESTS_SENT);
                    validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * add the number of content requests sent
                     */
                    String numberOfReferences = String
                            .valueOf(this.contentIds.length);
                    validation.replaceMessageText(numberOfReferences, 1);
                    validation.replaceMessageEncoded(numberOfReferences, 1);
                    validation.replaceMessageHTML(numberOfReferences, 1);

                    /*
                     * add the name of the approver to the message
                     */
                    validation.replaceMessageText(approver
                            .getFullNameAndLoginText(), 2);
                    validation.replaceMessageEncoded(approver
                            .getFullNameAndLoginEncoded(), 2);
                    validation.replaceMessageHTML(approver
                            .getFullNameAndLoginHTMLWithLink(), 2);

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
                    String myLinkEncoded = myContentRequestsPage
                            .getNameEncoded();
                    String myLinkHTML = lw.getBreakableLinkHTML(
                            myContentRequestsPage.getDefaultLocation(), "",
                            myContentRequestsPage.getNameHTML(), null);

                    /*
                     * add the link to the my requests page to the message
                     */
                    validation.replaceMessageText(myLinkText, 3);
                    validation.replaceMessageEncoded(myLinkEncoded, 3);
                    validation.replaceMessageHTML(myLinkHTML, 3);
                }

            }

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
            /*
             * redirect to the article detail page for the current article
             */
            String redirectURL = parentArticle.getAbsoluteLocation();
            ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                    .commitAndSendAbsoluteRedirect(redirectURL);
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }
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

// end AbstractReferenceEntryBlock
