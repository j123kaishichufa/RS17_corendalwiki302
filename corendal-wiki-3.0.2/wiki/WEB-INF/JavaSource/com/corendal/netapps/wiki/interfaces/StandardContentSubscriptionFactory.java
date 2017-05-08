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
package com.corendal.netapps.wiki.interfaces;

import java.util.List;

import com.corendal.netapps.framework.core.interfaces.StandardBean;
import com.corendal.netapps.framework.core.interfaces.StandardWriteBeanFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: StandardContentSubscriptionFactory.java,v 1.1 2005/09/06
 *          21:25:29 tdanard Exp $
 */
public interface StandardContentSubscriptionFactory extends
        StandardWriteBeanFactory {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardContentSubscriptionFactory";

    /**
     * Creates a new content subscription.
     * 
     * @param contentPk
     *            primary key of the content
     * 
     * 
     * @return a StandardBean object
     */
    public StandardBean create(PrimaryKey contentPk, PrimaryKey typePk,
            PrimaryKey modePk);

    /**
     * Returns the list of standard content subscriptions for a content primary
     * key and a subscription type primary key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentSubscription> findEnabledByContentAndTypePrimaryKeys(
            PrimaryKey pk, PrimaryKey typePk);

    /**
     * Returns the list of individual standard content subscriptions for a
     * content primary key and a subscriber primary key.
     * 
     * @param contentPk
     *            primary key of the content
     * @param subscriberPk
     *            primary key of the subscriber account
     * 
     * 
     * @return a StandardBean object
     */
    public List<StandardContentSubscription> findEnabledByContentAndIndividualSubscriberPrimaryKeys(
            PrimaryKey contentPk, PrimaryKey subscriberPk);

    /**
     * Returns the list of group standard content subscriptions for a content
     * primary key and a subscriber primary key.
     * 
     * @param contentPk
     *            primary key of the content
     * @param subscriberPk
     *            primary key of the subscriber group
     * 
     * 
     * @return a StandardBean object
     */
    public List<StandardContentSubscription> findEnabledByContentAndGroupSubscriberPrimaryKeys(
            PrimaryKey contentPk, PrimaryKey subscriberPk);

    /**
     * Returns the list of standard content subscriptions for an individual
     * primary key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentSubscription> findEnabledByIndividualPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard content subscriptions for an group primary
     * key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentSubscription> findEnabledByGroupPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard content subscriptions for an individual and
     * a mode primary keys.
     * 
     * @param pk
     *            primary key of the account
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentSubscription> findEnabledByIndividualAndModePrimaryKeys(
            PrimaryKey pk, PrimaryKey modePk);

    /**
     * Returns the list of standard content subscriptions for a group and a mode
     * primary keys
     * 
     * @param pk
     *            primary key of the group
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentSubscription> findEnabledByGroupAndModePrimaryKeys(
            PrimaryKey pk, PrimaryKey modePk);

    /**
     * Returns the list of standard content subscriptions for a content primary
     * key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentSubscription> findEnabledByContentPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard content subscriptions for a mode primary
     * key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentSubscription> findEnabledByModePrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard content subscriptions for a type primary
     * key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContentSubscription> findEnabledByTypePrimaryKey(
            PrimaryKey pk);
}

// end StandardContentSubscriptionFactory
