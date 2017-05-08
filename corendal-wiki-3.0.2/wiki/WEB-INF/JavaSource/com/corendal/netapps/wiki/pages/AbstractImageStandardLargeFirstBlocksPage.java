/*   
 * Corendal NetApps DocSide - Web application for image management   
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
package com.corendal.netapps.wiki.pages;

import com.corendal.netapps.framework.core.dictionaries.ExtensionsDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardExtension;
import com.corendal.netapps.framework.core.interfaces.StandardExtensionFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLargeFirstBlocksPage;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.pages.AbstractStandardLargeFirstBlocksPage;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardExtensionFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.interfaces.ContentWriter;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.writers.DefaultContentWriter;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractImageStandardLargeFirstBlocksPage is the abstract class handling
 * information about each image made of blocks of the application. Large blocks
 * are printed first, small blocks are printed last.
 * 
 * @version $Id: AbstractImageStandardLargeFirstBlocksPage.java,v 1.1 2005/09/06
 *          19:48:08 tdanard Exp $
 */
public abstract class AbstractImageStandardLargeFirstBlocksPage extends
        AbstractStandardLargeFirstBlocksPage implements Cloneable,
        StandardLargeFirstBlocksPage {

    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractImageStandardLargeFirstBlocksPage() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardLargeFirstBlocksPage.clone.
     */
    @Override
    public Object clone() {
        return (AbstractImageStandardLargeFirstBlocksPage) super.clone();
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

    /**
     * Returns the primary key of the record of this image download page.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
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

    /**
     * Returns the primary data parameter of the record of this image download
     * page.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public String getRecordPrimaryDataParameter() {
        return HTTPParameterConstants.IMAGE_ID;
    }

    /**
     * Returns the name of the record of this image download page.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    public String getRecordName() {
        StandardImage sp = this.getViewedImage();

        if (sp != null) {
            return sp.getNameText();
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlocksPage#printLargeBlockSets(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void printLargeBlockSets() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardExtensionFactory sef = (StandardExtensionFactory) pfs
                .getStandardBeanFactory(DefaultStandardExtensionFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        ContentWriter dw = (ContentWriter) pws
                .getWriter(DefaultContentWriter.class);

        /*
         * get the HTML extension
         */
        PrimaryKey extHTMLPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ExtensionsDictionary.HTML);
        StandardExtension extHTML = (StandardExtension) sef
                .findByPrimaryKey(extHTMLPk);

        /*
         * get the extension requested by the user
         */
        StandardExtension extension = rm.getStandardExtension();

        /*
         * find the requested image and print the path
         */
        if (extension.equals(extHTML)) {
            StandardImage image = this.getViewedImage();
            if (image != null) {
                /*
                 * get the parent article
                 */
                PrimaryKey parentArticlePk = image.getMainParentPrimaryKey();

                if (parentArticlePk != null) {
                    /*
                     * find the requested article
                     */
                    StandardArticle requestedArticle = (StandardArticle) sdf
                            .findByPrimaryKey(parentArticlePk);

                    /*
                     * print the path
                     */
                    if ((requestedArticle.getIsFound())
                            && (requestedArticle.getIsVisible())) {

                        /*
                         * print the path
                         */
                        dw.printPathHTML(requestedArticle);
                    }
                }

            }
        }

        /*
         * print the rest of the block
         */
        super.printLargeBlockSets();
    }
}

// end AbstractImageStandardLargeFirstBlocksPage
