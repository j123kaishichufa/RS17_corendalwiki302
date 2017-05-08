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

import java.util.Collections;
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccountHistoryManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardAccountFactory;
import com.corendal.netapps.framework.core.managers.DefaultAccountHistoryManager;
import com.corendal.netapps.framework.core.utils.EntityUtil;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersion;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersionFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.utils.ArticleUtil;
import com.corendal.netapps.wiki.utils.ContentInfoUtil;
import com.corendal.netapps.wiki.writedatabeans.AbstractArticleVersion;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleVersionFactory;

/**
 * AbstractStandardArticleVersion is the abstract class handling information
 * about each article version of the application.
 * 
 * @version $Id: AbstractStandardArticleVersion.java,v 1.1 2005/09/06 21:25:31
 *          tdanard Exp $
 */
public abstract class AbstractStandardArticleVersion extends
        AbstractArticleVersion implements Cloneable, StandardArticleVersion {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardArticleVersion() {
        // no initialization required
    }

    /**
     * Returns a clone. Overrides AbstractArticleVersion.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardArticleVersion) super.clone();
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

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameText() {
        return ContentInfoUtil.getNameText(this.getInfoPrimaryKey());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameEncoded() {
        return ContentInfoUtil.getNameEncoded(this.getInfoPrimaryKey());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameHTML() {
        return ContentInfoUtil.getNameHTML(this.getInfoPrimaryKey());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionText() {
        return ContentInfoUtil.getDescriptionText(this.getInfoPrimaryKey());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionEncoded() {
        return ContentInfoUtil.getDescriptionEncoded(this.getInfoPrimaryKey());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionHTML() {
        return ContentInfoUtil.getDescriptionHTML(this.getInfoPrimaryKey());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getRelativeLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getRelativeLocation() {
        if (this.getCurrentFlag().equals("Y")) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardArticleFactory sdf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);

            /*
             * find the associated article
             */
            StandardArticle sd = (StandardArticle) sdf.findByPrimaryKey(this
                    .getContentPrimaryKey());

            /*
             * return
             */
            return sd.getRelativeLocation();
        } else {
            return EntityUtil
                    .getRelativeLocation(
                            PrimaryKeyUtil
                                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.ARTICLE_VERSIONS),
                            HTTPParameterConstants.ARTICLE_VERSION_ID, this
                                    .getPrimaryKey().toString());
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getDefaultLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDefaultLocation() {
        if (this.getCurrentFlag().equals("Y")) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardArticleFactory sdf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);

            /*
             * find the associated article
             */
            StandardArticle sd = (StandardArticle) sdf.findByPrimaryKey(this
                    .getContentPrimaryKey());

            /*
             * return
             */
            return sd.getDefaultLocation();
        } else {
            return EntityUtil
                    .getDefaultLocation(
                            PrimaryKeyUtil
                                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.ARTICLE_VERSIONS),
                            HTTPParameterConstants.ARTICLE_VERSION_ID, this
                                    .getPrimaryKey().toString());
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getAbsoluteLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAbsoluteLocation() {
        if (this.getCurrentFlag().equals("Y")) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardArticleFactory sdf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);

            /*
             * find the associated article
             */
            StandardArticle sd = (StandardArticle) sdf.findByPrimaryKey(this
                    .getContentPrimaryKey());

            /*
             * return
             */
            return sd.getAbsoluteLocation();
        } else {
            return EntityUtil
                    .getAbsoluteLocation(
                            PrimaryKeyUtil
                                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.ARTICLE_VERSIONS),
                            HTTPParameterConstants.ARTICLE_VERSION_ID, this
                                    .getPrimaryKey().toString());
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleVersion#getStandardArticle(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardArticle getStandardArticle() {
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
        ContentHistoryManager hm = (ContentHistoryManager) pms
                .getManager(DefaultContentHistoryManager.class);

        /*
         * get the associated article
         */
        PrimaryKey articlePk = this.getContentPrimaryKey();
        StandardArticle article = (StandardArticle) saf
                .findByPrimaryKey(articlePk);

        /*
         * add the article found to the history
         */
        if ((article != null) && (article.getIsFound())) {
            hm.add(article.getPrimaryKey());
        }

        /*
         * return
         */
        return article;
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
             * find the associated article
             */
            PrimaryKey articlePk = this.getContentPrimaryKey();
            StandardArticle article = (StandardArticle) sdf
                    .findByPrimaryKey(articlePk);

            /*
             * return
             */
            return article.getAuthorStandardAccount();
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
        return this.getNameText();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Matched#getMatchNameEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getMatchNameEncoded() {
        return this.getNameEncoded();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Matched#getMatchNameHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getMatchNameHTML() {
        return this.getNameHTML();
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
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleVersion#remove(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void remove() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleVersionFactory sdvf = (StandardArticleVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleVersionFactory.class);

        /*
         * reload to prevent caching issues
         */
        this.clearCacheAndLoad();

        /*
         * change the current flag
         */
        if (this.getCurrentFlag().equals("Y")) {
            int versionNum = this.getVersionNum();

            /*
             * get the list of article versions for the article
             */
            PrimaryKey articlePk = this.getContentPrimaryKey();
            List<StandardArticleVersion> articleVersions = sdvf
                    .findEnabledByArticlePrimaryKey(articlePk);
            Collections.reverse(articleVersions); // sorted by descending

            // version
            // numbers

            /*
             * find the most recent version after this version
             */
            boolean isOtherVersionFound = false;

            for (int i = 0; i < articleVersions.size(); i++) {
                if (!(isOtherVersionFound)) {
                    StandardArticleVersion currentArticleVersion = (StandardArticleVersion) articleVersions
                            .get(i);
                    currentArticleVersion.load(); // to prevent caching issues

                    int currentVersionNum = currentArticleVersion
                            .getVersionNum();

                    if (currentVersionNum < versionNum) {
                        isOtherVersionFound = true;
                        currentArticleVersion.storeCurrentFlag("Y");
                    }
                }
            }
        }

        /*
         * remove this article version
         */
        super.remove();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardArticleVersion#getBodyHTML(boolean,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getBodyHTML(boolean isOnlineHelp, PrimaryKey homeArticlePk) {
        String bodyHTML = this.getBodyHTML();
        if (StringUtil.getIsEmpty(bodyHTML)) {
            return ArticleUtil.getHighlightedBody(this.getStandardArticle()
                    .getPrimaryKey(), this.getRawBodyHTML(), homeArticlePk,
                    isOnlineHelp);

        } else {
            return bodyHTML;
        }
    }
}

// end AbstractStandardArticleVersion
