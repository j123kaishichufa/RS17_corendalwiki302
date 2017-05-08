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
package com.corendal.netapps.wiki.utils;

import java.util.List;

import com.corendal.netapps.framework.core.dictionaries.RolesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.ConfigManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardEmail;
import com.corendal.netapps.framework.core.interfaces.StandardEmailFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardEmailFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentRequestTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;

/**
 * ContentRequestUtil is the class for all content request utilities.
 * 
 * @version $Id: ContentRequestUtil.java,v 1.11 2007/10/17 21:29:10 tdanard Exp $
 */
public final class ContentRequestUtil {
    /** Value of the hidden parameter when no button was pressed. */
    public static final String BUTTON_DETECTOR_NONE_VALUE = "NONE";

    /** Value of the hidden parameter when approve was pressed. */
    public static final String BUTTON_DETECTOR_APPROVE_VALUE = "APPROVE";

    /** Value of the hidden parameter when reject was pressed. */
    public static final String BUTTON_DETECTOR_REJECT_VALUE = "REJECT";

    /** Value of the hidden parameter when cancel was pressed. */
    public static final String BUTTON_DETECTOR_CANCEL_VALUE = "CANCEL";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static methods should be used explicitly.
     */
    private ContentRequestUtil() {
        // this class contains only static methods
    }

