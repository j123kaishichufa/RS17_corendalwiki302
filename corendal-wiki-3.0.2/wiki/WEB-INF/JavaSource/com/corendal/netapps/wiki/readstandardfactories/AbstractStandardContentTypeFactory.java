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

import com.corendal.netapps.framework.core.interfaces.StandardReadBean;
import com.corendal.netapps.framework.core.standardfactories.AbstractStandardReadBeanFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentTypeFactory;
import com.corendal.netapps.wiki.readstandardbeans.DefaultStandardContentType;

/**
 * AbstractStandardContentTypeFactory is the abstract class building new
 * StandardContentType instances.
 * 
 * @version $Id: AbstractStandardContentTypeFactory.java,v 1.1 2005/09/06
 *          21:25:30 tdanard Exp $
 */
public abstract class AbstractStandardContentTypeFactory extends
        AbstractStandardReadBeanFactory implements Cloneable,
        StandardContentTypeFactory {

    /**
     * Class constructor.
     * 
     * 
     * 
     */
    protected AbstractStandardContentTypeFactory() {
        super(DefaultStandardContentType.class, true);
    }

    /**
     * Returns a clone. Overrides AbstractStandardReadBeanFactory.clone.
     */
    @Override
    public Object clone() {
        return (AbstractStandardContentTypeFactory) super.clone();
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardReadBeanFactory#getStandardReadBeanNewInstance(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public StandardReadBean getStandardReadBeanNewInstance() {
        if (this.getClassName().equals(
                DefaultStandardContentType.class.getName())) {
            return new DefaultStandardContentType();
        } else {
            return super.getStandardReadBeanNewInstance();
        }
    }
}

// end AbstractStandardContentTypeFactory
