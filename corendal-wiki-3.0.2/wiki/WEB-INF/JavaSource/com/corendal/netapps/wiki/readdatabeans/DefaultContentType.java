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
package com.corendal.netapps.wiki.readdatabeans;

/**
 * DefaultContentType is the concrete implementation of AbstractContentType.
 * 
 * @version $Id: DefaultContentType.java,v 1.3 2006/04/01 20:48:08 tdanard Exp $
 */
public final class DefaultContentType extends AbstractContentType implements
        Cloneable {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.readdatabeans.DefaultContentType";

    /**
     * Default class constructor.
     */
    public DefaultContentType() {
        // parent class constructor is called
    }

    /**
     * Returns a clone.
     */
    @Override
    public Object clone() {
        return (DefaultContentType) super.clone();
    }

}

// end DefaultContentType
