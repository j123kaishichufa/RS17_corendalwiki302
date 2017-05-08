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

import com.corendal.netapps.framework.core.interfaces.StandardWriteBeanFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: StandardContentFactory.java,v 1.1 2005/09/06 21:25:29 tdanard
 *          Exp $
 */
public interface StandardContentFactory extends StandardWriteBeanFactory {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardContentFactory";

    /**
     * Returns the number of direct standard contents for an article primary
     * key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return an int representing the number of contents value
     */
    public int findCountOfEnabledByParentArticlePrimaryKey(PrimaryKey pk);

    /**
     * Returns the list of direct standard contents for an article primary key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByParentArticlePrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard contents for an article primary key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContent> findEnabledByParentArticlePrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the recursive list of standard contents for an article primary
     * key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findRecursiveEnabledPrimaryKeysByParentArticlePrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard contents for a subscriber primary key.
     * 
     * @param pk
     *            primary key of the subscriber
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContent> findEnabledBySubscriberPrimaryKey(PrimaryKey pk);

    /**
     * Returns the list of standard contents for a subscriber primary key.
     * 
     * @param pk
     *            primary key of the subscriber
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContent> findEnabledBySubscriberAndModePrimaryKeys(
            PrimaryKey pk, PrimaryKey modePk);

    /**
     * Returns the list of standard content primary keys for a subscriber
     * primary key.
     * 
     * @param pk
     *            primary key of the subscriber
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findEnabledPrimaryKeysBySubscriberPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard content primary keys for a subscriber
     * primary key.
     * 
     * @param pk
     *            primary key of the subscriber
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findEnabledPrimaryKeysBySubscriberAndModePrimaryKeys(
            PrimaryKey pk, PrimaryKey modePk);

    /**
     * Returns the list of standard contents for a content request primary key
     * 
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContent> findEnabledByContentRequestPrimaryKey(
            PrimaryKey contentRequestPk);

    /**
     * Returns the list of direct standard contents for an article primary key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardContent> findDirectEnabledByParentArticlePrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the number of direct standard contents for an article primary
     * key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return an int representing the number of contents value
     */
    public int findCountOfDirectEnabledByParentArticlePrimaryKey(PrimaryKey pk);

    /**
     * Returns the list of direct standard contents for an article primary key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findDirectEnabledPrimaryKeysByParentArticlePrimaryKey(
            PrimaryKey pk);
}

// end StandardContentFactory
