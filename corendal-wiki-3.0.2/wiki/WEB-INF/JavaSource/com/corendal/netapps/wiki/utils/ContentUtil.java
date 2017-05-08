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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.CaseUtil;
import com.corendal.netapps.framework.configuration.utils.FileUtil;
import com.corendal.netapps.framework.configuration.utils.NoDoubleUtil;
import com.corendal.netapps.framework.configuration.utils.ReplaceUtil;
import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.ConfigManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardIcon;
import com.corendal.netapps.framework.core.interfaces.StandardIconFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.interfaces.StandardProxy;
import com.corendal.netapps.framework.core.interfaces.StandardProxyFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardIconFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.ExtensionUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardProxyFactory;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupMembership;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupMembershipFactory;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupMembershipFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentClassificationTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentRuleTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionModesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.IconsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.Parented;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationType;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentSubscriptionFactory;

/**
 * ContentUtil is the class for all content utilities.
 * 
 * @version $Id: ContentUtil.java,v 1.29 2007/10/17 21:29:11 tdanard Exp $
 */
public final class ContentUtil {
    /** Symbol between two articles */
    private static final String CONTENT_PATH_SYMBOL = " > ";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static methods should be used explicitly.
     */
    private ContentUtil() {
        // this class contains only static methods
    }

    /**
     * Returns the path of a content in the text format.
     * 
     * @param childContent
     *            content to use
     * @param nameText
     *            name of the content to use
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getPathText(Parented childContent,
            String nameText) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * return
         */
        PrimaryKey mainParentPrimaryKey = childContent
                .getMainParentPrimaryKey();

