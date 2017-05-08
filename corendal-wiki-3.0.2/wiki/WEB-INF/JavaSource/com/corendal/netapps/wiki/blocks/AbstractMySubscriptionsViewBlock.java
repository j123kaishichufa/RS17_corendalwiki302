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
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;

/**
 * AbstractMySubscriptionsViewBlock is the class describing and printing all
 * subscriptions for the current user.
 * 
 * @version $Id: AbstractMySubscriptionsViewBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractMySubscriptionsViewBlock extends
        AbstractSubscriptionsViewBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractMySubscriptionsViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentsComplexResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractMySubscriptionsViewBlock) super.clone();
    }

    /**
     * Returns the standard account to be viewed. Implements abstract method
     * AbstractSubscriptionsViewBlock.getViewedAccount.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.core.interfaces.StandardAccount
     *         object
     */
    @Override
    public StandardAccount getViewedAccount() {
        /*
         * get the access manager
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);

        /*
         * find the current the account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * return
         */
        return sa;
    }
}

// end AbstractMySubscriptionsViewBlock
