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

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractImageSubscribersBlock is the class describing and printing the lkist
 * of image subscribers.
 * 
 * @version $Id: AbstractImageSubscribersBlock.java,v 1.1 2005/09/06 21:25:27
 *          tdanard Exp $
 */
public abstract class AbstractImageSubscribersBlock extends
        AbstractSubscribersBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractImageSubscribersBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractAccountSearchResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractImageSubscribersBlock) super.clone();
    }

    /**
     * Returns the standard image to be viewed.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.Searched object
     */
    @Override
    public Searched getViewedSearched() {
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
}

// end AbstractImageSubscribersBlock
