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

import java.util.Date;

import com.corendal.netapps.framework.modelaccess.datamappings.AbstractMappingWithAlphanumericSingleId;
import com.corendal.netapps.wiki.interfaces.ContentAccessPeriodMapping;

/**
 * AbstractContentAccessPeriodMapping is the abstract class mapping the
 * notificationAction table.
 * 
 * @version $Id: AbstractContentAccessPeriodMapping.java,v 1.1 2005/09/09
 *          17:08:40 tdanard Exp $
 */
public abstract class AbstractContentAccessPeriodMapping extends
        AbstractMappingWithAlphanumericSingleId implements
        ContentAccessPeriodMapping {
    /** Content id. */
    private String contentId;

    /** Access date. */
    private Date accessDate;

    /** Access number. */
    private int accessNumber;

    /** Access rank. */
    private int accessRank;

    /** Enabled flag. */
    private String enabledFlag;

    /**
     * Default class constructor.
     */
    public AbstractContentAccessPeriodMapping() {
        // parent constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractMappingWithAlphanumericSingleId.clone.
     */
    @Override
    public Object clone() {
        return (AbstractContentAccessPeriodMapping) super.clone();
    }

    /*
     * @see com.corendal.netapps.docside.interfaces.ContentAccessPeriodMapping#getContentId()
     */
    public String getContentId() {
        return this.contentId;
    }

    /*
     * @see com.corendal.netapps.docside.interfaces.ContentAccessPeriodMapping#setContentId(java.lang.String)
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    /*
     * @see com.corendal.netapps.docside.interfaces.ContentAccessPeriodMapping#getAccessDate()
     */
    public Date getAccessDate() {
        if (this.accessDate != null) {
            return (Date) this.accessDate.clone();
        } else {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.docside.interfaces.ContentAccessPeriodMapping#setAccessDate(java.util.Date)
     */
    public void setAccessDate(Date accessDate) {
        if (accessDate != null) {
            this.accessDate = (Date) accessDate.clone();
        } else {
            this.accessDate = null;
        }
    }

    /*
     * @see com.corendal.netapps.docside.interfaces.ContentAccessPeriodMapping#getAccessNumber()
     */
    public int getAccessNumber() {
        return this.accessNumber;
    }

    /*
     * @see com.corendal.netapps.docside.interfaces.ContentAccessPeriodMapping#setAccessNumber(int)
     */
    public void setAccessNumber(int accessNumber) {
        this.accessNumber = accessNumber;
    }

    /*
     * @see com.corendal.netapps.docside.interfaces.ContentAccessPeriodMapping#getAccessRank()
     */
    public int getAccessRank() {
        return this.accessRank;
    }

    /*
     * @see com.corendal.netapps.docside.interfaces.ContentAccessPeriodMapping#setAccessRank(java.lang.String)
     */
    public void setAccessRank(int accessRank) {
        this.accessRank = accessRank;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentAccessPeriodMapping#getEnabledFlag()
     */
    public String getEnabledFlag() {
        return this.enabledFlag;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.ContentAccessPeriodMapping#setEnabledFlag(java.lang.String)
     */
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
