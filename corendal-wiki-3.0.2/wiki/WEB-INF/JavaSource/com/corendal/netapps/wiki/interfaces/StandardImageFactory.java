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
 * @version $Id: StandardImageFactory.java,v 1.9 2007/09/03 19:42:01 tdanard Exp $
 */
public interface StandardImageFactory extends StandardWriteBeanFactory {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardImageFactory";

    /**
     * Returns the image based upon an author primary key.
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
     * Returns a list of images based upon an author primary key.
     * 
     * @param pk
     *            primary key of an author account
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardImage> findEnabledByAuthorPrimaryKey(PrimaryKey pk);

    /**
     * Returns the image based upon an author primary.
     * 
     * @param pk
     *            primary key of an author account
     * @param indexStart
     *            an int specifying the index of the first image to load
     * @param indexEnd
     *            an int specifying the index of the last image to load
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardImage> findPartialEnabledByAuthorPrimaryKey(
            PrimaryKey pk, int indexStart, int indexEnd);

    /**
     * Returns the number of images based upon an author primary.
     * 
     * @param pk
     *            primary key of an author account
     * 
     * 
     * @return a java.util.List object
     */
    public int findCountOfEnabledByAuthorPrimaryKey(PrimaryKey pk);

    /**
     * Creates a new image.
     * 
     * 
     * 
     */
    public StandardBean create(PrimaryKey contentInfoPk, PrimaryKey editorPk,
            PrimaryKey filePk, PrimaryKey parentContentPk,
            PrimaryKey contentRequestPk, PrimaryKey classificationTypePk,
            PrimaryKey ruleTypePk, String comment);

    /**
     * Returns a list of images based upon their rank.
     * 
     * @param cnt
     *            number of items to return
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardImage> findEnabledByRank(int cnt);

    /**
     * Returns a list of image primary keys based upon their creation order.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByCreationOrder(int cnt);

    /**
     * Returns a list of images based upon their creation order.
     * 
     * @param cnt
     *            number of items to return
     * 
     * 
     * @return a java.util.List object
     */
    public List<StandardImage> findEnabledByCreationOrder(int cnt);

    /**
     * Returns the image based upon a friendly address.
     * 
     * @param friendlyAddress
     *            a String representing a friendly address
     * 
     * 
     * @return a StandardImage object
     */
    public StandardImage findByFriendlyAddress(String friendlyAddress);

    /**
     * Returns an image matching am absolute or relative location.
     * 
     * @param location
     *            location to query
     * 
     * 
     * @return a java.util.List object
     */
    public StandardImage findByAbsoluteOrRelativeLocation(String location);
}

// end StandardImageFactory