    /**
     * Sends an email notifying the appropriate recipients of the disposition of
     * a content request.
     * 
     * @param contentRequest
     *            content request being dispositionned
     * @param actionRequest
     *            type of action (see button detector values)
     * 
     * 
     */
    public static void sendDisposition(StandardContentRequest contentRequest,
            String actionRequest) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEmailFactory semf = (StandardEmailFactory) pfs
                .getStandardBeanFactory(DefaultStandardEmailFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ConfigManager cm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the current account
         */
        StandardAccount currentAccount = am.getProxyStandardAccount();

        /*
         * get the property set
         */
        StandardPropertySet prop = cm.getStandardPropertySet();

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

        /*
         * get the current request type and content type
         */
        PrimaryKey currentRequestType = contentRequest
                .getRequestTypePrimaryKey();
        PrimaryKey currentContentType = contentRequest
                .getContentTypePrimaryKey();

        /*
         * instantiate the parent article
         */
        StandardArticle parentArticle = null;

        if (contentRequest.getParentContentPrimaryKey() != null) {
            parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(contentRequest
                            .getParentContentPrimaryKey());

            /*
             * try to create an email
             */
            StandardEmail email = null;

            if (actionRequest.equals(BUTTON_DETECTOR_APPROVE_VALUE)) {
                PrimaryKey subjectMessagePk = null;
                PrimaryKey bodyMessagePk = null;

                if (currentRequestType.equals(addTypePk)) {
                    if (currentContentType.equals(imageTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_IMAGE_APPROVED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_IMAGE_APPROVED_BODY);
                    } else if (currentContentType.equals(articleTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_ARTICLE_APPROVED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_ARTICLE_APPROVED_BODY);
                    }
                } else if (currentRequestType.equals(editTypePk)) {
                    if (currentContentType.equals(imageTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_UPDATE_APPROVED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_UPDATE_APPROVED_BODY);
                    } else if (currentContentType.equals(articleTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_UPDATE_APPROVED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_UPDATE_APPROVED_BODY);
                    }
                } else if (currentRequestType.equals(removeTypePk)) {
                    if (currentContentType.equals(imageTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_REMOVAL_APPROVED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_REMOVAL_APPROVED_BODY);
                    } else if (currentContentType.equals(articleTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_REMOVAL_APPROVED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_REMOVAL_APPROVED_BODY);
                    }
                }

                StandardMessage subjectMessage = (StandardMessage) smf
                        .findByPrimaryKey(subjectMessagePk);
                StandardMessage bodyMessage = (StandardMessage) smf
                        .findByPrimaryKey(bodyMessagePk);

                String companyNameText = prop.getCompanyNameText();
                String applicationNameText = prop.getApplicationNameText();
                String contentNameText = contentRequest.getNameText();
                String contentDescriptionText = contentRequest
                        .getDescriptionText();
                String parentArticleNameText = parentArticle.getNameText();
                String currentAccountNameText = currentAccount
                        .getFullNameText();
                String requestLocationText = contentRequest
                        .getAbsoluteLocation();

                subjectMessage.replaceMessageText(contentNameText, 1);

                bodyMessage.replaceMessageText(companyNameText, 1);
                bodyMessage.replaceMessageText(applicationNameText, 2);
                bodyMessage.replaceMessageText(currentAccountNameText, 3);
                bodyMessage.replaceMessageText(contentNameText, 4);
                bodyMessage.replaceMessageText(parentArticleNameText, 5);
                bodyMessage.replaceMessageText(contentDescriptionText, 6);
                bodyMessage.replaceMessageText(parentArticle
                        .getAbsoluteLocation(), 7);
                bodyMessage.replaceMessageText(requestLocationText, 8);

                email = (StandardEmail) semf.create(subjectMessage
                        .getCurrentMessageText(), bodyMessage
                        .getCurrentMessageText());
            } else if (actionRequest.equals(BUTTON_DETECTOR_REJECT_VALUE)) {
                PrimaryKey subjectMessagePk = null;
                PrimaryKey bodyMessagePk = null;

                if (currentRequestType.equals(addTypePk)) {
                    if (currentContentType.equals(imageTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_IMAGE_REJECTED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_IMAGE_REJECTED_BODY);
                    } else if (currentContentType.equals(articleTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_ARTICLE_REJECTED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_ARTICLE_REJECTED_BODY);
                    }
                } else if (currentRequestType.equals(editTypePk)) {
                    if (currentContentType.equals(imageTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_UPDATE_REJECTED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_UPDATE_REJECTED_BODY);
                    } else if (currentContentType.equals(articleTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_UPDATE_REJECTED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_UPDATE_REJECTED_BODY);
                    }
                } else if (currentRequestType.equals(removeTypePk)) {
                    if (currentContentType.equals(imageTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_REMOVAL_REJECTED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_REMOVAL_REJECTED_BODY);
                    } else if (currentContentType.equals(articleTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_REMOVAL_REJECTED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_REMOVAL_REJECTED_BODY);
                    }
                }

                StandardMessage subjectMessage = (StandardMessage) smf
                        .findByPrimaryKey(subjectMessagePk);
                StandardMessage bodyMessage = (StandardMessage) smf
                        .findByPrimaryKey(bodyMessagePk);

                String companyNameText = prop.getCompanyNameText();
                String applicationNameText = prop.getApplicationNameText();
                String contentNameText = contentRequest.getNameText();
                String contentDescriptionText = contentRequest
                        .getDescriptionText();
                String parentArticleNameText = parentArticle.getNameText();
                String currentAccountNameText = currentAccount
                        .getFullNameText();
                String requestLocationText = contentRequest
                        .getAbsoluteLocation();

                subjectMessage.replaceMessageText(contentNameText, 1);

                bodyMessage.replaceMessageText(companyNameText, 1);
                bodyMessage.replaceMessageText(applicationNameText, 2);
                bodyMessage.replaceMessageText(currentAccountNameText, 3);
                bodyMessage.replaceMessageText(contentNameText, 4);
                bodyMessage.replaceMessageText(parentArticleNameText, 5);
                bodyMessage.replaceMessageText(contentDescriptionText, 6);
                bodyMessage.replaceMessageText(requestLocationText, 7);

                email = (StandardEmail) semf.create(subjectMessage
                        .getCurrentMessageText(), bodyMessage
                        .getCurrentMessageText());
            } else if (actionRequest.equals(BUTTON_DETECTOR_CANCEL_VALUE)) {
                PrimaryKey subjectMessagePk = null;
                PrimaryKey bodyMessagePk = null;

                if (currentRequestType.equals(addTypePk)) {
                    if (currentContentType.equals(imageTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_IMAGE_CANCELLED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_IMAGE_CANCELLED_BODY);
                    } else if (currentContentType.equals(articleTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_ARTICLE_CANCELLED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEW_ARTICLE_CANCELLED_BODY);
                    }
                } else if (currentRequestType.equals(editTypePk)) {
                    if (currentContentType.equals(imageTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_UPDATE_CANCELLED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_UPDATE_CANCELLED_BODY);
                    } else if (currentContentType.equals(articleTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_UPDATE_CANCELLED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_UPDATE_CANCELLED_BODY);
                    }
                } else if (currentRequestType.equals(removeTypePk)) {
                    if (currentContentType.equals(imageTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_REMOVAL_CANCELLED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_IMAGE_REMOVAL_CANCELLED_BODY);
                    } else if (currentContentType.equals(articleTypePk)) {
                        subjectMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_REMOVAL_CANCELLED_SUBJECT);
                        bodyMessagePk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.DSP_ARTICLE_REMOVAL_CANCELLED_BODY);
                    }
                }

                StandardMessage subjectMessage = (StandardMessage) smf
                        .findByPrimaryKey(subjectMessagePk);
                StandardMessage bodyMessage = (StandardMessage) smf
                        .findByPrimaryKey(bodyMessagePk);

                String companyNameText = prop.getCompanyNameText();
                String applicationNameText = prop.getApplicationNameText();
                String contentNameText = contentRequest.getNameText();
                String contentDescriptionText = contentRequest
                        .getDescriptionText();
                String parentArticleNameText = parentArticle.getNameText();
                String requestorNameText = currentAccount.getFullNameText();
                String requestLocationText = contentRequest
                        .getAbsoluteLocation();

                subjectMessage.replaceMessageText(contentNameText, 1);

                bodyMessage.replaceMessageText(companyNameText, 1);
                bodyMessage.replaceMessageText(applicationNameText, 2);
                bodyMessage.replaceMessageText(requestorNameText, 3);
                bodyMessage.replaceMessageText(contentNameText, 4);
                bodyMessage.replaceMessageText(parentArticleNameText, 5);
                bodyMessage.replaceMessageText(contentDescriptionText, 6);
                bodyMessage.replaceMessageText(requestLocationText, 7);

                email = (StandardEmail) semf.create(subjectMessage
                        .getCurrentMessageText(), bodyMessage
                        .getCurrentMessageText());
            }

