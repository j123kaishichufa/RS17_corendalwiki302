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

import com.corendal.netapps.framework.core.constants.AccessTypeConstants;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;

/**
 * AbstractContentRequestViewBlock is the block printing the information of a
 * content request in view-only mode.
 * 
 * @version $Id: AbstractContentRequestViewBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractContentRequestViewBlock extends
        AbstractContentRequestEntryBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractContentRequestViewBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentRequestEntryBlock.clone.
     */
    @Override
    public Object clone() {
        return super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * initialize the content request block
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
         * set the content request to view
         */
        StandardContentRequest sp = getViewedContentRequest();

        /*
         * set the page to call when the form is submitted
         */
        this.setActionPage(page);

        /*
         * set the type of access
         */
        this.setAccessType(AccessTypeConstants.VIEW);

        /*
         * print the content request
         */
        if (sp != null) {
            /*
             * set the content request for default values
             */
            this.setStandardContentRequest(sp);
        } else {
            /*
             * indicate that no record could be found
             */
            this.setStandardContentRequest(null);
        }

        /*
         * build the layout
         */
        if (this.getIsBodyPrinted()) {
            this.buildViewRecord();
        }

        /*
         * close the definition of the form
         */
        this.wrapUp();
    }

    /**
     * Returns the standard content request to be viewed.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardContentRequest object
     */
    public StandardContentRequest getViewedContentRequest() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentRequestFactory sdocf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);

        /*
         * get the primary key of the requested contentRequest
         */
        PrimaryKey contentRequestPk = this.getRecordPrimaryKey();

        if (contentRequestPk != null) {
            StandardContentRequest contentRequest = (StandardContentRequest) sdocf
                    .findByPrimaryKey(contentRequestPk);

            if ((contentRequest.getIsFound())
                    && (contentRequest.getIsVisible())) {
                return contentRequest;
            } else {
                return null;
            }
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
        String contentRequestId = rm.getParameter(this
                .getRecordPrimaryDataParameter());

        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(contentRequestId);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordPrimaryDataParameter(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordPrimaryDataParameter() {
        return HTTPParameterConstants.CONTENT_REQUEST_ID;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordName(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordName() {
        StandardContentRequest sp = this.getViewedContentRequest();

        if (sp != null) {
            return sp.getNameText();
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardFormBlock#validateExtra(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void validateExtra() throws IOException {
        this.validateExtraEditRecord();
    }
}

// end AbstractContentRequestViewBlock
