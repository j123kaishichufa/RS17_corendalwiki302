/*   
 * Corendal NetApps Framework - Java framework for web applications   
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

import java.io.Serializable;
import java.util.List;

import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;

/**
 * @version $Id: ArticleBodyHighlighter.java,v 1.3 2006/04/01 19:37:33 tdanard
 *          Exp $
 */
public interface ArticleBodyHighlighter extends Serializable {
    /** Fully qualified name of this interface. */
    public static final String INTERFACE_NAME = "com.corendal.netapps.wiki.interfaces.ArticleBodyHighlighter";

    public Object clone();

    /**
     * Returns the resulting highlighted body.
     */
    public String getModifiedBodyHTML();

    /**
     * Returns the list of original locations found.
     */
    public List<String> getOriginalLocations();

    /**
     * Returns the list of modified locations included.
     */
    public List<String> getModifiedLocations();

    /**
     * Converts a body to highlight hypertext references.
     * 
     * @param parentArticlePk
     *            primary key of a parent article
     * @param bodyHTML
     *            body in the HTML format to convert
     * @param homeArticlePk
     *            primary key of the home article
     * @param isOnlineHelp
     *            indicates whether the body is part of the online help
     * 
     * 
     */
    public void process(PrimaryKey parentArticlePk, String bodyHTML,
            PrimaryKey homeArticlePk, boolean isOnlineHelp);

}

// end ArticleBodyHighlighter
