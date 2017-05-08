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
import com.corendal.netapps.wiki.interfaces.ContentMapping;

/**
 * AbstractContentMapping is the abstract class mapping the notificationAction
 * table.
 * 
 * @version $Id: AbstractContentMapping.java,v 1.1 2005/09/09 17:08:40 tdanard
 *          Exp $
 */
public abstract class AbstractContentMapping extends
        AbstractMappingWithAlphanumericSingleId implements ContentMapping {
    /** Content type id. */
    private String contentTypeId;

    /** Content class type id. */
    private String contentClassTypeId;

    /** Whats new flag. */
    private String whatsNewFlag;

    /** Content rule type id. */
    private String contentRuleTypeId;

    /** Order ord */
    private int order;

    /** Enabled flag. */
    private String enabledFlag;

    /**
     * Default class constructor.
     */
    public AbstractContentMapping() {
        // parent constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractMappingWithAlphanumericSingleId.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentMapping) super.clone();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapping#getContentTypeId()
     */
    public String getContentTypeId() {
        return this.contentTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapping#setContentTypeId(java.lang.String)
     */
    public void setContentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapping#getContentClassTypeId()
     */
    public String getContentClassTypeId() {
        return this.contentClassTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapping#setContentClassTypeId(java.lang.String)
     */
    public void setContentClassTypeId(String contentClassTypeId) {
        this.contentClassTypeId = contentClassTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapping#getWhatsNewFlag()
     */
    public String getWhatsNewFlag() {
        return this.whatsNewFlag;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapping#setWhatsNewFlag(java.lang.String)
     */
    public void setWhatsNewFlag(String whatsNewFlag) {
        this.whatsNewFlag = whatsNewFlag;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapping#getContentRuleTypeId()
     */
    public String getContentRuleTypeId() {
        return this.contentRuleTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapping#setContentRuleTypeId(java.lang.String)
     */
    public void setContentRuleTypeId(String contentRuleTypeId) {
        this.contentRuleTypeId = contentRuleTypeId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapping#getOrder()
     */
    public int getOrder() {
        return this.order;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentMapping#setOrder(int)
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentMapping#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentMapping#setEnabledFlag(java.lang.String)
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
