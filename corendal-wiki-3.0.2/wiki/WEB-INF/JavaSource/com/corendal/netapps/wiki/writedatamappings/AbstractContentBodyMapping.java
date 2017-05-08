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
import com.corendal.netapps.wiki.interfaces.ContentBodyMapping;

/**
 * AbstractContentBodyMapping is the abstract class mapping the
 * notificationAction table.
 * 
 * @version $Id: AbstractContentBodyMapping.java,v 1.1 2005/09/09 17:08:40
 *          tdanard Exp $
 */
public abstract class AbstractContentBodyMapping extends
        AbstractMappingWithAlphanumericSingleId implements ContentBodyMapping {
    /** Content version id. */
    private String contentVersionId;

    /** Body text. */
    private String bodyText;

    /** Body html. */
    private String bodyHtml;

    /** Enabled flag. */
    private String enabledFlag;

    /**
     * Default class constructor.
     */
    public AbstractContentBodyMapping() {
        // parent constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractMappingWithAlphanumericSingleId.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentBodyMapping) super.clone();
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentBodyMapping#getContentVersionId()
     */
    public String getContentVersionId() {
        return this.contentVersionId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentBodyMapping#setContentVersionId(java.lang.String)
     */
    public void setContentVersionId(String contentVersionId) {
        this.contentVersionId = contentVersionId;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentBodyMapping#getBodyText()
     */
    public String getBodyText() {
        return this.bodyText;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentBodyMapping#setBodyText(java.lang.String)
     */
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentBodyMapping#getBodyHtml()
     */
    public String getBodyHtml() {
        return this.bodyHtml;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentBodyMapping#setBodyHtml(java.lang.String)
     */
    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentBodyMapping#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentBodyMapping#setEnabledFlag(java.lang.String)
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
