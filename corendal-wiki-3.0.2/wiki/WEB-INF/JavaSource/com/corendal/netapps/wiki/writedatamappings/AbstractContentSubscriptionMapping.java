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
import com.corendal.netapps.wiki.interfaces.ContentSubscriptionMapping;

/**
 * AbstractContentSubscriptionMapping is the abstract class mapping the
 * notificationAction table.
 * 
 * @version $Id: AbstractContentSubscriptionMapping.java,v 1.1 2005/09/09
 *          17:08:40 tdanard Exp $
 */
public abstract class AbstractContentSubscriptionMapping extends
        AbstractMappingWithAlphanumericSingleId implements
        ContentSubscriptionMapping {
    /** Content id. */
    private String contentId;

    /** Content subscription type id. */
    private String contentSubscriptionTypeId;

    /** Content subscription mode id. */
    private String contentSubscriptionModeId;

    /** Order ord */
    private int order;

    /** Enabled flag. */
    private String enabledFlag;

    /**
     * Default class constructor.
     */
    public AbstractContentSubscriptionMapping() {
        // parent constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractMappingWithAlphanumericSingleId.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentSubscriptionMapping) super.clone();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMapping#getContentId()
     */
    public String getContentId() {
        return this.contentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMapping#setContentId(java.lang.String)
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMapping#getContentSubscriptionTypeId()
     */
    public String getContentSubscriptionTypeId() {
        return this.contentSubscriptionTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMapping#setContentSubscriptionTypeId(java.lang.String)
     */
    public void setContentSubscriptionTypeId(String contentSubscriptionTypeId) {
        this.contentSubscriptionTypeId = contentSubscriptionTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMapping#getContentSubscriptionModeId()
     */
    public String getContentSubscriptionModeId() {
        return this.contentSubscriptionModeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMapping#setContentSubscriptionModeId(java.lang.String)
     */
    public void setContentSubscriptionModeId(String contentSubscriptionModeId) {
        this.contentSubscriptionModeId = contentSubscriptionModeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMapping#getOrder()
     */
    public int getOrder() {
        return this.order;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentSubscriptionMapping#setOrder(int)
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentSubscriptionMapping#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentSubscriptionMapping#setEnabledFlag(java.lang.String)
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
