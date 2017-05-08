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

import com.corendal.netapps.wiki.utils.ArticleUtil;

/**
 * AbstractContentsAdvancedSearchResultsBlock is the class describing and
 * printing all contents of an article.
 * 
 * @version $Id: AbstractContentsAdvancedSearchResultsBlock.java,v 1.1
 *          2005/09/06 21:25:28 tdanard Exp $
 */
public abstract class AbstractContentsAdvancedSearchResultsBlock extends
        AbstractGenericSearchResultsBlock implements Cloneable {
    /**
     * Default class constructor. This procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    protected AbstractContentsAdvancedSearchResultsBlock() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractGenericSearchResultsBlock.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentsAdvancedSearchResultsBlock) super.clone();
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

        /*
         * sort the rows by descending score
         */
        this.sortResultsRows();
        this.reverseResultsRows();
    }

    /**
     * Returns the name to print in the HTML format.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    @Override
    protected String getCustomizedNameHTML(String customizedNameText) {
        return ArticleUtil
                .getCustomizedNameOrDescriptionHTML(customizedNameText);
    }

    /**
     * Returns the description to print in the HTML format.
     * 
     * 
     * 
     * @return a java.lang.String object
     */
    @Override
    protected String getCustomizedDescriptionHTML(
            String customizedDescriptionText) {
        return ArticleUtil
                .getCustomizedNameOrDescriptionHTML(customizedDescriptionText);
    }
}

// end AbstractContentsAdvancedSearchResultsBlock