        if (mainParentPrimaryKey != null) {
            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(mainParentPrimaryKey);

            if (parentArticle.getIsFound()) {
                return parentArticle.getPathText() + CONTENT_PATH_SYMBOL
                        + nameText;
            } else {
                return nameText;
            }
        } else {
            return nameText;
        }
    }

    /**
     * Returns the path of a content in the Encoded format.
     * 
     * @param childContent
     *            content to use
     * @param nameEncoded
     *            name of the content to use
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getPathEncoded(Parented childContent,
            String nameEncoded) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * return
         */
        PrimaryKey mainParentPrimaryKey = childContent
                .getMainParentPrimaryKey();

        if (mainParentPrimaryKey != null) {
            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(mainParentPrimaryKey);

            if (parentArticle.getIsFound()) {
                return parentArticle.getPathEncoded()
                        + TextFormatUtil.getTextToEncoded(CONTENT_PATH_SYMBOL)
                        + nameEncoded;
            } else {
                return nameEncoded;
            }
        } else {
            return nameEncoded;
        }
    }

    /**
     * Returns the path of a content in the HTML format.
     * 
     * @param childContent
     *            content to use
     * @param nameHTML
     *            name of the content to use
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getPathHTML(Parented childContent,
            String nameHTML) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * return
         */
        PrimaryKey mainParentPrimaryKey = childContent
                .getMainParentPrimaryKey();

        if (mainParentPrimaryKey != null) {
            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(mainParentPrimaryKey);

            if (parentArticle.getIsFound()) {
                return parentArticle.getPathHTML()
                        + TextFormatUtil.getTextToHTML(CONTENT_PATH_SYMBOL)
                        + nameHTML;
            } else {
                return nameHTML;
            }
        } else {
            return nameHTML;
        }
    }

    /**
     * Returns the path of a content in the HTML format, with a link for each
     * parent article.
     * 
     * @param childContent
     *            content to use
     * @param nameHTML
     *            name of the content to use
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getPathHTMLWithLink(Parented childContent,
            String nameHTML) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * return
         */
        PrimaryKey mainParentPrimaryKey = childContent
                .getMainParentPrimaryKey();

        if (mainParentPrimaryKey != null) {
            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(mainParentPrimaryKey);

            if (parentArticle.getIsFound()) {
                return parentArticle.getPathHTMLWithLink()
                        + TextFormatUtil.getTextToHTML(CONTENT_PATH_SYMBOL)
                        + nameHTML;
            } else {
                return nameHTML;
            }
        } else {
            return nameHTML;
        }
    }

    /**
     * Adds the required info to a standard icon.
     * 
     * @param previewIcon
     *            icon to use as preview
     * @param classificationTypePk
     *            primary key of a classification type
     * @param iconFileName
     *            name of a icon file
     * @param articleSize
     *            formatted size of an article
     * @param iconSize
     *            formatted size of an icon
     * @param modified
     *            formatted modification date of a content
     * 
     * 
     * @return a com.corendal.netapps.framework.core.interfaces.StandardIcon object
     */
    public static StandardIcon getInformedPreviewIcon(StandardIcon previewIcon,
            PrimaryKey classificationTypePk, String iconFileName,
            String articleSize, String iconSize, String modified) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentClassificationTypeFactory srctf = (StandardContentClassificationTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentClassificationTypeFactory.class);

        /*
         * get the primary key of the "no login required" classification
         */
        PrimaryKey noLoginRequiredPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.NO_LOGIN_REQUIRED);

        /*
         * get the unknown primary keys
         */
        PrimaryKey directUnknownIconPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.DIRECT_UNKNOWN);
        PrimaryKey indirectUnknownIconPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.INDIRECT_UNKNOWN);
        PrimaryKey secureDirectUnknownIconPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SECURE_DIRECT_UNKNOWN);
        PrimaryKey secureIndirectUnknownIconPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SECURE_INDIRECT_UNKNOWN);

        /*
         * add the extension when the content extension is unknown
         */
        PrimaryKey currentIconPk = previewIcon.getPrimaryKey();

        if ((currentIconPk.equals(directUnknownIconPk))
                || (currentIconPk.equals(indirectUnknownIconPk))
                || (currentIconPk.equals(secureDirectUnknownIconPk))
                || (currentIconPk.equals(secureIndirectUnknownIconPk))) {
            if (!(StringUtil.getIsEmpty(iconFileName))) {
                String currentExtensionText = ExtensionUtil
                        .getExtensionByFileName(iconFileName);

                if (currentExtensionText != null) {
                    String currentExtensionEncoded = TextFormatUtil
                            .getTextToEncoded(currentExtensionText);
                    String currentExtensionHTML = TextFormatUtil
                            .getTextToHTML(currentExtensionText);

                    String iconTitleText = previewIcon.getTitleText();
                    String iconTitleEncoded = previewIcon.getTitleEncoded();
                    String iconTitleHTML = previewIcon.getTitleHTML();

                    String finalTitleText = ConcatUtil
                            .getConcatWithParentheses(iconTitleText,
                                    currentExtensionText, iconTitleText,
                                    currentExtensionText);
                    String finalTitleEncoded = ConcatUtil
                            .getConcatWithParentheses(iconTitleText,
                                    currentExtensionText, iconTitleEncoded,
                                    currentExtensionEncoded);
                    String finalTitleHTML = ConcatUtil
                            .getConcatWithParentheses(iconTitleText,
                                    currentExtensionText, iconTitleHTML,
                                    currentExtensionHTML);

                    previewIcon.setTitleText(finalTitleText);
                    previewIcon.setTitleEncoded(finalTitleEncoded);
                    previewIcon.setTitleHTML(finalTitleHTML);
                }
            }
        }

        /*
         * add the icon size when applicable
         */
        if (!(StringUtil.getIsEmpty(iconSize))) {
            String iconTitleText = previewIcon.getTitleText();
            String iconTitleEncoded = previewIcon.getTitleEncoded();
            String iconTitleHTML = previewIcon.getTitleHTML();

            int size = Integer.parseInt(iconSize);
            String sizeTitleText = FileUtil.getFormattedSizeText(size);
            String sizeTitleEncoded = TextFormatUtil
                    .getTextToEncoded(sizeTitleText);
            String sizeTitleHTML = TextFormatUtil.getTextToHTML(sizeTitleText);

            String finalTitleText = ConcatUtil.getConcatWithComma(
                    iconTitleText, sizeTitleText, iconTitleText, sizeTitleText);
            String finalTitleEncoded = ConcatUtil.getConcatWithComma(
                    iconTitleText, sizeTitleText, iconTitleEncoded,
                    sizeTitleEncoded);
            String finalTitleHTML = ConcatUtil.getConcatWithComma(
                    iconTitleText, sizeTitleText, iconTitleHTML, sizeTitleHTML);

            previewIcon.setTitleText(finalTitleText);
            previewIcon.setTitleEncoded(finalTitleEncoded);
            previewIcon.setTitleHTML(finalTitleHTML);
        }

        /*
         * add the icon last modified date when applicable
         */
        if (!(StringUtil.getIsEmpty(modified))) {
            String iconTitleText = previewIcon.getTitleText();
            String iconTitleEncoded = previewIcon.getTitleEncoded();
            String iconTitleHTML = previewIcon.getTitleHTML();

            Date modifiedDate = DateFormatUtil
                    .getParsedInternalFormattedDate(modified);
            String modificationDateText = DateFormatUtil
                    .getShortFormattedDateText(modifiedDate);
            String modificationDateEncoded = TextFormatUtil
                    .getTextToEncoded(modificationDateText);
            String modificationDateHTML = TextFormatUtil
                    .getTextToHTML(modificationDateText);

            String finalTitleText = ConcatUtil.getConcatWithComma(
                    iconTitleText, modificationDateText, iconTitleText,
                    modificationDateText);
            String finalTitleEncoded = ConcatUtil.getConcatWithComma(
                    iconTitleText, modificationDateText, iconTitleEncoded,
                    modificationDateEncoded);
            String finalTitleHTML = ConcatUtil.getConcatWithComma(
                    iconTitleText, modificationDateText, iconTitleHTML,
                    modificationDateHTML);

            previewIcon.setTitleText(finalTitleText);
            previewIcon.setTitleEncoded(finalTitleEncoded);
            previewIcon.setTitleHTML(finalTitleHTML);
        }

        /*
         * add the classification type
         */
        if (!(classificationTypePk.equals(noLoginRequiredPk))) {
            StandardContentClassificationType classificationType = (StandardContentClassificationType) srctf
                    .findByPrimaryKey(classificationTypePk);

            String iconTitleText = previewIcon.getTitleText();
            String iconTitleEncoded = previewIcon.getTitleEncoded();
            String iconTitleHTML = previewIcon.getTitleHTML();

            String classificationTypeNameText = classificationType
                    .getNameText();
            String classificationTypeNameEncoded = classificationType
                    .getNameEncoded();
            String classificationTypeNameHTML = classificationType
                    .getNameHTML();

            String finalTitleText = ConcatUtil.getConcatWithComma(
                    iconTitleText, classificationTypeNameText, iconTitleText,
                    classificationTypeNameText);
            String finalTitleEncoded = ConcatUtil.getConcatWithComma(
                    iconTitleText, classificationTypeNameText,
                    iconTitleEncoded, classificationTypeNameEncoded);
            String finalTitleHTML = ConcatUtil.getConcatWithComma(
                    iconTitleText, classificationTypeNameText, iconTitleHTML,
                    classificationTypeNameHTML);

            previewIcon.setTitleText(finalTitleText);
            previewIcon.setTitleEncoded(finalTitleEncoded);
            previewIcon.setTitleHTML(finalTitleHTML);
        }

        /*
         * return
         */
        return previewIcon;
    }

    /**
     * Returns the direct preview icon to use for a previewed content.
     * 
     * @param currentContent
     *            content to use
     * @param classificationTypePk
     *            primary key of a classification type
     * @param iconFileName
     *            name of a icon file
     * @param articleSize
     *            formatted size of an article
     * @param iconSize
     *            formatted size of an icon
     * @param modified
     *            formatted modification date of a content
     * 
     * 
     * @return a com.corendal.netapps.framework.core.interfaces.StandardIcon object
     */
    public static StandardIcon getDirectPreviewIcon(Searched currentContent,
            PrimaryKey classificationTypePk, String iconFileName,
            String articleSize, String iconSize, String modified) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);

        /*
         * get the primary key of the "login required" and "no login required"
         * classification
         */
        PrimaryKey noLoginRequiredPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.NO_LOGIN_REQUIRED);
        PrimaryKey loginRequiredPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.LOGIN_REQUIRED);

        /*
         * get the icon
         */
        StandardIcon previewIcon = null;

        if ((classificationTypePk.equals(noLoginRequiredPk))
                || (classificationTypePk.equals(loginRequiredPk))) {
            previewIcon = (StandardIcon) sif.findByPrimaryKey(currentContent
                    .getDirectPreviewIconPrimaryKey());
        } else {
            if (currentContent.getIsAccessible()) {
                previewIcon = (StandardIcon) sif
                        .findByPrimaryKey(currentContent
                                .getDirectPreviewIconPrimaryKey());
            } else {
                previewIcon = (StandardIcon) sif
                        .findByPrimaryKey(currentContent
                                .getSecureDirectPreviewIconPrimaryKey());
            }
        }

        /*
         * return
         */
        return ContentUtil.getInformedPreviewIcon(previewIcon,
                classificationTypePk, iconFileName, articleSize, iconSize,
                modified);
    }

    /**
     * Returns the indirect preview icon to use for a previewed content.
     * 
     * @param currentContent
     *            content to use
     * @param classificationTypePk
     *            primary key of a classification type
     * @param iconFileName
     *            name of a icon file
     * @param articleSize
     *            formatted size of an article
     * @param iconSize
     *            formatted size of an icon
     * @param modified
     *            formatted modification date of a content
     * 
     * 
     * @return a com.corendal.netapps.framework.core.interfaces.StandardIcon object
     */
    public static StandardIcon getIndirectPreviewIcon(Searched currentContent,
            PrimaryKey classificationTypePk, String iconFileName,
            String articleSize, String iconSize, String modified) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);

        /*
         * get the primary key of the "no login required" classification
         */
        PrimaryKey noLoginRequiredPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.NO_LOGIN_REQUIRED);
        PrimaryKey loginRequiredPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.LOGIN_REQUIRED);

        /*
         * get the icon
         */
        StandardIcon previewIcon = null;

        if ((classificationTypePk.equals(noLoginRequiredPk))
                || (classificationTypePk.equals(loginRequiredPk))) {
            previewIcon = (StandardIcon) sif.findByPrimaryKey(currentContent
                    .getIndirectPreviewIconPrimaryKey());
        } else {
            if (currentContent.getIsAccessible()) {
                previewIcon = (StandardIcon) sif
                        .findByPrimaryKey(currentContent
                                .getIndirectPreviewIconPrimaryKey());
            } else {
                previewIcon = (StandardIcon) sif
                        .findByPrimaryKey(currentContent
                                .getSecureIndirectPreviewIconPrimaryKey());
            }
        }

        /*
         * return
         */
        return ContentUtil.getInformedPreviewIcon(previewIcon,
                classificationTypePk, iconFileName, articleSize, iconSize,
                modified);
    }

    /**
     * Returns the preview icon to use for a content.
     * 
     * @param currentContent
     *            content to use
     * @param iconFileName
     *            name of a icon file
     * @param articleSize
     *            formatted size of an article
     * @param iconSize
     *            formatted size of an icon
     * @param modified
     *            formatted modification date of a content
     * 
     * 
     * @return a com.corendal.netapps.framework.core.interfaces.StandardIcon object
     */
    public static StandardIcon getPreviewIcon(StandardContent currentContent,
            String iconFileName, String articleSize, String iconSize,
            String modified) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get the requested article
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKeyOrNull(articleId);

        /*
         * do not use this requested article in the home page
         */
        StandardPage page = rm.getStandardPage();

        if (page != null) {
            PrimaryKey pagePk = page.getPrimaryKey();
            PrimaryKey homePagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.PagesDictionary.HOME);

            if (pagePk.equals(homePagePk)) {
                articlePk = null;
            }
        }

        /*
         * get the governing content
         */
        Searched classificationSearched = currentContent
                .getClassificationSearched();
        PrimaryKey classificationTypePk = classificationSearched
                .getClassificationTypePrimaryKey();

        /*
         * get the icon
         */
        StandardIcon previewIcon = null;

        if (articlePk != null) {
            if (currentContent.getIsDirectChildOf(articlePk)) {
                previewIcon = ContentUtil.getDirectPreviewIcon(currentContent,
                        classificationTypePk, iconFileName, articleSize,
                        iconSize, modified);
            } else {
                previewIcon = ContentUtil.getIndirectPreviewIcon(
                        currentContent, classificationTypePk, iconFileName,
                        articleSize, iconSize, modified);
            }
        } else {
            previewIcon = ContentUtil.getDirectPreviewIcon(currentContent,
                    classificationTypePk, iconFileName, articleSize, iconSize,
                    modified);
        }

        /*
         * return
         */
        return previewIcon;
    }

    /**
     * Returns the list of accounts that a requestor can add to a subscribers
     * list of a content. The requestor cannot be the editor.
     * 
     * @param requestor
     *            requestor
     * @param contentPk
     *            primary key of the content being subscribed to
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<StandardAccount> getAccountsLeftToSubscribeByRequestor(
            StandardAccount requestor, PrimaryKey contentPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardProxyFactory spxf = (StandardProxyFactory) pfs
                .getStandardBeanFactory(DefaultStandardProxyFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ConfigManager cfgm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);

        /*
         * get the application primary key
         */
        StandardPropertySet prop = cfgm.getStandardPropertySet();
        PrimaryKey applicationPk = prop.getPrimaryKey();

        /*
         * initialize the result
         */
        List<StandardAccount> result = new ArrayList<StandardAccount>();

        /*
         * get the associated content
         */
        StandardContent content = (StandardContent) srf
                .findByPrimaryKey(contentPk);

        /*
         * add the requestor
         */
        if (!(content.getIsDirectlySubscribedByIndividual(requestor
                .getPrimaryKey()))) {
            result.add(requestor);
        }

        /*
         * get the list of proxies
         */
        List<StandardAccount> grantors = new ArrayList<StandardAccount>();
        List<StandardProxy> proxies = spxf
                .findEnabledByApplicationAndGranteePrimaryKeys(applicationPk,
                        requestor.getPrimaryKey());

        for (StandardProxy currentProxy : proxies) {
            StandardAccount currentGrantor = currentProxy
                    .getGrantorStandardAccount();

            if (currentGrantor.getIsFound()) {
                if (!(content
                        .getIsDirectlySubscribedByIndividual(currentGrantor
                                .getPrimaryKey()))) {
                    grantors.add(currentGrantor);
                }
            }
        }

        Collections.sort(grantors);

        /*
         * merge the two lists
         */
        for (StandardAccount currentGrantor : grantors) {
            result.add(currentGrantor);
        }

        /*
         * return
         */
        return result;
    }

    /**
     * Returns the list of account that a requestor can remove from a
     * subscribers list of a content. The requestor cannot be the editor.
     * 
     * @param requestor
     *            requestor account
     * @param contentPk
     *            primary key of the content being subscribed to
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<StandardAccount> getAccountsLeftToUnsubscribeByRequestor(
            StandardAccount requestor, PrimaryKey contentPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardProxyFactory spxf = (StandardProxyFactory) pfs
                .getStandardBeanFactory(DefaultStandardProxyFactory.class);
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ConfigManager cfgm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);

        /*
         * get the application primary key
         */
        StandardPropertySet prop = cfgm.getStandardPropertySet();
        PrimaryKey applicationPk = prop.getPrimaryKey();

        /*
         * initialize the result
         */
        List<StandardAccount> result = new ArrayList<StandardAccount>();

        /*
         * get the associated content
         */
        StandardContent content = (StandardContent) srf
                .findByPrimaryKey(contentPk);

        /*
         * add the requestor
         */
        if (content.getIsDirectlySubscribedByIndividual(requestor
                .getPrimaryKey())) {
            result.add(requestor);
        }

        /*
         * add all proxies
         */
        List<StandardAccount> grantors = new ArrayList<StandardAccount>();
        List<StandardProxy> proxies = spxf
                .findEnabledByApplicationAndGranteePrimaryKeys(applicationPk,
                        requestor.getPrimaryKey());

        for (StandardProxy currentProxy : proxies) {
            StandardAccount currentGrantor = currentProxy
                    .getGrantorStandardAccount();

            if (currentGrantor.getIsFound()) {
                if (content.getIsDirectlySubscribedByIndividual(currentGrantor
                        .getPrimaryKey())) {
                    grantors.add(currentGrantor);
                }
            }
        }

        Collections.sort(grantors);

        /*
         * merge the two lists
         */
        for (StandardAccount currentGrantor : grantors) {
            result.add(currentGrantor);
        }

        /*
         * return
         */
        return result;
    }

    /**
     * Returns the list of accounts that an editor can remove from a account
     * subscribers list of a content.
     * 
     * @param contentPk
     *            primary key of the content being subscribed to
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<StandardAccount> getAccountsLeftToUnsubscribeByEditor(
            PrimaryKey contentPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);

        /*
         * get the list of accounts
         */
        List<StandardAccount> accountsLeftToUnsubscribe = new ArrayList<StandardAccount>();
        PrimaryKey individualTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.INDIVIDUAL_DISTRIBUTION);
        List<StandardContentSubscription> contentSubcriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(contentPk,
                        individualTypesPk);

        for (StandardContentSubscription contentSubscription : contentSubcriptions) {
            StandardAccount subscriber = contentSubscription
                    .getSubscriberStandardAccount();

            if ((subscriber != null) && (subscriber.getIsFound())) {
                accountsLeftToUnsubscribe.add(subscriber);
            }
        }

        Collections.sort(accountsLeftToUnsubscribe);

        /*
         * return
         */
        return accountsLeftToUnsubscribe;
    }

    /**
     * Returns the list of groups that an editor can remove from a group
     * subscribers list of a content.
     * 
     * @param contentPk
     *            primary key of the content being subscribed to
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<StandardGroup> getGroupsLeftToUnsubscribeByEditor(
            PrimaryKey contentPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);

        /*
         * get the list of groups
         */
        List<StandardGroup> groupsLeftToUnsubscribe = new ArrayList<StandardGroup>();
        PrimaryKey groupTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.GROUP_DISTRIBUTION);
        List<StandardContentSubscription> contentSubcriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(contentPk, groupTypesPk);

        for (StandardContentSubscription contentSubscription : contentSubcriptions) {
            StandardGroup subscriber = contentSubscription
                    .getSubscriberStandardGroup();

            if ((subscriber != null) && (subscriber.getIsFound())) {
                groupsLeftToUnsubscribe.add(subscriber);
            }
        }

        Collections.sort(groupsLeftToUnsubscribe);

        /*
         * return
         */
        return groupsLeftToUnsubscribe;
    }

    /**
     * Returns the content governing the classification of a content.
     * 
     * @param content
     *            content being checked
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.Searched object
     */
    public static Searched getClassificationSearched(Searched content) {
        /*
         * get the primary key of the "same as parent" classifications
         */
        PrimaryKey sameAsParentPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);

        /*
         * return
         */
        if (content.getClassificationTypePrimaryKey().equals(sameAsParentPk)) {
            PrimaryKey parentPrimaryKey = content.getMainParentPrimaryKey();

            if (parentPrimaryKey != null) {
                /*
                 * get the factories of the instances used in this procedure
                 */
                FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
                StandardArticleFactory sdf = (StandardArticleFactory) pfs
                        .getStandardBeanFactory(DefaultStandardArticleFactory.class);

                /*
                 * get the factories of the instances used in this procedure
                 */
                StandardArticle parentArticle = (StandardArticle) sdf
                        .findByPrimaryKey(parentPrimaryKey);

                if (parentArticle.getIsFound()) {
                    return ContentUtil.getClassificationSearched(parentArticle);
                } else {
                    return content;
                }
            } else {
                return content;
            }
        } else {
            return content;
        }
    }

    /**
     * Returns the content governing the rule of a content.
     * 
     * @param content
     *            content being checked
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.Searched object
     */
    public static Searched getRuleSearched(Searched content) {
        /*
         * get the primary key of the "same as parent" classifications
         */
        PrimaryKey sameAsParentPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRuleTypesDictionary.SAME_AS_PARENT);

        /*
         * return
         */
        if (content.getRuleTypePrimaryKey().equals(sameAsParentPk)) {
            PrimaryKey parentPrimaryKey = content.getMainParentPrimaryKey();

            if (parentPrimaryKey != null) {
                /*
                 * get the factories of the instances used in this procedure
                 */
                FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
                StandardArticleFactory sdf = (StandardArticleFactory) pfs
                        .getStandardBeanFactory(DefaultStandardArticleFactory.class);

                /*
                 * get the factories of the instances used in this procedure
                 */
                StandardArticle parentArticle = (StandardArticle) sdf
                        .findByPrimaryKey(parentPrimaryKey);

                if (parentArticle.getIsFound()) {
                    return ContentUtil.getRuleSearched(parentArticle);
                } else {
                    return content;
                }
            } else {
                return content;
            }
        } else {
            return content;
        }
    }

    /**
     * Filters the invisible contents out of a list.
     * 
     * @param contentsFound
     *            list of contents to filter
     * 
     * 
     * @return a java.util.List object
     */
    public static final List getFilteredContents(List contentsFound) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * check the global access rights
         */
        if ((sa != null)
                && (resm.getIsAllowedToReadAllContent(sa.getPrimaryKey()))) {
            return contentsFound;
        } else {
            List filteredContentsFound = new ArrayList<Object>();

            for (int i = 0; i < contentsFound.size(); i++) {
                if (contentsFound.get(i) instanceof Searched) {
                    Searched currentContent = (Searched) contentsFound.get(i);

                    if (currentContent.getIsVisible()) {
                        filteredContentsFound.add(currentContent);
                    }
                } else {
                    filteredContentsFound.add(contentsFound.get(i));
                }
            }

            return filteredContentsFound;
        }

    }

    /**
     * Returns the list of direct individual subscribers of a content. use a
     * null mode for all subscribers.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> getDirectIndividualSubscribers(
            PrimaryKey contentPk, PrimaryKey modePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);

        /*
         * initialize the result
         */
        List<PrimaryKey> recipients = new ArrayList<PrimaryKey>();

        /*
         * get the different modes
         */
        PrimaryKey individualModesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.INDIVIDUAL_DISTRIBUTION);

        /*
         * get the individual subscriptions
         */
        List<StandardContentSubscription> individualSubscriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(contentPk,
                        individualModesPk);

        /*
         * add the individual subscribers
         */
        for (/*
                 * get the current subscription
                 */
        StandardContentSubscription currentSubscription : individualSubscriptions) {

            /*
             * add the current subscriber
             */
            if ((modePk == null)
                    || (currentSubscription.getModePrimaryKey().equals(modePk))) {
                StandardAccount subscriber = currentSubscription
                        .getSubscriberStandardAccount();

                if ((subscriber != null) && (subscriber.getIsFound())) {
                    if (!(recipients.contains(subscriber.getPrimaryKey()))) {
                        recipients.add(subscriber.getPrimaryKey());
                    }
                }
            }
        }

        /*
         * return
         */
        return recipients;
    }

    /**
     * Returns the list of indirect individual subscribers of a content. use a
     * null mode for all subscribers.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> getIndirectIndividualSubscribers(
            PrimaryKey contentPk, PrimaryKey modePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * initialize the result
         */
        List<PrimaryKey> indirectRecipients = new ArrayList<PrimaryKey>();

        /*
         * verify that there is a parent article
         */
        PrimaryKey homePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ArticlesDictionary.HOME);

        if (!(contentPk.equals(homePk))) {
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(contentPk);

            if (content.getIsFound()) {
                /*
                 * verify that the content is an article or a icon
                 */
                PrimaryKey articleTypePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
                PrimaryKey iconTypePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);
                PrimaryKey typePk = content.getTypePrimaryKey();

                /*
                 * verify that the classification is identical
                 */
                if ((typePk.equals(articleTypePk))
                        || (typePk.equals(iconTypePk))) {
                    PrimaryKey sameAsParentPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);
                    PrimaryKey classificationTypePk = content
                            .getClassificationTypePrimaryKey();

                    if (classificationTypePk.equals(sameAsParentPk)) {
                        PrimaryKey parentContentPk = content
                                .getMainParentPrimaryKey();

                        /*
                         * get the subscribers of that article
                         */
                        indirectRecipients = ContentUtil
                                .getDirectIndividualSubscribers(
                                        parentContentPk, modePk);
                    }
                }
            }
        }

        /*
         * return
         */
        return indirectRecipients;
    }

    /**
     * Returns the list of individual subscribers of a content. use a null mode
     * for all subscribers.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> getRecursiveIndividualSubscribers(
            PrimaryKey contentPk, PrimaryKey modePk) {
        /*
         * get the list of direct subscribers
         */
        List<PrimaryKey> recipients = ContentUtil
                .getDirectIndividualSubscribers(contentPk, modePk);

        /*
         * add the subscribers of the parent article
         */
        List<PrimaryKey> indirectRecipients = ContentUtil
                .getIndirectIndividualSubscribers(contentPk, modePk);

        for (PrimaryKey currentRecipientPk : indirectRecipients) {

            if (!(recipients.contains(currentRecipientPk))) {
                recipients.add(currentRecipientPk);
            }
        }

        /*
         * return
         */
        return recipients;
    }

    /**
     * Returns the list of direct group subscribers of a content. use a null
     * mode for all subscribers.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> getDirectGroupSubscribers(
            PrimaryKey contentPk, PrimaryKey modePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardGroupMembershipFactory sgmf = (StandardGroupMembershipFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupMembershipFactory.class);

        /*
         * initialize the result
         */
        List<PrimaryKey> recipients = new ArrayList<PrimaryKey>();

        /*
         * get the different modes
         */
        PrimaryKey groupModesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.GROUP_DISTRIBUTION);

        /*
         * get the group subscriptions
         */
        List<StandardContentSubscription> groupSubscriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(contentPk, groupModesPk);

        /*
         * add the group subscribers
         */
        for (/*
                 * get the current subscription
                 */
        StandardContentSubscription currentSubscription : groupSubscriptions) {

            /*
             * add the current subscriber
             */
            if ((modePk == null)
                    || (currentSubscription.getModePrimaryKey().equals(modePk))) {
                StandardGroup subscriber = currentSubscription
                        .getSubscriberStandardGroup();

                if ((subscriber != null) && (subscriber.getIsFound())) {
                    List<StandardGroupMembership> memberships = sgmf
                            .findEnabledByGroupPrimaryKey(subscriber
                                    .getPrimaryKey());

                    for (int j = 0; j < memberships.size(); j++) {
                        StandardGroupMembership currentMembership = memberships
                                .get(j);
                        StandardAccount currentMember = currentMembership
                                .getStandardAccount();

                        if (currentMember.getIsFound()) {
                            if (!(recipients.contains(currentMember
                                    .getPrimaryKey()))) {
                                recipients.add(currentMember.getPrimaryKey());
                            }
                        }
                    }
                }
            }
        }

        /*
         * return
         */
        return recipients;
    }

    /**
     * Returns the list of indirect group subscribers of a content. use a null
     * mode for all subscribers.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> getIndirectGroupSubscribers(
            PrimaryKey contentPk, PrimaryKey modePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * initialize the result
         */
        List<PrimaryKey> indirectRecipients = new ArrayList<PrimaryKey>();

        /*
         * verify that there is a parent article
         */
        PrimaryKey homePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ArticlesDictionary.HOME);

        if (!(contentPk.equals(homePk))) {
            StandardContent content = (StandardContent) srf
                    .findByPrimaryKey(contentPk);

            if (content.getIsFound()) {
                /*
                 * verify that the content is an article or a icon
                 */
                PrimaryKey articleTypePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
                PrimaryKey iconTypePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);
                PrimaryKey typePk = content.getTypePrimaryKey();

                /*
                 * verify that the classification is identical
                 */
                if ((typePk.equals(articleTypePk))
                        || (typePk.equals(iconTypePk))) {
                    PrimaryKey sameAsParentPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);
                    PrimaryKey classificationTypePk = content
                            .getClassificationTypePrimaryKey();

                    if (classificationTypePk.equals(sameAsParentPk)) {
                        PrimaryKey parentContentPk = content
                                .getMainParentPrimaryKey();

                        /*
                         * get the subscribers of that article
                         */
                        indirectRecipients = ContentUtil
                                .getDirectGroupSubscribers(parentContentPk,
                                        modePk);
                    }
                }
            }
        }

        /*
         * return
         */
        return indirectRecipients;
    }

    /**
     * Returns the list of group subscribers of a content. use a null mode for
     * all subscribers.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> getRecursiveGroupSubscribers(
            PrimaryKey contentPk, PrimaryKey modePk) {
        /*
         * get the list of direct subscribers
         */
        List<PrimaryKey> recipients = ContentUtil.getDirectGroupSubscribers(
                contentPk, modePk);

        /*
         * add the subscribers of the parent article
         */
        List<PrimaryKey> indirectRecipients = ContentUtil
                .getIndirectGroupSubscribers(contentPk, modePk);

        for (PrimaryKey currentRecipientPk : indirectRecipients) {

            if (!(recipients.contains(currentRecipientPk))) {
                recipients.add(currentRecipientPk);
            }
        }

        /*
         * return
         */
        return recipients;
    }

    /**
     * Returns the list of subscribers of a content. Use a null mode for all
     * subscribers.
     * 
     * @param contentPk
     *            primary key of the content to query
     * @param modePk
     *            primary key of the mode of content subscription
     * 
     * 
     * @return a java.util.List object
     */
    public static final List<PrimaryKey> getRecursiveSubscribers(
            PrimaryKey contentPk, PrimaryKey modePk) {
        /*
         * get the list of individual subscribers
         */
        List<PrimaryKey> individualSubscribers = ContentUtil
                .getRecursiveIndividualSubscribers(contentPk, modePk);

        /*
         * get the list of group subscribers
         */
        List<PrimaryKey> groupSubscribers = ContentUtil
                .getRecursiveGroupSubscribers(contentPk, modePk);

        /*
         * add group subscribers to the list of individual subscribers
         */
        for (PrimaryKey currentAccountPk : groupSubscribers) {

            if (!(individualSubscribers.contains(currentAccountPk))) {
                individualSubscribers.add(currentAccountPk);
            }
        }

        /*
         * return
         */
        return individualSubscribers;
    }

    /**
     * Prints a message indicating the number of subscribers notified.
     * 
     * @param contentPk
     *            primary key of the content being notified for
     * @param contentTypePk
     *            primary key of the type of that content
     * 
     * 
     */
    public static void printSubscriptionMessage(PrimaryKey contentPk,
            PrimaryKey contentTypePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * get the article type
         */
        PrimaryKey articleTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        /*
         * get the mark page
         */
        PrimaryKey markAsHotPagePk = null;

        if (contentTypePk.equals(articleTypePk)) {
            markAsHotPagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.ARTICLE_MARK_AS_HOT);
        } else {
            markAsHotPagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.IMAGE_MARK_AS_HOT);
        }

        StandardPage markAsHotPage = (StandardPage) spf
                .findByPrimaryKey(markAsHotPagePk);

        /*
         * get the mark page location
         */
        String location = markAsHotPage.getDefaultLocation();

        if (contentTypePk.equals(articleTypePk)) {
            location = HTTPUtil.getAddedParameterURL(location,
                    HTTPParameterConstants.ARTICLE_ID, contentPk.toString());
        } else {
            location = HTTPUtil
                    .getAddedParameterURL(
                            location,
                            com.corendal.netapps.wiki.constants.HTTPParameterConstants.IMAGE_ID,
                            contentPk.toString());
        }

        /*
         * get the list of immediate subscribers
         */
        PrimaryKey immediatePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.IMMEDIATE);
        List<PrimaryKey> immediateSubscribers = ContentUtil
                .getRecursiveSubscribers(contentPk, immediatePk);

        /*
         * get the total list subscribers
         */
        List<PrimaryKey> totalSubscribers = ContentUtil
                .getRecursiveSubscribers(contentPk, null);

        /*
         * print the message
         */
        int immediateSize = immediateSubscribers.size();
        int totalSize = totalSubscribers.size();

        if (immediateSize == 0) {
            if (totalSize > 0) {
                /*
                 * get the printed message
                 */
                PrimaryKey messagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.INF_HOT_PROPOSED);
                StandardMessage message = (StandardMessage) smf
                        .findByPrimaryKey(messagePk);

                /*
                 * get the mark message
                 */
                PrimaryKey markPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_MARK);
                StandardMessage mark = (StandardMessage) smf
                        .findByPrimaryKey(markPk);

                /*
                 * get the icon
                 */
                String iconHTML = lw.getBreakableLinkHTML(location, null, mark
                        .getCurrentMessageHTML(), null);

                /*
                 * replace the placeholders
                 */
                message.replaceMessageText(mark.getCurrentMessageText(), 1);
                message.replaceMessageEncoded(mark.getCurrentMessageEncoded(),
                        1);
                message.replaceMessageHTML(iconHTML, 1);

                /*
                 * print the message
                 */
                message.printBufferWithIconHTML();
            }
        } else if (immediateSize == 1) {
            if (totalSize == immediateSize) {
                /*
                 * get the printed message
                 */
                PrimaryKey messagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.INF_ONE_SUBSCRIBER_NOTIFIED);
                StandardMessage message = (StandardMessage) smf
                        .findByPrimaryKey(messagePk);

                /*
                 * print the message
                 */
                message.printBufferWithIconHTML();
            } else {
                /*
                 * get the printed message
                 */
                PrimaryKey messagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.INF_ONE_SUBSCRIBER_NOTIFIED_HOT_PROPOSED);
                StandardMessage message = (StandardMessage) smf
                        .findByPrimaryKey(messagePk);

                /*
                 * get the mark message
                 */
                PrimaryKey markPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_MARK);
                StandardMessage mark = (StandardMessage) smf
                        .findByPrimaryKey(markPk);

                /*
                 * get the icon
                 */
                String iconHTML = lw.getBreakableLinkHTML(location, null, mark
                        .getCurrentMessageHTML(), null);

                /*
                 * replace the placeholders
                 */
                message.replaceMessageText(mark.getCurrentMessageText(), 1);
                message.replaceMessageEncoded(mark.getCurrentMessageEncoded(),
                        1);
                message.replaceMessageHTML(iconHTML, 1);

                /*
                 * print the message
                 */
                message.printBufferWithIconHTML();
            }
        } else {
            if (totalSize == immediateSize) {
                /*
                 * get the printed message
                 */
                PrimaryKey messagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.INF_SEVERAL_SUBSCRIBERS_NOTIFIED);
                StandardMessage message = (StandardMessage) smf
                        .findByPrimaryKey(messagePk);

                /*
                 * replace the placeholders
                 */
                message.replaceMessageText(String.valueOf(immediateSize), 1);
                message.replaceMessageEncoded(String.valueOf(immediateSize), 1);
                message.replaceMessageHTML(String.valueOf(immediateSize), 1);

                /*
                 * print the message
                 */
                message.printBufferWithIconHTML();
            } else {
                /*
                 * get the printed message
                 */
                PrimaryKey messagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.INF_SEVERAL_SUBSCRIBERS_NOTIFIED_HOT_PROPOSED);
                StandardMessage message = (StandardMessage) smf
                        .findByPrimaryKey(messagePk);

                /*
                 * get the mark message
                 */
                PrimaryKey markPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_MARK);
                StandardMessage mark = (StandardMessage) smf
                        .findByPrimaryKey(markPk);

                /*
                 * get the icon
                 */
                String iconHTML = lw.getBreakableLinkHTML(location, null, mark
                        .getCurrentMessageHTML(), null);

                /*
                 * replace the placeholders
                 */
                message.replaceMessageText(String.valueOf(immediateSize), 1);
                message.replaceMessageEncoded(String.valueOf(immediateSize), 1);
                message.replaceMessageHTML(String.valueOf(immediateSize), 1);
                message.replaceMessageText(mark.getCurrentMessageText(), 2);
                message.replaceMessageEncoded(mark.getCurrentMessageEncoded(),
                        2);
                message.replaceMessageHTML(iconHTML, 2);

                /*
                 * print the message
                 */
                message.printBufferWithIconHTML();
            }
        }
    }

    /**
     * Cleans a friendly address by removing or replacing any problematic
     * chararacter.
     * 
     * @return a java.lang.String object
     */
    public static final String getCleanFriendlyAddress(String friendlyAddress) {
        if (friendlyAddress != null) {
            String result = CaseUtil.getLowerCaseDeleteAccents(friendlyAddress);
            result = ReplaceUtil.getReplacedWithMatchCase(result, '_', '-');
            result = ReplaceUtil.getReplacedWithMatchCase(result, '/', '-');
            result = ReplaceUtil.getReplacedWithMatchCase(result, '\\', '-');
            result = ReplaceUtil.getReplacedWithMatchCase(result, '?', '-');
            result = ReplaceUtil.getReplacedWithMatchCase(result, '=', '-');
            result = ReplaceUtil.getReplacedWithMatchCase(result, '%', '-');
            result = ReplaceUtil.getReplacedWithMatchCase(result, '&', '-');
            result = ReplaceUtil.getReplacedWithMatchCase(result, ';', '-');
            result = ReplaceUtil.getReplacedWithMatchCase(result, ',', '-');
            result = NoDoubleUtil.getNoDoubleSpace(result);
            return ReplaceUtil.getReplacedWithMatchCase(result, ' ', '-');
        } else {
            return null;
        }
    }

    /**
     * Suggests a name for a new content based upon a name.
     * 
     * @return a java.lang.String object
     */
    public static final String getCleanSuggestedName(String name) {
        if (name != null) {
            return CaseUtil.getFirstUpperCase(name.trim());
        } else {
            return null;
        }
    }
}
