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
package com.corendal.netapps.wiki.writestandardbeans;

/**
 * DefaultStandardContentInfo is the concrete implementation of
 * AbstractStandardContentInfo.
 * 
 * @version $Id: DefaultStandardContentInfo.java,v 1.1 2005/09/06 21:25:31
 *          tdanard Exp $
 */
public final class DefaultStandardContentInfo extends
        AbstractStandardContentInfo implements Cloneable {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.writestandardbeans.DefaultStandardContentInfo";

    /**
     * Default class constructor. This procedure is private to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead.
     */
    public DefaultStandardContentInfo() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractStandardContentInfo.clone.
     */
    @Override
    public Object clone() {
        return (DefaultStandardContentInfo) super.clone();
    }
}

// end DefaultStandardContentInfo
