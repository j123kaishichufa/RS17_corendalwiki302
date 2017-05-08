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

import javax.servlet.http.HttpServletRequest;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.dictionaries.FieldsDictionary;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardSearchFormFieldFactory;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.ConfigManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.ServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormField;
import com.corendal.netapps.framework.core.interfaces.StandardSearchFormFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardURL;
import com.corendal.netapps.framework.core.interfaces.StandardURLFactory;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardURLFactory;
import com.corendal.netapps.framework.core.utils.FullTextUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.AlphanumericSingleKey;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.LDAPUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.commons.DefaultArticleBodyHighlighter;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PropertiesDictionary;
import com.corendal.netapps.wiki.interfaces.ArticleBodyHighlighter;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;

/**
 * ArticleUtil is the class for all article utilities.
 * 
 * @version $Id: ArticleUtil.java,v 1.21 2007/10/17 21:29:10 tdanard Exp $
 */
public final class ArticleUtil {
    /** Slash character */
    private static final String STRING_SLASH = "/";

    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static methods should be used explicitly.
     */
    private ArticleUtil() {
        // this class contains only static methods
    }

    /**
     * Returns the friendly address of the article to be viewed. Returns null
     * when not applicable.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getArticleFriendlyAddress() {
        /*
         * find the friendly address requested
         */
        HttpServletRequest req = ((ServletLogicContext) AnyLogicContextGlobal
                .get()).getHttpServletRequest();
        String friendlyAddress = HTTPUtil.getPathInfoWithoutVariantPrefix(req);

        /*
         * decode dangerous characters
         */
        friendlyAddress = LDAPUtil.getDecodedURL(friendlyAddress);

        /*
         * remove any starting slash
         */
        if (friendlyAddress.startsWith(STRING_SLASH)) {
            friendlyAddress = friendlyAddress.substring(1);
        }

        /*
         * remove any trailing slash
         */
        if (friendlyAddress.endsWith(STRING_SLASH)) {
            friendlyAddress = friendlyAddress.substring(0, friendlyAddress
                    .length() - 1);
        }

