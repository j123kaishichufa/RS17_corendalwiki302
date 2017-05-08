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

import java.util.List;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.dictionaries.RolesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.AccountHistoryManager;
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
import com.corendal.netapps.framework.core.managers.DefaultConfigManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.EntityUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardEmailFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.LDAPUtil;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionModesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.utils.ContentInfoUtil;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writedatabeans.AbstractImage;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentSubscriptionFactory;

/**
 * AbstractStandardImage is the abstract class handling information about each
 * image of the application.
 * 
 * @version $Id: AbstractStandardImage.java,v 1.1 2005/09/06 21:25:31 tdanard
 *          Exp $
 */
public abstract class AbstractStandardImage extends AbstractImage implements
        Cloneable, StandardImage {
    /** Slash character */
    private static final String STRING_SLASH = "/";

    /** Indicator of an invisible content. */
    private static final String INVISIBLE_MARK = "...";

    /** Classification searched of this image. */
    private Searched classificationSearched = null;

    /** Rule searched of this image. */
    private Searched ruleSearched = null;

    /** Indicates whether classification searched data has been populated */
    private boolean isClassificationSearchedPopulated;

    /** Indicates whether rule searched data has been populated */
    private boolean isRuleSearchedPopulated;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractStandardImage() {
        // parent class constructor is called
    }

    /**
     * Returns a clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardImage) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBean#initStandardBean(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void initStandardBean() {
        // no initialization required
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
            return AbstractStandardImage.INVISIBLE_MARK;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameEncoded() {
        if (this.getIsVisible()) {
            return ContentInfoUtil.getNameEncoded(this.getInfoPrimaryKey());
        } else {
            return AbstractStandardImage.INVISIBLE_MARK;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameHTML() {
        if (this.getIsVisible()) {
            return ContentInfoUtil.getNameHTML(this.getInfoPrimaryKey());
        } else {
            return AbstractStandardImage.INVISIBLE_MARK;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionText() {
        if (this.getIsVisible()) {
            return ContentInfoUtil.getDescriptionText(this.getInfoPrimaryKey());
        } else {
            return AbstractStandardImage.INVISIBLE_MARK;
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
            return AbstractStandardImage.INVISIBLE_MARK;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionHTML() {
        if (this.getIsVisible()) {
            return ContentInfoUtil.getDescriptionHTML(this.getInfoPrimaryKey());
        } else {
            return AbstractStandardImage.INVISIBLE_MARK;
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
                .getAlphanumericSingleKey(PagesDictionary.IMAGE_PROPERTIES);
        StandardPage propertiesPage = (StandardPage) spf
                .findByPrimaryKey(propertiesPagePk);

        /*
         * return
         */
        String result = propertiesPage.getRelativeLocation();
        result = HTTPUtil.getAddedParameterURL(result,
                HTTPParameterConstants.IMAGE_ID, this.getPrimaryKey()
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
                .getAlphanumericSingleKey(PagesDictionary.IMAGE_PROPERTIES);
        StandardPage propertiesPage = (StandardPage) spf
                .findByPrimaryKey(propertiesPagePk);

        /*
         * return
         */
        String result = propertiesPage.getDefaultLocation();
        result = HTTPUtil.getAddedParameterURL(result,
                HTTPParameterConstants.IMAGE_ID, this.getPrimaryKey()
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
                .getAlphanumericSingleKey(PagesDictionary.IMAGE_PROPERTIES);
        StandardPage propertiesPage = (StandardPage) spf
                .findByPrimaryKey(propertiesPagePk);

        /*
         * return
         */
        String result = propertiesPage.getAbsoluteLocation();
        result = HTTPUtil.getAddedParameterURL(result,
                HTTPParameterConstants.IMAGE_ID, this.getPrimaryKey()
                        .toString());

        return result;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getRelativeLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getRelativeLocation() {
        /*
         * verify whether this image has a friendly address
         */
        if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the goto image page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_IMAGE);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getRelativeLocation()
                    + LDAPUtil.getEncodedURL(this.getFriendlyAddress())
                    + STRING_SLASH;
        } else {
            return EntityUtil
                    .getRelativeLocation(
                            PrimaryKeyUtil
                                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.IMAGES),
                            HTTPParameterConstants.IMAGE_ID, this
                                    .getPrimaryKey().toString());
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getDefaultLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDefaultLocation() {
        /*
         * verify whether this image has a friendly address
         */
        if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the goto image page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_IMAGE);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getDefaultLocation()
                    + LDAPUtil.getEncodedURL(this.getFriendlyAddress())
                    + STRING_SLASH;
        } else {
            return EntityUtil
                    .getDefaultLocation(
                            PrimaryKeyUtil
                                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.IMAGES),
                            HTTPParameterConstants.IMAGE_ID, this
                                    .getPrimaryKey().toString());
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getAbsoluteLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAbsoluteLocation() {
        /*
         * verify whether this image has a friendly address
         */
        if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the goto image page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_IMAGE);
            StandardPage sp = (StandardPage) spf.findByPrimaryKey(detailPk);

            /*
             * return
             */
            return sp.getAbsoluteLocation()
                    + LDAPUtil.getEncodedURL(this.getFriendlyAddress())
                    + STRING_SLASH;
        } else {
            return EntityUtil
                    .getAbsoluteLocation(
                            PrimaryKeyUtil
                                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.IMAGES),
                            HTTPParameterConstants.IMAGE_ID, this
                                    .getPrimaryKey().toString());
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardImage#getOnlineHelpRelativeLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getOnlineHelpRelativeLocation() {
        /*
         * verify whether this image has a friendly address
         */
        if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the goto image page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_ONLINE_HELP_IMAGE);
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
     * @see com.corendal.netapps.wiki.interfaces.StandardImage#getOnlineHelpDefaultLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getOnlineHelpDefaultLocation() {
        /*
         * verify whether this image has a friendly address
         */
        if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the goto image page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_ONLINE_HELP_IMAGE);
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
     * @see com.corendal.netapps.wiki.interfaces.StandardImage#getOnlineHelpAbsoluteLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getOnlineHelpAbsoluteLocation() {
        /*
         * verify whether this image has a friendly address
         */
        if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the goto image page
             */
            PrimaryKey detailPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.PagesDictionary.GOTO_ONLINE_HELP_IMAGE);
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
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleVersion#getAuthorStandardAccount(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardAccount getAuthorStandardAccount() {
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
        AccountHistoryManager hm = (AccountHistoryManager) pms
                .getManager(DefaultAccountHistoryManager.class);

        /*
         * get the primary key of the author account
         */
        PrimaryKey accountPk = this.getAuthorAccountPrimaryKey();

        if (accountPk == null) {
            /*
             * find the parent article
             */
            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(this.getMainParentPrimaryKey());

            /*
             * find the editor of this parent article
             */
            StandardAccount sa = parentArticle.getEditorStandardAccount();

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
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);
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
     * @see com.corendal.netapps.wiki.interfaces.StandardImage#remove(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
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
     * @see com.corendal.netapps.wiki.interfaces.StandardImage#notifySubscribers(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void notifySubscribers() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
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

        PrimaryKey articlePk = this.getMainParentPrimaryKey();
        StandardArticle parentArticle = (StandardArticle) sdf
                .findByPrimaryKey(articlePk);

        /*
         * get the sender
         */
        StandardAccount sender = am.getProxyStandardAccount();

        /*
         * get the values of the placeholders
         */
        String companyNameText = prop.getCompanyNameText();
        String applicationNameText = prop.getApplicationNameText();
        String imageNameText = this.getNameText();
        String imageDescriptionText = this.getDescriptionText();
        String articleNameText = parentArticle.getNameText();
        String versionNumberText = String.valueOf(this.getVersionNum());
        String imageLocationText = this.getAbsoluteLocation();
        imageLocationText = HTTPUtil.getAddedParameterURL(imageLocationText,
                HTTPParameterConstants.CHECK_VERSION_NUMBER, versionNumberText);

        String imageVersionNotesText = this.getComment();

        String senderNameText = sender.getFullNameText();

        /*
         * get the immediate mode
         */
        PrimaryKey immediateModesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionModesDictionary.IMMEDIATE);

        /*
         * send direct individual subscriptions
         */
        List<PrimaryKey> directIndividualRecipients = ContentUtil
                .getDirectIndividualSubscribers(this.getPrimaryKey(),
                        immediateModesPk);

        /*
         * send the email
         */
        if (directIndividualRecipients.size() > 0) {
            /*
             * get the unsubscribe page
             */
            PrimaryKey unsubscribePagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.IMAGE_UNSUBSCRIBE);
            StandardPage unsubscribePage = (StandardPage) spf
                    .findByPrimaryKey(unsubscribePagePk);
            String imageUnsubscribeText = unsubscribePage.getAbsoluteLocation();
            imageUnsubscribeText = HTTPUtil.getAddedParameterURL(
                    imageUnsubscribeText, HTTPParameterConstants.IMAGE_ID, this
                            .getPrimaryKey().toString());

            /*
             * get the messages
             */
            PrimaryKey subjectMessagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_INDIVIDUAL_IMAGE_SUBSCRIPTION_SUBJECT);
            PrimaryKey bodyMessagePk = null;

            if (StringUtil.getIsEmpty(this.getComment())) {
                bodyMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_INDIVIDUAL_IMAGE_SUBSCRIPTION_BODY);
            } else {
                bodyMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_INDIVIDUAL_IMAGE_WITH_NOTES_SUBSCRIPTION_BODY);
            }

            StandardMessage subjectMessage = (StandardMessage) smf
                    .findByPrimaryKey(subjectMessagePk);
            StandardMessage bodyMessage = (StandardMessage) smf
                    .findByPrimaryKey(bodyMessagePk);

            /*
             * fill the messages
             */
            subjectMessage.replaceMessageText(imageNameText, 1);

            bodyMessage.replaceMessageText(companyNameText, 1);
            bodyMessage.replaceMessageText(applicationNameText, 2);
            bodyMessage.replaceMessageText(senderNameText, 3);
            bodyMessage.replaceMessageText(imageNameText, 4);
            bodyMessage.replaceMessageText(articleNameText, 5);
            bodyMessage.replaceMessageText(versionNumberText, 6);
            bodyMessage.replaceMessageText(imageDescriptionText, 7);
            bodyMessage.replaceMessageText(imageLocationText, 8);
            bodyMessage.replaceMessageText(imageUnsubscribeText, 9);
            bodyMessage.replaceMessageText(imageVersionNotesText, 10);

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
            for (int i = 0; i < directIndividualRecipients.size(); i++) {
                PrimaryKey recipientPk = (PrimaryKey) directIndividualRecipients
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
        List<PrimaryKey> directGroupRecipients = ContentUtil
                .getDirectGroupSubscribers(this.getPrimaryKey(),
                        immediateModesPk);

        /*
         * send the email
         */
        if (directGroupRecipients.size() > 0) {
            /*
             * get the unsubscribe page
             */
            String imageUnsubscribeText = sender.getFullNameText();

            /*
             * get the messages
             */
            PrimaryKey subjectMessagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_GROUP_IMAGE_SUBSCRIPTION_SUBJECT);
            PrimaryKey bodyMessagePk = null;

            if (StringUtil.getIsEmpty(this.getComment())) {
                bodyMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_GROUP_IMAGE_SUBSCRIPTION_BODY);
            } else {
                bodyMessagePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_GROUP_IMAGE_WITH_NOTES_SUBSCRIPTION_BODY);
            }

            StandardMessage subjectMessage = (StandardMessage) smf
                    .findByPrimaryKey(subjectMessagePk);
            StandardMessage bodyMessage = (StandardMessage) smf
                    .findByPrimaryKey(bodyMessagePk);

            /*
             * fill the messages
             */
            subjectMessage.replaceMessageText(imageNameText, 1);

            bodyMessage.replaceMessageText(companyNameText, 1);
            bodyMessage.replaceMessageText(applicationNameText, 2);
            bodyMessage.replaceMessageText(senderNameText, 3);
            bodyMessage.replaceMessageText(imageNameText, 4);
            bodyMessage.replaceMessageText(articleNameText, 5);
            bodyMessage.replaceMessageText(versionNumberText, 6);
            bodyMessage.replaceMessageText(imageDescriptionText, 7);
            bodyMessage.replaceMessageText(imageLocationText, 8);
            bodyMessage.replaceMessageText(imageUnsubscribeText, 9);
            bodyMessage.replaceMessageText(imageVersionNotesText, 10);

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
            for (int i = 0; i < directGroupRecipients.size(); i++) {
                PrimaryKey recipientPk = (PrimaryKey) directGroupRecipients
                        .get(i);
                email.addRecipient(recipientPk, emailToPk);
            }

            /*
             * send
             */
            email.sendNow();
        }
    }
}

// end AbstractStandardImage
