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
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;

/**
 * AbstractArticleSummaryBlock is the block printing an article summary in
 * view-only mode.
 * 
 * @version $Id: AbstractArticleSummaryBlock.java,v 1.1 2005/09/06 21:25:27
 *          tdanard Exp $
 */
public abstract class AbstractArticleSummaryBlock extends
        AbstractArticleEntryBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractArticleSummaryBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractArticleEntryBlock.clone.
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
         * initialize the article block
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
         * set the article to view
         */
        StandardArticle sp = getViewedArticle();

        /*
         * set the page to call when the form is submitted
         */
        this.setActionPage(page);

        /*
         * set the type of access
         */
        this.setAccessType(AccessTypeConstants.VIEW);

        /*
         * print the article
         */
        if (sp != null) {
            /*
             * set the article for default values
             */
            this.setStandardArticle(sp);
        } else {
            /*
             * indicate that no record could be found
             */
            this.setStandardArticle(null);
        }

        /*
         * build the layout
         */
        if (this.getIsBodyPrinted()) {
            this.buildSummaryRecord();
        }

        /*
         * close the definition of the form
         */
        this.wrapUp();
    }

    /**
     * Returns the standard article to be viewed.
     * 
     * 
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardArticle object
     */
    public StandardArticle getViewedArticle() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdocf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);

        /*
         * get the primary key of the requested article
         */
        PrimaryKey articlePk = this.getRecordPrimaryKey();

        if (articlePk != null) {
            StandardArticle article = (StandardArticle) sdocf
                    .findByPrimaryKey(articlePk);

            if ((article.getIsFound()) && (article.getIsVisible())) {
                return article;
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
        String articleId = rm
                .getParameter(this.getRecordPrimaryDataParameter());

        return PrimaryKeyUtil.getAlphanumericSingleKeyOrNull(articleId);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordPrimaryDataParameter(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordPrimaryDataParameter() {
        return HTTPParameterConstants.ARTICLE_ID;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordName(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public String getRecordName() {
        StandardArticle sp = this.getViewedArticle();

        if (sp != null) {
            return sp.getNameText();
        } else {
            return null;
        }
    }

    /**
     * Prints the validation specific to this form. Implements abstract method
     * AbstractStandardFormBlock.validateExtra.
     * 
     * 
     * 
     * @throws IOException
     *             when a redirect occurs
     */
    @Override
    public void validateExtra() throws IOException {
        // no validation required
    }
}

// end AbstractArticleSummaryBlock
