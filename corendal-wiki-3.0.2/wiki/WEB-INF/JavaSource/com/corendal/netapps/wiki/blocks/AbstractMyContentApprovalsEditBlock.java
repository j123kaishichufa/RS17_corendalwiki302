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

import java.io.IOException;

import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;

/**
 * AbstractMyContentApprovalsEditBlock is the abstract block creating a new
 * Content through a form.
 * 
 * @version $Id: AbstractMyContentApprovalsEditBlock.java,v 1.1 2005/09/06
 *          21:25:28 tdanard Exp $
 */
public abstract class AbstractMyContentApprovalsEditBlock extends
        AbstractMyContentApprovalsEntryBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractMyContentApprovalsEditBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractMyContentApprovalsEntryBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractMyContentApprovalsEditBlock) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * initialize the account block
         */
        super.initStandardBlock();

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get the standard page
         */
        StandardPage page = rm.getStandardPage();

        /*
         * set the page to call when the form is submitted
         */
        this.setActionPage(page);

        /*
         * build the layout
         */
        if (this.getIsBodyPrinted()) {
            this.buildApproveEditRecord();
        }

        /*
         * close the definition
         */
        this.wrapUp();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardFormBlock#validateExtra(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void validateExtra() throws IOException {
        this.validateExtraApprovalRequest();
    }
}

// end AbstractMyContentApprovalsEditBlock
