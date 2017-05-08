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
package com.corendal.netapps.wiki.blocks;

import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardAccountFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.utils.LocaleUtil;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.helpdesk.dictionaries.MessagesDictionary;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;

/**
 * AbstractAnyArticlesViewBlock is the class describing and printing all
 * articles of an editor.
 * 
 * @version $Id: AbstractAnyArticlesViewBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractAnyArticlesViewBlock extends
        AbstractContentsComplexResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractAnyArticlesViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentsComplexResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractAnyArticlesViewBlock) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * initialize the standard results block
         */
        super.initStandardBlock();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * set the entity
         */
        PrimaryKey articlesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLES);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(articlesPk);
        this.setStandardEntity(entity);

        /*
         * get the viewed account
         */
        StandardAccount account = this.getViewedAccount();

        /*
         * verify that the account was found
         */
        if ((account != null) && (account.getIsFound())) {
            /*
             * get the something for account message
             */
            PrimaryKey somethingforAccountPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_SOMETHING_FOR_ACCOUNT);
            StandardMessage somethingforAccount = (StandardMessage) smf
                    .findByPrimaryKey(somethingforAccountPk);

            /*
             * add the current block name and the account name to the message
             */
            somethingforAccount
                    .replaceMessageText(this.getCurrentNameText(), 1);
            somethingforAccount.replaceMessageEncoded(this
                    .getCurrentNameEncoded(), 1);
            somethingforAccount
                    .replaceMessageHTML(this.getCurrentNameHTML(), 1);
            somethingforAccount
                    .replaceMessageText(account.getFullNameText(), 2);
            somethingforAccount.replaceMessageEncoded(account
                    .getFullNameEncoded(), 2);
            somethingforAccount.replaceMessageHTML(
                    account.getFullNameEncoded(), 2);

            /*
             * apply locale rules
             */
            LocaleUtil
                    .fixSomethingForAccountStandardMessage(somethingforAccount);

            /*
             * modify the name of this block
             */
            this
                    .setCurrentNameText(somethingforAccount
                            .getCurrentMessageText());
            this.setCurrentNameEncoded(somethingforAccount
                    .getCurrentMessageEncoded());
            this
                    .setCurrentNameHTML(somethingforAccount
                            .getCurrentMessageHTML());
        }
    }

    /**
     * Returns the list of article to print. Overrides
     * AbstractContentsComplexResultsBlock.getContentsFound.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    @Override
    protected List getContentsFound() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * The primay key comes from the account id in the HTTP request.
         */
        PrimaryKey authorPk = this.getRecordPrimaryKey();

        /*
         * build the list of articles
         */
        List<StandardArticle> articlesFound;

        if (authorPk != null) {
            articlesFound = sdf.findEnabledByAuthorPrimaryKey(authorPk);
        } else {
            articlesFound = new ArrayList<StandardArticle>();
        }

        /*
         * return
         */
        return articlesFound;
    }

    /**
     * Returns the standard account to be viewed.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.core.interfaces.StandardAccount
     *         object
     */
    public StandardAccount getViewedAccount() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory sdocf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);

        /*
         * get the primary key of the requested account
         */
        PrimaryKey accountPk = this.getRecordPrimaryKey();

        if (accountPk != null) {
            StandardAccount account = (StandardAccount) sdocf
                    .findByPrimaryKey(accountPk);

            if (account.getIsFound()) {
                return account;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordPrimaryKey(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
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
        String accountId = rm
                .getParameter(this.getRecordPrimaryDataParameter());

        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(accountId);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordPrimaryDataParameter(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordPrimaryDataParameter() {
        return com.corendal.netapps.framework.core.constants.HTTPParameterConstants.ACCOUNT_ID;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordName(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordName() {
        StandardAccount sp = this.getViewedAccount();

        if (sp != null) {
            return sp.getFullNameText();
        } else {
            return null;
        }
    }
}

// end AbstractAnyArticlesViewBlock
