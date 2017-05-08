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
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;

/**
 * AbstractMyContentApprovalsViewBlock is the class describing and printing all
 * content requests of a approver.
 * 
 * @version $Id: AbstractMyContentApprovalsViewBlock.java,v 1.1 2005/09/06
 *          21:25:27 tdanard Exp $
 */
public abstract class AbstractMyContentApprovalsViewBlock extends
        AbstractContentRequestsResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractMyContentApprovalsViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentRequestsResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractMyContentApprovalsViewBlock) super.clone();
    }

    /**
     * Returns the list of content requests to print. Overrides
     * AbstractContentRequestsResultsBlock.getContentRequestsFound.
     * 
     * 
     * 
     * @return a java.util.List object
     */
    @Override
    protected List getContentRequestsFound() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentRequestFactory srf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);

        /*
         * find the approver being viewed
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        StandardAccount sa = am.getProxyStandardAccount();
        PrimaryKey approverPk = sa.getPrimaryKey();

        /*
         * build the list of content requests
         */
        List<StandardContentRequest> contentRequestsFound;

        if (approverPk != null) {
            contentRequestsFound = srf
                    .findEnabledByApproverPrimaryKey(approverPk);
        } else {
            contentRequestsFound = new ArrayList<StandardContentRequest>();
        }

        /*
         * return
         */
        return contentRequestsFound;
    }
}

// end AbstractMyContentApprovalsViewBlock
