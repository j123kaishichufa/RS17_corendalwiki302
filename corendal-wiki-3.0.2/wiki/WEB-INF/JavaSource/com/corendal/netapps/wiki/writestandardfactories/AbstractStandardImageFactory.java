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

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.interfaces.StandardBean;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.standardfactories.AbstractStandardWriteBeanFactory;
import com.corendal.netapps.framework.core.throwables.CannotDoBeanException;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.Image;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.utils.ImageUtil;
import com.corendal.netapps.wiki.writedatabeans.AbstractImage;
import com.corendal.netapps.wiki.writedatafactories.ImageFactory;
import com.corendal.netapps.wiki.writestandardbeans.DefaultStandardImage;

/**
 * AbstractStandardImageFactory is the abstract class building new StandardImage
 * instances.
 * 
 * @version $Id: AbstractStandardImageFactory.java,v 1.1 2005/09/06 21:25:35
 *          tdanard Exp $
 */
public abstract class AbstractStandardImageFactory extends
        AbstractStandardWriteBeanFactory implements Cloneable,
        StandardImageFactory {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardImageFactory() {
        super(DefaultStandardImage.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardWriteBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardImageFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(DefaultStandardImage.class.getName())) {
            return new DefaultStandardImage();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }

    /**
     * Creates a new image.
     * 
     * @param contentInfoPk
     *            primary key of a content info
     * @param authorPk
     *            primary key of an author account
     * @param filePk
     *            primary key of a stored file
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
    public StandardBean create(PrimaryKey contentInfoPk, PrimaryKey authorPk,
            PrimaryKey filePk, PrimaryKey parentContentPk,
            PrimaryKey contentRequestPk, PrimaryKey classificationTypePk,
            PrimaryKey ruleTypePk, String comment) {
        /*
         * initialize the result and get the data session set
         */
        StandardImage result = null;
        /*
         * create the image
         */
        Image image = ImageFactory.create(contentInfoPk, authorPk, filePk,
                parentContentPk, contentRequestPk, classificationTypePk,
                ruleTypePk, comment);

        if ((image != null) && (image.getIsDone())) {
            result = (StandardImage) this.findByPrimaryKey(image
                    .getPrimaryKey());
            result.setIsDone(image.getIsDone());
            result.setStoreTrace(image.getStoreTrace());
        } else if (image != null) {
            throw new CannotDoBeanException(image.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardImageFactory#findPartialEnabledByAuthorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      int, int,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardImage> findPartialEnabledByAuthorPrimaryKey(
            PrimaryKey pk, int indexStart, int indexEnd) {
        List<StandardImage> standardImages = new ArrayList<StandardImage>();
        List<PrimaryKey> primaryKeys = AbstractImage
                .findEnabledByAuthorPrimaryKey(pk);

        for (int i = 0; i < indexStart; i++) {
            standardImages.add(null);
        }

        for (int i = indexStart; i < (indexEnd + 1); i++) {
            PrimaryKey sdPk = primaryKeys.get(i);
            StandardImage sd = (StandardImage) this.findByPrimaryKey(sdPk);
            standardImages.add(sd);
        }

        for (int i = indexEnd + 1; i < primaryKeys.size(); i++) {
            standardImages.add(null);
        }

        return standardImages;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardImageFactory#findCountOfEnabledByAuthorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public int findCountOfEnabledByAuthorPrimaryKey(PrimaryKey pk) {
        List<PrimaryKey> primaryKeys = AbstractImage
                .findEnabledByAuthorPrimaryKey(pk);

        /*
         * return
         */
        return primaryKeys.size();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardImageFactory#findEnabledPrimaryKeysByAuthorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByAuthorPrimaryKey(
            PrimaryKey pk) {
        return AbstractImage.findEnabledByAuthorPrimaryKey(pk);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardImageFactory#findEnabledByAuthorPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardImage> findEnabledByAuthorPrimaryKey(PrimaryKey pk) {
        List<StandardImage> standardImages = new ArrayList<StandardImage>();
        List<PrimaryKey> primaryKeys = AbstractImage
                .findEnabledByAuthorPrimaryKey(pk);

        for (PrimaryKey sdPk : primaryKeys) {
            StandardImage sd = (StandardImage) this.findByPrimaryKey(sdPk);
            standardImages.add(sd);
        }

        return standardImages;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardImageFactory#findEnabledByRank(int,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardImage> findEnabledByRank(int cnt) {
        List<StandardImage> standardImages = new ArrayList<StandardImage>();
        List<PrimaryKey> primaryKeys = AbstractImage.findEnabledByRank(cnt);

        /*
         * add data beans to list
         */
        for (PrimaryKey sdPk : primaryKeys) {
            StandardImage sd = (StandardImage) this.findByPrimaryKey(sdPk);
            standardImages.add(sd);
        }

        return standardImages;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardImageFactory#findEnabledPrimaryKeysByCreationOrder(int,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<PrimaryKey> findEnabledPrimaryKeysByCreationOrder(int cnt) {
        return AbstractImage.findEnabledByCreationOrder(cnt);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardImageFactory#findEnabledByCreationOrder(int,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardImage> findEnabledByCreationOrder(int cnt) {
        List<StandardImage> standardImages = new ArrayList<StandardImage>();
        List<PrimaryKey> primaryKeys = AbstractImage
                .findEnabledByCreationOrder(cnt);

        /*
         * add data beans to list
         */
        for (PrimaryKey sdPk : primaryKeys) {
            StandardImage sd = (StandardImage) this.findByPrimaryKey(sdPk);
            standardImages.add(sd);
        }

        return standardImages;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardImageFactory#findByFriendlyAddress(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardImage findByFriendlyAddress(String friendlyAddress) {
        StandardImage sd = (StandardImage) this
                .getStandardReadBeanNewInstance();
        sd.setFriendlyAddressAndLoad(friendlyAddress);

        return sd;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardImageFactory#findByAbsoluteOrRelativeLocation(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardImage findByAbsoluteOrRelativeLocation(String location) {
        /*
         * extract the friendly address
         */
        String friendlyAddress = ImageUtil
                .getFriendlyAddressFromLocation(location);

        /*
         * find a match
         */
        if (!(StringUtil.getIsEmpty(friendlyAddress))) {
            StandardImage image = this.findByFriendlyAddress(friendlyAddress);

            return image;
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#findPrimaryKeysByEnabledFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<PrimaryKey> findPrimaryKeysByEnabledFlag(String enabledFlag) {
        return AbstractImage.findByEnabledFlag(enabledFlag);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardPatchSetFactory#findByEnabledFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<StandardReadBean> findByEnabledFlag(String enabledFlag) {
        List<PrimaryKey> primaryKeys = AbstractImage
                .findByEnabledFlag(enabledFlag);
        List<StandardReadBean> result = new ArrayList<StandardReadBean>();

        for (PrimaryKey pk : primaryKeys) {
            StandardImage si = (StandardImage) this.findByPrimaryKey(pk);
            result.add(si);
        }

        return result;
    }
}

// end AbstractStandardImageFactory
