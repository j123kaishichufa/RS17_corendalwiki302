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
package com.corendal.netapps.wiki.batches;

import com.corendal.netapps.framework.runtime.batches.AbstractDatabaseExecutableBatch;
import com.corendal.netapps.wiki.writedatafactories.ContentAccessDayFactory;
import com.corendal.netapps.wiki.writedatafactories.ContentAccessPeriodFactory;

/**
 * ContentStatisticsBatch is the class updating content statistics.
 * 
 * @version $Id: ContentStatisticsBatch.java,v 1.1 2005/09/06 21:25:35 tdanard
 *          Exp $
 */
public class ContentStatisticsBatch extends AbstractDatabaseExecutableBatch
        implements Cloneable {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.batches.ContentStatisticsBatch";

    /**
     * Default class constructor.
     */
    public ContentStatisticsBatch() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractDatabaseExecutableBatch.clone.
     */
    @Override
    public Object clone() {
        return (ContentStatisticsBatch) super.clone();
    }

    /**
     * Execution method. Implements abstract method
     * AbstractDatabaseExecutableBatch.answerExecution.
     */
    @Override
    public void answerExecution() {
        /*
         * execute the refresh
         */
        ContentAccessDayFactory.refresh();
        ContentAccessPeriodFactory.refresh();
    }
}
