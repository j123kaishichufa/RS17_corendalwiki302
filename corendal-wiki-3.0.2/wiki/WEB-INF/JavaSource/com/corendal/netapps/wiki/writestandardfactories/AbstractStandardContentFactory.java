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

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.standardfactories.AbstractStandardWriteBeanFactory;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroup;
import com.corendal.netapps.framework.helpdesk.interfaces.StandardGroupFactory;
import com.corendal.netapps.framework.helpdesk.writestandardfactories.DefaultStandardGroupFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscription;
import com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory;
import com.corendal.netapps.wiki.writedatabeans.AbstractContent;
import com.corendal.netapps.wiki.writestandardbeans.DefaultStandardContent;

/**
 * AbstractStandardContentFactory is the abstract class building new
 * StandardContent instances.
 * 
 * @version $Id: AbstractStandardContentFactory.java,v 1.1 2005/09/06 21:25:35
 *          tdanard Exp $
 */
public abstract class AbstractStandardContentFactory extends
        AbstractStandardWriteBeanFactory implements Cloneable,
        StandardContentFactory {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentFactory() {
        super(DefaultStandardContent.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardWriteBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(DefaultStandardContent.class.getName())) {
            return new DefaultStandardContent();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findEnabledByParentArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContent> findEnabledByParentArticlePrimaryKey(
            PrimaryKey pk) {
        List<StandardContent> standardContents = new ArrayList<StandardContent>();
        List<PrimaryKey> primaryKeys = AbstractContent
                .findEnabledByParentArticlePrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContent sr = (StandardContent) this.findByPrimaryKey(srPk);
            standardContents.add(sr);
        }

        return standardContents;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findCountOfEnabledByParentArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public int findCountOfEnabledByParentArticlePrimaryKey(PrimaryKey pk) {
        List<PrimaryKey> primaryKeys = AbstractContent
                .findEnabledByParentArticlePrimaryKey(pk);

        /*
         * find the data beans
         */
        return primaryKeys.size();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findEnabledPrimaryKeysByParentArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByParentArticlePrimaryKey(
            PrimaryKey pk) {
        return AbstractContent.findEnabledByParentArticlePrimaryKey(pk);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findDirectEnabledByParentArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContent> findDirectEnabledByParentArticlePrimaryKey(
            PrimaryKey pk) {
        List<StandardContent> standardContents = new ArrayList<StandardContent>();
        List<PrimaryKey> primaryKeys = AbstractContent
                .findDirectEnabledByParentArticlePrimaryKey(pk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContent sr = (StandardContent) this.findByPrimaryKey(srPk);
            standardContents.add(sr);
        }

        return standardContents;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findCountOfDirectEnabledByParentArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public int findCountOfDirectEnabledByParentArticlePrimaryKey(PrimaryKey pk) {
        List<PrimaryKey> primaryKeys = AbstractContent
                .findDirectEnabledByParentArticlePrimaryKey(pk);

        /*
         * find the data beans
         */
        return primaryKeys.size();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findDirectEnabledPrimaryKeysByParentArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findDirectEnabledPrimaryKeysByParentArticlePrimaryKey(
            PrimaryKey pk) {
        return AbstractContent.findDirectEnabledByParentArticlePrimaryKey(pk);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findEnabledBySubscriberPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContent> findEnabledBySubscriberPrimaryKey(PrimaryKey pk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);

        /*
         * initialize the result
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        /*
         * add all individual subscriptions
         */
        List<StandardContentSubscription> individualSubscriptions = srsf
                .findEnabledByIndividualPrimaryKey(pk);

        for (int i = 0; i < individualSubscriptions.size(); i++) {
            StandardContentSubscription currentSubscription = (StandardContentSubscription) individualSubscriptions
                    .get(i);
            PrimaryKey contentPk = currentSubscription.getContentPrimaryKey();

            if (!(primaryKeys.contains(contentPk))) {
                primaryKeys.add(contentPk);
            }
        }

        /*
         * get all groups of the subscriber
         */
        List<StandardGroup> groupsFound = sgf
                .findEnabledByRecentEffectiveMemberPrimaryKey(pk);

        for (int i = 0; i < groupsFound.size(); i++) {
            StandardGroup currentGroup = (StandardGroup) groupsFound.get(i);

            /*
             * add all group subscriptions
             */
            List<StandardContentSubscription> groupSubscriptions = srsf
                    .findEnabledByGroupPrimaryKey(currentGroup.getPrimaryKey());

            for (int j = 0; j < groupSubscriptions.size(); j++) {
                StandardContentSubscription currentSubscription = (StandardContentSubscription) groupSubscriptions
                        .get(j);
                PrimaryKey contentPk = currentSubscription
                        .getContentPrimaryKey();

                if (!(primaryKeys.contains(contentPk))) {
                    primaryKeys.add(contentPk);
                }
            }
        }

        /*
         * find the data beans
         */
        List<StandardContent> standardContents = new ArrayList<StandardContent>();

        for (PrimaryKey srPk : primaryKeys) {
            StandardContent sr = (StandardContent) this.findByPrimaryKey(srPk);
            standardContents.add(sr);
        }

        return standardContents;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findEnabledPrimaryKeysBySubscriberPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysBySubscriberPrimaryKey(
            PrimaryKey pk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);

        /*
         * initialize the result
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        /*
         * add all individual subscriptions
         */
        List<StandardContentSubscription> individualSubscriptions = srsf
                .findEnabledByIndividualPrimaryKey(pk);

        for (int i = 0; i < individualSubscriptions.size(); i++) {
            StandardContentSubscription currentSubscription = (StandardContentSubscription) individualSubscriptions
                    .get(i);
            PrimaryKey contentPk = currentSubscription.getContentPrimaryKey();

            if (!(primaryKeys.contains(contentPk))) {
                primaryKeys.add(contentPk);
            }
        }

        /*
         * get all group of the subscriber
         */
        List<StandardGroup> groupsFound = sgf
                .findEnabledByRecentEffectiveMemberPrimaryKey(pk);

        /*
         * get all group memberships of the subscriber
         */
        for (int i = 0; i < groupsFound.size(); i++) {
            StandardGroup currentGroup = (StandardGroup) groupsFound.get(i);

            /*
             * add all group subscriptions
             */
            List<StandardContentSubscription> groupSubscriptions = srsf
                    .findEnabledByGroupPrimaryKey(currentGroup.getPrimaryKey());

            for (int j = 0; j < groupSubscriptions.size(); j++) {
                StandardContentSubscription currentSubscription = (StandardContentSubscription) groupSubscriptions
                        .get(j);
                PrimaryKey contentPk = currentSubscription
                        .getContentPrimaryKey();

                if (!(primaryKeys.contains(contentPk))) {
                    primaryKeys.add(contentPk);
                }
            }
        }

        /*
         * return null;
         */
        return primaryKeys;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findEnabledBySubscriberAndModePrimaryKeys(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContent> findEnabledBySubscriberAndModePrimaryKeys(
            PrimaryKey pk, PrimaryKey modePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);

        /*
         * initialize the result
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        /*
         * add all individual subscriptions
         */
        List<StandardContentSubscription> individualSubscriptions = srsf
                .findEnabledByIndividualAndModePrimaryKeys(pk, modePk);

        for (int i = 0; i < individualSubscriptions.size(); i++) {
            StandardContentSubscription currentSubscription = (StandardContentSubscription) individualSubscriptions
                    .get(i);
            PrimaryKey contentPk = currentSubscription.getContentPrimaryKey();

            if (!(primaryKeys.contains(contentPk))) {
                primaryKeys.add(contentPk);
            }
        }

        /*
         * get all groups of the subscriber
         */
        List<StandardGroup> groupsFound = sgf
                .findEnabledByRecentEffectiveMemberPrimaryKey(pk);

        for (int i = 0; i < groupsFound.size(); i++) {
            StandardGroup currentGroup = (StandardGroup) groupsFound.get(i);

            /*
             * add all group subscriptions
             */
            List<StandardContentSubscription> groupSubscriptions = srsf
                    .findEnabledByGroupAndModePrimaryKeys(currentGroup
                            .getPrimaryKey(), modePk);

            for (int j = 0; j < groupSubscriptions.size(); j++) {
                StandardContentSubscription currentSubscription = (StandardContentSubscription) groupSubscriptions
                        .get(j);
                PrimaryKey contentPk = currentSubscription
                        .getContentPrimaryKey();

                if (!(primaryKeys.contains(contentPk))) {
                    primaryKeys.add(contentPk);
                }
            }
        }

        /*
         * find the data beans
         */
        List<StandardContent> standardContents = new ArrayList<StandardContent>();

        for (PrimaryKey srPk : primaryKeys) {
            StandardContent sr = (StandardContent) this.findByPrimaryKey(srPk);
            standardContents.add(sr);
        }

        return standardContents;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findEnabledPrimaryKeysBySubscriberAndModePrimaryKeys(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysBySubscriberAndModePrimaryKeys(
            PrimaryKey pk, PrimaryKey modePk) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentSubscriptionFactory srsf = (StandardContentSubscriptionFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentSubscriptionFactory.class);
        StandardGroupFactory sgf = (StandardGroupFactory) pfs
                .getStandardBeanFactory(DefaultStandardGroupFactory.class);

        /*
         * initialize the result
         */
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();

        /*
         * add all individual subscriptions
         */
        List<StandardContentSubscription> individualSubscriptions = srsf
                .findEnabledByIndividualAndModePrimaryKeys(pk, modePk);

        for (int i = 0; i < individualSubscriptions.size(); i++) {
            StandardContentSubscription currentSubscription = (StandardContentSubscription) individualSubscriptions
                    .get(i);
            PrimaryKey contentPk = currentSubscription.getContentPrimaryKey();

            if (!(primaryKeys.contains(contentPk))) {
                primaryKeys.add(contentPk);
            }
        }

        /*
         * get all group of the subscriber
         */
        List<StandardGroup> groupsFound = sgf
                .findEnabledByRecentEffectiveMemberPrimaryKey(pk);

        /*
         * get all group memberships of the subscriber
         */
        for (int i = 0; i < groupsFound.size(); i++) {
            StandardGroup currentGroup = (StandardGroup) groupsFound.get(i);

            /*
             * add all group subscriptions
             */
            List<StandardContentSubscription> groupSubscriptions = srsf
                    .findEnabledByGroupAndModePrimaryKeys(currentGroup
                            .getPrimaryKey(), modePk);

            for (int j = 0; j < groupSubscriptions.size(); j++) {
                StandardContentSubscription currentSubscription = (StandardContentSubscription) groupSubscriptions
                        .get(j);
                PrimaryKey contentPk = currentSubscription
                        .getContentPrimaryKey();

                if (!(primaryKeys.contains(contentPk))) {
                    primaryKeys.add(contentPk);
                }
            }
        }

        /*
         * return null;
         */
        return primaryKeys;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findRecursiveEnabledPrimaryKeysByParentArticlePrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findRecursiveEnabledPrimaryKeysByParentArticlePrimaryKey(
            PrimaryKey pk) {
        /*
         * initialize the result
         */
        List<PrimaryKey> result = new ArrayList<PrimaryKey>();

        /*
         * find the contents under the current article
         */
        List<PrimaryKey> contents = this
                .findDirectEnabledPrimaryKeysByParentArticlePrimaryKey(pk);

        /*
         * add those contents to the list
         */
        result.addAll(contents);

        /*
         * add the contents in these direct articles
         */
        for (PrimaryKey articlePk : contents) {
            List<PrimaryKey> partialResult = this
                    .findRecursiveEnabledPrimaryKeysByParentArticlePrimaryKey(articlePk);
            result.addAll(partialResult);
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentFactory#findEnabledByContentRequestPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContent> findEnabledByContentRequestPrimaryKey(
            PrimaryKey contentRequestPk) {
        List<StandardContent> standardContents = new ArrayList<StandardContent>();
        List<PrimaryKey> primaryKeys = AbstractContent
                .findEnabledByContentRequestPrimaryKey(contentRequestPk);

        for (PrimaryKey srPk : primaryKeys) {
            StandardContent sr = (StandardContent) this.findByPrimaryKey(srPk);
            standardContents.add(sr);
        }

        return standardContents;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#findPrimaryKeysByEnabledFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<PrimaryKey> findPrimaryKeysByEnabledFlag(String enabledFlag) {
        return AbstractContent.findByEnabledFlag(enabledFlag);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardPatchSetFactory#findByEnabledFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<StandardReadBean> findByEnabledFlag(String enabledFlag) {
        List<PrimaryKey> primaryKeys = AbstractContent
                .findByEnabledFlag(enabledFlag);
        List<StandardReadBean> result = new ArrayList<StandardReadBean>();

        for (PrimaryKey pk : primaryKeys) {
            StandardContent si = (StandardContent) this.findByPrimaryKey(pk);
            result.add(si);
        }

        return result;
    }
}

// end AbstractStandardContentFactory
