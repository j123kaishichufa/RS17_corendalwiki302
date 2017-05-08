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
package com.corendal.netapps.wiki.writestandardbeans;

import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.dictionaries.RolesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.AccountHistoryManager;
import com.corendal.netapps.framework.core.interfaces.AccountManager;
import com.corendal.netapps.framework.core.interfaces.ConfigManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardAccountFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEmail;
import com.corendal.netapps.framework.core.interfaces.StandardEmailFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardPropertySet;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultAccountHistoryManager;
import com.corendal.netapps.framework.core.managers.DefaultAccountManager;
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.EntityUtil;
import com.corendal.netapps.framework.core.utils.HTMLFormatUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardEmailFactory;
import com.corendal.netapps.framework.helpdesk.interfaces.GroupHistoryManager;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupFactory;
import com.corendal.netapps.framework.helpdesk.managers.DefaultGroupHistoryManager;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.LDAPUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.commons.DefaultArticleBodyHighlighter;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionModesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.interfaces.ArticleBodyHighlighter;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.Parented;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.utils.ArticleUtil;
import com.corendal.netapps.wiki.utils.ContentInfoUtil;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writedatabeans.AbstractArticle;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentSubscriptionFactory;

/**
 * AbstractStandardArticle is the abstract class handling information about each
 * article of the application.
 * 
 * @version $Id: AbstractStandardArticle.java,v 1.1 2005/09/06 21:25:31 tdanard
 *          Exp $
 */
