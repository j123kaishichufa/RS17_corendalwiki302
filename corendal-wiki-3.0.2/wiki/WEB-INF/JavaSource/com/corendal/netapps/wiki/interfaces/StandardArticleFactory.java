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
 * @version $Id: StandardArticleFactory.java,v 1.1 2005/09/06 21:25:29 tdanard
 *          Exp $
 */
public interface StandardArticleFactory extends StandardWriteBeanFactory {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardArticleFactory";

    /**
     * Returns the article based upon a whats new flag.
     * 
     * @param whatsNewFlag
     *            whats new flag to query
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByWhatsNewFlag(
            String whatsNewFlag);

    /**
     * Returns a list of articles based upon a whats new flag.
     * 
     * @param whatsNewFlag
     *            whats new flag to query
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardArticle> findEnabledByWhatsNewFlag(String whatsNewFlag);

    /**
     * Returns the article based upon an author primary key.
     * 
     * @param pk
     *            primary key of an author account
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByAuthorPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns a list of articles based upon an author primary key.
     * 
     * @param pk
     *            primary key of an author account
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardArticle> findEnabledByAuthorPrimaryKey(PrimaryKey pk);

    /**
     * Returns the article based upon an author primary.
     * 
     * @param pk
     *            primary key of an author account
     * @param indexStart
     *            an int specifying the index of the first article to load
     * @param indexEnd
     *            an int specifying the index of the last article to load
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardArticle> findPartialEnabledByAuthorPrimaryKey(
            PrimaryKey pk, int indexStart, int indexEnd);

    /**
     * Returns the number of articles based upon an author primary.
     * 
     * @param pk
     *            primary key of an author account
     * 
     * 
     * @return a java.util.List object
     */
    public int findCountOfEnabledByAuthorPrimaryKey(PrimaryKey pk);

    /**
     * Returns the article based upon a friendly address.
     * 
     * @param friendlyAddress
     *            a String representing a friendly address
     * 
     * 
     * @return a StandardArticle object
     */
    public StandardArticle findByFriendlyAddress(String friendlyAddress);

    /**
     * Creates a new external article.
     * 
     * @param contentInfoPk
     *            primary key of a content info
     * @param authorPk
     *            primary key of an author account
     * @param body
     *            a String representing a body
     * @param parentContentPk
     *            primary key of a parent article
     * @param contentRequestPk
     *            primary key of a content request
     * @param classificationTypePk
     *            primary key of a classification type
     * @param ruleTypePk
     *            primary key of a rule type
     * @param comment
     *            a comment
     * 
     * 
     * @return a StandardBean object
     */
    public StandardBean createArticle(PrimaryKey contentInfoPk,
            PrimaryKey editorPk, PrimaryKey associateEditorsPk,
            PrimaryKey authorPk, PrimaryKey parentContentPk,
            PrimaryKey contentRequestPk, PrimaryKey classificationTypePk,
            PrimaryKey ruleTypePk, String comment);

    /**
     * Returns a list of articles based upon their rank.
     * 
     * @param cnt
     *            number of items to return
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardArticle> findEnabledByRank(int cnt);

    /**
     * Returns a list of article primary keys based upon their creation order.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByCreationOrder(int cnt);

    /**
     * Returns a list of articles based upon their creation order.
     * 
     * @param cnt
     *            number of items to return
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardArticle> findEnabledByCreationOrder(int cnt);

    /**
     * Returns the articles based upon a referenced content primary key.
     * 
     * @param pk
     *            primary key of a reference resource
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardArticle> findEnabledByReferencedContentPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the article based upon an editor primary.
     * 
     * @param pk
     *            primary key of an editor account
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardArticle> findEnabledByEditorPrimaryKey(PrimaryKey pk);

    /**
     * Returns the primary keys of the article based upon an editor primary.
     * 
     * @param pk
     *            primary key of an editor account
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByEditorPrimaryKey(
            PrimaryKey pk);

    /**
     * Returns an article matching am absolute or relative location.
     * 
     * @param location
     *            location to query
     * 
     * 
     * @return a java.util.List object
     */
    public StandardArticle findByAbsoluteOrRelativeLocation(String location);

    /**
     * Returns the list of standard articles for an article primary key.
     * 
     * @param pk
     *            primary key of the article
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardArticle> findDirectEnabledByParentArticlePrimaryKey(
            PrimaryKey pk);

    /**
     * Returns the list of standard article primary keys for an article primary
     * key.
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

// end StandardArticleFactory
