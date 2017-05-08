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

import java.util.List;

import com.corendal.netapps.framework.core.interfaces.HistoryManager;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: ContentHistoryManager.java,v 1.1 2005/09/06 21:25:29 tdanard
 *          Exp $
 */
public interface ContentHistoryManager extends HistoryManager {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ContentHistoryManager";

    /**
     * Returns the list of content primary keys with only article primary keys
     * included.
     * 
     * 
     * 
     */
    public List<PrimaryKey> getHistoryWithArticlesOnly();

    /**
     * Returns the list of content primary keys with only article primary keys
     * included. an article is added on top.
     * 
     * 
     * 
     */
    public List<PrimaryKey> getToppedHistoryWithArticlesOnly(
            PrimaryKey topArticlePk);

    /**
     * Returns the list of content primary keys without the contents of a given
     * article, or any recursive parent article.
     * 
     * 
     * 
     */
    public List<PrimaryKey> getCleanHistory(PrimaryKey articlePk);
}

// end ContentHistoryManager
