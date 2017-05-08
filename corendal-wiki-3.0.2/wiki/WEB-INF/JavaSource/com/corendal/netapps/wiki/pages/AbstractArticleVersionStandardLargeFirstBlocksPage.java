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
import com.corendal.netapps.wiki.interfaces.ArticleVersionWriter;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersion;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersionFactory;
import com.corendal.netapps.wiki.utils.ArticleVersionUtil;
import com.corendal.netapps.wiki.writers.DefaultArticleVersionWriter;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleVersionFactory;

/**
 * AbstractArticleVersionStandardLargeFirstBlocksPage is the abstract class
 * handling information about each page made of blocks of the application. Large
 * blocks are printed first, small blocks are printed last.
 * 
 * @version $Id: AbstractArticleVersionStandardLargeFirstBlocksPage.java,v 1.1
 *          2005/09/06 21:25:33 tdanard Exp $
 */
public abstract class AbstractArticleVersionStandardLargeFirstBlocksPage extends
        AbstractStandardLargeFirstBlocksPage implements Cloneable,
        StandardLargeFirstBlocksPage {
    /** Primary key of the requested article version */
    private PrimaryKey requestedArticleVersionPk;

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractArticleVersionStandardLargeFirstBlocksPage() {
        this.requestedArticleVersionPk = ArticleVersionUtil
                .getRequestedArticleVersionPrimaryKey();
    }

    /**
     * Returns a clone. Overrides AbstractStandardLargeFirstBlocksPage.clone.
     */
    @Override
    public Object clone() {
        AbstractArticleVersionStandardLargeFirstBlocksPage result = (AbstractArticleVersionStandardLargeFirstBlocksPage) super
                .clone();

        if (this.requestedArticleVersionPk != null) {
            result.requestedArticleVersionPk = (PrimaryKey) this.requestedArticleVersionPk
                    .clone();
        }

        return result;
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
        StandardArticleVersionFactory sdf = (StandardArticleVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleVersionFactory.class);
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
        ArticleVersionWriter dw = (ArticleVersionWriter) pws
                .getWriter(DefaultArticleVersionWriter.class);

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
         * find the requested article version and print the path
         */
        if (extension.equals(extHTML)) {
            if (this.requestedArticleVersionPk != null) {
                /*
                 * find the requested article version
                 */
                StandardArticleVersion requestedArticleVersion = (StandardArticleVersion) sdf
                        .findByPrimaryKey(this.requestedArticleVersionPk);

                /*
                 * get the associated article
                 */
                if (requestedArticleVersion.getIsFound()) {
                    StandardArticle requestedArticle = requestedArticleVersion
                            .getStandardArticle();

                    /*
                     * print the path
                     */
                    if ((requestedArticle.getIsFound())
                            && (requestedArticle.getIsVisible())) {
                        dw.printPathHTML(requestedArticleVersion);
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

// end AbstractArticleVersionStandardLargeFirstBlocksPage