            /*
             * try to set the sender
             */
            if (email != null) {
                email.storeFromPrimaryKey(currentAccount.getPrimaryKey());
            }

            /*
             * try to add recipients
             */
            if (email != null) {
                PrimaryKey emailToPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(RolesDictionary.EMAIL_TO);
                PrimaryKey emailCcPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(RolesDictionary.EMAIL_CC);

                if (actionRequest.equals(BUTTON_DETECTOR_CANCEL_VALUE)) {
                    email.addRecipient(contentRequest
                            .getApproverStandardAccount().getPrimaryKey(),
                            emailToPk);

                    StandardAccount requestor = contentRequest
                            .getRequestorStandardAccount();

                    if (!(requestor.equals(currentAccount))) {
                        email
                                .addRecipient(requestor.getPrimaryKey(),
                                        emailCcPk);
                    }
                } else {
                    email.addRecipient(contentRequest
                            .getRequestorStandardAccount().getPrimaryKey(),
                            emailToPk);

                    StandardAccount approver = contentRequest
                            .getApproverStandardAccount();

                    if (!(approver.equals(currentAccount))) {
                        email.addRecipient(approver.getPrimaryKey(), emailCcPk);
                    }
                }

                email.addRecipient(currentAccount.getPrimaryKey(), emailCcPk);
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
     * Rejects all content requests related to a image primary key.
     * 
     * @param imagePk
     *            primary key of the image to parse
     * 
     * 
     */
    public static void rejectImageRelated(PrimaryKey imagePk) {
        ContentRequestUtil.rejectContentRelated(imagePk);
    }

    /**
     * Rejects all content requests related to an article primary key.
     * 
     * @param articlePk
     *            primary key of the article to parse
     * 
     * 
     */
    public static void rejectArticleRelated(PrimaryKey articlePk) {
        ContentRequestUtil.rejectContentRelated(articlePk);
    }

    /**
     * Rejects all content requests related to an article primary key. This
     * includes the content requests attached to the article itself and any
     * other request to add, edit or remove a content in this article.
     * 
     * @param articlePk
     *            primary key of the article to parse
     * 
     * 
     */
    public static void rejectContentRelated(PrimaryKey articlePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);

        /*
         * get the list of content requests pending for the current article
         */
        List<StandardContentRequest> contentRequests = srrf
                .findEnabledPendingByParentArticlePrimaryKey(articlePk);

        /*
         * reject each content request
         */
        for (StandardContentRequest currentRequest : contentRequests) {
            currentRequest.storeRejection();
            ContentRequestUtil.sendDisposition(currentRequest,
                    ContentRequestUtil.BUTTON_DETECTOR_REJECT_VALUE);
        }
    }
}
