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
package com.corendal.netapps.wiki.interfaces;

import com.corendal.netapps.framework.core.interfaces.StandardBlock;
import com.corendal.netapps.framework.core.interfaces.Writer;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: ArticleVersionWriter.java,v 1.7 2007/09/02 23:10:14 tdanard Exp $
 */
public interface ArticleVersionWriter extends Writer {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ArticleVersionWriter";

    /**
     * Prints the path of an article in the HTML format.
     * 
     * @param sd
     *            a StandardContent object
     * 
     * 
     */
    public void printPathHTML(StandardArticleVersion sd);

    /**
     * Prints the body of a block.
     * 
     * 
     * 
     */
    public void printBodyHTML(StandardArticleVersion sd,
            PrimaryKey homeArticlePk, StandardBlock sb, boolean isOnlineHelp);
}

// end ArticleVersionWriter
