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

import com.corendal.netapps.framework.modelaccess.globals.DataSessionSetGlobal;
import com.corendal.netapps.framework.modelaccess.interfaces.DataSessionSet;
import com.corendal.netapps.framework.runtime.batches.AbstractDatabaseExecutableBatch;
import com.corendal.netapps.wiki.writedatabeans.AbstractArticle;
import com.corendal.netapps.wiki.writedatabeans.AbstractArticleVersion;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentAccessDay;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentAccessLog;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentAccessPeriod;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentInfo;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentRequest;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentSubscription;
import com.corendal.netapps.wiki.writedatabeans.AbstractImage;
import com.corendal.netapps.wiki.writedatabeans.AbstractImageVersion;

/**
 * PurgeWriteDataBeanBatch is the class purging duplicate entry logs.
 * 
 * @version $Id: PurgeWriteDataBeanBatch.java,v 1.1 2005/09/06 21:25:35 tdanard
 *          Exp $
 */
public class PurgeWriteDataBeanBatch extends AbstractDatabaseExecutableBatch
        implements Cloneable {
    /** Version uid for serialization. */
    private static final long serialVersionUID = 1L;

    /** Fully qualified name of this class. */
    public static final String DEFAULT_CLASS_NAME = "com.corendal.netapps.wiki.batches.PurgeWriteDataBeanBatch";

    /**
     * Default class constructor.
     */
    public PurgeWriteDataBeanBatch() {
        // parent class constructor is called
    }

    /**
     * Returns a clone. Overrides AbstractDatabaseExecutableBatch.clone.
     */
    @Override
    public Object clone() {
        return (PurgeWriteDataBeanBatch) super.clone();
    }

    /**
     * Execution method. Implements abstract method
     * AbstractDatabaseExecutableBatch.answerExecution.
     */
    @Override
    public void answerExecution() {
        /*
         * get the data session set
         */
        DataSessionSet dss = DataSessionSetGlobal.get();

        /*
         * purge all data
         */
        AbstractArticle.cleanup();
        dss.commitAndStartTransaction();
        AbstractImage.cleanup();
        dss.commitAndStartTransaction();
        AbstractArticle.cleanup();
        dss.commitAndStartTransaction();
        AbstractArticleVersion.cleanup();
        dss.commitAndStartTransaction();
        AbstractImageVersion.cleanup();
        dss.commitAndStartTransaction();
        AbstractContentRequest.cleanup();
        dss.commitAndStartTransaction();
        AbstractContentInfo.cleanup();
        dss.commitAndStartTransaction();
        AbstractContentSubscription.cleanup();
        dss.commitAndStartTransaction();
        AbstractContentAccessLog.cleanup();
        dss.commitAndStartTransaction();
        AbstractContentAccessDay.cleanup();
        dss.commitAndStartTransaction();
        AbstractContentAccessPeriod.cleanup();
    }
}
