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
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.interfaces.StandardImagePagesOfBlock;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractStandardImagePagesOfBlock is the abstract class handling information
 * about each block made of page articles in the application, for a particular
 * image.
 * 
 * @version $Id: AbstractStandardImagePagesOfBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractStandardImagePagesOfBlock extends
        AbstractStandardPagesBlock implements Cloneable,
        StandardImagePagesOfBlock {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractStandardImagePagesOfBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardImagePagesOfBlock) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * initialize the pages block
         */
        super.initStandardBlock();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * get the viewed image
         */
        StandardImage image = this.getViewedImage();

        /*
         * verify that the image was found
         */
        if ((image != null) && (image.getIsFound())) {
            /*
             * get the something for image message
             */
            PrimaryKey somethingforImagePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.DSP_SOMETHING_FOR_IMAGE);
            StandardMessage somethingforImage = (StandardMessage) smf
                    .findByPrimaryKey(somethingforImagePk);

            /*
             * add the current block name and the image name to the message
             */
            somethingforImage.replaceMessageText(this.getCurrentNameText(), 1);
            somethingforImage.replaceMessageEncoded(this
                    .getCurrentNameEncoded(), 1);
            somethingforImage.replaceMessageHTML(this.getCurrentNameHTML(), 1);
            somethingforImage.replaceMessageText(image.getNameText(), 2);
            somethingforImage.replaceMessageEncoded(image.getNameEncoded(), 2);
            somethingforImage.replaceMessageHTML(image.getNameEncoded(), 2);

            /*
             * modify the name of this block
             */
            this.setCurrentNameText(somethingforImage.getCurrentMessageText());
            this.setCurrentNameEncoded(somethingforImage
                    .getCurrentMessageEncoded());
            this.setCurrentNameHTML(somethingforImage.getCurrentMessageHTML());
        }
    }

    /**
     * Returns the standard image to be viewed. Implements abstract method
     * AbstractImageViewBlock.getViewedImage.
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
        StandardImageFactory saf = (StandardImageFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentHistoryManager hm = (ContentHistoryManager) pms
                .getManager(DefaultContentHistoryManager.class);

        /*
         * add this image to the history
         */
        PrimaryKey pk = this.getRecordPrimaryKey();

        /*
         * return
         */
        StandardImage sa = null;

        if (pk != null) {
            hm.add(pk);
            sa = (StandardImage) saf.findByPrimaryKey(pk);
        }

        return sa;
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
        StandardImage sa = this.getViewedImage();

        if ((sa != null) && sa.getIsFound()) {
            return sa.getNameText();
        } else {
            return null;
        }
    }
}

// end AbstractStandardImagePagesOfBlock
