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
package com.corendal.netapps.wiki.managers;

import java.util.List;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.AccountManager;
import com.corendal.netapps.framework.core.interfaces.CachedAnswerManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.ProxyManager;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.managers.AbstractManager;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultAccountManager;
import com.corendal.netapps.framework.core.managers.DefaultCachedAnswerManager;
import com.corendal.netapps.framework.core.managers.DefaultProxyManager;
import com.corendal.netapps.framework.core.throwables.BooleanCachedAnswerIsFalseException;
import com.corendal.netapps.framework.core.throwables.BooleanCachedAnswerIsTrueException;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupFactory;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentClassificationTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentRequestTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentRuleTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.ContentTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.RolesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentSubscriptionFactory;

/**
 * AbstractContentManager is the abstract class managing the access to all
 * contents.
 * 
 * @version $Id: AbstractContentManager.java,v 1.1 2005/09/06 21:25:36 tdanard
 *          Exp $
 */
public abstract class AbstractContentManager extends AbstractManager implements
        Cloneable, ContentManager {
    /** Index of the first method */
    private static final int METHOD_INDEX_1 = 1;

    /** Index of the second method */
    private static final int METHOD_INDEX_2 = 2;

    /** Index of the third method */
    private static final int METHOD_INDEX_3 = 3;

    /** Index of the fourth method */
    private static final int METHOD_INDEX_4 = 4;

    /** Index of the fifth method */
    private static final int METHOD_INDEX_5 = 5;

    /** Index of the sixth method */
    private static final int METHOD_INDEX_6 = 6;

    /** Index of the seventh method */
    private static final int METHOD_INDEX_7 = 7;

    /** Index of the eight method */
    private static final int METHOD_INDEX_8 = 8;

    /** Index of the ninth method */
    private static final int METHOD_INDEX_9 = 9;

    /** Index of the tenth method */
    private static final int METHOD_INDEX_10 = 10;

    /** Index of the eleventh method */
    private static final int METHOD_INDEX_11 = 11;

    /** Index of the twelfth method */
    private static final int METHOD_INDEX_12 = 12;

    /** Index of the thirteenth method */
    private static final int METHOD_INDEX_13 = 13;

    /** Index of the fourteenth method */
    private static final int METHOD_INDEX_14 = 14;

    /** Index of the fifteenth method */
    private static final int METHOD_INDEX_15 = 15;

    /** Index of the sixteenth method */
    private static final int METHOD_INDEX_16 = 16;

    /** Index of the seventeenth method */
    private static final int METHOD_INDEX_17 = 17;

    /** Index of the eightteenth method */
    private static final int METHOD_INDEX_18 = 18;

    /** Index of the nineteenth method */
    private static final int METHOD_INDEX_19 = 19;

    /** Index of the twentieth method */
    private static final int METHOD_INDEX_20 = 20;

    /** Index of the twenty first method */
    private static final int METHOD_INDEX_21 = 21;

    /** Index of the twenty second method */
    private static final int METHOD_INDEX_22 = 22;

    /** Index of the twenty third method */
    private static final int METHOD_INDEX_23 = 23;

    /** Index of the twenty fourth method */
    private static final int METHOD_INDEX_24 = 24;

    /**
     * Default class constructor.
     */
    protected AbstractContentManager() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractManager.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentManager) super.clone();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsRequestApprovalAuthorized(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsRequestApprovalAuthorized(PrimaryKey accountPk,
            PrimaryKey requestPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ProxyManager sm = (ProxyManager) pms
                .getManager(DefaultProxyManager.class);
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_1, accountPk, requestPk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the request
         */
        StandardContentRequest contentRequest = (StandardContentRequest) srrf
                .findByPrimaryKey(requestPk);

        /*
         * get the approver for that request
         */
        PrimaryKey approverPk = contentRequest.getApproverAccountPrimaryKey();

        /*
         * return
         */
        if ((approverPk != null)
                && (sm.getIsAtLeastLimitedProxy(accountPk, approverPk))) {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_1, accountPk, requestPk, true);
        } else {
            PrimaryKey parentArticlePk = contentRequest
                    .getParentContentPrimaryKey();

            /*
             * adjust to the article to edit or remove if applicable
             */
            PrimaryKey articleTypePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

            if (contentRequest.getContentTypePrimaryKey().equals(articleTypePk)) {
                PrimaryKey editTypePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentRequestTypesDictionary.EDIT);
                PrimaryKey removeTypePk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(ContentRequestTypesDictionary.REMOVE);

                if ((contentRequest.getRequestTypePrimaryKey()
                        .equals(editTypePk))
                        || (contentRequest.getRequestTypePrimaryKey()
                                .equals(removeTypePk))) {
                    parentArticlePk = contentRequest
                            .getChildContentPrimaryKey();
                }
            }

            /*
             * verify the editing rights against the parent articles
             */
            if (parentArticlePk != null) {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_1, accountPk, requestPk, this
                                .getIsRecursiveProxyOrAssociateEditor(
                                        accountPk, parentArticlePk));
            } else {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_1, accountPk, requestPk, false);
            }
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsRequestCancellationAuthorized(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsRequestCancellationAuthorized(PrimaryKey accountPk,
            PrimaryKey requestPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ProxyManager sm = (ProxyManager) pms
                .getManager(DefaultProxyManager.class);
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_2, accountPk, requestPk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the request
         */
        StandardContentRequest contentRequest = (StandardContentRequest) srrf
                .findByPrimaryKey(requestPk);

        /*
         * get the requestor for that request
         */
        PrimaryKey requestorPk = contentRequest.getRequestorAccountPrimaryKey();

        /*
         * return
         */
        return canswm.getBooleanAnswerToCache(this.getClass(), METHOD_INDEX_2,
                accountPk, requestPk, (requestorPk != null)
                        && sm.getIsAtLeastLimitedProxy(accountPk, requestorPk));
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsRequestEditingAuthorized(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsRequestEditingAuthorized(PrimaryKey accountPk,
            PrimaryKey requestPk) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_3, accountPk, requestPk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * return
         */
        return canswm.getBooleanAnswerToCache(this.getClass(), METHOD_INDEX_3,
                accountPk, requestPk, this.getIsRequestCancellationAuthorized(
                        accountPk, requestPk)
                        || this.getIsRequestApprovalAuthorized(accountPk,
                                requestPk));
    }

    /**
     * Returns true if an account is the editor, the proxy of the editor or a
     * member of the associated editors group.
     * 
     * @param accountPk
     *            primary key of the account to query
     * @param article
     *            standard article to query
     * 
     * 
     * @return a boolean
     */
    protected boolean getIsAtLeastLimitedProxyOrAssociateEditor(
            PrimaryKey accountPk, StandardArticle article) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ProxyManager sm = (ProxyManager) pms
                .getManager(DefaultProxyManager.class);
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_4, accountPk, article.getPrimaryKey());
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the editor of that article
         */
        StandardAccount editor = article.getEditorStandardAccount();

        /*
         * return
         */
        if ((editor != null)
                && (sm.getIsAtLeastLimitedProxy(accountPk, editor
                        .getPrimaryKey()))) {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_4, accountPk, article.getPrimaryKey(), true);
        } else {
            StandardGroup associatedEditors = article
                    .getAssociateEditorsStandardGroup();

            if (associatedEditors != null) {
                return canswm
                        .getBooleanAnswerToCache(
                                this.getClass(),
                                METHOD_INDEX_4,
                                accountPk,
                                article.getPrimaryKey(),
                                associatedEditors
                                        .getHasAtLeastLimitedProxyRecentEffectiveMember(accountPk));
            }

            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_4, accountPk, article.getPrimaryKey(), false);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsRequestAuthorized(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsRequestAuthorized(PrimaryKey accountPk,
            PrimaryKey contentPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccountManager am = (AccountManager) pms
                .getManager(DefaultAccountManager.class);
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_5, accountPk, contentPk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the content
         */
        StandardContent content = (StandardContent) srf
                .findByPrimaryKey(contentPk);

        /*
         * get all content types
         */
        PrimaryKey articlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentTypesDictionary.ARTICLE);

        /*
         * get all rule types
         */
        PrimaryKey noRequestAllowedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRuleTypesDictionary.NO_REQUEST_ALLOWED);

        /*
         * apply business rules
         */
        if (content.getIsFound()) {
            PrimaryKey contentTypePk = content.getTypePrimaryKey();

            if (contentTypePk.equals(articlePk)) {
                /*
                 * the content is an article
                 */
                if (this.getIsRecursiveProxyOrAssociateEditor(accountPk,
                        contentPk)) {
                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_5, accountPk, contentPk, true);
                } else {
                    Searched ruleTypeSearched = content.getRuleSearched();
                    PrimaryKey ruleTypePk = ruleTypeSearched
                            .getRuleTypePrimaryKey();

                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_5, accountPk, contentPk, !(ruleTypePk
                                    .equals(noRequestAllowedPk)));
                }
            } else {
                /*
                 * get the parent article
                 */
                PrimaryKey mainParentPk = content.getMainParentPrimaryKey();

                /*
                 * apply the business rules
                 */
                if (this.getIsRecursiveProxyOrAssociateEditor(accountPk,
                        mainParentPk)) {
                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_5, accountPk, contentPk, true);
                } else {
                    Searched ruleTypeSearched = content.getRuleSearched();
                    PrimaryKey ruleTypePk = ruleTypeSearched
                            .getRuleTypePrimaryKey();

                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_5, accountPk, contentPk, !(ruleTypePk
                                    .equals(noRequestAllowedPk)));
                }
            }
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_5, accountPk, contentPk, am
                            .getIsSystemAdministrationAuthorized(accountPk));
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsRecursiveProxyOrAssociateEditor(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsRecursiveProxyOrAssociateEditor(PrimaryKey accountPk,
            PrimaryKey articlePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccountManager am = (AccountManager) pms
                .getManager(DefaultAccountManager.class);
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_6, accountPk, articlePk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the article
         */
        StandardArticle article = (StandardArticle) sdf
                .findByPrimaryKey(articlePk);

        /*
         * return
         */
        if (article.getIsFound()) {
            if (getIsAtLeastLimitedProxyOrAssociateEditor(accountPk, article)) {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_6, accountPk, articlePk, true);
            } else {
                PrimaryKey parentArticlePk = article.getMainParentPrimaryKey();

                while (parentArticlePk != null) {
                    StandardArticle parentArticle = (StandardArticle) sdf
                            .findByPrimaryKey(parentArticlePk);

                    if (parentArticle.getIsFound()) {
                        if (this.getIsAtLeastLimitedProxyOrAssociateEditor(
                                accountPk, parentArticle)) {
                            return canswm.getBooleanAnswerToCache(this
                                    .getClass(), METHOD_INDEX_6, accountPk,
                                    articlePk, true);
                        }
                    } else {
                        return canswm
                                .getBooleanAnswerToCache(
                                        this.getClass(),
                                        METHOD_INDEX_6,
                                        accountPk,
                                        articlePk,
                                        am
                                                .getIsSystemAdministrationAuthorized(accountPk));
                    }

                    parentArticlePk = parentArticle.getMainParentPrimaryKey();
                }

                return canswm
                        .getBooleanAnswerToCache(
                                this.getClass(),
                                METHOD_INDEX_6,
                                accountPk,
                                articlePk,
                                am
                                        .getIsSystemAdministrationAuthorized(accountPk));
            }
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_6, accountPk, articlePk, am
                            .getIsSystemAdministrationAuthorized(accountPk));
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsRequestRequirementWaived(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public boolean getIsRequestRequirementWaived(PrimaryKey accountPk,
            PrimaryKey articlePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccountManager am = (AccountManager) pms
                .getManager(DefaultAccountManager.class);
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_7, accountPk, articlePk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the article
         */
        StandardArticle article = (StandardArticle) sdf
                .findByPrimaryKey(articlePk);

        /*
         * get all rule types
         */
        PrimaryKey noRequestRequiredPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentRuleTypesDictionary.NO_REQUEST_REQUIRED);

        /*
         * return
         */
        if (article.getIsFound()) {
            if (getIsAtLeastLimitedProxyOrAssociateEditor(accountPk, article)) {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_7, accountPk, articlePk, true);
            } else {
                /*
                 * get the rule type of the governing article
                 */
                Searched searched = article.getRuleSearched();
                PrimaryKey ruleTypePk = searched.getRuleTypePrimaryKey();

                /*
                 * verify the governing rule type
                 */
                if (ruleTypePk.equals(noRequestRequiredPk)) {
                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_7, accountPk, articlePk, true);
                } else {
                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_7, accountPk, articlePk, this
                                    .getIsRecursiveProxyOrAssociateEditor(
                                            accountPk, articlePk));
                }
            }
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_7, accountPk, articlePk, am
                            .getIsSystemAdministrationAuthorized(accountPk));
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsDirectTotalProxySubscriber(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public boolean getIsDirectTotalProxySubscriber(PrimaryKey accountPk,
            PrimaryKey contentPk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ProxyManager pm = (ProxyManager) pms
                .getManager(DefaultProxyManager.class);
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_8, accountPk, contentPk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the individual subscriptions associated with the content
         */
        PrimaryKey individualTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.INDIVIDUAL_DISTRIBUTION);
        List<StandardContentSubscription> individualContentSubscriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(contentPk,
                        individualTypesPk);

        /*
         * verify each individual subscription
         */
        for (StandardContentSubscription currentSubscription : individualContentSubscriptions) {
            StandardAccount currentSubscriber = currentSubscription
                    .getSubscriberStandardAccount();

            if ((currentSubscriber != null) && (currentSubscriber.getIsFound())) {
                if (pm.getIsTotalProxy(accountPk, currentSubscriber
                        .getPrimaryKey())) {
                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_8, accountPk, contentPk, true);
                }
            }
        }

        /*
         * get the group subscriptions associated with the content
         */
        PrimaryKey groupTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.GROUP_DISTRIBUTION);
        List<StandardContentSubscription> groupContentSubscriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(contentPk, groupTypesPk);

        /*
         * verify each group subscription
         */
        for (StandardContentSubscription currentSubscription : groupContentSubscriptions) {
            StandardGroup currentSubscriber = currentSubscription
                    .getSubscriberStandardGroup();

            if ((currentSubscriber != null) && (currentSubscriber.getIsFound())) {
                if (currentSubscriber
                        .getHasTotalProxyEffectiveMember(accountPk)) {
                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_8, accountPk, contentPk, true);
                }
            }
        }

        /*
         * return
         */
        return canswm.getBooleanAnswerToCache(this.getClass(), METHOD_INDEX_8,
                accountPk, contentPk, false);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsDeniedTotalProxyAccessGroupMember(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public boolean getIsDeniedTotalProxyAccessGroupMember(PrimaryKey accountPk,
            PrimaryKey contentPk) {
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
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_9, accountPk, contentPk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the access groups associated with the content
         */
        PrimaryKey accessGroupRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_DENIED);
        List<StandardGroup> accessGroups = sgf
                .findEnabledByRecordAndRolePrimaryKeys(contentPk,
                        accessGroupRolePk);

        /*
         * verify each group membership
         */
        for (StandardGroup currentGroup : accessGroups) {

            if (currentGroup.getHasRecentEffectiveMember(accountPk)) {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_9, accountPk, contentPk, true);
            }
        }

        /*
         * return
         */
        return canswm.getBooleanAnswerToCache(this.getClass(), METHOD_INDEX_9,
                accountPk, contentPk, false);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsAccessGroupMissing(com.corendal.netapps.wiki.interfaces.Searched)
     */
    public boolean getIsAccessGroupMissing(Searched content) {
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
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_10, content.getPrimaryKey());
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the primary key of various classification
         */
        PrimaryKey sameAsParentPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);
        PrimaryKey noLoginRequiredPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.NO_LOGIN_REQUIRED);
        PrimaryKey loginRequiredPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.LOGIN_REQUIRED);

        /*
         * get the classification of the content
         */
        content.load();
        PrimaryKey classificationTypePk = content
                .getClassificationTypePrimaryKey();

        /*
         * look at access groups only if access groups required
         */
        if ((!(classificationTypePk.equals(sameAsParentPk)))
                && (!(classificationTypePk.equals(noLoginRequiredPk)))
                && (!(classificationTypePk.equals(loginRequiredPk)))) {
            /*
             * get the access groups associated with the content
             */
            PrimaryKey accessGroupRolePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_ALLOWED);
            List<StandardGroup> accessGroups = sgf
                    .findEnabledByRecordAndRolePrimaryKeys(content
                            .getPrimaryKey(), accessGroupRolePk);

            /*
             * return
             */
            if (accessGroups.size() == 0) {
                List<PrimaryKey> subscribers = ContentUtil
                        .getRecursiveSubscribers(content.getPrimaryKey(), null);

                if (subscribers.size() == 0) {
                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_10, content.getPrimaryKey(), true);
                } else {
                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_10, content.getPrimaryKey(), false);
                }
            } else {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_10, content.getPrimaryKey(), false);
            }
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_10, content.getPrimaryKey(), false);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsAllowedTotalProxyAccessGroupMember(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public boolean getIsAllowedTotalProxyAccessGroupMember(
            PrimaryKey accountPk, PrimaryKey contentPk) {
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
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_11, accountPk, contentPk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the access groups associated with the content
         */
        PrimaryKey accessGroupRolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.ACCESS_GROUP_ALLOWED);
        List<StandardGroup> accessGroups = sgf
                .findEnabledByRecordAndRolePrimaryKeys(contentPk,
                        accessGroupRolePk);

        /*
         * verify each group membership
         */
        for (StandardGroup currentGroup : accessGroups) {

            if (currentGroup.getHasTotalProxyEffectiveMember(accountPk)) {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_11, accountPk, contentPk, true);
            }
        }

        /*
         * return
         */
        return canswm.getBooleanAnswerToCache(this.getClass(), METHOD_INDEX_11,
                accountPk, contentPk, false);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsTotalProxyAccessGroupMember(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public boolean getIsTotalProxyAccessGroupMember(PrimaryKey accountPk,
            PrimaryKey contentPk) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_12, accountPk, contentPk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * return
         */
        if (this.getIsDeniedTotalProxyAccessGroupMember(accountPk, contentPk)) {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_12, accountPk, contentPk, false);
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_12, accountPk, contentPk, this
                            .getIsAllowedTotalProxyAccessGroupMember(accountPk,
                                    contentPk));
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsContentDirectlySubscribedByIndividual(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public boolean getIsContentDirectlySubscribedByIndividual(
            PrimaryKey contentPk, PrimaryKey individualPk, PrimaryKey modePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            if (modePk != null) {
                canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                        METHOD_INDEX_13, contentPk, individualPk, modePk);
            } else {
                canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                        METHOD_INDEX_13, contentPk, individualPk);
            }
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * find the subscriptions to this content
         */
        PrimaryKey individualTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.INDIVIDUAL_DISTRIBUTION);
        List<StandardContentSubscription> contentSubscriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(contentPk,
                        individualTypesPk);

        /*
         * check the subscribers
         */
        for (StandardContentSubscription currentSubscription : contentSubscriptions) {

            if ((modePk == null)
                    || (currentSubscription.getModePrimaryKey().equals(modePk))) {
                StandardAccount subscriber = currentSubscription
                        .getSubscriberStandardAccount();

                if ((subscriber != null)
                        && (subscriber.getPrimaryKey().equals(individualPk))) {
                    if (modePk != null) {
                        return canswm.getBooleanAnswerToCache(this.getClass(),
                                METHOD_INDEX_13, contentPk, individualPk,
                                modePk, true);
                    } else {
                        return canswm.getBooleanAnswerToCache(this.getClass(),
                                METHOD_INDEX_13, contentPk, individualPk, true);
                    }
                }
            }
        }

        /*
         * return
         */
        if (modePk != null) {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_13, contentPk, individualPk, modePk, false);
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_13, contentPk, individualPk, false);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsContentDirectlySubscribedByGroupMember(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public boolean getIsContentDirectlySubscribedByGroupMember(
            PrimaryKey contentPk, PrimaryKey memberPk, PrimaryKey modePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            if (modePk != null) {
                canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                        METHOD_INDEX_14, contentPk, memberPk, modePk);
            } else {
                canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                        METHOD_INDEX_14, contentPk, memberPk);
            }
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the group subscriptions associated with the content
         */
        PrimaryKey groupTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.GROUP_DISTRIBUTION);
        List<StandardContentSubscription> groupContentSubscriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(contentPk, groupTypesPk);

        /*
         * verify each group subscription
         */
        for (StandardContentSubscription currentSubscription : groupContentSubscriptions) {

            if ((modePk == null)
                    || (currentSubscription.getModePrimaryKey().equals(modePk))) {
                StandardGroup currentSubscriber = currentSubscription
                        .getSubscriberStandardGroup();

                if ((currentSubscriber != null)
                        && (currentSubscriber.getIsFound())) {
                    if (currentSubscriber.getHasRecentEffectiveMember(memberPk)) {
                        if (modePk != null) {
                            return canswm.getBooleanAnswerToCache(this
                                    .getClass(), METHOD_INDEX_14, contentPk,
                                    memberPk, modePk, true);
                        } else {
                            return canswm.getBooleanAnswerToCache(this
                                    .getClass(), METHOD_INDEX_14, contentPk,
                                    memberPk, true);
                        }
                    }
                }
            }
        }

        /*
         * return
         */
        if (modePk != null) {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_14, contentPk, memberPk, modePk, false);
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_14, contentPk, memberPk, false);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsContentDirectlySubscribedByAccount(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public boolean getIsContentDirectlySubscribedByAccount(
            PrimaryKey contentPk, PrimaryKey accountPk, PrimaryKey modePk) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            if (modePk != null) {
                canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                        METHOD_INDEX_15, contentPk, accountPk, modePk);
            } else {
                canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                        METHOD_INDEX_15, contentPk, accountPk);
            }
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * return
         */
        if (this.getIsContentDirectlySubscribedByIndividual(contentPk,
                accountPk, modePk)) {
            if (modePk != null) {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_15, contentPk, accountPk, modePk, true);
            } else {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_15, contentPk, accountPk, true);
            }
        } else {
            if (modePk != null) {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_15, contentPk, accountPk, modePk, this
                                .getIsContentDirectlySubscribedByGroupMember(
                                        contentPk, accountPk, modePk));
            } else {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_15, contentPk, accountPk, this
                                .getIsContentDirectlySubscribedByGroupMember(
                                        contentPk, accountPk, null));
            }
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsContentDirectlySubscribedByGroup(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public boolean getIsContentDirectlySubscribedByGroup(PrimaryKey contentPk,
            PrimaryKey groupPk, PrimaryKey modePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            if (modePk != null) {
                canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                        METHOD_INDEX_16, contentPk, groupPk, modePk);
            } else {
                canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                        METHOD_INDEX_16, contentPk, groupPk);
            }
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * find the subscriptions of this content
         */
        PrimaryKey groupTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.GROUP_DISTRIBUTION);
        List<StandardContentSubscription> contentSubscriptions = srsf
                .findEnabledByContentAndTypePrimaryKeys(contentPk, groupTypesPk);

        /*
         * check the subscribers
         */
        for (StandardContentSubscription currentSubscription : contentSubscriptions) {

            if ((modePk == null)
                    || (currentSubscription.getModePrimaryKey().equals(modePk))) {
                StandardGroup subscriber = currentSubscription
                        .getSubscriberStandardGroup();

                if ((subscriber != null)
                        && (subscriber.getPrimaryKey().equals(groupPk))) {
                    if (modePk != null) {
                        return canswm.getBooleanAnswerToCache(this.getClass(),
                                METHOD_INDEX_16, contentPk, groupPk, modePk,
                                true);
                    } else {
                        return canswm.getBooleanAnswerToCache(this.getClass(),
                                METHOD_INDEX_16, contentPk, groupPk, true);
                    }
                }
            }
        }

        /*
         * return
         */
        if (modePk != null) {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_16, contentPk, groupPk, modePk, false);
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_16, contentPk, groupPk, false);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsAllowedToReadAllContent(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public boolean getIsAllowedToReadAllContent(PrimaryKey accountPk) {
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
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_17, accountPk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the home directory
         */
        PrimaryKey homePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ArticlesDictionary.HOME);
        StandardArticle home = (StandardArticle) saf.findByPrimaryKey(homePk);

        /*
         * return
         */
        return canswm.getBooleanAnswerToCache(this.getClass(), METHOD_INDEX_17,
                accountPk, this.getIsAtLeastLimitedProxyOrAssociateEditor(
                        accountPk, home));
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsVisible(com.corendal.netapps.wiki.interfaces.StandardArticle)
     */
    public boolean getIsVisible(StandardArticle article) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_18, article.getPrimaryKey());
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the access limited classification type
         */
        PrimaryKey visibilityAndAccessLimitedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.VISIBILITY_AND_ACCESS_LIMITED);

        /*
         * get the classification
         */
        Searched classificationSearched = article.getClassificationSearched();

        /*
         * compare with the current classification type
         */
        PrimaryKey classificationTypePk = classificationSearched
                .getClassificationTypePrimaryKey();

        if (classificationTypePk.equals(visibilityAndAccessLimitedPk)) {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_18, article.getPrimaryKey(), this
                            .getHasPrivilegedAccess(article));
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_18, article.getPrimaryKey(), true);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsAccessible(com.corendal.netapps.wiki.interfaces.StandardArticle)
     */
    public boolean getIsAccessible(StandardArticle article) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_19, article.getPrimaryKey());
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the access limited classification type
         */
        PrimaryKey accessLimitedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.ACCESS_LIMITED);

        /*
         * get the classification
         */
        Searched classificationSearched = article.getClassificationSearched();

        /*
         * compare with the current classification type
         */
        PrimaryKey classificationTypePk = classificationSearched
                .getClassificationTypePrimaryKey();

        if (classificationTypePk.equals(accessLimitedPk)) {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_19, article.getPrimaryKey(), this
                            .getHasPrivilegedAccess(article));
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_19, article.getPrimaryKey(), this
                            .getIsVisible(article));
        }
    }

    public boolean getIsVisible(StandardImage image) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_20, image.getPrimaryKey());
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the access limited classification type
         */
        PrimaryKey visibilityAndAccessLimitedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.VISIBILITY_AND_ACCESS_LIMITED);

        /*
         * get the classification
         */
        Searched classificationSearched = image.getClassificationSearched();

        /*
         * compare with the current classification type
         */
        PrimaryKey classificationTypePk = classificationSearched
                .getClassificationTypePrimaryKey();

        if (classificationTypePk.equals(visibilityAndAccessLimitedPk)) {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_20, image.getPrimaryKey(), this
                            .getHasPrivilegedAccess(image));
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_20, image.getPrimaryKey(), true);
        }
    }

    public boolean getIsAccessible(StandardImage image) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_21, image.getPrimaryKey());
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the access limited classification type
         */
        PrimaryKey accessLimitedPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.ACCESS_LIMITED);

        /*
         * get the classification
         */
        Searched classificationSearched = image.getClassificationSearched();

        /*
         * compare with the current classification type
         */
        PrimaryKey classificationTypePk = classificationSearched
                .getClassificationTypePrimaryKey();

        if (classificationTypePk.equals(accessLimitedPk)) {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_21, image.getPrimaryKey(), this
                            .getHasPrivilegedAccess(image));
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_21, image.getPrimaryKey(), this
                            .getIsVisible(image));
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getHasPrivilegedAccess(com.corendal.netapps.wiki.interfaces.StandardArticle)
     */
    public boolean getHasPrivilegedAccess(StandardArticle article) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ProxyManager pm = (ProxyManager) pms
                .getManager(DefaultProxyManager.class);
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_22, article.getPrimaryKey());
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the same as parent classification type
         */
        PrimaryKey sameAsParentPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);

        /*
         * ensure that the user is logged in
         */
        StandardAccount currentAccount = am.getProxyStandardAccount();

        if (currentAccount != null) {
            /*
             * get the author of this article
             */
            StandardAccount author = article.getAuthorStandardAccount();

            /*
             * compare the author with the current user
             */
            if ((author != null)
                    && (pm.getIsTotalProxy(currentAccount.getPrimaryKey(),
                            author.getPrimaryKey()))) {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_22, article.getPrimaryKey(), true);
            } else {
                /*
                 * verify whether the current user is a subscriber
                 */
                if (this.getIsDirectTotalProxySubscriber(currentAccount
                        .getPrimaryKey(), article.getPrimaryKey())) {
                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_22, article.getPrimaryKey(), true);
                } else {

                    if (this.getIsRecursiveProxyOrAssociateEditor(
                            currentAccount.getPrimaryKey(), article
                                    .getPrimaryKey())) {
                        return canswm.getBooleanAnswerToCache(this.getClass(),
                                METHOD_INDEX_22, article.getPrimaryKey(), true);
                    } else {
                        if (article.getClassificationTypePrimaryKey().equals(
                                sameAsParentPk)) {
                            Searched classificationSearched = article
                                    .getClassificationSearched();
                            return canswm.getBooleanAnswerToCache(this
                                    .getClass(), METHOD_INDEX_22, article
                                    .getPrimaryKey(), this
                                    .getIsTotalProxyAccessGroupMember(
                                            currentAccount.getPrimaryKey(),
                                            classificationSearched
                                                    .getPrimaryKey()));
                        } else {
                            return canswm.getBooleanAnswerToCache(this
                                    .getClass(), METHOD_INDEX_22, article
                                    .getPrimaryKey(), this
                                    .getIsTotalProxyAccessGroupMember(
                                            currentAccount.getPrimaryKey(),
                                            article.getPrimaryKey()));
                        }
                    }
                }
            }
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_22, article.getPrimaryKey(), false);
        }
    }

    public boolean getHasPrivilegedAccess(StandardImage image) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ProxyManager pm = (ProxyManager) pms
                .getManager(DefaultProxyManager.class);
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_23, image.getPrimaryKey());
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * get the same as parent classification type
         */
        PrimaryKey sameAsParentPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);

        /*
         * ensure that the user is logged in
         */
        StandardAccount currentAccount = am.getProxyStandardAccount();

        if (currentAccount != null) {
            /*
             * get the author of this image
             */
            StandardAccount author = image.getAuthorStandardAccount();

            /*
             * compare the author with the current user
             */
            if ((author != null)
                    && (pm.getIsTotalProxy(currentAccount.getPrimaryKey(),
                            author.getPrimaryKey()))) {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_23, image.getPrimaryKey(), true);
            } else {
                /*
                 * verify whether the current user is a subscriber
                 */
                if (this.getIsDirectTotalProxySubscriber(currentAccount
                        .getPrimaryKey(), image.getPrimaryKey())) {
                    return canswm.getBooleanAnswerToCache(this.getClass(),
                            METHOD_INDEX_23, image.getPrimaryKey(), true);
                } else {
                    PrimaryKey articlePk = image.getMainParentPrimaryKey();

                    if (this.getIsRequestRequirementWaived(currentAccount
                            .getPrimaryKey(), articlePk)) {
                        return canswm.getBooleanAnswerToCache(this.getClass(),
                                METHOD_INDEX_23, image.getPrimaryKey(), true);
                    } else {
                        if (image.getClassificationTypePrimaryKey().equals(
                                sameAsParentPk)) {
                            Searched classificationSearched = image
                                    .getClassificationSearched();
                            return canswm.getBooleanAnswerToCache(this
                                    .getClass(), METHOD_INDEX_23, image
                                    .getPrimaryKey(), this
                                    .getIsTotalProxyAccessGroupMember(
                                            currentAccount.getPrimaryKey(),
                                            classificationSearched
                                                    .getPrimaryKey()));
                        } else {
                            return canswm.getBooleanAnswerToCache(this
                                    .getClass(), METHOD_INDEX_23, image
                                    .getPrimaryKey(), this
                                    .getIsTotalProxyAccessGroupMember(
                                            currentAccount.getPrimaryKey(),
                                            image.getPrimaryKey()));
                        }
                    }
                }
            }
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_23, image.getPrimaryKey(), false);
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentManager#getIsContentRemovalAuthorized(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey)
     */
    public boolean getIsContentRemovalAuthorized(PrimaryKey accountPk) {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccountManager am = (AccountManager) pms
                .getManager(DefaultAccountManager.class);
        CachedAnswerManager canswm = (CachedAnswerManager) pms
                .getManager(DefaultCachedAnswerManager.class);

        /*
         * use the cached answer
         */
        try {
            canswm.throwBooleanCachedAnswerIfKnown(this.getClass(),
                    METHOD_INDEX_24, accountPk);
        } catch (BooleanCachedAnswerIsTrueException e) {
            return true;
        } catch (BooleanCachedAnswerIsFalseException e) {
            return false;
        }

        /*
         * check whether at least one person can remove content
         */
        PrimaryKey rolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.CONTENT_REMOVER);
        StandardAccount contentRemover = am
                .getUniqueStandardAccountWithRole(rolePk);
        if ((contentRemover != null) && (contentRemover.getIsFound())) {
            if (am.getIsRoleAuthorizedThroughAtLeastLimitedProxy(rolePk,
                    accountPk)) {
                return canswm.getBooleanAnswerToCache(this.getClass(),
                        METHOD_INDEX_24, accountPk, true);
            }

            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_24, accountPk, am
                            .getIsSystemAdministrationAuthorized(accountPk));
        } else {
            return canswm.getBooleanAnswerToCache(this.getClass(),
                    METHOD_INDEX_24, accountPk, true);
        }

    }

}
