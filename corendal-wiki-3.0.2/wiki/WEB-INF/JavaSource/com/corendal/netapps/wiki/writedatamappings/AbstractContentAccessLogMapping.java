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
import com.corendal.netapps.wiki.interfaces.ContentAccessLogMapping;

/**
 * AbstractContentAccessLogMapping is the abstract class mapping the
 * notificationAction table.
 * 
 * @version $Id: AbstractContentAccessLogMapping.java,v 1.1 2005/09/09 17:08:40
 *          tdanard Exp $
 */
public abstract class AbstractContentAccessLogMapping extends
        AbstractMappingWithAlphanumericSingleId implements
        ContentAccessLogMapping {
    /** Content d. */
    private String contentId;

    /** Enabled flag. */
    private String enabledFlag;

    /**
     * Default class constructor.
     */
    public AbstractContentAccessLogMapping() {
        // parent constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractMappingWithAlphanumericSingleId.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentAccessLogMapping) super.clone();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentAccessLogMapping#getContentId()
     */
    public String getContentId() {
        return this.contentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentAccessLogMapping#setContentId(java.lang.String)
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentAccessLogMapping#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentAccessLogMapping#setEnabledFlag(java.lang.String)
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
