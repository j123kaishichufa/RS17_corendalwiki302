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
package com.corendal.netapps.wiki.commons;

/**
 * DefaultArticleBodyHighlighter is concrete implementation of
 * AbstractArticleBodyHighlighter.
 * 
 * @version $Id: DefaultArticleBodyHighlighter.java,v 1.3 2006/04/01 19:37:43
 *          tdanard Exp $
 */
public final class DefaultArticleBodyHighlighter extends
        AbstractArticleBodyHighlighter implements Cloneable {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.commons.DefaultArticleBodyHighlighter";

    /**
     * Class constructor.
     */
    public DefaultArticleBodyHighlighter() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractArticleBodyHighlighter.clone.
     */
    @Override
    public Object clone() {
        return (DefaultArticleBodyHighlighter) super.clone();
    }
}

// end DefaultArticleBodyHighlighter
