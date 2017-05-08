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

import com.corendal.netapps.framework.configuration.utils.CaseUtil;
import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.blocks.AbstractStandardEntryFormBlock;
import com.corendal.netapps.framework.core.constants.AccessTypeConstants;
import com.corendal.netapps.framework.core.constants.CanvasTypeConstants;
import com.corendal.netapps.framework.core.constants.LayoutTypeConstants;
import com.corendal.netapps.framework.core.dictionaries.IconsDictionary;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormIconFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormMessageFactory;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
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
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFile;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFileFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.BlockUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardStoredFileFactory;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentRequestTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.dictionaries.RequestStatusTypesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationType;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestType;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRuleType;
import com.corendal.netapps.wiki.interfaces.StandardContentRuleTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentType;
import com.corendal.netapps.wiki.interfaces.StandardContentTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.interfaces.StandardRequestStatusType;
import com.corendal.netapps.wiki.interfaces.StandardRequestStatusTypeFactory;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentRequestTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentRuleTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentTypeFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardRequestStatusTypeFactory;
import com.corendal.netapps.wiki.utils.ContentRequestUtil;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractContentRequestEntryBlock is the parent block common to all
 * contentRequest viewing blocks.
 * 
 * @version $Id: AbstractContentRequestEntryBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractContentRequestEntryBlock extends
        AbstractStandardEntryFormBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractContentRequestEntryBlock";

    /** Name of the hidden parameter detecting the button being pressed */
    private static final String BUTTON_DETECTOR_NAME = "buttondetector";

    /** Content request viewed through this form */
    private StandardContentRequest contentRequest;

    /** Name entered through this form */
    private String name;

    /** Description entered through this form */
    private String description;

    /** Version notes entered through this form */
    private String versionNotes;

    /** Friendly address entered through this form */
    private String friendlyAddress;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractContentRequestEntryBlock() {
        this.contentRequest = null;
    }

    /**
     * Returns a clone. Overrides AbstractStandardEntryFormBlock.clone.
     */
    @Override
    public Object clone() {
        AbstractContentRequestEntryBlock result = (AbstractContentRequestEntryBlock) super
                .clone();

        if (this.contentRequest != null) {
            result.contentRequest = (StandardContentRequest) this.contentRequest
                    .clone();
        }

        return result;
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
        PrimaryKey contentRequestsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(contentRequestsPk);
        this.setStandardEntity(entity);
    }

    /**
     * Builds the layout of the form. This procedure is used when viewing a
     * contentRequest.
     * 
     * 
     * 
     */
    private void build() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntryFormButtonFactory sefbf = (StandardEntryFormButtonFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormButtonFactory.class);
        StandardEntryFormLabelFactory seflf = (StandardEntryFormLabelFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormLabelFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardStoredFileFactory ssff = (StandardStoredFileFactory) pfs
                .getStandardBeanFactory(DefaultStandardStoredFileFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardFormIconFactory sfif = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);
        StandardRequestStatusTypeFactory srstf = (StandardRequestStatusTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardRequestStatusTypeFactory.class);
        StandardContentRequestTypeFactory sresrtf = (StandardContentRequestTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestTypeFactory.class);
        StandardContentTypeFactory srtf = (StandardContentTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentTypeFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);
        StandardContentClassificationTypeFactory srctf = (StandardContentClassificationTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentClassificationTypeFactory.class);
        StandardContentRuleTypeFactory srrtf = (StandardContentRuleTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRuleTypeFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);

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
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * get all request types
         */
        PrimaryKey addTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRequestTypesDictionary.ADD);
        PrimaryKey editTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRequestTypesDictionary.EDIT);
        PrimaryKey removeTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRequestTypesDictionary.REMOVE);

        /*
         * get all content types
         */
        PrimaryKey imageTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
        PrimaryKey referenceTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.REFERENCE);

        /*
         * get the current request type and content type
         */
        PrimaryKey currentRequestTypePk = this.contentRequest
                .getRequestTypePrimaryKey();
        PrimaryKey currentContentTypePk = this.contentRequest
                .getContentTypePrimaryKey();

        /*
         * set the layout type to multiple to provide a tabbing where the submit
         * button is the last object to reach
         */
        this.setLayoutType(LayoutTypeConstants.BORDER);

        /*
         * set the max sizes of the form
         */
        if (currentRequestTypePk.equals(addTypePk)) {
            if (currentContentTypePk.equals(imageTypePk)) {
                this.setMaxSizes(2, 16, CanvasTypeConstants.CENTER);
            } else if (currentContentTypePk.equals(articleTypePk)) {
                this.setMaxSizes(2, 16, CanvasTypeConstants.CENTER);
            } else if (currentContentTypePk.equals(referenceTypePk)) {
                this.setMaxSizes(2, 10, CanvasTypeConstants.CENTER);
            }
        } else if (currentRequestTypePk.equals(editTypePk)) {
            if (currentContentTypePk.equals(referenceTypePk)) {
                this.setMaxSizes(2, 15, CanvasTypeConstants.CENTER);
            } else if (currentContentTypePk.equals(articleTypePk)) {
                this.setMaxSizes(2, 17, CanvasTypeConstants.CENTER);
            } else if (currentContentTypePk.equals(imageTypePk)) {
                this.setMaxSizes(2, 17, CanvasTypeConstants.CENTER);
            }
        } else if (currentRequestTypePk.equals(removeTypePk)) {
            if (currentContentTypePk.equals(referenceTypePk)) {
                this.setMaxSizes(2, 11, CanvasTypeConstants.CENTER);
            } else {
                this.setMaxSizes(2, 10, CanvasTypeConstants.CENTER);
            }
        }

        this.setMaxSizes(4, 1, CanvasTypeConstants.SOUTH); // 4

        // columns,
        // 1 line

        /*
         * get the primary keys of the required instances
         */
        PrimaryKey entityPk = entity.getPrimaryKey();
        PrimaryKey contentRequestTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_REQUEST_TYPE);
        PrimaryKey contentRequestStatusFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_REQUEST_STATUS);
        PrimaryKey contentRequestRequestorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_REQUEST_REQUESTOR);
        PrimaryKey contentRequestApproverFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_REQUEST_APPROVER);
        PrimaryKey contentRequestParentArticleFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PARENT_ARTICLE);
        PrimaryKey contentRequestContentTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_TYPE);
        PrimaryKey contentRequestContentPathFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PATH);

        PrimaryKey contentRequestNameFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_NAME);
        PrimaryKey contentRequestDescriptionFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_DESCRIPTION);
        PrimaryKey contentRequestEditorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_EDITOR);
        PrimaryKey contentRequestAssociateEditorsFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_ASSOCIATE_EDITORS);
        PrimaryKey contentRequestAuthorFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_AUTHOR);
        PrimaryKey contentRequestClassificationTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_CLASSIFICATION);
        PrimaryKey contentRequestRuleTypeFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_RULE);
        PrimaryKey contentRequestFriendlyAddressFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FRIENDLY_ADDRESS);
        PrimaryKey contentRequestFileFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_FILE);
        PrimaryKey contentRequestReferencedContentFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_REQUEST_REFERENCED_CONTENT);

        PrimaryKey contentRequestCreatedFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_REQUEST_CREATED);
        PrimaryKey contentRequestModifiedFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_REQUEST_MODIFIED);
        PrimaryKey contentRequestVersionNotesFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_VERSION_NOTES);

        PrimaryKey contentRequestTypeLabelPk = seflf.getPrimaryKey(entityPk,
                contentRequestTypeFieldPk);
        PrimaryKey contentRequestStatusLabelPk = seflf.getPrimaryKey(entityPk,
                contentRequestStatusFieldPk);
        PrimaryKey contentRequestRequestorLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestRequestorFieldPk);
        PrimaryKey contentRequestApproverLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestApproverFieldPk);
        PrimaryKey contentRequestParentArticleLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestParentArticleFieldPk);
        PrimaryKey contentRequestContentTypeLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestContentTypeFieldPk);
        PrimaryKey contentRequestContentPathLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestContentPathFieldPk);

        PrimaryKey contentRequestNameLabelPk = seflf.getPrimaryKey(entityPk,
                contentRequestNameFieldPk);
        PrimaryKey contentRequestDescriptionLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestDescriptionFieldPk);
        PrimaryKey contentRequestEditorLabelPk = seflf.getPrimaryKey(entityPk,
                contentRequestEditorFieldPk);
        PrimaryKey contentRequestAssociateEditorsLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestAssociateEditorsFieldPk);
        PrimaryKey contentRequestAuthorLabelPk = seflf.getPrimaryKey(entityPk,
                contentRequestAuthorFieldPk);
        PrimaryKey contentRequestClassificationTypeLabelPk = seflf
                .getPrimaryKey(entityPk,
                        contentRequestClassificationTypeFieldPk);
        PrimaryKey contentRequestRuleTypeLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestRuleTypeFieldPk);
        PrimaryKey contentRequestFriendlyAddressLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestFriendlyAddressFieldPk);
        PrimaryKey contentRequestFileLabelPk = seflf.getPrimaryKey(entityPk,
                contentRequestFileFieldPk);
        PrimaryKey contentRequestReferencedContentLabelPk = seflf
                .getPrimaryKey(entityPk, contentRequestReferencedContentFieldPk);

        PrimaryKey contentRequestCreatedLabelPk = seflf.getPrimaryKey(entityPk,
                contentRequestCreatedFieldPk);
        PrimaryKey contentRequestModifiedLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestModifiedFieldPk);
        PrimaryKey contentRequestVersionNotesLabelPk = seflf.getPrimaryKey(
                entityPk, contentRequestVersionNotesFieldPk);

        /*
         * instantiate all entry fields
         */
        StandardEntryFormField contentRequestTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestTypeFieldPk);
        StandardEntryFormField contentRequestStatusField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestStatusFieldPk);
        StandardEntryFormField contentRequestRequestorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestRequestorFieldPk);
        StandardEntryFormField contentRequestApproverField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestApproverFieldPk);
        StandardEntryFormField contentRequestParentArticleField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestParentArticleFieldPk);
        StandardEntryFormField contentRequestContentTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestContentTypeFieldPk);
        StandardEntryFormField contentRequestContentPathField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestContentPathFieldPk);

        StandardEntryFormField contentRequestNameField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestNameFieldPk);
        StandardEntryFormField contentRequestDescriptionField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestDescriptionFieldPk);
        contentRequestDescriptionField.setTextAreaType(60, 4);

        StandardEntryFormField contentRequestEditorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestEditorFieldPk);
        StandardEntryFormField contentRequestAssociateEditorsField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestAssociateEditorsFieldPk);
        StandardEntryFormField contentRequestAuthorField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestAuthorFieldPk);
        StandardEntryFormField contentRequestClassificationTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestClassificationTypeFieldPk);
        StandardEntryFormField contentRequestRuleTypeField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestRuleTypeFieldPk);
        StandardEntryFormField contentRequestFriendlyAddressField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestFriendlyAddressFieldPk);
        StandardEntryFormField contentRequestFileField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestFileFieldPk);
        StandardEntryFormField contentRequestReferencedContentField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestReferencedContentFieldPk);

        StandardEntryFormField contentRequestCreatedField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestCreatedFieldPk);
        StandardEntryFormField contentRequestModifiedField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestModifiedFieldPk);
        StandardEntryFormField contentRequestVersionNotesField = (StandardEntryFormField) sefff
                .findByPrimaryKey(contentRequestVersionNotesFieldPk);
        contentRequestVersionNotesField.setTextAreaType(60, 4);

        /*
         * instantiate all entry labels
         */
        StandardEntryFormLabel contentRequestTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestTypeLabelPk);
        StandardEntryFormLabel contentRequestStatusLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestStatusLabelPk);
        StandardEntryFormLabel contentRequestRequestorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestRequestorLabelPk);
        StandardEntryFormLabel contentRequestApproverLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestApproverLabelPk);
        StandardEntryFormLabel contentRequestParentArticleLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestParentArticleLabelPk);
        StandardEntryFormLabel contentRequestContentTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestContentTypeLabelPk);
        StandardEntryFormLabel contentRequestContentPathLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestContentPathLabelPk);

        StandardEntryFormLabel contentRequestNameLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestNameLabelPk);
        StandardEntryFormLabel contentRequestDescriptionLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestDescriptionLabelPk);
        StandardEntryFormLabel contentRequestEditorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestEditorLabelPk);
        StandardEntryFormLabel contentRequestAssociateEditorsLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestAssociateEditorsLabelPk);
        StandardEntryFormLabel contentRequestAuthorLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestAuthorLabelPk);
        StandardEntryFormLabel contentRequestClassificationTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestClassificationTypeLabelPk);
        StandardEntryFormLabel contentRequestRuleTypeLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestRuleTypeLabelPk);
        StandardEntryFormLabel contentRequestFriendlyAddressLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestFriendlyAddressLabelPk);
        StandardEntryFormLabel contentRequestFileLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestFileLabelPk);
        StandardEntryFormLabel contentRequestReferencedContentLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestReferencedContentLabelPk);

        StandardEntryFormLabel contentRequestCreatedLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestCreatedLabelPk);
        StandardEntryFormLabel contentRequestModifiedLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestModifiedLabelPk);
        StandardEntryFormLabel contentRequestVersionNotesLabel = (StandardEntryFormLabel) seflf
                .findByPrimaryKey(contentRequestVersionNotesLabelPk);

        /*
         * associate them
         */
        contentRequestTypeLabel
                .associateStandardFormField(contentRequestTypeField);
        contentRequestStatusLabel
                .associateStandardFormField(contentRequestStatusField);
        contentRequestRequestorLabel
                .associateStandardFormField(contentRequestRequestorField);
        contentRequestApproverLabel
                .associateStandardFormField(contentRequestApproverField);
        contentRequestParentArticleLabel
                .associateStandardFormField(contentRequestParentArticleField);
        contentRequestContentTypeLabel
                .associateStandardFormField(contentRequestContentTypeField);
        contentRequestContentPathLabel
                .associateStandardFormField(contentRequestContentPathField);

        contentRequestNameLabel
                .associateStandardFormField(contentRequestNameField);
        contentRequestDescriptionLabel
                .associateStandardFormField(contentRequestDescriptionField);
        contentRequestEditorLabel
                .associateStandardFormField(contentRequestEditorField);
        contentRequestAssociateEditorsLabel
                .associateStandardFormField(contentRequestAssociateEditorsField);
        contentRequestAuthorLabel
                .associateStandardFormField(contentRequestAuthorField);
        contentRequestClassificationTypeLabel
                .associateStandardFormField(contentRequestClassificationTypeField);
        contentRequestRuleTypeLabel
                .associateStandardFormField(contentRequestRuleTypeField);
        contentRequestFriendlyAddressLabel
                .associateStandardFormField(contentRequestFriendlyAddressField);
        contentRequestFileLabel
                .associateStandardFormField(contentRequestFileField);
        contentRequestReferencedContentLabel
                .associateStandardFormField(contentRequestReferencedContentField);
        contentRequestFileLabel
                .associateStandardFormField(contentRequestFileField);

        contentRequestCreatedLabel
                .associateStandardFormField(contentRequestCreatedField);
        contentRequestModifiedLabel
                .associateStandardFormField(contentRequestModifiedField);
        contentRequestVersionNotesLabel
                .associateStandardFormField(contentRequestVersionNotesField);

        /*
         * instantiate all icons
         */
        PrimaryKey spacerPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SPACER);
        StandardFormIcon spacer = (StandardFormIcon) sfif
                .findByPrimaryKey(spacerPk);
        spacer.setSize(20, 1);

        /*
         * set the appropriate fields in print only mode
         */
        contentRequestTypeField.setPrintOnlyType();
        contentRequestStatusField.setPrintOnlyType();
        contentRequestRequestorField.setPrintOnlyType();
        contentRequestApproverField.setPrintOnlyType();
        contentRequestParentArticleField.setPrintOnlyType();
        contentRequestContentTypeField.setPrintOnlyType();
        contentRequestContentPathField.setPrintOnlyType();
        contentRequestEditorField.setPrintOnlyType();
        contentRequestAssociateEditorsField.setPrintOnlyType();
        contentRequestAuthorField.setPrintOnlyType();
        contentRequestClassificationTypeField.setPrintOnlyType();
        contentRequestRuleTypeField.setPrintOnlyType();
        contentRequestFileField.setPrintOnlyType();
        contentRequestReferencedContentField.setPrintOnlyType();
        contentRequestCreatedField.setPrintOnlyType();
        contentRequestModifiedField.setPrintOnlyType();

        PrimaryKey pendingRequestStatusTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RequestStatusTypesDictionary.PENDING);

        if (!(this.getAccessType().equals(AccessTypeConstants.EDIT))) {
            contentRequestNameField.setPrintOnlyType();
            contentRequestDescriptionField.setPrintOnlyType();
            contentRequestFriendlyAddressField.setPrintOnlyType();
            contentRequestVersionNotesField.setPrintOnlyType();
        }

        /*
         * set the mandatory objects
         */
        contentRequestTypeLabel.setAsMandatory();
        contentRequestTypeField.setAsMandatory();
        contentRequestStatusLabel.setAsMandatory();
        contentRequestStatusField.setAsMandatory();
        contentRequestRequestorLabel.setAsMandatory();
        contentRequestRequestorField.setAsMandatory();
        contentRequestApproverLabel.setAsMandatory();
        contentRequestApproverField.setAsMandatory();
        contentRequestParentArticleField.setAsMandatory();
        contentRequestParentArticleLabel.setAsMandatory();
        contentRequestContentTypeLabel.setAsMandatory();
        contentRequestContentTypeField.setAsMandatory();
        contentRequestContentPathLabel.setAsMandatory();
        contentRequestContentPathField.setAsMandatory();
        contentRequestFileLabel.setAsMandatory();
        contentRequestFileField.setAsMandatory();
        contentRequestNameLabel.setAsMandatory();
        contentRequestNameField.setAsMandatory();
        contentRequestDescriptionLabel.setAsMandatory();
        contentRequestDescriptionField.setAsMandatory();
        contentRequestAuthorLabel.setAsMandatory();
        contentRequestAuthorField.setAsMandatory();
        contentRequestFriendlyAddressLabel.setAsMandatory();
        contentRequestFriendlyAddressField.setAsMandatory();
        contentRequestClassificationTypeLabel.setAsMandatory();
        contentRequestClassificationTypeField.setAsMandatory();
        contentRequestRuleTypeLabel.setAsMandatory();
        contentRequestRuleTypeField.setAsMandatory();
        contentRequestEditorLabel.setAsMandatory();
        contentRequestEditorField.setAsMandatory();
        contentRequestReferencedContentLabel.setAsMandatory();
        contentRequestReferencedContentField.setAsMandatory();
        contentRequestCreatedLabel.setAsMandatory();
        contentRequestCreatedField.setAsMandatory();
        contentRequestModifiedLabel.setAsMandatory();
        contentRequestModifiedField.setAsMandatory();

        /*
         * instantiate the content type
         */
        StandardContentType contentType = null;

        if (this.contentRequest.getContentTypePrimaryKey() != null) {
            contentType = (StandardContentType) srtf
                    .findByPrimaryKey(this.contentRequest
                            .getContentTypePrimaryKey());
            contentRequestContentTypeField.setFirstDefaultValue(contentType
                    .getNameText());
        }

        /*
         * instantiate the content request type
         */
        StandardContentRequestType contentRequestType = null;

        if (this.contentRequest.getRequestTypePrimaryKey() != null) {
            contentRequestType = (StandardContentRequestType) sresrtf
                    .findByPrimaryKey(this.contentRequest
                            .getRequestTypePrimaryKey());
            contentRequestTypeField.setFirstDefaultValue(contentRequestType
                    .getNameText());
        }

        /*
         * instantiate the request status type
         */
        StandardRequestStatusType requestStatusType = null;

        if (this.contentRequest.getStatusTypePrimaryKey() != null) {
            requestStatusType = (StandardRequestStatusType) srstf
                    .findByPrimaryKey(this.contentRequest
                            .getStatusTypePrimaryKey());
            contentRequestStatusField.setFirstDefaultValue(requestStatusType
                    .getNameText());
        }

        /*
         * instantiate the requestor and approver
         */
        StandardAccount requestorStandardAccount = this.contentRequest
                .getRequestorStandardAccount();
        StandardAccount approverStandardAccount = this.contentRequest
                .getApproverStandardAccount();

        if ((requestorStandardAccount != null)
                && (requestorStandardAccount.getIsFound())) {
            contentRequestRequestorField
                    .setFirstDefaultValue(requestorStandardAccount
                            .getFullNameAndLoginText());
            contentRequestRequestorField
                    .setFirstDefaultValueHTML(requestorStandardAccount
                            .getFullNameAndLoginHTMLWithLink());
        } else {
            contentRequestRequestorField.setFirstDefaultValue("");
        }

        if ((approverStandardAccount != null)
                && (approverStandardAccount.getIsFound())) {
            contentRequestApproverField
                    .setFirstDefaultValue(approverStandardAccount
                            .getFullNameAndLoginText());
            contentRequestApproverField
                    .setFirstDefaultValueHTML(approverStandardAccount
                            .getFullNameAndLoginHTMLWithLink());
        } else {
            contentRequestApproverField.setFirstDefaultValue("");
        }

        /*
         * instantiate the created and modified dates
         */
        contentRequestCreatedField.setFirstDefaultValue(DateFormatUtil
                .getLongFormattedDateText(this.contentRequest
                        .getFirstEntryLogTimestamp()));
        contentRequestModifiedField.setFirstDefaultValue(DateFormatUtil
                .getLongFormattedDateText(this.contentRequest
                        .getLastEntryLogTimestamp()));

        /*
         * set the current line number
         */
        int lineNumber = 0;

        /*
         * add other info based upon type of request
         */
        if (currentRequestTypePk.equals(addTypePk)) {
            if (currentContentTypePk.equals(imageTypePk)) {
                /*
                 * add those objects
                 */
                this.addStandardFormObject(contentRequestTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestTypeField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestStatusLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestStatusField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestRequestorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestRequestorField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestApproverLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestApproverField, 2,
                        lineNumber);

                /*
                 * add the parent image
                 */
                StandardArticle parentArticle = null;

                if (this.contentRequest.getParentContentPrimaryKey() != null) {
                    parentArticle = (StandardArticle) sdf
                            .findByPrimaryKey(this.contentRequest
                                    .getParentContentPrimaryKey());
                    contentRequestParentArticleField
                            .setFirstDefaultValue(parentArticle.getPrimaryKey()
                                    .toString());
                    contentRequestParentArticleField
                            .setFirstDefaultValueHTML(parentArticle
                                    .getPathHTMLWithLink());
                    this.addStandardFormObject(
                            contentRequestParentArticleLabel, 1, ++lineNumber);
                    this.addStandardFormObject(
                            contentRequestParentArticleField, 2, lineNumber);
                }

                /*
                 * add the content type
                 */
                this.addStandardFormObject(contentRequestContentTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestContentTypeField, 2,
                        lineNumber);

                /*
                 * add the file
                 */
                PrimaryKey storedFilePk = this.contentRequest
                        .getFilePrimaryKey();
                StandardStoredFile storedFile = (StandardStoredFile) ssff
                        .findByPrimaryKey(storedFilePk);

                if (storedFile.getIsFound()) {
                    contentRequestFileField.setFirstDefaultValue(storedFile
                            .getName());

                    PrimaryKey contentRequestDownloadImagePagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(PagesDictionary.CONTENT_REQUEST_DOWNLOAD_IMAGE);
                    StandardPage contentRequestDownloadImagePage = (StandardPage) spf
                            .findByPrimaryKey(contentRequestDownloadImagePagePk);

                    if (contentRequestDownloadImagePage.getIsFound()) {
                        String location = contentRequestDownloadImagePage
                                .getDefaultLocation();
                        location = HTTPUtil.getAddedParameterURL(location,
                                HTTPParameterConstants.CONTENT_REQUEST_ID,
                                this.contentRequest.getPrimaryKey().toString());

                        String linkHTML = lw.getBreakableLinkHTML(location, "",
                                TextFormatUtil.getTextToHTML(storedFile
                                        .getName()), null);
                        contentRequestFileField
                                .setFirstDefaultValueHTML(linkHTML);
                    }
                }

                this.addStandardFormObject(contentRequestFileLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestFileField, 2,
                        lineNumber);

                /*
                 * add the name
                 */
                contentRequestNameField
                        .setFirstDefaultValue(this.contentRequest.getNameText());
                contentRequestNameField
                        .setFirstDefaultValueHTML(this.contentRequest
                                .getNameHTML());
                this.addStandardFormObject(contentRequestNameLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestNameField, 2,
                        lineNumber);

                /*
                 * add the description
                 */
                contentRequestDescriptionField
                        .setFirstDefaultValue(this.contentRequest
                                .getDescriptionText());
                contentRequestDescriptionField
                        .setFirstDefaultValueHTML(this.contentRequest
                                .getDescriptionHTML());
                this.addStandardFormObject(contentRequestDescriptionLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestDescriptionField, 2,
                        lineNumber);

                /*
                 * add the author
                 */
                StandardAccount authorStandardAccount = this.contentRequest
                        .getAuthorStandardAccount();

                if ((authorStandardAccount != null)
                        && (authorStandardAccount.getIsFound())) {
                    contentRequestAuthorField
                            .setFirstDefaultValue(authorStandardAccount
                                    .getFullNameAndLoginText());
                    contentRequestAuthorField
                            .setFirstDefaultValueHTML(authorStandardAccount
                                    .getFullNameAndLoginHTMLWithLink());
                } else {
                    contentRequestAuthorField.setFirstDefaultValue("");
                }

                this.addStandardFormObject(contentRequestAuthorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestAuthorField, 2,
                        lineNumber);

                /*
                 * add the classification type
                 */
                StandardContentClassificationType classificationType = (StandardContentClassificationType) srctf
                        .findByPrimaryKey(this.contentRequest
                                .getClassificationTypePrimaryKey());
                contentRequestClassificationTypeField
                        .setFirstDefaultValue(classificationType.getNameText());
                this.addStandardFormObject(
                        contentRequestClassificationTypeLabel, 1, ++lineNumber);
                this.addStandardFormObject(
                        contentRequestClassificationTypeField, 2, lineNumber);

                /*
                 * add the rule type
                 */
                StandardContentRuleType ruleType = (StandardContentRuleType) srrtf
                        .findByPrimaryKey(this.contentRequest
                                .getRuleTypePrimaryKey());
                contentRequestRuleTypeField.setFirstDefaultValue(ruleType
                        .getNameText());
                this.addStandardFormObject(contentRequestRuleTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestRuleTypeField, 2,
                        lineNumber);

                /*
                 * add the friendly address
                 */
                contentRequestFriendlyAddressField
                        .setFirstDefaultValue(this.contentRequest
                                .getFriendlyAddress());
                this.addStandardFormObject(contentRequestFriendlyAddressLabel,
                        1, ++lineNumber);
                this.addStandardFormObject(contentRequestFriendlyAddressField,
                        2, lineNumber);

                /*
                 * add the created and modified dates
                 */
                this.addStandardFormObject(contentRequestCreatedLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestCreatedField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestModifiedLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestModifiedField, 2,
                        lineNumber);

                /*
                 * add the version notes
                 */
                contentRequestVersionNotesField
                        .setFirstDefaultValue(this.contentRequest.getComment());
                this.addStandardFormObject(contentRequestVersionNotesLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestVersionNotesField, 2,
                        lineNumber);
            } else if (currentContentTypePk.equals(articleTypePk)) {
                /*
                 * add those objects
                 */
                this.addStandardFormObject(contentRequestTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestTypeField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestStatusLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestStatusField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestRequestorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestRequestorField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestApproverLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestApproverField, 2,
                        lineNumber);

                /*
                 * add the parent image
                 */
                StandardArticle parentArticle = null;

                if (this.contentRequest.getParentContentPrimaryKey() != null) {
                    parentArticle = (StandardArticle) sdf
                            .findByPrimaryKey(this.contentRequest
                                    .getParentContentPrimaryKey());
                    contentRequestParentArticleField
                            .setFirstDefaultValue(parentArticle.getPrimaryKey()
                                    .toString());
                    contentRequestParentArticleField
                            .setFirstDefaultValueHTML(parentArticle
                                    .getPathHTMLWithLink());
                    this.addStandardFormObject(
                            contentRequestParentArticleLabel, 1, ++lineNumber);
                    this.addStandardFormObject(
                            contentRequestParentArticleField, 2, lineNumber);
                }

                /*
                 * add the content type
                 */
                this.addStandardFormObject(contentRequestContentTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestContentTypeField, 2,
                        lineNumber);

                /*
                 * add the name
                 */
                contentRequestNameField
                        .setFirstDefaultValue(this.contentRequest.getNameText());
                contentRequestNameField
                        .setFirstDefaultValueHTML(this.contentRequest
                                .getNameHTML());
                this.addStandardFormObject(contentRequestNameLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestNameField, 2,
                        lineNumber);

                /*
                 * add the description
                 */
                contentRequestDescriptionField
                        .setFirstDefaultValue(this.contentRequest
                                .getDescriptionText());
                contentRequestDescriptionField
                        .setFirstDefaultValueHTML(this.contentRequest
                                .getDescriptionHTML());
                this.addStandardFormObject(contentRequestDescriptionLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestDescriptionField, 2,
                        lineNumber);

                /*
                 * add the author
                 */
                StandardAccount authorStandardAccount = this.contentRequest
                        .getAuthorStandardAccount();

                if ((authorStandardAccount != null)
                        && (authorStandardAccount.getIsFound())) {
                    contentRequestAuthorField
                            .setFirstDefaultValue(authorStandardAccount
                                    .getFullNameAndLoginText());
                    contentRequestAuthorField
                            .setFirstDefaultValueHTML(authorStandardAccount
                                    .getFullNameAndLoginHTMLWithLink());
                } else {
                    contentRequestAuthorField.setFirstDefaultValue("");
                }

                this.addStandardFormObject(contentRequestAuthorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestAuthorField, 2,
                        lineNumber);

                /*
                 * add the editor
                 */
                StandardAccount editorStandardAccount = this.contentRequest
                        .getEditorStandardAccount();

                if ((editorStandardAccount != null)
                        && (editorStandardAccount.getIsFound())) {
                    contentRequestEditorField
                            .setFirstDefaultValue(editorStandardAccount
                                    .getFullNameAndLoginText());
                    contentRequestEditorField
                            .setFirstDefaultValueHTML(editorStandardAccount
                                    .getFullNameAndLoginHTMLWithLink());
                } else {
                    contentRequestEditorField.setFirstDefaultValue("");
                }

                this.addStandardFormObject(contentRequestEditorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestEditorField, 2,
                        lineNumber);

                /*
                 * add the associate editors
                 */
                StandardGroup associateEditorsStandardGroup = this.contentRequest
                        .getAssociateEditorsStandardGroup();

                if ((associateEditorsStandardGroup != null)
                        && (associateEditorsStandardGroup.getIsFound())) {
                    contentRequestAssociateEditorsField
                            .setFirstDefaultValue(associateEditorsStandardGroup
                                    .getNameText());
                    contentRequestAssociateEditorsField
                            .setFirstDefaultValueHTML(associateEditorsStandardGroup
                                    .getNameHTML());
                } else {
                    contentRequestAssociateEditorsField
                            .setFirstDefaultValue("");
                }

                this.addStandardFormObject(contentRequestAssociateEditorsLabel,
                        1, ++lineNumber);
                this.addStandardFormObject(contentRequestAssociateEditorsField,
                        2, lineNumber);

                /*
                 * add the classification type
                 */
                StandardContentClassificationType classificationType = (StandardContentClassificationType) srctf
                        .findByPrimaryKey(this.contentRequest
                                .getClassificationTypePrimaryKey());
                contentRequestClassificationTypeField
                        .setFirstDefaultValue(classificationType.getNameText());
                this.addStandardFormObject(
                        contentRequestClassificationTypeLabel, 1, ++lineNumber);
                this.addStandardFormObject(
                        contentRequestClassificationTypeField, 2, lineNumber);

                /*
                 * add the rule type
                 */
                StandardContentRuleType ruleType = (StandardContentRuleType) srrtf
                        .findByPrimaryKey(this.contentRequest
                                .getRuleTypePrimaryKey());
                contentRequestRuleTypeField.setFirstDefaultValue(ruleType
                        .getNameText());
                this.addStandardFormObject(contentRequestRuleTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestRuleTypeField, 2,
                        lineNumber);

                /*
                 * add the created and modified dates
                 */
                this.addStandardFormObject(contentRequestCreatedLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestCreatedField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestModifiedLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestModifiedField, 2,
                        lineNumber);

                /*
                 * add the version notes
                 */
                contentRequestVersionNotesField
                        .setFirstDefaultValue(this.contentRequest.getComment());
                this.addStandardFormObject(contentRequestVersionNotesLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestVersionNotesField, 2,
                        lineNumber);
            } else if (currentContentTypePk.equals(referenceTypePk)) {
                if ((this.getAccessType().equals(AccessTypeConstants.VIEW))) {
                    /*
                     * add those objects
                     */
                    this.addStandardFormObject(contentRequestTypeLabel, 1,
                            ++lineNumber);
                    this.addStandardFormObject(contentRequestTypeField, 2,
                            lineNumber);
                    this.addStandardFormObject(contentRequestStatusLabel, 1,
                            ++lineNumber);
                    this.addStandardFormObject(contentRequestStatusField, 2,
                            lineNumber);
                    this.addStandardFormObject(contentRequestRequestorLabel, 1,
                            ++lineNumber);
                    this.addStandardFormObject(contentRequestRequestorField, 2,
                            lineNumber);
                    this.addStandardFormObject(contentRequestApproverLabel, 1,
                            ++lineNumber);
                    this.addStandardFormObject(contentRequestApproverField, 2,
                            lineNumber);

                    /*
                     * add the parent image
                     */
                    StandardArticle parentArticle = null;

                    if (this.contentRequest.getParentContentPrimaryKey() != null) {
                        parentArticle = (StandardArticle) sdf
                                .findByPrimaryKey(this.contentRequest
                                        .getParentContentPrimaryKey());
                        contentRequestParentArticleField
                                .setFirstDefaultValue(parentArticle
                                        .getPrimaryKey().toString());
                        contentRequestParentArticleField
                                .setFirstDefaultValueHTML(parentArticle
                                        .getPathHTMLWithLink());
                        this.addStandardFormObject(
                                contentRequestParentArticleLabel, 1,
                                ++lineNumber);
                        this
                                .addStandardFormObject(
                                        contentRequestParentArticleField, 2,
                                        lineNumber);
                    }

                    /*
                     * add the content type
                     */
                    this.addStandardFormObject(contentRequestContentTypeLabel,
                            1, ++lineNumber);
                    this.addStandardFormObject(contentRequestContentTypeField,
                            2, lineNumber);

                    /*
                     * add the referenced content
                     */
                    PrimaryKey referencedContentPk = this.contentRequest
                            .getChildContentPrimaryKey();
                    StandardContent referencedContent = (StandardContent) srf
                            .findByPrimaryKey(referencedContentPk);

                    if (referencedContent.getIsFound()) {
                        contentRequestReferencedContentField
                                .setFirstDefaultValue(referencedContent
                                        .getPathText());
                        contentRequestReferencedContentField
                                .setFirstDefaultValueHTML(referencedContent
                                        .getPathHTMLWithLink());
                    }

                    this.addStandardFormObject(
                            contentRequestReferencedContentLabel, 1,
                            ++lineNumber);
                    this
                            .addStandardFormObject(
                                    contentRequestReferencedContentField, 2,
                                    lineNumber);

                    /*
                     * add the description
                     */
                    contentRequestDescriptionField
                            .setFirstDefaultValue(this.contentRequest
                                    .getDescriptionText());
                    contentRequestDescriptionField
                            .setFirstDefaultValueHTML(this.contentRequest
                                    .getDescriptionHTML());
                    this.addStandardFormObject(contentRequestDescriptionLabel,
                            1, ++lineNumber);
                    this.addStandardFormObject(contentRequestDescriptionField,
                            2, lineNumber);

                    /*
                     * add the created and modified dates
                     */
                    this.addStandardFormObject(contentRequestCreatedLabel, 1,
                            ++lineNumber);
                    this.addStandardFormObject(contentRequestCreatedField, 2,
                            lineNumber);
                    this.addStandardFormObject(contentRequestModifiedLabel, 1,
                            ++lineNumber);
                    this.addStandardFormObject(contentRequestModifiedField, 2,
                            lineNumber);
                } else {
                    /*
                     * get the error message
                     */
                    PrimaryKey cannotEditReferenceRequestMessagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_EDIT_REFERENCE_REQUEST);
                    StandardFormMessage cannotEditReferenceRequestMessage = (StandardFormMessage) sfmf
                            .findByPrimaryKey(cannotEditReferenceRequestMessagePk);

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
                    this.addStandardFormObject(
                            cannotEditReferenceRequestMessage, 1, 1);
                }
            }
        } else if (currentRequestTypePk.equals(editTypePk)) {
            if (currentContentTypePk.equals(imageTypePk)) {
                /*
                 * add those objects
                 */
                this.addStandardFormObject(contentRequestTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestTypeField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestStatusLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestStatusField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestRequestorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestRequestorField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestApproverLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestApproverField, 2,
                        lineNumber);

                /*
                 * add the parent image
                 */
                StandardArticle parentArticle = null;

                if (this.contentRequest.getParentContentPrimaryKey() != null) {
                    parentArticle = (StandardArticle) sdf
                            .findByPrimaryKey(this.contentRequest
                                    .getParentContentPrimaryKey());
                    contentRequestParentArticleField
                            .setFirstDefaultValue(parentArticle.getPrimaryKey()
                                    .toString());
                    contentRequestParentArticleField
                            .setFirstDefaultValueHTML(parentArticle
                                    .getPathHTMLWithLink());
                    this.addStandardFormObject(
                            contentRequestParentArticleLabel, 1, ++lineNumber);
                    this.addStandardFormObject(
                            contentRequestParentArticleField, 2, lineNumber);
                }

                /*
                 * add the content type
                 */
                this.addStandardFormObject(contentRequestContentTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestContentTypeField, 2,
                        lineNumber);

                /*
                 * add the content path
                 */
                PrimaryKey updatedContentPk = this.contentRequest
                        .getChildContentPrimaryKey();
                StandardContent updatedContent = (StandardContent) srf
                        .findByPrimaryKey(updatedContentPk);

                if (updatedContent.getIsFound()) {
                    contentRequestContentPathField
                            .setFirstDefaultValue(updatedContent.getPathText());
                    contentRequestContentPathField
                            .setFirstDefaultValueHTML(updatedContent
                                    .getPathHTMLWithLink());
                }

                this.addStandardFormObject(contentRequestContentPathLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestContentPathField, 2,
                        lineNumber);

                /*
                 * add the file
                 */
                PrimaryKey storedFilePk = this.contentRequest
                        .getFilePrimaryKey();
                StandardStoredFile storedFile = (StandardStoredFile) ssff
                        .findByPrimaryKey(storedFilePk);

                if (storedFile.getIsFound()) {
                    contentRequestFileField.setFirstDefaultValue(storedFile
                            .getName());

                    PrimaryKey contentRequestDownloadImagePagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(PagesDictionary.CONTENT_REQUEST_DOWNLOAD_IMAGE);
                    StandardPage contentRequestDownloadImagePage = (StandardPage) spf
                            .findByPrimaryKey(contentRequestDownloadImagePagePk);

                    if (contentRequestDownloadImagePage.getIsFound()) {
                        String location = contentRequestDownloadImagePage
                                .getDefaultLocation();
                        location = HTTPUtil.getAddedParameterURL(location,
                                HTTPParameterConstants.CONTENT_REQUEST_ID,
                                this.contentRequest.getPrimaryKey().toString());

                        String linkHTML = lw.getBreakableLinkHTML(location, "",
                                TextFormatUtil.getTextToHTML(storedFile
                                        .getName()), null);
                        contentRequestFileField
                                .setFirstDefaultValueHTML(linkHTML);
                    }
                }

                this.addStandardFormObject(contentRequestFileLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestFileField, 2,
                        lineNumber);

                /*
                 * add the name
                 */
                contentRequestNameField
                        .setFirstDefaultValue(this.contentRequest.getNameText());
                contentRequestNameField
                        .setFirstDefaultValueHTML(this.contentRequest
                                .getNameHTML());
                this.addStandardFormObject(contentRequestNameLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestNameField, 2,
                        lineNumber);

                /*
                 * add the description
                 */
                contentRequestDescriptionField
                        .setFirstDefaultValue(this.contentRequest
                                .getDescriptionText());
                contentRequestDescriptionField
                        .setFirstDefaultValueHTML(this.contentRequest
                                .getDescriptionHTML());
                this.addStandardFormObject(contentRequestDescriptionLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestDescriptionField, 2,
                        lineNumber);

                /*
                 * add the author
                 */
                StandardAccount authorStandardAccount = this.contentRequest
                        .getAuthorStandardAccount();

                if ((authorStandardAccount != null)
                        && (authorStandardAccount.getIsFound())) {
                    contentRequestAuthorField
                            .setFirstDefaultValue(authorStandardAccount
                                    .getFullNameAndLoginText());
                    contentRequestAuthorField
                            .setFirstDefaultValueHTML(authorStandardAccount
                                    .getFullNameAndLoginHTMLWithLink());
                } else {
                    contentRequestAuthorField.setFirstDefaultValue("");
                }

                this.addStandardFormObject(contentRequestAuthorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestAuthorField, 2,
                        lineNumber);

                /*
                 * add the classification type
                 */
                StandardContentClassificationType classificationType = (StandardContentClassificationType) srctf
                        .findByPrimaryKey(this.contentRequest
                                .getClassificationTypePrimaryKey());
                contentRequestClassificationTypeField
                        .setFirstDefaultValue(classificationType.getNameText());
                this.addStandardFormObject(
                        contentRequestClassificationTypeLabel, 1, ++lineNumber);
                this.addStandardFormObject(
                        contentRequestClassificationTypeField, 2, lineNumber);

                /*
                 * add the rule type
                 */
                StandardContentRuleType ruleType = (StandardContentRuleType) srrtf
                        .findByPrimaryKey(this.contentRequest
                                .getRuleTypePrimaryKey());
                contentRequestRuleTypeField.setFirstDefaultValue(ruleType
                        .getNameText());
                this.addStandardFormObject(contentRequestRuleTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestRuleTypeField, 2,
                        lineNumber);

                /*
                 * add the friendly address
                 */
                contentRequestFriendlyAddressField
                        .setFirstDefaultValue(this.contentRequest
                                .getFriendlyAddress());
                this.addStandardFormObject(contentRequestFriendlyAddressLabel,
                        1, ++lineNumber);
                this.addStandardFormObject(contentRequestFriendlyAddressField,
                        2, lineNumber);

                /*
                 * add the created and modified dates
                 */
                this.addStandardFormObject(contentRequestCreatedLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestCreatedField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestModifiedLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestModifiedField, 2,
                        lineNumber);

                /*
                 * add the version notes
                 */
                contentRequestVersionNotesField
                        .setFirstDefaultValue(this.contentRequest.getComment());
                this.addStandardFormObject(contentRequestVersionNotesLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestVersionNotesField, 2,
                        lineNumber);
            } else if (currentContentTypePk.equals(articleTypePk)) {
                /*
                 * add those objects
                 */
                this.addStandardFormObject(contentRequestTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestTypeField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestStatusLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestStatusField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestRequestorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestRequestorField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestApproverLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestApproverField, 2,
                        lineNumber);

                /*
                 * add the parent image
                 */
                StandardArticle parentArticle = null;

                if (this.contentRequest.getParentContentPrimaryKey() != null) {
                    parentArticle = (StandardArticle) sdf
                            .findByPrimaryKey(this.contentRequest
                                    .getParentContentPrimaryKey());
                    contentRequestParentArticleField
                            .setFirstDefaultValue(parentArticle.getPrimaryKey()
                                    .toString());
                    contentRequestParentArticleField
                            .setFirstDefaultValueHTML(parentArticle
                                    .getPathHTMLWithLink());
                    this.addStandardFormObject(
                            contentRequestParentArticleLabel, 1, ++lineNumber);
                    this.addStandardFormObject(
                            contentRequestParentArticleField, 2, lineNumber);
                }

                /*
                 * add the content type
                 */
                this.addStandardFormObject(contentRequestContentTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestContentTypeField, 2,
                        lineNumber);

                /*
                 * add the content path
                 */
                PrimaryKey updatedContentPk = this.contentRequest
                        .getChildContentPrimaryKey();
                StandardContent updatedContent = (StandardContent) srf
                        .findByPrimaryKey(updatedContentPk);

                if (updatedContent.getIsFound()) {
                    contentRequestContentPathField
                            .setFirstDefaultValue(updatedContent.getPathText());
                    contentRequestContentPathField
                            .setFirstDefaultValueHTML(updatedContent
                                    .getPathHTMLWithLink());
                }

                this.addStandardFormObject(contentRequestContentPathLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestContentPathField, 2,
                        lineNumber);

                /*
                 * add the name
                 */
                contentRequestNameField
                        .setFirstDefaultValue(this.contentRequest.getNameText());
                contentRequestNameField
                        .setFirstDefaultValueHTML(this.contentRequest
                                .getNameHTML());
                this.addStandardFormObject(contentRequestNameLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestNameField, 2,
                        lineNumber);

                /*
                 * add the description
                 */
                contentRequestDescriptionField
                        .setFirstDefaultValue(this.contentRequest
                                .getDescriptionText());
                contentRequestDescriptionField
                        .setFirstDefaultValueHTML(this.contentRequest
                                .getDescriptionHTML());
                this.addStandardFormObject(contentRequestDescriptionLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestDescriptionField, 2,
                        lineNumber);

                /*
                 * add the author
                 */
                StandardAccount authorStandardAccount = this.contentRequest
                        .getAuthorStandardAccount();

                if ((authorStandardAccount != null)
                        && (authorStandardAccount.getIsFound())) {
                    contentRequestAuthorField
                            .setFirstDefaultValue(authorStandardAccount
                                    .getFullNameAndLoginText());
                    contentRequestAuthorField
                            .setFirstDefaultValueHTML(authorStandardAccount
                                    .getFullNameAndLoginHTMLWithLink());
                } else {
                    contentRequestAuthorField.setFirstDefaultValue("");
                }

                this.addStandardFormObject(contentRequestAuthorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestAuthorField, 2,
                        lineNumber);

                /*
                 * add the editor
                 */
                StandardAccount editorStandardAccount = this.contentRequest
                        .getEditorStandardAccount();

                if ((editorStandardAccount != null)
                        && (editorStandardAccount.getIsFound())) {
                    contentRequestEditorField
                            .setFirstDefaultValue(editorStandardAccount
                                    .getFullNameAndLoginText());
                    contentRequestEditorField
                            .setFirstDefaultValueHTML(editorStandardAccount
                                    .getFullNameAndLoginHTMLWithLink());
                } else {
                    contentRequestEditorField.setFirstDefaultValue("");
                }

                this.addStandardFormObject(contentRequestEditorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestEditorField, 2,
                        lineNumber);

                /*
                 * add the associate editors
                 */
                StandardGroup associateEditorsStandardGroup = this.contentRequest
                        .getAssociateEditorsStandardGroup();

                if ((associateEditorsStandardGroup != null)
                        && (associateEditorsStandardGroup.getIsFound())) {
                    contentRequestAssociateEditorsField
                            .setFirstDefaultValue(associateEditorsStandardGroup
                                    .getNameText());
                    contentRequestAssociateEditorsField
                            .setFirstDefaultValueHTML(associateEditorsStandardGroup
                                    .getNameHTML());
                } else {
                    contentRequestAssociateEditorsField
                            .setFirstDefaultValue("");
                }

                this.addStandardFormObject(contentRequestAssociateEditorsLabel,
                        1, ++lineNumber);
                this.addStandardFormObject(contentRequestAssociateEditorsField,
                        2, lineNumber);

                /*
                 * add the classification type
                 */
                StandardContentClassificationType classificationType = (StandardContentClassificationType) srctf
                        .findByPrimaryKey(this.contentRequest
                                .getClassificationTypePrimaryKey());
                contentRequestClassificationTypeField
                        .setFirstDefaultValue(classificationType.getNameText());
                this.addStandardFormObject(
                        contentRequestClassificationTypeLabel, 1, ++lineNumber);
                this.addStandardFormObject(
                        contentRequestClassificationTypeField, 2, lineNumber);

                /*
                 * add the rule type
                 */
                StandardContentRuleType ruleType = (StandardContentRuleType) srrtf
                        .findByPrimaryKey(this.contentRequest
                                .getRuleTypePrimaryKey());
                contentRequestRuleTypeField.setFirstDefaultValue(ruleType
                        .getNameText());
                this.addStandardFormObject(contentRequestRuleTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestRuleTypeField, 2,
                        lineNumber);

                /*
                 * add the created and modified dates
                 */
                this.addStandardFormObject(contentRequestCreatedLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestCreatedField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestModifiedLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestModifiedField, 2,
                        lineNumber);

                /*
                 * add the version notes
                 */
                contentRequestVersionNotesField
                        .setFirstDefaultValue(this.contentRequest.getComment());
                this.addStandardFormObject(contentRequestVersionNotesLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestVersionNotesField, 2,
                        lineNumber);
            }
        } else if (currentRequestTypePk.equals(removeTypePk)) {
            if (currentContentTypePk.equals(referenceTypePk)) {
                if ((this.getAccessType().equals(AccessTypeConstants.VIEW))) {
                    /*
                     * add the parent image
                     */
                    StandardArticle parentArticle = null;

                    if (this.contentRequest.getParentContentPrimaryKey() != null) {
                        parentArticle = (StandardArticle) sdf
                                .findByPrimaryKey(this.contentRequest
                                        .getParentContentPrimaryKey());
                        contentRequestParentArticleField
                                .setFirstDefaultValue(parentArticle
                                        .getPrimaryKey().toString());
                        contentRequestParentArticleField
                                .setFirstDefaultValueHTML(parentArticle
                                        .getPathHTMLWithLink());
                        this.addStandardFormObject(
                                contentRequestParentArticleLabel, 1,
                                ++lineNumber);
                        this
                                .addStandardFormObject(
                                        contentRequestParentArticleField, 2,
                                        lineNumber);
                    }

                    /*
                     * add the content type
                     */
                    this.addStandardFormObject(contentRequestContentTypeLabel,
                            1, ++lineNumber);
                    this.addStandardFormObject(contentRequestContentTypeField,
                            2, lineNumber);

                    /*
                     * add the referenced content
                     */
                    PrimaryKey referencedContentPk = this.contentRequest
                            .getChildContentPrimaryKey();
                    StandardContent referencedContent = (StandardContent) srf
                            .findByPrimaryKey(referencedContentPk);

                    if (referencedContent.getIsFound()) {
                        contentRequestReferencedContentField
                                .setFirstDefaultValue(referencedContent
                                        .getPathText());
                        contentRequestReferencedContentField
                                .setFirstDefaultValueHTML(referencedContent
                                        .getPathHTMLWithLink());
                    }

                    this.addStandardFormObject(
                            contentRequestReferencedContentLabel, 1,
                            ++lineNumber);
                    this
                            .addStandardFormObject(
                                    contentRequestReferencedContentField, 2,
                                    lineNumber);

                    /*
                     * add the description
                     */
                    contentRequestDescriptionField
                            .setFirstDefaultValue(this.contentRequest
                                    .getDescriptionText());
                    contentRequestDescriptionField
                            .setFirstDefaultValueHTML(this.contentRequest
                                    .getDescriptionHTML());
                    this.addStandardFormObject(contentRequestDescriptionLabel,
                            1, ++lineNumber);
                    this.addStandardFormObject(contentRequestDescriptionField,
                            2, lineNumber);

                    /*
                     * add the created and modified dates
                     */
                    this.addStandardFormObject(contentRequestCreatedLabel, 1,
                            ++lineNumber);
                    this.addStandardFormObject(contentRequestCreatedField, 2,
                            lineNumber);
                    this.addStandardFormObject(contentRequestModifiedLabel, 1,
                            ++lineNumber);
                    this.addStandardFormObject(contentRequestModifiedField, 2,
                            lineNumber);
                } else {
                    /*
                     * get the error message
                     */
                    PrimaryKey cannotEditReferenceRequestMessagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_EDIT_REFERENCE_REQUEST);
                    StandardFormMessage cannotEditReferenceRequestMessage = (StandardFormMessage) sfmf
                            .findByPrimaryKey(cannotEditReferenceRequestMessagePk);

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
                    this.addStandardFormObject(
                            cannotEditReferenceRequestMessage, 1, 1);
                }
            } else {
                /*
                 * add those objects
                 */
                this.addStandardFormObject(contentRequestTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestTypeField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestStatusLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestStatusField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestRequestorLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestRequestorField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestApproverLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestApproverField, 2,
                        lineNumber);

                /*
                 * add the content type
                 */
                this.addStandardFormObject(contentRequestContentTypeLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestContentTypeField, 2,
                        lineNumber);

                /*
                 * add the content path
                 */
                PrimaryKey removedContentPk = this.contentRequest
                        .getChildContentPrimaryKey();
                StandardContent removedContent = (StandardContent) srf
                        .findByPrimaryKey(removedContentPk);

                if (removedContent.getIsFound()) {
                    contentRequestContentPathField
                            .setFirstDefaultValue(removedContent.getPathText());
                    contentRequestContentPathField
                            .setFirstDefaultValueHTML(removedContent
                                    .getPathHTMLWithLink());
                }

                this.addStandardFormObject(contentRequestContentPathLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestContentPathField, 2,
                        lineNumber);

                /*
                 * add the description
                 */
                contentRequestDescriptionField
                        .setFirstDefaultValue(this.contentRequest
                                .getDescriptionText());
                contentRequestDescriptionField
                        .setFirstDefaultValueHTML(this.contentRequest
                                .getDescriptionHTML());
                this.addStandardFormObject(contentRequestDescriptionLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestDescriptionField, 2,
                        lineNumber);

                /*
                 * add the created and modified dates
                 */
                this.addStandardFormObject(contentRequestCreatedLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestCreatedField, 2,
                        lineNumber);
                this.addStandardFormObject(contentRequestModifiedLabel, 1,
                        ++lineNumber);
                this.addStandardFormObject(contentRequestModifiedField, 2,
                        lineNumber);
            }
        }

        /*
         * instantiate and add the buttons
         */
        this.setCurrentCanvasType(CanvasTypeConstants.SOUTH);

        if ((!(this.getAccessType().equals(AccessTypeConstants.EDIT)))) {
            if (this.contentRequest.getStatusTypePrimaryKey().equals(
                    pendingRequestStatusTypePk)) {
                PrimaryKey approveRequestPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ButtonsDictionary.APPROVE_REQUEST);
                StandardEntryFormButton approveRequest = (StandardEntryFormButton) sefbf
                        .findByPrimaryKey(approveRequestPk);
                approveRequest.setDetectorNameAndValue(BUTTON_DETECTOR_NAME,
                        ContentRequestUtil.BUTTON_DETECTOR_APPROVE_VALUE);

                PrimaryKey rejectRequestPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ButtonsDictionary.REJECT_REQUEST);
                StandardEntryFormButton rejectRequest = (StandardEntryFormButton) sefbf
                        .findByPrimaryKey(rejectRequestPk);
                rejectRequest.setDetectorNameAndValue(BUTTON_DETECTOR_NAME,
                        ContentRequestUtil.BUTTON_DETECTOR_REJECT_VALUE);
                PrimaryKey cancelRequestPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ButtonsDictionary.CANCEL_REQUEST);
                StandardEntryFormButton cancelRequest = (StandardEntryFormButton) sefbf
                        .findByPrimaryKey(cancelRequestPk);
                cancelRequest.setDetectorNameAndValue(BUTTON_DETECTOR_NAME,
                        ContentRequestUtil.BUTTON_DETECTOR_CANCEL_VALUE);

                int columnNumber = 0;

                if (resm.getIsRequestApprovalAuthorized(sa.getPrimaryKey(),
                        this.contentRequest.getPrimaryKey())) {
                    this.addStandardFormObject(approveRequest, ++columnNumber,
                            1);
                    this.addStandardFormObject(spacer, ++columnNumber, 1);
                    this
                            .addStandardFormObject(rejectRequest,
                                    ++columnNumber, 1);
                }

                if (resm.getIsRequestCancellationAuthorized(sa.getPrimaryKey(),
                        this.contentRequest.getPrimaryKey())) {
                    this
                            .addStandardFormObject(cancelRequest,
                                    ++columnNumber, 1);
                }
            }
        } else {
            PrimaryKey updateRequestPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.UPDATE_REQUEST);
            StandardEntryFormButton updateRequest = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(updateRequestPk);

            this.addStandardFormObject(updateRequest, 1, 1);
        }

        /*
         * extract the data entries from the form for validation
         */
        this.name = contentRequestNameField.getRequestValue();
        this.description = contentRequestDescriptionField.getRequestValue();
        this.versionNotes = contentRequestVersionNotesField.getRequestValue();
        this.friendlyAddress = ContentUtil
                .getCleanFriendlyAddress(contentRequestFriendlyAddressField
                        .getRequestValue());
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
        StandardArticleFactory sartf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardImageFactory sif = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);

        /*
         * extract the data entries from the form for validation
         */
        String actionRequest = this.getFormButtonDetectorValue(
                BUTTON_DETECTOR_NAME,
                ContentRequestUtil.BUTTON_DETECTOR_NONE_VALUE);

        /*
         * get all request types
         */
        PrimaryKey addTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRequestTypesDictionary.ADD);
        PrimaryKey editTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRequestTypesDictionary.EDIT);
        PrimaryKey removeTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRequestTypesDictionary.REMOVE);

        /*
         * get all content types
         */
        PrimaryKey imageTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
        PrimaryKey referenceTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.REFERENCE);

        /*
         * get the current request type and content type
         */
        PrimaryKey currentRequestTypePk = this.contentRequest
                .getRequestTypePrimaryKey();
        PrimaryKey currentContentTypePk = this.contentRequest
                .getContentTypePrimaryKey();

        /*
         * instantiate the parent image
         */
        StandardArticle parentArticle = null;

        if (this.contentRequest.getParentContentPrimaryKey() != null) {
            parentArticle = (StandardArticle) sartf
                    .findByPrimaryKey(this.contentRequest
                            .getParentContentPrimaryKey());
        }

        /*
         * verify that the name of the content is not all uppercase
         */
        if (this.getIsValid()) {
            if ((currentRequestTypePk.equals(addTypePk))
                    || (currentRequestTypePk.equals(editTypePk))) {
                if (CaseUtil.getIsTextualAndAllUpperCase(this.name)) {
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
        }

        /*
         * verify that the description of the content is not all uppercase
         */
        if (this.getIsValid()) {
            if ((currentRequestTypePk.equals(addTypePk))
                    || (currentRequestTypePk.equals(editTypePk))) {
                if (CaseUtil.getIsTextualAndAllUpperCase(this.description)) {
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
        }

        /*
         * if this is a image removal request, verify that the image is empty
         */
        if (this.getIsValid()) {
            if (actionRequest
                    .equals(ContentRequestUtil.BUTTON_DETECTOR_APPROVE_VALUE)) {
                if (currentContentTypePk.equals(imageTypePk)) {
                    if (currentRequestTypePk.equals(removeTypePk)) {
                        PrimaryKey imagePk = this.contentRequest
                                .getChildContentPrimaryKey();
                        List<StandardContent> children = srf
                                .findDirectEnabledByParentArticlePrimaryKey(imagePk);
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

        /*
         * if this is an article add or edit request, verify that the friendly
         * address is unique
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.friendlyAddress))) {
                if (actionRequest
                        .equals(ContentRequestUtil.BUTTON_DETECTOR_APPROVE_VALUE)) {
                    if (currentContentTypePk.equals(articleTypePk)) {
                        StandardArticle friendlyArticle = sartf
                                .findByFriendlyAddress(this.friendlyAddress);

                        if (friendlyArticle.getIsFound()) {
                            if (currentRequestTypePk.equals(addTypePk)) {
                                this.setIsValid(false);

                                PrimaryKey errorPk = PrimaryKeyUtil
                                        .getAlphanumericSingleKey(MessagesDictionary.ERR_FRIENDLY_ADDRESS_ALREADY_USED);
                                StandardMessage error = (StandardMessage) smf
                                        .findByPrimaryKey(errorPk);
                                error.printBufferWithIconHTML();
                            } else if (currentRequestTypePk.equals(editTypePk)) {
                                PrimaryKey articlePk = this.contentRequest
                                        .getChildContentPrimaryKey();

                                if (!(articlePk.equals(friendlyArticle
                                        .getPrimaryKey()))) {
                                    this.setIsValid(false);

                                    PrimaryKey errorPk = PrimaryKeyUtil
                                            .getAlphanumericSingleKey(MessagesDictionary.ERR_FRIENDLY_ADDRESS_ALREADY_USED);
                                    StandardMessage error = (StandardMessage) smf
                                            .findByPrimaryKey(errorPk);
                                    error.printBufferWithIconHTML();
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
         * if this is an image add or edit request, verify that the friendly
         * address is unique
         */
        if (this.getIsValid()) {
            if (!(StringUtil.getIsEmpty(this.friendlyAddress))) {
                if (actionRequest
                        .equals(ContentRequestUtil.BUTTON_DETECTOR_APPROVE_VALUE)) {
                    if (currentContentTypePk.equals(imageTypePk)) {
                        StandardImage friendlyImage = sif
                                .findByFriendlyAddress(this.friendlyAddress);

                        if (friendlyImage.getIsFound()) {
                            if (currentRequestTypePk.equals(addTypePk)) {
                                this.setIsValid(false);

                                PrimaryKey errorPk = PrimaryKeyUtil
                                        .getAlphanumericSingleKey(MessagesDictionary.ERR_FRIENDLY_ADDRESS_ALREADY_USED);
                                StandardMessage error = (StandardMessage) smf
                                        .findByPrimaryKey(errorPk);
                                error.printBufferWithIconHTML();
                            } else if (currentRequestTypePk.equals(editTypePk)) {
                                PrimaryKey imagePk = this.contentRequest
                                        .getChildContentPrimaryKey();

                                if (!(imagePk.equals(friendlyImage
                                        .getPrimaryKey()))) {
                                    this.setIsValid(false);

                                    PrimaryKey errorPk = PrimaryKeyUtil
                                            .getAlphanumericSingleKey(MessagesDictionary.ERR_FRIENDLY_ADDRESS_ALREADY_USED);
                                    StandardMessage error = (StandardMessage) smf
                                            .findByPrimaryKey(errorPk);
                                    error.printBufferWithIconHTML();
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
         * verify that the request is still pending approval
         */
        if (this.getIsValid()) {
            PrimaryKey statusPk = this.contentRequest.getStatusTypePrimaryKey();
            PrimaryKey pendingPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RequestStatusTypesDictionary.PENDING);

            if ((statusPk == null) || (!(statusPk.equals(pendingPk)))) {
                this.setIsValid(false);

                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CONTENT_REQUEST_NOT_PENDING);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();
            }
        }

        /*
         * try to approve the request
         */
        if (this.getIsValid()) {
            if (actionRequest
                    .equals(ContentRequestUtil.BUTTON_DETECTOR_APPROVE_VALUE)) {
                if (currentContentTypePk.equals(referenceTypePk)) {
                    /*
                     * instantiate the content referenced
                     */
                    StandardContent content = (StandardContent) srf
                            .findByPrimaryKey(this.contentRequest
                                    .getChildContentPrimaryKey());

                    /*
                     * approve the reference request for this content
                     */
                    if (currentRequestTypePk.equals(addTypePk)) {
                        content.approveNewReferenceRequest(parentArticle
                                .getPrimaryKey(), this.contentRequest);
                    } else if (currentRequestTypePk.equals(removeTypePk)) {
                        content.approveReferenceRemovalRequest(parentArticle
                                .getPrimaryKey(), this.contentRequest);
                    }

                    this.setIsValid(content.getIsDone());

                    if (!(content.getIsDone())) {
                        MessageUtil.printBufferInternalErrorHTML(
                                ABSTRACT_CLASS_NAME, content.getStoreTrace());
                    }
                } else if (currentContentTypePk.equals(imageTypePk)) {
                    this.contentRequest.approveImageRequest();
                    this.setIsValid(this.contentRequest.getIsDone());

                    if (!(this.contentRequest.getIsDone())) {
                        MessageUtil.printBufferInternalErrorHTML(
                                ABSTRACT_CLASS_NAME, this.contentRequest
                                        .getStoreTrace());
                    }
                } else if (currentContentTypePk.equals(articleTypePk)) {
                    this.contentRequest.approveArticleRequest();
                    this.setIsValid(this.contentRequest.getIsDone());

                    if (!(this.contentRequest.getIsDone())) {
                        MessageUtil.printBufferInternalErrorHTML(
                                ABSTRACT_CLASS_NAME, this.contentRequest
                                        .getStoreTrace());
                    }
                } else if (currentContentTypePk.equals(imageTypePk)) {
                    this.contentRequest.approveImageRequest();
                    this.setIsValid(this.contentRequest.getIsDone());

                    if (!(this.contentRequest.getIsDone())) {
                        MessageUtil.printBufferInternalErrorHTML(
                                ABSTRACT_CLASS_NAME, this.contentRequest
                                        .getStoreTrace());
                    }
                }
            } else if (actionRequest
                    .equals(ContentRequestUtil.BUTTON_DETECTOR_REJECT_VALUE)) {
                this.contentRequest.storeRejection();
                this.setIsValid(this.contentRequest.getIsDone());

                if (!(this.contentRequest.getIsDone())) {
                    MessageUtil.printBufferInternalErrorHTML(
                            ABSTRACT_CLASS_NAME, this.contentRequest
                                    .getStoreTrace());
                }
            } else if (actionRequest
                    .equals(ContentRequestUtil.BUTTON_DETECTOR_CANCEL_VALUE)) {
                this.contentRequest.storeCancellation();
                this.setIsValid(this.contentRequest.getIsDone());

                if (!(this.contentRequest.getIsDone())) {
                    MessageUtil.printBufferInternalErrorHTML(
                            ABSTRACT_CLASS_NAME, this.contentRequest
                                    .getStoreTrace());
                }
            } else {
                this.contentRequest.storeInfo(this.name, this.description);
                this.contentRequest.storeComment(this.versionNotes);
                this.setIsValid(this.contentRequest.getIsDone());

                if (!(this.contentRequest.getIsDone())) {
                    MessageUtil.printBufferInternalErrorHTML(
                            ABSTRACT_CLASS_NAME, this.contentRequest
                                    .getStoreTrace());
                }

                if (this.contentRequest.getIsDone()) {
                    this.contentRequest
                            .storeFriendlyAddress(this.friendlyAddress);

                    if (!(this.contentRequest.getIsDone())) {
                        MessageUtil.printBufferInternalErrorHTML(
                                ABSTRACT_CLASS_NAME, this.contentRequest
                                        .getStoreTrace());
                    }
                }
            }
        }

        /*
         * try to create an email
         */
        if (this.getIsValid()) {
            ContentRequestUtil.sendDisposition(this.contentRequest,
                    actionRequest);
        }

        /*
         * print the acknowledgment information
         */
        if (this.getIsValid()) {
            if (actionRequest
                    .equals(ContentRequestUtil.BUTTON_DETECTOR_APPROVE_VALUE)) {
                /*
                 * get the validation message
                 */
                PrimaryKey validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_CONTENT_REQUEST_APPROVED);
                StandardMessage validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            } else if (actionRequest
                    .equals(ContentRequestUtil.BUTTON_DETECTOR_REJECT_VALUE)) {
                /*
                 * get the validation message
                 */
                PrimaryKey validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_CONTENT_REQUEST_REJECTED);
                StandardMessage validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            } else if (actionRequest
                    .equals(ContentRequestUtil.BUTTON_DETECTOR_CANCEL_VALUE)) {
                /*
                 * get the validation message
                 */
                PrimaryKey validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_CONTENT_REQUEST_CANCELLED);
                StandardMessage validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            } else {
                /*
                 * get the validation message
                 */
                PrimaryKey validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_CONTENT_REQUEST_UPDATED);
                StandardMessage validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * print the message
                 */
                validation.printBufferWithIconHTML();
            }
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect the user to the content request detail page.
         * otherwise, rollback the current transaction
         */
        if (this.getIsValid()) {
            /*
             * redirect to the current, parent image or the content request
             * detail page
             */
            if (actionRequest
                    .equals(ContentRequestUtil.BUTTON_DETECTOR_APPROVE_VALUE)) {
                if ((currentRequestTypePk.equals(editTypePk))
                        && (currentContentTypePk.equals(imageTypePk))) {
                    String redirectURL = parentArticle.getAbsoluteLocation();
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                } else if (currentRequestTypePk.equals(addTypePk)) {
                    String redirectURL = parentArticle.getAbsoluteLocation();
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                } else if (currentRequestTypePk.equals(removeTypePk)) {
                    String redirectURL = parentArticle.getAbsoluteLocation();
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                } else {
                    /*
                     * get the article being changed
                     */
                    PrimaryKey articlePk = this.contentRequest
                            .getChildContentPrimaryKey();
                    StandardArticle currentArticle = (StandardArticle) sartf
                            .findByPrimaryKey(articlePk);

                    /*
                     * force a reload as the friendly address might have changed
                     */
                    currentArticle.load();

                    /*
                     * redirect
                     */
                    String redirectURL = currentArticle.getAbsoluteLocation();
                    ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                            .commitAndSendAbsoluteRedirect(redirectURL);
                }
            } else {
                String redirectURL = this.contentRequest.getAbsoluteLocation();

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
            }
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }
    }

    /**
     * Builds the layout of the form for a record to view
     * 
     * 
     * 
     */
    protected void buildViewRecord() {
        /*
         * print only in view mode
         */
        if (this.contentRequest != null) {
            /*
             * verify the status
             */
            if ((this.getAccessType().equals(AccessTypeConstants.VIEW))
                    && (!(this.contentRequest.getIsAccessible()))) {
                /*
                 * get the factories of the instances used in this procedure
                 */
                FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
                StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                        .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);

                /*
                 * get the error message
                 */
                PrimaryKey cannotAccessContentRequestPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_ACCESS_CONTENT_REQUEST);
                StandardFormMessage cannotAccessContentRequestMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(cannotAccessContentRequestPk);

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
                this.addStandardFormObject(cannotAccessContentRequestMessage,
                        1, 1);

            } else {
                this.build();
            }
        } else {
            this.buildNoRecordFound();
        }
    }

    /**
     * Builds the layout of the form for a record to edit.
     * 
     * 
     * 
     */
    protected void buildEditRecord() {
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
         * get all request types
         */
        PrimaryKey removeTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRequestTypesDictionary.REMOVE);

        /*
         * get all content types
         */
        PrimaryKey referenceTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.REFERENCE);

        /*
         * get the pending request status type
         */
        PrimaryKey pendingRequestStatusTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RequestStatusTypesDictionary.PENDING);

        /*
         * print only in edit mode if not closed
         */
        if (this.contentRequest != null) {
            /*
             * get the current request type and content type
             */
            PrimaryKey currentRequestTypePk = this.contentRequest
                    .getRequestTypePrimaryKey();
            PrimaryKey currentContentTypePk = this.contentRequest
                    .getContentTypePrimaryKey();

            /*
             * verify the status
             */
            if ((this.getAccessType().equals(AccessTypeConstants.EDIT))
                    && (!(this.contentRequest.getStatusTypePrimaryKey()
                            .equals(pendingRequestStatusTypePk)))) {
                /*
                 * get the factories of the instances used in this procedure
                 */
                FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
                StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                        .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);

                /*
                 * get the error message
                 */
                PrimaryKey cannotEditClosedRequestPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_EDIT_CLOSED_REQUEST);
                StandardFormMessage cannotEditClosedRequestMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(cannotEditClosedRequestPk);

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
                this
                        .addStandardFormObject(cannotEditClosedRequestMessage,
                                1, 1);
            } else if ((this.getAccessType().equals(AccessTypeConstants.EDIT))
                    && ((currentContentTypePk.equals(referenceTypePk)) || (currentRequestTypePk
                            .equals(removeTypePk)))) {
                /*
                 * get the factories of the instances used in this procedure
                 */
                FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
                StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                        .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);

                /*
                 * get the error message
                 */
                PrimaryKey nothingToUpdateInContentRequestPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_NOTHING_TO_UPDATE_IN_CONTENT_REQUEST);
                StandardFormMessage nothingToUpdateInContentRequestMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(nothingToUpdateInContentRequestPk);

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
                this.addStandardFormObject(
                        nothingToUpdateInContentRequestMessage, 1, 1);
            } else if ((this.getAccessType().equals(AccessTypeConstants.EDIT))
                    && (!(resm.getIsRequestEditingAuthorized(
                            sa.getPrimaryKey(), this.contentRequest
                                    .getPrimaryKey())))) {
                /*
                 * get the factories of the instances used in this procedure
                 */
                FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
                StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                        .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);

                /*
                 * get the error message
                 */
                PrimaryKey cannotUpdateContentRequestPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_UPDATE_CONTENT_REQUEST);
                StandardFormMessage cannotUpdateContentRequestMessage = (StandardFormMessage) sfmf
                        .findByPrimaryKey(cannotUpdateContentRequestPk);

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
                this.addStandardFormObject(cannotUpdateContentRequestMessage,
                        1, 1);
            } else {
                this.build();
            }
        } else {
            this.buildNoRecordFound();
        }
    }

    /**
     * Returns the content request viewed or edited by this form.
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardContentRequest object
     * @see #setStandardContentRequest
     */
    public StandardContentRequest getStandardContentRequest() {
        return this.contentRequest;
    }

    /**
     * Sets the content request viewed by this form.
     * 
     * @param srr
     *            standard contentRequest viewed by this form
     * @see #getStandardContentRequest
     */
    public void setStandardContentRequest(StandardContentRequest srr) {
        this.contentRequest = srr;
    }
}

// end AbstractContentRequestEntryBlock
