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

import com.corendal.netapps.framework.modelaccess.datamappings.AbstractMappingWithAlphanumericSingleId;
import com.corendal.netapps.wiki.interfaces.ContentAddressMapping;

/**
 * AbstractContentAddressMapping is the abstract class mapping the
 * notificationAction table.
 * 
 * @version $Id: AbstractContentAddressMapping.java,v 1.1 2005/09/09 17:08:40
 *          tdanard Exp $
 */
public abstract class AbstractContentAddressMapping extends
        AbstractMappingWithAlphanumericSingleId implements
        ContentAddressMapping {
    /** Content version id. */
    private String contentVersionId;

    /** Address. */
    private String address;

    /** Enabled flag. */
    private String enabledFlag;

    /**
     * Default class constructor.
     */
    public AbstractContentAddressMapping() {
        // parent constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractMappingWithAlphanumericSingleId.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentAddressMapping) super.clone();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentAddressMapping#getContentVersionId()
     */
    public String getContentVersionId() {
        return this.contentVersionId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentAddressMapping#setContentVersionId(java.lang.String)
     */
    public void setContentVersionId(String contentVersionId) {
        this.contentVersionId = contentVersionId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentAddressMapping#getAddress()
     */
    public String getAddress() {
        return this.address;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentAddressMapping#setAddress(java.lang.String)
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentAddressMapping#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentAddressMapping#setEnabledFlag(java.lang.String)
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
