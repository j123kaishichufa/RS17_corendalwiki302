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

import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.standardfactories.AbstractStandardReadBeanFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.StandardContentAccessDay;
import com.corendal.netapps.wiki.interfaces.StandardContentAccessDayFactory;
import com.corendal.netapps.wiki.writedatabeans.AbstractContentAccessDay;
import com.corendal.netapps.wiki.writestandardbeans.DefaultStandardContentAccessDay;

/**
 * AbstractStandardContentAccessDayFactory is the abstract class building new
 * StandardContentAccessDay instances.
 * 
 * @version $Id: AbstractStandardContentAccessDayFactory.java,v 1.1 2005/09/06
 *          21:25:35 tdanard Exp $
 */
public abstract class AbstractStandardContentAccessDayFactory extends
        AbstractStandardReadBeanFactory implements Cloneable,
        StandardContentAccessDayFactory {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentAccessDayFactory() {
        super(DefaultStandardContentAccessDay.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardReadBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentAccessDayFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(
                DefaultStandardContentAccessDay.class.getName())) {
            return new DefaultStandardContentAccessDay();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.StandardContentAccessDayFactory#findByContentPrimaryKey(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public List<StandardContentAccessDay> findByContentPrimaryKey(PrimaryKey pk) {
        List<StandardContentAccessDay> standardContentAccessDays = new ArrayList<StandardContentAccessDay>();
        List<PrimaryKey> primaryKeys = AbstractContentAccessDay
                .findByContentPrimaryKey(pk);

        for (PrimaryKey sdPk : primaryKeys) {
            StandardContentAccessDay sd = (StandardContentAccessDay) this
                    .findByPrimaryKey(sdPk);
            standardContentAccessDays.add(sd);
        }

        return standardContentAccessDays;
    }
}

// end AbstractStandardContentAccessDayFactory
