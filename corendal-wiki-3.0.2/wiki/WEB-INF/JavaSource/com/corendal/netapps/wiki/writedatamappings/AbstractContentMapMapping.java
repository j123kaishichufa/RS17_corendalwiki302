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
import com.corendal.netapps.wiki.interfaces.ContentMapMapping;

/**
 * AbstractContentMapMapping is the abstract class mapping the
 * notificationAction table.
 * 
 * @version $Id: AbstractContentMapMapping.java,v 1.1 2005/09/09 17:08:40
 *          tdanard Exp $
 */
public abstract class AbstractContentMapMapping extends
        AbstractMappingWithAlphanumericSingleId implements ContentMapMapping {
    /** Parent content id. */
    private String parentContentId;

    /** Child content id. */
    private String childContentId;

    /** Main flag. */
    private String mainFlag;

    private String requestId;

    /** Order ord */
    private int order;

    /** Enabled flag. */
    private String enabledFlag;

    /**
     * Default class constructor.
     */
    public AbstractContentMapMapping() {
        // parent constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractMappingWithAlphanumericSingleId.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentMapMapping) super.clone();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapMapping#getParentContentId()
     */
    public String getParentContentId() {
        return this.parentContentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapMapping#setParentContentId(java.lang.String)
     */
    public void setParentContentId(String parentContentId) {
        this.parentContentId = parentContentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapMapping#getChildContentId()
     */
    public String getChildContentId() {
        return this.childContentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapMapping#setChildContentId(java.lang.String)
     */
    public void setChildContentId(String childContentId) {
        this.childContentId = childContentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapMapping#getMainFlag()
     */
    public String getMainFlag() {
        return this.mainFlag;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapMapping#setMainFlag(java.lang.String)
     */
    public void setMainFlag(String mainFlag) {
        this.mainFlag = mainFlag;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapMapping#getRequestId()
     */
    public String getRequestId() {
        return this.requestId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapMapping#setRequestId(java.lang.String)
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapMapping#getOrder()
     */
    public int getOrder() {
        return this.order;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapMapping#setOrder(int)
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentMapMapping#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentMapMapping#setEnabledFlag(java.lang.String)
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
