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

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardLargeFirstBlocksPage;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.pages.AbstractStandardLargeFirstBlocksPage;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;

/**
 * AbstractHomeStandardLargeFirstBlocksPage is the abstract class handling
 * information about each page made of blocks of the application. Large blocks
 * are printed first, small blocks are printed last.
 * 
 * @version $Id: AbstractHomeStandardLargeFirstBlocksPage.java,v 1.1 2005/09/06
 *          21:25:33 tdanard Exp $
 */
public abstract class AbstractHomeStandardLargeFirstBlocksPage extends
        AbstractStandardLargeFirstBlocksPage implements Cloneable,
        StandardLargeFirstBlocksPage {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractHomeStandardLargeFirstBlocksPage() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardLargeFirstBlocksPage.clone.
     */
    @Override
    public Object clone() {
        return (AbstractHomeStandardLargeFirstBlocksPage) super.clone();
    }

    /*
     * current tier context
     */
    @Override
    public void initStandardBean() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * add the home article as virtual parameter
         */
        rm.addVirtualParameter(HTTPParameterConstants.ARTICLE_ID,
                ArticlesDictionary.HOME);

        /*
         * call the super method
         */
        super.initStandardBean();
    }
}

// end AbstractHomeStandardLargeFirstBlocksPage
