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

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.blocks.AbstractStandardEntryFormBlock;
import com.corendal.netapps.framework.core.constants.CanvasTypeConstants;
import com.corendal.netapps.framework.core.constants.LayoutTypeConstants;
import com.corendal.netapps.framework.core.dictionaries.IconsDictionary;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormIconFactory;
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
import com.corendal.netapps.framework.core.interfaces.StandardFormIcon;
import com.corendal.netapps.framework.core.interfaces.StandardFormIconFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLabel;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentRequestTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.RequestStatusTypesDictionary;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.utils.ContentRequestUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;

/**
 * AbstractContentEntryBlock is the parent block common to all reference viewing
 * blocks.
 * 
 * @version $Id: AbstractMyContentApprovalsEntryBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractMyContentApprovalsEntryBlock extends
        AbstractStandardEntryFormBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractMyContentApprovalsEntryBlock";

    /** Id of the content selected by the user */
    private String[] requestIds;

    /**
     * Default class constructor this procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead
     */
    protected AbstractMyContentApprovalsEntryBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardEntryFormBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractMyContentApprovalsEntryBlock) super.clone();
    }

    /**
     * Builds the layout of the form. This procedure is used when removing a
     * content.
     * 
     * 
     * 
     */
    protected void buildApproveEditRecord() {
        /*
         * get a list of content requests for the user
         */
        List contents = getContentRequestsFound();

        /*
         * check whether contents could be found
         */
        if ((contents != null) && (contents.size() > 0)) {
            /*
             * one or more requests found, process it.
             */
            this.buildContentRequestsFound(contents);
        } else {
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
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
            StandardEntity entity = (StandardEntity) sef
                    .findByPrimaryKey(contentsPk);
            this.setStandardEntity(entity);

            /*
             * no records are found
             */
            this.buildNoRecordFound();
        }
    }

    /**
     * Returns the list of content requests to print.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    private List<StandardContentRequest> getContentRequestsFound() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentRequestFactory srf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);

        /*
         * find the approver being viewed
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        StandardAccount sa = am.getProxyStandardAccount();
        PrimaryKey approverPk = sa.getPrimaryKey();

        /*
         * build the list of content requests
         */
        List<StandardContentRequest> contentRequestsFound;

        if (approverPk != null) {
            contentRequestsFound = srf
                    .findEnabledByApproverPrimaryKey(approverPk);
        } else {
            contentRequestsFound = new ArrayList<StandardContentRequest>();
        }

        /*
         * return
         */
        return contentRequestsFound;
    }

    /**
     * Lists all content requests found
     * 
     * @param contents
     *            content requests to print
     * 
     * 
     */
    private void buildContentRequestsFound(List contents) {
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
        StandardFormIconFactory sfif = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);
        StandardFormIconFactory sficonf = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

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
         * set the layout type to multiple to provide a tabbing where the submit
         * button is the last object to reach
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
        // 1 line

        /*
         * get the primary keys of the required instances
         */
        PrimaryKey entityPk = entity.getPrimaryKey();
        PrimaryKey nameFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.CONTENT_PATH);
        PrimaryKey nameLabelPk = seflf.getPrimaryKey(entityPk, nameFieldPk);

        /*
         * instantiate all entry fields
         */
        StandardEntryFormField nameField = (StandardEntryFormField) sefff
                .findByPrimaryKey(nameFieldPk);
        nameField.setCheckBoxType();

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
            StandardContentRequest currentRequest = (StandardContentRequest) contents
                    .get(i);
            PrimaryKey currentRequestPk = currentRequest.getPrimaryKey();

            /*
             * verify that the content still exists
             */
            if ((currentRequest.getIsFound())
                    && (currentRequest.getIsVisible())) {
                /*
                 * clone the field and modify the field clone
                 */
                StandardEntryFormField nameFieldClone = (StandardEntryFormField) nameField
                        .clone();
                nameFieldClone.setOptionValueText(currentRequestPk.toString());

                /*
                 * get the preview icon
                 */
                PrimaryKey previewIconPk = currentRequest
                        .getPreviewIconPrimaryKey();
                StandardFormIcon previewFormIcon = (StandardFormIcon) sfif
                        .findByPrimaryKey(previewIconPk);

                /*
                 * clone the label and modify the name of the label
                 */
                StandardEntryFormLabel nameLabelClone = (StandardEntryFormLabel) nameLabel
                        .clone();
                StandardLabel sl3 = nameLabelClone.getStandardLabel();

                /*
                 * modify the short description of the label
                 */
                String myShortDescriptionText = currentRequest
                        .getShortDescriptionText();
                String myShortDescriptionEncoded = currentRequest
                        .getShortDescriptionEncoded();
                String myShortDescriptionHTML = currentRequest
                        .getShortDescriptionHTML();

                sl3.setCurrentShortDescriptionText(myShortDescriptionText);
                sl3
                        .setCurrentShortDescriptionEncoded(myShortDescriptionEncoded);
                sl3.setCurrentShortDescriptionHTML(myShortDescriptionHTML);

                /*
                 * set the name of each label
                 */
                String image = lw.getBreakableLinkHTML(currentRequest
                        .getDefaultLocation(), null, currentRequest
                        .getNameHTML(), null);

                String myNameText = currentRequest.getNameText();
                String myNameHTML = ConcatUtil.getConcatWithColumn(myNameText,
                        myShortDescriptionText, image, myShortDescriptionHTML);

                sl3.setCurrentNameText(currentRequest.getNameText());
                sl3.setCurrentNameEncoded(currentRequest.getNameEncoded());
                sl3.setCurrentNameHTML(myNameHTML);

                /*
                 * associate the name field with its label
                 */
                nameLabelClone.associateStandardFormField(nameFieldClone);

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

        PrimaryKey sendApproveRequestPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ButtonsDictionary.APPROVE_REQUEST);
        StandardEntryFormButton sendApproveRequest = (StandardEntryFormButton) sefbf
                .findByPrimaryKey(sendApproveRequestPk);
        this.addStandardFormObject(sendApproveRequest, 1, 1);

        /*
         * extract the data entries from the form for validation
         */
        this.requestIds = nameField.getRequestValues();

        /*
         * set the counts of this block
         */
        this.wrapUp();
    }

    /**
     * Prints the validation specific to this form.
     * 
     * 
     * 
     * @throws IOException
     *             when a redirect occurs
     */
    public void validateExtraApprovalRequest() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);

        /*
         * get all content types
         */
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
        PrimaryKey imageTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);
        PrimaryKey referenceTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.REFERENCE);

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
         * instantiate the parent article
         */
        StandardArticle parentArticle = null;

        /*
         * validate each approval request
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.requestIds.length; i++) {
                if (this.getIsValid()) {
                    String requestId = this.requestIds[i];

                    if (StringUtil.getIsEmpty(requestId)) {
                        this.setIsValid(false);

                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_CONTENT_TO_ADD);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);
                        error.printBufferWithIconHTML();
                    } else {
                        PrimaryKey requestPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(requestId);
                        StandardContentRequest contentRequest = (StandardContentRequest) srrf
                                .findByPrimaryKey(requestPk);

                        if (!(contentRequest.getIsFound())) {
                            this.setIsValid(false);

                            PrimaryKey errorPk = PrimaryKeyUtil
                                    .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_CONTENT_TO_ADD);
                            StandardMessage error = (StandardMessage) smf
                                    .findByPrimaryKey(errorPk);
                            error.printBufferWithIconHTML();
                        }

                        // end if
                    }

                    // end if-else
                }

                // end inner this.getIsValid()
            }

            // end for
        }

        // end if( this.getIsValid())

        /*
         * if there are article removal requests, verify that these articles are
         * empty
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.requestIds.length; i++) {
                if (this.getIsValid()) {
                    String requestId = this.requestIds[i];

                    /*
                     * get the content request
                     */
                    PrimaryKey currentContentRequestPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(requestId);
                    StandardContentRequest currentContentRequest = (StandardContentRequest) srrf
                            .findByPrimaryKey(currentContentRequestPk);

                    /*
                     * verify the article size
                     */
                    if (currentContentRequest.getContentTypePrimaryKey()
                            .equals(articleTypePk)) {
                        if (currentContentRequest.getRequestTypePrimaryKey()
                                .equals(removeTypePk)) {
                            PrimaryKey articlePk = currentContentRequest
                                    .getChildContentPrimaryKey();
                            List<StandardContent> children = srf
                                    .findEnabledByParentArticlePrimaryKey(articlePk);
                            this.setIsValid(children.size() == 0);

                            if (!(children.size() == 0)) {
                                PrimaryKey errorPk = PrimaryKeyUtil
                                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CONTENT_REQUEST_EMPTY_ARTICLE_ISSUE);
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
         * if there are article add or edit requests, verify that the friendly
         * addresses are unique
         */
        if (this.getIsValid()) {
            List<String> friendlyAddresses = new ArrayList<String>();

            for (int i = 0; i < this.requestIds.length; i++) {
                if (this.getIsValid()) {
                    String requestId = this.requestIds[i];

                    /*
                     * get the content request
                     */
                    PrimaryKey currentContentRequestPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(requestId);
                    StandardContentRequest currentContentRequest = (StandardContentRequest) srrf
                            .findByPrimaryKey(currentContentRequestPk);

                    /*
                     * verify the friendly address for existing records
                     */
                    if (currentContentRequest.getContentTypePrimaryKey()
                            .equals(articleTypePk)) {
                        if (!(StringUtil.getIsEmpty(currentContentRequest
                                .getFriendlyAddress()))) {
                            StandardArticle friendlyArticle = sdf
                                    .findByFriendlyAddress(currentContentRequest
                                            .getFriendlyAddress());

                            if (friendlyArticle.getIsFound()) {
                                if (currentContentRequest
                                        .getRequestTypePrimaryKey().equals(
                                                addTypePk)) {
                                    this.setIsValid(false);

                                    PrimaryKey errorPk = PrimaryKeyUtil
                                            .getAlphanumericSingleKey(MessagesDictionary.ERR_CONTENT_REQUEST_FRIENDLY_ADDRESS_ISSUE);
                                    StandardMessage error = (StandardMessage) smf
                                            .findByPrimaryKey(errorPk);
                                    error.printBufferWithIconHTML();
                                } else if (currentContentRequest
                                        .getRequestTypePrimaryKey().equals(
                                                editTypePk)) {
                                    PrimaryKey articlePk = currentContentRequest
                                            .getChildContentPrimaryKey();

                                    if (!(articlePk.equals(friendlyArticle
                                            .getPrimaryKey()))) {
                                        this.setIsValid(false);

                                        PrimaryKey errorPk = PrimaryKeyUtil
                                                .getAlphanumericSingleKey(MessagesDictionary.ERR_CONTENT_REQUEST_FRIENDLY_ADDRESS_ISSUE);
                                        StandardMessage error = (StandardMessage) smf
                                                .findByPrimaryKey(errorPk);
                                        error.printBufferWithIconHTML();
                                    }
                                }
                            }
                        }
                    }

                    /*
                     * verify the friendly address for previous content requests
                     */
                    if (currentContentRequest.getContentTypePrimaryKey()
                            .equals(articleTypePk)) {
                        if (!(StringUtil.getIsEmpty(currentContentRequest
                                .getFriendlyAddress()))) {
                            if (friendlyAddresses
                                    .contains(currentContentRequest
                                            .getFriendlyAddress())) {
                                this.setIsValid(false);

                                PrimaryKey errorPk = PrimaryKeyUtil
                                        .getAlphanumericSingleKey(MessagesDictionary.ERR_CONTENT_REQUEST_FRIENDLY_ADDRESS_ISSUE);
                                StandardMessage error = (StandardMessage) smf
                                        .findByPrimaryKey(errorPk);
                                error.printBufferWithIconHTML();
                            } else {
                                friendlyAddresses.add(currentContentRequest
                                        .getFriendlyAddress());
                            }
                        }
                    }
                }
            }
        }

        /*
         * verify that each content request is pending approval
         */
        if (this.getIsValid()) {
            PrimaryKey pendingPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RequestStatusTypesDictionary.PENDING);

            for (int i = 0; i < this.requestIds.length; i++) {
                if (this.getIsValid()) {
                    String requestId = this.requestIds[i];

                    /*
                     * get the content request
                     */
                    PrimaryKey currentContentRequestPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(requestId);
                    StandardContentRequest currentContentRequest = (StandardContentRequest) srrf
                            .findByPrimaryKey(currentContentRequestPk);

                    /*
                     * verify the status
                     */
                    PrimaryKey statusPk = currentContentRequest
                            .getStatusTypePrimaryKey();

                    if ((statusPk == null) || (!(statusPk.equals(pendingPk)))) {
                        this.setIsValid(false);

                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_CONTENT_REQUEST_STATUS_CHANGED);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);
                        error.printBufferWithIconHTML();
                    }
                }
            }
        }

        /*
         * approve each request
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.requestIds.length; i++) {
                if (this.getIsValid()) {
                    String requestId = this.requestIds[i];

                    /*
                     * get the content request
                     */
                    PrimaryKey currentContentRequestPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(requestId);
                    StandardContentRequest currentContentRequest = (StandardContentRequest) srrf
                            .findByPrimaryKey(currentContentRequestPk);

                    /*
                     * get parent article if exists
                     */
                    if (currentContentRequest.getParentContentPrimaryKey() != null) {
                        parentArticle = (StandardArticle) sdf
                                .findByPrimaryKey(currentContentRequest
                                        .getParentContentPrimaryKey());
                    }

                    /*
                     * get the content type
                     */
                    PrimaryKey currentContentType = currentContentRequest
                            .getContentTypePrimaryKey();

                    /*
                     * instantiate the content parent article
                     */
                    if (currentContentRequest.getParentContentPrimaryKey() != null) {
                        parentArticle = (StandardArticle) sdf
                                .findByPrimaryKey(currentContentRequest
                                        .getParentContentPrimaryKey());
                    }

                    if (currentContentType.equals(referenceTypePk)) {
                        StandardContent currentContent = (StandardContent) srf
                                .findByPrimaryKey(currentContentRequest
                                        .getChildContentPrimaryKey());

                        /*
                         * approve the reference request for this content
                         */
                        currentContent.approveNewReferenceRequest(parentArticle
                                .getPrimaryKey(), currentContentRequest);

                        this.setIsValid(currentContent.getIsDone());

                        if (!(currentContent.getIsDone())) {
                            MessageUtil.printBufferInternalErrorHTML(
                                    ABSTRACT_CLASS_NAME, currentContentRequest
                                            .getStoreTrace());
                        }
                    } else if (currentContentType.equals(articleTypePk)) {
                        /*
                         * approve the article add request
                         */
                        currentContentRequest.approveArticleRequest();
                        this.setIsValid(currentContentRequest.getIsDone());

                        if (!(currentContentRequest.getIsDone())) {
                            MessageUtil.printBufferInternalErrorHTML(
                                    ABSTRACT_CLASS_NAME, currentContentRequest
                                            .getStoreTrace());
                        }
                    } else if (currentContentType.equals(imageTypePk)) {
                        currentContentRequest.approveImageRequest();
                        this.setIsValid(currentContentRequest.getIsDone());

                        if (!(currentContentRequest.getIsDone())) {
                            MessageUtil.printBufferInternalErrorHTML(
                                    ABSTRACT_CLASS_NAME, currentContentRequest
                                            .getStoreTrace());
                        }
                    } // end if

                    // end if approve requests
                }

                // end if this.getIsValid()
            }

            // end for-loop
        }

        // end if(this.getIsValid())

        /*
         * try to create an email
         */
        if (this.getIsValid()) {
            String requestId;

            /*
             * create an email for each request
             */
            for (int i = 0; i < this.requestIds.length; i++) {
                requestId = this.requestIds[i];

                /*
                 * get the content request and the requested content
                 */
                PrimaryKey currentContentRequestPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(requestId);
                StandardContentRequest currentContentRequest = (StandardContentRequest) srrf
                        .findByPrimaryKey(currentContentRequestPk);

                /*
                 * send the email
                 */
                ContentRequestUtil.sendDisposition(currentContentRequest,
                        ContentRequestUtil.BUTTON_DETECTOR_APPROVE_VALUE);
            }

            // end for-loop
        }

        // end if this.isValid()

        /*
         * print the acknowledgment information
         */
        if (this.getIsValid()) {
            StandardMessage validation = null;
            PrimaryKey validationPk = null;

            if (this.requestIds.length == 1) {
                validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_SINGLE_CONTENT_REQUEST_APPROVED);
                validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * get the content request and the requested content
                 */
                String requestId = this.requestIds[0];
                PrimaryKey currentContentRequestPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(requestId);
                StandardContentRequest currentContentRequest = (StandardContentRequest) srrf
                        .findByPrimaryKey(currentContentRequestPk);

                /*
                 * indicate the name of content created
                 */
                String currentContentNameText = currentContentRequest
                        .getNameText();
                String currentContentNameEncoded = currentContentRequest
                        .getNameEncoded();
                String currentContentHTML = currentContentRequest.getNameHTML();

                validation.replaceMessageText(currentContentNameText, 1);
                validation.replaceMessageEncoded(currentContentNameEncoded, 1);
                validation.replaceMessageHTML(currentContentHTML, 1);
            } else {
                /*
                 * get the validation message
                 */
                validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_MULTIPLE_CONTENT_REQUEST_APPROVED);
                validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * indicate the number of content created
                 */
                String numberOfRequestsApproved = String
                        .valueOf(this.requestIds.length);
                validation.replaceMessageText(numberOfRequestsApproved, 1);
                validation.replaceMessageEncoded(numberOfRequestsApproved, 1);
                validation.replaceMessageHTML(numberOfRequestsApproved, 1);
            }

            /*
             * print the message
             */
            validation.printBufferWithIconHTML();
        }

        /*
         * if everything went ok, store the validation message in the validation
         * buffer and redirect the user to the content request detail page.
         * otherwise, rollback the current transaction
         */
        if (this.getIsValid()) {
            ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                    .commitAndSendAbsoluteRedirect(this.getRedirectURL());
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }
    }

    /**
     * If there is only one content request return the URL of the direct the
     * requested content belongs to. If there are more than one content request,
     * returns the URL for the current page.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    private String getRedirectURL() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get all content types
         */
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        /*
         * get all request types
         */
        PrimaryKey editTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRequestTypesDictionary.EDIT);

        /*
         * redirect to the appropriate article when only one request approved
         */
        if (this.requestIds.length == 1) {
            /*
             * get the content request
             */
            String requestId = this.requestIds[0];
            PrimaryKey currentContentRequestPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(requestId);
            StandardContentRequest currentContentRequest = (StandardContentRequest) srrf
                    .findByPrimaryKey(currentContentRequestPk);

            /*
             * get the request type and content type
             */
            PrimaryKey currentRequestType = currentContentRequest
                    .getRequestTypePrimaryKey();
            PrimaryKey currentContentType = currentContentRequest
                    .getContentTypePrimaryKey();

            /*
             * do the redirect
             */
            if ((currentRequestType.equals(editTypePk))
                    && (currentContentType.equals(articleTypePk))) {
                /*
                 * get the article being changed
                 */
                PrimaryKey articlePk = currentContentRequest
                        .getChildContentPrimaryKey();
                StandardArticle currentArticle = (StandardArticle) sdf
                        .findByPrimaryKey(articlePk);

                /*
                 * force a reload as the friendly address might have changed
                 */
                currentArticle.load();

                /*
                 * redirect
                 */
                return currentArticle.getAbsoluteLocation();
            } else {
                /*
                 * get the article being changed
                 */
                PrimaryKey articlePk = currentContentRequest
                        .getParentContentPrimaryKey();
                StandardArticle parentArticle = (StandardArticle) sdf
                        .findByPrimaryKey(articlePk);

                /*
                 * redirect
                 */
                return parentArticle.getAbsoluteLocation();
            }
        } else {
            /*
             * set the redirect URL to reprint the same page again.
             */
            return rm.getStandardPage().getAbsoluteLocation();
        }
    }
}

// end AbstractContentEntryBlock
