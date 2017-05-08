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

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupFactory;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.RolesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.ContentRequest;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.utils.ContentInfoUtil;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writedatabeans.AbstractContent;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractStandardContent is the abstract class handling information about each
 * content of the application.
 * 
 * @version $Id: AbstractStandardContent.java,v 1.1 2005/09/06 21:25:31 tdanard
 *          Exp $
 */
public abstract class AbstractStandardContent extends AbstractContent implements
        Cloneable, StandardContent {
    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContent() {
        // no initialization required
    }

    /**
     * Returns a clone. Overrides AbstractContent.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContent) super.clone();
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
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the text based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getNameText();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getNameText();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameEncoded() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the text based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getNameEncoded();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getNameEncoded();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameHTML() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the text based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getNameHTML();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getNameHTML();
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
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionText() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the text based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getDescriptionText();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getDescriptionText();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionEncoded() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the text based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getDescriptionEncoded();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getDescriptionEncoded();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionHTML() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the text based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getDescriptionHTML();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getDescriptionHTML();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getRelativeLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getRelativeLocation() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the relative location based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getRelativeLocation();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getRelativeLocation();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.PropertiesLocated#getPropertiesDefaultLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPropertiesDefaultLocation() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the relative location based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getPropertiesDefaultLocation();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getPropertiesDefaultLocation();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.PropertiesLocated#getPropertiesAbsoluteLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPropertiesAbsoluteLocation() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the relative location based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getPropertiesAbsoluteLocation();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getPropertiesAbsoluteLocation();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.PropertiesLocated#getPropertiesRelativeLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getPropertiesRelativeLocation() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the relative location based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getPropertiesRelativeLocation();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getPropertiesRelativeLocation();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getDefaultLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDefaultLocation() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the relative location based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getDefaultLocation();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getDefaultLocation();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getAbsoluteLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAbsoluteLocation() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the relative location based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getAbsoluteLocation();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getAbsoluteLocation();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContent#approveNewReferenceRequest(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.wiki.interfaces.ContentRequest,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void approveNewReferenceRequest(PrimaryKey parentArticlePk,
            ContentRequest contentRequest) {
        /*
         * change the status of the request
         */
        contentRequest.storeApproval();

        /*
         * store the reference
         */
        this.storeReference(parentArticlePk, contentRequest.getPrimaryKey());
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContent#approveReferenceRemovalRequest(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.wiki.interfaces.ContentRequest,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void approveReferenceRemovalRequest(PrimaryKey parentArticlePk,
            ContentRequest contentRequest) {
        /*
         * change the status of the request
         */
        contentRequest.storeApproval();

        /*
         * store the reference
         */
        this.removeReference(parentArticlePk, contentRequest.getPrimaryKey());
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

    /**
     * Returns true if this content is a direct content in an article.
     * 
     * @param articlePk
     *            primary key of the article to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getIsDirectChildOf(PrimaryKey articlePk) {
        PrimaryKey mainParentPrimaryKey = this.getMainParentPrimaryKey();

        if (mainParentPrimaryKey != null) {
            return mainParentPrimaryKey.equals(articlePk);
        } else {
            return false;
        }
    }

    /**
     * Returns true if this content is an indirect content in an article.
     * 
     * @param articlePk
     *            primary key of the article to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getIsIndirectChildOf(PrimaryKey articlePk) {
        return this.getIsChildOf(articlePk)
                && (!(this.getIsDirectChildOf(articlePk)));
    }

    /**
     * Returns true if this content is in an article (direct or indirect).
     * 
     * @param articlePk
     *            primary key of the article to look at
     * 
     * 
     * @return a boolean
     */
    public boolean getIsChildOf(PrimaryKey articlePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * find all contents of this article
         */
        boolean isChildOf = false;
        List<StandardContent> children = srf
                .findEnabledByParentArticlePrimaryKey(articlePk);

        for (int i = 0; i < children.size(); i++) {
            StandardContent currentContent = (StandardContent) children.get(i);

            if (currentContent.equals(this)) {
                isChildOf = true;
            }
        }

        /*
         * return
         */
        return isChildOf;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContent#getIsDirectlySubscribedByIndividual(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsDirectlySubscribedByIndividual(PrimaryKey individualPk) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * return
         */
        return resm.getIsContentDirectlySubscribedByIndividual(this
                .getPrimaryKey(), individualPk, null);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContent#getIsDirectlySubscribedByGroupMember(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsDirectlySubscribedByGroupMember(PrimaryKey memberPk) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * return
         */
        return resm.getIsContentDirectlySubscribedByGroupMember(this
                .getPrimaryKey(), memberPk, null);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContent#getIsDirectlySubscribedByAccount(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsDirectlySubscribedByAccount(PrimaryKey accountPk) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * return
         */
        return resm.getIsContentDirectlySubscribedByAccount(this
                .getPrimaryKey(), accountPk, null);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContent#getIsDirectlySubscribedByGroup(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsDirectlySubscribedByGroup(PrimaryKey groupPk) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * return
         */
        return resm.getIsContentDirectlySubscribedByGroup(this.getPrimaryKey(),
                groupPk, null);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContent#getHasAccessGroup(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getHasAccessGroup(PrimaryKey groupPk) {
        return this.getHasAllowedAccessGroup(groupPk)
                || this.getHasDeniedAccessGroup(groupPk);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContent#getHasAllowedAccessGroup(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getHasAllowedAccessGroup(PrimaryKey groupPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);

        /*
         * get the list of access groups
         */
        PrimaryKey accessGroupRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_ALLOWED);
        List<StandardGroup> groups = sgf.findEnabledByRecordAndRolePrimaryKeys(
                this.getPrimaryKey(), accessGroupRolePk);

        /*
         * initialize the result
         */
        boolean result = false;

        /*
         * check the groups
         */
        for (StandardGroup currentGroup : groups) {

            if ((currentGroup != null)
                    && (currentGroup.getPrimaryKey().equals(groupPk))) {
                result = true;
            }
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContent#getHasDeniedAccessGroup(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getHasDeniedAccessGroup(PrimaryKey groupPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);

        /*
         * get the list of access groups
         */
        PrimaryKey accessGroupRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_DENIED);
        List<StandardGroup> groups = sgf.findEnabledByRecordAndRolePrimaryKeys(
                this.getPrimaryKey(), accessGroupRolePk);

        /*
         * initialize the result
         */
        boolean result = false;

        /*
         * check the groups
         */
        for (StandardGroup currentGroup : groups) {

            if ((currentGroup != null)
                    && (currentGroup.getPrimaryKey().equals(groupPk))) {
                result = true;
            }
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Secured#getClassificationSearched(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public Searched getClassificationSearched() {
        return ContentUtil.getClassificationSearched(this);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Secured#getRuleSearched(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public Searched getRuleSearched() {
        return ContentUtil.getRuleSearched(this);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getIsVisible(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsVisible() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the relative location based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getIsFound()) {
            if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
                StandardArticleFactory slf = (StandardArticleFactory) pfs
                        .getStandardBeanFactory(DefaultStandardArticleFactory.class);
                StandardArticle article = (StandardArticle) slf
                        .findByPrimaryKey(this.getPrimaryKey());

                return article.getIsVisible();
            } else {
                StandardImageFactory sdf = (StandardImageFactory) pfs
                        .getStandardBeanFactory(DefaultStandardImageFactory.class);
                StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                        .getPrimaryKey());

                return image.getIsVisible();
            }
        } else {
            return false;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Secured#getIsAccessible(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsAccessible() {
        /*
         * get the factory set
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();

        /*
         * get the relative location based upon the content type
         */
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        if (this.getTypePrimaryKey().equals(articleContentTypePk)) {
            StandardArticleFactory slf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardArticle article = (StandardArticle) slf
                    .findByPrimaryKey(this.getPrimaryKey());

            return article.getIsAccessible();
        } else {
            StandardImageFactory sdf = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardImage image = (StandardImage) sdf.findByPrimaryKey(this
                    .getPrimaryKey());

            return image.getIsAccessible();
        }
    }
}

// end AbstractStandardContent
