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
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardAccountFactory;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardAccountFactory;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupFactory;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.RolesDictionary;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentSubscription;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;

/**
 * AbstractStandardContentSubscription is the abstract class handling
 * information about each content of the application.
 * 
 * @version $Id: AbstractStandardContentSubscription.java,v 1.1 2005/09/06
 *          21:25:31 tdanard Exp $
 */
public abstract class AbstractStandardContentSubscription extends
        AbstractContentSubscription implements Cloneable,
        StandardContentSubscription {
    /** Indicates whether the current can see this content subscription. */
    private boolean isVisible;

    /** Classification searched of this content subscription. */
    private Searched classificationSearched = null;

    /** Rule searched of this content subscription. */
    private Searched ruleSearched = null;

    /** Indicates whether data has been populated */
    private boolean isPopulated;

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractStandardContentSubscription() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentSubscription.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentSubscription) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBean#initStandardBean(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void initStandardBean() {
        // no initialization required
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
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * populate the variables
         */
        this.isPopulated = true;

        PrimaryKey contentPk = this.getContentPrimaryKey();
        StandardContent content = (StandardContent) srf
                .findByPrimaryKey(contentPk);

        if (content.getIsFound()) {
            this.classificationSearched = content.getClassificationSearched();
            this.ruleSearched = content.getRuleSearched();
            this.isVisible = content.getIsVisible();
        } else {
            PrimaryKey homeContentPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ArticlesDictionary.HOME);
            content = (StandardContent) srf.findByPrimaryKey(homeContentPk);

            if (content.getIsFound()) {
                this.classificationSearched = content
                        .getClassificationSearched();
                this.ruleSearched = content.getRuleSearched();
                this.isVisible = content.getIsVisible();
            } else {
                this.classificationSearched = null;
                this.ruleSearched = null;
                this.isVisible = false;
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
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscription#getSubscriberStandardAccount(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardAccount getSubscriberStandardAccount() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardAccountFactory saf = (StandardAccountFactory) pfs
                .getStandardBeanFactory(DefaultStandardAccountFactory.class);

        /*
         * get the list of accounts
         */
        PrimaryKey rolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.SUBSCRIBER);
        List<StandardAccount> accounts = saf
                .findEnabledByRecordAndRolePrimaryKeys(this.getPrimaryKey(),
                        rolePk);

        /*
         * return
         */
        if (accounts.size() > 0) {
            return (StandardAccount) accounts.get(0);
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscription#getSubscriberStandardGroup(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardGroup getSubscriberStandardGroup() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);

        /*
         * get the list of groups
         */
        PrimaryKey rolePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(RolesDictionary.SUBSCRIBER);
        List<StandardGroup> groups = sgf.findEnabledByRecordAndRolePrimaryKeys(
                this.getPrimaryKey(), rolePk);

        /*
         * return
         */
        if (groups.size() > 0) {
            return groups.get(0);
        } else {
            return null;
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
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the content being subscribed to
         */
        PrimaryKey contentPk = this.getContentPrimaryKey();
        StandardContent content = (StandardContent) srf
                .findByPrimaryKey(contentPk);

        /*
         * return
         */
        return content.getIsAccessible();
    }
}

// end AbstractStandardContentSubscription
