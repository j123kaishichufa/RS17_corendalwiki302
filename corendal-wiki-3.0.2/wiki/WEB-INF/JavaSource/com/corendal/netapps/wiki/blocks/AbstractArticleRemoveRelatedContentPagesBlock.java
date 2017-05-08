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
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.managers.DefaultContentManager;

/**
 * AbstractArticleRemoveRelatedContentPagesBlock is the block printing the
 * references of a article.
 * 
 * @version $Id: AbstractArticleRemoveRelatedContentPagesBlock.java,v 1.1
 *          2005/09/06 21:25:28 tdanard Exp $
 */
public abstract class AbstractArticleRemoveRelatedContentPagesBlock extends
        AbstractStandardPagesBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractArticleRemoveRelatedContentPagesBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides
     * AbstractContentRemoveRelatedContentPagesBlock.clone.
     */
    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public boolean getIsVisible() {
        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        ContentManager cm = (ContentManager) pms
                .getManager(DefaultContentManager.class);
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * return
         */
        if ((sa != null) && (sa.getIsFound())) {
            return cm.getIsContentRemovalAuthorized(sa.getPrimaryKey());
        } else {
            return false;
        }
    }
}

// end AbstractArticleRemoveRelatedContentPagesBlock
