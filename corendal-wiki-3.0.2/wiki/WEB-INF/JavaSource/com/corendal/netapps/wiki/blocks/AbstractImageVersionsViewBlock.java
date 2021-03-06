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
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.interfaces.StandardImageVersion;
import com.corendal.netapps.wiki.interfaces.StandardImageVersionFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageVersionFactory;

/**
 * AbstractImageVersionsViewBlock is the class describing and printing all image
 * versions used in the application.
 * 
 * @version $Id: AbstractImageVersionsViewBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractImageVersionsViewBlock extends
        AbstractImageVersionResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead
     */
    protected AbstractImageVersionsViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractImageVersionResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractImageVersionsViewBlock) super.clone();
    }

    /**
     * Returns the list of image versions to print. Overrides
     * AbstractImageVersionResultsBlock.getImageVersionsFound.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    @Override
    protected List getImageVersionsFound() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardImageVersionFactory sdvf = (StandardImageVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageVersionFactory.class);

        /*
         * build the list of image versions
         */
        List<StandardImageVersion> imageVersionsFound;
        StandardImage image = this.getViewedImage();

        if ((image != null) && (image.getIsFound())) {
            imageVersionsFound = sdvf.findEnabledByImagePrimaryKey(image
                    .getPrimaryKey());
        } else {
            imageVersionsFound = new ArrayList<StandardImageVersion>();
        }

        /*
         * return
         */
        return imageVersionsFound;
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
         * get the primary key of the requested image
         */
        PrimaryKey imagePk = this.getRecordPrimaryKey();

        if (imagePk != null) {
            StandardImage image = (StandardImage) sdocf
                    .findByPrimaryKey(imagePk);

            if ((image.getIsFound()) && (image.getIsVisible())) {
                return image;
            } else {
                return null;
            }
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

        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(imageId);
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

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#getSortColumn(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public int getSortColumn() {
        return -1; // use the first column as sort criteria, already done by
        // the

        // database
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#getSortOrder(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public int getSortOrder() {
        return 2; // reverse the order used by the database
    }
}

// end AbstractImageVersionsViewBlock
