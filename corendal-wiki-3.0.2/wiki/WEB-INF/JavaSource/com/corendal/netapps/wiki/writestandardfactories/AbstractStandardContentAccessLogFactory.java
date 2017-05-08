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
package com.corendal.netapps.wiki.writestandardfactories;

import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.core.interfaces.StandardBean;
import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.standardfactories.AbstractStandardWriteBeanFactory;
import com.corendal.netapps.framework.core.throwables.CannotDoBeanException;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.ContentAccessLog;
import com.corendal.netapps.wiki.interfaces.StandardContentAccessLog;
import com.corendal.netapps.wiki.interfaces.StandardContentAccessLogFactory;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentAccessLog;
import com.corendal.netapps.wiki.writedatafactories.ContentAccessLogFactory;
import com.corendal.netapps.wiki.writestandardbeans.DefaultStandardContentAccessLog;

/**
 * AbstractStandardContentAccessLogFactory is the abstract class building new
 * StandardContentAccessLog instances.
 * 
 * @version $Id: AbstractStandardContentAccessLogFactory.java,v 1.1 2005/09/06
 *          21:25:35 tdanard Exp $
 */
public abstract class AbstractStandardContentAccessLogFactory extends
        AbstractStandardWriteBeanFactory implements Cloneable,
        StandardContentAccessLogFactory {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentAccessLogFactory() {
        super(DefaultStandardContentAccessLog.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardWriteBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentAccessLogFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(
                DefaultStandardContentAccessLog.class.getName())) {
            return new DefaultStandardContentAccessLog();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentAccessLogFactory#create(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean create(PrimaryKey contentPk) {
        /*
         * initialize the result and get the data session set
         */
        StandardContentAccessLog result = null;
        /*
         * create the link
         */
        ContentAccessLog contentAccessLog = ContentAccessLogFactory
                .create(contentPk);

        if ((contentAccessLog != null) && (contentAccessLog.getIsDone())) {
            result = (StandardContentAccessLog) this
                    .findByPrimaryKey(contentAccessLog.getPrimaryKey());
            result.setIsDone(contentAccessLog.getIsDone());
            result.setStoreTrace(contentAccessLog.getStoreTrace());
        } else if (contentAccessLog != null) {
            throw new CannotDoBeanException(contentAccessLog.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentAccessLogFactory#findByContentAndAccountPrimaryKeys(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentAccessLog> findByContentAndAccountPrimaryKeys(
            PrimaryKey pk, PrimaryKey accountPk) {
        List<StandardContentAccessLog> standardContentAccessLogs = new ArrayList<StandardContentAccessLog>();
        List<PrimaryKey> primaryKeys = AbstractContentAccessLog
                .findByContentAndAccountPrimaryKeys(pk, accountPk);

        for (PrimaryKey sdPk : primaryKeys) {
            StandardContentAccessLog sd = (StandardContentAccessLog) this
                    .findByPrimaryKey(sdPk);
            standardContentAccessLogs.add(sd);
        }

        return standardContentAccessLogs;
    }
}

// end AbstractStandardContentAccessLogFactory