        /*
         * return
         */
        return friendlyAddress;
    }

    /**
     * Returns the friendly address of the onbline help article to be viewed.
     * Returns null when not applicable.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getOnlineHelpArticleFriendlyAddress() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardURLFactory suf = (StandardURLFactory) pfs
                .getStandardBeanFactory(DefaultStandardURLFactory.class);

        /*
         * find the friendly address requested
         */
        HttpServletRequest req = ((ServletLogicContext) AnyLogicContextGlobal
                .get()).getHttpServletRequest();
        String friendlyAddress = HTTPUtil.getPathInfoWithoutVariantPrefix(req);

        /*
         * get the goto page
         */
        PrimaryKey gotoPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.GOTO_ONLINE_HELP_ARTICLE);

        /*
         * get the URL of that page
         */
        StandardURL gotoURL = (StandardURL) suf
                .findEnabledByPagePrimaryKey(gotoPk);

        /*
         * decode dangerous characters
         */
        friendlyAddress = LDAPUtil.getDecodedURL(friendlyAddress);

        /*
         * remove the base
         */
        String base = gotoURL.getBase();

        if (friendlyAddress.startsWith(base)) {
            friendlyAddress = friendlyAddress.substring(base.length());
        }

        /*
         * remove any starting slash
         */
        if (friendlyAddress.startsWith(STRING_SLASH)) {
            friendlyAddress = friendlyAddress.substring(1);
        }

        /*
         * remove any trailing slash
         */
        if (friendlyAddress.endsWith(STRING_SLASH)) {
            friendlyAddress = friendlyAddress.substring(0, friendlyAddress
                    .length() - 1);
        }

        /*
         * return
         */
        return friendlyAddress;
    }

    /**
     * Returns the primary key of the article requested by the user.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public static final PrimaryKey getRequestedArticlePrimaryKey() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get the article id
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);

        /*
         * use the friendly address if no article id specified
         */
        if (StringUtil.getIsEmpty(articleId)) {
            /*
             * get the current page
             */
            StandardPage page = rm.getStandardPage();
            PrimaryKey pagePk = page.getPrimaryKey();

            /*
             * verify whether this is the goto page
             */
            PrimaryKey gotoPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.GOTO_ARTICLE);

            if (gotoPk.equals(pagePk)) {
                /*
                 * test whether we are using a friendly address
                 */
                String friendlyAddress = ArticleUtil
                        .getArticleFriendlyAddress();

                if (!(StringUtil.getIsEmpty(friendlyAddress))) {
                    /*
                     * get the factories of the instances used in this procedure
                     */
                    FactorySet pfs = AnyLogicContextGlobal.get()
                            .getFactorySet();
                    StandardArticleFactory sdf = (StandardArticleFactory) pfs
                            .getStandardBeanFactory(DefaultStandardArticleFactory.class);

                    /*
                     * get the article for that friendly address
                     */
                    StandardArticle sd = sdf
                            .findByFriendlyAddress(friendlyAddress);

                    if (sd.getIsFound()) {
                        /*
                         * add the article id as virtual parameter. This will
                         * allow this part of the code to be called only once
                         */
                        rm.addVirtualParameter(
                                HTTPParameterConstants.ARTICLE_ID,
                                ((AlphanumericSingleKey) sd.getPrimaryKey())
                                        .toString());

                        /*
                         * return
                         */
                        return sd.getPrimaryKey();
                    } else {
                        /*
                         * the associated article has been removed, let's create
                         * a phony primary key that will trigger the article to
                         * be "not found"
                         */
                        return PrimaryKeyUtil
                                .getAlphanumericSingleKey("Unknown friendly address: "
                                        + friendlyAddress);
                    }
                } else {
                    /*
                     * no article was specified, let's just use the home page
                     */
                    return PrimaryKeyUtil
                            .getAlphanumericSingleKey(ArticlesDictionary.HOME);
                }
            } else {
                return null;
            }
        } else {
            return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(articleId);
        }
    }

    /**
     * Returns the primary key of the online help article requested by the user.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    public static final PrimaryKey getRequestedOnlineHelpArticlePrimaryKey() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get the article id
         */
        String articleId = rm.getParameter(HTTPParameterConstants.ARTICLE_ID);

        /*
         * use the friendly address if no article id specified
         */
        if (StringUtil.getIsEmpty(articleId)) {
            /*
             * get the current page
             */
            StandardPage page = rm.getStandardPage();
            PrimaryKey pagePk = page.getPrimaryKey();

            /*
             * verify whether this is the goto page
             */
            PrimaryKey gotoPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.GOTO_ONLINE_HELP_ARTICLE);

            if (gotoPk.equals(pagePk)) {
                /*
                 * test whether we are using a friendly address
                 */
                String friendlyAddress = ArticleUtil
                        .getOnlineHelpArticleFriendlyAddress();

                if (!(StringUtil.getIsEmpty(friendlyAddress))) {
                    /*
                     * get the factories of the instances used in this procedure
                     */
                    FactorySet pfs = AnyLogicContextGlobal.get()
                            .getFactorySet();
                    StandardArticleFactory sdf = (StandardArticleFactory) pfs
                            .getStandardBeanFactory(DefaultStandardArticleFactory.class);

                    /*
                     * get the article for that friendly address
                     */
                    StandardArticle sd = sdf
                            .findByFriendlyAddress(friendlyAddress);

                    if (sd.getIsFound()) {
                        /*
                         * add the article id as virtual parameter. This will
                         * allow this part of the code to be called only once
                         */
                        rm.addVirtualParameter(
                                HTTPParameterConstants.ARTICLE_ID,
                                ((AlphanumericSingleKey) sd.getPrimaryKey())
                                        .toString());

                        /*
                         * return
                         */
                        return sd.getPrimaryKey();
                    } else {
                        /*
                         * the associated article has been removed, let's create
                         * a phony primary key that will trigger the article to
                         * be "not found"
                         */
                        return PrimaryKeyUtil
                                .getAlphanumericSingleKey("Unknown friendly address: "
                                        + friendlyAddress);
                    }
                } else {
                    /*
                     * no article was specified, let's just use the home page
                     */
                    return PrimaryKeyUtil
                            .getAlphanumericSingleKey(ArticlesDictionary.HOME);
                }
            } else {
                return null;
            }
        } else {
            return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(articleId);
        }
    }

    /**
     * Returns the keyword being used.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getKeyword() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardSearchFormFieldFactory ssfff = (StandardSearchFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardSearchFormFieldFactory.class);

        /*
         * get the keyword field primary keys
         */
        PrimaryKey siteKeywordSearchFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.SITE_KEYWORD_SEARCH);
        PrimaryKey entityKeywordSearchFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.ENTITY_KEYWORD_SEARCH);
        PrimaryKey contentKeywordSearchFieldPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.FieldsDictionary.CONTENT_KEYWORD);

        /*
         * get the keyword fields
         */
        StandardSearchFormField siteKeywordSearchField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(siteKeywordSearchFieldPk);
        StandardSearchFormField entityKeywordSearchField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(entityKeywordSearchFieldPk);
        StandardSearchFormField contentKeywordSearchField = (StandardSearchFormField) ssfff
                .findByPrimaryKey(contentKeywordSearchFieldPk);

        /*
         * get the keyword entered
         */
        String siteKeyword = siteKeywordSearchField.getRequestValue();

        if (StringUtil.getIsEmpty(siteKeyword)) {
            siteKeyword = entityKeywordSearchField.getRequestValue();
        }

        if (StringUtil.getIsEmpty(siteKeyword)) {
            siteKeyword = contentKeywordSearchField.getRequestValue();
        }

        /*
         * return
         */
        return siteKeyword;
    }

    /**
     * Returns the description to print in the text format when using the deep
     * keyword search.
     * 
     * @param article
     *            a article
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getCustomizedDescriptionText(
            StandardArticle article, boolean isDeepDescriptionsNeeded) {
        /*
         * get the keyword entered
         */
        String keyword = ArticleUtil.getKeyword();

        /*
         * customize the description if a keyword was entered
         */
        if (!(StringUtil.getIsEmpty(keyword))) {
            String customizedDescription = null;

            if (article.getIsAccessible()) {
                if ((isDeepDescriptionsNeeded)
                        && (!(StringUtil.getIsEmpty(article.getBodyText())))) {
                    customizedDescription = FullTextUtil
                            .getCustomizedDescriptionText(keyword, article
                                    .getLongDescriptionText()
                                    + ". " + article.getBodyText());
                } else {
                    customizedDescription = FullTextUtil
                            .getCustomizedDescriptionText(keyword, article
                                    .getLongDescriptionText());
                }
            } else {
                if ((isDeepDescriptionsNeeded)
                        && (!(StringUtil.getIsEmpty(article.getBodyText())))) {
                    customizedDescription = FullTextUtil
                            .getSecureCustomizedDescriptionText(keyword,
                                    article.getLongDescriptionText() + ". "
                                            + article.getBodyText());
                } else {
                    customizedDescription = FullTextUtil
                            .getSecureCustomizedDescriptionText(keyword,
                                    article.getLongDescriptionText());
                }
            }

            if (!(StringUtil.getIsEmpty(customizedDescription))) {
                return customizedDescription;
            } else {
                return article.getLongDescriptionText();
            }
        } else {
            return article.getLongDescriptionText();
        }
    }

    /**
     * Returns the name or description to print in the HTML format when using
     * the deep keyword search.
     * 
     * @param customizedText
     *            a customized name or description in the HTML format
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getCustomizedNameOrDescriptionHTML(
            String customizedText) {
        /*
         * get the keyword entered
         */
        String keyword = ArticleUtil.getKeyword();

        /*
         * customize the description if a keyword was entered
         */
        if (!(StringUtil.getIsEmpty(keyword))) {
            return FullTextUtil.getHighlightedHTML(keyword, customizedText);
        } else {
            return TextFormatUtil.getTextToHTML(customizedText);
        }
    }

    /**
     * Returns true if a location points to an article.
     * 
     * @param location
     *            location to query
     * 
     * 
     * @return a boolean
     */
    public static final boolean getIsArticleLocation(String location) {

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the article goto page
         */
        PrimaryKey gotoPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.GOTO_ARTICLE);
        StandardPage gotoPage = (StandardPage) spf.findByPrimaryKey(gotoPk);

        /*
         * get the location prefixes
         */
        if (gotoPage.getIsFound()) {
            String absoluteGotoArticlePrefix = gotoPage.getAbsoluteLocation();
            String relativeGotoArticlePrefix = gotoPage.getRelativeLocation();

            /*
             * return
             */
            return ((absoluteGotoArticlePrefix != null) && (location
                    .startsWith(absoluteGotoArticlePrefix)))
                    || ((relativeGotoArticlePrefix != null) && (location
                            .startsWith(relativeGotoArticlePrefix)));
        } else {
            return false;
        }
    }

    /**
     * Returns the friendly address embedded in an aticle location.
     * 
     * @param location
     *            location to query
     * 
     * 
     * @return a java.lang.String object
     */
    public static final String getFriendlyAddressFromLocation(String location) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the article goto page
         */
        PrimaryKey gotoPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.GOTO_ARTICLE);
        StandardPage gotoPage = (StandardPage) spf.findByPrimaryKey(gotoPk);

        /*
         * verify that the page was found
         */
        if (gotoPage.getIsFound()) {
            /*
             * remove anything after the sharp sign
             */
            String modifiedLocation = location;
            int pos = modifiedLocation.indexOf("#");
            if (pos >= 0) {
                modifiedLocation = modifiedLocation.substring(0, pos);
            }

            /*
             * get the location prefixes
             */
            String absoluteGotoArticlePrefix = gotoPage.getAbsoluteLocation();
            String relativeGotoArticlePrefix = gotoPage.getRelativeLocation();

            /*
             * verify that the location requested starts with one of these
             * locations
             */
            if (modifiedLocation.startsWith(absoluteGotoArticlePrefix)) {
                /*
                 * remove the prefix
                 */
                String friendlyAddress = modifiedLocation
                        .substring(absoluteGotoArticlePrefix.length());

                /*
                 * remove the suffix
                 */
                if (friendlyAddress.endsWith(STRING_SLASH)) {
                    friendlyAddress = friendlyAddress.substring(0,
                            friendlyAddress.length() - 1);
                }

                /*
                 * decode the special characters
                 */
                friendlyAddress = LDAPUtil.getDecodedURL(friendlyAddress);

                /*
                 * return
                 */
                return ContentUtil.getCleanFriendlyAddress(friendlyAddress);
            } else if (modifiedLocation.startsWith(relativeGotoArticlePrefix)) {
                /*
                 * remove the prefix
                 */
                String friendlyAddress = modifiedLocation
                        .substring(relativeGotoArticlePrefix.length());

                /*
                 * remove the suffix
                 */
                if (friendlyAddress.endsWith(STRING_SLASH)) {
                    friendlyAddress = friendlyAddress.substring(0,
                            friendlyAddress.length() - 1);
                }

                /*
                 * decode the special characters
                 */
                friendlyAddress = LDAPUtil.getDecodedURL(friendlyAddress);

                /*
                 * return
                 */
                return ContentUtil.getCleanFriendlyAddress(friendlyAddress);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the standard article for a property.
     * 
     * @param propertyFriendlyAddressPk
     *            primary key of the property where the friendly address is
     *            stored
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardArticle object
     */
    public static final StandardArticle getPropertyStandardArticle(
            PrimaryKey propertyFriendlyAddressPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory saf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ConfigManager cm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);

        /*
         * get the property set
         */
        StandardPropertySet prop = cm.getStandardPropertySet();

        /*
         * get the property friendly address
         */
        String propertyFriendlyAddress = prop
                .getValueString(propertyFriendlyAddressPk);

        /*
         * get the property article
         */
        return saf.findByFriendlyAddress(propertyFriendlyAddress);
    }

    /**
     * Returns the help desk standard article.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardArticle object
     */
    public static final StandardArticle getHelpDeskStandardArticle() {
        PrimaryKey helpDeskFriendlyAddressPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PropertiesDictionary.HELP_DESK_FRIENDLY_ADDRESS);

        return ArticleUtil
                .getPropertyStandardArticle(helpDeskFriendlyAddressPk);
    }

    /**
     * Returns the release notes standard article.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardArticle object
     */
    public static final StandardArticle getReleaseNotesStandardArticle() {
        PrimaryKey releaseNotesFriendlyAddressPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PropertiesDictionary.RELEASE_NOTES_FRIENDLY_ADDRESS);

        return ArticleUtil
                .getPropertyStandardArticle(releaseNotesFriendlyAddressPk);
    }

    /**
     * Returns the online help standard article.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardArticle object
     */
    public static final StandardArticle getOnlineHelpStandardArticle() {
        PrimaryKey onlineHelpFriendlyAddressPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PropertiesDictionary.ONLINE_HELP_FRIENDLY_ADDRESS);

        return ArticleUtil
                .getPropertyStandardArticle(onlineHelpFriendlyAddressPk);
    }

    /**
     * Returns a highlighted version of a raw body.
     */
    public static final String getHighlightedBody(PrimaryKey parentArticlePk,
            String rawBodyHTML, PrimaryKey homeArticlePk, boolean isOnlineHelp) {
        ArticleBodyHighlighter highlighter = new DefaultArticleBodyHighlighter();
        highlighter.process(parentArticlePk, rawBodyHTML, homeArticlePk, false);
        return highlighter.getModifiedBodyHTML();
    }
}
