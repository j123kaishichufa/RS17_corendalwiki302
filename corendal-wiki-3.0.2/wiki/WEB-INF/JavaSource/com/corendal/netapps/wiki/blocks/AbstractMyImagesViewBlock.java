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
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractMyImagesViewBlock is the class describing and printing all images of
 * an editor.
 * 
 * @version $Id: AbstractMyImagesViewBlock.java,v 1.1 2005/09/06 21:25:27
 *          tdanard Exp $
 */
public abstract class AbstractMyImagesViewBlock extends
        AbstractContentsComplexResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractMyImagesViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentsComplexResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractMyImagesViewBlock) super.clone();
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

        /*
         * set the entity
         */
        PrimaryKey imagesPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGES);
        StandardEntity entity = (StandardEntity) sef.findByPrimaryKey(imagesPk);
        this.setStandardEntity(entity);
    }

    /**
     * Returns the list of image to print. Overrides
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
        StandardImageFactory sdf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);

        /*
         * find the author being viewed
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        StandardAccount sa = am.getProxyStandardAccount();
        PrimaryKey authorPk = sa.getPrimaryKey();

        /*
         * build the list of images
         */
        List<StandardImage> imagesFound;

        if (authorPk != null) {
            imagesFound = sdf.findEnabledByAuthorPrimaryKey(authorPk);
        } else {
            imagesFound = new ArrayList<StandardImage>();
        }

        /*
         * return
         */
        return imagesFound;
    }

    /**
     * Returns the total number of images found. Overrides
     * AbstractContentsComplexResultsBlock.getCountOfContentsFound.
     * 
     * 
     * 
     * @return an int
     */
    @Override
    public int getCountOfFilteredContentsFound() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardImageFactory sdf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);

        /*
         * find the author being viewed
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        StandardAccount sa = am.getProxyStandardAccount();
        PrimaryKey authorPk = sa.getPrimaryKey();

        /*
         * return
         */
        if (authorPk != null) {
            return sdf.findCountOfEnabledByAuthorPrimaryKey(authorPk);
        } else {
            return 0;
        }
    }
}

// end AbstractMyImagesViewBlock
