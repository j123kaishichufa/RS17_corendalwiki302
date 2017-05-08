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
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.ContentHistoryManager;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.managers.DefaultContentHistoryManager;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;

/**
 * AbstractMyRecentContentsBlock is the class describing and printing all
 * contents of an article.
 * 
 * @version $Id: AbstractMyRecentContentsBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractMyRecentContentsBlock extends
        AbstractContentsSimpleResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractMyRecentContentsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentsSimpleResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractMyRecentContentsBlock) super.clone();
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
        this.setIsNavigationPrinted(false);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardResultsBlock#getIsSearchFormAccepted()
     */
    @Override
    public boolean getIsSearchFormAccepted() {
        return false;
    }

    /**
     * Returns the list of contents to print. Overrides
     * AbstractContentsSimpleResultsBlock.getContentsFound.
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
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentHistoryManager hm = (ContentHistoryManager) pms
                .getManager(DefaultContentHistoryManager.class);

        /*
         * get the list of contents in the history
         */
        List<PrimaryKey> contentPrimaryKeys = hm.getHistory();

        /*
         * set the maximum
         */
        int max = contentPrimaryKeys.size();

        if (max > 5) {
            max = 5;
        }

        /*
         * add all contents
         */
        List<StandardContent> contentsFound = new ArrayList<StandardContent>();

        for (int i = 0; i < max; i++) {
            /*
             * get the content
             */
            PrimaryKey currentContentPk = (PrimaryKey) contentPrimaryKeys
                    .get(i);
            StandardContent currentContent = (StandardContent) srf
                    .findByPrimaryKey(currentContentPk);

            /*
             * add the content
             */
            if (currentContent.getIsFound()) {
                contentsFound.add(currentContent);
            }
        }

        /*
         * return
         */
        return contentsFound;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getIsVisible(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public boolean getIsVisible() {
        if (this.getRowsCount() == 0) {
            return false;
        } else {
            return true;
        }
    }
}

// end AbstractMyRecentContentsBlock
