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
import com.corendal.netapps.framework.core.utils.LessUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.ContentInfo;
import com.corendal.netapps.wiki.interfaces.StandardContentInfo;
import com.corendal.netapps.wiki.interfaces.StandardContentInfoFactory;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentInfo;
import com.corendal.netapps.wiki.writedatafactories.ContentInfoFactory;
import com.corendal.netapps.wiki.writestandardbeans.DefaultStandardContentInfo;

/**
 * AbstractStandardContentInfoFactory is the abstract class building new
 * StandardContentInfo instances.
 * 
 * @version $Id: AbstractStandardContentInfoFactory.java,v 1.1 2005/09/06
 *          21:25:35 tdanard Exp $
 */
public abstract class AbstractStandardContentInfoFactory extends
        AbstractStandardWriteBeanFactory implements Cloneable,
        StandardContentInfoFactory {
    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentInfoFactory() {
        super(DefaultStandardContentInfo.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardWriteBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentInfoFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(
                DefaultStandardContentInfo.class.getName())) {
            return new DefaultStandardContentInfo();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentInfoFactory#create(java.lang.String,
     *      java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public StandardBean create(String name, String description) {
        /*
         * remove any dot at the start or end of the strings
         */
        String modifiedName = LessUtil.getDotLess(name);
        String modifiedDescription = LessUtil.getDotLess(description);

        ContentInfo contentInfo = ContentInfoFactory.create(modifiedName,
                modifiedDescription);
        StandardContentInfo result = null;

        if ((contentInfo != null) && (contentInfo.getIsDone())) {
            result = (StandardContentInfo) this.findByPrimaryKey(contentInfo
                    .getPrimaryKey());
            result.setIsDone(contentInfo.getIsDone());
            result.setStoreTrace(contentInfo.getStoreTrace());
        } else if (contentInfo != null) {
            throw new CannotDoBeanException(contentInfo.getStoreTrace());
        } else {
            throw new CannotDoBeanException();
        }

        /*
         * return
         */
        return result;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#findPrimaryKeysByEnabledFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<PrimaryKey> findPrimaryKeysByEnabledFlag(String enabledFlag) {
        return AbstractContentInfo.findByEnabledFlag(enabledFlag);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardPatchSetFactory#findByEnabledFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<StandardReadBean> findByEnabledFlag(String enabledFlag) {
        List<PrimaryKey> primaryKeys = AbstractContentInfo
                .findByEnabledFlag(enabledFlag);
        List<StandardReadBean> result = new ArrayList<StandardReadBean>();

        for (PrimaryKey pk : primaryKeys) {
            StandardContentInfo si = (StandardContentInfo) this
                    .findByPrimaryKey(pk);
            result.add(si);
        }

        return result;
    }
}

// end AbstractStandardContentInfoFactory
