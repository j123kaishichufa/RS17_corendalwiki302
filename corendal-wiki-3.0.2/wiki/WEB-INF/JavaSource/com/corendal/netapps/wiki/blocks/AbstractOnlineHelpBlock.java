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
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.ArticleWriter;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.utils.ArticleUtil;
import com.corendal.netapps.wiki.writers.DefaultArticleWriter;

/**
 * AbstractOnlineHelpBlock is the class describing and printing the online help.
 * 
 * @version $Id: AbstractOnlineHelpBlock.java,v 1.1 2005/09/06 21:25:28 tdanard
 *          Exp $
 */
public abstract class AbstractOnlineHelpBlock extends AbstractArticleReadBlock
        implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractOnlineHelpBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractArticleReadBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractOnlineHelpBlock) super.clone();
    }

    /**
     * Returns the primary key of the home article. Overrides
     * AbstractArticleViewBlock.getHomeArticlePrimaryKey.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey
     *         object
     */
    @Override
    public PrimaryKey getHomeArticlePrimaryKey() {
        /*
         * get the online help article
         */
        StandardArticle onlineHelp = ArticleUtil.getOnlineHelpStandardArticle();

        /*
         * verify that the article exists
         */
        if (onlineHelp.getIsFound()) {
            return onlineHelp.getPrimaryKey();
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#getRecordPrimaryKey(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public PrimaryKey getRecordPrimaryKey() {
        PrimaryKey pk = super.getRecordPrimaryKey();

        if (pk == null) {
            return this.getHomeArticlePrimaryKey();
        } else {
            return pk;
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#printBodyWithoutAuthenticationCheck(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void printBodyWithoutAuthenticationCheck() {
        /*
         * get the managers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        ArticleWriter aw = (ArticleWriter) pws
                .getWriter(DefaultArticleWriter.class);

        /*
         * get the article
         */
        StandardArticle article = this.getViewedArticle();

        /*
         * print the body
         */
        aw.printBodyHTMLWithoutName(article, this.getHomeArticlePrimaryKey(),
                this, true);
    }
}

// end AbstractOnlineHelpBlock