public abstract class AbstractStandardArticle extends AbstractArticle implements
        Cloneable, StandardArticle {
    /** Slash character */
    private static final String STRING_SLASH = "/";

    /** Indicator of an invisible content. */
    private static final String INVISIBLE_MARK = "...";

    /** Classification searched of this article */
    private Searched classificationSearched = null;

    /** Rule searched of this image. */
    private Searched ruleSearched = null;

    /** Indicates whether classification searched data has been populated */
    private boolean isClassificationSearchedPopulated;

    /** Indicates whether rule searched data has been populated */
    private boolean isRuleSearchedPopulated;

    /** Indicates whether sibling data has been populated */
    private boolean isSiblingsPopulated;

    /** List of sibling primary keys of this article */
    private List<PrimaryKey> siblingPrimaryKeys;

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardArticle() {
        // no initialization required
    }

    /**
     * Returns a clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardArticle) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBean#initStandardBean(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void initStandardBean() {
        this.siblingPrimaryKeys = new ArrayList<PrimaryKey>();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DataBean#getSortValue()
     */
    @Override
    public String getSortValue() {
        if (this.getIsFound()) {
            return ContentInfoUtil.getNameText(this.getInfoPrimaryKey());
        } else {
            return null;
        }
    }

    /**
     * Populates the current classification and other classification-dependant
     * variables.
     * 
     * 
     * 
     */
    private void populateClassificationSearched() {
        this.isClassificationSearchedPopulated = true;
        this.classificationSearched = ContentUtil
                .getClassificationSearched(this);
    }

    /**
     * Populates the current classification and other classification-dependant
     * variables if not already done.
     * 
     * 
     * 
     */
    private void populateClassificationSearchedIfNeeded() {
        if ((!(this.isClassificationSearchedPopulated)) && (this.getIsFound())) {
            this.populateClassificationSearched();
        }
    }

    /**
     * Populates the current rule and other rule-dependant variables.
     * 
     * 
     * 
     */
    private void populateRuleSearched() {
        this.isRuleSearchedPopulated = true;
        this.ruleSearched = ContentUtil.getRuleSearched(this);
    }

    /**
     * Populates the current rule and other rule-dependant variables if not
     * already done.
     * 
     * 
     * 
     */
    private void populateRuleSearchedIfNeeded() {
        if ((!(this.isRuleSearchedPopulated)) && (this.getIsFound())) {
            this.populateRuleSearched();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameText() {
        if (this.getIsVisible()) {
            return ContentInfoUtil.getNameText(this.getInfoPrimaryKey());
        } else {
            return AbstractStandardArticle.INVISIBLE_MARK;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameEncoded() {
        if (this.getIsVisible()) {
            return ContentInfoUtil.getNameEncoded(this.getInfoPrimaryKey());
        } else {
            return AbstractStandardArticle.INVISIBLE_MARK;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameHTML() {
        if (this.getIsVisible()) {
            return ContentInfoUtil.getNameHTML(this.getInfoPrimaryKey());
        } else {
            return AbstractStandardArticle.INVISIBLE_MARK;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionText() {
        if (this.getIsVisible()) {
            return ContentInfoUtil.getDescriptionText(this.getInfoPrimaryKey());
        } else {
            return AbstractStandardArticle.INVISIBLE_MARK;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionEncoded() {
        if (this.getIsVisible()) {
            return ContentInfoUtil.getDescriptionEncoded(this
                    .getInfoPrimaryKey());
        } else {
            return AbstractStandardArticle.INVISIBLE_MARK;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionHTML() {
        if (this.getIsVisible()) {
            return ContentInfoUtil.getDescriptionHTML(this.getInfoPrimaryKey());
        } else {
            return AbstractStandardArticle.INVISIBLE_MARK;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Pathed#getPathText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPathText() {
        return ContentUtil.getPathText(this, this.getNameText());
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Pathed#getPathEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPathEncoded() {
        return ContentUtil.getPathEncoded(this, this.getNameEncoded());
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Pathed#getPathHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPathHTML() {
        return ContentUtil.getPathHTML(this, this.getNameHTML());
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Pathed#getPathHTMLWithLink(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPathHTMLWithLink() {
        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * build the name of the image
         */
        String nameHTML = lw.getBreakableLinkHTML(this.getDefaultLocation(),
                null, this.getNameEncoded(), null);

        /*
         * return
         */
        return ContentUtil.getPathHTMLWithLink(this, nameHTML);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Pathed#getPathHTMLNoLastLink(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPathHTMLNoLastLink() {
        return ContentUtil.getPathHTMLWithLink(this, this.getNameHTML());
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.PropertiesLocated#getPropertiesRelativeLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPropertiesRelativeLocation() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the properties page
         */
        PrimaryKey propertiesPagePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.ARTICLE_PROPERTIES);
        StandardPage propertiesPage = (StandardPage) spf
                .findByPrimaryKey(propertiesPagePk);

        /*
         * return
         */
        String result = propertiesPage.getRelativeLocation();
        result = HTTPUtil.getAddedParameterURL(result,
                HTTPParameterConstants.ARTICLE_ID, this.getPrimaryKey()
                        .toString());

        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.PropertiesLocated#getPropertiesDefaultLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPropertiesDefaultLocation() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the properties page
         */
        PrimaryKey propertiesPagePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.ARTICLE_PROPERTIES);
        StandardPage propertiesPage = (StandardPage) spf
                .findByPrimaryKey(propertiesPagePk);

        /*
         * return
         */
        String result = propertiesPage.getDefaultLocation();
        result = HTTPUtil.getAddedParameterURL(result,
                HTTPParameterConstants.ARTICLE_ID, this.getPrimaryKey()
                        .toString());

        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.PropertiesLocated#getPropertiesAbsoluteLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPropertiesAbsoluteLocation() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);

        /*
         * get the properties page
         */
        PrimaryKey propertiesPagePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(PagesDictionary.ARTICLE_PROPERTIES);
        StandardPage propertiesPage = (StandardPage) spf
                .findByPrimaryKey(propertiesPagePk);

        /*
         * return
         */
        String result = propertiesPage.getAbsoluteLocation();
        result = HTTPUtil.getAddedParameterURL(result,
                HTTPParameterConstants.ARTICLE_ID, this.getPrimaryKey()
                        .toString());

        return result;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getRelativeLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getRelativeLocation() {
        /*
         * verify whether this article has a friendly address
         */
        if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the goto article page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_ARTICLE);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getRelativeLocation()
                    + LDAPUtil.getEncodedURL(this.getFriendlyAddress())
                    + STRING_SLASH;
        } else {
            PrimaryKey homeArticlePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ArticlesDictionary.HOME);

            if (this.getPrimaryKey().equals(homeArticlePk)) {
                /*
                 * get the factories of the instances used in this procedure
                 */
                FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
                StandardPageFactory spf = (StandardPageFactory) pfs
                        .getStandardBeanFactory(DefaultStandardPageFactory.class);

                /*
                 * get the home page
                 */
                PrimaryKey homePagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.PagesDictionary.HOME);
                StandardPage homePage = (StandardPage) spf
                        .findByPrimaryKey(homePagePk);

                /*
                 * return
                 */
                return homePage.getRelativeLocation();
            } else {
                return EntityUtil
                        .getRelativeLocation(
                                PrimaryKeyUtil
                                        .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.ARTICLES),
                                HTTPParameterConstants.ARTICLE_ID, this
                                        .getPrimaryKey().toString());
            }
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getDefaultLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDefaultLocation() {
        /*
         * verify whether this article has a friendly address
         */
        if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the goto article page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_ARTICLE);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getDefaultLocation()
                    + LDAPUtil.getEncodedURL(this.getFriendlyAddress())
                    + STRING_SLASH;
        } else {
            PrimaryKey homeArticlePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ArticlesDictionary.HOME);

            if (this.getPrimaryKey().equals(homeArticlePk)) {
                /*
                 * get the factories of the instances used in this procedure
                 */
                FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
                StandardPageFactory spf = (StandardPageFactory) pfs
                        .getStandardBeanFactory(DefaultStandardPageFactory.class);

                /*
                 * get the home page
                 */
                PrimaryKey homePagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.PagesDictionary.HOME);
                StandardPage homePage = (StandardPage) spf
                        .findByPrimaryKey(homePagePk);

                /*
                 * return
                 */
                return homePage.getDefaultLocation();
            } else {
                return EntityUtil
                        .getDefaultLocation(
                                PrimaryKeyUtil
                                        .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.ARTICLES),
                                HTTPParameterConstants.ARTICLE_ID, this
                                        .getPrimaryKey().toString());
            }
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getAbsoluteLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAbsoluteLocation() {
        /*
         * verify whether this article has a friendly address
         */
        if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the goto article page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_ARTICLE);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getAbsoluteLocation()
                    + LDAPUtil.getEncodedURL(this.getFriendlyAddress())
                    + STRING_SLASH;
        } else {
            PrimaryKey homeArticlePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ArticlesDictionary.HOME);

            if (this.getPrimaryKey().equals(homeArticlePk)) {
                /*
                 * get the factories of the instances used in this procedure
                 */
                FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
                StandardPageFactory spf = (StandardPageFactory) pfs
                        .getStandardBeanFactory(DefaultStandardPageFactory.class);

                /*
                 * get the home page
                 */
                PrimaryKey homePagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.PagesDictionary.HOME);
                StandardPage homePage = (StandardPage) spf
                        .findByPrimaryKey(homePagePk);

                /*
                 * return
                 */
                return homePage.getAbsoluteLocation();
            } else {
                return EntityUtil
                        .getAbsoluteLocation(
                                PrimaryKeyUtil
                                        .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.ARTICLES),
                                HTTPParameterConstants.ARTICLE_ID, this
                                        .getPrimaryKey().toString());
            }
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getOnlineHelpRelativeLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getOnlineHelpRelativeLocation() {
        /*
         * get the online help article
         */
        StandardArticle onlineHelp = ArticleUtil.getOnlineHelpStandardArticle();

        if ((onlineHelp.getIsFound()) && (onlineHelp.equals(this))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the online help page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.PagesDictionary.ONLINE_HELP);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getRelativeLocation();
        } else if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the online help goto article page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_ONLINE_HELP_ARTICLE);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getRelativeLocation()
                    + LDAPUtil.getEncodedURL(this.getFriendlyAddress())
                    + STRING_SLASH;
        } else {
            return this.getRelativeLocation();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getOnlineHelpDefaultLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getOnlineHelpDefaultLocation() {
        /*
         * get the online help article
         */
        StandardArticle onlineHelp = ArticleUtil.getOnlineHelpStandardArticle();

        if ((onlineHelp.getIsFound()) && (onlineHelp.equals(this))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the online help page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.PagesDictionary.ONLINE_HELP);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getDefaultLocation();
        } else if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the online help goto article page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_ONLINE_HELP_ARTICLE);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getDefaultLocation()
                    + LDAPUtil.getEncodedURL(this.getFriendlyAddress())
                    + STRING_SLASH;
        } else {
            return this.getDefaultLocation();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getOnlineHelpAbsoluteLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getOnlineHelpAbsoluteLocation() {
        /*
         * get the online help article
         */
        StandardArticle onlineHelp = ArticleUtil.getOnlineHelpStandardArticle();

        if ((onlineHelp.getIsFound()) && (onlineHelp.equals(this))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the online help page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.helpdesk.dictionaries.PagesDictionary.ONLINE_HELP);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getAbsoluteLocation();
        } else if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the online help goto article page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_ONLINE_HELP_ARTICLE);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getAbsoluteLocation()
                    + LDAPUtil.getEncodedURL(this.getFriendlyAddress())
                    + STRING_SLASH;
        } else {
            return this.getAbsoluteLocation();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Matched#getMatchNameText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getMatchNameText() {
        return this.getPathText();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Matched#getMatchNameEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getMatchNameEncoded() {
        return this.getPathEncoded();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Matched#getMatchNameHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getMatchNameHTML() {
        return this.getPathHTML();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedShort#getShortDescriptionText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getShortDescriptionText() {
        return this.getDescriptionText();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedShort#getShortDescriptionEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getShortDescriptionEncoded() {
        return this.getDescriptionEncoded();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedShort#getShortDescriptionHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getShortDescriptionHTML() {
        return this.getDescriptionHTML();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedLong#getLongDescriptionText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getLongDescriptionText() {
        return this.getDescriptionText();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedLong#getLongDescriptionEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getLongDescriptionEncoded() {
        return this.getDescriptionEncoded();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.DescribedLong#getLongDescriptionHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getLongDescriptionHTML() {
        return this.getDescriptionHTML();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.AccessKeyed#getAccessKeyText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAccessKeyText() {
        return null;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.AccessKeyed#getAccessKeyEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAccessKeyEncoded() {
        return null;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.AccessKeyed#getAccessKeyHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAccessKeyHTML() {
        return null;
    }

    /*
     * @see com.corendal.netapps.framework.helpdesk.interfaces.NativeGroup#getTypePrimaryKey()
     */
    public PrimaryKey getTypePrimaryKey() {
        return PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Secured#getClassificationSearched(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public Searched getClassificationSearched() {
        this.populateClassificationSearchedIfNeeded();

        return this.classificationSearched;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Secured#getRuleSearched(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public Searched getRuleSearched() {
        this.populateRuleSearchedIfNeeded();

        return this.ruleSearched;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getIsVisible(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsVisible() {
        if (this.getIsFound()) {
            /*
             * get the managers used in this procedure
             */
            ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
            ContentManager resm = (ContentManager) pms
                    .getManager(DefaultContentManager.class);

            /*
             * return
             */
            return resm.getIsVisible(this);
        } else {
            return false;
        }
    }

    /**
     * Returns true if this article is a parent of another content (direct or
     * indirect). no recursivity is applied.
     * 
     * @param contentPk
     *            primary key of the content to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getIsParentOf(PrimaryKey contentPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the content
         */
        StandardContent currentContent = (StandardContent) srf
                .findByPrimaryKey(contentPk);

        /*
         * return
         */
        return currentContent.getIsChildOf(this.getPrimaryKey());
    }

    /**
     * Returns true if this article is a direct parent of another content no
     * recursivity is applied.
     * 
     * @param contentPk
     *            primary key of the content to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getIsDirectParentOf(PrimaryKey contentPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the content
         */
        StandardContent currentContent = (StandardContent) srf
                .findByPrimaryKey(contentPk);

        /*
         * return
         */
        return currentContent.getIsDirectChildOf(this.getPrimaryKey());
    }

    /**
     * Returns true if this article is an indirect parent of another content. no
     * recursivity is applied.
     * 
     * @param contentPk
     *            primary key of the content to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getIsIndirectParentOf(PrimaryKey contentPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the content
         */
        StandardContent currentContent = (StandardContent) srf
                .findByPrimaryKey(contentPk);

        /*
         * return
         */
        return currentContent.getIsIndirectChildOf(this.getPrimaryKey());
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getIsRecursiveDirectParentOf(com.corendal.netapps.wiki.interfaces.Parented,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsRecursiveDirectParentOf(Parented child) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * return
         */
        if (this.getIsDirectParentOf(child.getPrimaryKey())) {
            return true;
        } else {
            PrimaryKey parentPrimaryKey = child.getMainParentPrimaryKey();

            if (parentPrimaryKey != null) {
                StandardArticle currentArticle = (StandardArticle) sdf
                        .findByPrimaryKey(parentPrimaryKey);

                if (currentArticle.getIsFound()) {
                    return this.getIsRecursiveDirectParentOf(currentArticle);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getAuthorStandardAccount(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardAccount getAuthorStandardAccount() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccountHistoryManager hm = (AccountHistoryManager) pms
                .getManager(DefaultAccountHistoryManager.class);

        /*
         * get the primary key of the author account
         */
        PrimaryKey accountPk = this.getAuthorAccountPrimaryKey();

        if (accountPk == null) {
            /*
             * get the editor of this article
             */
            StandardAccount sa = this.getEditorStandardAccount();

            /*
             * add this account to the history
             */
            hm.add(sa.getPrimaryKey());

            /*
             * return
             */
            return sa;
        } else {
            /*
             * add this account to the history
             */
            hm.add(accountPk);

            /*
             * return
             */
            return (StandardAccount) saf.findByPrimaryKey(accountPk);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getEditorStandardAccount(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardAccount getEditorStandardAccount() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccountManager am = (AccountManager) pms
                .getManager(DefaultAccountManager.class);
        AccountHistoryManager hm = (AccountHistoryManager) pms
                .getManager(DefaultAccountHistoryManager.class);

        /*
         * get the primary key of the editor account
         */
        PrimaryKey accountPk = this.getEditorAccountPrimaryKey();

        if (accountPk == null) {
            /*
             * find the parent article
             */
            PrimaryKey mainParentPrimaryKey = this.getMainParentPrimaryKey();

            /*
             * verify that a main parent exists
             */
            if (mainParentPrimaryKey != null) {
                /*
                 * get the parent article
                 */
                StandardArticle parentArticle = (StandardArticle) sdf
                        .findByPrimaryKey(mainParentPrimaryKey);

                /*
                 * check that the parent article was found
                 */
                if (parentArticle.getIsFound()) {
                    /*
                     * find the editor of this parent article
                     */
                    StandardAccount sa = parentArticle
                            .getEditorStandardAccount();

                    /*
                     * check that the account can be found
                     */
                    if ((sa != null) && (sa.getIsFound())) {
                        /*
                         * add this account to the history
                         */
                        hm.add(sa.getPrimaryKey());

                        /*
                         * return
                         */
                        return sa;
                    } else {
                        /*
                         * use the system administrator account
                         */
                        StandardAccount admin = am
                                .getSystemAdministratorStandardAccount();

                        /*
                         * verify that this account exists
                         */
                        if ((admin != null) && (admin.getIsFound())) {
                            /*
                             * add this account to the history
                             */
                            hm.add(admin.getPrimaryKey());

                            /*
                             * return
                             */
                            return admin;
                        } else {
                            /*
                             * return
                             */
                            return null;
                        }
                    }
                } else {
                    /*
                     * use the system administrator account
                     */
                    StandardAccount admin = am
                            .getSystemAdministratorStandardAccount();

                    /*
                     * verify that this account exists
                     */
                    if ((admin != null) && (admin.getIsFound())) {
                        /*
                         * add this account to the history
                         */
                        hm.add(admin.getPrimaryKey());

                        /*
                         * return
                         */
                        return admin;
                    } else {
                        /*
                         * return
                         */
                        return null;
                    }
                }
            } else {
                /*
                 * use the system administrator account
                 */
                StandardAccount admin = am
                        .getSystemAdministratorStandardAccount();

                /*
                 * verify that this account exists
                 */
                if ((admin != null) && (admin.getIsFound())) {
                    /*
                     * add this account to the history
                     */
                    hm.add(admin.getPrimaryKey());

                    /*
                     * return
                     */
                    return admin;
                } else {
                    /*
                     * return
                     */
                    return null;
                }
            }
        } else {
            /*
             * add this account to the history
             */
            hm.add(accountPk);

            /*
             * return
             */
            return (StandardAccount) saf.findByPrimaryKey(accountPk);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getAssociateEditorsStandardGroup(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardGroup getAssociateEditorsStandardGroup() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        GroupHistoryManager hm = (GroupHistoryManager) pms
                .getManager(DefaultGroupHistoryManager.class);

        /*
         * get the primary key of the editor group
         */
        PrimaryKey groupPk = this.getAssociateEditorsGroupPrimaryKey();

        if (groupPk == null) {
            /*
             * return
             */
            return null;
        } else {
            /*
             * add this group to the history
             */
            hm.add(groupPk);

            /*
             * return
             */
            return (StandardGroup) sgf.findByPrimaryKey(groupPk);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Secured#getIsAccessible(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsAccessible() {
        if (this.getIsFound()) {
            /*
             * get the managers used in this procedure
             */
            ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
            ContentManager resm = (ContentManager) pms
                    .getManager(DefaultContentManager.class);

            /*
             * return
             */
            return resm.getIsAccessible(this);
        } else {
            return false;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#remove(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void remove(PrimaryKey referenceRequestPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);

        /*
         * get all content subscriptions associated with this content
         */
        List<StandardContentSubscription> contentSubscriptions = srsf
                .findEnabledByContentPrimaryKey(this.getPrimaryKey());

        /*
         * remove each content subscription
         */
        for (int i = 0; i < contentSubscriptions.size(); i++) {
            StandardContentSubscription currentSubscription = (StandardContentSubscription) contentSubscriptions
                    .get(i);
            currentSubscription.remove();
        }

        /*
         * remove this image
         */
        super.remove(referenceRequestPk);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#notifySubscribers(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void notifySubscribers() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);
        StandardPageFactory spf = (StandardPageFactory) pfs
                .getStandardBeanFactory(DefaultStandardPageFactory.class);
        StandardEmailFactory semf = (StandardEmailFactory) pfs
                .getStandardBeanFactory(DefaultStandardEmailFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ConfigManager cm = (ConfigManager) pms
                .getManager(DefaultConfigManager.class);
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);

        /*
         * get the property set
         */
        StandardPropertySet prop = cm.getStandardPropertySet();

        /*
         * get the parent article
         */
        this.clearCacheAndLoad(); // to prevent caching issues

        /*
         * get the sender
         */
        StandardAccount sender = am.getProxyStandardAccount();

        /*
         * get the values of the placeholders
         */
        String companyNameText = prop.getCompanyNameText();
        String applicationNameText = prop.getApplicationNameText();
        String articleNameText = this.getNameText();
        String articleDescriptionText = this.getDescriptionText();
        String versionNumberText = String.valueOf(this.getVersionNum());
        String articleLocationText = this.getAbsoluteLocation();
        articleLocationText = HTTPUtil.getAddedParameterURL(
                articleLocationText,
                HTTPParameterConstants.CHECK_VERSION_NUMBER, versionNumberText);

        String articleVersionNotesText = this.getComment();

        String senderNameText = sender.getFullNameText();

        /*
         * get the immediate mode
         */
        PrimaryKey immediateModesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.IMMEDIATE);

        /*
         * send individual subscriptions
         */
        List<PrimaryKey> individualRecipients = ContentUtil
                .getDirectIndividualSubscribers(this.getPrimaryKey(),
                        immediateModesPk);

        /*
         * send the email
         */
        if (individualRecipients.size() > 0) {
            /*
             * get the unsubscribe page
             */
            PrimaryKey unsubscribePagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.ARTICLE_UNSUBSCRIBE);
            StandardPage unsubscribePage = (StandardPage) spf
                    .findByPrimaryKey(unsubscribePagePk);
            String articleUnsubscribeText = unsubscribePage
                    .getAbsoluteLocation();
            articleUnsubscribeText = HTTPUtil.getAddedParameterURL(
                    articleUnsubscribeText, HTTPParameterConstants.ARTICLE_ID,
                    this.getPrimaryKey().toString());

            /*
             * get the messages
             */
            PrimaryKey subjectMessagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_INDIVIDUAL_ARTICLE_SUBSCRIPTION_SUBJECT);
            PrimaryKey bodyMessagePk = null;

            if (StringUtil.getIsEmpty(this.getComment())) {
                bodyMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_INDIVIDUAL_ARTICLE_SUBSCRIPTION_BODY);
            } else {
                bodyMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_INDIVIDUAL_ARTICLE_WITH_NOTES_SUBSCRIPTION_BODY);
            }

            StandardMessage subjectMessage = (StandardMessage) smf
                    .findByPrimaryKey(subjectMessagePk);
            StandardMessage bodyMessage = (StandardMessage) smf
                    .findByPrimaryKey(bodyMessagePk);

            /*
             * fill the messages
             */
            subjectMessage.replaceMessageText(articleNameText, 1);

            bodyMessage.replaceMessageText(companyNameText, 1);
            bodyMessage.replaceMessageText(applicationNameText, 2);
            bodyMessage.replaceMessageText(senderNameText, 3);
            bodyMessage.replaceMessageText(articleNameText, 4);
            bodyMessage.replaceMessageText(articleNameText, 5);
            bodyMessage.replaceMessageText(versionNumberText, 6);
            bodyMessage.replaceMessageText(articleDescriptionText, 7);
            bodyMessage.replaceMessageText(articleLocationText, 8);
            bodyMessage.replaceMessageText(articleUnsubscribeText, 9);
            bodyMessage.replaceMessageText(articleVersionNotesText, 10);

            /*
             * create the email
             */
            PrimaryKey emailToPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EMAIL_TO);
            PrimaryKey emailCcPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EMAIL_CC);
            StandardEmail email = (StandardEmail) semf.create(subjectMessage
                    .getCurrentMessageText(), bodyMessage
                    .getCurrentMessageText());

            /*
             * add sender as from and cc
             */
            email.storeFromPrimaryKey(sender.getPrimaryKey());
            email.addRecipient(sender.getPrimaryKey(), emailCcPk);

            /*
             * add the list of individual recipients
             */
            for (int i = 0; i < individualRecipients.size(); i++) {
                PrimaryKey recipientPk = (PrimaryKey) individualRecipients
                        .get(i);
                email.addRecipient(recipientPk, emailToPk);
            }

            /*
             * send
             */
            email.sendNow();
        }

        /*
         * send direct group subscriptions
         */
        List<PrimaryKey> groupRecipients = ContentUtil
                .getDirectGroupSubscribers(this.getPrimaryKey(),
                        immediateModesPk);

        /*
         * send the email
         */
        if (groupRecipients.size() > 0) {
            /*
             * get the unsubscribe page
             */
            String articleUnsubscribeText = sender.getFullNameText();

            /*
             * get the messages
             */
            PrimaryKey subjectMessagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_GROUP_ARTICLE_SUBSCRIPTION_SUBJECT);
            PrimaryKey bodyMessagePk = null;

            if (StringUtil.getIsEmpty(this.getComment())) {
                bodyMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_GROUP_ARTICLE_SUBSCRIPTION_BODY);
            } else {
                bodyMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_GROUP_ARTICLE_WITH_NOTES_SUBSCRIPTION_BODY);
            }

            StandardMessage subjectMessage = (StandardMessage) smf
                    .findByPrimaryKey(subjectMessagePk);
            StandardMessage bodyMessage = (StandardMessage) smf
                    .findByPrimaryKey(bodyMessagePk);

            /*
             * fill the messages
             */
            subjectMessage.replaceMessageText(articleNameText, 1);

            bodyMessage.replaceMessageText(companyNameText, 1);
            bodyMessage.replaceMessageText(applicationNameText, 2);
            bodyMessage.replaceMessageText(senderNameText, 3);
            bodyMessage.replaceMessageText(articleNameText, 4);
            bodyMessage.replaceMessageText(articleNameText, 5);
            bodyMessage.replaceMessageText(versionNumberText, 6);
            bodyMessage.replaceMessageText(articleDescriptionText, 7);
            bodyMessage.replaceMessageText(articleLocationText, 8);
            bodyMessage.replaceMessageText(articleUnsubscribeText, 9);
            bodyMessage.replaceMessageText(articleVersionNotesText, 10);

            /*
             * create the email
             */
            PrimaryKey emailToPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EMAIL_TO);
            PrimaryKey emailCcPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.EMAIL_CC);
            StandardEmail email = (StandardEmail) semf.create(subjectMessage
                    .getCurrentMessageText(), bodyMessage
                    .getCurrentMessageText());

            /*
             * add sender as from and cc
             */
            email.storeFromPrimaryKey(sender.getPrimaryKey());
            email.addRecipient(sender.getPrimaryKey(), emailCcPk);

            /*
             * add the list of group recipients
             */
            for (int i = 0; i < groupRecipients.size(); i++) {
                PrimaryKey recipientPk = (PrimaryKey) groupRecipients.get(i);
                email.addRecipient(recipientPk, emailToPk);
            }

            /*
             * send
             */
            email.sendNow();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getSize(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public int getSize() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * return
         */
        return srf.findCountOfEnabledByParentArticlePrimaryKey(this
                .getPrimaryKey());
    }

    /**
     * Populates the siblingPrimaryKeys of this article.
     * 
     * 
     * 
     */
    private void populateSiblings() {
        /*
         * populates the populate variable
         */
        this.isSiblingsPopulated = true;

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * find the parent article
         */
        PrimaryKey mainParentPrimaryKey = this.getMainParentPrimaryKey();

        /*
         * verify that a main parent exists
         */
        if (mainParentPrimaryKey != null) {
            /*
             * get the parent article
             */
            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(mainParentPrimaryKey);

            /*
             * check that the parent article was found
             */
            if ((parentArticle.getIsFound()) && (parentArticle.getIsVisible())) {
                /*
                 * get the list of children articles
                 */
                this.siblingPrimaryKeys = sdf
                        .findDirectEnabledPrimaryKeysByParentArticlePrimaryKey(parentArticle
                                .getPrimaryKey());
            }
        }
    }

    /**
     * Populates the siblingPrimaryKeys
     * 
     * 
     * 
     */
    private void populateSiblingsIfNeeded() {
        if ((!(this.isSiblingsPopulated)) && (this.getIsFound())) {
            this.populateSiblings();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getSiblings(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardArticle> getSiblings() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * find the parent article
         */
        PrimaryKey mainParentPrimaryKey = this.getMainParentPrimaryKey();

        /*
         * verify that a main parent exists
         */
        if (mainParentPrimaryKey != null) {
            /*
             * get the parent article
             */
            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(mainParentPrimaryKey);

            /*
             * check that the parent article was found
             */
            if ((parentArticle.getIsFound()) && (parentArticle.getIsVisible())) {
                /*
                 * get the list of children articles
                 */
                return sdf
                        .findDirectEnabledByParentArticlePrimaryKey(parentArticle
                                .getPrimaryKey());
            } else {
                return new ArrayList<StandardArticle>();
            }
        } else {
            return new ArrayList<StandardArticle>();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getFirstSiblingStandardArticle(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardArticle getFirstSiblingStandardArticle() {
        this.populateSiblingsIfNeeded();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the first sibling
         */
        if (this.siblingPrimaryKeys.size() > 0) {
            /*
             * get the first sibling
             */
            PrimaryKey firstSiblingPk = siblingPrimaryKeys.get(0);
            StandardArticle firstSibling = (StandardArticle) sdf
                    .findByPrimaryKey(firstSiblingPk);

            /*
             * return
             */
            if (firstSibling.equals(this)) {
                return null;
            } else {
                if (firstSibling.getIsVisible()) {
                    return firstSibling;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getPreviousSiblingStandardArticle(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardArticle getPreviousSiblingStandardArticle() {
        this.populateSiblingsIfNeeded();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the previous sibling
         */
        if (this.siblingPrimaryKeys.size() > 0) {
            /*
             * get the position of this article
             */
            int currentPosition = this.siblingPrimaryKeys.indexOf(this
                    .getPrimaryKey());

            /*
             * get the previous sibling
             */
            if (currentPosition > 0) {
                PrimaryKey previousSiblingPk = siblingPrimaryKeys
                        .get(currentPosition - 1);
                StandardArticle previousSibling = (StandardArticle) sdf
                        .findByPrimaryKey(previousSiblingPk);

                /*
                 * return
                 */
                if (previousSibling.equals(this)) {
                    return null;
                } else {
                    if (previousSibling.getIsVisible()) {
                        return previousSibling;
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getNextSiblingStandardArticle(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardArticle getNextSiblingStandardArticle() {
        this.populateSiblingsIfNeeded();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the next sibling
         */
        if (this.siblingPrimaryKeys.size() > 0) {
            /*
             * get the position of this article
             */
            int currentPosition = this.siblingPrimaryKeys.indexOf(this
                    .getPrimaryKey());

            /*
             * get the next sibling
             */
            if (currentPosition < (this.siblingPrimaryKeys.size() - 1)) {
                PrimaryKey nextSiblingPk = siblingPrimaryKeys
                        .get(currentPosition + 1);
                StandardArticle nextSibling = (StandardArticle) sdf
                        .findByPrimaryKey(nextSiblingPk);

                /*
                 * return
                 */
                if (nextSibling.equals(this)) {
                    return null;
                } else {
                    if (nextSibling.getIsVisible()) {
                        return nextSibling;
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getLastSiblingStandardArticle(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardArticle getLastSiblingStandardArticle() {
        this.populateSiblingsIfNeeded();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the first sibling
         */
        if (this.siblingPrimaryKeys.size() > 0) {
            /*
             * get the first sibling
             */
            PrimaryKey lastSiblingPk = siblingPrimaryKeys
                    .get(this.siblingPrimaryKeys.size() - 1);
            StandardArticle lastSibling = (StandardArticle) sdf
                    .findByPrimaryKey(lastSiblingPk);

            /*
             * return
             */
            if (lastSibling.equals(this)) {
                return null;
            } else {
                if (lastSibling.getIsVisible()) {
                    return lastSibling;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticle#getBodyHTML(boolean,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getBodyHTML(boolean isOnlineHelp, PrimaryKey homeArticlePk) {
        if (isOnlineHelp) {
            return ArticleUtil.getHighlightedBody(this.getPrimaryKey(), this
                    .getRawBodyHTML(), homeArticlePk, isOnlineHelp);
        } else {
            String bodyHTML = this.getBodyHTML();
            if (StringUtil.getIsEmpty(bodyHTML)) {
                /*
                 * extract the text from the raw body
                 */
                String rawBodyHTML = this.getRawBodyHTML();
                String bodyText = HTMLFormatUtil.getHTMLToText(rawBodyHTML);

                /*
                 * highlight the raw body
                 */
                ArticleBodyHighlighter highlighter = new DefaultArticleBodyHighlighter();
                highlighter.process(this.getPrimaryKey(), rawBodyHTML,
                        homeArticlePk, false);

                /*
                 * update the bodies
                 */
                this.storeBodies(rawBodyHTML, bodyText, highlighter
                        .getModifiedBodyHTML());

                /*
                 * update the addresses
                 */
                this.storeAddresses(highlighter.getModifiedLocations());

                /*
                 * return body
                 */
                return highlighter.getModifiedBodyHTML();
            } else {
                return bodyHTML;
            }
        }

    }
}

// end AbstractStandardArticle
