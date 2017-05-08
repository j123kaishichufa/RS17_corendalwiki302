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

import com.corendal.netapps.framework.core.interfaces.StandardLargeFirstBlocksPage;
import com.corendal.netapps.framework.core.pages.AbstractStandardLargeFirstBlocksPage;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.utils.ArticleUtil;

/**
 * AbstractOnlineHelpArticleStandardLargeFirstBlocksPage is the abstract class
 * handling information about each page made of blocks of the application. Large
 * blocks are printed first, small blocks are printed last.
 * 
 * @version $Id: AbstractOnlineHelpArticleStandardLargeFirstBlocksPage.java,v
 *          1.1 2005/09/06 21:25:33 tdanard Exp $
 */
public abstract class AbstractOnlineHelpArticleStandardLargeFirstBlocksPage
        extends AbstractStandardLargeFirstBlocksPage implements Cloneable,
        StandardLargeFirstBlocksPage {
    /** Primary key of the requested article */
    private PrimaryKey requestedArticlePk;

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractOnlineHelpArticleStandardLargeFirstBlocksPage() {
        this.requestedArticlePk = ArticleUtil
                .getRequestedOnlineHelpArticlePrimaryKey();
    }

    /**
     * Returns a clone. Overrides AbstractStandardLargeFirstBlocksPage.clone.
     */
    @Override
    public Object clone() {
        AbstractOnlineHelpArticleStandardLargeFirstBlocksPage result = (AbstractOnlineHelpArticleStandardLargeFirstBlocksPage) super
                .clone();

        if (this.requestedArticlePk != null) {
            result.requestedArticlePk = (PrimaryKey) this.requestedArticlePk
                    .clone();
        }

        return result;
    }
}

// end AbstractOnlineHelpArticleStandardLargeFirstBlocksPage
