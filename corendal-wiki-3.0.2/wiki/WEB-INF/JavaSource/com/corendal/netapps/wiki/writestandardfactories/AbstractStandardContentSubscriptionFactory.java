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
package com.corendal.netapps.wiki.writestandardfactories;

import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardBean;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.standardfactories.AbstractStandardWriteBeanFactory;
import com.corendal.netapps.framework.core.throwables.CannotDoBeanException;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentSubscriptionTypesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentSubscription;
import com.corendal.netapps.wiki.writedatafactories.ContentSubscriptionFactory;
import com.corendal.netapps.wiki.writestandardbeans.DefaultStandardContentSubscription;

/**
 * AbstractStandardContentSubscriptionFactory is the abstract class building new
 * StandardContentSubscription instances.
 * 
 * @version $Id: AbstractStandardContentSubscriptionFactory.java,v 1.1
 *          2005/09/06 21:25:35 tdanard Exp $
 */
public abstract class AbstractStandardContentSubscriptionFactory extends
        AbstractStandardWriteBeanFactory implements Cloneable,
        StandardContentSubscriptionFactory {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentSubscriptionFactory() {
        super(DefaultStandardContentSubscription.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardWriteBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentSubscriptionFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(
                DefaultStandardContentSubscription.class.getName())) {
            return new DefaultStandardContentSubscription();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#create(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean create(PrimaryKey contentPk, PrimaryKey typePk,
            PrimaryKey modePk) {
        ContentSubscription contentSubscription = ContentSubscriptionFactory
                .create(contentPk, typePk, modePk);
        StandardContentSubscription result = null;

        if ((contentSubscription != null) && (contentSubscription.getIsDone())) {
            result = (StandardContentSubscription) this
                    .findByPrimaryKey(contentSubscription.getPrimaryKey());
            result.setIsDone(contentSubscription.getIsDone());
            result.setStoreTrace(contentSubscription.getStoreTrace());
        } else if (contentSubscription != null) {
            throw new CannotDoBeanException(contentSubscription.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#findEnabledByIndividualPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentSubscription> findEnabledByIndividualPrimaryKey(
            PrimaryKey pk) {
        List<StandardContentSubscription> standardContentSubscriptions = new ArrayList<StandardContentSubscription>();
        List<PrimaryKey> primaryKeys = AbstractContentSubscription
                .findEnabledByIndividualPrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentSubscription sr = (StandardContentSubscription) this
                    .findByPrimaryKey(srPk);
            standardContentSubscriptions.add(sr);
        }

        return standardContentSubscriptions;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#findEnabledByGroupPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentSubscription> findEnabledByGroupPrimaryKey(
            PrimaryKey pk) {
        List<StandardContentSubscription> standardContentSubscriptions = new ArrayList<StandardContentSubscription>();
        List<PrimaryKey> primaryKeys = AbstractContentSubscription
                .findEnabledByGroupPrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentSubscription sr = (StandardContentSubscription) this
                    .findByPrimaryKey(srPk);
            standardContentSubscriptions.add(sr);
        }

        return standardContentSubscriptions;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#findEnabledByIndividualAndModePrimaryKeys(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentSubscription> findEnabledByIndividualAndModePrimaryKeys(
            PrimaryKey pk, PrimaryKey modePk) {
        List<StandardContentSubscription> standardContentSubscriptions = new ArrayList<StandardContentSubscription>();
        List<PrimaryKey> primaryKeys = AbstractContentSubscription
                .findEnabledByIndividualAndModePrimaryKeys(pk, modePk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentSubscription sr = (StandardContentSubscription) this
                    .findByPrimaryKey(srPk);
            standardContentSubscriptions.add(sr);
        }

        return standardContentSubscriptions;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#findEnabledByGroupAndModePrimaryKeys(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentSubscription> findEnabledByGroupAndModePrimaryKeys(
            PrimaryKey pk, PrimaryKey modePk) {
        List<StandardContentSubscription> standardContentSubscriptions = new ArrayList<StandardContentSubscription>();
        List<PrimaryKey> primaryKeys = AbstractContentSubscription
                .findEnabledByGroupAndModePrimaryKeys(pk, modePk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentSubscription sr = (StandardContentSubscription) this
                    .findByPrimaryKey(srPk);
            standardContentSubscriptions.add(sr);
        }

        return standardContentSubscriptions;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#findEnabledByContentPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentSubscription> findEnabledByContentPrimaryKey(
            PrimaryKey pk) {
        List<StandardContentSubscription> standardContentSubscriptions = new ArrayList<StandardContentSubscription>();
        List<PrimaryKey> primaryKeys = AbstractContentSubscription
                .findEnabledByContentPrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentSubscription sr = (StandardContentSubscription) this
                    .findByPrimaryKey(srPk);
            standardContentSubscriptions.add(sr);
        }

        return standardContentSubscriptions;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#findEnabledByModePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentSubscription> findEnabledByModePrimaryKey(
            PrimaryKey pk) {
        List<StandardContentSubscription> standardContentSubscriptions = new ArrayList<StandardContentSubscription>();
        List<PrimaryKey> primaryKeys = AbstractContentSubscription
                .findEnabledByModePrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentSubscription sr = (StandardContentSubscription) this
                    .findByPrimaryKey(srPk);
            standardContentSubscriptions.add(sr);
        }

        return standardContentSubscriptions;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#findEnabledByTypePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentSubscription> findEnabledByTypePrimaryKey(
            PrimaryKey pk) {
        List<StandardContentSubscription> standardContentSubscriptions = new ArrayList<StandardContentSubscription>();
        List<PrimaryKey> primaryKeys = AbstractContentSubscription
                .findEnabledByTypePrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentSubscription sr = (StandardContentSubscription) this
                    .findByPrimaryKey(srPk);
            standardContentSubscriptions.add(sr);
        }

        return standardContentSubscriptions;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#findEnabledByContentAndTypePrimaryKeys(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentSubscription> findEnabledByContentAndTypePrimaryKeys(
            PrimaryKey pk, PrimaryKey typePk) {
        List<StandardContentSubscription> standardContentSubscriptions = new ArrayList<StandardContentSubscription>();
        List<PrimaryKey> primaryKeys = AbstractContentSubscription
                .findEnabledByContentAndTypePrimaryKeys(pk, typePk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContentSubscription sr = (StandardContentSubscription) this
                    .findByPrimaryKey(srPk);
            standardContentSubscriptions.add(sr);
        }

        return standardContentSubscriptions;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#findEnabledByContentAndIndividualSubscriberPrimaryKeys(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentSubscription> findEnabledByContentAndIndividualSubscriberPrimaryKeys(
            PrimaryKey contentPk, PrimaryKey subscriberPk) {
        List<StandardContentSubscription> results = new ArrayList<StandardContentSubscription>();
        PrimaryKey individualTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.INDIVIDUAL_DISTRIBUTION);
        List<StandardContentSubscription> contentSubscriptions = this
                .findEnabledByContentAndTypePrimaryKeys(contentPk,
                        individualTypesPk);

        for (int i = 0; i < contentSubscriptions.size(); i++) {
            StandardContentSubscription currentSubscription = (StandardContentSubscription) contentSubscriptions
                    .get(i);
            StandardAccount subscriber = currentSubscription
                    .getSubscriberStandardAccount();

            if ((subscriber != null)
                    && (subscriber.getPrimaryKey().equals(subscriberPk))) {
                results.add(currentSubscription);
            }
        }

        return results;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory#findEnabledByContentAndGroupSubscriberPrimaryKeys(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentSubscription> findEnabledByContentAndGroupSubscriberPrimaryKeys(
            PrimaryKey contentPk, PrimaryKey subscriberPk) {
        List<StandardContentSubscription> results = new ArrayList<StandardContentSubscription>();
        PrimaryKey groupTypesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ContentSubscriptionTypesDictionary.GROUP_DISTRIBUTION);
        List<StandardContentSubscription> contentSubscriptions = this
                .findEnabledByContentAndTypePrimaryKeys(contentPk, groupTypesPk);

        for (int i = 0; i < contentSubscriptions.size(); i++) {
            StandardContentSubscription currentSubscription = (StandardContentSubscription) contentSubscriptions
                    .get(i);
            StandardGroup subscriber = currentSubscription
                    .getSubscriberStandardGroup();

            if ((subscriber != null)
                    && (subscriber.getPrimaryKey().equals(subscriberPk))) {
                results.add(currentSubscription);
            }
        }

        return results;
    }
}

// end AbstractStandardContentSubscriptionFactory
