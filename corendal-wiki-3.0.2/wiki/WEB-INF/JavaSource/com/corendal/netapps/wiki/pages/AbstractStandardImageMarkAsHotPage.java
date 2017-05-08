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
package com.corendal.netapps.wiki.pages;

import java.io.IOException;
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.dictionaries.RolesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.BufferManager;
import com.corendal.netapps.framework.core.interfaces.ConfigManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardEmail;
import com.corendal.netapps.framework.core.interfaces.StandardEmailFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultBufferManager;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardbeans.AbstractStandardPage;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardEmailFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionModesDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.interfaces.StandardImageMarkAsHotPage;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractStandardImageMarkAsHotPage is the abstract class handling information
 * about each image download page of the application.
 * 
 * @version $Id: AbstractStandardImageMarkAsHotPage.java,v 1.1 2005/09/06
 *          21:25:33 tdanard Exp $
 */
public abstract class AbstractStandardImageMarkAsHotPage extends
        AbstractStandardPage implements Cloneable, StandardImageMarkAsHotPage {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractStandardImageMarkAsHotPage() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardPage.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardImageMarkAsHotPage) super.clone();
    }

    /**
     * Returns the primary key of the record of this image mark as hot download
     * page.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public PrimaryKey getRecordPrimaryKey() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * return
         */
        String imageId = rm.getParameter(this.getRecordPrimaryDataParameter());

        return PrimaryKeyUtil.getAlphanumericSingleKey(imageId);
    }

    /**
     * Returns the primary data parameter of the record of this image mark as
     * hot page.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public String getRecordPrimaryDataParameter() {
        return HTTPParameterConstants.IMAGE_ID;
    }

    /**
     * Returns the standard image request to be viewed.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardImage object
     */
    public StandardImage getViewedImage() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardImageFactory sdocf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);

        /*
         * get the image
         */
        StandardImage image = (StandardImage) sdocf.findByPrimaryKey(this
                .getRecordPrimaryKey());

        /*
         * return
         */
        if ((image.getIsFound()) && (image.getIsVisible())) {
            return image;
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardPage#print(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void print() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEmailFactory semf = (StandardEmailFactory) pfs
                .getStandardBeanFactory(DefaultStandardEmailFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ConfigManager cm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);
        BufferManager bm = (BufferManager) pms
                .getManager(DefaultBufferManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * get the property set
         */
        StandardPropertySet prop = cm.getStandardPropertySet();

        /*
         * get the image viewed
         */
        StandardImage image = getViewedImage();

        /*
         * notify the digest-only subscribers
         */
        if (image != null) {
            /*
             * get the parent article
             */
            PrimaryKey parentArticlePk = image.getMainParentPrimaryKey();
            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(parentArticlePk);

            /*
             * verify that the user can mark the image
             */
            if (resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                    parentArticlePk)) {
                /*
                 * get the editor
                 */
                StandardAccount editor = parentArticle
                        .getEditorStandardAccount();

                /*
                 * get the list of digest subscribers not already notified
                 */
                PrimaryKey immediatePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.IMMEDIATE);
                List<PrimaryKey> immediateSubscribers = ContentUtil
                        .getRecursiveSubscribers(image.getPrimaryKey(),
                                immediatePk);
                List<PrimaryKey> allSubscribers = ContentUtil
                        .getRecursiveSubscribers(image.getPrimaryKey(), null);

                /*
                 * remove the immediate subscribers
                 */
                allSubscribers.removeAll(immediateSubscribers);

                /*
                 * get the messages
                 */
                PrimaryKey subjectMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_HOT_IMAGE_SUBSCRIPTION_SUBJECT);
                PrimaryKey bodyMessagePk = null;

                if (StringUtil.getIsEmpty(image.getComment())) {
                    bodyMessagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.DSP_HOT_IMAGE_SUBSCRIPTION_BODY);
                } else {
                    bodyMessagePk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.DSP_HOT_IMAGE_WITH_NOTES_SUBSCRIPTION_BODY);
                }

                StandardMessage subjectMessage = (StandardMessage) smf
                        .findByPrimaryKey(subjectMessagePk);
                StandardMessage bodyMessage = (StandardMessage) smf
                        .findByPrimaryKey(bodyMessagePk);

                /*
                 * get the values of the placeholders
                 */
                String companyNameText = prop.getCompanyNameText();
                String applicationNameText = prop.getApplicationNameText();
                String imageNameText = image.getNameText();
                String imageDescriptionText = image.getDescriptionText();
                String articleNameText = parentArticle.getNameText();
                String versionNumberText = String
                        .valueOf(image.getVersionNum());
                String imageLocationText = image.getAbsoluteLocation();
                String editorNameText = editor.getFullNameText();
                String imageUnsubscribeText = editorNameText;
                String versionNotesText = image.getComment();

                /*
                 * fill the messages
                 */
                subjectMessage.replaceMessageText(imageNameText, 1);

                bodyMessage.replaceMessageText(companyNameText, 1);
                bodyMessage.replaceMessageText(applicationNameText, 2);
                bodyMessage.replaceMessageText(editorNameText, 3);
                bodyMessage.replaceMessageText(imageNameText, 4);
                bodyMessage.replaceMessageText(articleNameText, 5);
                bodyMessage.replaceMessageText(versionNumberText, 6);
                bodyMessage.replaceMessageText(imageDescriptionText, 7);
                bodyMessage.replaceMessageText(imageLocationText, 8);
                bodyMessage.replaceMessageText(imageUnsubscribeText, 9);
                bodyMessage.replaceMessageText(versionNotesText, 10);

                /*
                 * create the email
                 */
                PrimaryKey emailToPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(RolesDictionary.EMAIL_TO);
                PrimaryKey emailCcPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(RolesDictionary.EMAIL_CC);
                StandardEmail email = (StandardEmail) semf.create(
                        subjectMessage.getCurrentMessageText(), bodyMessage
                                .getCurrentMessageText());

                /*
                 * add editor as sender and cc
                 */
                email.storeFromPrimaryKey(editor.getPrimaryKey());
                email.addRecipient(editor.getPrimaryKey(), emailCcPk);

                /*
                 * notify each subscriber
                 */
                int size = allSubscribers.size();

                for (int i = 0; i < size; i++) {
                    PrimaryKey currentSubscriberPk = allSubscribers.get(i);
                    email.addRecipient(currentSubscriberPk, emailToPk);
                }

                /*
                 * send the email
                 */
                if (size > 0) {
                    email.sendNow();
                }

                /*
                 * get the validation message
                 */
                if (size == 1) {
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_ONE_HOT_IMAGE_SUBSCRIPTION_SENT);
                    StandardMessage validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * print the message
                     */
                    bm.startPageMessage();
                    validation.printBufferWithIconHTML();
                    bm.endPageMessage();
                } else if (size > 1) {
                    PrimaryKey validationPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(MessagesDictionary.VLD_SEVERAL_HOT_IMAGE_SUBSCRIPTIONS_SENT);
                    StandardMessage validation = (StandardMessage) smf
                            .findByPrimaryKey(validationPk);

                    /*
                     * add the name of the image to the message
                     */
                    validation.replaceMessageText(String.valueOf(size), 1);
                    validation.replaceMessageEncoded(String.valueOf(size), 1);
                    validation.replaceMessageHTML(String.valueOf(size), 1);

                    /*
                     * print the message
                     */
                    bm.startPageMessage();
                    validation.printBufferWithIconHTML();
                    bm.endPageMessage();
                }
            }

            /*
             * redirect to the parent article
             */
            String redirectURL = parentArticle.getAbsoluteLocation();
            ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                    .commitAndSendAbsoluteRedirect(redirectURL);
        }
    }
}

// end AbstractStandardImageMarkAsHotPage
