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

import com.corendal.netapps.framework.core.interfaces.Described;
import com.corendal.netapps.framework.core.interfaces.DescribedLong;
import com.corendal.netapps.framework.core.interfaces.DescribedShort;
import com.corendal.netapps.framework.core.interfaces.Located;
import com.corendal.netapps.framework.core.interfaces.Matched;
import com.corendal.netapps.framework.core.interfaces.Named;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardWriteBean;
import com.corendal.netapps.framework.modelaccess.interfaces.NotCached;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: StandardArticleVersion.java,v 1.1 2005/09/06 21:25:29 tdanard
 *          Exp $
 */
public interface StandardArticleVersion extends ArticleVersion,
        StandardWriteBean, Located, Named, Described, DescribedShort,
        DescribedLong, Matched, NotCached {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.StandardArticleVersion";

    /**
     * Removes this article version.
     * 
     * 
     * 
     */
    public void remove();

    /**
     * Returns the account being author of this article version.
     * 
     * 
     * 
     * @return a com.corendal.netapps.framework.core.interfaces.StandardAccount
     *         object
     */
    public StandardAccount getAuthorStandardAccount();

    /**
     * Returns the article associated with this article version.
     * 
     * 
     * 
     */
    public StandardArticle getStandardArticle();

    /**
     * Returns the latest version of the body of this article version.
     */
    public String getBodyHTML(boolean isOnlineHelp, PrimaryKey homeArticlePk);
}

// end StandardArticleVersion
