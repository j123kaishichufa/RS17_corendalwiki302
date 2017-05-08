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

import com.corendal.netapps.framework.core.blocks.AbstractStandardPagesBlock;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ContentClassificationTypesDictionary;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.interfaces.StandardImagePagesBlock;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractStandardImagePagesBlock is the abstract class handling information
 * about each image made of blocks of the application. Large blocks are printed
 * first, small blocks are printed last.
 * 
 * @version $Id: AbstractStandardImagePagesBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractStandardImagePagesBlock extends
        AbstractStandardPagesBlock implements Cloneable,
        StandardImagePagesBlock {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractStandardImagePagesBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardPagesBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardImagePagesBlock) super.clone();
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
         * verify the visibility of this block
         */
        StandardImage image = this.getViewedImage();

        if (image != null) {
            /*
             * get the primary key of the "no login required" classification
             */
            PrimaryKey noLoginRequiredPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentClassificationTypesDictionary.NO_LOGIN_REQUIRED);
            PrimaryKey loginRequiredPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentClassificationTypesDictionary.LOGIN_REQUIRED);
            PrimaryKey sameAsParentPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentClassificationTypesDictionary.SAME_AS_PARENT);

            /*
             * verify the classification type
             */
            PrimaryKey classificationTypePk = image
                    .getClassificationTypePrimaryKey();
            this
                    .setIsVisible(!(classificationTypePk
                            .equals(noLoginRequiredPk)
                            || classificationTypePk.equals(loginRequiredPk) || classificationTypePk
                            .equals(sameAsParentPk)));
        } else {
            this.setIsVisible(false);
        }
    }

    /**
     * Returns the standard image to be viewed.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardImage object
     */
    public StandardImage getViewedImage() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardImageFactory sdocf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);

        /*
         * get the image
         */
        StandardImage image = (StandardImage) sdocf.findByPrimaryKey(this
                .getRecordPrimaryKey());

        /*
         * return
         */
        if ((image.getIsFound()) && (image.getIsVisible())) {
            return image;
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
        String imageId = rm.getParameter(this.getRecordPrimaryDataParameter());

        return PrimaryKeyUtil.getAlphanumericSingleKey(imageId);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordPrimaryDataParameter(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordPrimaryDataParameter() {
        return HTTPParameterConstants.IMAGE_ID;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordName(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordName() {
        StandardImage sp = this.getViewedImage();

        if (sp != null) {
            return sp.getNameText();
        } else {
            return null;
        }
    }
}

// end AbstractStandardImagePagesBlock
