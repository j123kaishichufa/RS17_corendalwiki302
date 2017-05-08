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
package com.corendal.netapps.wiki.readstandardfactories;

import java.util.ArrayList;
import java.util.List;

import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.standardfactories.AbstractStandardReadBeanFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.wiki.interfaces.StandardContentRuleType;
import com.corendal.netapps.wiki.interfaces.StandardContentRuleTypeFactory;
import com.corendal.netapps.wiki.readdatabeans.AbstractContentRuleType;
import com.corendal.netapps.wiki.readstandardbeans.DefaultStandardContentRuleType;

/**
 * AbstractStandardContentRuleTypeFactory is the abstract class building new
 * StandardContentRuleType instances.
 * 
 * @version $Id: AbstractStandardContentRuleTypeFactory.java,v 1.1 2005/09/06
 *          21:25:30 tdanard Exp $
 */
public abstract class AbstractStandardContentRuleTypeFactory extends
        AbstractStandardReadBeanFactory implements Cloneable,
        StandardContentRuleTypeFactory {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentRuleTypeFactory() {
        super(DefaultStandardContentRuleType.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardReadBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentRuleTypeFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(
                DefaultStandardContentRuleType.class.getName())) {
            return new DefaultStandardContentRuleType();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#findPrimaryKeysByEnabledFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<PrimaryKey> findPrimaryKeysByEnabledFlag(String enabledFlag) {
        return AbstractContentRuleType.findByEnabledFlag(enabledFlag);
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardPatchSetFactory#findByEnabledFlag(java.lang.String,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public List<StandardReadBean> findByEnabledFlag(String enabledFlag) {
        List<PrimaryKey> primaryKeys = AbstractContentRuleType
                .findByEnabledFlag(enabledFlag);
        List<StandardReadBean> result = new ArrayList<StandardReadBean>();

        for (PrimaryKey pk : primaryKeys) {
            StandardContentRuleType si = (StandardContentRuleType) this
                    .findByPrimaryKey(pk);
            result.add(si);
        }

        return result;
    }
}

// end AbstractStandardContentRuleTypeFactory
