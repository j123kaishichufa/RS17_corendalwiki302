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
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.AccountHistoryManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardAccountFactory;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultAccountHistoryManager;
import com.corendal.netapps.framework.core.utils.EntityUtil;
import com.corendal.netapps.framework.core.utils.HTMLFormatUtil;
import com.corendal.netapps.framework.core.utils.LessUtil;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.helpdesk.interfaces.GroupHistoryManager;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupFactory;
import com.corendal.netapps.framework.helpdesk.managers.DefaultGroupHistoryManager;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.commons.DefaultArticleBodyHighlighter;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentClassificationTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentRequestTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.RequestStatusTypesDictionary;
import com.corendal.netapps.wiki.interfaces.ArticleBodyHighlighter;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersion;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersionFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentInfo;
import com.corendal.netapps.wiki.interfaces.StandardContentInfoFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.utils.ContentInfoUtil;
import com.corendal.netapps.wiki.utils.ContentRequestUtil;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentRequest;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleVersionFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentInfoFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractStandardContentRequest is the abstract class handling information
 * about each content request of the application.
 * 
 * @version $Id: AbstractStandardContentRequest.java,v 1.1 2005/09/06 21:25:31
 *          tdanard Exp $
 */
public abstract class AbstractStandardContentRequest extends
        AbstractContentRequest implements Cloneable, StandardContentRequest {
    /** Indicates whether the current can see this content request. */
    private boolean isVisible;

    /** Indicates whether the current can access this content request. */
    private boolean isAccessible;

    /** Classification searched of this content request. */
    private Searched classificationSearched = null;

    /** Rule searched of this content request. */
    private Searched ruleSearched = null;

    /** Indicates whether data has been populated */
    private boolean isPopulated;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractStandardContentRequest() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentRequest.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentRequest) super.clone();
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
            return this.getNameText();
        } else {
            return null;
        }
    }

    /**
     * Adds a reference to an article only if not already done.
     * 
     * @param parentArticle
     *            a parent article
     * @param contentPk
     *            a primary key of a content
     * 
     * 
     */
    protected void addReferenceIfNeeded(StandardArticle parentArticle,
            PrimaryKey contentPk) {
        if ((!(parentArticle.getPrimaryKey().equals(contentPk)))
                && (!(parentArticle.getIsParentOf(contentPk)))) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardContentFactory scf = (StandardContentFactory) pfs
                    .getStandardBeanFactory(DefaultStandardContentFactory.class);

            /*
             * get the associated content
             */
            StandardContent content = (StandardContent) scf
                    .findByPrimaryKey(contentPk);

            /*
             * store the association
             */
            content.storeReference(parentArticle.getPrimaryKey(), this
                    .getPrimaryKey());
        }
    }

    /**
     * Adds references to an article based upon its HTML body.
     * 
     * @param parentArticle
     *            a parent article
     * 
     * 
     */
    protected void addReferencesIfNeededFromBodyHTML(
            StandardArticle parentArticle) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory saf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * decode the body into locations
         */
        List<String> anchorLocations = HTMLFormatUtil
                .getHTMLToAnchorLocations(parentArticle.getRawBodyHTML());

        /*
         * try to add each location
         */
        for (int i = 0; i < anchorLocations.size(); i++) {
            /*
             * get the location found in the HTML body
             */
            String currentLocation = (String) anchorLocations.get(i);

            /*
             * match this location with an article
             */
            StandardArticle currentArticle = saf
                    .findByAbsoluteOrRelativeLocation(currentLocation);

            /*
             * add a reference to that article if needed
             */
            if ((currentArticle != null) && (currentArticle.getIsFound())) {
                this.addReferenceIfNeeded(parentArticle, currentArticle
                        .getPrimaryKey());
            }
        }
    }

    /**
     * Returns the content to use as reference for visibility and accessibility.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardContent object
     */
    private StandardContent getReferenceContent() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * initialize the result
         */
        StandardContent content = null;

        /*
         * get the primary key of the "same as parent" classification
         */
        PrimaryKey sameAsParentPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);

        /*
         * get the primary key of the "approved" status
         */
        PrimaryKey approvedStatusTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RequestStatusTypesDictionary.APPROVED);

        /*
         * get the child and parent primary keys
         */
        PrimaryKey childPk = this.getChildContentPrimaryKey();
        PrimaryKey parentPk = this.getParentContentPrimaryKey();

        /*
         * get the status and the requested classification type
         */
        PrimaryKey statusTypePk = this.getStatusTypePrimaryKey();
        PrimaryKey requestedClassificationTypePk = this
                .getClassificationTypePrimaryKey();

        /*
         * verify that a classification was entered
         */
        if (requestedClassificationTypePk != null) {
            /*
             * use the parent by default when same as parent requested
             */
            if (sameAsParentPk.equals(requestedClassificationTypePk)) {
                if (parentPk != null) {
                    content = (StandardContent) srf.findByPrimaryKey(parentPk);

                    if (!(content.getIsFound())) {
                        content = null;
                    }
                }
            }
        }

        /*
         * override when request was approved and the associated content still
         * exists
         */
        if (approvedStatusTypePk.equals(statusTypePk)) {
            if (childPk != null) {
                content = (StandardContent) srf.findByPrimaryKey(childPk);

                if (!(content.getIsFound())) {
                    content = null;
                }
            } else {
                List<StandardContent> candidates = srf
                        .findEnabledByContentRequestPrimaryKey(this
                                .getPrimaryKey());

                if (candidates.size() > 0) {
                    content = (StandardContent) candidates.get(0);

                    if (!(content.getIsFound())) {
                        content = null;
                    }
                }
            }
        }

        /*
         * return
         */
        return content;
    }

    /**
     * Populates the current classification and other classification-dependant
     * variables.
     * 
     * 
     * 
     */
    private void populate() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * get the primary key of the "same as parent" classification
         */
        PrimaryKey sameAsParentPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);
        PrimaryKey accessLimitedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.ACCESS_LIMITED);
        PrimaryKey visibilityAndAccessLimitedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.VISIBILITY_AND_ACCESS_LIMITED);

        /*
         * get the requested classification type
         */
        PrimaryKey requestedClassificationTypePk = this
                .getClassificationTypePrimaryKey();

        /*
         * populate the variables
         */
        this.isPopulated = true;

        /*
         * default the visibility
         */
        this.classificationSearched = null;
        this.ruleSearched = null;
        this.isVisible = false;
        this.isAccessible = false;

        /*
         * use the reference content when available
         */
        StandardContent content = this.getReferenceContent();

        if (content != null) {
            this.classificationSearched = content.getClassificationSearched();
            this.ruleSearched = content.getRuleSearched();
            this.isVisible = content.getIsVisible();
            this.isAccessible = content.getIsAccessible();
        } else {
            /*
             * do not prevent access when no security restrictions requested
             */
            if (!(requestedClassificationTypePk.equals(sameAsParentPk))) {
                this.isVisible = (!(requestedClassificationTypePk
                        .equals(visibilityAndAccessLimitedPk)));
                this.isAccessible = this.isVisible
                        && (!(requestedClassificationTypePk
                                .equals(accessLimitedPk)));
            }
        }

        /*
         * use the access rights
         */
        if ((!(this.isVisible)) || (!(this.isAccessible))) {
            StandardAccount currentAccount = am.getProxyStandardAccount();

            if (currentAccount != null) {
                if (resm.getIsRequestApprovalAuthorized(currentAccount
                        .getPrimaryKey(), this.getPrimaryKey())
                        || resm.getIsRequestCancellationAuthorized(
                                currentAccount.getPrimaryKey(), this
                                        .getPrimaryKey())
                        || resm.getIsRequestEditingAuthorized(currentAccount
                                .getPrimaryKey(), this.getPrimaryKey())) {
                    this.isVisible = true;
                    this.isAccessible = true;
                }
            }
        }
    }

    /**
     * Populates the current classification and other classification-dependant
     * variables if not already done.
     * 
     * 
     * 
     */
    private void populateIfNeeded() {
        if ((!(this.isPopulated)) && (this.getIsFound())) {
            this.populate();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameText() {
        if (this.getContentInfoPrimaryKey() != null) {
            return ContentInfoUtil.getNameText(this.getContentInfoPrimaryKey());
        } else {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardContentFactory srf = (StandardContentFactory) pfs
                    .getStandardBeanFactory(DefaultStandardContentFactory.class);

            /*
             * get the associated content
             */
            StandardContent sr = (StandardContent) srf.findByPrimaryKey(this
                    .getChildContentPrimaryKey());

            /*
             * return
             */
            if (sr.getIsFound()) {
                return sr.getNameText();
            } else {
                return null;
            }
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameEncoded() {
        if (this.getContentInfoPrimaryKey() != null) {
            return ContentInfoUtil.getNameEncoded(this
                    .getContentInfoPrimaryKey());
        } else {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardContentFactory srf = (StandardContentFactory) pfs
                    .getStandardBeanFactory(DefaultStandardContentFactory.class);

            /*
             * get the associated content
             */
            StandardContent sr = (StandardContent) srf.findByPrimaryKey(this
                    .getChildContentPrimaryKey());

            /*
             * return
             */
            if (sr.getIsFound()) {
                return sr.getNameEncoded();
            } else {
                return null;
            }
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Named#getNameHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getNameHTML() {
        if (this.getContentInfoPrimaryKey() != null) {
            return ContentInfoUtil.getNameHTML(this.getContentInfoPrimaryKey());
        } else {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardContentFactory srf = (StandardContentFactory) pfs
                    .getStandardBeanFactory(DefaultStandardContentFactory.class);

            /*
             * get the associated content
             */
            StandardContent sr = (StandardContent) srf.findByPrimaryKey(this
                    .getChildContentPrimaryKey());

            /*
             * return
             */
            if (sr.getIsFound()) {
                return sr.getNameHTML();
            } else {
                return null;
            }
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionText(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionText() {
        if (this.getContentInfoPrimaryKey() != null) {
            return ContentInfoUtil.getDescriptionText(this
                    .getContentInfoPrimaryKey());
        } else {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardContentFactory srf = (StandardContentFactory) pfs
                    .getStandardBeanFactory(DefaultStandardContentFactory.class);

            /*
             * get the associated content
             */
            StandardContent sr = (StandardContent) srf.findByPrimaryKey(this
                    .getChildContentPrimaryKey());

            /*
             * return
             */
            if (sr.getIsFound()) {
                return sr.getDescriptionText();
            } else {
                return null;
            }
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionEncoded(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionEncoded() {
        if (this.getContentInfoPrimaryKey() != null) {
            return ContentInfoUtil.getDescriptionEncoded(this
                    .getContentInfoPrimaryKey());
        } else {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardContentFactory srf = (StandardContentFactory) pfs
                    .getStandardBeanFactory(DefaultStandardContentFactory.class);

            /*
             * get the associated content
             */
            StandardContent sr = (StandardContent) srf.findByPrimaryKey(this
                    .getChildContentPrimaryKey());

            /*
             * return
             */
            if (sr.getIsFound()) {
                return sr.getDescriptionEncoded();
            } else {
                return null;
            }
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Described#getDescriptionHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDescriptionHTML() {
        if (this.getContentInfoPrimaryKey() != null) {
            return ContentInfoUtil.getDescriptionHTML(this
                    .getContentInfoPrimaryKey());
        } else {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardContentFactory srf = (StandardContentFactory) pfs
                    .getStandardBeanFactory(DefaultStandardContentFactory.class);

            /*
             * get the associated content
             */
            StandardContent sr = (StandardContent) srf.findByPrimaryKey(this
                    .getChildContentPrimaryKey());

            /*
             * return
             */
            if (sr.getIsFound()) {
                return sr.getDescriptionHTML();
            } else {
                return null;
            }
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getRelativeLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getRelativeLocation() {
        return EntityUtil
                .getRelativeLocation(
                        PrimaryKeyUtil
                                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.CONTENT_REQUESTS),
                        HTTPParameterConstants.CONTENT_REQUEST_ID, this
                                .getPrimaryKey().toString());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getDefaultLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getDefaultLocation() {
        return EntityUtil
                .getDefaultLocation(
                        PrimaryKeyUtil
                                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.CONTENT_REQUESTS),
                        HTTPParameterConstants.CONTENT_REQUEST_ID, this
                                .getPrimaryKey().toString());
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.Located#getAbsoluteLocation(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public String getAbsoluteLocation() {
        return EntityUtil
                .getAbsoluteLocation(
                        PrimaryKeyUtil
                                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.EntitiesDictionary.CONTENT_REQUESTS),
                        HTTPParameterConstants.CONTENT_REQUEST_ID, this
                                .getPrimaryKey().toString());
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Edited#getEditorStandardAccount(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardAccount getEditorStandardAccount() {
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
         * add this account to the history
         */
        PrimaryKey accountPk = this.getEditorAccountPrimaryKey();

        if (accountPk != null) {
            hm.add(accountPk);

            return (StandardAccount) saf.findByPrimaryKey(accountPk);
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardDirectory#getAssociateEditorsStandardGroup(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
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
         * add this group to the history
         */
        PrimaryKey groupPk = this.getAssociateEditorsGroupPrimaryKey();

        if (groupPk != null) {
            hm.add(groupPk);

            return (StandardGroup) sgf.findByPrimaryKey(groupPk);
        } else {
            return null;
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

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccountHistoryManager hm = (AccountHistoryManager) pms
                .getManager(DefaultAccountHistoryManager.class);

        /*
         * add this account to the history
         */
        PrimaryKey accountPk = this.getAuthorAccountPrimaryKey();

        if (accountPk != null) {
            hm.add(accountPk);

            return (StandardAccount) saf.findByPrimaryKey(accountPk);
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Approved#getApproverStandardAccount(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardAccount getApproverStandardAccount() {
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
         * add this account to the history
         */
        PrimaryKey accountPk = this.getApproverAccountPrimaryKey();

        if (accountPk != null) {
            hm.add(accountPk);

            return (StandardAccount) saf.findByPrimaryKey(accountPk);
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Requested#getRequestorStandardAccount(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardAccount getRequestorStandardAccount() {
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
         * add this account to the history
         */
        PrimaryKey accountPk = this.getRequestorAccountPrimaryKey();

        if (accountPk != null) {
            hm.add(accountPk);

            return (StandardAccount) saf.findByPrimaryKey(accountPk);
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequest#approveImageRequest(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardImage approveImageRequest() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardImageFactory sdocf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardContentInfoFactory srif = (StandardContentInfoFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentInfoFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentHistoryManager hm = (ContentHistoryManager) pms
                .getManager(DefaultContentHistoryManager.class);

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
         * change the status of this request
         */
        this.storeApproval();

        /*
         * get the type of this request
         */
        PrimaryKey requestTypePk = this.getRequestTypePrimaryKey();

        /*
         * approve the image request
         */
        StandardImage result = null;

        if (this.getIsDone()) {
            /*
             * create the image
             */
            if (requestTypePk.equals(addTypePk)) {
                /*
                 * try to create
                 */
                result = (StandardImage) sdocf.create(this
                        .getContentInfoPrimaryKey(), this
                        .getAuthorAccountPrimaryKey(),
                        this.getFilePrimaryKey(), this
                                .getParentContentPrimaryKey(), this
                                .getPrimaryKey(), this
                                .getClassificationTypePrimaryKey(), this
                                .getRuleTypePrimaryKey(), this.getComment());

                /*
                 * store the friendly address
                 */
                if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
                    result.storeFriendlyAddress(this.getFriendlyAddress());
                }

                /*
                 * notify all subscribers
                 */
                if (this.getIsDone()) {
                    result.notifySubscribers();
                }

                /*
                 * add the image to the history
                 */
                if (this.getIsDone()) {
                    hm.add(result.getPrimaryKey());
                }
            } else if (requestTypePk.equals(editTypePk)) {
                /*
                 * get the image being updated
                 */
                StandardImage image = (StandardImage) sdocf
                        .findByPrimaryKey(this.getChildContentPrimaryKey());

                /*
                 * get the requested content info
                 */
                PrimaryKey requestedInfoPk = this.getContentInfoPrimaryKey();
                StandardContentInfo requestedInfo = (StandardContentInfo) srif
                        .findByPrimaryKey(requestedInfoPk);

                /*
                 * get the current content info
                 */
                PrimaryKey currentInfoPk = image.getInfoPrimaryKey();
                StandardContentInfo currentInfo = (StandardContentInfo) srif
                        .findByPrimaryKey(currentInfoPk);

                /*
                 * get the current friendly address
                 */
                String currentFriendlyAddress = image.getFriendlyAddress();

                /*
                 * get the current file primary key
                 */
                PrimaryKey currentFilePk = image.getFilePrimaryKey();

                /*
                 * get the current author
                 */
                StandardAccount currentAuthor = image
                        .getAuthorStandardAccount();

                /*
                 * get the current parent article
                 */
                StandardArticle currentParentArticle = (StandardArticle) sdf
                        .findByPrimaryKey(image.getMainParentPrimaryKey());

                /*
                 * get the current classification and rule types
                 */
                PrimaryKey currentClassificationTypePk = image
                        .getClassificationTypePrimaryKey();
                PrimaryKey currentRuleTypePk = image.getRuleTypePrimaryKey();

                /*
                 * initialize the isDone flag
                 */
                this.setIsDone(true);

                /*
                 * create a new version only if file primary has changed
                 */
                if (!(currentFilePk.equals(this.getFilePrimaryKey()))) {
                    /*
                     * supersede the image
                     */
                    image.supersede(this.getPrimaryKey());
                    image.storeComment(this.getComment());
                } else {
                    String currentComment = image.getComment();

                    if (!(StringUtil.getIsIdentical(currentComment, this
                            .getComment()))) {
                        image.storeComment(this.getComment());
                    }
                }

                /*
                 * compare and update the content infos
                 */
                if (!(currentInfo.getIsIdentical(requestedInfo))) {
                    image.storeInfo(requestedInfoPk);
                    this.setIsDone(image.getIsDone());

                    if (!(this.getIsDone())) {
                        this.setStoreTrace(image.getStoreTrace());
                    }
                }

                /*
                 * compare and update the author
                 */
                if (this.getIsDone()) {
                    if (!(currentAuthor.getPrimaryKey().equals(this
                            .getAuthorAccountPrimaryKey()))) {
                        image.storeAuthor(this.getAuthorAccountPrimaryKey());
                        this.setIsDone(image.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(image.getStoreTrace());
                        }
                    }
                }

                /*
                 * compare and update the friendly addresses
                 */
                if (this.getIsDone()) {
                    if (!(StringUtil.getIsIdentical(currentFriendlyAddress,
                            this.getFriendlyAddress()))) {
                        image.storeFriendlyAddress(this.getFriendlyAddress());
                        this.setIsDone(image.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(image.getStoreTrace());
                        }
                    }
                }

                /*
                 * compare and update the parent article
                 */
                if (this.getIsDone()) {
                    if (!(currentParentArticle.getPrimaryKey().equals(this
                            .getParentContentPrimaryKey()))) {
                        image.storeParentContent(this
                                .getParentContentPrimaryKey());
                        this.setIsDone(image.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(image.getStoreTrace());
                        }
                    }
                }

                /*
                 * compare and update the classification type primary key
                 */
                if (this.getIsDone()) {
                    if (!(currentClassificationTypePk.equals(this
                            .getClassificationTypePrimaryKey()))) {
                        image.storeClassificationType(this
                                .getClassificationTypePrimaryKey());
                        this.setIsDone(image.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(image.getStoreTrace());
                        }
                    }
                }

                /*
                 * compare and update the rule type primary key
                 */
                if (this.getIsDone()) {
                    if (!(currentRuleTypePk
                            .equals(this.getRuleTypePrimaryKey()))) {
                        image.storeRuleType(this.getRuleTypePrimaryKey());
                        this.setIsDone(image.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(image.getStoreTrace());
                        }
                    }
                }

                /*
                 * compare and update the file primary key
                 */
                if (this.getIsDone()) {
                    if (!(currentFilePk.equals(this.getFilePrimaryKey()))) {
                        image.storeFilePrimaryKey(this.getFilePrimaryKey());
                        this.setIsDone(image.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(image.getStoreTrace());
                        }
                    }
                }

                /*
                 * send email notifications only if file has changed
                 */
                if (!(currentFilePk.equals(this.getFilePrimaryKey()))) {
                    image.notifySubscribers();
                }

                /*
                 * add the image to the history
                 */
                if (this.getIsDone()) {
                    hm.add(image.getPrimaryKey());
                }
            } else if (requestTypePk.equals(removeTypePk)) {
                /*
                 * remove the image (the associated content requests and files
                 * will be removed one month later by cleanup)
                 */
                StandardImage image = (StandardImage) sdocf
                        .findByPrimaryKey(this.getChildContentPrimaryKey());

                if (image.getIsFound()) {
                    /*
                     * reject all related requests
                     */
                    PrimaryKey imagePk = image.getPrimaryKey();
                    ContentRequestUtil.rejectImageRelated(imagePk);

                    /*
                     * remove the image
                     */
                    image.remove(this.getPrimaryKey());

                    /*
                     * remove the image from the history
                     */
                    hm.remove(imagePk);
                }
            }
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequest#approveArticleRequest(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardArticle approveArticleRequest() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory slf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardArticleVersionFactory sdvf = (StandardArticleVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleVersionFactory.class);
        StandardContentInfoFactory srif = (StandardContentInfoFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentInfoFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentHistoryManager hm = (ContentHistoryManager) pms
                .getManager(DefaultContentHistoryManager.class);

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
         * get the primary key of the home article
         */
        PrimaryKey homeArticlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ArticlesDictionary.HOME);

        /*
         * change the status of this request
         */
        this.storeApproval();

        /*
         * get the type of this request
         */
        PrimaryKey requestTypePk = this.getRequestTypePrimaryKey();

        /*
         * create the article
         */
        StandardArticle result = null;

        if (this.getIsDone()) {
            if (requestTypePk.equals(addTypePk)) {
                /*
                 * try to create
                 */
                result = (StandardArticle) slf.createArticle(this
                        .getContentInfoPrimaryKey(), this
                        .getEditorAccountPrimaryKey(), this
                        .getAssociateEditorsGroupPrimaryKey(), this
                        .getAuthorAccountPrimaryKey(), this
                        .getParentContentPrimaryKey(), this.getPrimaryKey(),
                        this.getClassificationTypePrimaryKey(), this
                                .getRuleTypePrimaryKey(), this.getComment());

                /*
                 * store the friendly address
                 */
                if (!(StringUtil.getIsEmpty(this.getFriendlyAddress()))) {
                    result.storeFriendlyAddress(this.getFriendlyAddress());
                }

                /*
                 * add the references from the body of that article
                 */
                this.addReferencesIfNeededFromBodyHTML(result);

                /*
                 * notify all subscribers
                 */
                if (this.getIsDone()) {
                    result.notifySubscribers();
                }

                /*
                 * add this article to the history
                 */
                if (this.getIsDone()) {
                    hm.add(result.getPrimaryKey());
                }

                /*
                 * changed the whats new flag
                 */
                if (this.getIsDone()) {
                    Searched searched = result.getClassificationSearched();
                    PrimaryKey classificationTypePk = searched
                            .getClassificationTypePrimaryKey();
                    PrimaryKey noLoginRequiredPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(ContentClassificationTypesDictionary.NO_LOGIN_REQUIRED);

                    if (noLoginRequiredPk.equals(classificationTypePk)) {
                        result.storeWhatsNewFlag("Y");
                    }
                }

                /*
                 * extract the text from the raw body
                 */
                String rawBodyHTML = this.getBodyHTML();
                String bodyText = HTMLFormatUtil.getHTMLToText(rawBodyHTML);

                /*
                 * highlight the raw body
                 */
                ArticleBodyHighlighter highlighter = new DefaultArticleBodyHighlighter();
                highlighter.process(result.getPrimaryKey(), rawBodyHTML,
                        homeArticlePk, false);

                /*
                 * update the bodies
                 */
                result.storeBodies(rawBodyHTML, bodyText, highlighter
                        .getModifiedBodyHTML());

                /*
                 * update the addresses
                 */
                result.storeAddresses(highlighter.getModifiedLocations());

                /*
                 * make all versions obsolete containing that relative location
                 */
                List<StandardArticleVersion> obsoleteArticleVersions = sdvf
                        .findEnabledByAddress(result.getRelativeLocation());
                for (StandardArticleVersion currentArticleVersion : obsoleteArticleVersions) {
                    currentArticleVersion.makeObsolete();
                }
            } else if (requestTypePk.equals(editTypePk)) {
                /*
                 * get the article being updated
                 */
                StandardArticle article = (StandardArticle) slf
                        .findByPrimaryKey(this.getChildContentPrimaryKey());

                /*
                 * get the current editor
                 */
                StandardAccount currentEditor = article
                        .getEditorStandardAccount();

                /*
                 * get the associate editors
                 */
                StandardGroup currentAssociateEditors = article
                        .getAssociateEditorsStandardGroup();

                /*
                 * get the current parent article
                 */
                PrimaryKey currentParentArticlePk = article
                        .getMainParentPrimaryKey();

                /*
                 * get the requested parent article
                 */
                PrimaryKey requestedParentArticlePk = this
                        .getParentContentPrimaryKey();
                StandardArticle requestedParentArticle = null;

                if (requestedParentArticlePk != null) {
                    requestedParentArticle = (StandardArticle) sdf
                            .findByPrimaryKey(requestedParentArticlePk);
                }

                /*
                 * get the requested content info
                 */
                PrimaryKey requestedInfoPk = this.getContentInfoPrimaryKey();
                StandardContentInfo requestedInfo = (StandardContentInfo) srif
                        .findByPrimaryKey(requestedInfoPk);

                /*
                 * get the current content info
                 */
                PrimaryKey currentInfoPk = article.getInfoPrimaryKey();
                StandardContentInfo currentInfo = (StandardContentInfo) srif
                        .findByPrimaryKey(currentInfoPk);

                /*
                 * get the current friendly address and relative location
                 */
                String currentFriendlyAddress = article.getFriendlyAddress();
                String currentRelativeLocation = article.getRelativeLocation();

                /*
                 * get the current author
                 */
                StandardAccount currentAuthor = article
                        .getAuthorStandardAccount();

                /*
                 * get the current body
                 */
                String currentBody = article.getRawBodyHTML();

                /*
                 * get the current classification and rule types
                 */
                PrimaryKey currentClassificationTypePk = article
                        .getClassificationTypePrimaryKey();
                PrimaryKey currentRuleTypePk = article.getRuleTypePrimaryKey();

                /*
                 * initialize the isDone flag
                 */
                this.setIsDone(true);

                /*
                 * create a new version only if body has changed
                 */
                if (!(StringUtil
                        .getIsIdentical(currentBody, this.getBodyHTML()))) {
                    article.supersede(this.getPrimaryKey());
                    article.storeComment(this.getComment());
                } else {
                    String currentComment = article.getComment();

                    if (!(StringUtil.getIsIdentical(currentComment, this
                            .getComment()))) {
                        article.storeComment(this.getComment());
                    }
                }

                /*
                 * compare and update the content infos
                 */
                if (!(currentInfo.getIsIdentical(requestedInfo))) {
                    article.storeInfo(requestedInfoPk);
                    this.setIsDone(article.getIsDone());

                    if (!(this.getIsDone())) {
                        this.setStoreTrace(article.getStoreTrace());
                    }
                }

                /*
                 * make all versions obsolete in case of name or description
                 * change
                 */
                if (this.getIsDone()) {
                    if (!(currentInfo.getIsIdentical(requestedInfo))) {
                        List<StandardArticleVersion> obsoleteArticleVersions = sdvf
                                .findEnabledByAddress(currentRelativeLocation);
                        for (StandardArticleVersion currentArticleVersion : obsoleteArticleVersions) {
                            currentArticleVersion.makeObsolete();
                        }
                    }
                }

                /*
                 * compare and update the author
                 */
                if (this.getIsDone()) {
                    if (!(currentAuthor.getPrimaryKey().equals(this
                            .getAuthorAccountPrimaryKey()))) {
                        article.storeAuthor(this.getAuthorAccountPrimaryKey());
                        this.setIsDone(article.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(article.getStoreTrace());
                        }
                    }
                }

                /*
                 * compare and update the friendly addresses
                 */
                if (this.getIsDone()) {
                    if (!(StringUtil.getIsIdentical(currentFriendlyAddress,
                            this.getFriendlyAddress()))) {
                        article.storeFriendlyAddress(this.getFriendlyAddress());
                        this.setIsDone(article.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(article.getStoreTrace());
                        }
                    }
                }

                /*
                 * make all versions obsolete in case of friendly address change
                 */
                if (this.getIsDone()) {
                    if (!(StringUtil.getIsIdentical(currentFriendlyAddress,
                            this.getFriendlyAddress()))) {
                        List<StandardArticleVersion> obsoleteArticleVersions = sdvf
                                .findEnabledByAddress(currentRelativeLocation);
                        for (StandardArticleVersion currentArticleVersion : obsoleteArticleVersions) {
                            currentArticleVersion.makeObsolete();
                        }
                    }
                }

                /*
                 * compare and update the parent article
                 */
                if (this.getIsDone()) {
                    if ((currentParentArticlePk != null)
                            && (requestedParentArticlePk != null)) {
                        if (!(currentParentArticlePk
                                .equals(requestedParentArticlePk))) {
                            article
                                    .storeParentContent(requestedParentArticlePk);
                            this.setIsDone(article.getIsDone());

                            if (!(this.getIsDone())) {
                                this.setStoreTrace(article.getStoreTrace());
                            }
                        }
                    }
                }

                /*
                 * compare and update the classification type primary key
                 */
                if (this.getIsDone()) {
                    if (!(currentClassificationTypePk.equals(this
                            .getClassificationTypePrimaryKey()))) {
                        article.storeClassificationType(this
                                .getClassificationTypePrimaryKey());
                        this.setIsDone(article.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(article.getStoreTrace());
                        }
                    }
                }

                /*
                 * compare and update the rule type primary key
                 */
                if (this.getIsDone()) {
                    if (!(currentRuleTypePk
                            .equals(this.getRuleTypePrimaryKey()))) {
                        article.storeRuleType(this.getRuleTypePrimaryKey());
                        this.setIsDone(article.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(article.getStoreTrace());
                        }
                    }
                }

                /*
                 * compare and update the editor
                 */
                if ((this.getIsDone()) && (requestedParentArticle != null)) {
                    StandardAccount parentEditor = requestedParentArticle
                            .getEditorStandardAccount();

                    if (parentEditor.getPrimaryKey().equals(
                            this.getEditorAccountPrimaryKey())) {
                        if (article.getEditorAccountPrimaryKey() != null) { // editor

                            // record
                            // is
                            // duplicate
                            // of
                            // parent
                            // article
                            // editor
                            // record
                            article.storeEditor(null);
                            this.setIsDone(article.getIsDone());

                            if (!(this.getIsDone())) {
                                this.setStoreTrace(article.getStoreTrace());
                            }
                        }
                    } else {
                        if (!(currentEditor.getPrimaryKey().equals(this
                                .getEditorAccountPrimaryKey()))) {
                            article.storeEditor(this
                                    .getEditorAccountPrimaryKey());
                            this.setIsDone(article.getIsDone());

                            if (!(this.getIsDone())) {
                                this.setStoreTrace(article.getStoreTrace());
                            }
                        }
                    }
                } else if ((this.getIsDone())
                        && (requestedParentArticle == null)) { // this is the
                    // home page
                    if (!(currentEditor.getPrimaryKey().equals(this
                            .getEditorAccountPrimaryKey()))) {
                        article.storeEditor(this.getEditorAccountPrimaryKey());
                        this.setIsDone(article.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(article.getStoreTrace());
                        }
                    }
                }

                /*
                 * compare and update the associate editors
                 */
                if (this.getIsDone()) {
                    if ((currentAssociateEditors == null)
                            && (this.getAssociateEditorsGroupPrimaryKey() != null)) {
                        article.storeAssociateEditors(this
                                .getAssociateEditorsGroupPrimaryKey());
                        this.setIsDone(article.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(article.getStoreTrace());
                        }
                    } else if ((currentAssociateEditors != null)
                            && (this.getAssociateEditorsGroupPrimaryKey() == null)) {
                        article.storeAssociateEditors(this
                                .getAssociateEditorsGroupPrimaryKey());
                        this.setIsDone(article.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(article.getStoreTrace());
                        }
                    } else if ((currentAssociateEditors != null)
                            && (this.getAssociateEditorsGroupPrimaryKey() != null)) {
                        if (!(currentAssociateEditors.getPrimaryKey()
                                .equals(this
                                        .getAssociateEditorsGroupPrimaryKey()))) {
                            article.storeAssociateEditors(this
                                    .getAssociateEditorsGroupPrimaryKey());
                            this.setIsDone(article.getIsDone());

                            if (!(this.getIsDone())) {
                                this.setStoreTrace(article.getStoreTrace());
                            }
                        }
                    }
                }

                /*
                 * compare and update the bodies
                 */
                if (this.getIsDone()) {
                    if (!(StringUtil.getIsIdentical(currentBody, this
                            .getBodyHTML()))) {

                        /*
                         * extract the text from the raw body
                         */
                        String rawBodyHTML = this.getBodyHTML();
                        String bodyText = HTMLFormatUtil
                                .getHTMLToText(rawBodyHTML);

                        /*
                         * highlight the raw body
                         */
                        ArticleBodyHighlighter highlighter = new DefaultArticleBodyHighlighter();
                        highlighter.process(article.getPrimaryKey(),
                                rawBodyHTML, homeArticlePk, false);

                        /*
                         * update the bodies
                         */
                        article.storeBodies(rawBodyHTML, bodyText, highlighter
                                .getModifiedBodyHTML());

                        /*
                         * update the addresses
                         */
                        article.storeAddresses(highlighter
                                .getModifiedLocations());

                        this.setIsDone(article.getIsDone());

                        if (!(this.getIsDone())) {
                            this.setStoreTrace(article.getStoreTrace());
                        }
                    }
                }

                /*
                 * send email notifications only if body has changed
                 */
                if (!(StringUtil
                        .getIsIdentical(currentBody, this.getBodyHTML()))) {
                    article.notifySubscribers();
                }

                /*
                 * add the references from the body of that article
                 */
                if (this.getIsDone()) {
                    if (!(StringUtil.getIsIdentical(currentBody, this
                            .getBodyHTML()))) {
                        this.addReferencesIfNeededFromBodyHTML(article);
                    }
                }

                /*
                 * add the article to the history
                 */
                if (this.getIsDone()) {
                    hm.add(article.getPrimaryKey());
                }
            } else if (requestTypePk.equals(removeTypePk)) {

                /*
                 * remove the article (the associated content requests will be
                 * removed one month later by cleanup)
                 */
                StandardArticle article = (StandardArticle) slf
                        .findByPrimaryKey(this.getChildContentPrimaryKey());

                if (article.getIsFound()) {
                    /*
                     * get the current relative location
                     */
                    String currentRelativeLocation = article
                            .getRelativeLocation();

                    /*
                     * reject all related requests
                     */
                    PrimaryKey articlePk = article.getPrimaryKey();
                    ContentRequestUtil.rejectArticleRelated(articlePk);

                    /*
                     * remove the article
                     */
                    article.remove(this.getPrimaryKey());

                    /*
                     * remove the article from the history
                     */
                    hm.remove(articlePk);

                    /*
                     * make all versions obsolete
                     */
                    List<StandardArticleVersion> obsoleteArticleVersions = sdvf
                            .findEnabledByAddress(currentRelativeLocation);
                    for (StandardArticleVersion currentArticleVersion : obsoleteArticleVersions) {
                        currentArticleVersion.makeObsolete();
                    }
                }
            }
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequest#storeInfo(java.lang.String,
     *      java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void storeInfo(String name, String description) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentInfoFactory srif = (StandardContentInfoFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentInfoFactory.class);

        /*
         * remove any dot at the start or end of the strings
         */
        String modifiedName = LessUtil.getDotLess(name);
        String modifiedDescription = LessUtil.getDotLess(description);

        /*
         * get the content info
         */
        PrimaryKey infoPk = this.getContentInfoPrimaryKey();
        StandardContentInfo contentInfo = (StandardContentInfo) srif
                .findByPrimaryKey(infoPk);

        if (contentInfo.getIsFound()) {
            contentInfo.storeInfo(modifiedName, modifiedDescription);
            this.setIsDone(contentInfo.getIsDone());
            this.setStoreTrace(contentInfo.getStoreTrace());
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
     * @see com.corendal.netapps.wiki.interfaces.StandardContentRequest#getPreviewIconPrimaryKey(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public PrimaryKey getPreviewIconPrimaryKey() {
        /*
         * get the reference content type
         */
        PrimaryKey referenceContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.REFERENCE);
        PrimaryKey imageContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.IMAGE);
        PrimaryKey articleContentTypePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        /*
         * find the preview depending on the content type
         */
        if (this.getContentTypePrimaryKey().equals(referenceContentTypePk)) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardContentFactory srf = (StandardContentFactory) pfs
                    .getStandardBeanFactory(DefaultStandardContentFactory.class);

            /*
             * get the associated content
             */
            StandardContent sr = null;

            if (this.getChildContentPrimaryKey() != null) {
                sr = (StandardContent) srf.findByPrimaryKey(this
                        .getChildContentPrimaryKey());
            } else {
                sr = (StandardContent) srf.findByPrimaryKey(this
                        .getParentContentPrimaryKey());
            }

            /*
             * return
             */
            return sr.getIndirectPreviewIconPrimaryKey();
        } else {
            PrimaryKey filePk = this.getFilePrimaryKey();

            if (this.getContentTypePrimaryKey().equals(imageContentTypePk)) {
                return this.getDirectPreviewIconPrimaryKey();
            } else if (this.getContentTypePrimaryKey().equals(
                    articleContentTypePk)) {
                return this.getDirectPreviewIconPrimaryKey();
            } else if (filePk != null) {
                return this.getDirectPreviewIconPrimaryKey();
            } else { // this is an update of an existing content, with no
                // new

                // file uploaded

                /*
                 * get the factories of the instances used in this procedure
                 */
                FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
                StandardContentFactory srf = (StandardContentFactory) pfs
                        .getStandardBeanFactory(DefaultStandardContentFactory.class);

                /*
                 * get the associated content
                 */
                StandardContent sr = null;

                if (this.getChildContentPrimaryKey() != null) {
                    sr = (StandardContent) srf.findByPrimaryKey(this
                            .getChildContentPrimaryKey());
                } else {
                    sr = (StandardContent) srf.findByPrimaryKey(this
                            .getParentContentPrimaryKey());
                }

                /*
                 * return
                 */
                return sr.getDirectPreviewIconPrimaryKey();
            }
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Secured#getClassificationSearched(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public Searched getClassificationSearched() {
        this.populateIfNeeded();

        return this.classificationSearched;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Secured#getRuleSearched(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public Searched getRuleSearched() {
        this.populateIfNeeded();

        return this.ruleSearched;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getIsVisible(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsVisible() {
        this.populateIfNeeded();

        return this.isVisible;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.Secured#getIsAccessible(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsAccessible() {
        this.populateIfNeeded();

        return this.isAccessible;
    }
}

// end AbstractStandardContentRequest
