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
package com.corendal.netapps.wiki.writedatamappings;

/**
 * DefaultContentAddressMapping is the concrete implementation of
 * AbstractContentAddressMapping.
 * 
 * @version $Id: DefaultContentAddressMapping.java,v 1.1 2005/09/09 17:08:40
 *          tdanard Exp $
 */
public class DefaultContentAddressMapping extends AbstractContentAddressMapping {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.writedatamappings.DefaultContentAddressMapping";

    /**
     * Default class constructor.
     */
    public DefaultContentAddressMapping() {
        // parent constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractContentAddressMapping.clone.
     */
    @Override
    public Object clone() {
        return (DefaultContentAddressMapping) super.clone();
    }
}
